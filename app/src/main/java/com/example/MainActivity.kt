package com.example

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import com.example.data.UserProfile
import com.example.ui.theme.SoftGrey
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.AddCourtView
import com.example.ui.CourtDetailView
import com.example.ui.FulbitoViewModel
import com.example.ui.LoginView
import com.example.ui.OwnerDashboardView
import com.example.ui.PlayerHomeView
import com.example.ui.PlayerMapView
import com.example.ui.ProfileView
import com.example.ui.RegisterOwnerView
import com.example.ui.RegisterPlayerView
import com.example.ui.RoleSelectorView
import com.example.ui.SplashView
import com.example.ui.theme.Asphalt
import com.example.ui.theme.Bleachers
import com.example.ui.theme.BorderGrey
import com.example.ui.theme.ChalkWhite
import com.example.ui.theme.DarkAsphalt
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextGrey

class MainActivity : ComponentActivity() {
    private val viewModel: FulbitoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MobileAds.initialize(this) { status ->
            Log.i("AdMob", "Initialization status: $status")
        }

        FirebaseApp.initializeApp(this)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            RecaptchaAppCheckProviderFactory.getInstance()
        )
        Log.i("AppCheck", "Firebase App Check initialized with reCAPTCHA Enterprise")
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: FulbitoViewModel) {
    val navController = rememberNavController()
    val userProfile by viewModel.userProfile.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Check if the current route is one of the main dashboard tabs
    val isPlayerTab = currentRoute in listOf("player_home", "player_map", "profile")
    val isOwnerTab = currentRoute in listOf("owner_dashboard", "profile")

    // Request location permission when profile is loaded
    if (profileIsLoaded(userProfile)) {
        val locationLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { }
        LaunchedEffect(Unit) {
            locationLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Request notification permission on Android 13+ when profile is loaded
    if (profileIsLoaded(userProfile) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notifLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { }
        LaunchedEffect(Unit) {
            notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Header toolbar with team badge, app title, and avatar
            if (isPlayerTab || isOwnerTab) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkAsphalt)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icono),
                        contentDescription = "Sale Fulbito Logo",
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillHeight
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "SALE FULBITO",
                        color = NeonGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    val roleTag = userProfile?.role ?: ""
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .border(2.dp, NeonGreen, shape = CircleShape)
                            .background(Bleachers, shape = CircleShape)
                            .clickable {
                                if (currentRoute != "profile") {
                                    navController.navigate("profile") {
                                        popUpTo("player_home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = SoftGrey,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (roleTag == "DUEÑO") "DUEÑO" else "JUGADOR",
                                color = SoftGrey,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (profileIsLoaded(userProfile)) {
                androidx.compose.foundation.layout.Column(modifier = Modifier.fillMaxWidth()) {
                    com.example.ui.AdMobBanner()
                    val selectedColor = NeonGreen
                    val unselectedColor = TextGrey
                    if (userProfile?.role == "JUGADOR") {
                        NavigationBar(
                            containerColor = DarkAsphalt,
                            modifier = Modifier
                                .background(DarkAsphalt)
                                .navigationBarsPadding(),
                            tonalElevation = 0.dp
                        ) {
                            NavigationBarItem(
                                selected = currentRoute == "player_home",
                                onClick = {
                                    navController.navigate("player_home") {
                                        popUpTo("player_home") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentRoute == "player_home") Icons.Filled.Home else Icons.Default.Home,
                                        contentDescription = "Home",
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text("CANCHAS", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = selectedColor,
                                    selectedTextColor = selectedColor,
                                    indicatorColor = selectedColor.copy(alpha = 0.15f),
                                    unselectedIconColor = unselectedColor,
                                    unselectedTextColor = unselectedColor
                                )
                            )

                            NavigationBarItem(
                                selected = currentRoute == "player_map",
                                onClick = {
                                    navController.navigate("player_map") {
                                        popUpTo("player_home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentRoute == "player_map") Icons.Filled.Map else Icons.Default.Map,
                                        contentDescription = "Mapa",
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text("MAPA", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = selectedColor,
                                    selectedTextColor = selectedColor,
                                    indicatorColor = selectedColor.copy(alpha = 0.15f),
                                    unselectedIconColor = unselectedColor,
                                    unselectedTextColor = unselectedColor
                                )
                            )

                            NavigationBarItem(
                                selected = currentRoute == "profile",
                                onClick = {
                                    navController.navigate("profile") {
                                        popUpTo("player_home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentRoute == "profile") Icons.Filled.Person else Icons.Default.Person,
                                        contentDescription = "Perfil",
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text("PERFIL", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = selectedColor,
                                    selectedTextColor = selectedColor,
                                    indicatorColor = selectedColor.copy(alpha = 0.15f),
                                    unselectedIconColor = unselectedColor,
                                    unselectedTextColor = unselectedColor
                                )
                            )
                        }
                    } else if (userProfile?.role == "DUEÑO") {
                        NavigationBar(
                            containerColor = DarkAsphalt,
                            modifier = Modifier
                                .background(DarkAsphalt)
                                .navigationBarsPadding(),
                            tonalElevation = 0.dp
                        ) {
                            NavigationBarItem(
                                selected = currentRoute == "owner_dashboard",
                                onClick = {
                                    navController.navigate("owner_dashboard") {
                                        popUpTo("owner_dashboard") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentRoute == "owner_dashboard") Icons.Filled.Dashboard else Icons.Default.Dashboard,
                                        contentDescription = "Dashboard",
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text("PANEL", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = selectedColor,
                                    selectedTextColor = selectedColor,
                                    indicatorColor = selectedColor.copy(alpha = 0.15f),
                                    unselectedIconColor = unselectedColor,
                                    unselectedTextColor = unselectedColor
                                )
                            )

                            NavigationBarItem(
                                selected = currentRoute == "profile",
                                onClick = {
                                    navController.navigate("profile") {
                                        popUpTo("owner_dashboard") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentRoute == "profile") Icons.Filled.Person else Icons.Default.Person,
                                        contentDescription = "Perfil",
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text("PERFIL", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = selectedColor,
                                    selectedTextColor = selectedColor,
                                    indicatorColor = selectedColor.copy(alpha = 0.15f),
                                    unselectedIconColor = unselectedColor,
                                    unselectedTextColor = unselectedColor
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable("welcome") {
                SplashView(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToRoleSelector = { navController.navigate("role_selection") },
                    onNavigateToHome = {
                        val destination = if (userProfile?.role == "DUEÑO") "owner_dashboard" else "player_home"
                        navController.navigate(destination) {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }

            composable("login") {
                LoginView(
                    onLoginSuccess = {
                        val destination = if (userProfile?.role == "DUEÑO") "owner_dashboard" else "player_home"
                        navController.navigate(destination) {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }

            composable("role_selection") {
                RoleSelectorView(
                    onNavigateToRegisterPlayer = { navController.navigate("register_player") },
                    onNavigateToRegisterOwner = { navController.navigate("register_owner") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("register_player") {
                RegisterPlayerView(
                    onRegisterSuccess = {
                        navController.navigate("player_home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }

            composable("register_owner") {
                RegisterOwnerView(
                    onRegisterSuccess = {
                        navController.navigate("owner_dashboard") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }

            composable("player_home") {
                PlayerHomeView(
                    onNavigateToDetail = { id -> navController.navigate("court_detail/$id") },
                    viewModel = viewModel,
                    onNavigateToMap = { navController.navigate("player_map") }
                )
            }

            composable("player_map") {
                PlayerMapView(
                    viewModel = viewModel,
                    onNavigateToDetail = { id -> navController.navigate("court_detail/$id") }
                )
            }

            composable("profile") {
                ProfileView(
                    viewModel = viewModel,
                    onLogoutSuccess = {
                        navController.navigate("welcome") {
                            popUpTo("player_home") { inclusive = true }
                            popUpTo("owner_dashboard") { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = "court_detail/{courtId}",
                arguments = listOf(navArgument("courtId") { type = NavType.IntType })
            ) { backStackEntry ->
                val courtId = backStackEntry.arguments?.getInt("courtId") ?: 1
                CourtDetailView(
                    courtId = courtId,
                    onBack = { navController.popBackStack() },
                    onBookingSuccess = {
                        navController.navigate("player_home") {
                            popUpTo("player_home") { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }

            composable("owner_dashboard") {
                OwnerDashboardView(
                    viewModel = viewModel,
                    onNavigateToAddCourt = { navController.navigate("add_court") }
                )
            }

            composable("add_court") {
                AddCourtView(
                    onSuccess = { navController.popBackStack() },
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}

private fun profileIsLoaded(profile: UserProfile?): Boolean {
    return profile != null && (profile.role == "JUGADOR" || profile.role == "DUEÑO")
}
