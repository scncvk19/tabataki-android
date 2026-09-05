package com.example.tabataki

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

@Composable
fun TabataMainScreen(tabataState: TabataState) {
    val state = tabataState.currentState

    LaunchedEffect(state) {
        if (state != TimerState.IDLE && state != TimerState.DONE) {
            while (tabataState.currentState != TimerState.IDLE && tabataState.currentState != TimerState.DONE) {
                delay(1000L)
                tabataState.tick()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            TimerState.IDLE, TimerState.DONE -> SettingsScreen(tabataState)
            else -> ActiveTimerScreen(tabataState)
        }
    }
}

@Composable
fun SettingsScreen(tabataState: TabataState) {
    val lang = tabataState.appLang
    fun str(key: String) = AppStrings.get(lang, key)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(str("lang"), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(8.dp))
            var langExpanded by remember { mutableStateOf(false) }
            Box {
                Button(
                    onClick = { langExpanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) { Text(lang.name + " ▼") }
                DropdownMenu(
                    expanded = langExpanded,
                    onDismissRequest = { langExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    Language.entries.forEach { l ->
                        DropdownMenuItem(
                            text = { Text(l.name, color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                tabataState.setLanguage(l)
                                langExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(str("timer"), fontSize = 36.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        if (tabataState.isManualMode) {
            TimeNumberSelector(str("work"), tabataState.manualWorkTime, lang) { tabataState.manualWorkTime = it }
            Spacer(modifier = Modifier.height(16.dp))
            TimeNumberSelector(str("rest"), tabataState.manualRestTime, lang) { tabataState.manualRestTime = it }
            Spacer(modifier = Modifier.height(16.dp))
            NumberSelector(str("rounds"), tabataState.manualTotalRounds) { tabataState.manualTotalRounds = it }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(tabataState.activeDayName.ifBlank { " " }, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${str("total_ex")} ${tabataState.activePlaylist.size}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    LazyColumn {
                        itemsIndexed(tabataState.activePlaylist) { index, routine ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${index + 1}. ${routine.name}", color = MaterialTheme.colorScheme.onSurface)
                                Text("${routine.work}s / ${routine.rest}s", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        if (!tabataState.isManualMode) {
            OutlinedButton(
                onClick = { tabataState.setManualMode() },
                modifier = Modifier.fillMaxWidth(0.8f).height(48.dp)
            ) { Text(str("switch_manual")) }
            Spacer(modifier = Modifier.height(12.dp))
        }

        val context = LocalContext.current
        Text(str("pro_on"), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/scncvk19/tabataki-android/issues"))
                runCatching { context.startActivity(Intent.createChooser(intent, "Feedback")) }
            },
            modifier = Modifier.fillMaxWidth(0.8f).height(48.dp)
        ) { Text(str("feedback")) }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { tabataState.startTimer() },
            modifier = Modifier.fillMaxWidth(0.8f).height(72.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            enabled = tabataState.isManualMode || tabataState.activePlaylist.isNotEmpty()
        ) {
            Text(str("start"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ActiveTimerScreen(tabataState: TabataState) {
    val lang = tabataState.appLang
    fun str(key: String) = AppStrings.get(lang, key)
    val state = tabataState.currentState
    val timeLeft = tabataState.timeLeft
    val round = tabataState.currentRound
    val totalRounds = tabataState.currentTotalRounds

    val (bgColor, textColor, label) = when (state) {
        TimerState.PREPARE -> Triple(Color(0xFFC3B091), Color(0xFF1E2019), str("prepare"))
        TimerState.WORK -> Triple(Color(0xFF4A5D23), Color.White, str("work_phase"))
        TimerState.REST -> Triple(Color(0xFF704214), Color.White, str("rest_phase"))
        else -> Triple(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onBackground, " ")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1.5f).background(bgColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(tabataState.currentRoutineName, fontSize = 32.sp, color = textColor, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Text("${str("round")} $round / $totalRounds", fontSize = 24.sp, color = textColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(if (tabataState.isPaused) str("pause") else label, fontSize = 48.sp, color = textColor, fontWeight = FontWeight.Black)
            val minutes = timeLeft / 60
            val seconds = timeLeft % 60
            Text(String.format(Locale.US, "%02d:%02d", minutes, seconds), fontSize = 110.sp, color = textColor, fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { tabataState.togglePause() }) {
                    Text(if (tabataState.isPaused) str("resume") else str("pause"))
                }
                Button(onClick = { tabataState.stopTimer() }) { Text(str("stop")) }
            }
        }

        if (!tabataState.isManualMode) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f).background(MaterialTheme.colorScheme.surface),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(tabataState.activePlaylist) { index, routine ->
                    val isCurrent = index == tabataState.currentRoutineIndex
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${index + 1}. ${routine.name}",
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                        )
                        Text("${routine.work}s / ${routine.rest}s x ${routine.rounds}")
                    }
                }
            }
        }
    }
}

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
    var isDayDialogOpen by remember { mutableStateOf(false) }
    var editingDay by remember { mutableStateOf<WorkoutDay?>(null) }
    var isRoutineDialogOpen by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<Routine?>(null) }
    var showCatalog by remember { mutableStateOf(false) }
    var suggestedExercise by remember { mutableStateOf<Exercise?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val all = exerciseRepo.allExercises.first()
            val json = repo.exportToJson(all.filter { it.isCustom })
            context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(json) }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val json = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: return@launch
            repo.importFromJson(json).forEach { exerciseRepo.insert(it) }
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
                isRoutineDialogOpen = true
            }
        )
        return
    }

    if (selectedDay == null) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                FloatingActionButton(onClick = { editingDay = null; isDayDialogOpen = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Day")
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(str("workout_days"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    var showBackupMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showBackupMenu = true }) { Icon(Icons.Default.Settings, contentDescription = "Backup") }
                        DropdownMenu(expanded = showBackupMenu, onDismissRequest = { showBackupMenu = false }) {
                            DropdownMenuItem(text = { Text("Export Backup (JSON)") }, onClick = { showBackupMenu = false; exportLauncher.launch("tabataki_backup.json") })
                            DropdownMenuItem(text = { Text("Import Backup (JSON)") }, onClick = { showBackupMenu = false; importLauncher.launch("application/json") })
                        }
                    }
                }
                if (days.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(str("no_days")) }
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(days) { day ->
                            DayCard(
                                day = day,
                                lang = lang,
                                onClick = { selectedDay = day },
                                onEdit = { editingDay = day; isDayDialogOpen = true },
                                onDelete = { repo.saveDays(repo.getDays().filter { it.id != day.id }) },
                                onPlay = { onPlayDay(day) }
                            )
                        }
                    }
                }
            }
        }

        if (isDayDialogOpen) {
            var dayName by remember(editingDay?.id) { mutableStateOf(editingDay?.name.orEmpty()) }
            AlertDialog(
                onDismissRequest = { isDayDialogOpen = false },
                title = { Text(if (editingDay == null) str("new_day") else str("edit_day")) },
                text = { OutlinedTextField(value = dayName, onValueChange = { dayName = it }, label = { Text(str("day_name")) }) },
                confirmButton = {
                    Button(onClick = {
                        val current = repo.getDays()
                        if (editingDay == null) {
                            current.add(WorkoutDay(UUID.randomUUID().toString(), dayName.ifBlank { str("unnamed") }, mutableListOf()))
                        } else {
                            val index = current.indexOfFirst { it.id == editingDay!!.id }
                            if (index >= 0) current[index] = current[index].copy(name = dayName.ifBlank { str("unnamed") })
                        }
                        repo.saveDays(current)
                        isDayDialogOpen = false
                    }) { Text(str("save")) }
                },
                dismissButton = { TextButton(onClick = { isDayDialogOpen = false }) { Text(str("cancel")) } }
            )
        }
        return
    }

    val day = selectedDay ?: return
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(day.name) },
                navigationIcon = {
                    IconButton(onClick = { selectedDay = null }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { suggestedExercise = null; editingRoutine = null; isRoutineDialogOpen = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Routine")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onPlayDay(day) }, enabled = day.routines.isNotEmpty(), modifier = Modifier.weight(1f)) {
                    Text(str("start_day"))
                }
                OutlinedButton(onClick = { showCatalog = true }, modifier = Modifier.weight(1f)) {
                    Text(if (lang == Language.DE) "Bibliothek" else "Library")
                }
            }

            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(day.routines) { routine ->
                    RoutineCard(
                        routine = routine,
                        onEdit = { editingRoutine = routine; suggestedExercise = null; isRoutineDialogOpen = true },
                        onDelete = {
                            val currentDays = repo.getDays()
                            val idx = currentDays.indexOfFirst { it.id == day.id }
                            if (idx >= 0) {
                                val newDay = currentDays[idx].copy(routines = currentDays[idx].routines.filter { it.id != routine.id }.toMutableList())
                                currentDays[idx] = newDay
                                repo.saveDays(currentDays)
                                selectedDay = newDay
                            }
                        }
                    )
                }
            }
        }
    }

    if (isRoutineDialogOpen) {
        AddRoutineDialog(
            initialRoutine = editingRoutine,
            suggestedExercise = suggestedExercise,
            lang = lang,
            onDismiss = { isRoutineDialogOpen = false },
            onSave = { name, work, rest, rounds ->
                val currentDays = repo.getDays()
                val idx = currentDays.indexOfFirst { it.id == day.id }
                if (idx >= 0) {
                    val routines = currentDays[idx].routines.toMutableList()
                    if (editingRoutine == null) {
                        routines.add(Routine(UUID.randomUUID().toString(), name.ifBlank { str("custom") }, work, rest, rounds))
                    } else {
                        val ridx = routines.indexOfFirst { it.id == editingRoutine!!.id }
                        if (ridx >= 0) routines[ridx] = Routine(editingRoutine!!.id, name, work, rest, rounds)
                    }
                    val newDay = currentDays[idx].copy(routines = routines)
                    currentDays[idx] = newDay
                    repo.saveDays(currentDays)
                    selectedDay = newDay
                }
                isRoutineDialogOpen = false
            }
        )
    }
}

