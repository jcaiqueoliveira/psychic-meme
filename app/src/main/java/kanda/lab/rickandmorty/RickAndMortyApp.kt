package kanda.lab.rickandmorty

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kanda.lab.rickandmorty.character.CharDetail
import kanda.lab.rickandmorty.home.data.Character
import kanda.lab.rickandmorty.home.ui.CharactersScreen

@Composable
fun RickAndMortyApp() {
    val navHost = rememberNavController()

    NavHost(
        navController = navHost,
        route = Router.ENTRY_POINT,
        startDestination = Router.CHARACTER_GRID
    ) {
        composable(Router.CHARACTER_GRID) { nav ->
            CharactersScreen(navigateToDetail = {
                navHost.currentBackStackEntry?.savedStateHandle?.set("char", it)
                navHost.navigate(Router.CHARACTER_DETAIL)
            })
        }
        composable(Router.CHARACTER_DETAIL) {
            val char = navHost.previousBackStackEntry?.savedStateHandle?.get<Character>("char")
            CharDetail(char)
        }
    }
}