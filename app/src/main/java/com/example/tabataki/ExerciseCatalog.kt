package com.example.tabataki

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCatalogScreen(
    repo: ExerciseRepository,
    tabataState: TabataState,
    onBack: (() -> Unit)? = null,
    onExerciseSelected: (Exercise) -> Unit
) {
    val lang = tabataState.appLang
    fun str(key: String) = AppStrings.get(lang, key)
    val allExercises by repo.allExercises.collectAsState(initial = emptyList())
    val categories = remember(allExercises) {
        val cats = allExercises.map { it.category }.distinct().sorted().toMutableList()
        cats.add(0, "ALL")
        cats
    }
    
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    if (categories.isNotEmpty() && selectedCategoryIndex >= categories.size) {
        selectedCategoryIndex = 0
    }
    val selectedCategory = if (categories.isNotEmpty()) categories[selectedCategoryIndex] else ""
    
    var searchQuery by remember { mutableStateOf("") }
    
    var showAddDialog by remember { mutableStateOf(false) }
    var exerciseToEdit by remember { mutableStateOf<Exercise?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(str("lib_title"), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    exerciseToEdit = null
                    showAddDialog = true 
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(str("search_ph")) },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
                if (selectedCategory != "ALL" && selectedCategory.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    var showDelCat by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { showDelCat = true },
                        modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer, androidx.compose.foundation.shape.CircleShape)
                    ) {
                        Icon(androidx.compose.material.icons.Icons.Default.Clear, contentDescription = "Delete Category", tint = MaterialTheme.colorScheme.onErrorContainer)
                    }
                    if (showDelCat) {
                        AlertDialog(
                            onDismissRequest = { showDelCat = false },
                            containerColor = MaterialTheme.colorScheme.surface,
                            title = { Text(str("del_cat_title"), color = MaterialTheme.colorScheme.onSurface) },
                            text = { Text(str("del_cat_desc"), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            confirmButton = {
                                Button(onClick = { 
                                    showDelCat = false
                                    coroutineScope.launch { repo.deleteByCategory(selectedCategory) }
                                }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                                    Text("Delete", color = MaterialTheme.colorScheme.onError)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDelCat = false }) { Text("Cancel", color = MaterialTheme.colorScheme.primary) }
                            }
                        )
                    }
                }
            }
            
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                edgePadding = 8.dp,
                divider = {}
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedCategoryIndex == index,
                        onClick = { selectedCategoryIndex = index },
                        text = {
                            Text(
                                text = category.uppercase().ifEmpty { " " },
                                color = if (selectedCategoryIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            val catPagerState = rememberPagerState(pageCount = { if (categories.isEmpty()) 1 else categories.size })
            
            // Sync Category Selection -> Pager
            LaunchedEffect(selectedCategoryIndex) {
                if (catPagerState.currentPage != selectedCategoryIndex && categories.isNotEmpty()) {
                    try {
                        catPagerState.animateScrollToPage(selectedCategoryIndex)
                    } catch (e: kotlinx.coroutines.CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        // Avoid Pager constraint crashes and layout bugs
                    }
                }
            }

            // Sync Pager -> Category Selection (Only when animation settles)
            LaunchedEffect(catPagerState.settledPage) {
                if (selectedCategoryIndex != catPagerState.settledPage && categories.isNotEmpty()) {
                    selectedCategoryIndex = catPagerState.settledPage
                }
            }

            HorizontalPager(
                state = catPagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val currentPageCat = categories.getOrNull(page) ?: " "
                val currentFiltered = allExercises.filter { 
                    (currentPageCat == "ALL" || it.category == currentPageCat) && 
                    (it.name.contains(searchQuery, ignoreCase = true) || it.focus.contains(searchQuery, ignoreCase = true))
                }

                if (currentFiltered.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(str("no_ex_found"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(currentFiltered) { exercise ->
                            ExerciseCard(
                                exercise = exercise, 
                                lang = lang, 
                                onClick = { onExerciseSelected(exercise) },
                                showAddButton = onBack != null,
                                onDelete = {
                                    coroutineScope.launch {
                                        repo.delete(exercise)
                                    }
                                },
                                onEdit = {
                                    exerciseToEdit = exercise
                                    showAddDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
        
        if (showAddDialog) {
            AddExerciseDialog(
                lang = lang,
                existingCategories = categories,
                initialExercise = exerciseToEdit,
                onDismiss = { 
                    showAddDialog = false
                    exerciseToEdit = null
                },
                onSave = { name, category, description, focus ->
                    coroutineScope.launch {
                        if (exerciseToEdit != null) {
                            repo.update(exerciseToEdit!!.copy(name = name, category = category.uppercase().trim(), description = description, focus = focus))
                        } else {
                            repo.insert(Exercise(name = name, category = category.uppercase().trim(), description = description, focus = focus, isCustom = true))
                        }
                    }
                    showAddDialog = false
                    exerciseToEdit = null
                    val newCat = category.uppercase().trim()
                    val idx = categories.indexOf(newCat)
                    if (idx >= 0) {
                        selectedCategoryIndex = idx
                    }
                }
            )
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise, lang: Language, onClick: () -> Unit, showAddButton: Boolean = true, onDelete: (() -> Unit)? = null, onEdit: (() -> Unit)? = null) {
    var expanded by remember { mutableStateOf(false) }
    fun str(key: String): String = AppStrings.get(lang, key)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(exercise.name.ifEmpty { " " }, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${str("focus_label")}${exercise.focus}", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                }
                Row {
                    if (onEdit != null) {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(androidx.compose.material.icons.Icons.Default.Settings, contentDescription = "Edit", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (onDelete != null) {
                        var showConfirm by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { showConfirm = true },
                            modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(androidx.compose.material.icons.Icons.Default.Clear, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onErrorContainer)
                        }
                        if (showConfirm) {
                            AlertDialog(
                                onDismissRequest = { showConfirm = false },
                                containerColor = MaterialTheme.colorScheme.surface,
                                title = { Text("Löschen / Delete?", color = MaterialTheme.colorScheme.onSurface) },
                                text = { Text(exercise.name.ifEmpty { " " }, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                confirmButton = {
                                    Button(onClick = { 
                                        showConfirm = false
                                        onDelete() 
                                    }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                                        Text("Delete", color = MaterialTheme.colorScheme.onError)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showConfirm = false }) {
                                        Text("Cancel", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (showAddButton) {
                        IconButton(
                            onClick = onClick,
                            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(androidx.compose.material.icons.Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(exercise.description.ifEmpty { " " }, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(str("read_more"), color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseDialog(
    lang: Language,
    existingCategories: List<String>,
    initialExercise: Exercise? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, category: String, description: String, focus: String) -> Unit
) {
    var name by remember { mutableStateOf(initialExercise?.name ?: "") }
    var description by remember { mutableStateOf(initialExercise?.description ?: "") }
    var focus by remember { mutableStateOf(initialExercise?.focus ?: "") }
    var selectedCategory by remember { mutableStateOf(initialExercise?.category ?: if (existingCategories.isNotEmpty()) existingCategories[0] else "BODYWEIGHT") }
    var categoryExpanded by remember { mutableStateOf(false) }
    fun str(key: String) = AppStrings.get(lang, key)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text(if (initialExercise == null) str("create_ex_title") else str("edit_ex"), color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(str("ex_name_label")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Category Input
                Box {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = { selectedCategory = it.uppercase() },
                        label = { Text(str("cat_label")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = { categoryExpanded = true }) {
                                Text("▼")
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = categoryExpanded, 
                        onDismissRequest = { categoryExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface).heightIn(max = 200.dp)
                    ) {
                        existingCategories.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(c, color = MaterialTheme.colorScheme.onSurface) },
                                onClick = { 
                                    selectedCategory = c
                                    categoryExpanded = false 
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = focus,
                    onValueChange = { focus = it },
                    label = { Text(str("focus_input")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(str("desc_input")) },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (name.isNotBlank()) onSave(name, selectedCategory, description, focus) 
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) { 
                Text(str("save"), color = MaterialTheme.colorScheme.onPrimaryContainer) 
            }
        },
        dismissButton = { 
            TextButton(onClick = onDismiss) { 
                Text(str("cancel"), color = MaterialTheme.colorScheme.primary) 
            } 
        }
    )
}
