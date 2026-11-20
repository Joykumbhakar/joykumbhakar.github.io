package com.securething.vault

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.ui.text.input.KeyboardCapitalization
import com.securething.vault.R


/**
 * SECURETHING VAULT - Apple Aesthetic Design
 * Version: 2.0 PREMIUM
 *
 * Design Philosophy:
 * - Clean, minimalist Apple-style interface
 * - Soft shadows and subtle elevations
 * - Generous spacing and padding
 * - Frosted glass effects
 * - Smooth, fluid animations
 * - Premium iOS-inspired color palette
 */

// ============================================================================
// APPLE DESIGN SYSTEM - Color Palette
// ============================================================================

object AppleColors {
    val verticalGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF059200),
            Color(0xFFB7CDF3),
            Color(0xFFA82800)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )
    // Light Theme
    val LightBackground = Color(0xFFF2F2F7)
    val LightSurface = Color(0xFFFFFFFF)
    val LightPrimary = Color(0xFF007AFF)
    val LightSecondary = Color(0xFF5856D6)
    val LightTertiary = Color(0xFFAF52DE)

    // Card backgrounds with subtle transparency
    val LightCard = Color(0xFFFBFBFD)
    val LightCardElevated = Color(0xFFFFFFFF)

    // Text colors
    val LightText = Color(0xFF000000)
    val LightTextSecondary = Color(0xFF8E8E93)
    val LightTextTertiary = Color(0xFFC7C7CC)

    // Separator
    val LightSeparator = Color(0xFFC6C6C8)

    // System colors
    val SystemBlue = Color(0xFF007AFF)
    val SystemGreen = Color(0xFF34C759)
    val SystemRed = Color(0xFFFF3B30)
    val SystemOrange = Color(0xFFFF9500)
    val SystemPurple = Color(0xFF5856D6)
    val SystemPink = Color(0xFFFF2D55)
    val SystemTeal = Color(0xFF5AC8FA)

    // Dark Theme
    val DarkBackground = Color(0xFF000000)
    val DarkSurface = Color(0xFF1C1C1E)
    val DarkCard = Color(0xFF2C2C2E)
    val DarkCardElevated = Color(0xFF3A3A3C)
    val DarkText = Color(0xFFFFFFFF)
    val DarkTextSecondary = Color(0xFF8E8E93)
    val DarkSeparator = Color(0xFF38383A)
}

// ============================================================================
// DATA STORE EXTENSION
// ============================================================================
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "securething_vault_v5_apple")

// ============================================================================
// DATA MODELS
// ============================================================================

data class User(
    val name: String = "",
    val gender: String = "male",
    val authType: String = "pin",
    val pin: String = "",
    val password: String = ""
)

data class Security(
    val q1: String = "",
    val a1: String = "",
    val q2: String = "",
    val a2: String = ""
)

data class VaultFile(
    val id: String = "",
    val name: String = "",
    val type: String = "file",
    val data: String = "",
    val size: Long = 0,
    val fav: Boolean = false,
    val date: String = "",
    val deletedDate: String = "",
    val thumbnail: String = ""
)

data class PasswordEntry(
    val id: Long = 0L,
    val platform: String = "",
    val username: String = "",
    val password: String = "",
    val note: String = "",
    var revealed: Boolean = false
)

data class CardEntry(
    val id: String = "",
    val type: String = "visa",
    val holder: String = "",
    val number: String = "",
    val expiry: String = "", // Used as "Valid Thru" for CC, "Issue Date" for Gov
    val cvv: String = "",
    // New Fields for Govt IDs
    val dob: String = "",
    val address: String = "",
    val relation: String = "", // S/o, D/o, C/o
    val photo: String = "" // Base64 encoded image string
)

data class HistoryEntry(
    val date: String = "",
    val reason: String = "",
    val img: String = ""
)

data class AppSettings(
    val theme: String = "light",
    val autoLockEnabled: Boolean = true,
    val autoLockMinutes: Int = 1
)

data class AppData(
    val user: User = User(),
    val security: Security = Security(),
    val files: List<VaultFile> = emptyList(),
    val passwords: List<PasswordEntry> = emptyList(),
    val cards: List<CardEntry> = emptyList(),
    val history: List<HistoryEntry> = emptyList(),
    val recycleBin: List<VaultFile> = emptyList(),
    val settings: AppSettings = AppSettings(),
    val init: Boolean = false,
    val version: String = "2.0"
)

// ============================================================================
// ENCRYPTION UTILITIES
// ============================================================================

object CryptoUtil {
    private const val ALGORITHM = "AES/ECB/PKCS5Padding"
    private const val SECRET_KEY = "securething-master-key-salt-32b"

    private fun getKey(): SecretKeySpec {
        val sha = MessageDigest.getInstance("SHA-256")
        val key = sha.digest(SECRET_KEY.toByteArray())
        // Note: The algorithm name for the KEY remains "AES"
        return SecretKeySpec(key, "AES")
    }

    fun encrypt(data: String): String {
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, getKey())
            val encrypted = cipher.doFinal(data.toByteArray())
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun decrypt(data: String): String? {
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, getKey())
            val decrypted = cipher.doFinal(Base64.decode(data, Base64.NO_WRAP))
            String(decrypted)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

// ============================================================================
// OPTIMIZED FILE UTILITIES
// ============================================================================

object FileUtil {
    private const val MAX_IMAGE_SIZE = 1024 * 1024
    private const val THUMBNAIL_SIZE = 200
    private const val COMPRESSION_QUALITY = 85

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int = 2048, reqHeight: Int = 2048): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun compressImage(bitmap: Bitmap?, maxSizeBytes: Long = MAX_IMAGE_SIZE.toLong()): Bitmap? {
        if (bitmap == null) return null
        var quality = COMPRESSION_QUALITY
        var compressed: ByteArray

        do {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            compressed = outputStream.toByteArray()
            quality -= 5
        } while (compressed.size > maxSizeBytes && quality > 10)

        return BitmapFactory.decodeByteArray(compressed, 0, compressed.size) ?: bitmap
    }

    fun createThumbnail(bitmap: Bitmap): Bitmap {
        val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val width = if (ratio > 1) THUMBNAIL_SIZE else (THUMBNAIL_SIZE * ratio).toInt().coerceAtLeast(1)
        val height = if (ratio > 1) (THUMBNAIL_SIZE / ratio).toInt().coerceAtLeast(1) else THUMBNAIL_SIZE

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    suspend fun uriToOptimizedBase64(context: Context, uri: Uri): Pair<String, String>? = withContext(Dispatchers.IO) {
        try {
            val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, boundsOptions)
            }

            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = calculateInSampleSize(boundsOptions, 2048, 2048)
            }

            val originalBitmap = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, decodeOptions)
            } ?: return@withContext null

            val compressedBitmap = compressImage(originalBitmap) ?: originalBitmap
            val mainOutputStream = ByteArrayOutputStream()
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, mainOutputStream)
            val mainBase64 = Base64.encodeToString(mainOutputStream.toByteArray(), Base64.NO_WRAP)

            val thumbnail = createThumbnail(compressedBitmap)
            val thumbOutputStream = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, thumbOutputStream)
            val thumbBase64 = Base64.encodeToString(thumbOutputStream.toByteArray(), Base64.NO_WRAP)

            if (compressedBitmap != originalBitmap) {
                compressedBitmap.recycle()
            }
            originalBitmap.recycle()
            thumbnail.recycle()

            mainBase64 to thumbBase64
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun uriToBase64(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFileSize(context: Context, uri: Uri): Long {
        return try {
            context.contentResolver.openInputStream(uri)?.use { it.available().toLong() } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    fun getFileName(context: Context, uri: Uri): String {
        var name = "unknown"
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }

    fun getFileType(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri) ?: return "file"
        return when {
            mimeType.startsWith("image/") -> "photo"
            mimeType.startsWith("video/") -> "video"
            mimeType.startsWith("audio/") -> "audio"
            else -> "file"
        }
    }

    suspend fun exportFile(context: Context, uri: Uri, base64Data: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val bytes = Base64.decode(base64Data, Base64.NO_WRAP)
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(bytes)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
// ============================================================================
// 1. CLAYMORPHISM HELPER MODIFIERS
// ============================================================================

/**
 * Creates a "Floating Bubble" effect.
 * Uses a drop shadow + an inner top-left highlight to simulate curvature.
 */
fun Modifier.clayShadow(
    color: Color = Color(0xFFF0F0F3), // Base surface color
    borderRadius: Dp = 50.dp,
    spread: Dp = 8.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = Color.Transparent.toArgb()

            val isDark = color.luminance() < 0.5f
            val darkShadowColor = if (isDark) Color.Black.copy(alpha = 0.6f) else Color(0xFFA3B1C6).copy(alpha = 0.4f)
            val lightShadowColor = if (isDark) Color.White.copy(alpha = 0.08f) else Color.White

            // 1. Outer Dark Shadow (Bottom Right)
            frameworkPaint.setShadowLayer(
                spread.toPx(),
                spread.toPx() / 2,
                spread.toPx() / 2,
                darkShadowColor.toArgb()
            )
            it.drawRoundRect(
                0f, 0f, size.width, size.height,
                borderRadius.toPx(), borderRadius.toPx(),
                paint
            )

            // 2. Outer Light Shadow (Top Left) - Simulates light source
            frameworkPaint.setShadowLayer(
                spread.toPx(),
                -spread.toPx() / 2,
                -spread.toPx() / 2,
                lightShadowColor.toArgb()
            )
            it.drawRoundRect(
                0f, 0f, size.width, size.height,
                borderRadius.toPx(), borderRadius.toPx(),
                paint
            )
        }
    }
)

/**
 * Creates an "Inset/Pressed" effect.
 * Simulates a hole pressed into the clay surface.
 */
fun Modifier.clayInset(
    color: Color = Color(0xFFF0F0F3),
    borderRadius: Dp = 50.dp
) = this.drawBehind {
    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = color.toArgb()

        val isDark = color.luminance() < 0.5f
        val darkShadowColor = if (isDark) Color.Black.copy(alpha = 0.5f) else Color(0xFFA3B1C6).copy(alpha = 0.3f)
        val lightShadowColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.8f)

        // Inner Dark Shadow (Top Left)
        frameworkPaint.setShadowLayer(
            6.dp.toPx(),
            3.dp.toPx(),
            3.dp.toPx(),
            darkShadowColor.toArgb()
        )
        it.drawRoundRect(
            0f, 0f, size.width, size.height,
            borderRadius.toPx(), borderRadius.toPx(),
            paint
        )

        // Inner Light Shadow (Bottom Right)
        frameworkPaint.setShadowLayer(
            6.dp.toPx(),
            -3.dp.toPx(),
            -3.dp.toPx(),
            lightShadowColor.toArgb()
        )
        it.drawRoundRect(
            0f, 0f, size.width, size.height,
            borderRadius.toPx(), borderRadius.toPx(),
            paint
        )
    }
}
// ============================================================================
// OPTIMIZED DATA REPOSITORY
// ============================================================================

class DataRepository(private val context: Context) {
    private val gson = Gson()
    private val dataKey = stringPreferencesKey("app_data")
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var saveJob: Job? = null

