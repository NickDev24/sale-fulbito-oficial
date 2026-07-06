package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val role: String, // "JUGADOR" or "DUEÑO"
    val position: String = "", // for player: Arquero, Defensor, Mediocampista, Delantero
    val level: String = "", // for player: Amateur, Pro
    val complexName: String = "", // for owner
    val complexAddress: String = "", // for owner
    val cuit: String = "", // for owner
    val password: String = "",
    val balance: Double = 0.0 // persistent user balance
)

@Entity(tableName = "courts")
data class Court(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val neighborhood: String,
    val type: String, // F5, F7, F9
    val surface: String, // Sintético, Cemento, Césped
    val pricePerHour: Double,
    val imageUrl: String = "",
    val rating: Float = 0.0f,
    val isFavorite: Boolean = false,
    val isOwnerCourt: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val availableHours: String = "18:00,19:00,20:00,21:00,22:00,23:00"
)

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courtId: Int,
    val courtName: String,
    val courtType: String,
    val date: String, // e.g. "Jue 12"
    val timeSlot: String, // e.g. "21:00"
    val price: Double,
    val status: String, // "Confirmado", "Pendiente Pago", "Seña Pagada", "Pagado Total", "En Juego", "Cancha Ocupada"
    val teamName: String,
    val isMyBooking: Boolean = false // booked by logged-in player
)

@Entity(tableName = "match_slots")
data class MatchSlot(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courtId: Int,
    val courtName: String,
    val neighborhood: String,
    val date: String,
    val timeSlot: String,
    val teamName: String,
    val matchType: String, // "F5", "F7", "F9"
    val slotsMissing: Int,
    val roleNeeded: String, // "Arquero", "Defensor", "Mediocampista", "Delantero", "Cualquiera"
    val pricePerPlayer: Double,
    val joinedPlayers: String, // Comma separated list of players
    val status: String = "Abierto" // "Abierto", "Completo"
)

@Entity(tableName = "court_reviews")
data class CourtReview(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courtId: Int,
    val author: String,
    val rating: Int, // 1 to 5
    val comment: String,
    val date: String,
    val visitVerified: Boolean = false, // Visit verification
    val visitDate: String = "" // Date of verified visit
)

@Entity(tableName = "user_video_progress")
data class UserVideoProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val date: String, // Format: "YYYY-MM-DD"
    val videosWatched: Int = 0,
    val consecutiveDays: Int = 0,
    val lastCouponDate: String = ""
)

@Entity(tableName = "court_visits")
data class CourtVisit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val courtId: Int,
    val visitDate: String, // Format: "YYYY-MM-DD"
    val bookingId: Int = 0, // Optional: linked to a booking
    val verified: Boolean = false // Visit verified via GPS location
)

@Entity(tableName = "free_passes")
data class FreePass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerId: Int,
    val playerName: String,
    val playerEmail: String,
    val passCode: String,
    val status: String = "active", // "active", "used", "expired"
    val createdAt: String,
    val usedAt: String = "",
    val usedByOwner: String = "",
    val usedAtCourtId: Int = 0
)

