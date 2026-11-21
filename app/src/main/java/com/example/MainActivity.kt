package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.firebase.AiTeacherRepository
import com.example.data.firebase.AuthManager.authStateFlow
import com.example.data.firebase.CivilsCloudRepository
import com.example.data.firebase.configureAppCheck
import com.example.data.local.AppPreferences
import com.example.data.local.CivilsDatabase
import com.example.data.model.ThemeMode
import com.example.notifications.StudyReminderScheduler
import com.example.ui.screens.AiTeacherScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChapterDetailScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FlashcardsAndMotivationScreen
import com.example.ui.screens.PlannerAndRemindersScreen
import com.example.ui.screens.ProgressDashboardScreen
import com.example.ui.screens.SyllabusLibraryScreen
import com.example.ui.screens.TestAcademyScreen
import com.example.ui.theme.CivilsPurpleDreamTheme
import com.example.ui.theme.DreamyGalaxyBackground
import com.example.ui.viewmodel.AiTeacherViewModel
import com.example.ui.viewmodel.CivilsViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

const val ROUTE_HOME = "home"
const val ROUTE_SYLLABUS = "syllabus"
const val ROUTE_CHAPTER = "chapter"
const val ROUTE_PLANNER = "planner"
const val ROUTE_REMINDERS = "reminders"
const val ROUTE_FLASHCARDS = "flashcards"
const val ROUTE_MOTIVATION = "motivation"
const val ROUTE_TESTS = "tests"
const val ROUTE_AI_TEACHER = "ai_teacher"
const val ROUTE_PROGRESS = "progress"

