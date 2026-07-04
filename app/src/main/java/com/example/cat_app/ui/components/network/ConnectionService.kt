package com.example.cat_app.ui.components.network

import android.content.Context
import android.net.ConnectivityManager
import org.koin.core.component.KoinComponent

class ConnectionService(
    private val context: Context
) : KoinComponent {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


}