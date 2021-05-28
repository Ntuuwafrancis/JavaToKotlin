package com.sriyank.javatokotlindemo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(repo: Repo)

    @Update
    fun update(repo: Repo)

    @Delete
    fun delete(repo: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRepos(repoList: List<Repo?>?)

    @Query("Delete from repos")
    fun deleteAll()

    //    @Query("Select * from repos order by id DESC")
    @get:Query("Select * from repos order by id DESC")
    val allRepos: LiveData<List<Repo>>
    //    DataSource.Factory<Integer, Repo> getAllRepos();
}