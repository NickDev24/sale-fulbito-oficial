package com.example.ui

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.BuildConfig
import com.example.ui.theme.Asphalt
import com.example.ui.theme.BorderGrey
import com.example.ui.theme.NeonGreen
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

private val TEST_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
private val TEST_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
private val TEST_REWARDED_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

private fun adUnitId(debugId: String, productionId: String): String {
    return if (BuildConfig.BUILD_TYPE == "debug") debugId else productionId
}

@Composable
fun AdMobBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = adUnitId(TEST_BANNER_UNIT_ID, BuildConfig.ADMOB_BANNER_UNIT_ID)
) {
    val context = LocalContext.current
    var isAdLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Black)
            .border(1.dp, BorderGrey),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                AdView(ctx).apply {
                    setAdSize(AdSize.BANNER)
                    this.adUnitId = adUnitId
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            isAdLoaded = true
                        }
                        override fun onAdFailedToLoad(error: LoadAdError) {
                            isAdLoaded = false
                        }
                    }
                    loadAd(AdRequest.Builder().build())
                }
            },
            update = {}
        )

        if (!isAdLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Asphalt),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SALE FULBITO SPONSOR",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonGreen.copy(alpha = 0.3f),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

class AdMobInterstitialManager(private val context: Context) {
    private var realInterstitialAd: InterstitialAd? = null
    var isAdLoading by mutableStateOf(false)

    val isRealAdLoaded: Boolean
        get() = realInterstitialAd != null

    init {
        loadAd()
    }

    fun loadAd() {
        if (isAdLoading) return
        isAdLoading = true
        val adRequest = AdRequest.Builder().build()
        val interstitialId = adUnitId(TEST_INTERSTITIAL_UNIT_ID, BuildConfig.ADMOB_INTERSTITIAL_UNIT_ID)
        InterstitialAd.load(
            context,
            interstitialId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    realInterstitialAd = interstitialAd
                    isAdLoading = false
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    realInterstitialAd = null
                    isAdLoading = false
                }
            }
        )
    }

    fun showAd(activity: Activity, onAdClosed: () -> Unit) {
        if (realInterstitialAd != null) {
            realInterstitialAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    realInterstitialAd = null
                    loadAd()
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                    realInterstitialAd = null
                    Toast.makeText(context, "Error al mostrar anuncio", Toast.LENGTH_SHORT).show()
                    onAdClosed()
                }
            }
            realInterstitialAd?.show(activity)
        } else {
            Toast.makeText(context, "Anuncio no disponible en este momento", Toast.LENGTH_SHORT).show()
            onAdClosed()
        }
    }

    fun cleanup() {
        realInterstitialAd = null
    }
}

enum class AdState { IDLE, LOADING, LOADED, FAILED }

class AdMobRewardedManager(private val context: Context) {
    private var realRewardedAd: RewardedAd? = null
    var state by mutableStateOf(AdState.IDLE)
    var isProcessingReward by mutableStateOf(false)
    var rewardAmount by mutableIntStateOf(500)
    private var retryCount = 0
    private var loadAttempted = false

    val isRealAdLoaded: Boolean
        get() = realRewardedAd != null

    val isRewardAvailable: Boolean
        get() = realRewardedAd != null && !isProcessingReward

    init {
        loadAd()
    }

    fun loadAd() {
        if (state == AdState.LOADING) return
        state = AdState.LOADING
        loadAttempted = true
        val adRequest = AdRequest.Builder().build()
        val rewardedId = adUnitId(TEST_REWARDED_UNIT_ID, BuildConfig.ADMOB_REWARDED_UNIT_ID)
        RewardedAd.load(
            context,
            rewardedId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    realRewardedAd = rewardedAd
                    state = AdState.LOADED
                    retryCount = 0
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    realRewardedAd = null
                    state = AdState.FAILED
                    retryCount++
                    if (retryCount < 5) {
                        val delay = (retryCount * 2000L).coerceAtMost(10000L)
                        android.os.Handler(context.mainLooper).postDelayed({
                            loadAd()
                        }, delay)
                    }
                }
            }
        )
    }

    fun showAd(activity: Activity, onRewardEarned: (Int) -> Unit) {
        if (realRewardedAd != null && !isProcessingReward) {
            isProcessingReward = true
            try {
                realRewardedAd?.show(activity) { rewardItem ->
                    val amount = rewardItem.amount.toInt().takeIf { it > 0 } ?: rewardAmount
                    rewardAmount = amount
                    onRewardEarned(amount)
                    isProcessingReward = false
                    realRewardedAd = null
                    loadAd()
                }
            } catch (e: Exception) {
                isProcessingReward = false
                realRewardedAd = null
                loadAd()
                Toast.makeText(context, "Error al mostrar anuncio: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (state == AdState.LOADING) {
                Toast.makeText(context, "El anuncio todavía se está cargando...", Toast.LENGTH_SHORT).show()
            } else {
                loadAd()
                Toast.makeText(context, "Cargando anuncio... intentá de nuevo en unos segundos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cleanup() {
        realRewardedAd = null
        isProcessingReward = false
        retryCount = 0
        state = AdState.IDLE
    }
}
