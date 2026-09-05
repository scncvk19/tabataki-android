package com.example.tabataki

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen(
    repo: RoutineRepository,
    exerciseRepo: ExerciseRepository,
    tabataState: TabataState,
    onPlayDay: (WorkoutDay) -> Unit
) {
    val lang = tabataState.appLang
    fun str(key: String) = AppStrings.get(lang, key)

    val days by repo.daysFlow.collectAsState()
    var selectedDay by remember { mutableStateOf<WorkoutDay?>(null) }
    var dayDialog by remember { mutableStateOf(false) }
    var editingDay by remember { mutableStateOf<WorkoutDay?>(null) }
    var routineDialog by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<Routine?>(null) }
    var suggestedExercise by remember { mutableStateOf<Exercise?>(null) }
    var showCatalog by remember { mutableStateOf(false) }
    var showAddChoice by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val exportLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                runCatching {
                    val custom = exerciseRepo.allExercises.first().filter { it.isCustom }
                    context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use {
                        it.write(repo.exportToJson(custom))
                    }
                }
            }
        }
    }

    val importLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                runCatching {
                    val json = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                    if (json != null) {
                        repo.importFromJson(json).forEach { exerciseRepo.insert(it) }
                    }
                }
            }
        }
    }

    if (showCatalog) {
        ExerciseCatalogScreen(
            repo = exerciseRepo,
            tabataState = tabataState,
            onBack = { showCatalog = false },
            onExerciseSelected = { exercise ->
                showCatalog = false
                suggestedExercise = exercise
                editingRoutine = null
                routineDialog = true
            }
        )
        return
    }

    if (selectedDay == null) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        editingDay = null
                        dayDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) { Icon(Icons.Default.Add, contentDescription = "Add day") }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(str("workout_days"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    var backupMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { backupMenu = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Backup")
                        }
                        DropdownMenu(expanded = backupMenu, onDismissRequest = { backupMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Export Backup (JSON)") },
                                onClick = { backupMenu = false; exportLauncher.launch("tabataki_backup.json") }
                            )
                            DropdownMenuItem(
                                text = { Text("Import Backup (JSON)") },
                                onClick = { backupMenu = false; importLauncher.launch("application/json") }
                            )
                        }
                    }
                }

                if (days.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(str("no_days"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(days, key = { it.id }) { day ->
                            DayCard(
                                day = day,
                                lang = lang,
                                onClick = { selectedDay = day },
                                onEdit = { editingDay = day; dayDialog = true },
                                onDelete = { repo.saveDays(repo.getDays().filter { it.id != day.id }) },
                                onPlay = { onPlayDay(day) }
                            )
                        }
                    }
                }
            }
        }

        if (dayDialog) {
            var dayName by remember(editingDay?.id) { mutableStateOf(editingDay?.name.orEmpty()) }
            AlertDialog(
                onDismissRequest = { dayDialog = false },
                title = { Text(if (editingDay == null) str("new_day") else str("edit_day")) },
                text = {
                    OutlinedTextField(
                        value = dayName,
                        onValueChange = { dayName = it },
                        label = { Text(str("day_name")) },
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        val current = repo.getDays()
                        val finalName = dayName.ifBlank { AppStrings.get(lang, "unnamed") }
                        if (editingDay == null) {
                            current.add(WorkoutDay(UUID.randomUUID().toString(), finalName, mutableListOf()))
                        } else {
                            val index = current.indexOfFirst { it.id == editingDay!!.id }
                            if (index >= 0) current[index] = current[index].copy(name = finalName)
                        }
                        repo.saveDays(current)
                        dayDialog = false
                    }) { Text(str("save")) }
                },
                dismissButton = { TextButton(onClick = { dayDialog = false }) { Text(str("cancel")) } }
            )
        }
        return
    }

    val day = selectedDay ?: return
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(day.name, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                navigationIcon = {
                    IconButton(onClick = { selectedDay = null }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddChoice = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) { Icon(Icons.Default.Add, contentDescription = "Add routine") }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Button(
                onClick = { onPlayDay(day) },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                enabled = day.routines.isNotEmpty()
            ) {
                Text("${str("start_day")} (${day.routines.size} ${str("ex")})")
            }

            if (day.routines.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(str("no_exercises"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(day.routines, key = { it.id }) { routine ->
                        RoutineCard(
                            routine = routine,
                            onEdit = {
                                editingRoutine = routine
                                suggestedExercise = null
                                routineDialog = true
                            },
                            onDelete = {
                                val current = repo.getDays()
                                val index = current.indexOfFirst { it.id == day.id }
                                if (index >= 0) {
                                    val updated = current[index].copy(
                                        routines = current[index].routines.filterNot { it.id == routine.id }.toMutableList()
                                    )
                                    current[index] = updated
                                    repo.saveDays(current)
                                    selectedDay = updated
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddChoice) {
        AlertDialog(
            onDismissRequest = { showAddChoice = false },
            title = { Text(if (lang == Language.DE) "Übung hinzufügen" else "Add Exercise") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            showAddChoice = false
                            suggestedExercise = null
                            showCatalog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(if (lang == Language.DE) "Aus Bibliothek wählen" else "Choose from Library") }
                    OutlinedButton(
                        onClick = {
                            showAddChoice = false
                            suggestedExercise = null
                            editingRoutine = null
                            routineDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(if (lang == Language.DE) "Schnelle eigene Übung" else "Quick Custom Exercise") }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { showAddChoice = false }) { Text(str("cancel")) } }
        )
    }

    if (routineDialog) {
        AddRoutineDialog(
            initialRoutine = editingRoutine,
            suggestedExercise = suggestedExercise,
            lang = lang,
            onDismiss = { routineDialog = false },
            onSave = { name, work, rest, rounds ->
                val current = repo.getDays()
                val dayIndex = current.indexOfFirst { it.id == day.id }
                if (dayIndex >= 0) {
                    val routines = current[dayIndex].routines.toMutableList()
                    if (editingRoutine == null) {
                        routines.add(Routine(UUID.randomUUID().toString(), name.ifBlank { AppStrings.get(lang, "custom") }, work, rest, rounds))
                    } else {
                        val index = routines.indexOfFirst { it.id == editingRoutine!!.id }
                        if (index >= 0) routines[index] = routines[index].copy(name = name, work = work, rest = rest, rounds = rounds)
                    }
                    val updated = current[dayIndex].copy(routines = routines)
                    current[dayIndex] = updated
                    repo.saveDays(current)
                    selectedDay = updated
                }
                routineDialog = false
            }
        )
    }
}

@Composable
fun DayCard(
    day: WorkoutDay,
    lang: Language,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPlay: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(day.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("${day.routines.size} ${AppStrings.get(lang, "exercises_linked")}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE57373)) }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onPlay, modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }
    }
}

@Composable
fun RoutineCard(routine: Routine, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(routine.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Work: ${routine.work}s | Rest: ${routine.rest}s | Rounds: ${routine.rounds}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE57373)) }
        }
    }
}

@Composable
fun AddRoutineDialog(
    initialRoutine: Routine?,
    suggestedExercise: Exercise?,
    lang: Language,
    onDismiss: () -> Unit,
    onSave: (String, Int, Int, Int) -> Unit
) {
    var name by remember(initialRoutine?.id, suggestedExercise?.id) { mutableStateOf(initialRoutine?.name ?: suggestedExercise?.name.orEmpty()) }
    var work by remember(initialRoutine?.id) { mutableIntStateOf(initialRoutine?.work ?: 20) }
    var rest by remember(initialRoutine?.id) { mutableIntStateOf(initialRoutine?.rest ?: 10) }
    var rounds by remember(initialRoutine?.id) { mutableIntStateOf(initialRoutine?.rounds ?: 8) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialRoutine == null) AppStrings.get(lang, "new_ex") else AppStrings.get(lang, "edit_ex")) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(AppStrings.get(lang, "ex_name")) },
                    singleLine = true
                )
                TimeNumberSelector(AppStrings.get(lang, "work"), work, lang) { work = it }
                TimeNumberSelector(AppStrings.get(lang, "rest"), rest, lang) { rest = it }
                NumberSelector(AppStrings.get(lang, "rounds"), rounds) { rounds = it }
            }
        },
        confirmButton = { Button(onClick = { onSave(name, work, rest, rounds) }) { Text(AppStrings.get(lang, "save")) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(AppStrings.get(lang, "cancel")) } }
    )
}
