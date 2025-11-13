package com.example.creativeblock.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.creativeblock.model.Categoria
import com.example.creativeblock.model.Estado
import com.example.creativeblock.model.Prioridad
import com.example.creativeblock.ui.components.AppTopBarWithBack
import com.example.creativeblock.viewmodel.IdeaViewModel
import kotlinx.coroutines.delay

// Pantalla para crear o editar ideas creativas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: IdeaViewModel,
    onNavigateBack: () -> Unit,
    isEditingMode: Boolean = false,
    ideaId: Int = 0
) {
    // Observa el estado del formulario del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Estados para los dropdowns (menus desplegables)
    var expandedCategoria by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Barra superior con titulo dinámico
            AppTopBarWithBack(
                title = if (isEditingMode) "Editar Idea" else "Nueva Idea",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        // Formulario principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo de titulo
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = { viewModel.onTituloChange(it) },
                label = { Text("Título *") },
                placeholder = { Text("Ej: Novela de ciencia ficción") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Create, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.tituloError != null,
                supportingText = {
                    if (uiState.tituloError != null) {
                        Text(uiState.tituloError!!)
                    }
                }
            )

            // Campo de descripcion
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Descripción *") },
                placeholder = { Text("Describe tu idea creativa...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 6,
                isError = uiState.descripcionError != null,
                supportingText = {
                    if (uiState.descripcionError != null) {
                        Text(uiState.descripcionError!!)
                    }
                }
            )

            // Dropdown de categoria
            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria }
            ) {
                OutlinedTextField(
                    value = if (uiState.categoria.isNotEmpty()) {
                        // Mostrar nombre display de la categoría
                        Categoria.fromString(uiState.categoria).displayName
                    } else {
                        "Seleccionar categoría"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.FolderOpen, contentDescription = null)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    isError = uiState.categoriaError != null,
                    supportingText = {
                        if (uiState.categoriaError != null) {
                            Text(uiState.categoriaError!!)
                        }
                    }
                )

                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    // Muestra todas las categorias disponibles
                    Categoria.entries.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.displayName) },
                            onClick = {
                                viewModel.onCategoriaChange(categoria.name)
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

            // Dropdown de prioridad
            ExposedDropdownMenuBox(
                expanded = expandedPrioridad,
                onExpandedChange = { expandedPrioridad = !expandedPrioridad }
            ) {
                OutlinedTextField(
                    value = if (uiState.prioridad.isNotEmpty()) {
                        // Mostrar nombre display de la prioridad
                        Prioridad.fromString(uiState.prioridad).displayName
                    } else {
                        "Seleccionar prioridad"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    isError = uiState.prioridadError != null,
                    supportingText = {
                        if (uiState.prioridadError != null) {
                            Text(uiState.prioridadError!!)
                        }
                    }
                )

                ExposedDropdownMenu(
                    expanded = expandedPrioridad,
                    onDismissRequest = { expandedPrioridad = false }
                ) {
                    // Muestra todas las prioridades disponibles
                    Prioridad.entries.forEach { prioridad ->
                        DropdownMenuItem(
                            text = { Text(prioridad.displayName) },
                            onClick = {
                                viewModel.onPrioridadChange(prioridad.name)
                                expandedPrioridad = false
                            }
                        )
                    }
                }
            }

            // Dropdown de estado
            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = { expandedEstado = !expandedEstado }
            ) {
                OutlinedTextField(
                    value = if (uiState.estado.isNotEmpty()) {
                        // Mostrar nombre display del estado
                        Estado.fromString(uiState.estado).displayName
                    } else {
                        "Seleccionar estado"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Flag, contentDescription = null)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    isError = uiState.estadoError != null,
                    supportingText = {
                        if (uiState.estadoError != null) {
                            Text(uiState.estadoError!!)
                        }
                    }
                )

                ExposedDropdownMenu(
                    expanded = expandedEstado,
                    onDismissRequest = { expandedEstado = false }
                ) {
                    // Muestra todos los estados disponibles
                    Estado.entries.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado.displayName) },
                            onClick = {
                                viewModel.onEstadoChange(estado.name)
                                expandedEstado = false
                            }
                        )
                    }
                }
            }

            // Campo de recursos necesarios
            OutlinedTextField(
                value = uiState.recursosNecesarios,
                onValueChange = { viewModel.onRecursosChange(it) },
                label = { Text("Recursos necesarios") },
                placeholder = { Text("Ej: Laptop, software de edición, tiempo...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Build, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Boton dinámico (Guardar/Actualizar)
            Button(
                onClick = {
                    if (isEditingMode) {
                        viewModel.updateExistingIdea(ideaId)
                    } else {
                        viewModel.onGuardarClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    // Muestra circulo de carga mientras guarda/actualiza
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    // Texto dinámico del botón
                    Icon(
                        imageVector = if (isEditingMode) Icons.Default.Edit else Icons.Default.Save,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isEditingMode) "Actualizar Idea" else "Guardar Idea")
                }
            }

            // Efecto para volver automaticamente cuando se guarda/actualiza exitosamente
            LaunchedEffect(uiState.guardadoExitoso) {
                if (uiState.guardadoExitoso) {
                    // Pequeno delay para que el usuario vea el mensaje de exito
                    delay(500)
                    viewModel.resetForm()
                    onNavigateBack()
                }
            }
        }
    }
}