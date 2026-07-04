package com.example.cat_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    val isSearching = mutableStateOf(false)
    val searchQuery = mutableStateOf(String())

    fun clearSearch() {
        isSearching.value = false
        searchQuery.value = String()
    }


}