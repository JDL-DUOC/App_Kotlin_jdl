package com.example.creativeblock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.creativeblock.data.AppDatabase
import com.example.creativeblock.model.IdeaRepository
import com.example.creativeblock.navigation.AppNavigation
import com.example.creativeblock.ui.theme.CreativeblockTheme
import com.example.creativeblock.viewmodel.IdeaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CreativeblockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Inicializa la base de datos y repository
                    val database = AppDatabase.getDatabase(this)
                    val repository = IdeaRepository(database.ideaDao())

                    // Crea el ViewModel usando viewModel() de Compose
                    val viewModel: IdeaViewModel = viewModel(
                        factory = IdeaViewModelFactory(repository)
                    )

                    // Controlador de navegacion
                    val navController = rememberNavController()

                    // Configura la navegacion
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// Factory simplificada
class IdeaViewModelFactory(
    private val repository: IdeaRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return IdeaViewModel(repository) as T
    }
}