@Composable
fun DayCard(day: WorkoutDay, lang: Language, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit, onPlay: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(day.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${day.routines.size} ${AppStrings.get(lang, "exercises_linked")}")
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
            IconButton(onClick = onPlay) { Icon(Icons.Default.PlayArrow, contentDescription = "Play") }
        }
    }
}

@Composable
fun RoutineCard(routine: Routine, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(routine.name, fontWeight = FontWeight.Bold)
                Text("Work: ${routine.work}s | Rest: ${routine.rest}s | Rounds: ${routine.rounds}")
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
        }
    }
}

@Composable
fun AddRoutineDialog(
    initialRoutine: Routine?,
    suggestedExercise: Exercise?,
    lang: Language,
    onDismiss: () -> Unit,
    onSave: (name: String, work: Int, rest: Int, rounds: Int) -> Unit
) {
    var name by remember(initialRoutine?.id, suggestedExercise?.id) { mutableStateOf(initialRoutine?.name ?: suggestedExercise?.name.orEmpty()) }
    var work by remember(initialRoutine?.id) { mutableIntStateOf(initialRoutine?.work ?: 20) }
    var rest by remember(initialRoutine?.id) { mutableIntStateOf(initialRoutine?.rest ?: 10) }
    var rounds by remember(initialRoutine?.id) { mutableIntStateOf(initialRoutine?.rounds ?: 8) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialRoutine == null) AppStrings.get(lang, "new_ex") else AppStrings.get(lang, "edit_ex")) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(AppStrings.get(lang, "ex_name")) })
                TimeNumberSelector(AppStrings.get(lang, "work"), work, lang) { work = it }
                TimeNumberSelector(AppStrings.get(lang, "rest"), rest, lang) { rest = it }
                NumberSelector(AppStrings.get(lang, "rounds"), rounds) { rounds = it }
            }
        },
        confirmButton = { Button(onClick = { onSave(name, work, rest, rounds) }) { Text(AppStrings.get(lang, "save")) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(AppStrings.get(lang, "cancel")) } }
    )
}

