package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import android.app.Activity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Booking
import com.example.data.Court
import com.example.data.UserProfile
import com.example.ui.AdState
import com.example.data.MatchSlot
import com.example.data.FreePass
import com.example.R
import com.example.ui.theme.Asphalt
import com.example.ui.theme.Bleachers
import com.example.ui.theme.BorderGrey
import com.example.ui.theme.ChalkWhite
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DarkAsphalt
import com.example.ui.theme.DarkGrey
import com.example.ui.theme.LightAsphalt
import com.example.ui.theme.MediumGrey
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SoftGrey
import com.example.ui.theme.TextGrey
import kotlin.math.roundToInt

@Composable
fun SplashView(
    onNavigateToLogin: () -> Unit,
    onNavigateToRoleSelector: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: FulbitoViewModel
) {
    val profile by viewModel.userProfile.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Asphalt)
    ) {
        // Aesthetic Potrero dark diagonal grass/fence canvas lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(MediumGrey, Asphalt),
                    center = Offset(size.width * 0.5f, size.height * 0.4f),
                    radius = size.width * 0.9f
                )
            )
            // Stencil fence drawings
            val gridStep = 80f
            for (i in 0..(size.width / gridStep).toInt() + 10) {
                drawLine(
                    color = BorderGrey.copy(alpha = 0.25f),
                    start = Offset(i * gridStep, 0f),
                    end = Offset(i * gridStep - size.height, size.height),
                    strokeWidth = 2f
                )
                drawLine(
                    color = BorderGrey.copy(alpha = 0.25f),
                    start = Offset(i * gridStep - size.height, 0f),
                    end = Offset(i * gridStep, size.height),
                    strokeWidth = 2f
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Identity Logo representation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = 40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_fondo_negro),
                    contentDescription = "Sale Fulbito Logo",
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .border(3.dp, NeonGreen, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "RESERVÁ TU CANCHA EN 30 SEGUNDOS",
                    style = MaterialTheme.typography.labelSmall,
                    color = ChalkWhite,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Slang slogan & Call to actions
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "LA PELOTA NO ESPERA.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = NeonGreen,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Juntá los pibes y caé a jugar. 🇦🇷⚽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SoftGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                BrutalistButton(
                    text = if (profile != null) "ENTRAR AL POTRERO" else "INICIAR SESIÓN",
                    onClick = {
                        if (profile != null) {
                            onNavigateToHome()
                        } else {
                            onNavigateToLogin()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("entrar_button")
                )

                if (profile == null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToRoleSelector() }
                            .border(2.dp, NeonGreen, shape = RoundedCornerShape(4.dp))
                            .background(Asphalt, shape = RoundedCornerShape(4.dp))
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "REGISTRARME",
                            color = NeonGreen,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "— O —",
                        color = TextGrey,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "CARGAR DATOS DE DEMO",
                        color = ChalkWhite,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !viewModel.isLoadingDemo.value) {
                                viewModel.seedDemoPlayer()
                            }
                            .border(2.dp, ChalkWhite, shape = RoundedCornerShape(4.dp))
                            .background(Asphalt, shape = RoundedCornerShape(4.dp))
                            .padding(vertical = 14.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "CARGAR DATOS DUEÑO DE CANCHA",
                        color = ChalkWhite,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !viewModel.isLoadingDemo.value) {
                                viewModel.seedDemoOwner()
                            }
                            .border(1.dp, BorderGrey, shape = RoundedCornerShape(4.dp))
                            .background(Asphalt, shape = RoundedCornerShape(4.dp))
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun LoginView(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: FulbitoViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginError by viewModel.loginError.collectAsState()
    val isLoggingIn by viewModel.isLoggingIn.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkAsphalt)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INICIAR SESIÓN",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonGreen,
                    fontWeight = FontWeight.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Asphalt)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BIENVENIDO DE VUELTA",
                style = MaterialTheme.typography.headlineLarge,
                color = NeonGreen,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(4.dp)
                    .background(NeonGreen)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.clearLoginError()
                },
                label = { Text("Email", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.clearLoginError()
                },
                label = { Text("Contraseña", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (loginError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = loginError,
                    color = DangerRed,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            BrutalistButton(
                text = if (isLoggingIn) "INICIANDO SESIÓN..." else "ENTRAR",
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.loginUser(email.trim(), password, onLoginSuccess)
                    } else {
                        viewModel.clearLoginError()
                    }
                },
                enabled = !isLoggingIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_submit")
            )
        }
    }
}

