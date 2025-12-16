package com.example.creativeblock.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.creativeblock.ui.edit.EditScreen
import com.example.creativeblock.ui.home.HomeScreen
import com.example.creativeblock.ui.form.FormScreen
import com.example.creativeblock.ui.detail.DetailScreen
import com.example.creativeblock.viewmodel.IdeaViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Form : Screen("form")
    object Detail : Screen("detail/{id}") {
        fun withId(id: Long) = "detail/$id"
    }
    object Edit : Screen("edit/{id}") {
        fun withId(id: Long) = "edit/$id"
    }
}

@Composable
fun AppNavigation(viewModel: IdeaViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddIdea = { nav.navigate(Screen.Form.route) },
                onOpenDetail = { id -> nav.navigate(Screen.Detail.withId(id)) }
            )
        }

        composable(Screen.Form.route) {
            FormScreen(
                viewModel = viewModel,
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            Screen.Detail.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: 0L
            DetailScreen(
                ideaId = id,
                viewModel = viewModel,
                onBack = { nav.popBackStack() },
                onEdit = { ideaId -> nav.navigate(Screen.Edit.withId(ideaId)) }
            )
        }

        composable(
            Screen.Edit.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: 0L
            EditScreen(
                ideaId = id,
                viewModel = viewModel,
                onBack = { nav.popBackStack() }
            )
        }
    }
}