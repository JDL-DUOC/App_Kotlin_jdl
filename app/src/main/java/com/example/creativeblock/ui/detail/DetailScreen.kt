package com.example.creativeblock.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.creativeblock.ui.components.AppTopBarWithBack
import com.example.creativeblock.viewmodel.IdeaViewModel

@Composable
fun DetailScreen(
    ideaId: Long,
    viewModel: IdeaViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit
) {
    // Cargar ideas si no están cargadas
    LaunchedEffect(ideaId) {
        if (viewModel.ideas.value.isEmpty()) {
            viewModel.loadIdeas()
        }
    }

    val ideas by viewModel.ideas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val idea = ideas.find { it.id == ideaId }

    Scaffold(
        topBar = {
            AppTopBarWithBack(
                title = idea?.titulo ?: "Detalle",
                onBackClick = onBack
            )
        }
    ) { padding ->

        if (loading && idea == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (idea == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Idea no encontrada",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "La idea con ID $ideaId no existe",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // TÍTULO
                        Text(
                            text = "Título",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = idea.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // DESCRIPCIÓN
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = idea.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // INFORMACIÓN EN GRID
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Categoría",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = idea.categoria,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Prioridad",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = idea.prioridad,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Estado",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = idea.estado,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Fecha Creación",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = idea.fechaCreacion,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // RECURSOS NECESARIOS
                        Text(
                            text = "Recursos Necesarios",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = idea.recursosNecesarios,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                // BOTÓN EDITAR
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    onClick = {
                        onEdit(ideaId)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Editar Idea")
                }

                // BOTÓN ELIMINAR
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        viewModel.deleteIdea(ideaId)
                        onBack()
                    },
                    enabled = !loading
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Eliminar Idea")
                }
            }
        }
    }
}