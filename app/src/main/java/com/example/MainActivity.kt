package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UiState
import com.example.ui.QaAppShell
import com.example.ui.QaViewModel
import com.example.ui.QaViewModelFactory
import com.example.ui.auth.AuthScreen
import com.example.ui.auth.attemptAutoSignIn
import com.example.ui.auth.authStateFlow
import com.example.ui.theme.QaPathTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                AppNavigation(
                    application = application,
                    widthSizeClass = windowSizeClass.widthSizeClass
                )
            }
        }
    }
}

@Composable
private fun AppNavigation(
    application: Application,
    widthSizeClass: WindowWidthSizeClass
) {
    val auth = Firebase.auth
    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    LaunchedEffect(Unit) {
        if (auth.currentUser == null) {
            attemptAutoSignIn(
                context = context,
                credentialManager = credentialManager,
                onAuthSuccess = {},
                onUnauthenticated = {},
                scope = scope
            )
        }
    }

    val user = currentUser
    if (user == null) {
        QaPathTheme(themeMode = "auto") {
            AuthScreen(onAuthSuccess = {})
        }
    } else {
        key(user.uid) {
            val viewModel: QaViewModel = viewModel(
                key = user.uid,
                factory = QaViewModelFactory(application, user.uid)
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val themeMode = (uiState as? UiState.Success)?.data?.profile?.theme ?: "auto"
            QaPathTheme(themeMode = themeMode) {
                QaAppShell(
                    viewModel = viewModel,
                    widthSizeClass = widthSizeClass
                )
            }
        }
    }
}
