package com.sriyank.javatokotlindemo.models

import com.google.gson.annotations.SerializedName
import com.sriyank.javatokotlindemo.data.Repo

data class SearchResponse(
        @SerializedName("total_count") var totalCount: Int,
        var items: List<Repo>?
        )