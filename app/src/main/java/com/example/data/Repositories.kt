package com.example.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FulbitoRepository(
    private val userProfileDao: UserProfileDao,
    private val courtDao: CourtDao,
    private val bookingDao: BookingDao,
    private val matchSlotDao: MatchSlotDao,
    private val courtReviewDao: CourtReviewDao,
    private val userVideoProgressDao: UserVideoProgressDao,
    private val courtVisitDao: CourtVisitDao,
    private val freePassDao: FreePassDao
) {
    private val TAG = "FulbitoRepository"

    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfileFlow()
    val courts: Flow<List<Court>> = courtDao.getAllCourtsFlow()
    val bookings: Flow<List<Booking>> = bookingDao.getAllBookingsFlow()
    val matchSlots: Flow<List<MatchSlot>> = matchSlotDao.getAllMatchSlotsFlow()
    val courtReviews: Flow<List<CourtReview>> = courtReviewDao.getAllReviewsFlow()

    suspend fun saveProfile(profile: UserProfile) {
        userProfileDao.clearAll()
        userProfileDao.insertUserProfile(profile)
        
        if (SupabaseManager.isConfigured) {
            try {
                SupabaseManager.syncUserProfile(profile)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync profile with Supabase", e)
            }
        }
    }

    suspend fun incrementBalance(amount: Double) {
        userProfileDao.incrementBalance(amount)
        userProfileDao.getUserProfile()?.let { updated ->
            if (SupabaseManager.isConfigured) {
                try {
                    SupabaseManager.syncUserProfile(updated)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to sync incremented balance to Supabase", e)
                }
            }
        }
    }

    suspend fun decrementBalance(amount: Double) {
        userProfileDao.decrementBalance(amount)
        userProfileDao.getUserProfile()?.let { updated ->
            if (SupabaseManager.isConfigured) {
                try {
                    SupabaseManager.syncUserProfile(updated)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to sync decremented balance to Supabase", e)
                }
            }
        }
    }

    suspend fun clearProfile() {
        userProfileDao.clearAll()
    }

    suspend fun getProfile(): UserProfile? {
        return userProfileDao.getUserProfile()
    }

    suspend fun addCourt(court: Court) {
        courtDao.insertCourt(court)
        if (SupabaseManager.isConfigured) {
            try {
                SupabaseManager.pushCourt(court)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync new court to Supabase", e)
            }
        }
    }

    suspend fun toggleCourtFavorite(courtId: Int, isFav: Boolean) {
        courtDao.updateFavorite(courtId, isFav)
        // Locally updated, no need to force a sync if it is a local preference, but let's push if it's there
    }

    suspend fun addBooking(booking: Booking) {
        bookingDao.insertBooking(booking)
        if (SupabaseManager.isConfigured) {
            try {
                SupabaseManager.pushBooking(booking)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync booking to Supabase", e)
            }
        }
    }

    suspend fun updateBookingStatus(bookingId: Int, status: String) {
        bookingDao.updateBookingStatus(bookingId, status)
        if (SupabaseManager.isConfigured) {
            try {
                SupabaseManager.updateBookingStatus(bookingId, status)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync booking status to Supabase", e)
            }
        }
    }

    suspend fun addMatchSlot(matchSlot: MatchSlot) {
        matchSlotDao.insertMatchSlot(matchSlot)
        if (SupabaseManager.isConfigured) {
            try {
                SupabaseManager.pushMatchSlot(matchSlot)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync match slot to Supabase", e)
            }
        }
    }

    suspend fun updateMatchSlotPlayers(slotId: Int, slotsMissing: Int, joinedPlayers: String, status: String) {
        matchSlotDao.updateMatchSlotPlayers(slotId, slotsMissing, joinedPlayers, status)
        if (SupabaseManager.isConfigured) {
            try {
                SupabaseManager.updateMatchSlotPlayers(slotId, slotsMissing, joinedPlayers, status)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync joined players to Supabase", e)
            }
        }
    }

    suspend fun seedIfNeeded() {
        // 1. Fetch remote data first if Supabase is active to keep in sync
        if (SupabaseManager.isConfigured) {
            try {
                Log.i(TAG, "Supabase active. Pulling fresh data...")
                val remoteCourts = SupabaseManager.fetchCourts()
                if (remoteCourts.isNotEmpty()) {
                    courtDao.insertCourts(remoteCourts)
                }
                
                val remoteBookings = SupabaseManager.fetchBookings()
                if (remoteBookings.isNotEmpty()) {
                    bookingDao.insertBookings(remoteBookings)
                }

                val remoteMatchSlots = SupabaseManager.fetchMatchSlots()
                if (remoteMatchSlots.isNotEmpty()) {
                    matchSlotDao.insertMatchSlots(remoteMatchSlots)
                }

                val localProfile = userProfileDao.getUserProfile()
                if (localProfile != null) {
                    val remoteProfile = SupabaseManager.fetchUserProfile(localProfile.email)
                    if (remoteProfile != null) {
                        userProfileDao.clearAll()
                        userProfileDao.insertUserProfile(remoteProfile)
                    } else {
                        SupabaseManager.syncUserProfile(localProfile)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error performing background Supabase sync at startup", e)
            }
        }

        // 2. Local Fallback Seeding - REMOVED: Data should come from Supabase only
        // All data will be fetched from Supabase backend
    }

    // Court Reviews Methods
    suspend fun addCourtReview(review: CourtReview) {
        courtReviewDao.insertReview(review)
    }

    suspend fun getCourtReviews(courtId: Int): List<CourtReview> {
        return courtReviewDao.getAllReviewsFlow().first().filter { it.courtId == courtId }
    }

    suspend fun getCourt(courtId: Int): Court? {
        return courtDao.getAllCourtsFlow().first().find { it.id == courtId }
    }

    suspend fun getAverageRating(courtId: Int): Double {
        return courtReviewDao.getAverageRating(courtId)
    }

    suspend fun getVerifiedReview(author: String, courtId: Int): CourtReview? {
        return courtReviewDao.getVerifiedReview(author, courtId)
    }

    // User Video Progress Methods
    suspend fun getLatestVideoProgress(userId: Int): UserVideoProgress? {
        return userVideoProgressDao.getLatestProgress(userId)
    }

    suspend fun updateVideoProgress(progress: UserVideoProgress) {
        userVideoProgressDao.insertProgress(progress)
    }

    suspend fun incrementVideosWatched(userId: Int, currentDate: String): Int {
        val latest = getLatestVideoProgress(userId)
        val newCount = if (latest != null && latest.date == currentDate) {
            latest.videosWatched + 1
        } else {
            1
        }
        
        val progress = UserVideoProgress(
            id = latest?.id ?: 0,
            userId = userId,
            date = currentDate,
            videosWatched = newCount,
            consecutiveDays = if (latest != null && isConsecutiveDay(latest.date, currentDate)) {
                latest.consecutiveDays + 1
            } else {
                1
            },
            lastCouponDate = latest?.lastCouponDate ?: ""
        )
        
        updateVideoProgress(progress)
        return newCount
    }

    suspend fun canRedeemCoupon(userId: Int): Boolean {
        val latest = getLatestVideoProgress(userId) ?: return false
        return latest.consecutiveDays >= 3 && latest.videosWatched >= 5
    }

    suspend fun markCouponRedeemed(userId: Int, date: String) {
        val latest = getLatestVideoProgress(userId) ?: return
        val updated = latest.copy(
            lastCouponDate = date,
            videosWatched = 0,
            consecutiveDays = 0
        )
        updateVideoProgress(updated)
    }

    private fun isConsecutiveDay(lastDate: String, currentDate: String): Boolean {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val last = sdf.parse(lastDate) ?: return false
            val current = sdf.parse(currentDate) ?: return false
            val diff = current.time - last.time
            diff in (1..86400000) // 24 hours in milliseconds
        } catch (e: Exception) {
            false
        }
    }

    // Court Visit Methods
    suspend fun recordCourtVisit(userId: Int, courtId: Int, bookingId: Int = 0) {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val todayStr = sdf.format(java.util.Calendar.getInstance().time)
        
        val visit = CourtVisit(
            userId = userId,
            courtId = courtId,
            visitDate = todayStr,
            bookingId = bookingId,
            verified = false
        )
        
        courtVisitDao.insertVisit(visit)
    }

    suspend fun verifyCourtVisit(userId: Int, courtId: Int, userLat: Double, userLon: Double): Boolean {
        // Get court location
        val court = courts.first().find { it.id == courtId }
        if (court == null || court.latitude == 0.0 || court.longitude == 0.0) {
            return false
        }
        
        // Calculate distance (simple Haversine approximation)
        val distance = calculateDistance(userLat, userLon, court.latitude, court.longitude)
        
        // Verify if within 100 meters of the court
        if (distance <= 100.0) {
            val visits = courtVisitDao.getUserVisitsToCourt(userId, courtId)
            val todayVisit = visits.firstOrNull { 
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val todayStr = sdf.format(java.util.Calendar.getInstance().time)
                it.visitDate == todayStr 
            }
            
            if (todayVisit != null) {
                courtVisitDao.markVisitVerified(todayVisit.id)
                return true
            }
        }
        
        return false
    }

    suspend fun hasVerifiedVisit(userId: Int, courtId: Int): Boolean {
        return courtVisitDao.getVerifiedVisitCount(userId, courtId) > 0
    }

    // FreePass Methods
    val freePasses: Flow<List<FreePass>> = freePassDao.getAllPassesFlow()

    fun getActivePlayerPassesFlow(playerId: Int): Flow<List<FreePass>> =
        freePassDao.getActivePlayerPassesFlow(playerId)

    suspend fun getPassByCode(code: String): FreePass? =
        freePassDao.getPassByCode(code)

    suspend fun saveFreePass(pass: FreePass) {
        freePassDao.insertPass(pass)
    }

    suspend fun redeemFreePass(passId: Int, ownerName: String, courtId: Int) {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.forLanguageTag("es-AR"))
        val dateStr = sdf.format(java.util.Date())
        freePassDao.updatePassStatus(passId, "used", dateStr, ownerName, courtId)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3 // Earth radius in meters
        val φ1 = Math.toRadians(lat1)
        val φ2 = Math.toRadians(lat2)
        val Δφ = Math.toRadians(lat2 - lat1)
        val Δλ = Math.toRadians(lon2 - lon1)

        val a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
                Math.cos(φ1) * Math.cos(φ2) *
                Math.sin(Δλ / 2) * Math.sin(Δλ / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return R * c
    }
}