    suspend fun saveAppData(appData: AppData) = withContext(Dispatchers.IO) {
        // 1. Remove saveJob logic
        // 2. Remove delay(300)
        // 3. Save immediately
        try {
            val json = gson.toJson(appData)

            // Make sure your CryptoUtil.encrypt is using "AES/ECB/PKCS5Padding"
            val encrypted = CryptoUtil.encrypt(json)

            context.dataStore.edit { preferences ->
                preferences[dataKey] = encrypted
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun loadAppData(): Flow<AppData> {
        return context.dataStore.data
            .map { preferences ->
                try {
                    val encrypted = preferences[dataKey] ?: ""
                    if (encrypted.isEmpty()) {
                        AppData()
                    } else {
                        val json = CryptoUtil.decrypt(encrypted)
                        if (!json.isNullOrBlank()) {
                            gson.fromJson(json, AppData::class.java) ?: AppData()
                        } else {
                            AppData()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    AppData()
                }
            }
            .catch { e ->
                e.printStackTrace()
                emit(AppData())
            }
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        try {
            context.dataStore.edit { it.clear() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
// ============================================================================
// MAIN ACTIVITY WITH APPLE SPLASH SCREEN
// ============================================================================

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        setContent {
            LaunchedEffect(Unit) {
                delay(1200)
                keepSplashScreen = false
            }

            SecureThingApp()
        }
    }
}

// ============================================================================
// APPLE THEME SETUP
// ============================================================================

@Composable
fun AppleColorScheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = AppleColors.SystemBlue,
            onPrimary = Color.White,
            primaryContainer = AppleColors.SystemBlue.copy(alpha = 0.2f),
            onPrimaryContainer = AppleColors.SystemBlue,

            secondary = AppleColors.SystemPurple,
            onSecondary = Color.White,
            secondaryContainer = AppleColors.SystemPurple.copy(alpha = 0.2f),
            onSecondaryContainer = AppleColors.SystemPurple,

            background = AppleColors.DarkBackground,
            onBackground = AppleColors.DarkText,

            surface = AppleColors.DarkSurface,
            onSurface = AppleColors.DarkText,

            surfaceVariant = AppleColors.DarkCard,
            onSurfaceVariant = AppleColors.DarkTextSecondary,

            outline = AppleColors.DarkSeparator,

            error = AppleColors.SystemRed,
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = AppleColors.SystemBlue,
            onPrimary = Color.White,
            primaryContainer = AppleColors.SystemBlue.copy(alpha = 0.1f),
            onPrimaryContainer = AppleColors.SystemBlue,

            secondary = AppleColors.SystemPurple,
            onSecondary = Color.White,
            secondaryContainer = AppleColors.SystemPurple.copy(alpha = 0.1f),
            onSecondaryContainer = AppleColors.SystemPurple,

            background = AppleColors.LightBackground,
            onBackground = AppleColors.LightText,

            surface = AppleColors.LightSurface,
            onSurface = AppleColors.LightText,

            surfaceVariant = AppleColors.LightCard,
            onSurfaceVariant = AppleColors.LightTextSecondary,

            outline = AppleColors.LightSeparator,

            error = AppleColors.SystemRed,
            onError = Color.White
        )
    }
}

// ============================================================================
// MAIN APP COMPOSABLE WITH APPLE DESIGN
// ============================================================================

@Composable
fun SecureThingApp() {
    val context = LocalContext.current
    val repository = remember { DataRepository(context) }

    val appData by repository.loadAppData().collectAsState(initial = AppData())
    var currentAppData by remember { mutableStateOf(AppData()) }
    var isDataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(appData) {
        currentAppData = appData
        isDataLoaded = true
    }

    val saveData: (AppData) -> Unit = remember {
        { data ->
            currentAppData = data
            CoroutineScope(Dispatchers.IO).launch {
                repository.saveAppData(data)
            }
        }
    }

    val isDarkTheme = currentAppData.settings.theme == "dark"

    MaterialTheme(
        colorScheme = AppleColorScheme(isDarkTheme)
    ) {
        if (!isDataLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = AppleColors.SystemBlue,
                    strokeWidth = 2.dp
                )
            }
        } else {
            SecureThingNavigation(
                appData = currentAppData,
                onSaveData = saveData,
                repository = repository
            )
        }
    }
}

// ============================================================================
// NAVIGATION & SCREEN MANAGEMENT
// ============================================================================

@Composable
fun SecureThingNavigation(
    appData: AppData,
    onSaveData: (AppData) -> Unit,
    repository: DataRepository
) {
    var currentScreen by remember {
        mutableStateOf(
            if (appData.init) "login" else "launch"
        )
    }

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current
    var autoLockJob by remember { mutableStateOf<Job?>(null) }
    var pauseTimestamp by remember { mutableLongStateOf(0L) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (appData.init && currentScreen == "main" && appData.settings.autoLockEnabled) {
                        pauseTimestamp = System.currentTimeMillis()
                        autoLockJob = CoroutineScope(Dispatchers.Main).launch {
                            delay(appData.settings.autoLockMinutes * 60000L)
                            currentScreen = "login"
                            snackbarMessage = "App Auto-Locked"
                            showSnackbar = true
                        }
                    }
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (appData.init && currentScreen == "main" && appData.settings.autoLockEnabled && pauseTimestamp > 0L) {
                        val elapsed = System.currentTimeMillis() - pauseTimestamp
                        if (elapsed >= appData.settings.autoLockMinutes * 60000L) {
                            currentScreen = "login"
                            snackbarMessage = "App Auto-Locked"
                            showSnackbar = true
                        }
                    }
                    pauseTimestamp = 0L
                    autoLockJob?.cancel()
                    autoLockJob = null
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            autoLockJob?.cancel()
        }
    }

    LaunchedEffect(appData.init) {
        if (currentScreen == "launch") {
            delay(1800)
            currentScreen = if (!appData.init) "register" else "login"
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(snackbarMessage)
            showSnackbar = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) togetherWith
                        fadeOut(animationSpec = tween(200)) +
                        slideOutHorizontally(
                            targetOffsetX = { -it / 2 },
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                "launch" -> LaunchScreen()
                "welcome" -> WelcomeScreen(
                    userName = appData.user.name,
                    onComplete = { currentScreen = "main" }
                )
                "register" -> RegisterScreen(
                    onNext = { user ->
                        onSaveData(appData.copy(user = user))
                        currentScreen = "setup_lock"
                    }
                )
                "security_setup" -> SecuritySetupScreen(
                    appData = appData,
                    onComplete = { security ->
                        onSaveData(appData.copy(security = security, init = true))
                        currentScreen = "welcome"
                    }
                )
                "setup_lock" -> LockSetupScreen(
                    authType = appData.user.authType,
                    onComplete = { pin, password ->
                        val updatedUser = appData.user.copy(pin = pin, password = password)
                        onSaveData(appData.copy(user = updatedUser))
                        currentScreen = "security_setup"
                    }
                )
                "login" -> LoginScreen(
                    user = appData.user,
                    onLoginSuccess = { currentScreen = "main" },
                    onLoginFailed = {
                        val newHistory = HistoryEntry(
                            date = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(Date()),
                            reason = "Auth Failed"
                        )
                        onSaveData(appData.copy(history = listOf(newHistory) + appData.history))
                        snackbarMessage = "Access Denied"
                        showSnackbar = true
                    },
                    onForgotCredentials = { currentScreen = "recovery" }
                )
                "recovery" -> RecoveryScreen(
                    security = appData.security,
                    authType = appData.user.authType,
                    onRecoverySuccess = { pin, password ->
                        val updatedUser = appData.user.copy(pin = pin, password = password)
                        onSaveData(appData.copy(user = updatedUser))
                        snackbarMessage = "Reset Successful. Please Login."
                        showSnackbar = true
                        currentScreen = "login"
                    },
                    onCancel = { currentScreen = "login" }
                )
                "main" -> MainAppScreen(
                    appData = appData,
                    onSaveData = onSaveData,
                    onLogout = { currentScreen = "login" },
                    onShowSnackbar = { message ->
                        snackbarMessage = message
                        showSnackbar = true
                    },
                    repository = repository
                )
            }
        }

        // Apple-style Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            snackbar = { data ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = AppleColors.SystemGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = data.visuals.message,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        )
    }
}

// ============================================================================
// LAUNCH SCREEN - APPLE STYLE
// ============================================================================

@Composable
fun LaunchScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "launch_animation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppleColors.SystemBlue,
                        AppleColors.SystemBlue.copy(alpha = 0.9f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Apple-style icon container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(32.dp),
                        spotColor = Color.Black.copy(alpha = 0.25f)
                    )
                    .background(
                        Color.White.copy(alpha = 0.15f),
                        RoundedCornerShape(32.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(64.dp)
                        .graphicsLayer(alpha = alpha),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SecureThing",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 38.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = Color.White,
                fontWeight = FontWeight.W600
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Privacy, Our Priority",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    letterSpacing = (-0.2).sp
                ),
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.W400
            )
        }
    }
}

// ============================================================================
// WELCOME SCREEN - APPLE STYLE
// ============================================================================

@Composable
fun WelcomeScreen(userName: String, onComplete: () -> Unit) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        }
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = 0.6f,
                stiffness = 200f
            )
        )
        delay(2200)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    alpha = alpha.value
                )
        ) {
            // Success icon with Apple styling
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        AppleColors.SystemGreen.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = AppleColors.SystemGreen
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome,",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    letterSpacing = (-0.3).sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = userName.split(" ").first(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 34.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your vault is secured and ready",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    letterSpacing = (-0.2).sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W400
            )
        }
    }
}

// ============================================================================
// APPLE-STYLE UTILITY COMPOSABLES
// ============================================================================

/**
 * Apple-style Card with subtle shadow and proper elevation
 */
@Composable
fun AppleCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Card(
        modifier = cardModifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f),
                ambientColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(content = content)
    }
}

/**
 * Apple-style Button with proper sizing and styling
 */
@Composable
fun AppleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AppleColors.SystemBlue,
        contentColor = Color.White
    ),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        content = content
    )
}

/**
 * Apple-style Section Header
 */
@Composable
fun AppleSectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.W600,
        modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

/**
 * Apple-style Divider
 */
