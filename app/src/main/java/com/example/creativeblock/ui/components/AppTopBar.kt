package com.example.creativeblock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// Componente de barra superior personalizada para la app
// Se usa en todas las pantallas para mantener consistencia
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,                          // Titulo a mostrar
    navigationIcon: ImageVector? = null,    // Icono de navegacion (opcional)
    navigationIconDescription: String? = null,
    onNavigationClick: (() -> Unit)? = null, // Accion al hacer clic en el icono
    actions: @Composable () -> Unit = {}    // Acciones adicionales (iconos a la derecha)
) {
    // Colores para el gradiente de la barra
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primaryContainer
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            },
            navigationIcon = {
                // Muestra el icono de navegacion solo si se proporciono
                if (navigationIcon != null && onNavigationClick != null) {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = navigationIconDescription,
                            tint = Color.White
                        )
                    }
                }
            },
            actions = { actions() },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,  // Fondo transparente para ver el gradiente
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            windowInsets = WindowInsets.statusBars
        )
    }
}

// Variante conveniente de AppTopBar con boton de volver
// Para pantallas secundarias que necesitan regresar
@Composable
fun AppTopBarWithBack(
    title: String,
    onBackClick: () -> Unit,
    actions: @Composable () -> Unit = {}
) {
    AppTopBar(
        title = title,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        navigationIconDescription = "Volver",
        onNavigationClick = onBackClick,
        actions = actions
    )
}