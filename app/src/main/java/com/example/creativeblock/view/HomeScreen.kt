package com.example.creativeblock.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.creativeblock.data.Idea
import com.example.creativeblock.ui.components.AppTopBar
import com.example.creativeblock.viewmodel.IdeaViewModel

// Pantalla principal que muestra la lista de ideas creativas
// Es la primera pantalla que ve el usuario al abrir la app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: IdeaViewModel,
    onNavigateToForm: () -> Unit,      // Funcion para ir al formulario
    onNavigateToDetail: (Int) -> Unit  // Funcion para ver detalle de idea
) {
    // Observa la lista de ideas del ViewModel
    // Se actualiza automaticamente cuando hay cambios
    val ideas by viewModel.allIdeas.collectAsState(initial = emptyList())

    // Estado para la busqueda
    var searchQuery by remember { mutableStateOf("") }

    // Filtra las ideas basado en la consulta de busqueda
    val filteredIdeas = ideas.filter { idea ->
        searchQuery.isEmpty() ||
                idea.titulo.contains(searchQuery, ignoreCase = true) ||
                idea.descripcion.contains(searchQuery, ignoreCase = true) ||
                idea.categoria.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            // Barra superior con titulo de la app
            AppTopBar(title = "CreativeBlock")
        },
        floatingActionButton = {
            // Boton flotante para agregar nueva idea
            FloatingActionButton(
                onClick = onNavigateToForm,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar idea")
            }
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de busqueda funcional - VERSIÓN QUE SÍ FUNCIONA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                // Usamos un TextField básico pero funcional
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Buscar ideas...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
            }

            // Lista de ideas
            if (filteredIdeas.isEmpty()) {
                // Mensaje cuando no hay ideas
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (searchQuery.isNotEmpty()) {
                        // Mensaje cuando no hay resultados de busqueda
                        Text(
                            text = "No se encontraron ideas",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Intenta con otros términos de búsqueda",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        // Mensaje cuando no hay ideas creadas
                        Text(
                            text = "¡Aún no tienes ideas!",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Presiona el botón + para crear tu primera idea creativa",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Muestra cuántos resultados se encontraron
                if (searchQuery.isNotEmpty()) {
                    Text(
                        text = "Encontradas ${filteredIdeas.size} ideas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Lista de ideas cuando hay datos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredIdeas) { idea ->
                        // Tarjeta para cada idea
                        IdeaCard(
                            idea = idea,
                            onClick = { onNavigateToDetail(idea.id) }
                        )
                    }
                }
            }
        }
    }
}

// Componente que muestra una idea en forma de tarjeta
// Se usa en la lista de la pantalla principal
@Composable
fun IdeaCard(
    idea: Idea,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Titulo de la idea
            Text(
                text = idea.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripcion corta (primeros 100 caracteres)
            val descripcionCorta = if (idea.descripcion.length > 100) {
                idea.descripcion.substring(0, 100) + "..."
            } else {
                idea.descripcion
            }

            Text(
                text = descripcionCorta,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Informacion adicional de la idea
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Categoria
                Text(
                    text = idea.categoria,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Estado
                Text(
                    text = idea.estado,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}