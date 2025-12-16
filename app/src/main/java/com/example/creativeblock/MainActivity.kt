package com.example.creativeblock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.creativeblock.di.appModule
import com.example.creativeblock.di.retrofitModule
import com.example.creativeblock.navigation.AppNavigation
import com.example.creativeblock.ui.theme.CreativeblockTheme
import com.example.creativeblock.viewmodel.IdeaViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Koin
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule, retrofitModule)
        }

        setContent {
            CreativeblockTheme {
                // Obtener ViewModel a través de Koin
                val viewModel: IdeaViewModel = getViewModel()
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CreativeblockTheme {
        // Vista previa
    }
}