package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles LIMIT 1")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Query("UPDATE user_profiles SET balance = balance + :amount")
    suspend fun incrementBalance(amount: Double)

    @Query("UPDATE user_profiles SET balance = balance - :amount")
    suspend fun decrementBalance(amount: Double)

    @Query("DELETE FROM user_profiles")
    suspend fun clearAll()
}

@Dao
interface CourtDao {
    @Query("SELECT * FROM courts")
    fun getAllCourtsFlow(): Flow<List<Court>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourt(court: Court)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourts(courts: List<Court>)

    @Query("UPDATE courts SET isFavorite = :isFav WHERE id = :courtId")
    suspend fun updateFavorite(courtId: Int, isFav: Boolean)

    @Query("SELECT COUNT(*) FROM courts")
    suspend fun getCourtCount(): Int
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAllBookingsFlow(): Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<Booking>)

    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: Int, status: String)

    @Query("SELECT COUNT(*) FROM bookings")
    suspend fun getBookingCount(): Int
}

@Dao
interface MatchSlotDao {
    @Query("SELECT * FROM match_slots ORDER BY id DESC")
    fun getAllMatchSlotsFlow(): Flow<List<MatchSlot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchSlot(matchSlot: MatchSlot)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchSlots(matchSlots: List<MatchSlot>)

    @Query("UPDATE match_slots SET slotsMissing = :slots, joinedPlayers = :players, status = :status WHERE id = :slotId")
    suspend fun updateMatchSlotPlayers(slotId: Int, slots: Int, players: String, status: String)

    @Query("SELECT COUNT(*) FROM match_slots")
    suspend fun getMatchSlotCount(): Int
}

@Dao
interface CourtReviewDao {
    @Query("SELECT * FROM court_reviews WHERE courtId = :courtId ORDER BY date DESC")
    fun getReviewsByCourtFlow(courtId: Int): Flow<List<CourtReview>>

    @Query("SELECT * FROM court_reviews ORDER BY date DESC")
    fun getAllReviewsFlow(): Flow<List<CourtReview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: CourtReview)

    @Query("SELECT AVG(rating) FROM court_reviews WHERE courtId = :courtId")
    suspend fun getAverageRating(courtId: Int): Double

    @Query("SELECT * FROM court_reviews WHERE author = :author AND courtId = :courtId AND visitVerified = 1")
    suspend fun getVerifiedReview(author: String, courtId: Int): CourtReview?
}

@Dao
interface UserVideoProgressDao {
    @Query("SELECT * FROM user_video_progress WHERE userId = :userId ORDER BY date DESC LIMIT 1")
    suspend fun getLatestProgress(userId: Int): UserVideoProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserVideoProgress)

    @Query("UPDATE user_video_progress SET videosWatched = :count WHERE id = :id")
    suspend fun updateVideosWatched(id: Int, count: Int)

    @Query("UPDATE user_video_progress SET consecutiveDays = :days WHERE id = :id")
    suspend fun updateConsecutiveDays(id: Int, days: Int)

    @Query("UPDATE user_video_progress SET lastCouponDate = :date WHERE id = :id")
    suspend fun updateLastCouponDate(id: Int, date: String)
}

@Dao
interface FreePassDao {
    @Query("SELECT * FROM free_passes ORDER BY createdAt DESC")
    fun getAllPassesFlow(): Flow<List<FreePass>>

    @Query("SELECT * FROM free_passes WHERE playerId = :playerId AND status = 'active' ORDER BY createdAt DESC")
    fun getActivePlayerPassesFlow(playerId: Int): Flow<List<FreePass>>

    @Query("SELECT * FROM free_passes WHERE passCode = :code LIMIT 1")
    suspend fun getPassByCode(code: String): FreePass?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPass(pass: FreePass)

    @Query("UPDATE free_passes SET status = :status, usedAt = :usedAt, usedByOwner = :usedByOwner, usedAtCourtId = :usedAtCourtId WHERE id = :passId")
    suspend fun updatePassStatus(passId: Int, status: String, usedAt: String, usedByOwner: String, usedAtCourtId: Int)
}

@Dao
interface CourtVisitDao {
    @Query("SELECT * FROM court_visits WHERE userId = :userId AND courtId = :courtId ORDER BY visitDate DESC")
    suspend fun getUserVisitsToCourt(userId: Int, courtId: Int): List<CourtVisit>

    @Query("SELECT * FROM court_visits WHERE userId = :userId AND courtId = :courtId AND verified = 1 LIMIT 1")
    suspend fun getVerifiedVisit(userId: Int, courtId: Int): CourtVisit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: CourtVisit)

    @Query("UPDATE court_visits SET verified = 1 WHERE id = :id")
    suspend fun markVisitVerified(id: Int)

    @Query("SELECT COUNT(*) FROM court_visits WHERE userId = :userId AND courtId = :courtId AND verified = 1")
    suspend fun getVerifiedVisitCount(userId: Int, courtId: Int): Int
}

@Database(entities = [UserProfile::class, Court::class, Booking::class, MatchSlot::class, CourtReview::class, UserVideoProgress::class, CourtVisit::class, FreePass::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun courtDao(): CourtDao
    abstract fun bookingDao(): BookingDao
    abstract fun matchSlotDao(): MatchSlotDao
    abstract fun courtReviewDao(): CourtReviewDao
    abstract fun userVideoProgressDao(): UserVideoProgressDao
    abstract fun courtVisitDao(): CourtVisitDao
    abstract fun freePassDao(): FreePassDao
}

// Migration from version 4 to 5 (adding CourtVisit table)
val MIGRATION_4_5 = object : androidx.room.migration.Migration(4, 5) {
    override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS court_visits (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                userId INTEGER NOT NULL,
                courtId INTEGER NOT NULL,
                visitDate TEXT NOT NULL,
                bookingId INTEGER NOT NULL,
                verified INTEGER NOT NULL DEFAULT 0
            )
        """)
    }
}

// Migration from version 5 to 6 (adding password to user_profiles)
val MIGRATION_5_6 = object : androidx.room.migration.Migration(5, 6) {
    override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE user_profiles ADD COLUMN password TEXT NOT NULL DEFAULT ''")
    }
}

// Migration from version 6 to 7 (adding free_passes table)
val MIGRATION_6_7 = object : androidx.room.migration.Migration(6, 7) {
    override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS free_passes (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                playerId INTEGER NOT NULL,
                playerName TEXT NOT NULL,
                playerEmail TEXT NOT NULL,
                passCode TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'active',
                createdAt TEXT NOT NULL,
                usedAt TEXT NOT NULL DEFAULT '',
                usedByOwner TEXT NOT NULL DEFAULT '',
                usedAtCourtId INTEGER NOT NULL DEFAULT 0
            )
        """)
    }
}

// Migration from version 7 to 8 (adding availableHours to courts)
val MIGRATION_7_8 = object : androidx.room.migration.Migration(7, 8) {
    override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE courts ADD COLUMN availableHours TEXT NOT NULL DEFAULT '18:00,19:00,20:00,21:00,22:00,23:00'")
    }
}
