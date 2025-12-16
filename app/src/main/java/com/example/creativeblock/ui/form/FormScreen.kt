package com.example.creativeblock.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.creativeblock.ui.components.AppTopBarWithBack
import com.example.creativeblock.ui.utils.ValidationUtils
import com.example.creativeblock.viewmodel.IdeaViewModel

@Composable
fun FormScreen(
    viewModel: IdeaViewModel,
    onBack: () -> Unit
) {
    // Estados para los campos del formulario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var recursos by remember { mutableStateOf("") }

    // Estados para las selecciones de dropdown
    var selectedCategoria by remember { mutableStateOf<com.example.creativeblock.data.remote.CatalogItem?>(null) }
    var selectedPrioridad by remember { mutableStateOf<com.example.creativeblock.data.remote.CatalogItem?>(null) }
    var selectedEstado by remember { mutableStateOf<com.example.creativeblock.data.remote.CatalogItem?>(null) }

    // Estados para mostrar/ocultar dropdowns
    var mostrarCategorias by remember { mutableStateOf(false) }
    var mostrarPrioridades by remember { mutableStateOf(false) }
    var mostrarEstados by remember { mutableStateOf(false) }

    // Estados para errores de validación
    var tituloError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }
    var categoriaError by remember { mutableStateOf<String?>(null) }
    var prioridadError by remember { mutableStateOf<String?>(null) }
    var estadoError by remember { mutableStateOf<String?>(null) }

    // Cargar catálogos al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadAllData()
    }

    // Observar los catálogos del ViewModel
    val categorias by viewModel.categorias.collectAsState()
    val prioridades by viewModel.prioridades.collectAsState()
    val estados by viewModel.estados.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            AppTopBarWithBack(
                title = "Nueva Idea",
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

            // Mostrar estados de carga
            if (loading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.width(8.dp))
                    Text("Cargando catálogos...")
                }
                Spacer(Modifier.height(16.dp))
            }

            // Mostrar error si existe
            error?.let { errorMessage ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // CAMPO TÍTULO
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

            // CAMPO DESCRIPCIÓN
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

            // DROPDOWN CATEGORÍA
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Categoría *",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (categoriaError != null) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    // Botón que abre el menú de categorías
                    OutlinedButton(
                        onClick = {
                            if (categorias.isNotEmpty()) {
                                mostrarCategorias = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (selectedCategoria == null)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            selectedCategoria?.let {
                                // Formatear: "ARTE" -> "Arte"
                                it.nombre.lowercase().replaceFirstChar { char -> char.uppercase() }
                            } ?: "Seleccionar categoría",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Menú desplegable de categorías
                    DropdownMenu(
                        expanded = mostrarCategorias,
                        onDismissRequest = { mostrarCategorias = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (categorias.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Cargando categorías...") },
                                onClick = {}
                            )
                        } else {
                            categorias.forEach { categoria ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            categoria.nombre.lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        )
                                    },
                                    onClick = {
                                        selectedCategoria = categoria
                                        mostrarCategorias = false
                                        categoriaError = null
                                    }
                                )
                            }
                        }
                    }
                }

                // Mensaje de error para categoría
                categoriaError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // DROPDOWN PRIORIDAD
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Prioridad *",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (prioridadError != null) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    // Botón que abre el menú de prioridades
                    OutlinedButton(
                        onClick = {
                            if (prioridades.isNotEmpty()) {
                                mostrarPrioridades = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (selectedPrioridad == null)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            selectedPrioridad?.let {
                                it.nombre.lowercase().replaceFirstChar { char -> char.uppercase() }
                            } ?: "Seleccionar prioridad",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Menú desplegable de prioridades
                    DropdownMenu(
                        expanded = mostrarPrioridades,
                        onDismissRequest = { mostrarPrioridades = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (prioridades.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Cargando prioridades...") },
                                onClick = {}
                            )
                        } else {
                            prioridades.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            prioridad.nombre.lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        )
                                    },
                                    onClick = {
                                        selectedPrioridad = prioridad
                                        mostrarPrioridades = false
                                        prioridadError = null
                                    }
                                )
                            }
                        }
                    }
                }

                // Mensaje de error para prioridad
                prioridadError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // DROPDOWN ESTADO
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Estado *",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (estadoError != null) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    // Botón que abre el menú de estados
                    OutlinedButton(
                        onClick = {
                            if (estados.isNotEmpty()) {
                                mostrarEstados = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (selectedEstado == null)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            selectedEstado?.let {
                                it.nombre.lowercase().replaceFirstChar { char -> char.uppercase() }
                            } ?: "Seleccionar estado",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Menú desplegable de estados
                    DropdownMenu(
                        expanded = mostrarEstados,
                        onDismissRequest = { mostrarEstados = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (estados.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Cargando estados...") },
                                onClick = {}
                            )
                        } else {
                            estados.forEach { estado ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            estado.nombre.lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        )
                                    },
                                    onClick = {
                                        selectedEstado = estado
                                        mostrarEstados = false
                                        estadoError = null
                                    }
                                )
                            }
                        }
                    }
                }

                // Mensaje de error para estado
                estadoError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // CAMPO RECURSOS
            OutlinedTextField(
                value = recursos,
                onValueChange = { recursos = it },
                label = { Text("Recursos Necesarios") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(Modifier.height(24.dp))

            // BOTÓN GUARDAR
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Validar campos
                    var isValid = true

                    tituloError = ValidationUtils.getTituloErrorMessage(titulo)
                    if (tituloError != null) isValid = false

                    descripcionError = ValidationUtils.getDescripcionErrorMessage(descripcion)
                    if (descripcionError != null) isValid = false

                    categoriaError = if (selectedCategoria == null) "Debe seleccionar una categoría" else null
                    if (categoriaError != null) isValid = false

                    prioridadError = if (selectedPrioridad == null) "Debe seleccionar una prioridad" else null
                    if (prioridadError != null) isValid = false

                    estadoError = if (selectedEstado == null) "Debe seleccionar un estado" else null
                    if (estadoError != null) isValid = false

                    if (isValid) {
                        viewModel.addIdea(
                            titulo = titulo,
                            descripcion = descripcion,
                            categoria = selectedCategoria!!.id,
                            prioridad = selectedPrioridad!!.id,
                            estado = selectedEstado!!.id,
                            recursos = recursos
                        )
                        onBack()
                    }
                },
                enabled = !loading &&
                        categorias.isNotEmpty() &&
                        prioridades.isNotEmpty() &&
                        estados.isNotEmpty()
            ) {
                Text("Guardar Idea")
            }

            // Botón para probar sin validación (debug)
            if (loading) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Esperando que se carguen los catálogos...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}