@Composable
fun AppleDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )
}
// ============================================================================
// REGISTER SCREEN - APPLE STYLE
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onNext: (User) -> Unit) {
    var username by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("male") }
    var authType by remember { mutableStateOf("pin") }

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            1f,
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .graphicsLayer(alpha = animatedProgress.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Header Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        AppleColors.SystemBlue.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = AppleColors.SystemBlue
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Your Profile",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Secure your digital life",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    letterSpacing = (-0.2).sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Gender Selection - Apple Style
            AppleSectionHeader("SELECT GENDER")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppleGenderCard(
                    gender = "male",
                    isSelected = selectedGender == "male",
                    onClick = { selectedGender = "male" },
                    modifier = Modifier.weight(1f)
                )
                AppleGenderCard(
                    gender = "female",
                    isSelected = selectedGender == "female",
                    onClick = { selectedGender = "female" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Username Input
            AppleSectionHeader("USERNAME")

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = {
                    Text(
                        "Enter your name",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppleColors.SystemBlue,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Security Method Selection
            AppleSectionHeader("SECURITY METHOD")

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = when (authType) {
                        "pin" -> "4-Digit PIN"
                        "password" -> "Strong Password"
                        else -> "Biometric / Screen Lock"
                    },
                    onValueChange = {},
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            when (authType) {
                                "pin" -> Icons.Filled.Pin
                                "password" -> Icons.Filled.Password
                                else -> Icons.Filled.Fingerprint
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleColors.SystemBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "4-Digit PIN",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            authType = "pin"
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Pin, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Strong Password",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            authType = "password"
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Password, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Biometric / Screen Lock",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            authType = "biometric"
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Fingerprint, contentDescription = null)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Continue Button
            AppleButton(
                onClick = {
                    if (username.isNotEmpty()) {
                        onNext(User(username, selectedGender, authType))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotEmpty()
            ) {
                Text(
                    "Continue",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    letterSpacing = (-0.2).sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AppleGenderCard(
    gender: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "scale"
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(
                elevation = if (isSelected) 8.dp else 1.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                AppleColors.SystemBlue.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected)
            BorderStroke(2.dp, AppleColors.SystemBlue)
        else
            BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (gender == "male")
                            AppleColors.SystemBlue.copy(alpha = 0.15f)
                        else
                            AppleColors.SystemPink.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (gender == "male") Icons.Filled.Male else Icons.Filled.Female,
                    contentDescription = gender,
                    modifier = Modifier.size(32.dp),
                    tint = if (gender == "male") AppleColors.SystemBlue else AppleColors.SystemPink
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = gender.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 17.sp,
                    letterSpacing = (-0.2).sp
                ),
                fontWeight = FontWeight.W600,
                color = if (isSelected)
                    AppleColors.SystemBlue
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ============================================================================
// SECURITY SETUP SCREEN - APPLE STYLE
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecuritySetupScreen(
    appData: AppData,
    onComplete: (Security) -> Unit
) {
    var q1Index by remember { mutableStateOf(0) }
    var a1 by remember { mutableStateOf("") }
    var q2Index by remember { mutableStateOf(0) }
    var a2 by remember { mutableStateOf("") }

    val questions1 = listOf(
        "What was your first pet's name?",
        "What elementary school did you attend?",
        "In what city were you born?"
    )

    val questions2 = listOf(
        "What is your favorite movie?",
        "What is your favorite food?",
        "What was your first car model?"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Header Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        AppleColors.SystemPurple.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = AppleColors.SystemPurple
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Security Questions",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "These help you recover your account",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    letterSpacing = (-0.2).sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Question 1
            AppleCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "QUESTION 1",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = AppleColors.SystemBlue,
                        fontWeight = FontWeight.W600
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var expanded1 by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded1,
                        onExpandedChange = { expanded1 = !expanded1 }
                    ) {
                        OutlinedTextField(
                            value = questions1[q1Index],
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppleColors.SystemBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded1,
                            onDismissRequest = { expanded1 = false }
                        ) {
                            questions1.forEachIndexed { index, question ->
                                DropdownMenuItem(
                                    text = { Text(question) },
                                    onClick = {
                                        q1Index = index
                                        expanded1 = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = a1,
                        onValueChange = { a1 = it },
                        placeholder = { Text("Your answer") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppleColors.SystemBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Question 2
            AppleCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "QUESTION 2",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = AppleColors.SystemBlue,
                        fontWeight = FontWeight.W600
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var expanded2 by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded2,
                        onExpandedChange = { expanded2 = !expanded2 }
                    ) {
                        OutlinedTextField(
                            value = questions2[q2Index],
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppleColors.SystemBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false }
                        ) {
                            questions2.forEachIndexed { index, question ->
                                DropdownMenuItem(
                                    text = { Text(question) },
                                    onClick = {
                                        q2Index = index
                                        expanded2 = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = a2,
                        onValueChange = { a2 = it },
                        placeholder = { Text("Your answer") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppleColors.SystemBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            AppleButton(
                onClick = {
                    if (a1.isNotEmpty() && a2.isNotEmpty()) {
                        onComplete(
                            Security(
                                q1 = questions1[q1Index],
                                a1 = a1.lowercase().trim(),
                                q2 = questions2[q2Index],
                                a2 = a2.lowercase().trim()
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = a1.isNotEmpty() && a2.isNotEmpty()
            ) {
                Text(
                    "Complete Setup",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    letterSpacing = (-0.2).sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

fun calculatePasswordStrength(password: String): Int {
    var strength = 0
    if (password.length >= 6) strength++
    if (password.length >= 8) strength++
    if (password.any { it.isDigit() } && password.any { it.isLetter() }) strength++
    if (password.any { !it.isLetterOrDigit() }) strength++
    return strength.coerceAtMost(4)
}

// ============================================================================
// 2. MAIN KEYBOARD COMPONENT
// ============================================================================

@Composable
fun ApplePinKeyboard(
    pinInput: String,
    onPinChanged: (String) -> Unit,
    onDelete: () -> Unit,
    onBiometricClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showDots: Boolean = true
) {
    val haptic = LocalHapticFeedback.current

    // Adaptive theme color for Claymorphism
    val isDark = MaterialTheme.colorScheme.surface == AppleColors.DarkSurface || MaterialTheme.colorScheme.background == AppleColors.DarkBackground
    val baseColor = if (isDark) AppleColors.DarkCard else Color(0xFFF0F0F3)

    // Responsive Sizing
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val buttonSize = if (screenHeight < 600.dp) 65.dp else 80.dp
    val spacing = if (screenHeight < 600.dp) 16.dp else 24.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(baseColor) // Important: Clay needs this background color context
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. PIN Dots (Clay Style) ---
        if (showDots) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 40.dp, top = 20.dp)
            ) {
                repeat(4) { index ->
                    val isFilled = index < pinInput.length

                    // Animate color
                    val colorAnim by animateColorAsState(
                        targetValue = if (isFilled) AppleColors.SystemBlue else Color.Transparent,
                        animationSpec = tween(300),
                        label = "color"
                    )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            // If empty -> Inset (Hole). If filled -> Flat surface
                            .then(if (!isFilled) Modifier.clayInset(color = baseColor, borderRadius = 12.dp) else Modifier)
                            .background(if (isFilled) baseColor else Color.Transparent, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // The Glowing Dot
                        if (isFilled) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    // Add a small shadow to the dot itself so it pops out
                                    .shadow(4.dp, CircleShape, spotColor = AppleColors.SystemBlue)
                                    .background(colorAnim, CircleShape)
                            )
                        }
                    }
                }
            }
        }

        // --- 2. Keypad Grid ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            // Rows 1-3
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9")
            )

            rows.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                    row.forEach { num ->
                        ClayKeypadButton(
                            text = num,
                            size = buttonSize,
                            enabled = pinInput.length < 4 && enabled,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onPinChanged(pinInput + num)
                            }
                        )
                    }
                }
            }

            // Row 4 (Biometric, 0, Delete)
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Biometric Button
                if (onBiometricClick != null) {
                    ClayKeypadButton(
                        text = "",
                        icon = Icons.Filled.Fingerprint,
                        size = buttonSize,
                        iconColor = AppleColors.SystemBlue,
                        enabled = enabled,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onBiometricClick()
                        }
                    )
                } else {
                    // Empty Spacer to keep alignment
                    Box(modifier = Modifier.size(buttonSize))
                }

                // 0 Button
                ClayKeypadButton(
                    text = "0",
                    size = buttonSize,
                    enabled = pinInput.length < 4 && enabled,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onPinChanged(pinInput + "0")
                    }
                )

                // Delete Button
                ClayKeypadButton(
                    text = "",
                    icon = Icons.Filled.Backspace,
                    size = buttonSize,
                    iconColor = AppleColors.SystemRed,
                    isFunctionKey = true,
                    enabled = pinInput.isNotEmpty() && enabled,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onDelete()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (showDots) {
            Text(
                text = "Secure Vault Access",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

// ============================================================================
// 3. CLAY BUTTON COMPONENT
// ============================================================================

@Composable
fun ClayKeypadButton(
    text: String,
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    isFunctionKey: Boolean = false,
    enabled: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }

    // Animate scale on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "scale"
    )

    // Adaptive Colors
    val isDark = MaterialTheme.colorScheme.surface == AppleColors.DarkSurface || MaterialTheme.colorScheme.background == AppleColors.DarkBackground
    val baseColor = if (isDark) AppleColors.DarkCard else Color(0xFFF0F0F3)
    val textColor = if (isFunctionKey) AppleColors.SystemRed else if (isDark) AppleColors.DarkText else Color(0xFF1C1C1E)
    val contentAlpha = if (enabled) 1f else 0.3f

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(enabled) {
                detectTapGestures(
                    onPress = {
                        if (enabled) {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                            onClick()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // 1. The Clay Shadow Layer
        // When pressed, we reduce the shadow spread to simulate the button moving "down" into the surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clayShadow(
                    color = baseColor,
                    spread = if (isPressed) 2.dp else 6.dp,
                    borderRadius = size / 2
                )
                .background(baseColor, CircleShape)
        )

        // 2. Subtle Surface Gradient (Simulates 3D curvature/inflation)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp) // Inset slightly
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.6f),
                            Color.Transparent
                        ),
                        center = Offset(x = size.value * 0.3f, y = size.value * 0.3f), // Light source top-left
                        radius = size.value * 0.8f
                    ),
                    shape = CircleShape
                )
        )

        // 3. Content
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor.copy(alpha = contentAlpha),
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = textColor.copy(alpha = contentAlpha)
            )
        }
    }
}

// ============================================================================
// SIMPLIFIED LOCK SETUP SCREEN FOR BETTER RESPONSIVENESS
// ============================================================================

@Composable
fun LockSetupScreen(
    authType: String,
    onComplete: (String, String) -> Unit
) {
    var pinInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Header Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        AppleColors.SystemGreen.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (authType) {
                        "password" -> Icons.Filled.Lock
                        "biometric" -> Icons.Filled.Fingerprint
                        else -> Icons.Filled.Pin
                    },
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = AppleColors.SystemGreen
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = when (authType) {
                    "biometric" -> "Set Fallback PIN"
                    "pin" -> "Set Your PIN"
                    else -> "Set Your Password"
                },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (authType) {
                    "biometric" -> "Enter a 4-digit fallback PIN in case biometric is unavailable"
                    "pin" -> "Enter a 4-digit PIN to secure your vault"
                    else -> "Create a strong password to unlock your vault"
                },
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    letterSpacing = (-0.2).sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (authType != "password") {
                // Use enhanced PIN keyboard
                ApplePinKeyboard(
                    pinInput = pinInput,
                    onPinChanged = { newPin ->
                        pinInput = newPin
                    },
                    onDelete = {
                        if (pinInput.isNotEmpty()) {
                            pinInput = pinInput.dropLast(1)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    enabled = true,
                    showDots = true
                )

                // Confirm button appears when PIN complete
                AnimatedVisibility(
                    visible = pinInput.length == 4,
                    enter = scaleIn(spring(dampingRatio = 0.7f)) + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                    modifier = Modifier.padding(bottom = 40.dp)
                ) {
                    FloatingActionButton(
                        onClick = { onComplete(pinInput, "") },
                        modifier = Modifier.size(60.dp),
                        containerColor = AppleColors.SystemBlue,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Confirm",
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                    }
                }
            } else {
                // Password input (rest of your existing code)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        placeholder = { Text("Enter password") },
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword)
                                        Icons.Filled.VisibilityOff
                                    else
                                        Icons.Filled.Visibility,
                                    contentDescription = "Toggle visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (showPassword)
                            androidx.compose.ui.text.input.VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppleColors.SystemBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )

                    if (passwordInput.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        val strength = calculatePasswordStrength(passwordInput)

                        // Strength indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            repeat(4) { index ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .background(
                                            if (index < strength) {
                                                when (strength) {
                                                    1 -> AppleColors.SystemRed
                                                    2 -> AppleColors.SystemOrange
                                                    3 -> AppleColors.SystemOrange
                                                    else -> AppleColors.SystemGreen
                                                }
                                            } else {
                                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                            },
                                            RoundedCornerShape(2.dp)
                                        )
                                )
                            }
                        }

                        Text(
                            text = when (strength) {
                                1 -> "Weak password"
                                2 -> "Fair password"
                                3 -> "Good password"
                                else -> "Strong password"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    AppleButton(
                        onClick = {
                            if (passwordInput.length >= 6) {
                                onComplete("", passwordInput)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = passwordInput.length >= 6
                    ) {
                        Text(
                            "Confirm",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W600,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// ============================================================================
// UPDATED LOGIN SCREEN WITH ENHANCED KEYBOARD
// ============================================================================

@Composable
fun LoginScreen(
    user: User,
    onLoginSuccess: () -> Unit,
    onLoginFailed: () -> Unit,
    onForgotCredentials: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    var pinInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var authAttempted by remember { mutableStateOf(false) }

    val executor = remember { ContextCompat.getMainExecutor(context) }
    val biometricManager = remember { androidx.biometric.BiometricManager.from(context) }
    val canAuthenticate = remember {
        biometricManager.canAuthenticate(
            androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or
            androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
    }

    val biometricPrompt = remember(activity) {
        if (activity != null && user.authType == "biometric") {
            BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        onLoginSuccess()
                    }

                    override fun onAuthenticationFailed() {
                        // Don't call onLoginFailed here
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                            errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                            errorCode != BiometricPrompt.ERROR_CANCELED
                        ) {
                            onLoginFailed()
                        }
                    }
                }
            )
        } else null
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock SecureThing")
            .setSubtitle("Use biometric to unlock")
            .setNegativeButtonText("Use PIN/Password")
            .build()
    }

    LaunchedEffect(user.authType) {
        if (user.authType == "biometric" && biometricPrompt != null && canAuthenticate) {
            delay(500)
            try {
                biometricPrompt.authenticate(promptInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fixed PIN input logic
    LaunchedEffect(pinInput) {
        // Only process if we have exactly 4 digits and haven't attempted auth yet
        if (pinInput.length == 4 && !authAttempted) {
            // Allow login if Auth Type is PIN OR if they are using Biometric (as fallback)
            val isPinAuth = user.authType == "pin" || user.authType == "biometric"

            if (isPinAuth) {
                authAttempted = true
                delay(100) // Small visual delay for the 4th dot

                // Check if stored PIN matches input
                if (user.pin.isNotEmpty() && pinInput == user.pin) {
                    onLoginSuccess()
                } else {
                    onLoginFailed()
                    delay(300) // Wait for user to see the error
                    pinInput = "" // Reset for incorrect PIN
                    authAttempted = false // Reset for next attempt
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Profile Avatar - Apple Style
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    )
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .background(
                            if (user.gender == "male")
                                AppleColors.SystemBlue.copy(alpha = 0.15f)
                            else
                                AppleColors.SystemPink.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (user.gender == "male")
                            Icons.Filled.Male
                        else
                            Icons.Filled.Female,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = if (user.gender == "male")
                            AppleColors.SystemBlue
                        else
                            AppleColors.SystemPink
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val displayName = if (user.name.isNotEmpty())
                user.name.split(" ").first()
            else
                "User"

            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    letterSpacing = (-0.2).sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 34.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(48.dp))

            when (user.authType) {
                "biometric" -> {
                    // Biometric button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Card(
                            onClick = {
                                if (canAuthenticate && biometricPrompt != null) {
                                    biometricPrompt.authenticate(promptInfo)
                                }
                            },
                            modifier = Modifier
                                .size(120.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = CircleShape,
                                    spotColor = AppleColors.SystemBlue.copy(alpha = 0.2f)
                                ),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = AppleColors.SystemBlue.copy(alpha = 0.1f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Fingerprint,
                                    contentDescription = "Biometric",
                                    modifier = Modifier.size(56.dp),
                                    tint = AppleColors.SystemBlue
                                )
                            }
                        }

                        Text(
                            text = "Touch to Unlock",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.W400
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Add PIN fallback for biometric users
                        Text(
                            text = "Or use PIN",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                letterSpacing = (-0.1).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.W400
                        )

                        ApplePinKeyboard(
                            pinInput = pinInput,
                            onPinChanged = { newPin ->
                                pinInput = newPin
                            },
                            onDelete = {
                                if (pinInput.isNotEmpty()) {
                                    pinInput = pinInput.dropLast(1)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = true,
                            showDots = true
                        )
                    }
                }

                "pin" -> {
                    // Use enhanced PIN keyboard
                    ApplePinKeyboard(
                        pinInput = pinInput,
                        onPinChanged = { newPin ->
                            pinInput = newPin
                        },
                        onDelete = {
                            if (pinInput.isNotEmpty()) {
                                pinInput = pinInput.dropLast(1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        showDots = true
                    )
                }

                else -> {
                    // Password input
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        placeholder = { Text("Enter password") },
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword)
                                        Icons.Filled.VisibilityOff
                                    else
                                        Icons.Filled.Visibility,
                                    contentDescription = "Toggle"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (showPassword)
                            androidx.compose.ui.text.input.VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppleColors.SystemBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AppleButton(
                        onClick = {
                            if (passwordInput == user.password) {
                                onLoginSuccess()
                            } else {
                                onLoginFailed()
                                passwordInput = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Unlock",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W600,
                            letterSpacing = (-0.2).sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Forgot credentials
            TextButton(
                onClick = onForgotCredentials,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppleColors.SystemBlue
                )
            ) {
                Text(
                    "Forgot Credentials?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W400,
                    letterSpacing = (-0.2).sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ============================================================================
// UTILITY FUNCTION FOR PIN ENTRY (Also use in RecoveryScreen if needed)
// ============================================================================

@Composable
fun ApplePinEntryScreen(
    title: String,
    subtitle: String = "",
    onPinComplete: (String) -> Unit,
    onCancel: (() -> Unit)? = null,
    showCancelButton: Boolean = false
) {
    var pinInput by remember { mutableStateOf("") }

    LaunchedEffect(pinInput) {
        if (pinInput.length == 4) {
            onPinComplete(pinInput)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        AppleColors.SystemBlue.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Pin,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = AppleColors.SystemBlue
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W700
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.W400
                )
                Spacer(modifier = Modifier.height(48.dp))
            }

            // PIN keyboard
            ApplePinKeyboard(
                pinInput = pinInput,
                onPinChanged = { newPin ->
                    pinInput = newPin
                },
                onDelete = {
                    if (pinInput.isNotEmpty()) {
                        pinInput = pinInput.dropLast(1)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                showDots = true
            )

            if (showCancelButton && onCancel != null) {
                Spacer(modifier = Modifier.height(32.dp))

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Cancel",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W400,
                        letterSpacing = (-0.2).sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
// ============================================================================
// RECOVERY SCREEN - APPLE STYLE
// ============================================================================

@Composable
fun RecoveryScreen(
    security: Security,
    authType: String,
    onRecoverySuccess: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var answer1 by remember { mutableStateOf("") }
    var answer2 by remember { mutableStateOf("") }
    var showResetScreen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (!showResetScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                // Header Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            AppleColors.SystemOrange.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Help,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = AppleColors.SystemOrange
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Account Recovery",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.W700
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Answer your security questions",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.W400
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Question 1
                AppleCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = security.q1,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.W500
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = answer1,
                            onValueChange = {
                                answer1 = it
                                errorMessage = ""
                            },
                            placeholder = { Text("Your answer") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppleColors.SystemBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Question 2
                AppleCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = security.q2,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.W500
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = answer2,
                            onValueChange = {
                                answer2 = it
                                errorMessage = ""
                            },
                            placeholder = { Text("Your answer") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppleColors.SystemBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    AppleCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = null,
                                tint = AppleColors.SystemRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppleColors.SystemRed
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                AppleButton(
                    onClick = {
                        val a1 = answer1.lowercase().trim()
                        val a2 = answer2.lowercase().trim()
                        if (a1 == security.a1 && a2 == security.a2) {
                            showResetScreen = true
                        } else {
                            errorMessage = "Incorrect answers. Please try again."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = answer1.isNotEmpty() && answer2.isNotEmpty()
                ) {
                    Text(
                        "Verify & Reset",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W600,
                        letterSpacing = (-0.2).sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppleColors.SystemBlue
                    )
                ) {
                    Text(
                        "Cancel",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W400,
                        letterSpacing = (-0.2).sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    } else {
        LockSetupScreen(
            authType = authType,
            onComplete = { pin, password ->
                onRecoverySuccess(pin, password)
            }
        )
    }
}

// ============================================================================
// MAIN APP SCREEN - APPLE STYLE
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    appData: AppData,
    onSaveData: (AppData) -> Unit,
    onLogout: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    repository: DataRepository
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Apple-style Top Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 0.5.dp,
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (appData.user.gender == "male")
                                        AppleColors.SystemBlue.copy(alpha = 0.15f)
                                    else
                                        AppleColors.SystemPink.copy(alpha = 0.15f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (appData.user.gender == "male")
                                    Icons.Filled.Male
                                else
                                    Icons.Filled.Female,
                                contentDescription = null,
                                tint = if (appData.user.gender == "male")
                                    AppleColors.SystemBlue
                                else
                                    AppleColors.SystemPink,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "SecureThing",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 22.sp,
                                    letterSpacing = (-0.3).sp
                                ),
                                fontWeight = FontWeight.W700
                            )
                            Text(
                                text = appData.user.name.split(" ").first(),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 13.sp,
                                    letterSpacing = (-0.1).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                val newTheme = if (appData.settings.theme == "dark") "light" else "dark"
                                onSaveData(appData.copy(settings = appData.settings.copy(theme = newTheme)))
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (appData.settings.theme == "dark")
                                    Icons.Filled.LightMode
                                else
                                    Icons.Filled.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { showSettingsDialog = true },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Apple-style Tab Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    ),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    contentColor = AppleColors.SystemBlue,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text(
                                "Home",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.W500
                            )
                        },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppleColors.SystemBlue,
                            selectedTextColor = AppleColors.SystemBlue,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = AppleColors.SystemBlue.copy(alpha = 0.1f)
                        )
                    )
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (appData.recycleBin.isNotEmpty()) {
                                        Badge(
                                            containerColor = AppleColors.SystemRed
                                        ) {
                                            Text(
                                                "${appData.recycleBin.size}",
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    if (selectedTab == 1) Icons.Filled.Delete else Icons.Outlined.Delete,
                                    contentDescription = "Bin"
                                )
                            }
                        },
                        label = {
                            Text(
                                "Bin",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.W500
                            )
                        },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppleColors.SystemBlue,
                            selectedTextColor = AppleColors.SystemBlue,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = AppleColors.SystemBlue.copy(alpha = 0.1f)
                        )
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedTab == 2) Icons.Filled.Key else Icons.Outlined.Key,
                                contentDescription = "Keys"
                            )
                        },
                        label = {
                            Text(
                                "Keys",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.W500
                            )
                        },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppleColors.SystemBlue,
                            selectedTextColor = AppleColors.SystemBlue,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = AppleColors.SystemBlue.copy(alpha = 0.1f)
                        )
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedTab == 3) Icons.Filled.Wallet else Icons.Outlined.AccountBalanceWallet,
                                contentDescription = "Wallet"
                            )
                        },
                        label = {
                            Text(
                                "Wallet",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.W500
                            )
                        },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppleColors.SystemBlue,
                            selectedTextColor = AppleColors.SystemBlue,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = AppleColors.SystemBlue.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showImportDialog = true },
                    containerColor = AppleColors.SystemBlue,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Import",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(200))
                },
                label = "tab_transition"
            ) { tab ->
                when (tab) {
                    0 -> HomeTab(appData, onSaveData, onShowSnackbar)
                    1 -> RecycleBinTab(appData, onSaveData, onShowSnackbar)
                    2 -> PasswordsTab(appData, onSaveData, onShowSnackbar)
                    3 -> WalletTab(appData, onSaveData, onShowSnackbar)
                }
            }
        }
    }

    if (showSettingsDialog) {
        SettingsDialog(
            appData = appData,
            onDismiss = { showSettingsDialog = false },
            onSaveData = onSaveData,
            onLogout = onLogout,
            onShowSnackbar = onShowSnackbar,
            repository = repository
        )
    }

    if (showImportDialog) {
        ImportFileDialog(
            appData = appData,
            onDismiss = { showImportDialog = false },
            onSaveData = onSaveData,
            onShowSnackbar = onShowSnackbar
        )
    }
}
@Composable
fun ImportFileDialog(
    appData: AppData,
    onDismiss: () -> Unit,
    onSaveData: (AppData) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    var isImporting     by remember { mutableStateOf(false) }
    var importProgress  by remember { mutableStateOf(0f) }
    var importingLabel  by remember { mutableStateOf("Importing & Optimizing") }

    // Shared import handler
    fun launchImport(uri: Uri, label: String) {
        isImporting    = true
        importingLabel = label
        CoroutineScope(Dispatchers.Main).launch {
            handleOptimizedFileImport(
                context, uri, appData, onSaveData, onShowSnackbar,
                onProgress = { progress -> importProgress = progress }
            )
            isImporting   = false
            importProgress = 0f
            onDismiss()
        }
    }

    // ── Photo picker ────────────────────────────────────────────────────────
    val photoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { launchImport(it, "Optimizing Image…") } }

    // ── Video picker ────────────────────────────────────────────────────────
    val videoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { launchImport(it, "Importing Video…") } }

    // ── Audio document picker (audio/* MIME) ────────────────────────────────
    val audioPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri -> uri?.let { launchImport(it, "Importing Audio…") } }

    // ── Generic document picker (*/*) ────────────────────────────────────────
    val documentPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri -> uri?.let { launchImport(it, "Importing Document…") } }

    // ── Importing progress dialog ────────────────────────────────────────────
    if (isImporting) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            AppleCard {
                Column(
                    modifier = Modifier.padding(36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { importProgress },
                        modifier = Modifier.size(60.dp),
                        color = AppleColors.SystemBlue,
                        strokeWidth = 4.dp,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    )
                    Text(
                        text = importingLabel,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 17.sp,
                            letterSpacing = (-0.2).sp
                        ),
                        fontWeight = FontWeight.W600
                    )
                    Text(
                        text = "${(importProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.W400
                    )
                }
            }
        }

    } else {
        // ── Picker dialog ─────────────────────────────────────────────────────
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AppleCard(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    // Title row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Import File",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontSize = 22.sp,
                                    letterSpacing = (-0.3).sp
                                ),
                                fontWeight = FontWeight.W700
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Files are automatically compressed to save space",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.W400
                    )

                    Spacer(Modifier.height(22.dp))

                    // ── Import options ─────────────────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                        ImportOptionCard(
                            icon     = Icons.Filled.Image,
                            title    = "Import Image",
                            subtitle = "Photos and pictures (JPG, PNG, WEBP…)",
                            color    = AppleColors.SystemBlue,
                            onClick  = {
                                photoPickerLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                        )

                        ImportOptionCard(
                            icon     = Icons.Filled.VideoLibrary,
                            title    = "Import Video",
                            subtitle = "Video files (MP4, MOV, MKV…)",
                            color    = AppleColors.SystemPurple,
                            onClick  = {
                                videoPickerLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.VideoOnly
                                    )
                                )
                            }
                        )

                        // ── NEW: Audio import ─────────────────────────────────
                        ImportOptionCard(
                            icon     = Icons.Filled.MusicNote,
                            title    = "Import Audio",
                            subtitle = "Music and sound files (MP3, AAC, FLAC, OGG, WAV…)",
                            color    = AppleColors.SystemGreen,
                            onClick  = {
                                audioPickerLauncher.launch(
                                    arrayOf(
                                        "audio/*",
                                        "audio/mpeg",
                                        "audio/mp4",
                                        "audio/ogg",
                                        "audio/wav",
                                        "audio/flac",
                                        "audio/aac",
                                        "audio/x-ms-wma"
                                    )
                                )
                            }
                        )

                        ImportOptionCard(
                            icon     = Icons.Filled.Description,
                            title    = "Import Document",
                            subtitle = "PDFs, text, code, and any file type",
                            color    = AppleColors.SystemOrange,
                            onClick  = {
                                documentPickerLauncher.launch(arrayOf("*/*"))
                            }
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

// ── Import Option Row Card ─────────────────────────────────────────────────────

@Composable
fun ImportOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon badge
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(46.dp)
                    .then(
                        Modifier.then(
                            androidx.compose.ui.Modifier.background(
                                color.copy(alpha = 0.14f),
                                RoundedCornerShape(11.dp)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    fontWeight = FontWeight.W600
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.W400,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}



// ============================================================================
// FILE IMPORT LOGIC
// ============================================================================

suspend fun handleOptimizedFileImport(
    context: Context,
    uri: Uri,
    appData: AppData,
    onSaveData: (AppData) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onProgress: (Float) -> Unit
) = withContext(Dispatchers.IO) {
    try {
        onProgress(0.1f)

        val fileSize = FileUtil.getFileSize(context, uri)
        val fileType = FileUtil.getFileType(context, uri)

        onProgress(0.3f)

        val (base64Data, thumbnailData) = when (fileType) {
            "photo" -> {
                val result = FileUtil.uriToOptimizedBase64(context, uri)
                if (result != null) result.first to result.second
                else {
                    withContext(Dispatchers.Main) { onShowSnackbar("Failed to compress image") }
                    return@withContext
                }
            }
            else -> {
                // audio, video, file — raw base64
                val data = FileUtil.uriToBase64(context, uri)
                if (data != null) data to ""
                else {
                    withContext(Dispatchers.Main) { onShowSnackbar("Failed to read file") }
                    return@withContext
                }
            }
        }

        onProgress(0.7f)

        val newFile = VaultFile(
            id        = UUID.randomUUID().toString(),
            name      = FileUtil.getFileName(context, uri),
            type      = fileType,
            data      = base64Data,
            size      = fileSize,
            fav       = false,
            date      = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date()),
            thumbnail = thumbnailData
        )

        onProgress(0.9f)

        withContext(Dispatchers.Main) {
            onSaveData(appData.copy(files = appData.files + newFile))
            onShowSnackbar("✓ ${newFile.name} imported")
        }

        onProgress(1f)

    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            onShowSnackbar("Import failed: ${e.message}")
        }
    }
}

// ============================================================================
// RECYCLE BIN TAB - APPLE STYLE
// ============================================================================

@Composable
fun RecycleBinTab(
    appData: AppData,
    onSaveData: (AppData) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    var viewMode by remember { mutableStateOf("grid") }
    var selectedFile by remember { mutableStateOf<VaultFile?>(null) }
    var showEmptyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Recycle Bin",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 28.sp,
                            letterSpacing = (-0.4).sp
                        ),
                        fontWeight = FontWeight.W700
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${appData.recycleBin.size} ${if (appData.recycleBin.size == 1) "item" else "items"}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            letterSpacing = (-0.2).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.W400
                    )
                }

                if (appData.recycleBin.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { viewMode = if (viewMode == "grid") "list" else "grid" },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                if (viewMode == "grid") Icons.Filled.ViewList else Icons.Filled.GridView,
                                contentDescription = "Toggle View",
                                tint = AppleColors.SystemBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Button(
                            onClick = { showEmptyDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppleColors.SystemRed.copy(alpha = 0.15f),
                                contentColor = AppleColors.SystemRed
                            ),
                            shape = RoundedCornerShape(10.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Icon(
                                Icons.Filled.DeleteForever,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Empty",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (appData.recycleBin.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        }

                        Text(
                            text = "Recycle Bin is Empty",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                letterSpacing = (-0.3).sp
                            ),
                            fontWeight = FontWeight.W700
                        )

                        Text(
                            text = "Deleted files will appear here",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.W400
                        )
                    }
                }
            } else {
                if (viewMode == "grid") {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.heightIn(max = 2000.dp),
                        userScrollEnabled = false
                    ) {
                        items(appData.recycleBin, key = { it.id }) { file ->
                            AppleRecycleBinGridItem(
                                file = file,
                                onRestore = {
                                    val updatedBin = appData.recycleBin.filter { it.id != file.id }
                                    val restoredFile = file.copy(deletedDate = "")
                                    onSaveData(
                                        appData.copy(
                                            files = appData.files + restoredFile,
                                            recycleBin = updatedBin
                                        )
                                    )
                                    onShowSnackbar("✓ File restored")
                                },
                                onDelete = {
                                    val updatedBin = appData.recycleBin.filter { it.id != file.id }
                                    onSaveData(appData.copy(recycleBin = updatedBin))
                                    onShowSnackbar("✓ Permanently deleted")
                                }
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        appData.recycleBin.forEach { file ->
                            AppleRecycleBinListItem(
                                file = file,
                                onRestore = {
                                    val updatedBin = appData.recycleBin.filter { it.id != file.id }
                                    val restoredFile = file.copy(deletedDate = "")
                                    onSaveData(
                                        appData.copy(
                                            files = appData.files + restoredFile,
                                            recycleBin = updatedBin
                                        )
                                    )
                                    onShowSnackbar("✓ File restored")
                                },
                                onDelete = {
                                    val updatedBin = appData.recycleBin.filter { it.id != file.id }
                                    onSaveData(appData.copy(recycleBin = updatedBin))
                                    onShowSnackbar("✓ Permanently deleted")
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showEmptyDialog) {
        Dialog(onDismissRequest = { showEmptyDialog = false }) {
            AppleCard {
                Column(modifier = Modifier.padding(24.dp)) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                AppleColors.SystemRed.copy(alpha = 0.15f),
                                CircleShape
                            )
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = AppleColors.SystemRed,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Empty Recycle Bin?",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 20.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This will permanently delete all ${appData.recycleBin.size} items. This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            letterSpacing = (-0.2).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AppleButton(
                        onClick = {
                            onSaveData(appData.copy(recycleBin = emptyList()))
                            onShowSnackbar("✓ Recycle Bin emptied")
                            showEmptyDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppleColors.SystemRed
                        )
                    ) {
                        Text(
                            "Empty Bin",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W600,
                            letterSpacing = (-0.2).sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { showEmptyDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = AppleColors.SystemBlue
                        )
                    ) {
                        Text(
                            "Cancel",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W400,
                            letterSpacing = (-0.2).sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppleRecycleBinGridItem(
    file: VaultFile,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.06f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (file.type == "photo" && file.thumbnail.isNotEmpty()) {
                val bitmap = remember(file.id) {
                    try {
                        val imageBytes = Base64.decode(file.thumbnail, Base64.NO_WRAP)
                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    } catch (e: Exception) {
                        null
                    }
                }

                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = file.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(
                            ColorMatrix().apply { setToSaturation(0.2f) }
                        ),
                        alpha = 0.5f
                    )

                    DisposableEffect(file.id) {
                        onDispose { bitmap.recycle() }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (file.type) {
                            "video" -> Icons.Filled.VideoLibrary
                            "photo" -> Icons.Filled.Image
                            else -> Icons.Filled.Description
                        },
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                }
            }

            // Deleted overlay badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Surface(
                    color = AppleColors.SystemRed.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = "DELETED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Bottom info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(12.dp)
            ) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        letterSpacing = (-0.1).sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    fontWeight = FontWeight.W500
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Deleted",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp
                        ),
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.W400
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = onRestore,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Filled.Restore,
                                contentDescription = "Restore",
                                tint = AppleColors.SystemGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Filled.DeleteForever,
                                contentDescription = "Delete",
                                tint = AppleColors.SystemRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppleRecycleBinListItem(
    file: VaultFile,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (file.type) {
                            "video" -> Icons.Filled.VideoLibrary
                            "photo" -> Icons.Filled.Image
                            else -> Icons.Filled.Description
                        },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            letterSpacing = (-0.2).sp
                        ),
                        fontWeight = FontWeight.W500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Deleted: ${file.deletedDate}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            letterSpacing = (-0.1).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.W400
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = onRestore,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            AppleColors.SystemGreen.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        Icons.Filled.Restore,
                        contentDescription = "Restore",
                        tint = AppleColors.SystemGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            AppleColors.SystemRed.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        Icons.Filled.DeleteForever,
                        contentDescription = "Delete",
                        tint = AppleColors.SystemRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ============================================================================
// PASSWORDS TAB - APPLE STYLE
// ============================================================================

@Composable
fun PasswordsTab(
    appData: AppData,
    onSaveData: (AppData) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var editingPassword by remember { mutableStateOf<PasswordEntry?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Passwords",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 28.sp,
                            letterSpacing = (-0.4).sp
                        ),
                        fontWeight = FontWeight.W700
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${appData.passwords.size} ${if (appData.passwords.size == 1) "password" else "passwords"}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            letterSpacing = (-0.2).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.W400
                    )
                }

                FloatingActionButton(
                    onClick = {
                        editingPassword = null
                        showAddDialog = true
                    },
                    containerColor = AppleColors.SystemBlue,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (appData.passwords.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    AppleColors.SystemBlue.copy(alpha = 0.1f),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Key,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = AppleColors.SystemBlue
                            )
                        }

                        Text(
                            text = "No Passwords Saved",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                letterSpacing = (-0.3).sp
                            ),
                            fontWeight = FontWeight.W700
                        )

                        Text(
                            text = "Add your first password to get started",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.W400
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    appData.passwords.forEach { password ->
                        ApplePasswordCard(
                            password = password,
                            onToggleReveal = {
                                val updated = appData.passwords.map {
                                    if (it.id == password.id) it.copy(revealed = !it.revealed) else it
                                }
                                onSaveData(appData.copy(passwords = updated))
                            },
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                val clip = android.content.ClipData.newPlainText("password", password.password)
                                clipboard.setPrimaryClip(clip)
                                onShowSnackbar("✓ Copied to clipboard")
                            },
                            onEdit = {
                                editingPassword = password
                                showAddDialog = true
                            },
                            onDelete = {
                                val updated = appData.passwords.filter { it.id != password.id }
                                onSaveData(appData.copy(passwords = updated))
                                onShowSnackbar("✓ Password deleted")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showAddDialog) {
        AddPasswordDialog(
            password = editingPassword,
            onDismiss = { showAddDialog = false },
            onSave = { platform, username, password, note ->
                if (editingPassword != null) {
                    val updated = appData.passwords.map {
                        if (it.id == editingPassword!!.id)
                            it.copy(platform = platform, username = username, password = password, note = note)
                        else it
                    }
                    onSaveData(appData.copy(passwords = updated))
                    onShowSnackbar("✓ Password updated")
                } else {
                    val newEntry = PasswordEntry(
                        id = System.currentTimeMillis(),
                        platform = platform,
                        username = username,
                        password = password,
                        note = note
                    )
                    onSaveData(appData.copy(passwords = appData.passwords + newEntry))
                    onShowSnackbar("✓ Password saved")
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ApplePasswordCard(
    password: PasswordEntry,
    onToggleReveal: () -> Unit,
    onCopy: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    AppleCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                AppleColors.SystemBlue.copy(alpha = 0.15f),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = password.platform.take(1).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                letterSpacing = (-0.3).sp
                            ),
                            color = AppleColors.SystemBlue,
                            fontWeight = FontWeight.W700
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = password.platform,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 17.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            fontWeight = FontWeight.W600,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = password.username,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                letterSpacing = (-0.1).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.W400,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                showMenu = false
                                onEdit()
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Edit, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = AppleColors.SystemRed) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = null,
                                    tint = AppleColors.SystemRed
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (password.revealed) password.password else "••••••••••",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp,
                        letterSpacing = if (password.revealed) (-0.2).sp else 2.sp
                    ),
                    fontWeight = if (password.revealed) FontWeight.W500 else FontWeight.W400,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onToggleReveal,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                RoundedCornerShape(10.dp)
                            )
                    ) {
                        Icon(
                            if (password.revealed) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "Toggle",
                            tint = AppleColors.SystemBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                AppleColors.SystemBlue.copy(alpha = 0.15f),
                                RoundedCornerShape(10.dp)
                            )
                    ) {
                        Icon(
                            Icons.Filled.ContentCopy,
                            contentDescription = "Copy",
                            tint = AppleColors.SystemBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (password.note.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = password.note,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        letterSpacing = (-0.1).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontWeight = FontWeight.W400
                )
            }
        }
    }
}
// ============================================================================
// ADD PASSWORD DIALOG - APPLE STYLE
// ============================================================================

@Composable
fun AddPasswordDialog(
    password: PasswordEntry?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var platform by remember { mutableStateOf(password?.platform ?: "") }
    var username by remember { mutableStateOf(password?.username ?: "") }
    var passwordText by remember { mutableStateOf(password?.password ?: "") }
    var note by remember { mutableStateOf(password?.note ?: "") }
    var showPassword by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (password != null) "Edit Password" else "New Password",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 22.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        fontWeight = FontWeight.W700
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Platform
                OutlinedTextField(
                    value = platform,
                    onValueChange = { platform = it },
                    label = { Text("Platform") },
                    placeholder = { Text("e.g., Gmail, Facebook") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleColors.SystemBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username/Email
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username / Email") },
                    placeholder = { Text("your@email.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleColors.SystemBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password
                OutlinedTextField(
                    value = passwordText,
                    onValueChange = { passwordText = it },
                    label = { Text("Password") },
                    placeholder = { Text("Enter password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (showPassword)
                        androidx.compose.ui.text.input.VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        Row {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = "Toggle",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = {
                                    passwordText = generateRandomPassword()
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Refresh,
                                    contentDescription = "Generate",
                                    tint = AppleColors.SystemBlue
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleColors.SystemBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notes
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Notes (Optional)") },
                    placeholder = { Text("Add notes...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleColors.SystemBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Save button
                AppleButton(
                    onClick = {
                        if (platform.isNotEmpty() && passwordText.isNotEmpty()) {
                            onSave(platform, username, passwordText, note)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = platform.isNotEmpty() && passwordText.isNotEmpty()
                ) {
                    Text(
                        if (password != null) "Update" else "Save",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W600,
                        letterSpacing = (-0.2).sp
                    )
                }
            }
        }
    }
}

fun generateRandomPassword(length: Int = 16): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*"
    return (1..length).map { chars.random() }.joinToString("")
}

// ============================================================================
// 2. WALLET TAB SCREEN
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletTab(
    appData: AppData,
    onSaveData: (AppData) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCard by remember { mutableStateOf<CardEntry?>(null) }
    var selectedCard by remember { mutableStateOf<CardEntry?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Wallet",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 28.sp,
                            letterSpacing = (-0.4).sp
                        ),
                        fontWeight = FontWeight.W700
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${appData.cards.size} ${if (appData.cards.size == 1) "card" else "cards"}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            letterSpacing = (-0.2).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.W400
                    )
                }

                FloatingActionButton(
                    onClick = {
                        editingCard = null
                        showAddDialog = true
                    },
                    containerColor = AppleColors.SystemBlue,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add Card",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (appData.cards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    AppleColors.SystemPurple.copy(alpha = 0.1f),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Wallet,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = AppleColors.SystemPurple
                            )
                        }

                        Text(
                            text = "No Cards Added",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                letterSpacing = (-0.3).sp
                            ),
                            fontWeight = FontWeight.W700
                        )

                        Text(
                            text = "Add your first card to get started",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.W400
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    appData.cards.forEach { card ->
                        AppleWalletCard(
                            card = card,
                            onClick = { selectedCard = card },
                            onEdit = {
                                editingCard = card
                                showAddDialog = true
                            },
                            onDelete = {
                                val updatedCards = appData.cards.filter { it.id != card.id }
                                onSaveData(appData.copy(cards = updatedCards))
                                onShowSnackbar("✓ Card removed")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showAddDialog) {
        AddCardDialog(
            card = editingCard,
            onDismiss = { showAddDialog = false },
            onSave = { newCardEntry ->
                if (editingCard != null) {
                    val updated = appData.cards.map {
                        if (it.id == editingCard!!.id) newCardEntry.copy(id = it.id) else it
                    }
                    onSaveData(appData.copy(cards = updated))
                    onShowSnackbar("✓ Card updated")
                } else {
                    onSaveData(appData.copy(cards = appData.cards + newCardEntry))
                    onShowSnackbar("✓ Card saved")
                }
                showAddDialog = false
            }
        )
    }

    if (selectedCard != null) {
        CardDetailsDialog(
            card = selectedCard!!,
            onDismiss = { selectedCard = null }
        )
    }
}

// ============================================================================
// 3. APPLE WALLET CARD COMPONENT
// ============================================================================

@Composable
fun AppleWalletCard(
    card: CardEntry,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 1. Try to get a specific Background Image Resource
    // Make sure you place these pngs in res/drawable/
    val bgResource: Int? = when (card.type.lowercase()) {
        "aadhaar" -> context.resources.getIdentifier("aadhaar_bg", "drawable", context.packageName).takeIf { it != 0 }
        "pan" -> context.resources.getIdentifier("pan_bg", "drawable", context.packageName).takeIf { it != 0 }
        "voter" -> context.resources.getIdentifier("voter_bg", "drawable", context.packageName).takeIf { it != 0 }
        "license" -> context.resources.getIdentifier("license_bg", "drawable", context.packageName).takeIf { it != 0 }
        "visa" -> context.resources.getIdentifier("visa_bg", "drawable", context.packageName).takeIf { it != 0 }
        "master" -> context.resources.getIdentifier("master_bg", "drawable", context.packageName).takeIf { it != 0 }
        "rupay" -> context.resources.getIdentifier("rupay_bg", "drawable", context.packageName).takeIf { it != 0 }
        else -> null
    }

    // 2. Fallback Gradients (Used if PNG is missing)
    val gradient = when (card.type.lowercase()) {
        "visa" -> Brush.linearGradient(
            colors = listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E)),
            start = Offset(0f, Float.POSITIVE_INFINITY), end = Offset(0f, 0f)
        )
        "master" -> Brush.linearGradient(
            colors = listOf(Color(0xFFEB001B), Color(0xFFF79E1B)),
            start = Offset(0f, 0f), end = Offset(1000f, 1000f)
        )
        "aadhaar" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFFA82800), // Start (Red)
                Color(0xFFB7CDF3), // Center (Blue)
                Color(0xFF059200)  // End (Green)
            ),
            start = Offset(0f, 0f),
            end = Offset(0f, Float.POSITIVE_INFINITY)
        )
        "pan" -> Brush.linearGradient(
            colors = listOf(Color(79, 54, 155), Color(0, 36, 131)),
            start = Offset(0f, 0f), end = Offset(0f, Float.POSITIVE_INFINITY)
        )
        "voter" -> Brush.linearGradient(
            colorStops = arrayOf(
                0.000f to Color(221, 108, 81),   // Saffron
                0.498f to Color(255, 255, 255),  // White
                1.000f to Color(180, 255, 96)    // Green
            ),
            start = Offset(0f, 0f), end = Offset(0f, Float.POSITIVE_INFINITY)
        )
        "rupay" -> Brush.linearGradient(
            colors = listOf(Color(0xFF097ABA), Color(0xFF39C167)),
            start = Offset(0f, 0f), end = Offset(1000f, 1000f)
        )
        "license" -> Brush.linearGradient(
            colorStops = arrayOf(
                0.000f to Color(110, 110, 126),  // Greyish Blue
                0.498f to Color(234, 232, 223),  // Off White
                1.000f to Color(146, 170, 175)   // Light Teal Grey
            ),
            start = Offset(0f, 0f), end = Offset(0f, Float.POSITIVE_INFINITY)
        )
        else -> Brush.linearGradient(
            colors = listOf(Color(0xFF212121), Color(0xFF424242)),
            start = Offset(0f, 0f), end = Offset(1000f, 1000f)
        )
    }

    val isGovtId = card.type.lowercase() in listOf("aadhaar", "pan", "voter", "license")

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- BACKGROUND LAYER ---
            if (bgResource != null) {
                // Render PNG if available
                Image(
                    painter = painterResource(id = bgResource),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Optional: Add a dark scrim if text is hard to read on the image
                // Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)))
            } else {
                // Fallback to Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient)
                )
            }

            // Gloss Layer (Texture overlay)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.05f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            )

            // Content Layer
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)) {

                // --- Menu Button ---
                Box(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(1f)) {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = { showMenu = false; onEdit() },
                            leadingIcon = { Icon(Icons.Filled.Edit, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = Color(0xFFFF3B30)) },
                            onClick = { showMenu = false; onDelete() },
                            leadingIcon = { Icon(Icons.Filled.Delete, null, tint = Color(0xFFFF3B30)) }
                        )
                    }
                }

                // --- Card Face ---
                if (isGovtId) {
                    GovtIdFace(card)
                } else {
                    CreditCardFace(card)
                }
            }
        }
    }
}

// ============================================================================
// 4. CARD FACES
// ============================================================================

@Composable
fun CreditCardFace(card: CardEntry) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top: Chip & Wifi
        Row(verticalAlignment = Alignment.CenterVertically) {
            EmvChip(modifier = Modifier.size(width = 45.dp, height = 32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            ContactlessIcon(color = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
        }

        // Middle: Number
        Text(
            text = formatCardNumber(card.number),
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                shadow = Shadow(Color.Black.copy(0.5f), Offset(2f, 2f), 3f)
            ),
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Bottom: Holder, Expiry, Logo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Column {
                    CardLabelText("CARD HOLDER")
                    CardValueText(card.holder.uppercase())
                }
                Column {
                    CardLabelText("EXPIRES")
                    CardValueText(card.expiry)
                }
            }

            // Text Logo
            Text(
                text = card.type.uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                color = Color.White
            )
        }
    }
}

@Composable
fun GovtIdFace(card: CardEntry) {
    val isPan = card.type.lowercase() == "pan"
    val isVoter = card.type.lowercase() == "voter"
    val isAadhaar = card.type.lowercase() == "aadhaar"

    // --- Determine Text Colors based on Background ---
    // Aadhaar has a light background, so use dark text.
    // Others have dark backgrounds, so use white text.
    val textColor = if (isAadhaar) Color(0xFF212121) else Color.White
    val labelColor = if (isAadhaar) Color(0xFF616161) else Color.White.copy(alpha = 0.8f)
    val headerColor = if (isAadhaar) Color.Black else Color.White
    // Shadow only needed for white text on dark bg to make it pop
    val textShadow = if (isAadhaar) null else Shadow(Color.Black.copy(0.5f), Offset(1f, 1f), 2f)

    Column(modifier = Modifier.fillMaxSize()) {

        // 1. Header: Emblem & Type
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Replace the previous if/else block with this structure:

            if (isAadhaar) {
                // Aadhaar Logo (Keep original colors)
                Image(
                    painter = painterResource(id = R.drawable.aadhaar_logo),
                    contentDescription = "Aadhaar Logo",
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Fit
                )
            } else if (isVoter) {
                // Election Commission of India Logo (Keep original colors)
                Image(
                    painter = painterResource(id = R.drawable.eci_logo),
                    contentDescription = "ECI Logo",
                    modifier = Modifier.size(34.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                // Standard Government of India Emblem for others (e.g., PAN, License)
                // We apply a tint so it matches the header text color (usually white on dark backgrounds)
                Image(
                    painter = painterResource(id = R.drawable.gov_logo_india),
                    contentDescription = "Govt Logo",
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = when {
                        isPan -> "INCOME TAX DEPARTMENT"
                        isVoter -> "ELECTION COMMISSION OF INDIA"
                        isAadhaar -> "GOVERNMENT OF INDIA"
                        else -> "GOVERNMENT OF INDIA"
                    },
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                    color = headerColor
                )
                Text(
                    text = when {
                        isPan -> "GOVT. OF INDIA"
                        isVoter -> "ELECTOR PHOTO IDENTITY CARD"
                        isAadhaar -> "Unique Identification Authority of India"
                        else -> "Ministry of Transport"
                    },
                    style = TextStyle(fontSize = 8.sp, fontWeight = FontWeight.Normal),
                    color = labelColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Middle: Photo + Details + QR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Real Photo or Avatar
            if (card.photo.isNotEmpty()) {
                val bitmap = remember(card.photo) {
                    try {
                        val bytes = Base64.decode(card.photo, Base64.NO_WRAP)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
                    } catch (e: Exception) { null }
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "User Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(80.dp)
                            .fillMaxHeight(0.9f)
                            .clip(RoundedCornerShape(4.dp))
                            // Border color also needs to adapt
                            .border(1.dp, if(isAadhaar) Color.DarkGray else Color.White, RoundedCornerShape(4.dp))
                    )
                } else {
                    PassportPhoto(modifier = Modifier.width(80.dp).fillMaxHeight(0.9f))
                }
            } else {
                PassportPhoto(modifier = Modifier.width(80.dp).fillMaxHeight(0.9f))
            }

            // Middle: Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Name
                Column {
                    CardLabelText("NAME", color = labelColor)
                    Text(
                        text = card.holder.uppercase(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            shadow = textShadow
                        ),
                        color = textColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Relation (Father's Name etc)
                if (card.relation.isNotEmpty()) {
                    Column {
                        CardLabelText(if(isVoter) "FATHER'S/HUSBAND'S NAME" else "C/O, S/O, D/O", color = labelColor)
                        Text(
                            text = card.relation,
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                            color = textColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // DOB or Issue Date
                if (card.dob.isNotEmpty()) {
                    Column {
                        CardLabelText("DOB", color = labelColor)
                        Text(
                            text = card.dob,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                            color = textColor
                        )
                    }
                } else if (card.expiry.isNotEmpty()) {
                    Column {
                        CardLabelText("ISSUE DATE", color = labelColor)
                        Text(
                            text = card.expiry,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                            color = textColor
                        )
                    }
                }
            }

            // Right: Auto-Generated QR
            Column(verticalArrangement = Arrangement.Center) {
                val qrData = "${card.number}${card.holder}"
                AutoGeneratedQr(data = qrData, modifier = Modifier.size(54.dp))
            }
        }

        // 3. Bottom: ID Number
        Text(
            text = formatIdNumber(card.number, card.type),
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = if (isPan || isVoter) 20.sp else 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                shadow = if (isAadhaar) null else Shadow(Color.Black.copy(0.6f), Offset(2f, 2f), 4f)
            ),
            color = textColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 6.dp)
        )
    }
}

// ============================================================================
// 5. SMART ADD CARD DIALOG
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardDialog(
    card: CardEntry?,
    onDismiss: () -> Unit,
    onSave: (CardEntry) -> Unit
) {
    val context = LocalContext.current

    // Form State
    var type by remember { mutableStateOf(card?.type ?: "visa") }
    var holder by remember { mutableStateOf(card?.holder ?: "") }
    var number by remember { mutableStateOf(card?.number ?: "") }
    var expiry by remember { mutableStateOf(card?.expiry ?: "") }
    var cvv by remember { mutableStateOf(card?.cvv ?: "") }
    var dob by remember { mutableStateOf(card?.dob ?: "") }
    var address by remember { mutableStateOf(card?.address ?: "") }
    var relation by remember { mutableStateOf(card?.relation ?: "") }
    var photoBase64 by remember { mutableStateOf(card?.photo ?: "") }

    // Logic to detect Gov ID
    val isGovtId = type.lowercase() in listOf("aadhaar", "pan", "voter", "license")

    // Image Picker
    val dialogScope = rememberCoroutineScope()
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            dialogScope.launch(Dispatchers.IO) {
                try {
                    val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    context.contentResolver.openInputStream(it)?.use { stream ->
                        BitmapFactory.decodeStream(stream, null, boundsOptions)
                    }
                    val sample = FileUtil.calculateInSampleSize(boundsOptions, 800, 800)
                    val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sample }
                    val bitmap = context.contentResolver.openInputStream(it)?.use { stream ->
                        BitmapFactory.decodeStream(stream, null, decodeOptions)
                    }
                    if (bitmap != null) {
                        val outputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
                        val compressedBytes = outputStream.toByteArray()
                        bitmap.recycle()
                        withContext(Dispatchers.Main) {
                            photoBase64 = Base64.encodeToString(compressedBytes, Base64.NO_WRAP)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- Header ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (card != null) "Edit Card" else "Add New",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, null)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- Type Selector ---
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = when (type) {
                            "visa" -> "Visa Card"
                            "master" -> "Mastercard"
                            "rupay" -> "RuPay Card"
                            "aadhaar" -> "Aadhaar Card"
                            "pan" -> "PAN Card"
                            "voter" -> "Voter ID"
                            "license" -> "Driving License"
                            else -> "Other Card"
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Card Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        val types = listOf(
                            "visa" to "Visa", "master" to "Mastercard",
                            "aadhaar" to "Aadhaar", "pan" to "PAN Card",
                            "voter" to "Voter ID", "license" to "Driving License"
                        )
                        types.forEach { (id, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = { type = id; expanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- Name & ID ---
                OutlinedTextField(
                    value = holder,
                    onValueChange = { holder = it },
                    label = { Text("Full Name (Mandatory)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleColors.SystemBlue
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text(if (isGovtId) "ID Number (Mandatory)" else "Card Number") },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = if (isGovtId) KeyboardType.Text else KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // --- Conditional Fields ---
                if (isGovtId) {
                    // 1. DOB and Relation
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = dob,
                            onValueChange = { dob = it },
                            label = { Text("DOB / Issue") },
                            placeholder = { Text("DD/MM/YYYY") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = relation,
                            onValueChange = { relation = it },
                            label = { Text("S/o, W/o") },
                            placeholder = { Text("Father/Husband") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. Address
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. Passport Photo Picker
                    Text("Passport Photo", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray)
                                .clickable {
                                    photoLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (photoBase64.isNotEmpty()) {
                                val bitmap = remember(photoBase64) {
                                    try {
                                        val bytes = Base64.decode(photoBase64, Base64.NO_WRAP)
                                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
                                    } catch (e: Exception) { null }
                                }
                                if(bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(Icons.Filled.BrokenImage, null)
                                }
                            } else {
                                Icon(Icons.Filled.AddAPhoto, null, tint = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "Tap to upload photo.\nOr use default avatar.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                } else {
                    // CREDIT CARD FIELDS
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = expiry,
                            onValueChange = { expiry = it },
                            label = { Text("Expiry (MM/YY)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Save Button ---
                Button(
                    onClick = {
                        // Minimal validation: Save as long as Name & ID are present
                        if (holder.isNotEmpty() && number.isNotEmpty()) {
                            val newEntry = CardEntry(
                                id = card?.id ?: UUID.randomUUID().toString(),
                                type = type,
                                holder = holder,
                                number = number,
                                expiry = expiry,
                                cvv = cvv,
                                dob = dob,
                                address = address,
                                relation = relation,
                                photo = photoBase64
                            )
                            onSave(newEntry)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = holder.isNotEmpty() && number.isNotEmpty()
                ) {
                    Text("Save Details")
                }
            }
        }
    }
}

// ============================================================================
// 6. HELPER COMPONENTS
// ============================================================================

@Composable
fun AutoGeneratedQr(data: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.background(Color.White, RoundedCornerShape(2.dp))) {
        // QR Code Version 1 is 21x21 modules
        val gridCount = 21
        // Calculate the size of a single black/white block
        val moduleSize = size.width / gridCount

        // Use the data hash to create a seed.
        // This ensures the SAME user data always generates the SAME QR pattern.
        val seed = data.hashCode()
        val random = Random(seed)

        for (row in 0 until gridCount) {
            for (col in 0 until gridCount) {
                // --- 1. Identify Finder Patterns (The 3 big squares) ---
                // Top-Left Finder (7x7)
                val isTL = (row < 7 && col < 7)
                // Top-Right Finder (7x7)
                val isTR = (row < 7 && col >= gridCount - 7)
                // Bottom-Left Finder (7x7)
                val isBL = (row >= gridCount - 7 && col < 7)

                // --- 2. Identify Separators (White space around finders) ---
                val isTLSep = (row == 7 && col <= 7) || (col == 7 && row <= 7)
                val isTRSep = (row == 7 && col >= gridCount - 8) || (col == gridCount - 8 && row <= 7)
                val isBLSep = (row == gridCount - 8 && col <= 7) || (col == 7 && row >= gridCount - 8)

                if (isTL || isTR || isBL) {
                    // Draw the structured Finder Pattern
                    drawFinderModule(row, col, gridCount, moduleSize)
                } else if (isTLSep || isTRSep || isBLSep) {
                    // Draw nothing (White Separator)
                } else {
                    // --- 3. Draw Data Modules ---
                    // Randomly draw black pixels based on the seed
                    // We add a check to ensure it doesn't look too dense or too sparse
                    if (random.nextBoolean()) {
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(col * moduleSize, row * moduleSize),
                            size = Size(moduleSize + 0.5f, moduleSize + 0.5f) // +0.5f to close anti-aliasing gaps
                        )
                    }
                }
            }
        }
    }
}

// Helper to draw the specific concentric squares of a finder pattern
fun DrawScope.drawFinderModule(row: Int, col: Int, gridCount: Int, moduleSize: Float) {
    // Normalize coordinates to 0..6 for the specific finder pattern logic
    val r = when {
        row < 7 -> row // Top
        else -> row - (gridCount - 7) // Bottom
    }
    val c = when {
        col < 7 -> col // Left
        col >= gridCount - 7 -> col - (gridCount - 7) // Right
        else -> col
    }

    // Logic for 7x7 Finder Pattern:
    // 1. Outer Ring (Black): Rows 0,6 OR Cols 0,6
    // 2. Inner Box (Black): Rows 2-4 AND Cols 2-4
    // 3. Middle Ring (White): Everything else

    val isOuterRing = (r == 0 || r == 6 || c == 0 || c == 6)
    val isInnerBox = (r in 2..4 && c in 2..4)

    if (isOuterRing || isInnerBox) {
        drawRect(
            color = Color.Black,
            topLeft = Offset(col * moduleSize, row * moduleSize),
            size = Size(moduleSize + 0.5f, moduleSize + 0.5f)
        )
    }
}

@Composable
fun CardLabelText(text: String, color: Color = Color.White.copy(alpha = 0.8f)) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            // Only show shadow if color is light
            shadow = if (color.luminance() > 0.5) Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 1f) else null
        ),
        color = color,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun CardValueText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp,
            shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 2f)
        ),
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun EmvChip(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val radius = 6.dp.toPx()
        val strokeWidth = 1.dp.toPx()

        // 1. Rich Metallic Gold Gradient
        val goldGradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFFFFEA7C), // Light reflection
                Color(0xFFD4AF37), // Base metallic gold
                Color(0xFFAA7700), // Shadowed gold
                Color(0xFFFFD700), // Standard gold
                Color(0xFFC59B27)  // Deep brass
            ),
            start = Offset(0f, 0f),
            end = Offset(width, height)
        )

        // Draw the base card plate
        drawRoundRect(
            brush = goldGradient,
            cornerRadius = CornerRadius(radius, radius)
        )

        // Colors for the 3D "engraved" look
        val shadowColor = Color(0xFF5A4005).copy(alpha = 0.8f)
        val highlightColor = Color(0xFFFFF2A8).copy(alpha = 0.8f)

        // 2. Define the standard EMV contact pad paths
        val chipPath = Path().apply {
            // Inner pill/rectangle
            val innerW = width * 0.45f
            val innerH = height * 0.60f
            val innerL = (width - innerW) / 2f
            val innerT = (height - innerH) / 2f
            val innerR = innerL + innerW
            val innerB = innerT + innerH
            val innerCorner = CornerRadius(width * 0.08f)

            addRoundRect(RoundRect(innerL, innerT, innerR, innerB, innerCorner))

            // Horizontal cuts (Dividing left and right contacts)
            val yTop = height * 0.35f
            val yBottom = height * 0.65f

            // Left side cuts
            moveTo(0f, yTop)
            lineTo(innerL, yTop)
            moveTo(0f, yBottom)
            lineTo(innerL, yBottom)

            // Right side cuts
            moveTo(width, yTop)
            lineTo(innerR, yTop)
            moveTo(width, yBottom)
            lineTo(innerR, yBottom)

            // Vertical cuts (Center dividers)
            val xCenter = width / 2f

            // Top vertical line
            moveTo(xCenter, 0f)
            lineTo(xCenter, innerT)

            // Inner vertical line (inside the pill)
            moveTo(xCenter, innerT)
            lineTo(xCenter, innerB)

            // Bottom vertical line
            moveTo(xCenter, innerB)
            lineTo(xCenter, height)
        }

        // 3. Draw Highlight (Offset slightly down and right)
        translate(left = strokeWidth * 0.5f, top = strokeWidth * 0.5f) {
            drawPath(
                path = chipPath,
                color = highlightColor,
                style = Stroke(strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        // 4. Draw Shadow (The actual cut)
        drawPath(
            path = chipPath,
            color = shadowColor,
            style = Stroke(strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // 5. Draw the outer engraved border
        drawRoundRect(
            color = shadowColor,
            cornerRadius = CornerRadius(radius, radius),
            style = Stroke(strokeWidth)
        )
    }
}

@Composable
fun ContactlessIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.graphicsLayer(rotationZ = 90f)) {
        val stroke = 2.5.dp.toPx()
        for (i in 0..2) {
            val scale = 1f - (i * 0.3f)
            if (scale > 0) {
                val arcSize = size.width * scale
                val offset = (size.width - arcSize) / 2
                drawArc(
                    color = color,
                    startAngle = 225f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(offset, size.height - (arcSize / 2) - (size.height / 2)),
                    size = Size(arcSize, arcSize),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Composable
fun PassportPhoto(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFFE0E0E0))
            .border(2.dp, Color.White, RoundedCornerShape(2.dp))
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }
}

fun formatIdNumber(number: String, type: String): String {
    val clean = number.replace(" ", "").trim()
    return when (type.lowercase()) {
        "aadhaar" -> clean.chunked(4).joinToString(" ")
        "pan" -> clean.uppercase()
        "voter" -> clean.uppercase()
        else -> clean.chunked(4).joinToString(" ")
    }
}

fun formatCardNumber(number: String): String {
    val clean = number.replace(" ", "").trim()
    return clean.chunked(4).joinToString(" ")
}

@Composable
fun CardDetailsDialog(
    card: CardEntry,
    onDismiss: () -> Unit
) {
    var showCVV by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Card Details",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 22.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        fontWeight = FontWeight.W700
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                CardDetailRow("Type", card.type.uppercase())
                CardDetailRow("Holder", card.holder)
                val isGov = card.type.lowercase() in listOf("aadhaar", "pan", "voter", "license")
                CardDetailRow("Number", if (isGov) formatIdNumber(card.number, card.type) else formatCardNumber(card.number))

                if (card.dob.isNotEmpty()) CardDetailRow("DOB", card.dob)
                if (card.expiry.isNotEmpty()) CardDetailRow("Expiry/Issue", card.expiry)
                if (card.relation.isNotEmpty()) CardDetailRow("Relation", card.relation)
                if (card.address.isNotEmpty()) CardDetailRow("Address", card.address)

                if (card.cvv.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CVV",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.W400
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (showCVV) card.cvv else "•••",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 17.sp,
                                    letterSpacing = if (showCVV) (-0.2).sp else 2.sp
                                ),
                                fontWeight = FontWeight.W500
                            )

                            IconButton(
                                onClick = { showCVV = !showCVV },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        AppleColors.SystemBlue.copy(alpha = 0.15f),
                                        RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Icon(
                                    if (showCVV) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = "Toggle CVV",
                                    tint = AppleColors.SystemBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AppleButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Done",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W600,
                        letterSpacing = (-0.2).sp
                    )
                }
            }
        }
    }
}

@Composable
fun CardDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                letterSpacing = (-0.2).sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.W400,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp,
                letterSpacing = (-0.2).sp
            ),
            fontWeight = FontWeight.W500,
            modifier = Modifier.weight(1.5f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}
// ============================================================================
// SETTINGS DIALOG - APPLE STYLE
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    appData: AppData,
    onDismiss: () -> Unit,
    onSaveData: (AppData) -> Unit,
    onLogout: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    repository: DataRepository
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(appData.user.name) }
    var showExportProgress by remember { mutableStateOf(false) }
    var showImportProgress by remember { mutableStateOf(false) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            showExportProgress = true
            scope.launch(Dispatchers.IO) {
                try {
                    val json = Gson().toJson(appData)
                    context.contentResolver.openOutputStream(it)?.use { output ->
                        output.write(json.toByteArray())
                    }
                    withContext(Dispatchers.Main) {
                        showExportProgress = false
                        onShowSnackbar("✓ Backup exported successfully")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showExportProgress = false
                        onShowSnackbar("Export failed: ${e.message}")
                    }
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            showImportProgress = true
            scope.launch(Dispatchers.IO) {
                try {
                    val json = context.contentResolver.openInputStream(it)?.bufferedReader()?.use { reader ->
                        reader.readText()
                    }
                    json?.let { jsonStr ->
                        val imported = Gson().fromJson(jsonStr, AppData::class.java)
                        withContext(Dispatchers.Main) {
                            onSaveData(imported)
                            showImportProgress = false
                            onShowSnackbar("✓ Backup imported successfully")
                            onDismiss()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showImportProgress = false
                        onShowSnackbar("Import failed: ${e.message}")
                    }
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 28.sp,
                                letterSpacing = (-0.4).sp
                            ),
                            fontWeight = FontWeight.W700
                        )

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                AppleDivider()

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // BACKUP & RESTORE Section
                    AppleSectionHeader("BACKUP & RESTORE")

                    AppleCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Save or restore your vault data",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 13.sp,
                                    letterSpacing = (-0.1).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.W400
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val timestamp = SimpleDateFormat(
                                            "yyyyMMdd_HHmmss",
                                            Locale.getDefault()
                                        ).format(Date())
                                        exportLauncher.launch("securething_backup_$timestamp.json")
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = !showExportProgress,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AppleColors.SystemBlue.copy(alpha = 0.15f),
                                        contentColor = AppleColors.SystemBlue
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                                ) {
                                    if (showExportProgress) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = AppleColors.SystemBlue
                                        )
                                    } else {
                                        Icon(
                                            Icons.Filled.Upload,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Export", fontSize = 15.sp, fontWeight = FontWeight.W600)
                                }

                                Button(
                                    onClick = {
                                        importLauncher.launch(arrayOf("application/json"))
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = !showImportProgress,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AppleColors.SystemGreen.copy(alpha = 0.15f),
                                        contentColor = AppleColors.SystemGreen
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                                ) {
                                    if (showImportProgress) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = AppleColors.SystemGreen
                                        )
                                    } else {
                                        Icon(
                                            Icons.Filled.Download,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Import", fontSize = 15.sp, fontWeight = FontWeight.W600)
                                }
                            }
                        }
                    }

                    // ACCOUNT Section
                    AppleSectionHeader("ACCOUNT")

                    AppleCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(
                                value = username,
                                onValueChange = {
                                    username = it
                                    onSaveData(appData.copy(user = appData.user.copy(name = it)))
                                },
                                label = { Text("Username") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppleColors.SystemBlue,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AppleDivider()

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Gender",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 15.sp,
                                            letterSpacing = (-0.2).sp
                                        ),
                                        fontWeight = FontWeight.W500
                                    )
                                    Text(
                                        text = appData.user.gender.replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 13.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.W400
                                    )
                                }

                                Icon(
                                    imageVector = if (appData.user.gender == "male")
                                        Icons.Filled.Male
                                    else
                                        Icons.Filled.Female,
                                    contentDescription = null,
                                    tint = if (appData.user.gender == "male")
                                        AppleColors.SystemBlue
                                    else
                                        AppleColors.SystemPink
                                )
                            }
                        }
                    }

                    // SECURITY Section
                    AppleSectionHeader("SECURITY")

                    AppleCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Authentication Type
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Authentication",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 15.sp,
                                            letterSpacing = (-0.2).sp
                                        ),
                                        fontWeight = FontWeight.W500
                                    )
                                    Text(
                                        text = when (appData.user.authType) {
                                            "pin" -> "4-digit PIN"
                                            "password" -> "Password"
                                            else -> "Biometric"
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 13.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.W400
                                    )
                                }

                                Icon(
                                    imageVector = when (appData.user.authType) {
                                        "pin" -> Icons.Filled.Pin
                                        "password" -> Icons.Filled.Lock
                                        else -> Icons.Filled.Fingerprint
                                    },
                                    contentDescription = null,
                                    tint = AppleColors.SystemBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            AppleDivider()
                            Spacer(modifier = Modifier.height(16.dp))

                            // Auto-lock settings
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Auto-lock",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 15.sp,
                                            letterSpacing = (-0.2).sp
                                        ),
                                        fontWeight = FontWeight.W500
                                    )
                                    Text(
                                        text = if (appData.settings.autoLockEnabled)
                                            "After ${appData.settings.autoLockMinutes} ${if (appData.settings.autoLockMinutes == 1) "minute" else "minutes"}"
                                        else
                                            "Disabled",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 13.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.W400
                                    )
                                }

                                Switch(
                                    checked = appData.settings.autoLockEnabled,
                                    onCheckedChange = {
                                        onSaveData(appData.copy(
                                            settings = appData.settings.copy(autoLockEnabled = it)
                                        ))
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AppleColors.SystemGreen,
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                            }

                            if (appData.settings.autoLockEnabled) {
                                Spacer(modifier = Modifier.height(16.dp))

                                var sliderPosition by remember {
                                    mutableFloatStateOf(appData.settings.autoLockMinutes.toFloat())
                                }

                                Text(
                                    text = "Lock after ${sliderPosition.toInt()} ${if (sliderPosition.toInt() == 1) "minute" else "minutes"}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontSize = 13.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.W400
                                )

                                Slider(
                                    value = sliderPosition,
                                    onValueChange = { sliderPosition = it },
                                    onValueChangeFinished = {
                                        onSaveData(appData.copy(
                                            settings = appData.settings.copy(
                                                autoLockMinutes = sliderPosition.toInt()
                                            )
                                        ))
                                    },
                                    valueRange = 1f..10f,
                                    steps = 8,
                                    colors = SliderDefaults.colors(
                                        thumbColor = AppleColors.SystemBlue,
                                        activeTrackColor = AppleColors.SystemBlue,
                                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        }
                    }

                    // STORAGE Section
                    AppleSectionHeader("STORAGE")

                    AppleCard {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val totalSize = appData.files.sumOf { it.size }
                            val binSize = appData.recycleBin.sumOf { it.size }

                            StorageInfoRow(
                                "Files",
                                appData.files.size,
                                totalSize,
                                Icons.Filled.Folder
                            )

                            StorageInfoRow(
                                "Recycle Bin",
                                appData.recycleBin.size,
                                binSize,
                                Icons.Filled.Delete
                            )

                            StorageInfoRow(
                                "Passwords",
                                appData.passwords.size,
                                0L,
                                Icons.Filled.Key
                            )

                            StorageInfoRow(
                                "Cards",
                                appData.cards.size,
                                0L,
                                Icons.Filled.CreditCard
                            )
                        }
                    }

                    // APP INFO Section
                    AppleSectionHeader("APP INFO")

                    AppleCard {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AppInfoRowItem("Version", "2.0 Apple Edition")
                            AppInfoRowItem("Build", "Optimized Premium")
                            AppInfoRowItem("Theme", appData.settings.theme.replaceFirstChar { it.uppercase() })
                        }
                    }

                    // ACTIONS
                    AppleButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppleColors.SystemBlue.copy(alpha = 0.15f),
                            contentColor = AppleColors.SystemBlue
                        )
                    ) {
                        Icon(
                            Icons.Filled.Logout,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Log Out",
                            fontWeight = FontWeight.W600,
                            fontSize = 17.sp,
                            letterSpacing = (-0.2).sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppleColors.SystemRed.copy(alpha = 0.15f),
                            contentColor = AppleColors.SystemRed
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Icon(
                            Icons.Filled.DeleteForever,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Delete Account & Reset",
                            fontWeight = FontWeight.W600,
                            fontSize = 17.sp,
                            letterSpacing = (-0.2).sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    if (showDeleteConfirm) {
        DeleteAccountDialog(
            security = appData.security,
            onConfirm = {
                scope.launch {
                    repository.clearAllData()
                    onDismiss()
                }
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }
}

@Composable
fun StorageInfoRow(
    label: String,
    count: Int,
    size: Long,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = AppleColors.SystemBlue
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    letterSpacing = (-0.2).sp
                ),
                fontWeight = FontWeight.W400
            )
        }

        Text(
            text = if (size > 0) {
                "$count (${(size / 1024f / 1024f).let { "%.1f".format(it) }} MB)"
            } else {
                "$count ${if (count == 1) "item" else "items"}"
            },
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 13.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.W400
        )
    }
}

@Composable
fun AppInfoRowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                letterSpacing = (-0.2).sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.W400
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                letterSpacing = (-0.2).sp
            ),
            fontWeight = FontWeight.W500
        )
    }
}

// ============================================================================
// DELETE ACCOUNT DIALOG - APPLE STYLE
// ============================================================================

@Composable
fun DeleteAccountDialog(
    security: Security,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var answer by remember { mutableStateOf("") }
    val question = if (Random.nextBoolean())
        security.q1 to security.a1
    else
        security.q2 to security.a2
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        AppleCard {
            Column(modifier = Modifier.padding(24.dp)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            AppleColors.SystemRed.copy(alpha = 0.15f),
                            CircleShape
                        )
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = null,
                        tint = AppleColors.SystemRed,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Delete Everything?",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 22.sp,
                        letterSpacing = (-0.3).sp
                    ),
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = AppleColors.SystemRed
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "This action is PERMANENT and cannot be undone. All your files, passwords, and cards will be deleted.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W400
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "To confirm, answer your security question:",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.W400
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppleCard {
                    Text(
                        text = question.first,
                        fontWeight = FontWeight.W600,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = answer,
                    onValueChange = {
                        answer = it
                        showError = false
                    },
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = showError,
                    supportingText = if (showError) {
                        {
                            Text(
                                "Incorrect answer. Please try again.",
                                color = AppleColors.SystemRed
                            )
                        }
                    } else null,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleColors.SystemBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        errorBorderColor = AppleColors.SystemRed
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                AppleButton(
                    onClick = {
                        if (answer.lowercase().trim() == question.second) {
                            onConfirm()
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = answer.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppleColors.SystemRed
                    )
                ) {
                    Icon(
                        Icons.Filled.DeleteForever,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "DELETE ALL",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W700,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppleColors.SystemBlue
                    )
                ) {
                    Text(
                        "Cancel",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W400,
                        letterSpacing = (-0.2).sp
                    )
                }
            }
        }
    }
}
@Composable
fun StorageLabel(
    color: Color,
    label: String,
    count: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun AppleFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    icon: ImageVector? = null,
    count: Int? = null
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) AppleColors.SystemBlue else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon as ImageVector,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp),
                fontWeight = if (selected) FontWeight.W600 else FontWeight.W400
            )
            if (count != null) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}