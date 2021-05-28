package com.sriyank.javatokotlindemo.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ViewModelRepos(application: Application) : AndroidViewModel(application) {
    private val mRepoRepository: RepoRepository = RepoRepository(application)
    val repoList: LiveData<List<Repo>> = mRepoRepository.getAllRepos
    fun insert(repo: Repo?) {
        mRepoRepository.insert(repo)
    }

    fun insertAllRepos(repoList: List<Repo?>?) {
        mRepoRepository.insertAllRepos(repoList)
    }

    fun update(repo: Repo?) {
        mRepoRepository.update(repo)
    }

    fun delete(repo: Repo?) {
        mRepoRepository.delete(repo)
    }

    fun deleteAllRepos() {
        mRepoRepository.deleteAllRepos()
    }

}