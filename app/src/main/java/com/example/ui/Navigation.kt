package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.model.AppState
import com.example.model.UiState
import com.example.ui.components.FloatingTimerPill
import com.example.ui.components.SectionCard
import com.example.ui.screens.CardsScreen
import com.example.ui.screens.CareerScreen
import com.example.ui.screens.CertificateScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GlossaryScreen
import com.example.ui.screens.LabScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.PlanScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.ResourcesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SkillsScreen
import com.example.ui.screens.SyllabusScreen
import com.example.ui.screens.TopicDetailScreen
import com.example.ui.theme.LocalQaColors
import kotlinx.serialization.Serializable

@Serializable object HomeRoute
@Serializable object SyllabusRoute
@Serializable data class TopicDetailRoute(val topicId: String)
@Serializable data class PracticeRoute(val ch: Int? = null, val mode: String? = null)
@Serializable object CardsRoute
@Serializable object PlanRoute
@Serializable object ResourcesRoute
@Serializable object LabRoute
@Serializable object SkillsRoute
@Serializable object CertificateRoute
@Serializable object CareerRoute
@Serializable object GlossaryRoute
@Serializable object NotesRoute
@Serializable object ProgressRoute
@Serializable object SettingsRoute
@Serializable object MoreRoute

data class NavSection(
    val route: Any,
    val label: String,
    val emoji: String,
    val icon: ImageVector,
    val testTag: String
)

val ALL_SECTIONS = listOf(
    NavSection(HomeRoute, "الرئيسية", "🏠", Icons.Filled.Home, "nav_home"),
    NavSection(SyllabusRoute, "المنهج", "📚", Icons.Filled.MenuBook, "nav_syllabus"),
    NavSection(PlanRoute, "الخطة والجدول", "🗓️", Icons.Filled.CalendarMonth, "nav_plan"),
    NavSection(PracticeRoute(), "اختبارات", "📝", Icons.Filled.Quiz, "nav_practice"),
    NavSection(CardsRoute, "البطاقات", "🃏", Icons.Filled.Style, "nav_cards"),
    NavSection(ResourcesRoute, "المصادر", "🔗", Icons.Filled.School, "nav_resources"),
    NavSection(LabRoute, "المختبر التفاعلي", "🧪", Icons.Filled.Science, "nav_lab"),
    NavSection(SkillsRoute, "المهارات العملية", "🛠️", Icons.Filled.Build, "nav_skills"),
    NavSection(CertificateRoute, "شهادة ISTQB", "🎓", Icons.Filled.WorkspacePremium, "nav_certificate"),
    NavSection(CareerRoute, "المسار المهني", "🚀", Icons.Filled.RocketLaunch, "nav_career"),
    NavSection(GlossaryRoute, "القاموس", "📖", Icons.Filled.MenuBook, "nav_glossary"),
    NavSection(NotesRoute, "ملاحظاتي", "🗒️", Icons.Filled.StickyNote2, "nav_notes"),
    NavSection(ProgressRoute, "التقدم والإنجازات", "📈", Icons.Filled.TrendingUp, "nav_progress"),
    NavSection(SettingsRoute, "الإعدادات", "⚙️", Icons.Filled.Settings, "nav_settings")
)

private data class BottomTab(
    val route: Any,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val BOTTOM_TABS = listOf(
    BottomTab(HomeRoute, "الرئيسية", Icons.Filled.Home, Icons.Outlined.Home, "tab_home"),
    BottomTab(SyllabusRoute, "المنهج", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "tab_syllabus"),
    BottomTab(PracticeRoute(), "اختبارات", Icons.Filled.Quiz, Icons.Outlined.Quiz, "tab_practice"),
    BottomTab(CardsRoute, "البطاقات", Icons.Filled.Style, Icons.Outlined.Style, "tab_cards"),
    BottomTab(MoreRoute, "المزيد", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz, "tab_more")
)

fun NavHostController.navigateTopLevel(route: Any) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = false
        }
        launchSingleTop = true
    }
}