@Composable
fun NumberSelector(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                FilledIconButton(onClick = { if (value > 1) onValueChange(value - 1) }) { Text("-") }
                var textValue by remember { mutableStateOf(value.toString()) }
                LaunchedEffect(value) { textValue = value.toString() }
                BasicTextField(
                    value = textValue,
                    onValueChange = { raw ->
                        val filtered = raw.filter(Char::isDigit).take(3)
                        textValue = filtered
                        filtered.toIntOrNull()?.let(onValueChange)
                    },
                    textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(64.dp)
                )
                FilledIconButton(onClick = { onValueChange(value + 1) }) { Text("+") }
            }
        }
    }
}

@Composable
fun TimeNumberSelector(label: String, totalSeconds: Int, lang: Language, onValueChange: (Int) -> Unit) {
    var minutesMode by remember { mutableStateOf(false) }
    val displayValue = if (minutesMode) (totalSeconds / 60).coerceAtLeast(1) else totalSeconds

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label)
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { minutesMode = !minutesMode }) {
                    Text(if (minutesMode) AppStrings.get(lang, "in_min") else AppStrings.get(lang, "in_sec"))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                val step = if (minutesMode) 60 else 1
                FilledIconButton(onClick = { onValueChange((totalSeconds - step).coerceAtLeast(step)) }) { Text("-") }
                var textValue by remember { mutableStateOf(displayValue.toString()) }
                LaunchedEffect(displayValue) { textValue = displayValue.toString() }
                BasicTextField(
                    value = textValue,
                    onValueChange = { raw ->
                        val filtered = raw.filter(Char::isDigit).take(4)
                        textValue = filtered
                        filtered.toIntOrNull()?.let { onValueChange(if (minutesMode) it * 60 else it) }
                    },
                    textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(80.dp)
                )
                FilledIconButton(onClick = { onValueChange(totalSeconds + step) }) { Text("+") }
            }
        }
    }
}