@Composable
fun RoleSelectorView(
    onNavigateToRegisterPlayer: () -> Unit,
    onNavigateToRegisterOwner: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkAsphalt)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "REGISTRO",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonGreen,
                    fontWeight = FontWeight.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Asphalt)
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿QUIÉN SOS EN LA CANCHA?",
                style = MaterialTheme.typography.headlineLarge,
                color = NeonGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(4.dp)
                    .background(NeonGreen)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Player Selection Card
            BrutalistCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("role_player_card"),
                borderColor = BorderGrey,
                backgroundColor = Bleachers,
                onClick = onNavigateToRegisterPlayer
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(DarkAsphalt, shape = CircleShape)
                            .border(2.dp, NeonGreen, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsSoccer,
                            contentDescription = "Player Icon",
                            tint = NeonGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "SOY JUGADOR",
                            style = MaterialTheme.typography.titleLarge,
                            color = ChalkWhite,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Quiero buscar canchas cerca, armar un partido y reservar.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGrey,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Owner Selection Card
            BrutalistCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("role_owner_card"),
                borderColor = BorderGrey,
                backgroundColor = Bleachers,
                onClick = onNavigateToRegisterOwner
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(DarkAsphalt, shape = CircleShape)
                            .border(2.dp, ChalkWhite, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Owner Icon",
                            tint = ChalkWhite,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "SOY DUEÑO",
                            style = MaterialTheme.typography.titleLarge,
                            color = ChalkWhite,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Quiero registrar mi complejo, gestionar turnos y ver mis ingresos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGrey,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterPlayerView(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: FulbitoViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("Delantero") }
    var level by remember { mutableStateOf("Amateur") }

    val positions = listOf("Arquero", "Defensor", "Mediocampista", "Delantero")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkAsphalt)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "NUEVO JUGADOR",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonGreen,
                    fontWeight = FontWeight.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Asphalt)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "LA PELOTA NO ESPERA. SUMATE.",
                style = MaterialTheme.typography.labelSmall,
                color = TextGrey,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("register_player_name")
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "POSICIÓN",
                style = MaterialTheme.typography.labelLarge,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                positions.forEach { pos ->
                    val isSelected = position == pos
                    Box(
                        modifier = Modifier
                            .clickable { position = pos }
                            .background(
                                if (isSelected) NeonGreen else Bleachers,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) NeonGreen else BorderGrey,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = pos.uppercase(),
                            color = if (isSelected) Asphalt else ChalkWhite,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "NIVEL",
                style = MaterialTheme.typography.labelLarge,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("Amateur", "Pro").forEach { lvl ->
                    val isSelected = level == lvl
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { level = lvl }
                            .background(
                                if (isSelected) NeonGreen else Bleachers,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                2.dp,
                                if (isSelected) NeonGreen else BorderGrey,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lvl.uppercase(),
                            color = if (isSelected) Asphalt else ChalkWhite,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            BrutalistButton(
                text = "CREAR CUENTA",
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        viewModel.registerPlayer(name, email, phone, password, position, level)
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_player_submit")
            )
        }
    }
}

@Composable
fun RegisterOwnerView(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: FulbitoViewModel
) {
    var complexName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cuit by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkAsphalt)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "REGISTRAR COMPLEJO",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonGreen,
                    fontWeight = FontWeight.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Asphalt)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "SUMA TUS CANCHAS Y EMPEZÁ A GESTIONAR.",
                style = MaterialTheme.typography.labelSmall,
                color = TextGrey,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = complexName,
                onValueChange = { complexName = it },
                label = { Text("Nombre del Complejo", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("register_owner_complex")
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección Física", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = cuit,
                onValueChange = { cuit = it },
                label = { Text("CUIT (sin guiones)", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email de Contacto", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono / WhatsApp", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            BrutalistButton(
                text = "REGISTRAR COMPLEJO",
                onClick = {
                    if (complexName.isNotBlank() && address.isNotBlank() && password.isNotBlank()) {
                        viewModel.registerOwner(complexName, email, phone, password, complexName, address, cuit)
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_owner_submit")
            )
        }
    }
}

@Composable
fun PlayerHomeView(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: FulbitoViewModel,
    onNavigateToMap: () -> Unit
) {
    val courts by viewModel.courts.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    val myUpcomingBookings = bookings.filter { it.isMyBooking }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Asphalt)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header Logo Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_fondo_negro),
                contentDescription = "Sale Fulbito Logo",
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillHeight
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Bleachers)
                    .border(1.dp, BorderGrey, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = NeonGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Hero Section "¿SALE FULBITO HOY?"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(Bleachers, shape = RoundedCornerShape(8.dp))
                .border(2.dp, NeonGreen, shape = RoundedCornerShape(8.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                StencilBadge(
                    text = "ALERTA DE PARTIDO",
                    borderColor = NeonGreen,
                    textColor = Asphalt,
                    backgroundColor = NeonGreen,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "¿SALE FULBITO\nHOY?",
                    style = MaterialTheme.typography.displayLarge,
                    color = ChalkWhite,
                    lineHeight = 40.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = if (courts.isNotEmpty()) "Elegí tu cancha favorita y asegurá tu turno." else "Sumate al fútbol en Salta. Reservá tu cancha ahora.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextGrey,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                if (courts.isNotEmpty()) {
                    BrutalistButton(
                        text = "RESERVÁ YA",
                        onClick = {
                            onNavigateToDetail(courts.first().id)
                        },
                        modifier = Modifier.testTag("hero_reserve_now")
                    )
                }
            }
        }

        // Scoreboard next matches section
        Text(
            text = "TUS PRÓXIMOS PARTIDOS",
            style = MaterialTheme.typography.headlineLarge,
            color = ChalkWhite,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (myUpcomingBookings.isNotEmpty()) {
            myUpcomingBookings.forEach { booking ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .background(Bleachers, shape = RoundedCornerShape(4.dp))
                        .border(2.dp, BorderGrey, shape = RoundedCornerShape(4.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                StencilBadge(
                                    text = booking.status,
                                    borderColor = NeonGreen,
                                    textColor = NeonGreen,
                                    backgroundColor = Asphalt,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Text(
                                    text = "${booking.courtType} - ${booking.courtName}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = ChalkWhite
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Location",
                                        tint = TextGrey,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = booking.teamName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextGrey
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = booking.date,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextGrey
                                )
                                Text(
                                    text = booking.timeSlot,
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = NeonGreen
                                )
                            }
                        }

                        DashedDivider(color = BorderGrey)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = "Players",
                                    tint = NeonGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "+6 pibes listos",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SoftGrey
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clickable { onNavigateToDetail(booking.courtId) }
                                    .border(1.dp, BorderGrey, shape = RoundedCornerShape(2.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "DETALLES",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChalkWhite
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .border(1.dp, BorderGrey, shape = RoundedCornerShape(4.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NO TENÉS PARTIDOS PRÓXIMOS. ¡RESERVÁ UNA CANCHA!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrey,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Near courts section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CANCHAS CERCANAS",
                style = MaterialTheme.typography.headlineLarge,
                color = ChalkWhite
            )

            IconButton(onClick = onNavigateToMap) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Map View",
                    tint = NeonGreen,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Horizontal scrolling courts list
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            courts.forEach { court ->
                BrutalistCard(
                    modifier = Modifier
                        .width(260.dp)
                        .clickable { onNavigateToDetail(court.id) }
                        .testTag("court_card_${court.id}"),
                    borderColor = if (court.isFavorite) NeonGreen else BorderGrey,
                    backgroundColor = Bleachers
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = court.name.uppercase(),
                                style = MaterialTheme.typography.titleLarge,
                                color = ChalkWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "★",
                                    color = NeonGreen,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = court.rating.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChalkWhite
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = TextGrey,
                                    modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${court.neighborhood} • ${court.type}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextGrey
                            )
                        }

                        Text(
                            text = court.surface.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonGreen,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        DashedDivider(color = BorderGrey)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "DESDE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextGrey
                                )
                                Text(
                                    text = "${court.pricePerHour.roundToInt()} Fichas/hr 🪙",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = NeonGreen
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Go to details",
                                tint = NeonGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourtDetailView(
    courtId: Int,
    onBack: () -> Unit,
    onBookingSuccess: () -> Unit,
    viewModel: FulbitoViewModel
) {
    val courts by viewModel.courts.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedSlot by viewModel.selectedSlot.collectAsState()

    val court = courts.find { it.id == courtId } ?: return

    val days = remember { viewModel.getUpcomingDays() }
    val context = LocalContext.current

    val slots = remember(court.pricePerHour) {
        val availableSlots = listOf("18:00", "19:00", "20:00", "21:00", "22:00", "23:00")
        val now = Calendar.getInstance()
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        availableSlots.map { time ->
            val hour = time.substringBefore(":").toInt()
            val status = when {
                hour < currentHour -> "Ocupado"
                hour == 21 -> "Disponible"
                else -> "${court.pricePerHour.toInt()} Fichas"
            }
            time to status
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkAsphalt)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "RESERVAR CANCHA",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonGreen,
                    fontWeight = FontWeight.Black
                )
            }
        },
        bottomBar = {
            val context = LocalContext.current
            val activity = context as? Activity
            val interstitialManager = remember { AdMobInterstitialManager(context) }

            DisposableEffect(Unit) {
                onDispose { interstitialManager.cleanup() }
            }

            // Sticky confirm action bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Bleachers)
                    .border(2.dp, BorderGrey)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SEÑA REQUERIDA",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextGrey
                        )
                        val deposit = (court.pricePerHour * 0.1).toInt()
                        Text(
                            text = "$deposit Fichas 🪙",
                            style = MaterialTheme.typography.headlineLarge,
                            color = ChalkWhite
                        )
                    }

                    BrutalistButton(
                        text = "SEÑAR CON FICHAS",
                        onClick = {
                            val depositAmount = court.pricePerHour * 0.1
                            val balance = viewModel.userProfile.value?.balance ?: 0.0
                            if (balance < depositAmount) {
                                android.widget.Toast.makeText(context, "Fichas insuficientes. ¡Mirá sponsors para cargar!", android.widget.Toast.LENGTH_LONG).show()
                            } else {
                                val act = activity
                                if (act != null) {
                                    interstitialManager.showAd(act) {
                                        viewModel.confirmBooking(
                                            courtId = court.id,
                                            courtName = court.name,
                                            courtType = court.type,
                                            price = court.pricePerHour
                                        )
                                        viewModel.deductBalance(depositAmount)
                                        onBookingSuccess()
                                    }
                                } else {
                                    viewModel.confirmBooking(
                                        courtId = court.id,
                                        courtName = court.name,
                                        courtType = court.type,
                                        price = court.pricePerHour
                                    )
                                    viewModel.deductBalance(depositAmount)
                                    onBookingSuccess()
                                }
                            }
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .testTag("confirm_booking_button"),
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Asphalt
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Asphalt)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // Court Header Info
            Text(
                text = court.name.uppercase(),
                style = MaterialTheme.typography.displayLarge,
                color = ChalkWhite
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = TextGrey,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = court.address.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGrey
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StencilBadge(
                    text = court.type,
                    borderColor = NeonGreen,
                    textColor = Asphalt,
                    backgroundColor = NeonGreen
                )
                StencilBadge(
                    text = court.surface,
                    borderColor = BorderGrey,
                    textColor = ChalkWhite,
                    backgroundColor = Bleachers
                )
            }

            // Navigate to court button (geo URI - works with any map app)
            if (court.latitude != 0.0 && court.longitude != 0.0) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Bleachers)
                        .border(1.dp, BorderGrey)
                        .clickable {
                            val uri = android.net.Uri.parse("geo:${court.latitude},${court.longitude}?q=${court.latitude},${court.longitude}(${court.name})")
                            val mapIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage(null) // shows app chooser
                            context.startActivity(mapIntent)
                        }
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CÓMO LLEGAR",
                            color = NeonGreen,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FECHA Select
            Text(
                text = "FECHA",
                style = MaterialTheme.typography.labelLarge,
                color = TextGrey,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
                    val isSelected = selectedDate == day
                    val isPast = false
                    Box(
                        modifier = Modifier
                            .size(75.dp, 85.dp)
                            .clickable(enabled = !isPast) { viewModel.selectDate(day) }
                            .background(
                                if (isSelected) NeonGreen else Bleachers,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                2.dp,
                                if (isSelected) NeonGreen else BorderGrey,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = day.split(" ")[0].uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Asphalt else TextGrey,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = day.split(" ")[1],
                                style = MaterialTheme.typography.headlineLarge,
                                color = if (isSelected) Asphalt else ChalkWhite,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // HORARIOS DISPONIBLES Select
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "HORARIOS DISPONIBLES",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextGrey
                )
                Text(
                    text = "TARDE / NOCHE",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                slots.chunked(3).forEach { rowSlots ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowSlots.forEach { (time, priceOrStatus) ->
                            val isOcupado = priceOrStatus == "Ocupado"
                            val isSelected = selectedSlot == time && !isOcupado

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .clickable(enabled = !isOcupado) { viewModel.selectSlot(time) }
                                    .background(
                                        if (isSelected) NeonGreen else if (isOcupado) DarkAsphalt else Bleachers,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) NeonGreen else BorderGrey,
                                        shape = RoundedCornerShape(4.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = time,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (isSelected) Asphalt else if (isOcupado) TextGrey.copy(alpha = 0.5f) else ChalkWhite,
                                        textDecoration = if (isOcupado) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                    Text(
                                        text = if (isSelected) "SELECCIONADO" else priceOrStatus.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) Asphalt else if (isOcupado) DangerRed else TextGrey,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = TextGrey,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Turnos de 60 minutos. Se requiere seña de 1.000 Fichas 🪙.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrey,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            DashedDivider(color = BorderGrey)
            Spacer(modifier = Modifier.height(16.dp))

            // --- SECCIÓN OPINIONES DE LOS PIBES ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OPINIONES DE LOS PIBES",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextGrey
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "★",
                        color = NeonGreen,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = court.rating.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = ChalkWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val reviewsList by viewModel.courtReviews.collectAsState()
            val courtReviews = reviewsList.filter { it.courtId == court.id }

            if (courtReviews.isEmpty()) {
                Text(
                    text = "No hay opiniones de esta cancha todavía. ¡Sé el primero en opinar!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftGrey,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    courtReviews.forEach { review ->
                        BrutalistCard(
                            modifier = Modifier.fillMaxWidth(),
                            borderColor = BorderGrey,
                            backgroundColor = Bleachers
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = review.author.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NeonGreen,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = review.date,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextGrey,
                                        fontSize = 10.sp
                                    )
                                }
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    for (i in 1..5) {
                                        Text(
                                            text = if (i <= review.rating) "★" else "☆",
                                            color = if (i <= review.rating) NeonGreen else BorderGrey,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                Text(
                                    text = review.comment,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ChalkWhite
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            DashedDivider(color = BorderGrey)
            Spacer(modifier = Modifier.height(16.dp))

            // --- FORMULARIO DEJAR OPINIÓN ---
            Text(
                text = "DEJÁ TU COMENTARIO Y NOTA ⭐",
                style = MaterialTheme.typography.labelLarge,
                color = TextGrey,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            var authorNameInput by remember { mutableStateOf(viewModel.userProfile.value?.fullName ?: "") }
            var ratingSelection by remember { mutableStateOf(0) }
            var commentInput by remember { mutableStateOf("") }

            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen,
                backgroundColor = Bleachers
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Nota:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ChalkWhite
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            for (i in 1..5) {
                                Text(
                                    text = if (i <= ratingSelection) "★" else "☆",
                                    color = if (i <= ratingSelection) NeonGreen else BorderGrey,
                                    fontSize = 28.sp,
                                    modifier = Modifier.clickable { ratingSelection = i }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = authorNameInput,
                        onValueChange = { authorNameInput = it },
                        label = { Text("Apodo/Nombre") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ChalkWhite,
                            unfocusedTextColor = ChalkWhite,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = BorderGrey,
                            focusedLabelColor = NeonGreen,
                            unfocusedLabelColor = TextGrey
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        label = { Text("¿Qué tal es la cancha, las luces, el buffet?") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ChalkWhite,
                            unfocusedTextColor = ChalkWhite,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = BorderGrey,
                            focusedLabelColor = NeonGreen,
                            unfocusedLabelColor = TextGrey
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    BrutalistButton(
                        text = "PUBLICAR COMENTARIO ⚽",
                        onClick = {
                            val author = if (authorNameInput.isBlank()) "Anónimo" else authorNameInput
                            if (commentInput.isNotBlank()) {
                                viewModel.addCourtReview(court.id, author, ratingSelection, commentInput)
                                commentInput = ""
                                ratingSelection = 5
                                android.widget.Toast.makeText(context, "¡Gracias por dejar tu opinión!", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                android.widget.Toast.makeText(context, "Escribí un comentario primero.", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = NeonGreen,
                        contentColor = Asphalt
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerMapView(
    viewModel: FulbitoViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val courts by viewModel.courts.collectAsState()
    val matchSlots by viewModel.matchSlots.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val currentUserName = userProfile?.fullName ?: ""
    val currentUserBalance = userProfile?.balance ?: 0.0

    var mapMode by remember { mutableStateOf("COURTS") } // "COURTS" or "MATCHES"
    var selectedMapCourt by remember { mutableStateOf(0) } // Default court ID
    var selectedMapMatch by remember { mutableStateOf(0) } // Default match ID
    var searchQuery by remember { mutableStateOf("") }
    var showJoinOptionDialog by remember { mutableStateOf<MatchSlot?>(null) }

    // Dialog state for publishing a match slot
    var showCreateMatchDialog by remember { mutableStateOf(false) }
    var formTeamName by remember { mutableStateOf("") }
    var formCourtId by remember { mutableStateOf(0) }
    var formMatchType by remember { mutableStateOf("F5") }
    var formSlotsMissing by remember { mutableStateOf(1) }
    var formRoleNeeded by remember { mutableStateOf("Arquero") }
    var formPricePerPlayer by remember { mutableStateOf("") }

    // Status message for feedback
    var statusMessage by remember { mutableStateOf("") }
    var statusType by remember { mutableStateOf("SUCCESS") } // "SUCCESS" or "ERROR"

    val highlightedCourt = courts.find { it.id == selectedMapCourt }
    val highlightedMatch = matchSlots.find { it.id == selectedMapMatch }

    val filteredCourts = if (searchQuery.isEmpty()) courts else {
        courts.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.neighborhood.contains(searchQuery, ignoreCase = true)
        }
    }

    val filteredMatches = if (searchQuery.isEmpty()) matchSlots else {
        matchSlots.filter {
            it.teamName.contains(searchQuery, ignoreCase = true) ||
            it.courtName.contains(searchQuery, ignoreCase = true) ||
            it.neighborhood.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAsphalt)
    ) {
        // Real OpenStreetMap integration
        Box(modifier = Modifier.fillMaxSize()) {
            OpenStreetMapView(
                courts = if (mapMode == "COURTS") filteredCourts else emptyList(),
                onCourtClick = { court ->
                    selectedMapCourt = court.id
                    onNavigateToDetail(court.id)
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // --- TOP SELECTOR & SEARCH BAR ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            // Salta Location Badge & MapMode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Salta Badge
                Box(
                    modifier = Modifier
                        .background(Asphalt, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, NeonGreen, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val detectedCity by viewModel.detectedCity.collectAsState()
                        Text(
                            text = detectedCity.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // MapMode Switcher
                Row(
                    modifier = Modifier
                        .background(Bleachers, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, BorderGrey, shape = RoundedCornerShape(4.dp))
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(if (mapMode == "COURTS") NeonGreen else Color.Transparent, shape = RoundedCornerShape(2.dp))
                            .clickable { mapMode = "COURTS" }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "CANCHAS",
                            color = if (mapMode == "COURTS") Asphalt else TextGrey,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(if (mapMode == "MATCHES") NeonGreen else Color.Transparent, shape = RoundedCornerShape(2.dp))
                            .clickable { mapMode = "MATCHES" }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "EQUIPOS/VACANTES",
                            color = if (mapMode == "MATCHES") Asphalt else TextGrey,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Search Bar Input
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Bleachers, shape = RoundedCornerShape(4.dp))
                    .border(2.dp, BorderGrey, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = null,
                        tint = SoftGrey,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = if (mapMode == "COURTS") "Buscar complejo o barrio en Salta..." else "Buscar equipo o puesto...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextGrey
                            )
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = ChalkWhite),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // --- FLOATING CREATE MATCH SLOT BUTTON ---
        if (mapMode == "MATCHES") {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 260.dp, end = 20.dp)
            ) {
                IconButton(
                    onClick = { showCreateMatchDialog = true },
                    modifier = Modifier
                        .size(56.dp)
                        .background(NeonGreen, shape = RoundedCornerShape(8.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Publicar Vacante",
                        tint = Asphalt,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // --- FEEDBACK TOAST STATUS BADGE ---
        if (statusMessage.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 110.dp, start = 32.dp, end = 32.dp)
                    .clickable { statusMessage = "" }
            ) {
                StencilBadge(
                    text = statusMessage.uppercase(),
                    borderColor = if (statusType == "SUCCESS") NeonGreen else DangerRed,
                    textColor = if (statusType == "SUCCESS") Asphalt else ChalkWhite,
                    backgroundColor = if (statusType == "SUCCESS") NeonGreen else DangerRed
                )
            }
        }

        // --- BOTTOM CARD PANELS (DETAILS) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
        ) {
            if (mapMode == "COURTS") {
                highlightedCourt?.let { court ->
                    BrutalistCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetail(court.id) },
                        borderColor = NeonGreen,
                        backgroundColor = Bleachers
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    StencilBadge(
                                        text = "CANCHA DE SALTA",
                                        borderColor = NeonGreen,
                                        textColor = Asphalt,
                                        backgroundColor = NeonGreen,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                    Text(
                                        text = court.name.uppercase(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = ChalkWhite
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = TextGrey,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${court.neighborhood} • ${court.type}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextGrey
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "★",
                                        color = NeonGreen,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = court.rating.toString(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = ChalkWhite
                                    )
                                }
                            }

                            DashedDivider(color = BorderGrey)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "TURNO DISPONIBLE HOY",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextGrey
                                    )
                                    Text(
                                        text = "${court.pricePerHour.roundToInt()} Fichas/hora 🪙",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = NeonGreen
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(NeonGreen, shape = RoundedCornerShape(2.dp))
                                        .border(1.dp, Color.Black, shape = RoundedCornerShape(2.dp))
                                        .clickable { onNavigateToDetail(court.id) }
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "RESERVAR CANCHA",
                                        color = Asphalt,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // MATCHES BOTTOM DETAIL PANEL
                highlightedMatch?.let { match ->
                    BrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = NeonGreen,
                        backgroundColor = Bleachers
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        StencilBadge(
                                            text = "${match.matchType} • VACANTES: ${match.slotsMissing}",
                                            borderColor = if (match.slotsMissing > 0) NeonGreen else BorderGrey,
                                            textColor = if (match.slotsMissing > 0) Asphalt else TextGrey,
                                            backgroundColor = if (match.slotsMissing > 0) NeonGreen else BorderGrey,
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        )
                                        if (match.slotsMissing == 0) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            StencilBadge(
                                                text = "COMPLETO",
                                                borderColor = BorderGrey,
                                                textColor = SoftGrey,
                                                backgroundColor = Asphalt,
                                                modifier = Modifier.padding(bottom = 6.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = match.teamName.uppercase(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = ChalkWhite
                                    )
                                    Text(
                                        text = "${match.courtName} (${match.neighborhood})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextGrey
                                    )
                                    Text(
                                        text = "Fecha: ${match.date} • Horario: ${match.timeSlot}hs",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = NeonGreen
                                    )
                                }
                            }

                            // Roster description
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Asphalt, shape = RoundedCornerShape(2.dp))
                                    .border(1.dp, BorderGrey, shape = RoundedCornerShape(2.dp))
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "ROSTER ACTUAL:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextGrey,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = match.joinedPlayers,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ChalkWhite,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            DashedDivider(color = BorderGrey)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "PAGO DIVIDIDO (AL INGRESAR)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextGrey
                                    )
                                    Text(
                                        text = "${match.pricePerPlayer.roundToInt()} Fichas c/u 🪙",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = NeonGreen
                                    )
                                }

                                if (match.slotsMissing > 0) {
                                    val isAlreadyIn = match.joinedPlayers.contains(currentUserName)
                                    val hasFreePass by viewModel.hasWeeklyFreePass.collectAsState()
                                    Box(
                                        modifier = Modifier
                                            .background(if (isAlreadyIn) BorderGrey else NeonGreen, shape = RoundedCornerShape(2.dp))
                                            .border(1.dp, Color.Black, shape = RoundedCornerShape(2.dp))
                                            .clickable {
                                                if (isAlreadyIn) {
                                                    statusMessage = "Ya formas parte de este equipo"
                                                    statusType = "ERROR"
                                                } else if (hasFreePass) {
                                                    showJoinOptionDialog = match
                                                } else if (currentUserBalance < match.pricePerPlayer) {
                                                    statusMessage = "Fichas insuficientes. ¡Cargá mirando videos sponsors!"
                                                    statusType = "ERROR"
                                                } else {
                                                    viewModel.joinMatchSlot(match, currentUserName, useFreePass = false)
                                                    statusMessage = "¡Te uniste! Se canjearon ${match.pricePerPlayer.roundToInt()} Fichas 🪙."
                                                    statusType = "SUCCESS"
                                                }
                                            }
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = if (isAlreadyIn) "ADENTRO" else "UNIRSE Y DIVIDIR",
                                            color = Asphalt,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .background(BorderGrey, shape = RoundedCornerShape(2.dp))
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "ROSTER COMPLETO",
                                            color = ChalkWhite,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- CREAR EQUIPO / PUBLICAR VACANTE POPUP DIALOG FORM ---
        if (showCreateMatchDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .pointerInput(Unit) {} // blocking underlying clicks
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                BrutalistCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    borderColor = NeonGreen,
                    backgroundColor = Bleachers
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PUBLICAR VACANTE",
                                style = MaterialTheme.typography.headlineLarge,
                                color = NeonGreen
                            )
                            IconButton(onClick = { showCreateMatchDialog = false }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Cerrar",
                                    tint = ChalkWhite,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .rotate(45f)
                                )
                            }
                        }

                        Text(
                            text = "Buscá jugadores de Salta para completar tu equipo en 30 segundos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGrey,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Team Name input
                        Text(
                            text = "NOMBRE DEL EQUIPO",
                            style = MaterialTheme.typography.labelSmall,
                            color = ChalkWhite,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = formTeamName,
                            onValueChange = { formTeamName = it },
                            placeholder = { Text("Ej. Los Changos de Grand Bourg", color = TextGrey) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = ChalkWhite,
                                unfocusedTextColor = ChalkWhite,
                                focusedBorderColor = NeonGreen,
                                unfocusedBorderColor = BorderGrey,
                                focusedContainerColor = Asphalt,
                                unfocusedContainerColor = Asphalt
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        // Complex selector
                        Text(
                            text = "COMPLEJO EN SALTA",
                            style = MaterialTheme.typography.labelSmall,
                            color = ChalkWhite,
                            fontWeight = FontWeight.Bold
                        )
                        var expandedCourts by remember { mutableStateOf(false) }
                        val selectedCourtName = courts.find { it.id == formCourtId }?.name ?: "Seleccionar complejo"
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Asphalt)
                                .border(1.dp, BorderGrey)
                                .clickable { expandedCourts = true }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = selectedCourtName, color = ChalkWhite, style = MaterialTheme.typography.bodyMedium)
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = NeonGreen)
                            }

                            DropdownMenu(
                                expanded = expandedCourts,
                                onDismissRequest = { expandedCourts = false },
                                modifier = Modifier.background(Bleachers)
                            ) {
                                courts.forEach { court ->
                                    DropdownMenuItem(
                                        text = { Text(court.name, color = ChalkWhite) },
                                        onClick = {
                                            formCourtId = court.id
                                            expandedCourts = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Match Type & Missing players
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "TIPO DE CANCHA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChalkWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                var expandedTypes by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Asphalt)
                                        .border(1.dp, BorderGrey)
                                        .clickable { expandedTypes = true }
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = formMatchType, color = ChalkWhite, style = MaterialTheme.typography.bodyMedium)
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = NeonGreen)
                                    }
                                    DropdownMenu(
                                        expanded = expandedTypes,
                                        onDismissRequest = { expandedTypes = false },
                                        modifier = Modifier.background(Bleachers)
                                    ) {
                                        listOf("F5", "F7", "F9").forEach { type ->
                                            DropdownMenuItem(
                                                text = { Text(type, color = ChalkWhite) },
                                                onClick = {
                                                    formMatchType = type
                                                    expandedTypes = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "VACANTES",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChalkWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                var expandedSlots by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Asphalt)
                                        .border(1.dp, BorderGrey)
                                        .clickable { expandedSlots = true }
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = formSlotsMissing.toString(), color = ChalkWhite, style = MaterialTheme.typography.bodyMedium)
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = NeonGreen)
                                    }
                                    DropdownMenu(
                                        expanded = expandedSlots,
                                        onDismissRequest = { expandedSlots = false },
                                        modifier = Modifier.background(Bleachers)
                                    ) {
                                        listOf(1, 2, 3, 4, 5).forEach { slotNum ->
                                            DropdownMenuItem(
                                                text = { Text(slotNum.toString(), color = ChalkWhite) },
                                                onClick = {
                                                    formSlotsMissing = slotNum
                                                    expandedSlots = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Role & Price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "PUESTO REQUERIDO",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChalkWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                var expandedRoles by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Asphalt)
                                        .border(1.dp, BorderGrey)
                                        .clickable { expandedRoles = true }
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = formRoleNeeded, color = ChalkWhite, style = MaterialTheme.typography.bodyMedium)
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = NeonGreen)
                                    }
                                    DropdownMenu(
                                        expanded = expandedRoles,
                                        onDismissRequest = { expandedRoles = false },
                                        modifier = Modifier.background(Bleachers)
                                    ) {
                                        listOf("Arquero", "Defensor", "Mediocampista", "Delantero", "Cualquiera").forEach { role ->
                                            DropdownMenuItem(
                                                text = { Text(role, color = ChalkWhite) },
                                                onClick = {
                                                    formRoleNeeded = role
                                                    expandedRoles = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "COSTO POR CABEZA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChalkWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                OutlinedTextField(
                                    value = formPricePerPlayer,
                                    onValueChange = { formPricePerPlayer = it },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = ChalkWhite,
                                        unfocusedTextColor = ChalkWhite,
                                        focusedBorderColor = NeonGreen,
                                        unfocusedBorderColor = BorderGrey,
                                        focusedContainerColor = Asphalt,
                                        unfocusedContainerColor = Asphalt
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Submit Button
                        BrutalistButton(
                            text = "PUBLICAR VACANTE",
                            onClick = {
                                if (formTeamName.isBlank()) {
                                    statusMessage = "Ingresá un nombre de equipo"
                                    statusType = "ERROR"
                                } else {
                                    val courtSelected = courts.find { it.id == formCourtId }
                                    viewModel.publishMatchSlot(
                                        courtId = formCourtId,
                                        courtName = courtSelected?.name ?: "",
                                        neighborhood = courtSelected?.neighborhood ?: "",
                                        teamName = formTeamName,
                                        matchType = formMatchType,
                                        slotsMissing = formSlotsMissing,
                                        roleNeeded = formRoleNeeded,
                                        pricePerPlayer = formPricePerPlayer.toDoubleOrNull() ?: 0.0
                                    )
                                    showCreateMatchDialog = false
                                    statusMessage = "¡Equipo publicado! Se busca ${formRoleNeeded}."
                                    statusType = "SUCCESS"
                                    formTeamName = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // --- DIALOGO CANJE PASE GRATIS SEMANAL EN VACANTE ---
        if (showJoinOptionDialog != null) {
            val matchToJoin = showJoinOptionDialog!!
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .pointerInput(Unit) {}
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    borderColor = NeonGreen,
                    backgroundColor = Bleachers
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "CANJE DE ENTRADA GRATUITA 🎟️",
                            style = MaterialTheme.typography.headlineMedium,
                            color = NeonGreen,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Tenés un Pase Gratis Semanal disponible. ¿Querés usarlo para entrar gratis a ${matchToJoin.teamName} o preferís pagar con Fichas?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ChalkWhite,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BrutalistButton(
                            text = "USAR PASE GRATIS SEMANAL (🎟️)",
                            onClick = {
                                viewModel.joinMatchSlot(matchToJoin, currentUserName, useFreePass = true)
                                statusMessage = "¡Listo! Te uniste gratis usando tu Pase Semanal. 🎟️"
                                statusType = "SUCCESS"
                                showJoinOptionDialog = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = NeonGreen,
                            contentColor = Asphalt
                        )
                        BrutalistButton(
                            text = "PAGAR ${matchToJoin.pricePerPlayer.roundToInt()} FICHAS 🪙",
                            onClick = {
                                if (currentUserBalance < matchToJoin.pricePerPlayer) {
                                    statusMessage = "Fichas insuficientes para pagar. Usá tu Pase Gratis o mirá anuncios."
                                    statusType = "ERROR"
                                } else {
                                    viewModel.joinMatchSlot(matchToJoin, currentUserName, useFreePass = false)
                                    statusMessage = "Te uniste al picadito. Se canjearon ${matchToJoin.pricePerPlayer.roundToInt()} Fichas."
                                    statusType = "SUCCESS"
                                }
                                showJoinOptionDialog = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Bleachers,
                            contentColor = ChalkWhite
                        )
                        BrutalistButton(
                            text = "CANCELAR",
                            onClick = { showJoinOptionDialog = null },
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Asphalt,
                            contentColor = DangerRed
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MatchSpotPin(teamName: String, missingInfo: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .background(
                    if (isSelected) NeonGreen else Bleachers,
                    shape = RoundedCornerShape(2.dp)
                )
                .border(
                    2.dp,
                    if (isSelected) Color.Black else BorderGrey,
                    shape = RoundedCornerShape(2.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = teamName.take(12).uppercase(),
                    color = if (isSelected) Asphalt else ChalkWhite,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = missingInfo,
                    color = if (isSelected) Asphalt else NeonGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(if (isSelected) NeonGreen else BorderGrey, shape = CircleShape)
                .border(1.dp, Color.Black, shape = CircleShape)
        )
    }
}

@Composable
fun PriceTagPin(price: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .background(
                    if (isSelected) NeonGreen else Bleachers,
                    shape = RoundedCornerShape(2.dp)
                )
                .border(
                    2.dp,
                    if (isSelected) Color.Black else BorderGrey,
                    shape = RoundedCornerShape(2.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = price,
                color = if (isSelected) Asphalt else NeonGreen,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black
            )
        }
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(if (isSelected) NeonGreen else BorderGrey, shape = CircleShape)
                .border(1.dp, Color.Black, shape = CircleShape)
        )
    }
}

@Composable
fun OwnerDashboardView(
    viewModel: FulbitoViewModel,
    onNavigateToAddCourt: () -> Unit
) {
    val bookings by viewModel.bookings.collectAsState()
    val courts by viewModel.courts.collectAsState()

    // Filter out some bookings representing owner's complex bookings (isMyBooking = false)
    val ownerBookings = bookings.filter { !it.isMyBooking }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Asphalt)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_fondo_negro),
                contentDescription = "Sale Fulbito Logo",
                modifier = Modifier
                    .height(32.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillHeight
            )
            
            StencilBadge(
                text = "PANEL DUEÑO",
                borderColor = BorderGrey,
                textColor = NeonGreen,
                backgroundColor = Color.Black
            )
        }

        Text(
            text = "HOY EN LA CANCHA",
            style = MaterialTheme.typography.displayLarge,
            color = NeonGreen,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        val detectedCity by viewModel.detectedCity.collectAsState()
        val formattedDate = remember {
            java.text.SimpleDateFormat("dd/MM/yyyy • HH:mm", java.util.Locale.forLanguageTag("es-AR")).format(java.util.Calendar.getInstance().time)
        }
        Text(
            text = "RESUMEN EN VIVO • ${detectedCity.uppercase()} • $formattedDate HS",
            style = MaterialTheme.typography.labelSmall,
            color = TextGrey,
            modifier = Modifier.padding(bottom = 24.dp)
        )

            // Bento Grid Stat Items
            val todayBookings = ownerBookings.size
            val totalSlots = courts.size * 6
            val occupancy = if (totalSlots > 0) (todayBookings.toFloat() / totalSlots * 100).toInt() else 0
            val totalRevenue = ownerBookings.sumOf { it.price.toInt() }.let { if (it >= 1000) "${it / 1000}k" else it.toString() }
            val nextBooking = ownerBookings.minByOrNull { it.date + it.timeSlot }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Stat 1
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BorderGrey,
                    backgroundColor = Bleachers
                ) {
                    Column {
                        Text(
                            text = "RESERVAS DE HOY",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextGrey
                        )
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$todayBookings",
                                style = MaterialTheme.typography.displayLarge,
                                color = NeonGreen
                            )
                            Text(
                                text = " / $totalSlots turnos",
                                style = MaterialTheme.typography.bodyLarge,
                                color = SoftGrey,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(NeonGreen, shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${occupancy}% Ocupación",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGreen
                            )
                        }
                    }
                }

                // Stat 2
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BorderGrey,
                    backgroundColor = Bleachers
                ) {
                    Column {
                        Text(
                            text = "INGRESOS PROYECTADOS",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextGrey
                        )
                        Text(
                            text = "${totalRevenue}Fichas 🪙",
                            style = MaterialTheme.typography.displayLarge,
                            color = ChalkWhite,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                // Stat 3
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BorderGrey,
                    backgroundColor = NeonGreen
                ) {
                    Column {
                        Text(
                            text = "PRÓXIMO TURNO",
                            style = MaterialTheme.typography.labelSmall,
                            color = Asphalt,
                            fontWeight = FontWeight.Bold
                        )
                        if (nextBooking != null) {
                            Text(
                                text = nextBooking.timeSlot,
                                style = MaterialTheme.typography.displayLarge,
                                color = Asphalt
                            )
                            Text(
                                text = "${nextBooking.courtName} • \"${nextBooking.teamName}\"",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Asphalt,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else {
                            Text(
                                text = "Sin turnos",
                                style = MaterialTheme.typography.displayLarge,
                                color = Asphalt
                            )
                            Text(
                                text = "No hay reservas próximas",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Asphalt,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

        Spacer(modifier = Modifier.height(32.dp))

        // Owner manage courts & list
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PRÓXIMAS RESERVAS",
                style = MaterialTheme.typography.headlineLarge,
                color = ChalkWhite
            )

            IconButton(
                onClick = onNavigateToAddCourt,
                modifier = Modifier
                    .background(NeonGreen, shape = RoundedCornerShape(4.dp))
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Court",
                    tint = Asphalt
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bookings management list
        if (ownerBookings.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ownerBookings.forEach { booking ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Bleachers)
                            .border(
                                2.dp,
                                if (booking.status == "Seña Pagada" || booking.status == "Pagado Total") NeonGreen else BorderGrey,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .background(Asphalt, shape = RoundedCornerShape(4.dp))
                                            .border(1.dp, BorderGrey, shape = RoundedCornerShape(4.dp))
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "HOY",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = NeonGreen
                                            )
                                            Text(
                                                text = booking.timeSlot,
                                                style = MaterialTheme.typography.labelLarge,
                                                color = ChalkWhite
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = booking.teamName.uppercase(),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = ChalkWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "★",
                                                color = TextGrey,
                                                fontSize = 14.sp
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Cancha 1 (${booking.courtType})",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextGrey
                                            )
                                        }
                                    }
                                }

                                StencilBadge(
                                    text = booking.status.uppercase(),
                                    borderColor = when (booking.status) {
                                        "Pendiente Pago" -> DangerRed
                                        "Cancha Ocupada", "Ocupada" -> DangerRed
                                        else -> NeonGreen
                                    },
                                    textColor = when (booking.status) {
                                        "Pendiente Pago" -> DangerRed
                                        "Cancha Ocupada", "Ocupada" -> ChalkWhite
                                        else -> Asphalt
                                    },
                                    backgroundColor = when (booking.status) {
                                        "Pendiente Pago" -> Asphalt
                                        "Cancha Ocupada", "Ocupada" -> DangerRed
                                        else -> NeonGreen
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (booking.status == "Pendiente Pago") {
                                    Box(modifier = Modifier.weight(1f)) {
                                        BrutalistButton(
                                            text = "CONFIRMAR SEÑA",
                                            onClick = {
                                                viewModel.updateBookingStatus(booking.id, "Seña Pagada")
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                if (booking.status == "Cancha Ocupada" || booking.status == "Ocupada") {
                                    Box(modifier = Modifier.weight(1f)) {
                                        BrutalistButton(
                                            text = "LIBERAR CANCHA",
                                            onClick = {
                                                viewModel.updateBookingStatus(booking.id, "Seña Pagada")
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                } else {
                                    Box(modifier = Modifier.weight(1f)) {
                                        BrutalistButton(
                                            text = "MARCAR OCUPADA",
                                            onClick = {
                                                viewModel.updateBookingStatus(booking.id, "Ocupada")
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderGrey, shape = RoundedCornerShape(4.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NO TENÉS RESERVAS REGISTRADAS",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrey
                )
            }
        }

        // --- CANJE DE PASE GRATUITO (DUEÑO VALIDA CÓDIGO DEL JUGADOR) ---
        Spacer(modifier = Modifier.height(24.dp))
        DashedDivider(color = BorderGrey)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "CANJEAR PASE DE JUGADOR 🎟️",
            style = MaterialTheme.typography.headlineLarge,
            color = NeonGreen
        )
        Text(
            text = "Ingresá el código que te muestra el jugador en su app para validar su pase gratuito.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrey,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        var redeemCode by remember { mutableStateOf("") }
        val ownerRedeemResult by viewModel.ownerRedeemResult.collectAsState()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = redeemCode,
                onValueChange = { redeemCode = it.uppercase().take(14) },
                label = { Text("Código del pase", color = SoftGrey) },
                placeholder = { Text("SF-0-0000", color = TextGrey) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite,
                    focusedContainerColor = Asphalt,
                    unfocusedContainerColor = Asphalt
                ),
                modifier = Modifier.weight(1f)
            )

            BrutalistButton(
                text = "CANJEAR",
                onClick = {
                    if (redeemCode.isNotBlank()) {
                        viewModel.ownerRedeemPlayerPass(redeemCode)
                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }

        if (ownerRedeemResult.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            val isError = ownerRedeemResult.contains("inválido", ignoreCase = true) ||
                          ownerRedeemResult.contains("error", ignoreCase = true) ||
                          ownerRedeemResult.contains("solo", ignoreCase = true) ||
                          ownerRedeemResult.contains("debés", ignoreCase = true)
            Text(
                text = ownerRedeemResult,
                color = if (isError) DangerRed else NeonGreen,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.clearOwnerRedeemResult() }
                    .background(if (isError) DarkAsphalt else Bleachers, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, if (isError) DangerRed else NeonGreen, shape = RoundedCornerShape(4.dp))
                    .padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AddCourtView(
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: FulbitoViewModel,
    initialLat: Double = 0.0,
    initialLng: Double = 0.0
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("F5") }
    var surface by remember { mutableStateOf("Sintético") }
    var price by remember { mutableStateOf("") }
    var selectedHours by remember { mutableStateOf(listOf("18:00", "19:00", "20:00", "21:00", "22:00", "23:00")) }
    var courtLat by remember { mutableStateOf(initialLat) }
    var courtLng by remember { mutableStateOf(initialLng) }
    var showMapPicker by remember { mutableStateOf(false) }

    val allHours = listOf("16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkAsphalt)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AGREGAR CANCHA",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonGreen,
                    fontWeight = FontWeight.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Asphalt)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "SUMÁ TU CANCHA AL COMPLEJO.",
                style = MaterialTheme.typography.labelSmall,
                color = TextGrey,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la Cancha", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = { Text("Barrio", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "TIPO DE CANCHA",
                style = MaterialTheme.typography.labelLarge,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("F5", "F7", "F9").forEach { t ->
                    val isSelected = type == t
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { type = t }
                            .background(
                                if (isSelected) NeonGreen else Bleachers,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                2.dp,
                                if (isSelected) NeonGreen else BorderGrey,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = t,
                            color = if (isSelected) Asphalt else ChalkWhite,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Text(
                text = "SUPERFICIE",
                style = MaterialTheme.typography.labelLarge,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Sintético", "Cemento", "Césped").forEach { s ->
                    val isSelected = surface == s
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { surface = s }
                            .background(
                                if (isSelected) NeonGreen else Bleachers,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                2.dp,
                                if (isSelected) NeonGreen else BorderGrey,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = s.uppercase(),
                            color = if (isSelected) Asphalt else ChalkWhite,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Text(
                text = "HORARIOS DISPONIBLES",
                style = MaterialTheme.typography.labelLarge,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                allHours.chunked(4).forEach { rowHours ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowHours.forEach { hour ->
                            val isSelected = hour in selectedHours
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedHours = if (isSelected) selectedHours - hour
                                        else selectedHours + hour
                                    }
                                    .background(
                                        if (isSelected) NeonGreen else Bleachers,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .border(
                                        2.dp,
                                        if (isSelected) NeonGreen else BorderGrey,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = hour,
                                    color = if (isSelected) Asphalt else ChalkWhite,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio por Hora (Fichas)", color = SoftGrey) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = ChalkWhite,
                    focusedTextColor = ChalkWhite,
                    unfocusedTextColor = ChalkWhite
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            var isGeocoding by remember { mutableStateOf(false) }

            // Map picker button
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BrutalistButton(
                    text = if (courtLat != 0.0) "📍 UBICACIÓN CONFIGURADA" else "📍 AJUSTAR EN MAPA",
                    onClick = { showMapPicker = true },
                    modifier = Modifier.weight(1f),
                    enabled = true
                )
            }

            if (courtLat != 0.0 || courtLng != 0.0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
                        .padding(bottom = 16.dp)
                ) {
                    OpenStreetMapView(
                        courts = listOf(Court(name = "", address = "", neighborhood = "", type = "", surface = "", pricePerHour = 0.0, latitude = courtLat, longitude = courtLng)),
                        onCourtClick = {},
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            BrutalistButton(
                text = if (isGeocoding) "UBICANDO DIRECCIÓN..." else "REGISTRAR CANCHA",
                onClick = {
                    if (name.isNotBlank() && price.isNotBlank() && !isGeocoding) {
                        isGeocoding = true
                        val fullAddress = "$address, $neighborhood, Salta, Argentina"
                        viewModel.geocodeAddress(fullAddress) { lat, lng ->
                            if (courtLat == 0.0 && courtLng == 0.0) {
                                courtLat = lat
                                courtLng = lng
                            }
                            isGeocoding = false
                            viewModel.addCourt(
                                name = name,
                                address = address,
                                neighborhood = neighborhood,
                                type = type,
                                surface = surface,
                                price = price.toDoubleOrNull() ?: 0.0,
                                latitude = courtLat,
                                longitude = courtLng,
                                availableHours = selectedHours.joinToString(",")
                            )
                            onSuccess()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Map picker overlay
    if (showMapPicker) {
        MapPickerOverlay(
            initialLat = courtLat,
            initialLng = courtLng,
            onConfirm = { lat, lng ->
                courtLat = lat
                courtLng = lng
                showMapPicker = false
            },
            onDismiss = { showMapPicker = false }
        )
    }
}

@Composable
fun MapPickerOverlay(
    initialLat: Double,
    initialLng: Double,
    onConfirm: (Double, Double) -> Unit,
    onDismiss: () -> Unit
) {
    var markerLat by remember { mutableStateOf(if (initialLat != 0.0) initialLat else -24.7854) }
    var markerLng by remember { mutableStateOf(if (initialLng != 0.0) initialLng else -65.4112) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.9f))) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    org.osmdroid.config.Configuration.getInstance().apply {
                        userAgentValue = ctx.packageName
                        osmdroidTileCache = ctx.cacheDir
                        osmdroidBasePath = ctx.filesDir
                    }
                    org.osmdroid.views.MapView(ctx).apply {
                        setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
                        controller.setZoom(15.0)
                        controller.setCenter(org.osmdroid.util.GeoPoint(markerLat, markerLng))
                        setMultiTouchControls(true)
                        isHorizontalMapRepetitionEnabled = false
                        isVerticalMapRepetitionEnabled = false
                        minZoomLevel = 4.0

                        setOnLongClickListener {
                            val proj = projection
                            val clickPoint = org.osmdroid.util.GeoPoint(
                                proj.fromPixels(it.x.toInt(), it.y.toInt())
                            )
                            markerLat = clickPoint.latitude
                            markerLng = clickPoint.longitude
                            overlays.removeAll { it is org.osmdroid.views.overlay.Marker }
                            val marker = org.osmdroid.views.overlay.Marker(this)
                            marker.position = clickPoint
                            marker.title = "UBICACIÓN DE CANCHA"
                            marker.setAnchor(
                                org.osmdroid.views.overlay.Marker.ANCHOR_CENTER,
                                org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM
                            )
                            overlays.add(marker)
                            invalidate()
                            true
                        }

                        val marker = org.osmdroid.views.overlay.Marker(this)
                        marker.position = org.osmdroid.util.GeoPoint(markerLat, markerLng)
                        marker.title = "UBICACIÓN DE CANCHA"
                        marker.setAnchor(
                            org.osmdroid.views.overlay.Marker.ANCHOR_CENTER,
                            org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM
                        )
                        overlays.add(marker)
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { }
            )
        }

        // Info overlay on top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkAsphalt, shape = RoundedCornerShape(8.dp))
                    .border(2.dp, NeonGreen, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "MARCÁ LA UBICACIÓN DE TU CANCHA",
                        color = NeonGreen,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mantené presionado en el mapa para colocar el marcador",
                        color = TextGrey,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Lat: ${"%.4f".format(markerLat)} • Lng: ${"%.4f".format(markerLng)}",
                        color = SoftGrey,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BrutalistButton(
                text = "CANCELAR",
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                backgroundColor = DangerRed,
                contentColor = ChalkWhite
            )
            BrutalistButton(
                text = "CONFIRMAR UBICACIÓN",
                onClick = { onConfirm(markerLat, markerLng) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProfileView(
    viewModel: FulbitoViewModel,
    onLogoutSuccess: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Asphalt)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(DarkAsphalt, shape = CircleShape)
                .border(3.dp, NeonGreen, shape = CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Photo",
                tint = NeonGreen,
                modifier = Modifier.size(70.dp)
            )
        }

        profile?.let { p ->
            Text(
                text = p.fullName.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = ChalkWhite,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            StencilBadge(
                text = p.role,
                borderColor = NeonGreen,
                textColor = Asphalt,
                backgroundColor = NeonGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            // WALLET & REWARDED AD CARD
            val context = LocalContext.current
            val activity = context as? Activity
            val rewardedManager = remember { AdMobRewardedManager(context) }

            DisposableEffect(Unit) {
                onDispose { rewardedManager.cleanup() }
            }

            val handleReward: (Double) -> Unit = { amt ->
                viewModel.triggerAdWatched(amt)
                viewModel.onVideoWatched()
            }

            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen,
                backgroundColor = Bleachers
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "TU BILLETERA FULBITERA",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextGrey,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${p.balance.toInt()} Fichas 🪙",
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp),
                        color = NeonGreen,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "FICHAS PARA UNIRTE Y SEÑAR PARTIDOS",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftGrey,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    DashedDivider(color = BorderGrey)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (p.role == "JUGADOR") {
                        val adState = rewardedManager.state
                        val adText = when {
                            rewardedManager.isProcessingReward -> "PROCESANDO RECOMPENSA..."
                            adState == AdState.LOADING -> "CARGANDO ANUNCIO..."
                            adState == AdState.LOADED -> "VER ANUNCIO: GANÁ ${rewardedManager.rewardAmount} FICHAS"
                            adState == AdState.FAILED -> "TOCÁ PARA REINTENTAR CARGA"
                            else -> "CARGANDO ANUNCIO..."
                        }
                        val isAdClickable = adState == AdState.LOADED && !rewardedManager.isProcessingReward
                        val adIcon = Icons.Default.PlayArrow

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkAsphalt)
                                .border(1.dp, if (adState == AdState.LOADED) NeonGreen else BorderGrey)
                                .clickable(enabled = isAdClickable) {
                                    val act = activity
                                    if (act != null) {
                                        rewardedManager.showAd(act) { amt ->
                                            handleReward(amt.toDouble())
                                        }
                                    }
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = adIcon,
                                contentDescription = "Ad Icon",
                                tint = if (adState == AdState.LOADED) NeonGreen else SoftGrey,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = adText,
                                fontSize = 11.sp,
                                color = if (adState == AdState.LOADED) NeonGreen else SoftGrey,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- CANJE SEMANAL JUGADORES AFICIONADOS (PASE GRATIS) ---
            if (p.role == "JUGADOR") {
                val hasFreePass by viewModel.hasWeeklyFreePass.collectAsState()
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (hasFreePass) NeonGreen else BorderGrey,
                    backgroundColor = Bleachers
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CANJE SEMANAL: ENTRADA GRATUITA 🎟️",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextGrey,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (hasFreePass) {
                            Box(
                                modifier = Modifier
                                    .background(NeonGreen)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "TENÉS 1 PASE GRATIS DISPONIBLE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Asphalt,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Usalo al unirse a cualquier partido donde falte un jugador.",
                                style = MaterialTheme.typography.bodySmall,
                                color = ChalkWhite,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "Canjeá 1000 Fichas por un Pase Gratis Semanal para entrar a cualquier partido con vacantes totalmente gratis.",
                                style = MaterialTheme.typography.bodySmall,
                                color = ChalkWhite,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            BrutalistButton(
                                text = "CANJEAR PASE POR 1000 FICHAS 🎟️",
                                onClick = { viewModel.redeemWeeklyFreePass() },
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = NeonGreen,
                                contentColor = Asphalt
                            )
                        }
                    }
                }

                // --- TUS PASES ACTIVOS ---
                Spacer(modifier = Modifier.height(12.dp))
                val activePasses by viewModel.activePlayerPasses.collectAsState()
                LaunchedEffect(p.id) { viewModel.loadActivePlayerPasses(p.id) }

                if (activePasses.isNotEmpty()) {
                    BrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = NeonGreen,
                        backgroundColor = Bleachers
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TUS PASES ACTIVOS 🎟️",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGreen,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Mostrale este código al dueño de la cancha para canjear tu entrada gratuita.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoftGrey,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            activePasses.forEach { pass ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(DarkAsphalt)
                                        .border(1.dp, NeonGreen, shape = RoundedCornerShape(4.dp))
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "CÓDIGO",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextGrey,
                                                fontSize = 9.sp
                                            )
                                            Text(
                                                text = pass.passCode,
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = NeonGreen,
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 2.sp
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "CREADO",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextGrey,
                                                fontSize = 9.sp
                                            )
                                            Text(
                                                text = pass.createdAt,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ChalkWhite
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details card
            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = BorderGrey,
                backgroundColor = Bleachers
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "EMAIL", style = MaterialTheme.typography.labelSmall, color = TextGrey)
                        Text(text = p.email, style = MaterialTheme.typography.bodyLarge, color = ChalkWhite, fontWeight = FontWeight.Bold)
                    }

                    DashedDivider(color = BorderGrey)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "TELÉFONO", style = MaterialTheme.typography.labelSmall, color = TextGrey)
                        Text(text = p.phone, style = MaterialTheme.typography.bodyLarge, color = ChalkWhite, fontWeight = FontWeight.Bold)
                    }

                    if (p.role == "JUGADOR") {
                        DashedDivider(color = BorderGrey)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "POSICIÓN", style = MaterialTheme.typography.labelSmall, color = TextGrey)
                            Text(text = p.position.uppercase(), style = MaterialTheme.typography.bodyLarge, color = NeonGreen, fontWeight = FontWeight.Bold)
                        }

                        DashedDivider(color = BorderGrey)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "NIVEL", style = MaterialTheme.typography.labelSmall, color = TextGrey)
                            Text(text = p.level.uppercase(), style = MaterialTheme.typography.bodyLarge, color = NeonGreen, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        DashedDivider(color = BorderGrey)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "COMPLEJO", style = MaterialTheme.typography.labelSmall, color = TextGrey)
                            Text(text = p.complexName.uppercase(), style = MaterialTheme.typography.bodyLarge, color = NeonGreen, fontWeight = FontWeight.Bold)
                        }

                        DashedDivider(color = BorderGrey)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "DIRECCIÓN", style = MaterialTheme.typography.labelSmall, color = TextGrey)
                            Text(text = p.complexAddress, style = MaterialTheme.typography.bodyLarge, color = ChalkWhite, fontWeight = FontWeight.Bold)
                        }

                        DashedDivider(color = BorderGrey)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "CUIT", style = MaterialTheme.typography.labelSmall, color = TextGrey)
                            Text(text = p.cuit, style = MaterialTheme.typography.bodyLarge, color = ChalkWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BrutalistButton(
            text = "CERRAR SESIÓN",
            onClick = {
                viewModel.logout()
                onLogoutSuccess()
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Asphalt,
            contentColor = DangerRed
        )

    }
}
