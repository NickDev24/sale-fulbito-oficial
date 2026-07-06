package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object SupabaseManager {
    private const val TAG = "SupabaseManager"

    // Retrieve keys from BuildConfig (injected via secrets plugin from .env / Secrets panel)
    val url: String = try { BuildConfig.SUPABASE_URL } catch (e: Exception) { "" }
    val anonKey: String = try { BuildConfig.SUPABASE_ANON_KEY } catch (e: Exception) { "" }

    val isConfigured: Boolean
        get() = url.isNotEmpty() && url.startsWith("http") && anonKey.isNotEmpty() && anonKey != "your-anon-key"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    init {
        if (isConfigured) {
            Log.d(TAG, "Supabase configured successfully with URL: $url")
        } else {
            Log.w(TAG, "Supabase is NOT configured. App running in offline local-only Room mode.")
        }
    }

    // --- USER PROFILE SYNC ---
    suspend fun syncUserProfile(profile: UserProfile): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                put("fullName", profile.fullName)
                put("email", profile.email)
                put("phone", profile.phone)
                put("role", profile.role)
                put("position", profile.position)
                put("level", profile.level)
                put("complexName", profile.complexName)
                put("complexAddress", profile.complexAddress)
                put("cuit", profile.cuit)
                put("password", profile.password)
                put("balance", profile.balance)
            }

            // Using upsert in Supabase (on_conflict email)
            val requestBody = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$url/rest/v1/user_profiles?on_conflict=email")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "resolution=merge-duplicates")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(TAG, "Sync UserProfile failed: ${response.code} - ${response.body?.string()}")
                    return@withContext false
                }
                Log.d(TAG, "UserProfile synced successfully with Supabase.")
                return@withContext true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing UserProfile", e)
            return@withContext false
        }
    }

    suspend fun fetchUserProfile(email: String): UserProfile? = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext null
        try {
            val request = Request.Builder()
                .url("$url/rest/v1/user_profiles?email=eq.$email&select=*")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val bodyStr = response.body?.string() ?: "[]"
                val jsonArray = JSONArray(bodyStr)
                if (jsonArray.length() > 0) {
                    val obj = jsonArray.getJSONObject(0)
                    return@withContext UserProfile(
                        fullName = obj.optString("fullName", ""),
                        email = obj.optString("email", ""),
                        phone = obj.optString("phone", ""),
                        role = obj.optString("role", "JUGADOR"),
                        position = obj.optString("position", ""),
                        level = obj.optString("level", ""),
                        complexName = obj.optString("complexName", ""),
                        complexAddress = obj.optString("complexAddress", ""),
                        cuit = obj.optString("cuit", ""),
                        password = obj.optString("password", ""),
                        balance = obj.optDouble("balance", 0.0)
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching UserProfile", e)
        }
        return@withContext null
    }

    // --- COURTS SYNC ---
    suspend fun fetchCourts(): List<Court> = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext emptyList()
        try {
            val request = Request.Builder()
                .url("$url/rest/v1/courts?select=*")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val bodyStr = response.body?.string() ?: "[]"
                val jsonArray = JSONArray(bodyStr)
                val list = mutableListOf<Court>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        Court(
                            id = obj.optInt("id", 0),
                            name = obj.optString("name", ""),
                            address = obj.optString("address", ""),
                            neighborhood = obj.optString("neighborhood", ""),
                            type = obj.optString("type", "F5"),
                            surface = obj.optString("surface", "Sintético"),
                            pricePerHour = obj.optDouble("pricePerHour", 0.0),
                            imageUrl = obj.optString("imageUrl", ""),
                            rating = obj.optDouble("rating", 0.0).toFloat(),
                            isFavorite = obj.optBoolean("isFavorite", false),
                            isOwnerCourt = obj.optBoolean("isOwnerCourt", false),
                            latitude = obj.optDouble("latitude", 0.0),
                            longitude = obj.optDouble("longitude", 0.0)
                        )
                    )
                }
                return@withContext list
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching Courts", e)
            return@withContext emptyList()
        }
    }

    suspend fun pushCourt(court: Court): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                if (court.id > 0) put("id", court.id)
                put("name", court.name)
                put("address", court.address)
                put("neighborhood", court.neighborhood)
                put("type", court.type)
                put("surface", court.surface)
                put("pricePerHour", court.pricePerHour)
                put("imageUrl", court.imageUrl)
                put("rating", court.rating.toDouble())
                put("isFavorite", court.isFavorite)
                put("isOwnerCourt", court.isOwnerCourt)
                put("latitude", court.latitude)
                put("longitude", court.longitude)
            }

            val requestBody = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$url/rest/v1/courts")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "resolution=merge-duplicates")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                return@withContext response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error pushing Court", e)
            return@withContext false
        }
    }

    // --- BOOKINGS SYNC ---
    suspend fun fetchBookings(): List<Booking> = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext emptyList()
        try {
            val request = Request.Builder()
                .url("$url/rest/v1/bookings?select=*")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val bodyStr = response.body?.string() ?: "[]"
                val jsonArray = JSONArray(bodyStr)
                val list = mutableListOf<Booking>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        Booking(
                            id = obj.optInt("id", 0),
                            courtId = obj.optInt("courtId", 0),
                            courtName = obj.optString("courtName", ""),
                            courtType = obj.optString("courtType", ""),
                            date = obj.optString("date", ""),
                            timeSlot = obj.optString("timeSlot", ""),
                            price = obj.optDouble("price", 0.0),
                            status = obj.optString("status", ""),
                            teamName = obj.optString("teamName", ""),
                            isMyBooking = obj.optBoolean("isMyBooking", false)
                        )
                    )
                }
                return@withContext list
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching Bookings", e)
            return@withContext emptyList()
        }
    }

    suspend fun pushBooking(booking: Booking): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                if (booking.id > 0) put("id", booking.id)
                put("courtId", booking.courtId)
                put("courtName", booking.courtName)
                put("courtType", booking.courtType)
                put("date", booking.date)
                put("timeSlot", booking.timeSlot)
                put("price", booking.price)
                put("status", booking.status)
                put("teamName", booking.teamName)
                put("isMyBooking", booking.isMyBooking)
            }

            val requestBody = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$url/rest/v1/bookings")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "resolution=merge-duplicates")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                return@withContext response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error pushing Booking", e)
            return@withContext false
        }
    }

    suspend fun updateBookingStatus(bookingId: Int, status: String): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                put("status", status)
            }

            val requestBody = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$url/rest/v1/bookings?id=eq.$bookingId")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .patch(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                return@withContext response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error patching Booking status", e)
            return@withContext false
        }
    }

    // --- MATCH SLOTS SYNC ---
    suspend fun fetchMatchSlots(): List<MatchSlot> = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext emptyList()
        try {
            val request = Request.Builder()
                .url("$url/rest/v1/match_slots?select=*")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val bodyStr = response.body?.string() ?: "[]"
                val jsonArray = JSONArray(bodyStr)
                val list = mutableListOf<MatchSlot>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        MatchSlot(
                            id = obj.optInt("id", 0),
                            courtId = obj.optInt("courtId", 0),
                            courtName = obj.optString("courtName", ""),
                            neighborhood = obj.optString("neighborhood", ""),
                            date = obj.optString("date", ""),
                            timeSlot = obj.optString("timeSlot", ""),
                            teamName = obj.optString("teamName", ""),
                            matchType = obj.optString("matchType", ""),
                            slotsMissing = obj.optInt("slotsMissing", 0),
                            roleNeeded = obj.optString("roleNeeded", ""),
                            pricePerPlayer = obj.optDouble("pricePerPlayer", 0.0),
                            joinedPlayers = obj.optString("joinedPlayers", ""),
                            status = obj.optString("status", "Abierto")
                        )
                    )
                }
                return@withContext list
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching MatchSlots", e)
            return@withContext emptyList()
        }
    }

    suspend fun pushMatchSlot(slot: MatchSlot): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                if (slot.id > 0) put("id", slot.id)
                put("courtId", slot.courtId)
                put("courtName", slot.courtName)
                put("neighborhood", slot.neighborhood)
                put("date", slot.date)
                put("timeSlot", slot.timeSlot)
                put("teamName", slot.teamName)
                put("matchType", slot.matchType)
                put("slotsMissing", slot.slotsMissing)
                put("roleNeeded", slot.roleNeeded)
                put("pricePerPlayer", slot.pricePerPlayer)
                put("joinedPlayers", slot.joinedPlayers)
                put("status", slot.status)
            }

            val requestBody = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$url/rest/v1/match_slots")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "resolution=merge-duplicates")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                return@withContext response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error pushing MatchSlot", e)
            return@withContext false
        }
    }

    suspend fun updateMatchSlotPlayers(slotId: Int, slotsMissing: Int, joinedPlayers: String, status: String): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                put("slotsMissing", slotsMissing)
                put("joinedPlayers", joinedPlayers)
                put("status", status)
            }

            val requestBody = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$url/rest/v1/match_slots?id=eq.$slotId")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .patch(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                return@withContext response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error patching MatchSlot players", e)
            return@withContext false
        }
    }

    // --- DEVICE TOKEN SYNC (for push notifications) ---
    suspend fun syncDeviceToken(token: String): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                put("token", token)
                put("platform", "android")
                put("updated_at", java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", java.util.Locale.US).format(java.util.Date()))
            }

            val requestBody = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$url/rest/v1/device_tokens")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "resolution=merge-duplicates")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(TAG, "Sync device token failed: ${response.code} - ${response.body?.string()}")
                    return@withContext false
                }
                Log.d(TAG, "Device token synced successfully.")
                return@withContext true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing device token", e)
            return@withContext false
        }
    }
}
