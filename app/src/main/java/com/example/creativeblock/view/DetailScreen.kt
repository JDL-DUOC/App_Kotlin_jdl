package com.example.creativeblock.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.creativeblock.model.Categoria
import com.example.creativeblock.model.Estado
import com.example.creativeblock.model.Prioridad
import com.example.creativeblock.navigation.Screen
import com.example.creativeblock.ui.components.AppTopBarWithBack
import com.example.creativeblock.viewmodel.IdeaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Pantalla para ver el detalle completo de una idea
// Muestra toda la informacion de una idea especifica
@Composable
fun DetailScreen(
    ideaId: Int,                    // ID de la idea a mostrar
    viewModel: IdeaViewModel,
    onNavigateBack: () -> Unit,     // Funcion para volver atras
    navController: NavHostController // NUEVO: Para navegar a editar
) {
    // Estado para guardar la idea que se va a mostrar
    var idea by remember { mutableStateOf<com.example.creativeblock.data.Idea?>(null) }

    // Estado para controlar el dialogo de confirmacion
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Coroutine scope para llamar funciones suspend
    val scope = rememberCoroutineScope()

    // Carga la idea cuando se abre la pantalla
    LaunchedEffect(ideaId) {
        // Obtiene la idea de la base de datos usando coroutine
        scope.launch {
            val ideaCargada = viewModel.getIdeaById(ideaId)
            idea = ideaCargada
        }
    }

    // Dialogo de confirmacion para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Idea") },
            text = { Text("¿Estás seguro de que quieres eliminar esta idea? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        if (idea != null) {
                            scope.launch {
                                viewModel.deleteIdea(idea!!)
                                onNavigateBack()
                            }
                        }
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            // Barra superior con boton de volver
            AppTopBarWithBack(
                title = "Detalle de Idea",
                onBackClick = onNavigateBack,
                actions = {
                    // NUEVO: Botón de editar
                    IconButton(onClick = {
                        // Navegar a la pantalla de edición
                        navController.navigate(Screen.Edit.createRoute(ideaId))
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }

                    // Boton de eliminar con dialogo de confirmacion
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (idea == null) {
            // Muestra mensaje si no se encontro la idea
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Muestra circulo de carga mientras busca la idea
                if (ideaId != 0) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cargando idea...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Idea no encontrada",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Muestra el detalle de la idea
            val ideaData = idea!!
            val categoria = Categoria.fromString(ideaData.categoria)
            val prioridad = Prioridad.fromString(ideaData.prioridad)
            val estado = Estado.fromString(ideaData.estado)

            // Formatea la fecha de creacion
            val dateFormat = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault())
            val fechaFormateada = dateFormat.format(Date(ideaData.fechaCreacion))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Tarjeta con informacion principal
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Titulo
                        Text(
                            text = ideaData.titulo,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Descripcion
                        Text(
                            text = ideaData.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Tarjeta con metadatos de la idea
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Información de la Idea",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Categoria
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Categoría:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = categoria.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Prioridad
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Prioridad:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = prioridad.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Estado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Estado:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = estado.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Fecha de creacion
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Creada:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = fechaFormateada,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Tarjeta con recursos necesarios
                if (ideaData.recursosNecesarios.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Recursos Necesarios",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = ideaData.recursosNecesarios,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}