@Composable
fun QaAppShell(
    viewModel: QaViewModel,
    widthSizeClass: WindowWidthSizeClass
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val syncError by viewModel.syncError.collectAsStateWithLifecycle()
    val timer by viewModel.timerState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val qa = LocalQaColors.current

    LaunchedEffect(syncError) {
        val msg = syncError ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        viewModel.clearSyncError()
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination
    val isHome = currentDest?.hasRoute<HomeRoute>() == true
    val useRail = widthSizeClass != WindowWidthSizeClass.Compact

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (!useRail) {
                NavigationBar {
                    BOTTOM_TABS.forEach { tab ->
                        val selected = when (tab.route) {
                            is HomeRoute -> currentDest?.hasRoute<HomeRoute>() == true
                            is SyllabusRoute -> currentDest?.hasRoute<SyllabusRoute>() == true || currentDest?.hasRoute<TopicDetailRoute>() == true
                            is PracticeRoute -> currentDest?.hasRoute<PracticeRoute>() == true
                            is CardsRoute -> currentDest?.hasRoute<CardsRoute>() == true
                            is MoreRoute -> currentDest?.hasRoute<MoreRoute>() == true
                            else -> false
                        }
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigateTopLevel(tab.route) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (useRail) {
                NavigationRail(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    ALL_SECTIONS.forEach { sec ->
                        val selected = when (sec.route) {
                            is HomeRoute -> currentDest?.hasRoute<HomeRoute>() == true
                            is SyllabusRoute -> currentDest?.hasRoute<SyllabusRoute>() == true || currentDest?.hasRoute<TopicDetailRoute>() == true
                            is PracticeRoute -> currentDest?.hasRoute<PracticeRoute>() == true
                            is CardsRoute -> currentDest?.hasRoute<CardsRoute>() == true
                            is PlanRoute -> currentDest?.hasRoute<PlanRoute>() == true
                            is ResourcesRoute -> currentDest?.hasRoute<ResourcesRoute>() == true
                            is LabRoute -> currentDest?.hasRoute<LabRoute>() == true
                            is SkillsRoute -> currentDest?.hasRoute<SkillsRoute>() == true
                            is CertificateRoute -> currentDest?.hasRoute<CertificateRoute>() == true
                            is CareerRoute -> currentDest?.hasRoute<CareerRoute>() == true
                            is GlossaryRoute -> currentDest?.hasRoute<GlossaryRoute>() == true
                            is NotesRoute -> currentDest?.hasRoute<NotesRoute>() == true
                            is ProgressRoute -> currentDest?.hasRoute<ProgressRoute>() == true
                            is SettingsRoute -> currentDest?.hasRoute<SettingsRoute>() == true
                            else -> false
                        }
                        NavigationRailItem(
                            selected = selected,
                            onClick = { navController.navigateTopLevel(sec.route) },
                            icon = { Icon(sec.icon, contentDescription = sec.label) },
                            label = { Text(sec.label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            modifier = Modifier.testTag(sec.testTag)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter
            ) {
                when (val current = uiState) {
                    is UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = "جاري مزامنة تقدمك من السحابة…",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = qa.muted
                                )
                            }
                        }
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SectionCard(
                                borderColor = qa.bad,
                                modifier = Modifier.widthIn(max = 480.dp)
                            ) {
                                Text(
                                    text = "تعذّر تحميل بيانات حسابك",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = qa.bad
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = current.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = { viewModel.retryProfileInit() }) {
                                    Text("إعادة المحاولة")
                                }
                            }
                        }
                    }

                    is UiState.Success -> {
                        val state: AppState = current.data
                        NavHost(
                            navController = navController,
                            startDestination = HomeRoute,
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 840.dp)
                        ) {
                            composable<HomeRoute> {
                                DashboardScreen(
                                    state = state,
                                    timer = timer,
                                    viewModel = viewModel,
                                    onOpenTopic = { id -> navController.navigate(TopicDetailRoute(id)) },
                                    onOpenCards = { navController.navigateTopLevel(CardsRoute) },
                                    onOpenPractice = { ch, mode -> navController.navigate(PracticeRoute(ch, mode)) }
                                )
                            }
                            composable<SyllabusRoute> {
                                SyllabusScreen(
                                    state = state,
                                    viewModel = viewModel,
                                    onOpenTopic = { id -> navController.navigate(TopicDetailRoute(id)) }
                                )
                            }
                            composable<TopicDetailRoute> { entry ->
                                val args = entry.toRoute<TopicDetailRoute>()
                                TopicDetailScreen(
                                    topicId = args.topicId,
                                    state = state,
                                    viewModel = viewModel,
                                    onBack = { navController.popBackStack() },
                                    onOpenTopic = { id ->
                                        navController.navigate(TopicDetailRoute(id)) {
                                            popUpTo<TopicDetailRoute> { inclusive = true }
                                        }
                                    },
                                    onOpenPractice = { ch -> navController.navigate(PracticeRoute(ch = ch)) },
                                    onOpenCards = { navController.navigateTopLevel(CardsRoute) },
                                    onOpenLab = { navController.navigate(LabRoute) },
                                    onOpenResources = { navController.navigate(ResourcesRoute) }
                                )
                            }
                            composable<PracticeRoute> { entry ->
                                val args = entry.toRoute<PracticeRoute>()
                                PracticeScreen(
                                    initialCh = args.ch,
                                    initialMode = args.mode,
                                    state = state,
                                    viewModel = viewModel,
                                    onBackToMenu = {
                                        if (args.ch != null || args.mode != null) {
                                            navController.popBackStack()
                                        }
                                    }
                                )
                            }
                            composable<CardsRoute> {
                                CardsScreen(state = state, viewModel = viewModel)
                            }
                            composable<PlanRoute> {
                                PlanScreen(
                                    state = state,
                                    viewModel = viewModel,
                                    onOpenTopic = { id -> navController.navigate(TopicDetailRoute(id)) },
                                    onNavigateSection = { route -> navController.navigateTopLevel(route) }
                                )
                            }
                            composable<ResourcesRoute> {
                                ResourcesScreen(state = state, viewModel = viewModel)
                            }
                            composable<LabRoute> {
                                LabScreen()
                            }
                            composable<SkillsRoute> {
                                SkillsScreen()
                            }
                            composable<CertificateRoute> {
                                CertificateScreen(
                                    state = state,
                                    viewModel = viewModel,
                                    onStartExam = { navController.navigate(PracticeRoute(mode = "exam")) }
                                )
                            }
                            composable<CareerRoute> {
                                CareerScreen(state = state, viewModel = viewModel)
                            }
                            composable<GlossaryRoute> {
                                GlossaryScreen()
                            }
                            composable<NotesRoute> {
                                NotesScreen(state = state, viewModel = viewModel)
                            }
                            composable<ProgressRoute> {
                                ProgressScreen(state = state)
                            }
                            composable<SettingsRoute> {
                                SettingsScreen(state = state, viewModel = viewModel)
                            }
                            composable<MoreRoute> {
                                MoreScreen(
                                    onNavigate = { route -> navController.navigate(route) }
                                )
                            }
                        }
                    }
                }

                if (!isHome && timer.running) {
                    FloatingTimerPill(
                        timer = timer,
                        onClick = { navController.navigateTopLevel(HomeRoute) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
