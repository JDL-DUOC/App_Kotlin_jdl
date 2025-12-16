package com.example.creativeblock.ui.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.creativeblock.ui.components.AppTopBarWithBack
import com.example.creativeblock.ui.utils.ValidationUtils
import com.example.creativeblock.viewmodel.IdeaViewModel

@Composable
fun EditScreen(
    ideaId: Long,
    viewModel: IdeaViewModel,
    onBack: () -> Unit
) {
    // Buscar la idea a editar
    val ideas by viewModel.ideas.collectAsState()
    val idea = ideas.find { it.id == ideaId }

    // Estados pre-cargados con datos actuales
    var titulo by remember { mutableStateOf(idea?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(idea?.descripcion ?: "") }
    var recursos by remember { mutableStateOf(idea?.recursosNecesarios ?: "") }

    // Estados para dropdowns (simplificados)
    var selectedCategoria by remember { mutableStateOf<com.example.creativeblock.data.remote.CatalogItem?>(null) }
    var selectedPrioridad by remember { mutableStateOf<com.example.creativeblock.data.remote.CatalogItem?>(null) }
    var selectedEstado by remember { mutableStateOf<com.example.creativeblock.data.remote.CatalogItem?>(null) }

    // Estados para mostrar dropdowns
    var mostrarCategorias by remember { mutableStateOf(false) }
    var mostrarPrioridades by remember { mutableStateOf(false) }
    var mostrarEstados by remember { mutableStateOf(false) }

    // Estados para errores
    var tituloError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }

    // Cargar catálogos si no están cargados
    LaunchedEffect(Unit) {
        if (viewModel.categorias.value.isEmpty()) {
            viewModel.loadAllData()
        }
    }

    // Observar catálogos
    val categorias by viewModel.categorias.collectAsState()
    val prioridades by viewModel.prioridades.collectAsState()
    val estados by viewModel.estados.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Scaffold(
        topBar = {
            AppTopBarWithBack(
                title = "Editar Idea",
                onBackClick = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // TÍTULO
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    tituloError = null
                },
                label = { Text("Título *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = tituloError != null,
                supportingText = {
                    tituloError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            // DESCRIPCIÓN
            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    descripcionError = null
                },
                label = { Text("Descripción *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                isError = descripcionError != null,
                supportingText = {
                    descripcionError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            // DROPDOWN CATEGORÍA (simplificado)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Categoría *",
                    style = MaterialTheme.typography.labelMedium
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { mostrarCategorias = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            selectedCategoria?.nombre?.let {
                                it.lowercase().replaceFirstChar { char -> char.uppercase() }
                            } ?: idea?.categoria ?: "Seleccionar categoría"
                        )
                    }

                    DropdownMenu(
                        expanded = mostrarCategorias,
                        onDismissRequest = { mostrarCategorias = false }
                    ) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = {
                                    Text(categoria.nombre.lowercase()
                                        .replaceFirstChar { it.uppercase() })
                                },
                                onClick = {
                                    selectedCategoria = categoria
                                    mostrarCategorias = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // DROPDOWN PRIORIDAD (simplificado)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Prioridad *",
                    style = MaterialTheme.typography.labelMedium
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { mostrarPrioridades = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            selectedPrioridad?.nombre?.let {
                                it.lowercase().replaceFirstChar { char -> char.uppercase() }
                            } ?: idea?.prioridad ?: "Seleccionar prioridad"
                        )
                    }

                    DropdownMenu(
                        expanded = mostrarPrioridades,
                        onDismissRequest = { mostrarPrioridades = false }
                    ) {
                        prioridades.forEach { prioridad ->
                            DropdownMenuItem(
                                text = {
                                    Text(prioridad.nombre.lowercase()
                                        .replaceFirstChar { it.uppercase() })
                                },
                                onClick = {
                                    selectedPrioridad = prioridad
                                    mostrarPrioridades = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // DROPDOWN ESTADO (simplificado)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Estado *",
                    style = MaterialTheme.typography.labelMedium
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { mostrarEstados = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            selectedEstado?.nombre?.let {
                                it.lowercase().replaceFirstChar { char -> char.uppercase() }
                            } ?: idea?.estado ?: "Seleccionar estado"
                        )
                    }

                    DropdownMenu(
                        expanded = mostrarEstados,
                        onDismissRequest = { mostrarEstados = false }
                    ) {
                        estados.forEach { estado ->
                            DropdownMenuItem(
                                text = {
                                    Text(estado.nombre.lowercase()
                                        .replaceFirstChar { it.uppercase() })
                                },
                                onClick = {
                                    selectedEstado = estado
                                    mostrarEstados = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // RECURSOS
            OutlinedTextField(
                value = recursos,
                onValueChange = { recursos = it },
                label = { Text("Recursos Necesarios") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(Modifier.height(24.dp))

            // BOTÓN GUARDAR CAMBIOS
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Validar
                    var isValid = true

                    tituloError = ValidationUtils.getTituloErrorMessage(titulo)
                    if (tituloError != null) isValid = false

                    descripcionError = ValidationUtils.getDescripcionErrorMessage(descripcion)
                    if (descripcionError != null) isValid = false

                    if (isValid) {

                        // Por simplicidad, uso valores por defecto
                        viewModel.updateIdea(
                            id = ideaId,
                            titulo = titulo,
                            descripcion = descripcion,
                            categoria = selectedCategoria?.id ?: 1,
                            prioridad = selectedPrioridad?.id ?: 1,
                            estado = selectedEstado?.id ?: 1,
                            recursos = recursos
                        )
                        onBack()
                    }
                },
                enabled = !loading
            ) {
                Icon(Icons.Filled.Save, contentDescription = "Guardar")
                Spacer(Modifier.width(8.dp))
                Text("Guardar Cambios")
            }
        }
    }
}