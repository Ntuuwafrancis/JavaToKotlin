package com.sriyank.javatokotlindemo.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.sriyank.javatokotlindemo.app.AppExecutors

class RepoRepository(application: Application) {
    private val mRepoDao: RepoDao
    @JvmField
    var getAllRepos: LiveData<List<Repo>>
    fun insert(repo: Repo?) {
        AppExecutors.getInstance().diskIO().execute { mRepoDao.insert(repo!!) }
    }

    fun insertAllRepos(repoList: List<Repo?>?) {
        AppExecutors.getInstance().diskIO().execute { mRepoDao.insertAllRepos(repoList) }
    }

    fun update(repo: Repo?) {
        AppExecutors.getInstance().diskIO().execute { mRepoDao.update(repo!!) }
    }

    fun delete(repo: Repo?) {
        AppExecutors.getInstance().diskIO().execute { mRepoDao.delete(repo!!) }
    }

    fun deleteAllRepos() {
        AppExecutors.getInstance().diskIO().execute { mRepoDao.deleteAll() }
    }

       init {
        val db = RepoDatabase.getRepoDatabase(application)
        mRepoDao = db.mRepoDao()
        getAllRepos = mRepoDao.allRepos

    }
}