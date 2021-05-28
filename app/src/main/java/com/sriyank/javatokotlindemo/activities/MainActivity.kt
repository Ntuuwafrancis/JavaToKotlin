package com.sriyank.javatokotlindemo.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.sriyank.javatokotlindemo.app.Constants
import com.sriyank.javatokotlindemo.databinding.ActivityMainBinding
import com.sriyank.javatokotlindemo.extensions.isNotEmpty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
    **Companion object can only be present within a particular class
     */
    companion object {
        private val TAG : String = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

    }

    /** Save app username in SharedPreferences  */
    fun saveName(@Nullable view: View) {

        if (binding.etName.isNotEmpty(binding.inputLayoutName)) {
            val personName = binding.etName.text.toString()
            val sp = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(Constants.KEY_PERSON_NAME, personName)
            editor.apply()
        }
    }

    /** Search repositories on github  */
    fun listRepositories(@Nullable view: View) {

        if (binding.etRepoName.isNotEmpty(binding.inputLayoutRepoName)) {
            val queryRepo = binding.etRepoName.text.toString()
            val repoLanguage = binding.etLanguage.text.toString()

            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_REPO)
            intent.putExtra(Constants.KEY_REPO_SEARCH, queryRepo)
            intent.putExtra(Constants.KEY_LANGUAGE, repoLanguage)
            startActivity(intent)
        }

    }

    /** Search repositories of a particular github user  */
    fun listUserRepositories(@Nullable view: View) {

        if (binding.etGithubUser.isNotEmpty(binding.inputLayoutGithubUser)) {
            val githubUser = binding.etGithubUser.text.toString()

            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_USER)
            intent.putExtra(Constants.KEY_GITHUB_USER, githubUser)
            startActivity(intent)
        }

    }

}