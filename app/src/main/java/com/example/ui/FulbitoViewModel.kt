package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.Booking
import com.example.data.Court
import com.example.data.FulbitoRepository
import com.example.data.MIGRATION_4_5
import com.example.data.MIGRATION_5_6
import com.example.data.MIGRATION_6_7
import com.example.data.MIGRATION_7_8
import com.example.data.SupabaseManager
import com.example.data.UserProfile
import com.example.data.MatchSlot
import com.example.data.FreePass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FulbitoViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "salefulbito_db"
     ).addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
          .fallbackToDestructiveMigration(false)
         .build()

    val repository = FulbitoRepository(
        userProfileDao = database.userProfileDao(),
        courtDao = database.courtDao(),
        bookingDao = database.bookingDao(),
        matchSlotDao = database.matchSlotDao(),
        courtReviewDao = database.courtReviewDao(),
        userVideoProgressDao = database.userVideoProgressDao(),
        courtVisitDao = database.courtVisitDao(),
        freePassDao = database.freePassDao()
    )

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val courts: StateFlow<List<Court>> = repository.courts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bookings: StateFlow<List<Booking>> = repository.bookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val matchSlots: StateFlow<List<MatchSlot>> = repository.matchSlots
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // UI Interactive States
    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedSlot = MutableStateFlow("")
    val selectedSlot: StateFlow<String> = _selectedSlot.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterSurface = MutableStateFlow("")
    val filterSurface: StateFlow<String> = _filterSurface.asStateFlow()

    // Dynamic city detection state
    private val _detectedCity = MutableStateFlow("")
    val detectedCity: StateFlow<String> = _detectedCity.asStateFlow()

    // Weekly Free Pass Exchange (Canje de Entrada Gratuita Semanal)
    private val _hasWeeklyFreePass = MutableStateFlow(false)
    val hasWeeklyFreePass: StateFlow<Boolean> = _hasWeeklyFreePass.asStateFlow()

    // Court reviews - now loaded from database
    val courtReviews: StateFlow<List<com.example.data.CourtReview>> = repository.courtReviews
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Video progress tracking
    private val _todayVideosWatched = MutableStateFlow(0)
    val todayVideosWatched: StateFlow<Int> = _todayVideosWatched.asStateFlow()

    private val _consecutiveDays = MutableStateFlow(0)
    val consecutiveDays: StateFlow<Int> = _consecutiveDays.asStateFlow()

    private val _canRedeemCoupon = MutableStateFlow(false)
    val canRedeemCoupon: StateFlow<Boolean> = _canRedeemCoupon.asStateFlow()

    // FreePass system
    val freePasses: StateFlow<List<FreePass>> = repository.freePasses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _activePlayerPasses = MutableStateFlow<List<FreePass>>(emptyList())
    val activePlayerPasses: StateFlow<List<FreePass>> = _activePlayerPasses.asStateFlow()

    private val _ownerRedeemResult = MutableStateFlow("")
    val ownerRedeemResult: StateFlow<String> = _ownerRedeemResult.asStateFlow()

    init {
        checkVideoProgress()
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun selectSlot(slot: String) {
        _selectedSlot.value = slot
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilterSurface(surface: String) {
        _filterSurface.value = surface
    }

    // Helper to generate next 5 upcoming days starting from current calendar system time
    fun getUpcomingDays(): List<String> {
        val days = mutableListOf<String>()
        val sdf = SimpleDateFormat("EEE d", Locale.forLanguageTag("es-AR"))
        val cal = Calendar.getInstance()
        for (i in 0 until 5) {
            val dateStr = sdf.format(cal.time)
            val formatted = dateStr.replace(".", "").replaceFirstChar { it.uppercase() }
            days.add(formatted)
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return days
    }

    // Free Pass Exchange actions
    fun redeemWeeklyFreePass() {
        viewModelScope.launch {
            val profile = userProfile.value
            if (profile != null && profile.balance >= 1000.0) {
                repository.decrementBalance(1000.0)
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.forLanguageTag("es-AR"))
                val todayStr = sdf.format(java.util.Calendar.getInstance().time)
                val code = "SF-${profile.id}-${(1000..9999).random()}"
                val freePass = FreePass(
                    playerId = profile.id,
                    playerName = profile.fullName,
                    playerEmail = profile.email,
                    passCode = code,
                    status = "active",
                    createdAt = todayStr
                )
                repository.saveFreePass(freePass)
                _hasWeeklyFreePass.value = true
                _adStatusMessage.value = "¡Pase Canjeado! Cambiaste 1000 Fichas por 1 Pase Gratis Semanal. Código: $code 🎟️"
            } else {
                _adStatusMessage.value = "Faltan Fichas. Necesitás al menos 1000 Fichas para canjear un pase."
            }
        }
    }

    fun useWeeklyFreePass() {
        _hasWeeklyFreePass.value = false
    }

    fun loadActivePlayerPasses(playerId: Int) {
        viewModelScope.launch {
            repository.getActivePlayerPassesFlow(playerId).collect { passes ->
                _activePlayerPasses.value = passes
            }
        }
    }

    fun ownerRedeemPlayerPass(passCode: String) {
        viewModelScope.launch {
            val profile = userProfile.value ?: run {
                _ownerRedeemResult.value = "Debés iniciar sesión como dueño de cancha."
                return@launch
            }
            if (profile.role != "DUEÑO") {
                _ownerRedeemResult.value = "Solo los dueños de cancha pueden canjear pases."
                return@launch
            }
            val pass = repository.getPassByCode(passCode.trim().uppercase())
            if (pass == null) {
                _ownerRedeemResult.value = "Código inválido. No se encontró ningún pase con ese código."
                return@launch
            }
            if (pass.status != "active") {
                _ownerRedeemResult.value = "Este pase ya fue utilizado (${pass.usedAt})."
                return@launch
            }
            repository.redeemFreePass(pass.id, profile.fullName, 0)
            _ownerRedeemResult.value = "¡Pase canjeado con éxito! ${pass.playerName} tiene acceso gratuito. 🎟️"
        }
    }

    fun clearOwnerRedeemResult() {
        _ownerRedeemResult.value = ""
    }

    // Opinion / Review Actions
    fun addCourtReview(courtId: Int, author: String, rating: Int, comment: String, visitVerified: Boolean = false) {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-AR"))
            val todayStr = sdf.format(Calendar.getInstance().time)
            
            val newReview = com.example.data.CourtReview(
                id = (courtReviews.value.maxOfOrNull { it.id } ?: 0) + 1,
                courtId = courtId,
                author = author,
                rating = rating,
                comment = comment,
                date = todayStr,
                visitVerified = visitVerified,
                visitDate = if (visitVerified) todayStr else ""
            )
            
            // Save to repository (persistent)
            repository.addCourtReview(newReview)
            
            // Calculate new average rating for the court
            val reviewsForCourt = repository.getCourtReviews(courtId)
            val avgRating = reviewsForCourt.map { it.rating }.average().toFloat()
            val roundedRating = Math.round(avgRating * 10f) / 10f

            // Update court's rating in database to be persistent
            courts.value.find { it.id == courtId }?.let { existingCourt ->
                val updatedCourt = existingCourt.copy(rating = roundedRating)
                repository.addCourt(updatedCourt)
            }
        }
    }

    // Video Reward System
    fun onVideoWatched() {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("es-AR"))
            val todayStr = sdf.format(Calendar.getInstance().time)
            
            val newCount = repository.incrementVideosWatched(profile.id, todayStr)
            _todayVideosWatched.value = newCount
            
            val latest = repository.getLatestVideoProgress(profile.id)
            latest?.let {
                _consecutiveDays.value = it.consecutiveDays
                _canRedeemCoupon.value = repository.canRedeemCoupon(profile.id)
            }
        }
    }

    fun redeemVideoCoupon() {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("es-AR"))
            val todayStr = sdf.format(Calendar.getInstance().time)
            
            repository.markCouponRedeemed(profile.id, todayStr)
            _hasWeeklyFreePass.value = true
            _canRedeemCoupon.value = false
            _todayVideosWatched.value = 0
            _consecutiveDays.value = 0
        }
    }

    fun checkVideoProgress() {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            val latest = repository.getLatestVideoProgress(profile.id)
            latest?.let {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("es-AR"))
                val todayStr = sdf.format(Calendar.getInstance().time)
                
                if (it.date == todayStr) {
                    _todayVideosWatched.value = it.videosWatched
                } else {
                    _todayVideosWatched.value = 0
                }
                
                _consecutiveDays.value = it.consecutiveDays
                _canRedeemCoupon.value = repository.canRedeemCoupon(profile.id)
            }
        }
    }

    // Court Visit Verification Methods
    fun recordCourtVisit(courtId: Int, bookingId: Int = 0) {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            repository.recordCourtVisit(profile.id, courtId, bookingId)
        }
    }

    fun verifyCourtVisit(courtId: Int, userLat: Double, userLon: Double) {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            val verified = repository.verifyCourtVisit(profile.id, courtId, userLat, userLon)
            if (verified) {
                _adStatusMessage.value = "¡Visita verificada! Ahora podés dejar tu reseña."
            } else {
                _adStatusMessage.value = "Debes estar en la cancha para verificar la visita."
            }
        }
    }

    private val _canReviewCourt = MutableStateFlow(false)
    val canReviewCourt: StateFlow<Boolean> = _canReviewCourt.asStateFlow()

    fun checkCanReviewCourt(courtId: Int) {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            _canReviewCourt.value = repository.hasVerifiedVisit(profile.id, courtId)
        }
    }

    // Notification methods
    fun requestNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // In a real implementation, you would request the permission here
            // For now, we assume permission is granted or handle in Activity
        }
    }

    fun subscribeToTopic(topic: String) {
        // In a real implementation, you would subscribe to FCM topics
        // FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

    fun sendBookingReminderNotification(bookingId: Int) {
        // This would be called by a background service or notification scheduler
        // In production, this would be handled by FCM from your backend
    }

    fun sendCouponAvailableNotification() {
        // Notify user that a coupon is available
        _adStatusMessage.value = "¡Tenés un cupón disponible! 🎟️"
    }

    // Auth actions
    fun registerPlayer(name: String, email: String, phone: String, password: String, position: String, level: String) {
        viewModelScope.launch {
            repository.saveProfile(
                UserProfile(
                    fullName = name,
                    email = email,
                    phone = phone,
                    password = password,
                    role = "JUGADOR",
                    position = position,
                    level = level,
                    balance = 0.0 // Starting token balance (fichas) - loaded from config
                )
            )
        }
    }

    fun registerOwner(name: String, email: String, phone: String, password: String, complexName: String, address: String, cuit: String) {
        viewModelScope.launch {
            repository.saveProfile(
                UserProfile(
                    fullName = name,
                    email = email,
                    phone = phone,
                    password = password,
                    role = "DUEÑO",
                    complexName = complexName,
                    complexAddress = address,
                    cuit = cuit,
                    balance = 0.0 // Owners do not earn tokens, they monetize via ads
                )
            )
        }
    }

    private val _isLoadingDemo = MutableStateFlow(false)
    val isLoadingDemo: StateFlow<Boolean> = _isLoadingDemo.asStateFlow()

    // Login state
    private val _loginError = MutableStateFlow("")
    val loginError: StateFlow<String> = _loginError.asStateFlow()

    private val _isLoggingIn = MutableStateFlow(false)
    val isLoggingIn: StateFlow<Boolean> = _isLoggingIn.asStateFlow()

    fun loginUser(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoggingIn.value = true
            _loginError.value = ""

            // 1. Check local profile first
            val localProfile = repository.getProfile()
            if (localProfile != null && localProfile.email == email && localProfile.password == password) {
                _isLoggingIn.value = false
                onSuccess()
                return@launch
            }

            // 2. Try Supabase fetch
            if (SupabaseManager.isConfigured) {
                val remoteProfile = SupabaseManager.fetchUserProfile(email)
                if (remoteProfile != null && remoteProfile.password == password) {
                    repository.saveProfile(remoteProfile)
                    _isLoggingIn.value = false
                    onSuccess()
                    return@launch
                }
            }

            _loginError.value = if (SupabaseManager.isConfigured) "Email o contraseña incorrectos." else "Sin conexión al servidor. Verificá tu conexión e intentá de nuevo."
            _isLoggingIn.value = false
        }
    }

    fun clearLoginError() {
        _loginError.value = ""
    }

    fun seedDemoPlayer() {
        viewModelScope.launch {
            _isLoadingDemo.value = true
            repository.saveProfile(
                UserProfile(
                    fullName = "Juan Pérez",
                    email = "juan@test.com",
                    phone = "11 5555-0101",
                    password = "demo123",
                    role = "JUGADOR",
                    position = "Delantero",
                    level = "Pro",
                    balance = 10000.0
                )
            )
            _isLoadingDemo.value = false
        }
    }

    fun seedDemoOwner() {
        viewModelScope.launch {
            _isLoadingDemo.value = true
            repository.saveProfile(
                UserProfile(
                    fullName = "Carlos García",
                    email = "carlos@test.com",
                    phone = "11 5555-0102",
                    password = "demo123",
                    role = "DUEÑO",
                    complexName = "Canchas del Parque",
                    complexAddress = "Av. Siempre Viva 123",
                    cuit = "20-12345678-9",
                    balance = 0.0
                )
            )
            _isLoadingDemo.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearProfile()
        }
    }

    fun updateBookingStatus(bookingId: Int, status: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, status)
        }
    }

    fun confirmBooking(
        courtId: Int,
        courtName: String,
        courtType: String,
        price: Double
    ) {
        viewModelScope.launch {
            repository.addBooking(
                Booking(
                    courtId = courtId,
                    courtName = courtName,
                    courtType = courtType,
                    date = _selectedDate.value,
                    timeSlot = _selectedSlot.value,
                    price = price,
                    status = "Seña Pagada",
                    teamName = userProfile.value?.fullName ?: "",
                    isMyBooking = true
                )
            )
        }
    }

    fun addCourt(
        name: String,
        address: String,
        neighborhood: String,
        type: String,
        surface: String,
        price: Double,
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        availableHours: String = "18:00,19:00,20:00,21:00,22:00,23:00"
    ) {
        viewModelScope.launch {
            repository.addCourt(
                Court(
                    name = name,
                    address = address,
                    neighborhood = neighborhood,
                    type = type,
                    surface = surface,
                    pricePerHour = price,
                    isOwnerCourt = true,
                    latitude = latitude,
                    longitude = longitude,
                    availableHours = availableHours
                )
            )
        }
    }

    fun geocodeAddress(address: String, onResult: (Double, Double) -> Unit) {
        viewModelScope.launch {
            try {
                val geocoder = android.location.Geocoder(getApplication(), java.util.Locale.getDefault())
                val results = geocoder.getFromLocationName(address, 1)
                if (!results.isNullOrEmpty()) {
                    val result = results[0]
                    onResult(result.latitude, result.longitude)
                } else {
                    onResult(0.0, 0.0)
                }
            } catch (e: Exception) {
                onResult(0.0, 0.0)
            }
        }
    }

    // AdMob Integration States & Handlers
    private val _adsWatchedCount = MutableStateFlow(0)
    val adsWatchedCount: StateFlow<Int> = _adsWatchedCount.asStateFlow()

    private val _adStatusMessage = MutableStateFlow("")
    val adStatusMessage: StateFlow<String> = _adStatusMessage.asStateFlow()

    fun clearAdStatusMessage() {
        _adStatusMessage.value = ""
    }

    fun triggerAdWatched(amount: Double) {
        viewModelScope.launch {
            repository.incrementBalance(amount)
            _adsWatchedCount.value += 1
            _adStatusMessage.value = "¡Sponsor Completado! Sumaste +${amount.toInt()} Fichas 🪙 a tu billetera."
        }
    }

    fun triggerInterstitialShown() {
        _adStatusMessage.value = "Anuncio Intersticial mostrado."
    }

    fun triggerAdError(error: String) {
        _adStatusMessage.value = "Error al cargar anuncio: $error"
    }

    fun deductBalance(amount: Double) {
        viewModelScope.launch {
            repository.decrementBalance(amount)
        }
    }

    fun publishMatchSlot(
        courtId: Int,
        courtName: String,
        neighborhood: String,
        teamName: String,
        matchType: String,
        slotsMissing: Int,
        roleNeeded: String,
        pricePerPlayer: Double
    ) {
        viewModelScope.launch {
            repository.addMatchSlot(
                MatchSlot(
                    courtId = courtId,
                    courtName = courtName,
                    neighborhood = neighborhood,
                    date = _selectedDate.value,
                    timeSlot = _selectedSlot.value,
                    teamName = teamName,
                    matchType = matchType,
                    slotsMissing = slotsMissing,
                    roleNeeded = roleNeeded,
                    pricePerPlayer = pricePerPlayer,
                    joinedPlayers = userProfile.value?.fullName ?: ""
                )
            )
        }
    }

    fun joinMatchSlot(slot: MatchSlot, playerName: String, useFreePass: Boolean = false) {
        viewModelScope.launch {
            if (slot.slotsMissing > 0) {
                val newSlots = slot.slotsMissing - 1
                val newPlayers = if (slot.joinedPlayers.isEmpty()) playerName else "${slot.joinedPlayers}, $playerName"
                val newStatus = if (newSlots == 0) "Completo" else "Abierto"
                repository.updateMatchSlotPlayers(slot.id, newSlots, newPlayers, newStatus)
                
                if (useFreePass) {
                    useWeeklyFreePass()
                    _adStatusMessage.value = "¡Te uniste gratis usando tu Pase Semanal! 🎟️"
                } else {
                    repository.decrementBalance(slot.pricePerPlayer)
                    _adStatusMessage.value = "Te uniste al partido. Se canjearon ${slot.pricePerPlayer.toInt()} Fichas."
                }
            }
        }
    }
}