private data class NavTabItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Must configure App Check BEFORE accessing Firebase.ai or creating ViewModels
        configureAppCheck(this, intent)
        StudyReminderScheduler.ensureChannel(this)
        enableEdgeToEdge()

        setContent {
            val appContext = LocalContext.current.applicationContext
            val prefs = remember { AppPreferences(appContext) }
            val themeMode by prefs.themeModeFlow.collectAsStateWithLifecycle(initialValue = ThemeMode.LIGHT_LAVENDER)
            val isDarkGalaxy = themeMode == ThemeMode.DARK_GALAXY
            val currentUser by Firebase.auth.authStateFlow().collectAsStateWithLifecycle(initialValue = Firebase.auth.currentUser)

            CivilsPurpleDreamTheme(darkTheme = isDarkGalaxy) {
                val user = currentUser
                if (user == null) {
                    AuthScreen(
                        isDarkGalaxy = isDarkGalaxy,
                        onAuthSuccess = {}
                    )
                } else {
                    key(user.uid) {
                        val db = remember { CivilsDatabase.getInstance(appContext) }
                        val dao = remember { db.civilsDao() }
                        val cloudRepo = remember { CivilsCloudRepository(appContext, dao) }
                        val aiRepo = remember { AiTeacherRepository() }

                        val civilsViewModel: CivilsViewModel = viewModel(
                            key = "civils_${user.uid}",
                            factory = viewModelFactory {
                                initializer {
                                    CivilsViewModel(
                                        cloudRepo = cloudRepo,
                                        dao = dao,
                                        prefs = prefs,
                                        aiRepo = aiRepo,
                                        currentUserId = user.uid,
                                        defaultDisplayName = user.displayName ?: "IAS Aspirant 2029"
                                    )
                                }
                            }
                        )
                        val aiTeacherViewModel: AiTeacherViewModel = viewModel(
                            key = "ai_${user.uid}",
                            factory = viewModelFactory {
                                initializer { AiTeacherViewModel(aiRepo = aiRepo) }
                            }
                        )

                        CivilsIasMainShell(
                            viewModel = civilsViewModel,
                            aiViewModel = aiTeacherViewModel,
                            isDarkGalaxy = isDarkGalaxy
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CivilsIasMainShell(
    viewModel: CivilsViewModel,
    aiViewModel: AiTeacherViewModel,
    isDarkGalaxy: Boolean
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ROUTE_HOME
    val snackbarHostState = remember { SnackbarHostState() }
    val statusBanner by viewModel.statusBanner.collectAsStateWithLifecycle()
    var pendingAiPrompt by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(statusBanner) {
        val msg = statusBanner
        if (!msg.isNullOrBlank()) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearBanner()
        }
    }

    val navTabs = listOf(
        NavTabItem(ROUTE_HOME, "Home", Icons.Default.Home, "nav_home"),
        NavTabItem(ROUTE_SYLLABUS, "Syllabus", Icons.Default.Book, "nav_syllabus"),
        NavTabItem(ROUTE_PLANNER, "Plan", Icons.Default.CalendarToday, "nav_planner"),
        NavTabItem(ROUTE_FLASHCARDS, "Cards", Icons.Default.Style, "nav_flashcards"),
        NavTabItem(ROUTE_TESTS, "Zones", Icons.Default.Quiz, "nav_tests"),
        NavTabItem(ROUTE_AI_TEACHER, "Teacher", Icons.Default.Psychology, "nav_ai_teacher")
    )

    val navigateToTopTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(ROUTE_HOME) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    DreamyGalaxyBackground(isDarkGalaxy = isDarkGalaxy) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 600.dp

            Row(modifier = Modifier.fillMaxSize()) {
                if (useRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        navTabs.forEach { tab ->
                            val selected = currentRoute.startsWith(tab.route)
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToTopTab(tab.route) },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }

                Scaffold(
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        if (!useRail) {
                            NavigationBar(
                                windowInsets = WindowInsets.navigationBars,
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                            ) {
                                navTabs.forEach { tab ->
                                    val selected = currentRoute.startsWith(tab.route)
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateToTopTab(tab.route) },
                                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                                        label = { Text(tab.label, maxLines = 1) },
                                        modifier = Modifier.testTag(tab.testTag)
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = ROUTE_HOME
                        ) {
                            composable(ROUTE_HOME) {
                                DashboardScreen(
                                    viewModel = viewModel,
                                    onNavigateToSyllabus = { navigateToTopTab(ROUTE_SYLLABUS) },
                                    onOpenChapter = { chId -> navController.navigate("$ROUTE_CHAPTER/$chId") },
                                    onNavigateToPlanner = { navigateToTopTab(ROUTE_PLANNER) },
                                    onNavigateToReminders = { navController.navigate(ROUTE_REMINDERS) },
                                    onNavigateToFlashcards = { navigateToTopTab(ROUTE_FLASHCARDS) },
                                    onNavigateToMotivation = { navController.navigate(ROUTE_MOTIVATION) },
                                    onNavigateToTests = { navigateToTopTab(ROUTE_TESTS) },
                                    onNavigateToAiTeacher = { navigateToTopTab(ROUTE_AI_TEACHER) },
                                    onNavigateToProgress = { navController.navigate(ROUTE_PROGRESS) }
                                )
                            }
                            composable(ROUTE_SYLLABUS) {
                                SyllabusLibraryScreen(
                                    viewModel = viewModel,
                                    onOpenChapter = { chId -> navController.navigate("$ROUTE_CHAPTER/$chId") }
                                )
                            }
                            composable(
                                route = "$ROUTE_CHAPTER/{chapterId}",
                                arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
                            ) { entry ->
                                val chId = entry.arguments?.getString("chapterId") ?: "ncert_hist_6_ch1"
                                ChapterDetailScreen(
                                    chapterId = chId,
                                    viewModel = viewModel,
                                    onBack = { navController.popBackStack() },
                                    onAskAiTeacher = { prompt ->
                                        pendingAiPrompt = prompt
                                        navigateToTopTab(ROUTE_AI_TEACHER)
                                    }
                                )
                            }
                            composable(ROUTE_PLANNER) {
                                PlannerAndRemindersScreen(viewModel = viewModel, initialTab = 0)
                            }
                            composable(ROUTE_REMINDERS) {
                                PlannerAndRemindersScreen(viewModel = viewModel, initialTab = 1)
                            }
                            composable(ROUTE_FLASHCARDS) {
                                FlashcardsAndMotivationScreen(viewModel = viewModel, initialTab = 0)
                            }
                            composable(ROUTE_MOTIVATION) {
                                FlashcardsAndMotivationScreen(viewModel = viewModel, initialTab = 1)
                            }
                            composable(ROUTE_TESTS) {
                                TestAcademyScreen(viewModel = viewModel, aiViewModel = aiViewModel)
                            }
                            composable(ROUTE_AI_TEACHER) {
                                AiTeacherScreen(
                                    aiViewModel = aiViewModel,
                                    initialPrompt = pendingAiPrompt
                                )
                            }
                            composable(ROUTE_PROGRESS) {
                                ProgressDashboardScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
