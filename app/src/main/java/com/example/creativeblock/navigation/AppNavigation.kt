package com.example.creativeblock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.creativeblock.view.DetailScreen
import com.example.creativeblock.view.FormScreen
import com.example.creativeblock.view.HomeScreen
import com.example.creativeblock.viewmodel.IdeaViewModel

// Clase que define las rutas de navegacion de la app
// Cada pantalla tiene su propia ruta
sealed class Screen(val route: String) {
    object Home : Screen("home")                           // Pantalla principal
    object Form : Screen("form")                           // Formulario de ideas
    object Edit : Screen("edit/{ideaId}") {                // NUEVO: Editar idea
        fun createRoute(ideaId: Int) = "edit/$ideaId"
    }
    object Detail : Screen("detail/{ideaId}") {            // Detalle de idea
        fun createRoute(ideaId: Int) = "detail/$ideaId"
    }
}

// Configuracion de navegacion de la aplicacion
// Define como navegar entre pantallas
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: IdeaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route  // Pantalla que se muestra al iniciar
    ) {
        // Pantalla principal - lista de ideas
        composable(route = Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToForm = {
                    // Navega al formulario para crear nueva idea
                    navController.navigate(Screen.Form.route)
                },
                onNavigateToDetail = { ideaId ->
                    // Navega al detalle de una idea especifica
                    navController.navigate(Screen.Detail.createRoute(ideaId))
                }
            )
        }

        // Pantalla de formulario - crear nueva idea
        composable(route = Screen.Form.route) {
            FormScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    // Regresa a la pantalla anterior
                    navController.popBackStack()
                },
                isEditingMode = false  // MODO CREACIÓN
            )
        }

        // NUEVA PANTALLA: Editar idea existente
        composable(
            route = Screen.Edit.route,
            arguments = listOf(
                navArgument("ideaId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val ideaId = backStackEntry.arguments?.getInt("ideaId") ?: 0

            // Cargar la idea para editar
            LaunchedEffect(ideaId) {
                val idea = viewModel.getIdeaById(ideaId)
                if (idea != null) {
                    viewModel.loadIdeaForEditing(idea)
                }
            }

            FormScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                isEditingMode = true,  // MODO EDICIÓN
                ideaId = ideaId
            )
        }

        // Pantalla de detalle - ver idea completa
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("ideaId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            // Obtiene el ID de la idea de los parametros de navegacion
            val ideaId = backStackEntry.arguments?.getInt("ideaId") ?: 0

            DetailScreen(
                ideaId = ideaId,
                viewModel = viewModel,
                onNavigateBack = {
                    // Regresa a la pantalla anterior
                    navController.popBackStack()
                },
                navController = navController  // PASA EL NAVCONTROLLER PARA EDITAR
            )
        }
    }
}