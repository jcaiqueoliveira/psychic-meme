package kanda.lab.rickandmorty.home.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavHostController.Start() {
    NavHost(this, startDestination = "home") {
        //composable("home") { HomeScreen(vm = vm) }
    }
}
