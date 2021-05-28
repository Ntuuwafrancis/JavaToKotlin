package com.sriyank.javatokotlindemo.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.sriyank.javatokotlindemo.R
import com.sriyank.javatokotlindemo.adapters.DisplayRepoAdapter
import com.sriyank.javatokotlindemo.app.Constants
import com.sriyank.javatokotlindemo.data.Repo
import com.sriyank.javatokotlindemo.data.ViewModelRepos
import com.sriyank.javatokotlindemo.databinding.ActivityDisplayBinding
import com.sriyank.javatokotlindemo.databinding.HeaderBinding
import com.sriyank.javatokotlindemo.extensions.showErrorMessage
import com.sriyank.javatokotlindemo.extensions.toast
import com.sriyank.javatokotlindemo.models.SearchResponse
import com.sriyank.javatokotlindemo.retrofit.GithubAPIService
import com.sriyank.javatokotlindemo.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.set

class DisplayActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationView : NavigationView
    private lateinit var repoAdapter: DisplayRepoAdapter
    private var browsedRepos: List<Repo> = mutableListOf()
    private val githubAPIService: GithubAPIService by lazy {
        RetrofitClient.githubAPIService
    }
    private lateinit var viewModelRepos: ViewModelRepos
    private lateinit var binding: ActivityDisplayBinding
    private lateinit var headerBinding: HeaderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar
        binding.apply {
            this@DisplayActivity.recyclerView = recyclerView
            this@DisplayActivity.drawerLayout = drawerLayout
            this@DisplayActivity.navigationView = navView
            toolbar = binding.toolbar
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Showing Browsed Results"

        setAppUsername()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        viewModelRepos = ViewModelProvider(this).get(ViewModelRepos::class.java)

        navigationView.setNavigationItemSelectedListener(this)

        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val intent = intent
        if (intent.getIntExtra(Constants.KEY_QUERY_TYPE, -1) == Constants.SEARCH_BY_REPO) {
            val queryRepo: String = intent.getStringExtra(Constants.KEY_REPO_SEARCH)!!
            val repoLanguage = intent.getStringExtra(Constants.KEY_LANGUAGE)!!
            fetchRepositories(queryRepo, repoLanguage)

        } else {
            val githubUser = intent.getStringExtra(Constants.KEY_GITHUB_USER)
            githubUser?.let {
                fetchUserRepositories(it)
            }

        }
    }

    private fun setAppUsername() {
        val sp = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val personName: String? = sp.getString(Constants.KEY_PERSON_NAME, "User")

        headerBinding = HeaderBinding.inflate(layoutInflater)
        navigationView.addHeaderView(headerBinding.root)
        headerBinding.txvName.text = personName
        headerBinding.txvEmail.text = "francis2wx@gmail.com"
    }

    private fun fetchUserRepositories(githubUser: String) {

        githubAPIService.searchRepositoriesByUser(githubUser).enqueue(object : Callback<List<Repo>>{

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {

                successfulResponse(call, response)

            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                toast(t.message ?: "Error Fetching Results")
            }
        })
    }

    private fun fetchRepositories(queryRepository: String, repoLanguage: String) {

        var queryRepo = queryRepository
        val query: MutableMap<String, String> = HashMap()

        if (repoLanguage.isNotEmpty())
            queryRepo += " language:$repoLanguage"
        query["q"] = queryRepo

        githubAPIService.searchRepositories(query).enqueue(object : Callback<SearchResponse> {

            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "posts loaded from API $response")

                    response.body()?.items?.let {
                        browsedRepos = it
                    }
                    if (browsedRepos.isNotEmpty())
                        setupRecyclerView(browsedRepos)
                    else
                        toast("No Items Found")
                } else {
                    Log.i(TAG, "error $response")
                    showErrorMessage( response.errorBody()!!)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                toast(t.toString(), Toast.LENGTH_LONG)
            }
        })
    }

    private fun successfulResponse (call: Call<List<Repo>>, response: Response<List<Repo>>) {
        if (response.isSuccessful) {
            Log.i(TAG, "posts loaded from API $response")

            response.body()?.let {
                browsedRepos = it
            }

            if (browsedRepos.isNotEmpty()) {
                setupRecyclerView(browsedRepos)
            } else {
                toast("No Items Found")
            }
        } else {
            Log.i(TAG, "Error $response")
            showErrorMessage( response.errorBody()!!)
        }
    }

    private fun setupRecyclerView(items: List<Repo>) {
        repoAdapter = DisplayRepoAdapter(this, viewModelRepos)
        repoAdapter.submitList(items)
        recyclerView.adapter = repoAdapter
        repoAdapter.setOnItemClickListener(object : DisplayRepoAdapter.OnItemClickListener{
            override fun onItemClick(repo: Repo) {
                this@DisplayActivity.onItemClick(repo)
            }

        })
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true

        when (menuItem.itemId) {
            R.id.item_bookmark -> {consumeMenuEvent { showBookmarks() }}
            R.id.item_browsed_results -> {consumeMenuEvent { showBrowsedResults() }}
        }
        return true
    }

    private inline fun consumeMenuEvent(myFunc: () -> Unit) {
        myFunc()
        closeDrawer()
    }

    private fun showBrowsedResults() {
        if (browsedRepos.isNotEmpty()) {
            this.setupRecyclerView(browsedRepos)
            supportActionBar!!.title = "Showing Browsed Results"
        } else {
            toast("No Browsed Results Available!")
            return
        }

    }

    private fun showBookmarks() {
        viewModelRepos.repoList.observe(this, {
            if (it!!.isNotEmpty()) {
                setupRecyclerView(it)
                supportActionBar!!.title = "Showing Bookmarks"
            } else {
                toast("No Bookmarks Available!")
                return@observe
            }

        })
    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else {
            super.onBackPressed()
        }
    }

    private fun onItemClick(repo: Repo) {
        val url = repo.htmlUrl
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(this.packageManager) != null) {
            this.startActivity(intent)
        }
    }

    companion object {
        private val TAG = DisplayActivity::class.java.simpleName
    }
}