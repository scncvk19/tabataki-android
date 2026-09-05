package com.example.tabataki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

val CamoColorScheme = darkColorScheme(
    primary = Color(0xFFA5B076),
    onPrimary = Color(0xFF1E2019),
    primaryContainer = Color(0xFF4A5D23),
    onPrimaryContainer = Color(0xFFE8F0C8),
    secondary = Color(0xFF8C7D62),
    onSecondary = Color(0xFF1E2019),
    background = Color(0xFF1E2019),
    onBackground = Color(0xFFDFE0D5),
    surface = Color(0xFF2C2F24),
    onSurface = Color(0xFFDFE0D5),
    surfaceVariant = Color(0xFF3B4031),
    onSurfaceVariant = Color(0xFFC2C5B5)
)

@Composable
fun CamoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CamoColorScheme,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CamoTheme {
                val context = LocalContext.current
                val tabataState = remember { TabataState(context) }
                val routineRepo = remember { RoutineRepository(context) }
                val dataStoreManager = remember { DataStoreManager(context) }

                val exerciseDb = remember { TabatakiDatabase.getDatabase(context) }
                val exerciseDao = remember { exerciseDb.exerciseDao() }
                val exerciseRepo = remember { ExerciseRepository(exerciseDao) }

                val isOnboardingCompleted by dataStoreManager.isOnboardingCompleted.collectAsState(initial = null)
                val currentExercises by exerciseDao.getAllExercises().collectAsState(initial = null)
                val coroutineScope = rememberCoroutineScope()

                var selectedTab by remember { mutableIntStateOf(0) }
                val lang = tabataState.appLang
                fun str(key: String) = AppStrings.get(lang, key)

                LaunchedEffect(currentExercises) {
                    val list = currentExercises
                    if (list != null && list.isEmpty()) {
                        exerciseRepo.populateInitialDataIfNeeded(0, context, lang)
                    }
                }

                DisposableEffect(Unit) {
                    onDispose { tabataState.release() }
                }

                when (isOnboardingCompleted) {
                    null -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )

                    false -> OnboardingScreen(
                        tabataState = tabataState,
                        onFinish = {
                            coroutineScope.launch {
                                dataStoreManager.setOnboardingCompleted(true)
                            }
                        }
                    )

                    true -> {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            containerColor = MaterialTheme.colorScheme.background,
                            bottomBar = {
                                if (tabataState.currentState == TimerState.IDLE || tabataState.currentState == TimerState.DONE) {
                                    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                                        NavigationBarItem(
                                            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Timer") },
                                            label = { Text(str("tabataki_tab")) },
                                            selected = selectedTab == 0,
                                            onClick = { selectedTab = 0 },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                            )
                                        )
                                        NavigationBarItem(
                                            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Routines") },
                                            label = { Text(if (lang == Language.DE) "Routinen" else "Routines") },
                                            selected = selectedTab == 1,
                                            onClick = { selectedTab = 1 },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                            )
                                        )
                                        NavigationBarItem(
                                            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Library") },
                                            label = { Text(if (lang == Language.DE) "Übungen" else "Library") },
                                            selected = selectedTab == 2,
                                            onClick = { selectedTab = 2 },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                            )
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->
                            val pagerState = rememberPagerState(pageCount = { 3 })

                            LaunchedEffect(selectedTab) {
                                if (pagerState.currentPage != selectedTab) {
                                    pagerState.animateScrollToPage(selectedTab)
                                }
                            }

                            LaunchedEffect(pagerState.settledPage) {
                                if (selectedTab != pagerState.settledPage) {
                                    selectedTab = pagerState.settledPage
                                }
                            }

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),
                                userScrollEnabled = tabataState.currentState == TimerState.IDLE || tabataState.currentState == TimerState.DONE
                            ) { page ->
                                when (page) {
                                    0 -> TabataMainScreen(tabataState)
                                    1 -> RoutinesScreen(
                                        repo = routineRepo,
                                        exerciseRepo = exerciseRepo,
                                        tabataState = tabataState,
                                        onPlayDay = { loadedDay ->
                                            tabataState.applyPlaylist(loadedDay)
                                            selectedTab = 0
                                        }
                                    )
                                    2 -> ExerciseCatalogScreen(
                                        repo = exerciseRepo,
                                        tabataState = tabataState,
                                        onBack = null,
                                        onExerciseSelected = { }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
