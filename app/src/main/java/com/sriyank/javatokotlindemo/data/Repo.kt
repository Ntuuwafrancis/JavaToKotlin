package com.sriyank.javatokotlindemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sriyank.javatokotlindemo.models.Owner

@Entity(tableName = "repos")
data class Repo(
        @PrimaryKey var id: Int,
        var name: String?,
        var language: String?,
        @SerializedName("html_url") var htmlUrl: String?,
        var description: String?,
        @SerializedName("stargazers_count") var stars: Int?,
        @SerializedName("watchers_count") var watchers: Int?,
        var forks: Int?,
        var owner: Owner?
)