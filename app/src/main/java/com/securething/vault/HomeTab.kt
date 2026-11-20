package com.securething.vault

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import android.util.Base64
import android.util.LruCache
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import com.composables.icons.lucide.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.*
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*
import androidx.compose.animation.core.RepeatMode

// ============================================================================
// APPLE STYLE THEME — iOS System Colors
// ============================================================================

data class NCThemeColors(
    val Bg         : Color,
    val Surface    : Color,
    val SurfaceHi  : Color,
    val SurfacePop : Color,
    val Border     : Color,
    val BorderGlow : Color,
    val TextPri    : Color,
    val TextSec    : Color,
    val TextMut    : Color,
    val TextHint   : Color,
    val isDark     : Boolean
)

// Samsung One UI Dark Theme — Pure AMOLED Black Canvas with Clean Focus Blocks
val DarkNCTheme = NCThemeColors(
    Bg         = Color(0xFF000000),
    Surface    = Color(0xFF1C1C1E),
    SurfaceHi  = Color(0xFF26262A),
    SurfacePop = Color(0xFF16253B),
    Border     = Color(0xFF2C2C2E),
    BorderGlow = Color(0xFF3E91FF).copy(alpha = 0.25f),
    TextPri    = Color(0xFFFFFFFF),
    TextSec    = Color(0xFF9E9E9E),
    TextMut    = Color(0xFF5A5A5E),
    TextHint   = Color(0xFF3A3A3E),
    isDark     = true
)

// Samsung One UI Light Theme — Clean Canvas (#FAFAFA) with Distinct Focus Blocks (#FFFFFF)
val LightNCTheme = NCThemeColors(
    Bg         = Color(0xFFFAFAFA),
    Surface    = Color(0xFFFFFFFF),
    SurfaceHi  = Color(0xFFF2F4F7),
    SurfacePop = Color(0xFFEBF3FE),
    Border     = Color(0xFFE5E7EB),
    BorderGlow = Color(0xFF0381FE).copy(alpha = 0.2f),
    TextPri    = Color(0xFF111111),
    TextSec    = Color(0xFF767676),
    TextMut    = Color(0xFF9CA3AF),
    TextHint   = Color(0xFFD1D5DB),
    isDark     = false
)

val LocalNCTheme = compositionLocalOf { DarkNCTheme }

// ============================================================================
// ============================================================================
// APPLE STYLE HELPERS — clean iOS surface builders
// ============================================================================
// ============================================================================

/** Primary One UI Focus Block surface — #FFFFFF (light) / #1C1C1E (dark) */
fun raisedBrush(isDark: Boolean) = Brush.verticalGradient(
    if (isDark) listOf(Color(0xFF1C1C1E), Color(0xFF1C1C1E))
    else        listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF))
)

/** Elevated surface / container */
fun recessedBrush(isDark: Boolean) = Brush.verticalGradient(
    if (isDark) listOf(Color(0xFF26262A), Color(0xFF222225))
    else        listOf(Color(0xFFF2F4F7), Color(0xFFECEFF3))
)

/** Grouped Focus Block card background */
fun metalBrush(isDark: Boolean) = Brush.verticalGradient(
    if (isDark) listOf(Color(0xFF1C1C1E), Color(0xFF1C1C1E))
    else        listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF))
)

/** One UI border — subtle hairline */
fun bevelBorder(isDark: Boolean) = BorderStroke(
    1.dp,
    if (isDark) Color(0xFF2C2C2E) else Color(0xFFE5E7EB)
)

/** One UI container border */
fun insetBorder(isDark: Boolean) = BorderStroke(
    1.dp,
    if (isDark) Color(0xFF26262A) else Color(0xFFE5E7EB)
)


// ============================================================================
// ACCENT COLORS — iOS system palette
// ============================================================================

object NC {
    // Samsung One UI Color Palette
    val Cyan     = Color(0xFF00B2FE)  // One UI Cyan
    val Violet   = Color(0xFF7B61FF)  // One UI Purple
    val Pink     = Color(0xFFFF2D55)  // One UI Pink
    val Green    = Color(0xFF22C55E)  // One UI Green
    val GreenDim = Color(0xFF16A34A)
    val Amber    = Color(0xFFFF9F0A)  // One UI Amber / Warning
    val Red      = Color(0xFFF04438)  // One UI Red / Danger

    // One UI Vibrant Blue — Primary Accent
    val Blue      = Color(0xFF0381FE)  // One UI Vibrant Blue
    val BlueDark  = Color(0xFF0072DE)  // One UI Primary Dark
    val Gold      = Color(0xFF0381FE)  // mapped to Blue
    val GoldLight = Color(0xFF3E91FF)  // One UI Active Light
    val GoldDark  = Color(0xFF0072DE)
    val Brass     = Color(0xFF0381FE)
    val Copper    = Color(0xFF22C55E)

    fun accentFor(type: String) = when (type) {
        "photo" -> Cyan; "video" -> Violet; "audio" -> Green; else -> Amber
    }

    fun gradientFor(type: GalleryType): Brush = when (type) {
        GalleryType.ALL       -> Brush.linearGradient(listOf(Color(0xFF0381FE), Color(0xFF3E91FF)))
        GalleryType.IMAGES    -> Brush.linearGradient(listOf(Cyan, Color(0xFF0381FE)))
        GalleryType.VIDEOS    -> Brush.linearGradient(listOf(Violet, Color(0xFF6366F1)))
        GalleryType.AUDIO     -> Brush.linearGradient(listOf(Green, Color(0xFF16A34A)))
        GalleryType.FILES     -> Brush.linearGradient(listOf(Amber, Color(0xFFF59E0B)))
        GalleryType.FAVORITES -> Brush.linearGradient(listOf(Pink, Color(0xFFE11D48)))
    }

    fun iconFor(type: String): ImageVector = when (type) {
        "photo" -> Lucide.Image
        "video" -> Lucide.Video
        "audio" -> Lucide.Music
        else    -> Lucide.FileText
    }
}

// ============================================================================
// NAVIGATION
// ============================================================================

enum class GalleryType(
    val label     : String,
    val filterKey : String,
    val icon      : ImageVector,
    val color     : @Composable () -> Color
) {
    ALL      ("All Files",   "all",      Lucide.FolderOpen, { NC.Blue }),
    IMAGES   ("Images",      "photo",    Lucide.Image,      { NC.Cyan }),
    VIDEOS   ("Videos",      "video",    Lucide.Video,      { NC.Violet }),
    AUDIO    ("Audio",       "audio",    Lucide.Music,      { NC.Green }),
    FILES    ("Documents",   "file",     Lucide.FileText,   { NC.Amber }),
    FAVORITES("Favorites",   "favorite", Lucide.Heart,      { NC.Pink })
}

sealed class VaultScreen {
    data object Home : VaultScreen()
    data class Gallery(val type: GalleryType) : VaultScreen()
}

// ============================================================================
// THUMBNAIL CACHE
// ============================================================================

object ThumbnailCache {
    private val maxMem = (Runtime.getRuntime().maxMemory() / 8).toInt()
    private val cache  = object : LruCache<String, Bitmap>(maxMem) {
        override fun sizeOf(key: String, value: Bitmap): Int = value.byteCount
    }
    fun get(key: String): Bitmap? = cache.get(key)
    fun put(key: String, bmp: Bitmap) { cache.put(key, bmp) }
}

// ============================================================================
// ASYNC THUMBNAIL LOADER
// ============================================================================

@Composable
fun rememberAsyncThumbnail(file: VaultFile, targetPx: Int = 240): State<Bitmap?> {
    val cacheKey = "${file.id}_$targetPx"
    return produceState<Bitmap?>(initialValue = ThumbnailCache.get(cacheKey), key1 = cacheKey) {
        if (value != null) return@produceState
        value = withContext(Dispatchers.IO) {
            try {
                val src = file.thumbnail.ifEmpty { file.data }
                if (src.isEmpty()) return@withContext null
                val bytes = Base64.decode(src, Base64.NO_WRAP)
                val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
                var sample = 1
                var w = opts.outWidth; var h = opts.outHeight
                while (w / 2 >= targetPx && h / 2 >= targetPx) { sample *= 2; w /= 2; h /= 2 }
                val decodeOpts = BitmapFactory.Options().apply {
                    inSampleSize      = sample
                    inPreferredConfig = Bitmap.Config.RGB_565
                }
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOpts)
                if (bmp != null) ThumbnailCache.put(cacheKey, bmp)
                bmp
            } catch (_: Exception) { null }
        }
    }
}

// ============================================================================
// DEVICE STORAGE UTILS
// ============================================================================

data class DeviceStorageInfo(
    val totalBytes : Long,
    val usedBytes  : Long,
    val freeBytes  : Long
)

fun getDeviceStorageInfo(): DeviceStorageInfo {
    return try {
        val stat  = StatFs(Environment.getDataDirectory().absolutePath)
        val total = stat.totalBytes
        val free  = stat.availableBytes
        DeviceStorageInfo(total, total - free, free)
    } catch (_: Exception) {
        DeviceStorageInfo(1L, 0L, 1L)
    }
}

fun formatStorageSize(bytes: Long): String {
    return when {
        bytes >= 1_000_000_000L -> "%.1f GB".format(bytes / 1_000_000_000.0)
        bytes >= 1_000_000L     -> "%.0f MB".format(bytes / 1_000_000.0)
        else                    -> "%.0f KB".format(bytes / 1_000.0)
    }
}

// ============================================================================
// BITMAP & FILE UTILS
// ============================================================================

object BitmapUtils {
    fun decodeBase64(data: String): Bitmap? = try {
        val b = Base64.decode(data, Base64.NO_WRAP)
        BitmapFactory.decodeByteArray(b, 0, b.size)
    } catch (_: Exception) { null }

    fun writeTempFile(context: Context, file: VaultFile): File {
        val bytes = Base64.decode(file.data, Base64.NO_WRAP)
        val ext   = file.name.substringAfterLast('.', "bin")
        val tmp   = File(context.cacheDir, "vault_${file.id}.$ext")
        if (!tmp.exists()) FileOutputStream(tmp).use { it.write(bytes) }
        return tmp
    }
}

// ============================================================================
// MUSIC PLAYER CONTROLLER
// ============================================================================

enum class RepeatModeEnum { OFF, ONE, ALL }

@Stable
class MusicPlayerController {
    var currentFile by mutableStateOf<VaultFile?>(null);  private set
    var isPlaying   by mutableStateOf(false);             private set
    var isLoading   by mutableStateOf(false)
    var progress    by mutableStateOf(0f)
    var duration    by mutableStateOf(1L)
    var position    by mutableStateOf(0L)
    var showMini    by mutableStateOf(false)
    var shuffle     by mutableStateOf(false)
    var repeatMode  by mutableStateOf(RepeatModeEnum.OFF)
    var queue       by mutableStateOf<List<VaultFile>>(emptyList()); private set
    var queueIndex  by mutableStateOf(-1);                private set

    private var mp: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var ticker: Runnable? = null
    private var loadJob: Job? = null
    private val playerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun updateQueue(files: List<VaultFile>) { queue = files }

    fun pause() {
        val m = mp ?: return
        if (m.isPlaying) {
            m.pause()
            isPlaying = false
            stopTicker()
        }
    }

    fun play(file: VaultFile, context: Context) {
        val idx = queue.indexOfFirst { it.id == file.id }
        if (idx >= 0) queueIndex = idx
        else { queue = queue + file; queueIndex = queue.lastIndex }
        playAtIndex(queueIndex, context)
    }

    fun playNext(context: Context) {
        if (queue.isEmpty()) return
        val n = when {
            shuffle    -> queue.indices.random()
            repeatMode == RepeatModeEnum.ONE -> queueIndex
            queueIndex < queue.lastIndex     -> queueIndex + 1
            repeatMode == RepeatModeEnum.ALL -> 0
            else -> return
        }
        queueIndex = n; playAtIndex(n, context)
    }

    fun playPrev(context: Context) {
        if (queue.isEmpty()) return
        val p = when {
            shuffle        -> queue.indices.random()
            queueIndex > 0 -> queueIndex - 1
            repeatMode == RepeatModeEnum.ALL -> queue.lastIndex
            else -> 0
        }
        queueIndex = p; playAtIndex(p, context)
    }

    fun togglePlayPause() {
        val m = mp ?: return
        if (m.isPlaying) { m.pause(); isPlaying = false; stopTicker() }
        else             { m.start(); isPlaying = true;  startTicker() }
    }

    fun seekTo(frac: Float) {
        val m   = mp ?: return
        val pos = (frac * m.duration).toInt().coerceAtLeast(0)
        m.seekTo(pos); progress = frac; position = pos.toLong()
    }

    fun stop() { stopInternal(); showMini = false; currentFile = null }

    private fun playAtIndex(index: Int, context: Context) {
        val file = queue.getOrNull(index) ?: return
        stopInternal()
        currentFile = file; showMini = true; isLoading = true
        loadJob = playerScope.launch {
            var tempMp: MediaPlayer? = null
            try {
                val newMp = withContext(Dispatchers.IO) {
                    val tmp = BitmapUtils.writeTempFile(context, file)
                    MediaPlayer().apply {
                        setDataSource(tmp.absolutePath)
                        prepare()
                    }
                }
                tempMp = newMp
                if (!isActive) {
                    newMp.release()
                    return@launch
                }
                mp = newMp
                duration = newMp.duration.toLong().coerceAtLeast(1L)
                isLoading = false
                newMp.setOnCompletionListener {
                    isPlaying = false; progress = 0f; position = 0L
                    when (repeatMode) {
                        RepeatModeEnum.ONE -> playAtIndex(queueIndex, context)
                        else -> playNext(context)
                    }
                }
                newMp.start(); isPlaying = true; startTicker()
            } catch (e: Exception) {
                tempMp?.release()
                if (isActive) {
                    isLoading = false; isPlaying = false
                }
            }
        }
    }

    private fun stopInternal() {
        loadJob?.cancel()
        loadJob = null
        stopTicker()
        try { mp?.let { if (it.isPlaying) it.stop(); it.release() } } catch (_: Exception) {}
        mp = null; isPlaying = false; progress = 0f; position = 0L
    }

    private fun startTicker() {
        stopTicker()
        val r = object : Runnable {
            override fun run() {
                val m = mp ?: return
                try {
                    duration = m.duration.toLong().coerceAtLeast(1L)
                    position = m.currentPosition.toLong()
                    progress = position.toFloat() / duration.toFloat()
                    handler.postDelayed(this, 150)
                } catch (_: Exception) {}
            }
        }
        ticker = r; handler.post(r)
    }

    private fun stopTicker() {
        ticker?.let { handler.removeCallbacks(it) }
        ticker = null
    }

    fun formatMs(ms: Long): String {
        val s = ms / 1000
        return "%d:%02d".format(s / 60, s % 60)
    }
}

// ============================================================================
// HOME TAB ROOT
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTab(
    appData: AppData,
    onSaveData: (AppData) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val isDark    = appData.settings.theme != "light"
    val theme     = if (isDark) DarkNCTheme else LightNCTheme
    val matScheme = if (isDark) darkColorScheme() else lightColorScheme()

    val musicCtrl      = remember { MusicPlayerController() }
    val context        = LocalContext.current
    var currentScreen  by remember { mutableStateOf<VaultScreen>(VaultScreen.Home) }
    var selectedFile   by remember { mutableStateOf<VaultFile?>(null) }
    var showMusicSheet by remember { mutableStateOf(false) }

    val audioFiles = remember(appData.files) { appData.files.filter { it.type == "audio" } }
    LaunchedEffect(audioFiles) { musicCtrl.updateQueue(audioFiles) }

    BackHandler(enabled = currentScreen !is VaultScreen.Home) {
        currentScreen = VaultScreen.Home
    }

    CompositionLocalProvider(LocalNCTheme provides theme) {
        MaterialTheme(colorScheme = matScheme) {
            Box(Modifier.fillMaxSize().background(theme.Bg)) {

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        val fwd = targetState is VaultScreen.Gallery
                        (if (fwd) slideInHorizontally { it } else slideInHorizontally { -it / 3 }) +
                                fadeIn(tween(240)) togetherWith
                                (if (fwd) slideOutHorizontally { -it / 3 } else slideOutHorizontally { it }) +
                                fadeOut(tween(160))
                    },
                    label = "screen"
                ) { screen ->
                    when (screen) {
                        is VaultScreen.Home    -> HomeScreen(
                            appData        = appData,
                            onSaveData     = onSaveData,
                            onShowSnackbar = onShowSnackbar,
                            musicCtrl      = musicCtrl,
                            onNavigate     = { currentScreen = VaultScreen.Gallery(it) },
                            onFileClick    = { selectedFile = it },
                            onOpenMusic    = { showMusicSheet = true }
                        )
                        is VaultScreen.Gallery -> GalleryScreen(
                            appData        = appData,
                            onSaveData     = onSaveData,
                            onShowSnackbar = onShowSnackbar,
                            galleryType    = screen.type,
                            musicCtrl      = musicCtrl,
                            onBack         = { currentScreen = VaultScreen.Home },
                            onFileClick    = { selectedFile = it },
                            onOpenMusic    = { showMusicSheet = true }
                        )
                    }
                }

                AnimatedVisibility(
                    visible  = musicCtrl.showMini,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 92.dp)
                        .zIndex(10f),
                    enter = slideInVertically { it / 2 } + fadeIn(tween(280)),
                    exit  = slideOutVertically { it / 2 } + fadeOut(tween(180))
                ) {
                    MiniMusicPlayer(musicCtrl, { showMusicSheet = true }, context)
                }

                MusicFAB(
                    ctrl         = musicCtrl,
                    onOpenPlayer = { showMusicSheet = true },
                    modifier     = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 92.dp, end = 16.dp)
                        .zIndex(11f)
                )
            }

            selectedFile?.let { f ->
                NeoMediaViewerDialog(f, { selectedFile = null }, onShowSnackbar, musicCtrl, context)
            }

            if (showMusicSheet) {
                MusicPlayerSheet(musicCtrl, audioFiles, context) { showMusicSheet = false }
            }
        }
    }
}

// ============================================================================
// HOME SCREEN
// ============================================================================

@Composable
fun HomeScreen(
    appData        : AppData,
    onSaveData     : (AppData) -> Unit,
    onShowSnackbar : (String) -> Unit,
    musicCtrl      : MusicPlayerController,
    onNavigate     : (GalleryType) -> Unit,
    onFileClick    : (VaultFile) -> Unit,
    onOpenMusic    : () -> Unit
) {
    val deviceStorage = remember { getDeviceStorageInfo() }
    val vaultBytes    = remember(appData.files) { appData.files.sumOf { it.size } }
    val recentFiles   = remember(appData.files) { appData.files.sortedByDescending { it.date }.take(12) }
    val audioFiles    = remember(appData.files) { appData.files.filter { it.type == "audio" } }

    val onToggleFav: (VaultFile) -> Unit = { file ->
        val newFav = !file.fav
        onSaveData(appData.copy(
            files = appData.files.map { if (it.id == file.id) it.copy(fav = newFav) else it }
        ))
        onShowSnackbar(if (newFav) "Added to Favorites" else "Removed from Favorites")
    }

    LazyColumn(
        modifier       = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 200.dp)
    ) {
        item {
            HomeTopBar(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 10.dp))
        }
        item {
            VaultHeaderCard(
                vaultBytes    = vaultBytes,
                deviceStorage = deviceStorage,
                appData       = appData,
                modifier      = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )
        }
        item { SkeuSectionHeader("Categories", modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)) }
        item {
            GalleryCategoryGrid(
                appData    = appData,
                onNavigate = onNavigate,
                modifier   = Modifier.padding(horizontal = 24.dp)
            )
        }
        if (recentFiles.isNotEmpty()) {
            item {
                SkeuSectionHeaderWithAction(
                    label    = "Recent",
                    action   = "See all",
                    onAction = { onNavigate(GalleryType.ALL) },
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp)
                )
            }
            item {
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(recentFiles, key = { it.id }) { file ->
                        RecentFileCard(
                            file        = file,
                            onClick     = { onFileClick(file) },
                            onToggleFav = { onToggleFav(file) },
                            onDelete    = {
                                val fmt = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
                                onSaveData(appData.copy(
                                    files      = appData.files.filter { it.id != file.id },
                                    recycleBin = appData.recycleBin + file.copy(deletedDate = fmt.format(Date()))
                                ))
                                onShowSnackbar("Moved to Recycle Bin")
                            }
                        )
                    }
                }
            }
        }
        if (audioFiles.isNotEmpty()) {
            item {
                SkeuSectionHeaderWithAction(
                    label       = "Music",
                    action      = "Open Player",
                    onAction    = onOpenMusic,
                    actionColor = NC.Green,
                    modifier    = Modifier.padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 14.dp)
                )
            }
            item {
                MusicQuickRow(audioFiles = audioFiles, musicCtrl = musicCtrl, onOpenPlayer = onOpenMusic)
            }
        }
        item {
            QuickStatsRow(
                appData       = appData,
                deviceStorage = deviceStorage,
                modifier      = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
            )
        }
    }
}

// ============================================================================
// HOME TOP BAR — engraved brass plate look
// ============================================================================

@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {
    val t = LocalNCTheme.current
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "My files",
                color         = t.TextPri,
                fontSize      = 34.sp,
                fontWeight    = FontWeight.Light,
                letterSpacing = (-0.5).sp
            )
            Text(
                "Encrypted Vault",
                color         = NC.Blue,
                fontSize      = 14.sp,
                fontWeight    = FontWeight.Medium
            )
        }
        // One UI Shield Icon Container
        Box(
            Modifier
                .size(48.dp)
                .background(NC.Blue.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Lucide.ShieldCheck, null, tint = NC.Blue, modifier = Modifier.size(24.dp))
        }
    }
}

// ============================================================================
// SECTION HELPERS — engraved style
// ============================================================================

@Composable
fun SkeuSectionHeader(text: String, modifier: Modifier = Modifier) {
    val t = LocalNCTheme.current
    Text(
        text       = text,
        color      = t.TextPri,
        fontSize   = 18.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        modifier   = modifier
    )
}

// Kept for backward compat
@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) = SkeuSectionHeader(text, modifier)

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    val t = LocalNCTheme.current
    Text(
        text          = text.uppercase(),
        color         = NC.Blue.copy(.55f),
        fontSize      = 9.sp,
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = 2.sp,
        fontFamily    = FontFamily.Monospace,
        modifier      = modifier
    )
}

@Composable
fun SkeuSectionHeaderWithAction(
    label       : String,
    action      : String,
    onAction    : () -> Unit,
    actionColor : Color = Color.Unspecified,
    modifier    : Modifier = Modifier
) {
    val t      = LocalNCTheme.current
    val aColor = if (actionColor == Color.Unspecified) NC.Blue else actionColor
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        SkeuSectionHeader(label)
        // One UI Pill Action Button
        Box(
            Modifier
                .shadow(1.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.1f))
                .background(if (t.isDark) Color(0xFF16253B) else Color(0xFFEBF3FE), RoundedCornerShape(20.dp))
                .clickable { onAction() }
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(action, color = aColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SectionHeaderWithAction(
    label       : String,
    action      : String,
    onAction    : () -> Unit,
    actionColor : Color = Color.Unspecified,
    modifier    : Modifier = Modifier
) = SkeuSectionHeaderWithAction(label, action, onAction, actionColor, modifier)

@Composable
fun SectionRow(
    label       : String,
    action      : String,
    onAction    : () -> Unit,
    actionColor : Color = Color.Unspecified,
    modifier    : Modifier = Modifier
) = SkeuSectionHeaderWithAction(label, action, onAction, actionColor, modifier)

// ============================================================================
// VAULT HEADER CARD — brass precision gauge + metal panel
// ============================================================================

@Composable
fun VaultHeaderCard(
    vaultBytes    : Long,
    deviceStorage : DeviceStorageInfo,
    appData       : AppData,
    modifier      : Modifier = Modifier
) {
    val t = LocalNCTheme.current

    val filledFraction = if (deviceStorage.totalBytes > 0)
        (vaultBytes.toFloat() / deviceStorage.totalBytes.toFloat()).coerceIn(0f, 1f)
    else 0f

    val arcAnim = remember { Animatable(0f) }
    LaunchedEffect(filledFraction) {
        arcAnim.animateTo(filledFraction, tween(1400, easing = FastOutSlowInEasing))
    }

    val needlePulse = rememberInfiniteTransition(label = "np")
    val shimmer by needlePulse.animateFloat(
        0f, 1f, infiniteRepeatable(tween(3000), RepeatMode.Restart), label = "sh"
    )

    val photoB = appData.files.filter { it.type == "photo" }.sumOf { it.size }.toFloat()
    val videoB = appData.files.filter { it.type == "video" }.sumOf { it.size }.toFloat()
    val audioB = appData.files.filter { it.type == "audio" }.sumOf { it.size }.toFloat()
    val docsB  = appData.files.filter { it.type == "file"  }.sumOf { it.size }.toFloat()
    val tot    = (photoB + videoB + audioB + docsB).coerceAtLeast(1f)

    val vaultUsedStr  = formatStorageSize(vaultBytes)
    val deviceFreeStr = formatStorageSize(deviceStorage.freeBytes)
    val deviceTotalStr= formatStorageSize(deviceStorage.totalBytes)

    // One UI Focus Block (26dp corner radius)
    Box(
        modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(26.dp), spotColor = Color.Black.copy(.08f))
            .background(t.Surface, RoundedCornerShape(26.dp))
            .border(bevelBorder(t.isDark), RoundedCornerShape(26.dp))
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Row 1: file count + precision gauge ──────────────────────────
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {

                // Left: count panel
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Stamped label
                    Text(
                        "Storage summary",
                        color         = t.TextMut,
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.Medium,
                        letterSpacing = 0.3.sp
                    )

                    // Inset count display — like an odometer
                    Box(
                        Modifier
                            .background(recessedBrush(t.isDark), RoundedCornerShape(8.dp))
                            .border(insetBorder(t.isDark), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        val animCount by animateIntAsState(appData.files.size, tween(900), label = "tc")
                        Row(
                            verticalAlignment     = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "$animCount",
                                color      = t.TextPri,
                                fontSize   = 46.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-2).sp,
                                lineHeight = 48.sp
                            )
                            Text(
                                if (appData.files.size == 1) "file" else "files",
                                color      = t.TextSec,
                                fontSize   = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier   = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

                    // Storage trio — raised pills
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SkeuStoragePill("Vault",  vaultUsedStr,   NC.Blue)
                        SkeuStoragePill("Free",   deviceFreeStr,  NC.Green)
                        SkeuStoragePill("Device", deviceTotalStr, t.TextSec)
                    }
                }

                // Right: precision analogue gauge
                Box(Modifier.size(106.dp), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.fillMaxSize()) {
                        val cx   = size.width / 2
                        val cy   = size.height / 2
                        val r    = size.minDimension / 2
                        val sw   = 8.dp.toPx()
                        val pad  = sw / 2 + 4.dp.toPx()
                        val rect = Rect(Offset(pad, pad), Size(size.width - pad * 2, size.height - pad * 2))

                        // Outer ring — iOS thin circular progress track
                        drawCircle(
                            Brush.sweepGradient(
                                listOf(Color(0xFF007AFF), Color(0xFF5AC8FA), Color(0xFF007AFF))
                            ), r
                        )
                        // Inner face — clean dark/light
                        drawCircle(
                            Brush.radialGradient(
                                listOf(Color(0xFF1C1C1E), Color(0xFF141416), Color(0xFF000000)),
                                Offset(cx, cy), r * .82f
                            ),
                            r * .82f
                        )

                        // Tick marks — like a real gauge
                        for (i in 0..20) {
                            val angle     = (-210f + i * 21f) * (PI / 180f).toFloat()
                            val isMajor   = i % 5 == 0
                            val innerR    = r * if (isMajor) .60f else .65f
                            val outerR    = r * .74f
                            val tickColor = if (i.toFloat() / 20f <= arcAnim.value)
                                Color(0xFF0A84FF).copy(.9f) else Color.White.copy(.12f)
                            drawLine(
                                tickColor,
                                Offset(cx + cos(angle) * innerR, cy + sin(angle) * innerR),
                                Offset(cx + cos(angle) * outerR,  cy + sin(angle) * outerR),
                                strokeWidth = if (isMajor) 2.2f else 1.2f,
                                cap = StrokeCap.Round
                            )
                        }

                        // Track arc — background
                        drawArc(
                            Color.Black.copy(.45f),
                            startAngle = 150f, sweepAngle = 240f, useCenter = false,
                            topLeft = rect.topLeft, size = rect.size,
                            style = Stroke(sw, cap = StrokeCap.Round)
                        )
                        // Filled arc — brass gradient
                        if (arcAnim.value > 0f) {
                            drawArc(
                                Brush.sweepGradient(
                                    listOf(Color(0xFF007AFF), Color(0xFF5AC8FA), Color(0xFF0A84FF)),
                                    rect.center
                                ),
                                startAngle = 150f,
                                sweepAngle = arcAnim.value * 240f,
                                useCenter  = false,
                                topLeft    = rect.topLeft,
                                size       = rect.size,
                                style      = Stroke(sw, cap = StrokeCap.Round)
                            )
                        }

                        // Needle
                        val needleAngle = (150f + arcAnim.value * 240f) * (PI / 180f).toFloat()
                        val needleR     = r * .56f
                        drawLine(
                            Brush.linearGradient(
                                listOf(Color.Transparent, Color(0xFF0A84FF), Color(0xFF007AFF)),
                                Offset(cx, cy),
                                Offset(cx + cos(needleAngle) * needleR, cy + sin(needleAngle) * needleR)
                            ),
                            Offset(cx, cy),
                            Offset(cx + cos(needleAngle) * needleR, cy + sin(needleAngle) * needleR),
                            strokeWidth = 2.5f,
                            cap = StrokeCap.Round
                        )
                        // Center pivot
                        drawCircle(
                            Brush.radialGradient(listOf(Color(0xFF0A84FF), Color(0xFF0055C1))),
                            r * .065f, Offset(cx, cy)
                        )

                        // Shimmer sweep
                        val shimA = (shimmer * 360f) * (PI / 180f).toFloat()
                        drawLine(
                            Color.White.copy(.06f),
                            Offset(cx, cy),
                            Offset(cx + cos(shimA) * r * .75f, cy + sin(shimA) * r * .75f),
                            strokeWidth = 2f
                        )
                    }
                    // Center label
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${(filledFraction * 100).toInt()}%",
                            color      = Color(0xFF0A84FF),
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            style      = TextStyle()
                        )
                        Text("USED", color = t.TextMut, fontSize = 7.sp, letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.Monospace)
                    }
                }
            }

            // ── Storage segmented bar ───────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    Modifier.fillMaxWidth().height(7.dp)
                        .shadow(2.dp, RoundedCornerShape(3.5.dp), spotColor = Color.Black.copy(.5f))
                        .background(recessedBrush(t.isDark), RoundedCornerShape(3.5.dp))
                        .border(insetBorder(t.isDark), RoundedCornerShape(3.5.dp))
                        .clip(RoundedCornerShape(3.5.dp))
                ) {
                    // Filled portion
                    Row(Modifier.fillMaxWidth(arcAnim.value).fillMaxHeight()) {
                        if (photoB > 0) Box(Modifier.weight(photoB / tot).fillMaxHeight()
                            .background(Brush.verticalGradient(listOf(NC.Cyan.copy(.9f), NC.Cyan.copy(.5f)))))
                        if (videoB > 0) Box(Modifier.weight(videoB / tot).fillMaxHeight()
                            .background(Brush.verticalGradient(listOf(NC.Violet.copy(.9f), NC.Violet.copy(.5f)))))
                        if (audioB > 0) Box(Modifier.weight(audioB / tot).fillMaxHeight()
                            .background(Brush.verticalGradient(listOf(NC.Green.copy(.9f), NC.Green.copy(.5f)))))
                        if (docsB  > 0) Box(Modifier.weight(docsB  / tot).fillMaxHeight()
                            .background(Brush.verticalGradient(listOf(NC.Amber.copy(.9f), NC.Amber.copy(.5f)))))
                    }
                }
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(14.dp)) {
                    listOf(NC.Cyan to "Photos", NC.Violet to "Videos", NC.Green to "Audio", NC.Amber to "Docs")
                        .forEach { (c, l) ->
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(Modifier.size(6.dp).background(c, CircleShape))
                                Text(l, color = t.TextMut, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                }
            }

            // ── Type count strip — raised segments ───────────────────────────
            Row(
                Modifier.fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.6f))
                    .background(raisedBrush(t.isDark), RoundedCornerShape(20.dp))
                    .border(bevelBorder(t.isDark), RoundedCornerShape(20.dp))
                    .padding(vertical = 14.dp),
                Arrangement.SpaceAround
            ) {
                listOf(
                    Triple(NC.Cyan,   Lucide.Image,    appData.files.count { it.type == "photo" }),
                    Triple(NC.Violet, Lucide.Video,    appData.files.count { it.type == "video" }),
                    Triple(NC.Green,  Lucide.Music,    appData.files.count { it.type == "audio" }),
                    Triple(NC.Amber,  Lucide.FileText, appData.files.count { it.type == "file"  })
                ).forEachIndexed { i, (color, icon, count) ->
                    if (i > 0) Box(
                        Modifier.width(1.dp).height(28.dp)
                            .background(Brush.verticalGradient(
                                listOf(Color.Black.copy(.4f), Color.White.copy(.06f), Color.Black.copy(.4f))
                            ))
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        // Inset icon well
                        Box(
                            Modifier.size(28.dp)
                                .background(recessedBrush(t.isDark), RoundedCornerShape(7.dp))
                                .border(insetBorder(t.isDark), RoundedCornerShape(7.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, null, tint = color, modifier = Modifier.size(13.dp))
                        }
                        val animC by animateIntAsState(count, tween(700), label = "hc$i")
                        Text(
                            "$animC",
                            color      = t.TextPri,
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkeuStoragePill(label: String, value: String, valueColor: Color) {
    val t = LocalNCTheme.current
    Box(
        Modifier
            .background(recessedBrush(t.isDark), RoundedCornerShape(6.dp))
            .border(insetBorder(t.isDark), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(label, color = t.TextMut, fontSize = 11.sp,
                letterSpacing = 0.sp)
            Text(value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ============================================================================
// GALLERY CATEGORY GRID — raised drawer tabs
// ============================================================================

@Composable
fun GalleryCategoryGrid(
    appData    : AppData,
    onNavigate : (GalleryType) -> Unit,
    modifier   : Modifier = Modifier
) {
    val types = GalleryType.entries
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        types.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { type ->
                    GalleryCategoryCard(type, appData, onNavigate, Modifier.weight(1f))
                }
                if (row.size < 2) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun GalleryCategoryCard(
    type       : GalleryType,
    appData    : AppData,
    onNavigate : (GalleryType) -> Unit,
    modifier   : Modifier = Modifier
) {
    val t     = LocalNCTheme.current
    val color = type.color()
    val count = when (type.filterKey) {
        "all"      -> appData.files.size
        "favorite" -> appData.files.count { it.fav }
        else       -> appData.files.count { it.type == type.filterKey }
    }

    val pressAnim = remember { Animatable(0f) }
    val scope     = rememberCoroutineScope()

    // Pressed state interpolates between raised and recessed
    val cardBg = Brush.verticalGradient(
        if (t.isDark)
            listOf(lerp(Color(0xFF2C2C2E), Color(0xFF1C1C1E), pressAnim.value),
                lerp(Color(0xFF1C1C1E), Color(0xFF141416), pressAnim.value))
        else
            listOf(lerp(Color(0xFFFFFFFF), Color(0xFFF2F2F7), pressAnim.value),
                lerp(Color(0xFFF9F9FB), Color(0xFFE5E5EA), pressAnim.value))
    )

    Row(
        modifier = modifier
            .height(68.dp)
            .shadow(
                elevation = 2.dp,
                shape     = RoundedCornerShape(26.dp),
                spotColor = Color.Black.copy(.06f)
            )
            .background(t.Surface, RoundedCornerShape(26.dp))
            .border(bevelBorder(t.isDark), RoundedCornerShape(26.dp))
            .clip(RoundedCornerShape(26.dp))
            .clickable {
                scope.launch {
                    pressAnim.animateTo(1f, tween(80))
                    pressAnim.animateTo(0f, spring(.5f))
                    onNavigate(type)
                }
            }
            .padding(horizontal = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // One UI Icon Pill Container
        Box(
            Modifier.size(38.dp)
                .background(color.copy(alpha = 0.12f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(type.icon, null, tint = color, modifier = Modifier.size(16.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                type.label,
                color      = t.TextPri,
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis,
                style      = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1f), 2f))
            )
            val animCount by animateIntAsState(count, tween(700), label = "gc")
            Text(
                "$animCount ${if (count == 1) "item" else "items"}",
                color      = t.TextMut,
                fontSize   = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        Icon(Lucide.ChevronRight, null, tint = color.copy(.35f), modifier = Modifier.size(15.dp))
    }
}

private fun lerp(a: Color, b: Color, t: Float) = Color(
    red   = a.red   + (b.red   - a.red)   * t,
    green = a.green + (b.green - a.green) * t,
    blue  = a.blue  + (b.blue  - a.blue)  * t,
    alpha = a.alpha + (b.alpha - a.alpha) * t
)

// ============================================================================
// RECENT FILE CARD — polaroid-style with embossed corner
// ============================================================================

@Composable
fun RecentFileCard(
    file        : VaultFile,
    onClick     : () -> Unit,
    onToggleFav : () -> Unit,
    onDelete    : () -> Unit
) {
    val t      = LocalNCTheme.current
    val accent = NC.accentFor(file.type)

    Box(
        Modifier
            .size(width = 124.dp, height = 154.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.08f))
            .background(t.Surface, RoundedCornerShape(20.dp))
            .border(bevelBorder(t.isDark), RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // Inner preview area
        Box(
            Modifier.fillMaxWidth().height(106.dp)
                .background(t.SurfaceHi, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            if (file.type == "photo") {
                val bmpState = rememberAsyncThumbnail(file, 240)
                val bmp = bmpState.value
                if (bmp != null) {
                    Image(bmp.asImageBitmap(), file.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Box(Modifier.fillMaxSize().background(recessedBrush(t.isDark)))
                    NeoIconBg(accent, NC.iconFor(file.type))
                }
            } else {
                NeoIconBg(accent, NC.iconFor(file.type))
            }

            // Subtle glass sheen on top
            Box(
                Modifier.fillMaxWidth().height(40.dp).align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(listOf(Color.White.copy(.12f), Color.Transparent))
                    )
            )
        }

        // Bottom label area — like a photo caption strip
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, bottom = 6.dp, top = 3.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                file.name,
                color      = t.TextPri,
                fontSize   = 9.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis,
                style      = TextStyle(shadow = Shadow(Color.Black.copy(.3f), Offset(0f, 1f), 2f))
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    formatStorageSize(file.size),
                    color      = t.TextMut,
                    fontSize   = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
                // Tiny type badge
                Box(
                    Modifier.background(recessedBrush(t.isDark), RoundedCornerShape(3.dp))
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(file.type.take(3).uppercase(), color = accent, fontSize = 7.sp,
                        fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
                }
            }
        }

        // Fav button — top right, engraved look
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(5.dp)
                .size(24.dp)
                .shadow(2.dp, CircleShape, spotColor = Color.Black.copy(.4f))
                .background(
                    if (file.fav) Brush.radialGradient(listOf(NC.Pink.copy(.4f), NC.Pink.copy(.1f)))
                    else raisedBrush(t.isDark),
                    CircleShape
                )
                .border(
                    if (file.fav) BorderStroke(1.dp, NC.Pink.copy(.5f))
                    else BorderStroke(1.dp, Brush.verticalGradient(
                        listOf(Color.White.copy(.15f), Color.Black.copy(.3f))
                    )),
                    CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null
                ) { onToggleFav() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Lucide.Heart, null,
                tint     = if (file.fav) NC.Pink else t.TextMut,
                modifier = Modifier.size(11.dp)
            )
        }
    }
}

// ============================================================================
// QUICK STATS ROW — instrument panel dials
// ============================================================================

@Composable
fun QuickStatsRow(
    appData       : AppData,
    deviceStorage : DeviceStorageInfo,
    modifier      : Modifier = Modifier
) {
    val t      = LocalNCTheme.current
    val favs   = appData.files.count { it.fav }
    val vaultMB = appData.files.sumOf { it.size } / 1024f / 1024f

    Box(
        modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(26.dp), spotColor = Color.Black.copy(.06f))
            .background(t.Surface, RoundedCornerShape(26.dp))
            .border(bevelBorder(t.isDark), RoundedCornerShape(26.dp))
    ) {
        Box(
            Modifier.fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround, Alignment.CenterVertically) {
                QuickStat(Lucide.Heart,       "$favs",                              "Favorites", NC.Pink)
                SkeuStatDivider()
                QuickStat(Lucide.HardDrive,   "%.1f MB".format(vaultMB),            "Vault",     NC.Blue)
                SkeuStatDivider()
                QuickStat(Lucide.Folder,      "${appData.files.size}",               "Files",     NC.Violet)
                SkeuStatDivider()
                QuickStat(Lucide.MemoryStick, formatStorageSize(deviceStorage.freeBytes), "Free", NC.Green)
            }
        }
    }
}

@Composable
private fun SkeuStatDivider() {
    Box(
        Modifier.width(1.dp).height(34.dp)
            .background(
                Brush.verticalGradient(listOf(Color.Black.copy(.4f), Color.White.copy(.06f), Color.Black.copy(.4f)))
            )
    )
}

@Composable
fun QuickStat(icon: ImageVector, value: String, label: String, color: Color) {
    val t = LocalNCTheme.current
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        // Inset dial face
        Box(
            Modifier.size(36.dp)
                .shadow(2.dp, CircleShape, spotColor = Color.Black.copy(.5f))
                .background(recessedBrush(t.isDark), CircleShape)
                .border(insetBorder(t.isDark), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(15.dp))
        }
        Text(
            value,
            color      = t.TextPri,
            fontSize   = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            maxLines   = 1,
            style      = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1f), 2f))
        )
        Text(label, color = t.TextMut, fontSize = 11.sp)
    }
}

// ============================================================================
// MUSIC QUICK ROW — cassette-style track cards
// ============================================================================

@Composable
fun MusicQuickRow(
    audioFiles   : List<VaultFile>,
    musicCtrl    : MusicPlayerController,
    onOpenPlayer : () -> Unit
) {
    val t       = LocalNCTheme.current
    val context = LocalContext.current

    LazyRow(
        contentPadding        = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(audioFiles.take(10), key = { it.id }) { file ->
            val isActive = musicCtrl.currentFile?.id == file.id

            Row(
                modifier = Modifier
                    .height(64.dp)
                    .width(200.dp)
                    .shadow(
                        elevation = if (isActive) 2.dp else 4.dp,
                        shape     = RoundedCornerShape(12.dp),
                        spotColor = Color.Black.copy(.5f)
                    )
                    .background(
                        if (isActive) recessedBrush(t.isDark)
                        else raisedBrush(t.isDark),
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        if (isActive) BorderStroke(1.dp, NC.Blue.copy(.25f))
                        else bevelBorder(t.isDark),
                        RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { if (isActive) onOpenPlayer() else musicCtrl.play(file, context) }
                    .padding(horizontal = 12.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Play button — raised circle
                Box(
                    Modifier.size(36.dp)
                        .shadow(if (isActive) 1.dp else 3.dp, CircleShape, spotColor = Color.Black.copy(.5f))
                        .background(
                            if (isActive) recessedBrush(t.isDark) else raisedBrush(t.isDark),
                            CircleShape
                        )
                        .border(
                            if (isActive) insetBorder(t.isDark) else bevelBorder(t.isDark),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isActive && musicCtrl.isLoading -> CircularProgressIndicator(
                            color = NC.Green, strokeWidth = 1.5.dp, modifier = Modifier.size(18.dp))
                        isActive && musicCtrl.isPlaying -> MiniEqBars(NC.Green)
                        else -> Icon(Lucide.Play, null,
                            tint     = if (isActive) NC.Green else t.TextSec,
                            modifier = Modifier.size(16.dp))
                    }
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        file.name.substringBeforeLast('.'),
                        color      = if (isActive) NC.Blue else t.TextPri,
                        fontSize   = 12.sp,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        style      = if (isActive) TextStyle(shadow = Shadow(NC.Blue.copy(.3f), Offset(0f, 1f), 4f))
                        else TextStyle()
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(formatStorageSize(file.size), color = t.TextMut, fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace)
                }
            }
        }

        if (audioFiles.size > 10) {
            item {
                Box(
                    Modifier.size(64.dp)
                        .shadow(3.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(.5f))
                        .background(raisedBrush(t.isDark), RoundedCornerShape(12.dp))
                        .border(bevelBorder(t.isDark), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenPlayer() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("+${audioFiles.size - 10}", color = NC.Blue, fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold)
                        Text("more", color = t.TextMut, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// ============================================================================
// MINI EQ BARS
// ============================================================================

@Composable
fun MiniEqBars(color: Color, barCount: Int = 3) {
    val inf = rememberInfiniteTransition(label = "eq")
    val h1 by inf.animateFloat(.3f, 1f, infiniteRepeatable(tween(380), RepeatMode.Reverse), label = "e1")
    val h2 by inf.animateFloat(.6f, 1f, infiniteRepeatable(tween(520), RepeatMode.Reverse), label = "e2")
    val h3 by inf.animateFloat(.4f, 1f, infiniteRepeatable(tween(460), RepeatMode.Reverse), label = "e3")
    Row(Modifier.size(14.dp, 14.dp), Arrangement.spacedBy(2.dp), Alignment.Bottom) {
        listOf(h1, h2, h3).take(barCount).forEach { h ->
            Box(Modifier.width(3.dp).fillMaxHeight(h)
                .background(Brush.verticalGradient(listOf(color, color.copy(.5f))), RoundedCornerShape(1.5.dp)))
        }
    }
}

// ============================================================================
// ICON BACKGROUND — recessed icon well
// ============================================================================

@Composable
fun NeoIconBg(color: Color, icon: ImageVector) {
    val t = LocalNCTheme.current
    Box(
        Modifier.fillMaxSize().background(
            Brush.radialGradient(listOf(color.copy(.08f), t.Surface.copy(0f)))
        ).background(recessedBrush(t.isDark)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = color.copy(.25f), modifier = Modifier.size(42.dp))
    }
}

// ============================================================================
// MUSIC FAB — brass turntable knob
// ============================================================================

@Composable
fun MusicFAB(ctrl: MusicPlayerController, onOpenPlayer: () -> Unit, modifier: Modifier = Modifier) {
    val t   = LocalNCTheme.current
    val inf = rememberInfiniteTransition(label = "fab")
    val rot by inf.animateFloat(0f, 360f,
        infiniteRepeatable(tween(3200, easing = LinearEasing)), label = "rot")

    AnimatedVisibility(
        visible  = ctrl.currentFile != null || ctrl.isLoading,
        modifier = modifier,
        enter    = scaleIn(spring()) + fadeIn(),
        exit     = scaleOut(spring()) + fadeOut()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .size(54.dp)
                    .shadow(
                        elevation    = if (ctrl.isPlaying) 10.dp else 4.dp,
                        shape        = CircleShape,
                        spotColor    = Color.Black.copy(.7f),
                        ambientColor = Color.Black.copy(.3f)
                    )
                    .background(
                        if (ctrl.isPlaying)
                            Brush.verticalGradient(listOf(Color(0xFF0055C1), Color(0xFF003A8A)))
                        else
                            raisedBrush(t.isDark),
                        CircleShape
                    )
                    .border(
                        if (ctrl.isPlaying)
                            BorderStroke(1.dp, NC.Blue.copy(.3f))
                        else bevelBorder(t.isDark),
                        CircleShape
                    )
                    .clickable { onOpenPlayer() },
                contentAlignment = Alignment.Center
            ) {
                when {
                    ctrl.isLoading -> CircularProgressIndicator(
                        color = NC.Blue, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                    ctrl.isPlaying -> Canvas(Modifier.size(38.dp).rotate(rot)) {
                        val c = Offset(size.width / 2, size.height / 2)
                        val r = size.minDimension / 2
                        // Vinyl record
                        drawCircle(Color(0xFF0A0804))
                        // Grooves
                        for (i in 2..7) {
                            drawCircle(Color.White.copy(.03f + i * .004f),
                                r * (.2f + i * .1f), c, style = Stroke(0.8f))
                        }
                        // iOS blue label
                        drawCircle(
                            Brush.radialGradient(listOf(Color(0xFF0A84FF), Color(0xFF007AFF)), c, r * .32f),
                            r * .32f, c
                        )
                        // Center hole
                        drawCircle(Color(0xFF060402), r * .048f, c)
                        // Sheen
                        drawCircle(
                            Brush.linearGradient(
                                listOf(Color.White.copy(.12f), Color.Transparent),
                                Offset(c.x - r * .4f, c.y - r * .4f),
                                Offset(c.x + r * .1f, c.y + r * .1f)
                            ), r
                        )
                    }
                    else -> Icon(Lucide.Music, null, tint = t.TextSec, modifier = Modifier.size(22.dp))
                }
            }

            ctrl.currentFile?.let { f ->
                Spacer(Modifier.height(4.dp))
                val name = f.name.substringBeforeLast('.').let {
                    if (it.length > 11) it.take(11) + "…" else it
                }
                Box(
                    Modifier
                        .shadow(2.dp, RoundedCornerShape(6.dp), spotColor = Color.Black.copy(.4f))
                        .background(raisedBrush(t.isDark), RoundedCornerShape(6.dp))
                        .border(bevelBorder(t.isDark), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(name, color = if (ctrl.isPlaying) NC.Blue else t.TextMut,
                        fontSize = 9.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ============================================================================
// MINI MUSIC PLAYER — hi-fi deck strip
// ============================================================================

@Composable
fun MiniMusicPlayer(ctrl: MusicPlayerController, onExpand: () -> Unit, context: Context) {
    val t     = LocalNCTheme.current
    val title = ctrl.currentFile?.name?.substringBeforeLast('.') ?: ""

    var swipeOff by remember { mutableFloatStateOf(0f) }
    val swAnim   by animateFloatAsState(swipeOff, spring(), label = "sw")
    val swAlpha  = (1f - abs(swipeOff) / 280f).coerceIn(0f, 1f)
    val scope    = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .offset { IntOffset(swAnim.toInt(), 0) }
            .alpha(swAlpha)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd    = { if (abs(swipeOff) > 180f) ctrl.stop(); scope.launch { swipeOff = 0f } },
                    onDragCancel = { scope.launch { swipeOff = 0f } },
                    onHorizontalDrag = { _, d -> swipeOff += d }
                )
            }
    ) {
        // One UI Floating Pill Player
        Box(
            Modifier.fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(26.dp), spotColor = Color.Black.copy(.15f))
                .background(t.Surface, RoundedCornerShape(26.dp))
                .border(bevelBorder(t.isDark), RoundedCornerShape(26.dp))
        ) {
            Column(
                Modifier.fillMaxWidth()
                    .clickable { onExpand() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Row(
                        Modifier.weight(1f),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // VU meter style box
                        Box(
                            Modifier.size(30.dp)
                                .shadow(2.dp, RoundedCornerShape(7.dp), spotColor = Color.Black.copy(.5f))
                                .background(raisedBrush(t.isDark), RoundedCornerShape(7.dp))
                                .border(bevelBorder(t.isDark), RoundedCornerShape(7.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (ctrl.isPlaying) MiniEqBars(NC.Green)
                            else Icon(Lucide.Music, null, tint = NC.Blue, modifier = Modifier.size(13.dp))
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                title,
                                color      = t.TextPri,
                                fontSize   = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines   = 1,
                                overflow   = TextOverflow.Ellipsis,
                                style      = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1f), 2f))
                            )
                            Text(
                                "${ctrl.formatMs(ctrl.position)} / ${ctrl.formatMs(ctrl.duration)}",
                                color      = t.TextMut,
                                fontSize   = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Transport controls — raised buttons
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        SkeuTransportBtn(size = 30.dp, onClick = { ctrl.playPrev(context) }) {
                            Icon(Lucide.SkipBack, null, tint = t.TextSec, modifier = Modifier.size(14.dp))
                        }
                        SkeuTransportBtn(size = 34.dp, isPrimary = true, onClick = { ctrl.togglePlayPause() }) {
                            Icon(
                                if (ctrl.isPlaying) Lucide.Pause else Lucide.Play,
                                null, tint = if (t.isDark) Color.Black else Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        SkeuTransportBtn(size = 30.dp, onClick = { ctrl.playNext(context) }) {
                            Icon(Lucide.SkipForward, null, tint = t.TextSec, modifier = Modifier.size(14.dp))
                        }
                    }
                }

                // LED progress track
                Box(
                    Modifier.fillMaxWidth().height(4.dp)
                        .background(recessedBrush(t.isDark), RoundedCornerShape(2.dp))
                        .border(insetBorder(t.isDark), RoundedCornerShape(2.dp))
                        .clip(RoundedCornerShape(2.dp))
                ) {
                    Box(
                        Modifier.fillMaxWidth(ctrl.progress.coerceIn(0f, 1f)).fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(listOf(NC.Blue, NC.Cyan)),
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

// ============================================================================
// SKEU TRANSPORT BUTTON — physical press feel
// ============================================================================

@Composable
private fun SkeuTransportBtn(
    size      : Dp = 32.dp,
    isPrimary : Boolean = false,
    onClick   : () -> Unit,
    content   : @Composable BoxScope.() -> Unit
) {
    val t     = LocalNCTheme.current
    val press = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val bgColor = if (isPrimary)
        Brush.verticalGradient(listOf(
            lerp(Color(0xFF0A84FF), Color(0xFF007AFF), press.value),
            lerp(Color(0xFF007AFF), Color(0xFF0055C1), press.value)
        ))
    else
        Brush.verticalGradient(listOf(
            lerp(if (t.isDark) Color(0xFF2C2C2E) else Color(0xFFFFFFFF),
                if (t.isDark) Color(0xFF1C1C1E) else Color(0xFFF2F2F7), press.value),
            lerp(if (t.isDark) Color(0xFF1C1C1E) else Color(0xFFF2F2F7),
                if (t.isDark) Color(0xFF141416) else Color(0xFFE5E5EA), press.value)
        ))

    val borderColor = if (isPrimary) Color(0xFF0A84FF).copy(.35f)
    else if (t.isDark) Color(0xFF48484A) else Color(0xFFC6C6C8)

    Box(
        Modifier
            .size(size)
            .shadow(((1f - press.value) * 4f).dp + 1.dp, CircleShape, spotColor = Color.Black.copy(.3f))
            .background(bgColor, CircleShape)
            .border(BorderStroke(0.5.dp, borderColor), CircleShape)
            .clickable {
                scope.launch { press.animateTo(1f, tween(70)); press.animateTo(0f, spring(.5f)) }
                onClick()
            },
        contentAlignment = Alignment.Center,
        content = content
    )
}

// ============================================================================
// MUSIC PLAYER BOTTOM SHEET — hi-fi console
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerSheet(
    ctrl       : MusicPlayerController,
    audioFiles : List<VaultFile>,
    context    : Context,
    onDismiss  : () -> Unit
) {
    val t = LocalNCTheme.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape            = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        containerColor   = t.Surface,
        dragHandle = {
            Box(
                Modifier.padding(top = 12.dp, bottom = 8.dp)
                    .width(36.dp).height(4.dp)
                    .background(t.Border, RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(.92f).padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            if (ctrl.currentFile != null || ctrl.isLoading) {
                if (ctrl.isLoading) {
                    Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            CircularProgressIndicator(color = NC.Blue, strokeWidth = 2.dp)
                            Text("Loading audio…", color = t.TextSec, fontSize = 13.sp)
                        }
                    }
                } else {
                    FullNowPlaying(ctrl, context)
                }
                Spacer(Modifier.height(20.dp))
                // Engraved divider
                Box(Modifier.fillMaxWidth().height(2.dp)
                    .background(recessedBrush(t.isDark))
                    .border(insetBorder(t.isDark), RoundedCornerShape(1.dp)))
                Spacer(Modifier.height(16.dp))
            }

            Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("Queue", color = t.TextPri, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                    style = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1f), 2f)))
                Box(
                    Modifier
                        .background(recessedBrush(t.isDark), RoundedCornerShape(6.dp))
                        .border(insetBorder(t.isDark), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("${audioFiles.size} tracks", color = NC.Blue, fontSize = 10.sp)
                }
            }

            if (audioFiles.isEmpty()) {
                Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.size(54.dp)
                            .background(recessedBrush(t.isDark), CircleShape)
                            .border(insetBorder(t.isDark), CircleShape),
                            contentAlignment = Alignment.Center) {
                            Icon(Lucide.VolumeX, null, tint = t.TextMut, modifier = Modifier.size(24.dp))
                        }
                        Text("No audio files yet", color = t.TextSec, fontSize = 13.sp)
                        Text("Import audio from the gallery", color = t.TextMut, fontSize = 11.sp)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(audioFiles, key = { it.id }) { file ->
                        val isActive = ctrl.currentFile?.id == file.id
                        Box(
                            Modifier.fillMaxWidth()
                                .shadow(if (isActive) 1.dp else 3.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(.5f))
                                .background(
                                    if (isActive) recessedBrush(t.isDark) else raisedBrush(t.isDark),
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    if (isActive) BorderStroke(1.dp, NC.Blue.copy(.25f))
                                    else bevelBorder(t.isDark),
                                    RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { ctrl.play(file, context) }
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    Modifier.size(40.dp)
                                        .shadow(if (isActive) 1.dp else 2.dp, CircleShape)
                                        .background(
                                            if (isActive) recessedBrush(t.isDark) else raisedBrush(t.isDark),
                                            CircleShape
                                        )
                                        .border(
                                            if (isActive) insetBorder(t.isDark) else bevelBorder(t.isDark),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when {
                                        isActive && ctrl.isLoading -> CircularProgressIndicator(
                                            color = NC.Blue, strokeWidth = 1.5.dp, modifier = Modifier.size(18.dp))
                                        isActive && ctrl.isPlaying -> MiniEqBars(NC.Green)
                                        else -> Icon(Lucide.Play, null,
                                            tint = if (isActive) NC.Blue else t.TextMut,
                                            modifier = Modifier.size(18.dp))
                                    }
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        file.name.substringBeforeLast('.'),
                                        color      = if (isActive) NC.Blue else t.TextPri,
                                        fontSize   = 13.sp,
                                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                        maxLines   = 1, overflow = TextOverflow.Ellipsis,
                                        style      = if (isActive) TextStyle(shadow = Shadow(NC.Blue.copy(.3f), Offset(0f, 1f), 4f))
                                        else TextStyle()
                                    )
                                    Text("${formatStorageSize(file.size)}  ·  ${file.date}",
                                        color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}

// ============================================================================
// FULL NOW PLAYING — classic vinyl turntable
// ============================================================================

@Composable
fun FullNowPlaying(ctrl: MusicPlayerController, context: Context) {
    val t        = LocalNCTheme.current
    val vinylInf = rememberInfiniteTransition(label = "vinyl")
    val rotation by vinylInf.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(5000, easing = LinearEasing)), label = "spin"
    )

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column {
                Text("NOW PLAYING", color = NC.Blue, fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
                Text(ctrl.currentFile?.name?.substringBeforeLast('.') ?: "",
                    color = t.TextPri, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    style = TextStyle(shadow = Shadow(Color.Black.copy(.5f), Offset(0f, 1.5f), 4f)))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeuToggleBtn(
                    active      = ctrl.shuffle,
                    activeColor = NC.Cyan,
                    onClick     = { ctrl.shuffle = !ctrl.shuffle }
                ) {
                    Icon(Lucide.Shuffle, null, modifier = Modifier.size(14.dp),
                        tint = if (ctrl.shuffle) NC.Cyan else t.TextMut)
                }
                SkeuToggleBtn(
                    active      = ctrl.repeatMode != RepeatModeEnum.OFF,
                    activeColor = when (ctrl.repeatMode) { RepeatModeEnum.ONE -> NC.Violet; else -> NC.Cyan },
                    onClick     = {
                        ctrl.repeatMode = when (ctrl.repeatMode) {
                            RepeatModeEnum.OFF -> RepeatModeEnum.ONE
                            RepeatModeEnum.ONE -> RepeatModeEnum.ALL
                            RepeatModeEnum.ALL -> RepeatModeEnum.OFF
                        }
                    }
                ) {
                    Icon(
                        when (ctrl.repeatMode) { RepeatModeEnum.ONE -> Lucide.Repeat1; else -> Lucide.Repeat2 },
                        null, modifier = Modifier.size(14.dp),
                        tint = when (ctrl.repeatMode) {
                            RepeatModeEnum.OFF -> t.TextMut; RepeatModeEnum.ONE -> NC.Violet; else -> NC.Cyan
                        }
                    )
                }
            }
        }

        // Turntable platter
        Box(Modifier.size(168.dp), contentAlignment = Alignment.Center) {
            // Plinth base — raised rounded square
            Box(
                Modifier.size(168.dp)
                    .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.8f))
                    .background(metalBrush(t.isDark), RoundedCornerShape(20.dp))
                    .border(bevelBorder(t.isDark), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Inset platter well
                Box(
                    Modifier.size(148.dp)
                        .background(recessedBrush(t.isDark), CircleShape)
                        .border(insetBorder(t.isDark), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Spinning vinyl disc
                    Canvas(
                        Modifier.size(132.dp)
                            .rotate(if (ctrl.isPlaying) rotation else 0f)
                    ) {
                        val c  = Offset(size.width / 2, size.height / 2)
                        val r  = size.minDimension / 2
                        // Record body
                        drawCircle(Color(0xFF0A0804))
                        // Grooves
                        for (i in 1..8) {
                            drawCircle(Color.White.copy(.025f + i * .003f),
                                r * (.28f + i * .082f), c, style = Stroke(.9f))
                        }
                        // iOS blue label center
                        drawCircle(
                            Brush.radialGradient(
                                listOf(Color(0xFF1C1C1E), Color(0xFF0A84FF).copy(.4f), Color(0xFF007AFF)),
                                c, r * .3f
                            ), r * .3f, c
                        )
                        // Ring accent
                        drawCircle(Color(0xFF0A84FF).copy(.25f), r * .28f, c, style = Stroke(.8f))
                        // Center dot
                        drawCircle(
                            Brush.radialGradient(listOf(Color(0xFF5AC8FA), Color(0xFF007AFF))),
                            r * .1f, c
                        )
                        // Spindle hole
                        drawCircle(Color(0xFF060402), r * .032f, c)
                        // Sheen
                        drawCircle(
                            Brush.linearGradient(
                                listOf(Color.White.copy(.1f), Color.Transparent),
                                Offset(c.x - r * .5f, c.y - r * .5f),
                                Offset(c.x + r * .1f, c.y + r * .1f)
                            ), r
                        )
                    }
                }
            }
        }

        // Seek slider in inset track
        Column(Modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth().height(14.dp)
                .shadow(2.dp, RoundedCornerShape(7.dp), spotColor = Color.Black.copy(.5f))
                .background(recessedBrush(t.isDark), RoundedCornerShape(7.dp))
                .border(insetBorder(t.isDark), RoundedCornerShape(7.dp))
                .clip(RoundedCornerShape(7.dp))
                .padding(horizontal = 4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Slider(
                    value         = ctrl.progress.coerceIn(0f, 1f),
                    onValueChange = { ctrl.seekTo(it) },
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = SliderDefaults.colors(
                        thumbColor         = NC.Blue,
                        activeTrackColor   = NC.Blue,
                        inactiveTrackColor = Color.Transparent
                    )
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp), Arrangement.SpaceBetween) {
                Text(ctrl.formatMs(ctrl.position), color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Text(ctrl.formatMs(ctrl.duration),  color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
        }

        // Transport controls — physical brass buttons
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
            SkeuTransportBtn(size = 50.dp, onClick = { ctrl.playPrev(context) }) {
                Icon(Lucide.SkipBack, null, tint = t.TextSec, modifier = Modifier.size(22.dp))
            }
            SkeuTransportBtn(size = 66.dp, isPrimary = true, onClick = { ctrl.togglePlayPause() }) {
                Icon(
                    if (ctrl.isPlaying) Lucide.Pause else Lucide.Play,
                    null, tint = Color.Black, modifier = Modifier.size(30.dp)
                )
            }
            SkeuTransportBtn(size = 50.dp, onClick = { ctrl.playNext(context) }) {
                Icon(Lucide.SkipForward, null, tint = t.TextSec, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
private fun SkeuToggleBtn(
    active      : Boolean,
    activeColor : Color,
    onClick     : () -> Unit,
    content     : @Composable () -> Unit
) {
    val t     = LocalNCTheme.current
    val press = remember { Animatable(if (active) 1f else 0f) }
    LaunchedEffect(active) { press.animateTo(if (active) 1f else 0f, tween(150)) }

    Box(
        Modifier.size(30.dp)
            .shadow(((1f - press.value) * 3f + 1f).dp, CircleShape, spotColor = Color.Black.copy(.5f))
            .background(
                Brush.verticalGradient(
                    if (t.isDark)
                        listOf(if (active) activeColor.copy(.18f) else Color(0xFF2C2C2E),
                            if (active) activeColor.copy(.08f) else Color(0xFF1C1C1E))
                    else
                        listOf(if (active) activeColor.copy(.12f) else Color(0xFFFFFFFF),
                            if (active) activeColor.copy(.06f) else Color(0xFFF2F2F7))
                ),
                CircleShape
            )
            .border(
                BorderStroke(0.5.dp,
                    if (active) activeColor.copy(.4f)
                    else if (t.isDark) Color(0xFF38383A) else Color(0xFFC6C6C8)
                ),
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { content() }
}

// ============================================================================
// GALLERY SCREEN
// ============================================================================

@Composable
fun GalleryScreen(
    appData        : AppData,
    onSaveData     : (AppData) -> Unit,
    onShowSnackbar : (String) -> Unit,
    galleryType    : GalleryType,
    musicCtrl      : MusicPlayerController,
    onBack         : () -> Unit,
    onFileClick    : (VaultFile) -> Unit,
    onOpenMusic    : () -> Unit
) {
    val t              = LocalNCTheme.current
    var searchQuery    by remember { mutableStateOf("") }
    var sortMode       by remember { mutableStateOf("date") }
    var viewMode       by remember { mutableStateOf("grid") }
    var searchExpanded by remember { mutableStateOf(false) }
    var showSortMenu   by remember { mutableStateOf(false) }
    var showImport     by remember { mutableStateOf(false) }
    val color          = galleryType.color()
    val context        = LocalContext.current
    val scope          = rememberCoroutineScope()

    val files = remember(appData.files, searchQuery, sortMode, galleryType) {
        appData.files
            .filter { f ->
                val matchType = when (galleryType.filterKey) {
                    "all"      -> true
                    "favorite" -> f.fav
                    else       -> f.type == galleryType.filterKey
                }
                val matchSearch = searchQuery.isBlank() ||
                        f.name.lowercase().contains(searchQuery.lowercase())
                matchType && matchSearch
            }
            .let { list ->
                when (sortMode) {
                    "name" -> list.sortedBy { it.name.lowercase() }
                    "size" -> list.sortedByDescending { it.size }
                    "type" -> list.sortedBy { it.type }
                    else   -> list.sortedByDescending { it.date }
                }
            }
    }

    var isImporting    by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importLabel    by remember { mutableStateOf("Importing…") }
    var importTotal    by remember { mutableIntStateOf(0) }
    var importDone     by remember { mutableIntStateOf(0) }

    fun launchMultiImport(uris: List<Uri>, label: String) {
        if (uris.isEmpty()) return
        isImporting = true; importTotal = uris.size; importDone = 0
        importLabel = label; importProgress = 0f
        scope.launch(Dispatchers.Main) {
            uris.forEachIndexed { idx, uri ->
                importLabel = if (uris.size > 1) "$label (${idx + 1}/${uris.size})" else label
                handleOptimizedFileImport(
                    context, uri, appData, onSaveData, onShowSnackbar,
                    onProgress = { p -> importProgress = (idx + p) / uris.size.toFloat() }
                )
                importDone = idx + 1
            }
            isImporting = false; importProgress = 0f; showImport = false
            if (uris.size > 1) onShowSnackbar("✓ Imported ${uris.size} files")
        }
    }

    val photoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        launchMultiImport(uris, "Importing Images…")
    }
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        launchMultiImport(uris, "Importing Videos…")
    }
    val audioLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        launchMultiImport(uris, "Importing Audio…")
    }
    val docLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        launchMultiImport(uris, "Importing Documents…")
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().background(t.Bg)) {
            GalleryTopBar(
                galleryType      = galleryType,
                color            = color,
                fileCount        = files.size,
                searchExpanded   = searchExpanded,
                onSearchToggle   = { searchExpanded = !searchExpanded; if (!searchExpanded) searchQuery = "" },
                sortMode         = sortMode,
                showSortMenu     = showSortMenu,
                onSortMenuToggle = { showSortMenu = !showSortMenu },
                onSortSelect     = { sortMode = it; showSortMenu = false },
                viewMode         = viewMode,
                onViewToggle     = { viewMode = if (viewMode == "grid") "list" else "grid" },
                onBack           = onBack
            )

            AnimatedVisibility(
                visible = searchExpanded,
                enter   = expandVertically() + fadeIn(),
                exit    = shrinkVertically() + fadeOut()
            ) {
                Box(
                    Modifier.fillMaxWidth()
                        .background(metalBrush(t.isDark))
                        .border(BorderStroke(1.dp, Brush.verticalGradient(
                            listOf(Color.Transparent, t.Border.copy(.4f))
                        )))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    GallerySearchBar(searchQuery, { searchQuery = it }, { searchQuery = "" })
                }
            }

            // Engraved separator
            Box(Modifier.fillMaxWidth().height(2.dp)
                .background(recessedBrush(t.isDark))
                .border(insetBorder(t.isDark), RoundedCornerShape(0.dp)))

            if (files.isEmpty()) GalleryEmptyState(galleryType, searchQuery)
            else if (viewMode == "grid") GalleryGridView(files, appData, onSaveData, onShowSnackbar, onFileClick)
            else GalleryListView(files, appData, onSaveData, onShowSnackbar, onFileClick)
        }

        // Import FAB — brass rivet button
        Box(
            Modifier.align(Alignment.BottomEnd).padding(bottom = 100.dp, end = 18.dp)
        ) {
            val press = remember { Animatable(0f) }
            val scope2 = rememberCoroutineScope()
            Box(
                Modifier
                    .size(54.dp)
                    .shadow(
                        if (showImport) 2.dp else 8.dp,
                        CircleShape,
                        spotColor = Color.Black.copy(.6f)
                    )
                    .background(
                        if (showImport) recessedBrush(t.isDark)
                        else Brush.verticalGradient(listOf(if (t.isDark) Color(0xFF2C2C2E) else Color(0xFFFFFFFF), if (t.isDark) Color(0xFF1C1C1E) else Color(0xFFF2F2F7))),
                        CircleShape
                    )
                    .border(
                        if (showImport) insetBorder(t.isDark)
                        else BorderStroke(1.dp, Brush.verticalGradient(
                            listOf(NC.Blue.copy(.5f), NC.BlueDark.copy(.3f))
                        )),
                        CircleShape
                    )
                    .clickable {
                        scope2.launch {
                            press.animateTo(1f, tween(80))
                            press.animateTo(0f, spring(.5f))
                        }
                        showImport = !showImport
                    },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(showImport, label = "fab_icon") { open ->
                    Icon(
                        if (open) Lucide.X else Lucide.Plus,
                        null,
                        tint     = if (open) t.TextSec else NC.Blue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }

    if (showImport) {
        GalleryImportSheet(
            onDismiss     = { showImport = false },
            onImportPhoto = {
                photoLauncher.launch(
                    androidx.activity.result.PickVisualMediaRequest(
                        androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onImportVideo = {
                videoLauncher.launch(
                    androidx.activity.result.PickVisualMediaRequest(
                        androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
                    )
                )
            },
            onImportAudio = { audioLauncher.launch(arrayOf("audio/*")) },
            onImportDoc   = { docLauncher.launch(arrayOf("*/*")) }
        )
    }

    if (isImporting) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            val t2 = LocalNCTheme.current
            Box(
                Modifier.fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(.7f))
                    .background(metalBrush(t2.isDark), RoundedCornerShape(22.dp))
                    .border(bevelBorder(t2.isDark), RoundedCornerShape(22.dp))
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(3.dp)
                        .background(recessedBrush(t2.isDark), RoundedCornerShape(19.dp))
                        .border(insetBorder(t2.isDark), RoundedCornerShape(19.dp))
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Gauge-style circular progress
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
                        Canvas(Modifier.size(72.dp)) {
                            val r = size.minDimension / 2
                            // Bezel
                            drawCircle(Brush.sweepGradient(listOf(Color(0xFF007AFF), Color(0xFF5AC8FA), Color(0xFF007AFF))))
                            // Face
                            drawCircle(recessedBrush(t2.isDark), r * .84f)
                            // Track
                            drawArc(Color.Black.copy(.4f), 0f, 360f, false,
                                Offset(r * .16f, r * .16f).let { Rect(it, Size(r * 1.68f, r * 1.68f)) }.let { it.topLeft },
                                Size(r * 1.68f, r * 1.68f),
                                style = Stroke(r * .12f, cap = StrokeCap.Round))
                            // Fill arc
                            drawArc(
                                Brush.sweepGradient(listOf(Color(0xFF007AFF), Color(0xFF5AC8FA), Color(0xFF0A84FF))),
                                -90f, importProgress.coerceIn(0f, 1f) * 360f, false,
                                Rect(Offset(r * .16f, r * .16f), Size(r * 1.68f, r * 1.68f)).topLeft,
                                Size(r * 1.68f, r * 1.68f),
                                style = Stroke(r * .12f, cap = StrokeCap.Round)
                            )
                        }
                        if (importTotal > 1) {
                            Text("$importDone/$importTotal", color = NC.Blue,
                                fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(importLabel, color = t2.TextPri, fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                    // LED progress bar
                    Box(Modifier.fillMaxWidth().height(6.dp)
                        .shadow(1.dp, RoundedCornerShape(3.dp))
                        .background(recessedBrush(t2.isDark), RoundedCornerShape(3.dp))
                        .border(insetBorder(t2.isDark), RoundedCornerShape(3.dp))
                        .clip(RoundedCornerShape(3.dp))
                    ) {
                        Box(Modifier.fillMaxWidth(importProgress.coerceIn(0f, 1f)).fillMaxHeight()
                            .background(Brush.horizontalGradient(listOf(Color(0xFF007AFF), Color(0xFF5AC8FA))),
                                RoundedCornerShape(3.dp)))
                    }
                    Text("${(importProgress * 100).toInt()}%", color = NC.Blue,
                        fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ============================================================================
// GALLERY IMPORT SHEET — engraved selection panel
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryImportSheet(
    onDismiss     : () -> Unit,
    onImportPhoto : () -> Unit,
    onImportVideo : () -> Unit,
    onImportAudio : () -> Unit,
    onImportDoc   : () -> Unit
) {
    val t = LocalNCTheme.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape            = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        containerColor   = t.Surface,
        dragHandle = {
            Box(
                Modifier.padding(top = 12.dp, bottom = 8.dp)
                    .width(36.dp).height(4.dp)
                    .background(t.Border, RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp).padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(Modifier.padding(bottom = 4.dp)) {
                Text("Import files", color = NC.Blue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(3.dp))
                Text("Select files to secure", color = t.TextPri, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    style = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1.5f), 4f)))
            }

            val opts = listOf(
                Triple(Lucide.Image,    "Photos",    NC.Cyan)   to onImportPhoto,
                Triple(Lucide.Video,   "Videos",    NC.Violet) to onImportVideo,
                Triple(Lucide.Music,   "Audio",     NC.Green)  to onImportAudio,
                Triple(Lucide.FileText,"Documents", NC.Amber)  to onImportDoc
            )
            opts.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { (meta, action) ->
                        val (icon, label, accent) = meta
                        val press = remember { Animatable(0f) }
                        val scope = rememberCoroutineScope()
                        Row(
                            modifier = Modifier.weight(1f).height(64.dp)
                                .shadow(((1f - press.value) * 5f + 1f).dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.5f))
                                .background(
                                    Brush.verticalGradient(
                                        if (t.isDark)
                                            listOf(lerp(Color(0xFF2C2C2E), Color(0xFF1C1C1E), press.value),
                                                lerp(Color(0xFF1C1C1E), Color(0xFF141416), press.value))
                                        else
                                            listOf(lerp(Color(0xFFFFFFFF), Color(0xFFF2F2F7), press.value),
                                                lerp(Color(0xFFF9F9FB), Color(0xFFE5E5EA), press.value))
                                    ),
                                    RoundedCornerShape(20.dp)
                                )
                                .border(BorderStroke(0.5.dp,
                                    if (t.isDark) Color(0xFF38383A) else Color(0xFFC6C6C8)
                                ), RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    scope.launch {
                                        press.animateTo(1f, tween(80))
                                        press.animateTo(0f, spring(.5f))
                                    }
                                    action(); onDismiss()
                                }
                                .padding(horizontal = 14.dp),
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                Modifier.size(32.dp)
                                    .shadow(1.dp, CircleShape)
                                    .background(recessedBrush(t.isDark), CircleShape)
                                    .border(insetBorder(t.isDark), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, null, tint = accent, modifier = Modifier.size(15.dp))
                            }
                            Text(label, color = t.TextPri, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    if (row.size < 2) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

// ============================================================================
// GALLERY TOP BAR — brushed metal header
// ============================================================================

@Composable
fun GalleryTopBar(
    galleryType      : GalleryType,
    color            : Color,
    fileCount        : Int,
    searchExpanded   : Boolean,
    onSearchToggle   : () -> Unit,
    sortMode         : String,
    showSortMenu     : Boolean,
    onSortMenuToggle : () -> Unit,
    onSortSelect     : (String) -> Unit,
    viewMode         : String,
    onViewToggle     : () -> Unit,
    onBack           : () -> Unit
) {
    val t = LocalNCTheme.current
    Box(
        Modifier.fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(0.dp), spotColor = Color.Black.copy(.5f))
            .background(metalBrush(t.isDark))
            .border(BorderStroke(1.dp, Brush.verticalGradient(
                listOf(Color.Transparent, t.Border.copy(.4f))
            )))
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            TopBarIconBtn(Lucide.ArrowLeft, t.TextSec, Color.Transparent) { onBack() }

            Column(Modifier.weight(1f)) {
                Text("VAULT", color = t.TextMut, fontSize = 10.sp,
                    fontWeight = FontWeight.Medium, letterSpacing = 0.3.sp)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(galleryType.label, color = t.TextPri, fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp,
                        style = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1f), 3f)))
                    Box(
                        Modifier.background(recessedBrush(t.isDark), RoundedCornerShape(5.dp))
                            .border(insetBorder(t.isDark), RoundedCornerShape(5.dp))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        AnimatedContent(fileCount, label = "gcount") { cnt ->
                            Text("$cnt", color = color, fontSize = 10.sp,
                                fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            TopBarIconBtn(
                icon    = if (searchExpanded) Lucide.SearchX else Lucide.Search,
                tint    = if (searchExpanded) color else t.TextSec,
                bgColor = Color.Transparent,
                onClick = onSearchToggle
            )

            Box {
                TopBarIconBtn(
                    icon    = Lucide.ListFilter,
                    tint    = if (sortMode != "date") color else t.TextSec,
                    bgColor = Color.Transparent,
                    onClick = onSortMenuToggle
                )
                DropdownMenu(
                    expanded         = showSortMenu,
                    onDismissRequest = { onSortSelect(sortMode) },
                    modifier         = Modifier
                        .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.6f))
                        .background(metalBrush(t.isDark), RoundedCornerShape(20.dp))
                        .border(bevelBorder(t.isDark), RoundedCornerShape(20.dp))
                ) {
                    listOf("date" to "Date Added", "name" to "Name A–Z",
                        "size" to "File Size", "type" to "File Type").forEach { (key, label) ->
                        DropdownMenuItem(
                            text = {
                                Text(label,
                                    color      = if (sortMode == key) color else t.TextSec,
                                    fontSize   = 13.sp,
                                    fontWeight = if (sortMode == key) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = FontFamily.Monospace
                                )
                            },
                            leadingIcon = {
                                if (sortMode == key)
                                    Icon(Lucide.Check, null, tint = color, modifier = Modifier.size(13.dp))
                                else Spacer(Modifier.size(13.dp))
                            },
                            onClick = { onSortSelect(key) }
                        )
                    }
                }
            }

            TopBarIconBtn(
                icon    = if (viewMode == "grid") Lucide.List else Lucide.Grid2x2,
                tint    = t.TextSec,
                bgColor = Color.Transparent,
                onClick = onViewToggle
            )
        }
    }
}

@Composable
fun TopBarIconBtn(icon: ImageVector, tint: Color, bgColor: Color, onClick: () -> Unit) {
    val t     = LocalNCTheme.current
    val press = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    Box(
        Modifier.size(36.dp)
            .shadow(((1f - press.value) * 3f + 0.5f).dp, RoundedCornerShape(10.dp), spotColor = Color.Black.copy(.4f))
            .background(
                if (bgColor == Color.Transparent) Brush.verticalGradient(
                    listOf(
                        if (t.isDark) Color(0xFF2C2C2E) else Color(0xFFFFFFFF),
                        if (t.isDark) Color(0xFF1C1C1E) else Color(0xFFF2F2F7)
                    )
                ) else Brush.verticalGradient(listOf(bgColor, bgColor)),
                RoundedCornerShape(10.dp)
            )
            .border(
                BorderStroke(0.5.dp, if (t.isDark) Color(0xFF38383A) else Color(0xFFC6C6C8)),
                RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                scope.launch { press.animateTo(1f, tween(70)); press.animateTo(0f, spring(.5f)) }
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(16.dp))
    }
}

// ============================================================================
// GALLERY SEARCH BAR — inset field
// ============================================================================

@Composable
fun GallerySearchBar(value: String, onValueChange: (String) -> Unit, onClear: () -> Unit) {
    val t       = LocalNCTheme.current
    val focused = remember { MutableInteractionSource() }
    val isFocused by focused.collectIsFocusedAsState()

    Row(
        Modifier.fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(.4f))
            .background(recessedBrush(t.isDark), RoundedCornerShape(12.dp))
            .border(
                BorderStroke(1.dp, Brush.verticalGradient(
                    if (isFocused)
                        listOf(Color.Black.copy(.4f), NC.Blue.copy(.3f))
                    else
                        listOf(Color.Black.copy(.5f), if (t.isDark) Color(0xFF2C2C2E) else Color(0xFFE5E5EA).copy(.6f))
                )),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Lucide.Search, null,
            tint     = if (isFocused) NC.Blue else t.TextMut,
            modifier = Modifier.size(15.dp)
        )
        BasicTextField(
            value             = value,
            onValueChange     = onValueChange,
            modifier          = Modifier.weight(1f),
            singleLine        = true,
            interactionSource = focused,
            textStyle         = TextStyle(color = t.TextPri, fontSize = 14.sp, fontFamily = FontFamily.Monospace),
            decorationBox     = { inner ->
                if (value.isEmpty()) Text("Search files…", color = t.TextMut, fontSize = 13.sp)
                inner()
            }
        )
        AnimatedVisibility(value.isNotEmpty()) {
            Box(
                Modifier.size(18.dp)
                    .shadow(1.dp, CircleShape)
                    .background(raisedBrush(t.isDark), CircleShape)
                    .border(bevelBorder(t.isDark), CircleShape)
                    .clickable { onClear() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Lucide.X, null, tint = t.TextSec, modifier = Modifier.size(9.dp))
            }
        }
    }
}

// ============================================================================
// GALLERY EMPTY STATE
// ============================================================================

@Composable
fun GalleryEmptyState(type: GalleryType, query: String) {
    val t     = LocalNCTheme.current
    val color = type.color()
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vault-door style empty indicator
            Box(
                Modifier.size(90.dp)
                    .shadow(6.dp, CircleShape, spotColor = Color.Black.copy(.5f))
                    .background(metalBrush(t.isDark), CircleShape)
                    .border(bevelBorder(t.isDark), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier.size(70.dp)
                        .background(recessedBrush(t.isDark), CircleShape)
                        .border(insetBorder(t.isDark), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (query.isNotEmpty()) Lucide.SearchX else type.icon,
                        null, tint = color.copy(.6f), modifier = Modifier.size(28.dp)
                    )
                }
            }
            Text(
                if (query.isNotEmpty()) "No results for \"$query\""
                else "No ${type.label.lowercase()} yet",
                color = t.TextSec, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                style = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1f), 3f))
            )
            Box(
                Modifier
                    .background(recessedBrush(t.isDark), RoundedCornerShape(8.dp))
                    .border(insetBorder(t.isDark), RoundedCornerShape(8.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    if (query.isNotEmpty()) "Try a different search term"
                    else "Tap + to import files",
                    color = t.TextMut, fontSize = 12.sp, fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

// ============================================================================
// GALLERY GRID VIEW
// ============================================================================

@Composable
fun GalleryGridView(
    files          : List<VaultFile>,
    appData        : AppData,
    onSaveData     : (AppData) -> Unit,
    onShowSnackbar : (String) -> Unit,
    onFileClick    : (VaultFile) -> Unit
) {
    LazyVerticalGrid(
        columns               = GridCells.Fixed(3),
        modifier              = Modifier.fillMaxSize(),
        contentPadding        = PaddingValues(10.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files, key = { it.id }) { file ->
            GalleryGridCell(
                file        = file,
                onToggleFav = {
                    val newFav = !file.fav
                    onSaveData(appData.copy(files = appData.files.map {
                        if (it.id == file.id) it.copy(fav = newFav) else it }))
                    onShowSnackbar(if (newFav) "Added to Favorites" else "Removed from Favorites")
                },
                onDelete    = {
                    val fmt = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
                    onSaveData(appData.copy(
                        files      = appData.files.filter { it.id != file.id },
                        recycleBin = appData.recycleBin + file.copy(deletedDate = fmt.format(Date()))
                    ))
                    onShowSnackbar("Moved to Recycle Bin")
                },
                onClick = { onFileClick(file) }
            )
        }
        item(span = { GridItemSpan(3) }) { Spacer(Modifier.height(180.dp)) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryGridCell(
    file        : VaultFile,
    onToggleFav : () -> Unit,
    onDelete    : () -> Unit,
    onClick     : () -> Unit
) {
    val t      = LocalNCTheme.current
    val accent = NC.accentFor(file.type)
    var showOpts by remember { mutableStateOf(false) }

    // One UI Grid Cell
    Box(
        Modifier.aspectRatio(1f)
            .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(.06f))
            .background(t.Surface, RoundedCornerShape(16.dp))
            .border(bevelBorder(t.isDark), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            Modifier.fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .combinedClickable(onClick = onClick, onLongClick = { showOpts = !showOpts })
        ) {
            when (file.type) {
                "photo" -> {
                    val bmpState = rememberAsyncThumbnail(file, 320)
                    val bmp = bmpState.value
                    if (bmp != null) {
                        Image(bmp.asImageBitmap(), file.name, Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop)
                        // Film border effect
                        Box(Modifier.fillMaxSize().background(
                            Brush.verticalGradient(listOf(Color.Black.copy(.2f), Color.Transparent,
                                Color.Transparent, Color.Black.copy(.55f)))
                        ))
                    } else {
                        NeoIconBg(accent, NC.iconFor(file.type))
                    }
                }
                else -> NeoIconBg(accent, NC.iconFor(file.type))
            }

            // Type badge — engraved look
            Box(
                Modifier.align(Alignment.TopStart).padding(4.dp)
                    .shadow(1.dp, RoundedCornerShape(4.dp))
                    .background(raisedBrush(t.isDark), RoundedCornerShape(4.dp))
                    .border(bevelBorder(t.isDark), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(file.type.take(3).uppercase(), color = accent, fontSize = 7.sp,
                    fontWeight = FontWeight.ExtraBold, letterSpacing = 0.8.sp, fontFamily = FontFamily.Monospace)
            }

            // Fav button
            Box(
                Modifier.align(Alignment.TopEnd).padding(4.dp).size(22.dp)
                    .shadow(1.dp, CircleShape, spotColor = Color.Black.copy(.4f))
                    .background(
                        if (file.fav) Brush.radialGradient(listOf(NC.Pink.copy(.5f), NC.Pink.copy(.15f)))
                        else raisedBrush(t.isDark),
                        CircleShape
                    )
                    .border(
                        if (file.fav) BorderStroke(1.dp, NC.Pink.copy(.6f))
                        else bevelBorder(t.isDark), CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) { onToggleFav() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Lucide.Heart, null,
                    tint = if (file.fav) NC.Pink else t.TextMut.copy(.5f),
                    modifier = Modifier.size(10.dp))
            }

            // Filename strip
            Box(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .background(Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(.8f))
                    ))
                    .padding(horizontal = 5.dp, vertical = 5.dp)
            ) {
                Text(file.name, color = Color.White.copy(.9f), fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            // Long-press options overlay
            AnimatedVisibility(
                visible  = showOpts,
                modifier = Modifier.fillMaxSize(),
                enter    = fadeIn(tween(100)), exit = fadeOut(tween(100))
            ) {
                Box(
                    Modifier.fillMaxSize()
                        .background(Color.Black.copy(.82f))
                        .clickable { showOpts = false },
                    contentAlignment = Alignment.Center
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        GridOptBtn(
                            icon  = Lucide.Heart,
                            label = if (file.fav) "Unfav" else "Fav",
                            color = if (file.fav) NC.Pink else Color.White
                        ) { onToggleFav(); showOpts = false }
                        GridOptBtn(Lucide.Trash2, "Delete", NC.Red) { onDelete(); showOpts = false }
                    }
                }
            }
        }
    }
}

@Composable
fun GridOptBtn(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    val t = LocalNCTheme.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier            = Modifier.clickable { onClick() }
    ) {
        Box(
            Modifier.size(40.dp)
                .shadow(3.dp, CircleShape, spotColor = Color.Black.copy(.5f))
                .background(raisedBrush(t.isDark), CircleShape)
                .border(
                    BorderStroke(1.dp, Brush.verticalGradient(
                        listOf(color.copy(.4f), color.copy(.1f))
                    )),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(17.dp))
        }
        Text(label, color = color, fontSize = 9.sp, fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace)
    }
}

// ============================================================================
// GALLERY LIST VIEW
// ============================================================================

@Composable
fun GalleryListView(
    files          : List<VaultFile>,
    appData        : AppData,
    onSaveData     : (AppData) -> Unit,
    onShowSnackbar : (String) -> Unit,
    onFileClick    : (VaultFile) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding      = PaddingValues(top = 12.dp, bottom = 180.dp)
    ) {
        items(files, key = { it.id }) { file ->
            GalleryListRow(
                file        = file,
                onToggleFav = {
                    val newFav = !file.fav
                    onSaveData(appData.copy(files = appData.files.map {
                        if (it.id == file.id) it.copy(fav = newFav) else it }))
                    onShowSnackbar(if (newFav) "Added to Favorites" else "Removed from Favorites")
                },
                onDelete    = {
                    val fmt = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
                    onSaveData(appData.copy(
                        files      = appData.files.filter { it.id != file.id },
                        recycleBin = appData.recycleBin + file.copy(deletedDate = fmt.format(Date()))
                    ))
                    onShowSnackbar("Moved to Recycle Bin")
                },
                onClick = { onFileClick(file) }
            )
        }
    }
}

@Composable
fun GalleryListRow(
    file        : VaultFile,
    onToggleFav : () -> Unit,
    onDelete    : () -> Unit,
    onClick     : () -> Unit
) {
    val t         = LocalNCTheme.current
    val accent    = NC.accentFor(file.type)
    var offsetX   by remember { mutableFloatStateOf(0f) }
    val anim      by animateFloatAsState(offsetX, spring(stiffness = Spring.StiffnessMediumLow), label = "sw")
    val threshold = 160f
    val scope     = rememberCoroutineScope()

    Box(
        Modifier.fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.06f))
            .background(t.Surface, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Delete reveal
        Box(
            Modifier.matchParentSize()
                .background(
                    Brush.horizontalGradient(listOf(Color.Transparent, NC.Red.copy(.18f))),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(Modifier.padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(Lucide.Trash2, null, tint = NC.Red, modifier = Modifier.size(14.dp))
                Text("Delete", color = NC.Red, fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace)
            }
        }

        // Card content
        Row(
            modifier = Modifier.fillMaxWidth()
                .offset { IntOffset(anim.toInt(), 0) }
                .shadow(if (anim < -10f) 0.dp else 2.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(.06f))
                .background(t.Surface, RoundedCornerShape(20.dp))
                .border(bevelBorder(t.isDark), RoundedCornerShape(20.dp))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd    = {
                            if (anim < -threshold) onDelete()
                            scope.launch { offsetX = 0f }
                        },
                        onDragCancel = { scope.launch { offsetX = 0f } },
                        onHorizontalDrag = { _, d ->
                            offsetX = (offsetX + d).coerceIn(-threshold * 1.15f, 0f)
                        }
                    )
                }
                .clickable { onClick() }
                .padding(10.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Thumbnail / icon well — recessed
            Box(
                Modifier.size(48.dp)
                    .shadow(2.dp, RoundedCornerShape(10.dp), spotColor = Color.Black.copy(.4f))
                    .background(recessedBrush(t.isDark), RoundedCornerShape(10.dp))
                    .border(insetBorder(t.isDark), RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (file.type == "photo") {
                    val bmpState = rememberAsyncThumbnail(file, 100)
                    val bmp = bmpState.value
                    if (bmp != null) {
                        Image(bmp.asImageBitmap(), file.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Icon(NC.iconFor(file.type), null, tint = accent, modifier = Modifier.size(20.dp))
                    }
                } else {
                    Icon(NC.iconFor(file.type), null, tint = accent, modifier = Modifier.size(20.dp))
                }
            }

            Column(Modifier.weight(1f)) {
                Text(file.name, color = t.TextPri, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    style = TextStyle(shadow = Shadow(Color.Black.copy(.3f), Offset(0f, 1f), 2f)))
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        Modifier.shadow(1.dp, RoundedCornerShape(4.dp))
                            .background(raisedBrush(t.isDark), RoundedCornerShape(4.dp))
                            .border(bevelBorder(t.isDark), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(file.type.take(3).uppercase(), color = accent, fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp, fontFamily = FontFamily.Monospace)
                    }
                    Text("${formatStorageSize(file.size)}  ·  ${file.date}",
                        color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(28.dp)
                        .shadow(1.dp, CircleShape, spotColor = Color.Black.copy(.3f))
                        .background(
                            if (file.fav) Brush.radialGradient(listOf(NC.Pink.copy(.4f), NC.Pink.copy(.1f)))
                            else raisedBrush(t.isDark),
                            CircleShape
                        )
                        .border(
                            if (file.fav) BorderStroke(1.dp, NC.Pink.copy(.5f))
                            else bevelBorder(t.isDark), CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onToggleFav() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Lucide.Heart, null,
                        tint = if (file.fav) NC.Pink else t.TextMut,
                        modifier = Modifier.size(12.dp))
                }
                Spacer(Modifier.width(6.dp))
                Box(
                    Modifier.size(28.dp)
                        .shadow(1.dp, CircleShape, spotColor = Color.Black.copy(.3f))
                        .background(raisedBrush(t.isDark), CircleShape)
                        .border(bevelBorder(t.isDark), CircleShape)
                        .clickable { onDelete() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Lucide.Trash, null, tint = t.TextMut, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

// ============================================================================
// MEDIA VIEWER DIALOG — porthole window style
// ============================================================================

@Composable
fun NeoMediaViewerDialog(
    file           : VaultFile,
    onDismiss      : () -> Unit,
    onShowSnackbar : (String) -> Unit,
    musicCtrl      : MusicPlayerController,
    context        : Context
) {
    val t           = LocalNCTheme.current
    var isExporting by remember { mutableStateOf(false) }

    LaunchedEffect(file.id, file.type) {
        if (file.type == "video" || file.type == "audio") {
            musicCtrl.pause()
        }
    }
    val launcher    = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("*/*")) { uri ->
        uri?.let {
            isExporting = true
            CoroutineScope(Dispatchers.IO).launch {
                val ok = FileUtil.exportFile(context, it, file.data)
                withContext(Dispatchers.Main) {
                    isExporting = false
                    onShowSnackbar(if (ok) "✓ Exported successfully" else "Export failed")
                    if (ok) onDismiss()
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            Modifier.fillMaxWidth(.96f).fillMaxHeight(.93f)
                .shadow(8.dp, RoundedCornerShape(26.dp), spotColor = Color.Black.copy(.2f))
                .background(t.Surface, RoundedCornerShape(26.dp))
                .border(bevelBorder(t.isDark), RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp))
        ) {
            Column(
                Modifier.fillMaxSize()
            ) {
                val accent = NC.accentFor(file.type)

                // Header — raised bar
                Box(
                    Modifier.fillMaxWidth()
                        .background(metalBrush(t.isDark))
                        .border(BorderStroke(1.dp, Brush.verticalGradient(
                            listOf(Color.Transparent, t.Border.copy(.3f))
                        )))
                        .padding(horizontal = 14.dp, vertical = 11.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(34.dp)
                                    .shadow(2.dp, RoundedCornerShape(9.dp), spotColor = Color.Black.copy(.4f))
                                    .background(raisedBrush(t.isDark), RoundedCornerShape(9.dp))
                                    .border(bevelBorder(t.isDark), RoundedCornerShape(9.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(NC.iconFor(file.type), null, tint = accent, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text(file.name, color = t.TextPri, fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1f), 2f)))
                                Text("${formatStorageSize(file.size)}  ·  ${file.date}",
                                    color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (file.type == "audio") {
                                TopBarIconBtn(Lucide.CirclePlay, NC.Green, Color.Transparent) {
                                    musicCtrl.play(file, context); onDismiss()
                                }
                            }
                            TopBarIconBtn(Lucide.X, t.TextSec, Color.Transparent) { onDismiss() }
                        }
                    }
                }

                // Content area
                Box(Modifier.fillMaxWidth().weight(1f).background(t.Bg), contentAlignment = Alignment.Center) {
                    val ext = file.name.substringAfterLast('.').lowercase()
                    when {
                        file.type == "photo"        -> ImageViewerContent(file)
                        file.type == "video"        -> VideoPlayerContent(file, context)
                        file.type == "audio"        -> AudioPlayerContent(file, context)
                        ext == "pdf"                -> PdfViewerContent(file)
                        ext in setOf("html","htm")  -> HtmlViewerContent(file)
                        ext in CODE_EXTENSIONS      -> TextViewerContent(file)
                        else                        -> GenericFileContent(file)
                    }
                }

                // Footer — export button
                Box(
                    Modifier.fillMaxWidth()
                        .background(metalBrush(t.isDark))
                        .border(BorderStroke(1.dp, Brush.verticalGradient(
                            listOf(t.Border.copy(.4f), Color.Transparent)
                        )))
                        .padding(14.dp)
                ) {
                    val press = remember { Animatable(0f) }
                    val scope = rememberCoroutineScope()
                    Box(
                        Modifier.fillMaxWidth()
                            .height(48.dp)
                            .shadow(
                                elevation = ((1f - press.value) * 6f + 1f).dp,
                                shape     = RoundedCornerShape(20.dp),
                                spotColor = Color.Black.copy(.6f)
                            )
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        lerp(Color(0xFF0A84FF), Color(0xFF007AFF), press.value),
                                        lerp(Color(0xFF007AFF), Color(0xFF0055C1), press.value)
                                    )
                                ),
                                RoundedCornerShape(20.dp)
                            )
                            .border(
                                BorderStroke(1.dp, Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF0A84FF).copy(.4f),
                                        Color(0xFF007AFF).copy(.2f)
                                    )
                                )),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable(enabled = !isExporting) {
                                scope.launch { press.animateTo(1f, tween(80)); press.animateTo(0f, spring(.5f)) }
                                launcher.launch(file.name)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(color = NC.Blue, strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Lucide.Download, null, tint = Color.White.copy(.9f),
                                    modifier = Modifier.size(17.dp))
                                Text("Export File", color = Color.White.copy(.9f), fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp,
                                    fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }
    }
}

private val CODE_EXTENSIONS = setOf(
    "txt","md","py","js","ts","css","xml","json","yaml","yml","kt","java",
    "swift","c","cpp","h","rs","go","rb","sh","bat","csv","log","ini","toml","sql"
)

// ============================================================================
// IMAGE VIEWER — unchanged logic, skeu chrome
// ============================================================================

@Composable
fun ImageViewerContent(file: VaultFile) {
    val t        = LocalNCTheme.current
    var bitmap   by remember { mutableStateOf<Bitmap?>(null) }
    var loading  by remember { mutableStateOf(true) }
    var scale    by remember { mutableFloatStateOf(1f) }
    var offset   by remember { mutableStateOf(Offset.Zero) }
    var showEdit by remember { mutableStateOf(false) }
    var bright   by remember { mutableFloatStateOf(0f) }
    var contrast by remember { mutableFloatStateOf(1f) }
    var saturat  by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(file.id) {
        bitmap = withContext(Dispatchers.IO) {
            try {
                val bytes = Base64.decode(file.data, Base64.NO_WRAP)
                val opts  = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
                var sample = 1
                val maxSide = maxOf(opts.outWidth, opts.outHeight)
                while (maxSide / sample > 2048) sample *= 2
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options().apply {
                    inSampleSize = sample; inPreferredConfig = Bitmap.Config.ARGB_8888
                })
            } catch (_: Exception) { null }
        }
        loading = false
    }
    DisposableEffect(file.id) { onDispose { bitmap?.recycle() } }

    val cm = remember(bright, contrast, saturat) {
        val m = androidx.compose.ui.graphics.ColorMatrix()
        m.setToSaturation(saturat)
        val tr = bright * 255f + (1f - contrast) * 128f
        val contrastMatrix = androidx.compose.ui.graphics.ColorMatrix(floatArrayOf(
            contrast, 0f, 0f, 0f, tr, 0f, contrast, 0f, 0f, tr,
            0f, 0f, contrast, 0f, tr, 0f, 0f, 0f, 1f, 0f
        ))
        m.timesAssign(contrastMatrix); m
    }

    when {
        loading -> CircularProgressIndicator(color = NC.Blue, strokeWidth = 2.dp,
            modifier = Modifier.size(32.dp))
        bitmap == null -> Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Lucide.ImageOff, null, tint = NC.Pink, modifier = Modifier.size(48.dp))
            Text("Cannot load image", color = t.TextSec)
        }
        else -> Box(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize()
                .pointerInput(Unit) { detectTransformGestures { _, pan, zoom, _ ->
                    scale  = (scale * zoom).coerceIn(.5f, 6f); offset += pan } }
                .pointerInput(Unit) { detectTapGestures(onDoubleTap = {
                    scale = if (scale > 1.4f) 1f else 2.5f; offset = Offset.Zero }) }
            ) {
                Image(bitmap!!.asImageBitmap(), file.name,
                    Modifier.fillMaxSize().graphicsLayer {
                        scaleX = scale; scaleY = scale
                        translationX = offset.x; translationY = offset.y
                    },
                    contentScale = ContentScale.Fit,
                    colorFilter  = ColorFilter.colorMatrix(cm))
            }
            // Edit and reset buttons — raised
            Row(Modifier.align(Alignment.TopEnd).padding(10.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TopBarIconBtn(
                    icon    = Lucide.SlidersHorizontal,
                    tint    = if (showEdit) NC.Amber else t.TextSec,
                    bgColor = Color.Transparent
                ) { showEdit = !showEdit }
                if (bright != 0f || contrast != 1f || saturat != 1f) {
                    TopBarIconBtn(Lucide.RotateCcw, NC.Amber, Color.Transparent) {
                        bright = 0f; contrast = 1f; saturat = 1f
                    }
                }
            }
            AnimatedVisibility(showEdit, Modifier.align(Alignment.BottomCenter),
                slideInVertically { it / 2 } + fadeIn(), slideOutVertically { it / 2 } + fadeOut()) {
                Box(
                    Modifier.fillMaxWidth()
                        .background(metalBrush(t.isDark), RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .border(BorderStroke(1.dp, Brush.verticalGradient(
                            listOf(t.Border, Color.Transparent)
                        )), RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ImageSlider("Brightness", bright,   -1f, 1f, NC.Amber)  { bright   = it }
                        ImageSlider("Contrast",   contrast, .5f, 2f, NC.Cyan)   { contrast = it }
                        ImageSlider("Saturation", saturat,   0f, 2f, NC.Violet) { saturat  = it }
                    }
                }
            }
            if (scale == 1f) {
                Box(
                    Modifier.align(Alignment.BottomCenter)
                        .padding(bottom = if (showEdit) 155.dp else 12.dp)
                        .shadow(1.dp, RoundedCornerShape(8.dp))
                        .background(raisedBrush(t.isDark), RoundedCornerShape(8.dp))
                        .border(bevelBorder(t.isDark), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text("Pinch to zoom  ·  Double-tap to fit",
                        color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
fun ImageSlider(label: String, value: Float, min: Float, max: Float, color: Color, onChange: (Float) -> Unit) {
    val t = LocalNCTheme.current
    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp), Alignment.CenterVertically) {
        Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace, modifier = Modifier.width(76.dp))
        Box(
            Modifier.weight(1f).height(24.dp)
                .shadow(1.dp, RoundedCornerShape(12.dp))
                .background(recessedBrush(t.isDark), RoundedCornerShape(12.dp))
                .border(insetBorder(t.isDark), RoundedCornerShape(12.dp))
        ) {
            Slider(value, onChange, Modifier.fillMaxSize().padding(horizontal = 4.dp),
                valueRange = min..max,
                colors = SliderDefaults.colors(
                    thumbColor = color, activeTrackColor = color.copy(.7f),
                    inactiveTrackColor = Color.Transparent
                ))
        }
        Text("%.2f".format(value), color = t.TextMut, fontSize = 9.sp,
            modifier = Modifier.width(32.dp), textAlign = TextAlign.End,
            fontFamily = FontFamily.Monospace)
    }
}

// ============================================================================
// VIDEO PLAYER — unchanged
// ============================================================================

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerContent(file: VaultFile, context: Context) {
    val t       = LocalNCTheme.current
    var tmp     by remember { mutableStateOf<File?>(null) }
    var ready   by remember { mutableStateOf(false) }
    var errored by remember { mutableStateOf(false) }

    LaunchedEffect(file.id) {
        withContext(Dispatchers.IO) {
            try { tmp = BitmapUtils.writeTempFile(context, file) }
            catch (_: Exception) { errored = true }
            ready = true
        }
    }

    when {
        !ready -> Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CircularProgressIndicator(color = NC.Violet, strokeWidth = 2.dp)
            Text("Preparing video…", color = t.TextSec, fontSize = 13.sp)
        }
        errored || tmp == null -> GenericFileContent(file)
        else -> {
            val player = remember(tmp) {
                try {
                    androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
                        setMediaItem(androidx.media3.common.MediaItem.fromUri(Uri.fromFile(tmp!!)))
                        prepare(); playWhenReady = true
                    }
                } catch (_: Exception) { null }
            }
            DisposableEffect(player) { onDispose { player?.release() } }
            if (player != null) {
                AndroidView(
                    factory = { ctx ->
                        androidx.media3.ui.PlayerView(ctx).apply {
                            this.player = player
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                            setShowBuffering(androidx.media3.ui.PlayerView.SHOW_BUFFERING_ALWAYS)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else GenericFileContent(file)
        }
    }
}

// ============================================================================
// AUDIO PLAYER (inline) — vinyl-inspired
// ============================================================================

@Composable
fun AudioPlayerContent(file: VaultFile, context: Context) {
    val t        = LocalNCTheme.current
    var playing  by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var duration by remember { mutableStateOf(1L) }
    var position by remember { mutableStateOf(0L) }
    var mp       by remember { mutableStateOf<MediaPlayer?>(null) }
    var loading  by remember { mutableStateOf(true) }
    val handler  = remember { Handler(Looper.getMainLooper()) }
    val ticker   = remember { mutableStateOf<Runnable?>(null) }

    fun startTicker(p: MediaPlayer) {
        ticker.value?.let { handler.removeCallbacks(it) }
        val r = object : Runnable {
            override fun run() {
                try {
                    if (p.isPlaying) {
                        val dur = p.duration.toLong().coerceAtLeast(1L)
                        val pos = p.currentPosition.toLong()
                        duration = dur; position = pos; progress = pos.toFloat() / dur.toFloat()
                        handler.postDelayed(this, 120)
                    }
                } catch (_: Exception) {}
            }
        }
        ticker.value = r; handler.post(r)
    }

    LaunchedEffect(file.id) {
        withContext(Dispatchers.IO) {
            try {
                val tmp = BitmapUtils.writeTempFile(context, file)
                val p = MediaPlayer().apply {
                    setDataSource(tmp.absolutePath); prepare()
                    setOnCompletionListener { playing = false; progress = 0f; position = 0L }
                }
                withContext(Dispatchers.Main) {
                    duration = p.duration.toLong().coerceAtLeast(1L); mp = p; loading = false
                }
            } catch (_: Exception) { loading = false }
        }
    }
    DisposableEffect(file.id) {
        onDispose {
            ticker.value?.let { handler.removeCallbacks(it) }
            mp?.let { try { if (it.isPlaying) it.stop(); it.release() } catch (_: Exception) {} }
            mp = null
        }
    }

    fun fmtMs(ms: Long) = "%d:%02d".format(ms / 60000, (ms % 60000) / 1000)

    val waveInf = rememberInfiniteTransition(label = "aw")
    val wave by waveInf.animateFloat(0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(2800, easing = LinearEasing)), label = "wv")

    Column(
        Modifier.fillMaxSize().background(t.Bg).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {
        // Vinyl record display
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
            Canvas(Modifier.fillMaxSize()) {
                val c = Offset(size.width / 2, size.height / 2)
                val r = size.minDimension / 2
                // Plinth ring
                drawCircle(metalBrush(t.isDark), r)
                drawCircle(recessedBrush(t.isDark), r * .9f, c)
                // Pulse rings
                if (playing) for (i in 0..3) {
                    val ph = wave + i * (PI / 2.5f).toFloat()
                    drawCircle(NC.Green.copy((sin(ph.toDouble()) * .1f + .15f).toFloat().coerceAtLeast(0f)),
                        r * (.45f + i * .12f + (sin(ph.toDouble()) * .08f + .06f).toFloat()),
                        c, style = Stroke(1.2f))
                }
                // Vinyl body
                drawCircle(Color(0xFF0A0804), r * .78f, c)
                for (i in 1..6) {
                    drawCircle(Color.White.copy(.025f + i * .003f), r * (.18f + i * .1f), c, style = Stroke(.8f))
                }
                // iOS blue label center
                drawCircle(Brush.radialGradient(
                    listOf(Color(0xFF1C1C1E), Color(0xFF0A84FF).copy(.5f)), c, r * .26f), r * .26f, c)
                drawCircle(Brush.radialGradient(listOf(Color(0xFF5AC8FA), Color(0xFF007AFF))), r * .085f, c)
                drawCircle(Color(0xFF060402), r * .032f, c)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("NOW PLAYING", color = NC.Blue, fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
            Text(file.name.substringBeforeLast('.'), color = t.TextPri, fontSize = 17.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                style = TextStyle(shadow = Shadow(Color.Black.copy(.5f), Offset(0f, 2f), 4f)))
            Text(formatStorageSize(file.size), color = t.TextMut, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        }

        if (loading) {
            Box(Modifier.fillMaxWidth().height(6.dp)
                .shadow(1.dp, RoundedCornerShape(3.dp))
                .background(recessedBrush(t.isDark), RoundedCornerShape(3.dp))
                .border(insetBorder(t.isDark), RoundedCornerShape(3.dp))
                .clip(RoundedCornerShape(3.dp))
            ) {
                val inf = rememberInfiniteTransition(label = "pl")
                val sh by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(1000)), label = "ps")
                Box(Modifier.fillMaxWidth(sh).fillMaxHeight()
                    .background(Brush.horizontalGradient(listOf(NC.Blue, NC.Cyan))))
            }
        } else {
            Column(Modifier.fillMaxWidth()) {
                Box(Modifier.fillMaxWidth().height(14.dp)
                    .shadow(1.dp, RoundedCornerShape(7.dp))
                    .background(recessedBrush(t.isDark), RoundedCornerShape(7.dp))
                    .border(insetBorder(t.isDark), RoundedCornerShape(7.dp))
                    .clip(RoundedCornerShape(7.dp)).padding(horizontal = 2.dp)
                ) {
                    Slider(progress.coerceIn(0f, 1f), { v ->
                        val p = mp ?: return@Slider
                        val pos = (v * p.duration).toInt().coerceAtLeast(0)
                        p.seekTo(pos); progress = v; position = pos.toLong()
                    }, Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(thumbColor = NC.Blue,
                            activeTrackColor = NC.Blue, inactiveTrackColor = Color.Transparent))
                }
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text(fmtMs(position), color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Text(fmtMs(duration),  color = t.TextMut, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
                SkeuTransportBtn(48.dp, onClick = { mp?.seekTo(0); position = 0L; progress = 0f }) {
                    Icon(Lucide.RotateCcw, null, tint = t.TextSec, modifier = Modifier.size(22.dp))
                }
                SkeuTransportBtn(64.dp, isPrimary = true, onClick = {
                    val p = mp ?: return@SkeuTransportBtn
                    if (p.isPlaying) { p.pause(); playing = false; ticker.value?.let { handler.removeCallbacks(it) } }
                    else { p.start(); playing = true; startTicker(p) }
                }) {
                    Icon(if (playing) Lucide.Pause else Lucide.Play,
                        null, tint = Color.Black, modifier = Modifier.size(30.dp))
                }
                SkeuTransportBtn(48.dp, onClick = { mp?.let { it.seekTo(it.duration) } }) {
                    Icon(Lucide.SkipForward, null, tint = t.TextSec, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}


// ============================================================================
// TEXT / CODE VIEWER — unchanged logic
// ============================================================================

private val KEYWORDS = setOf(
    "fun","val","var","class","object","interface","import","package","return",
    "if","else","when","for","while","do","try","catch","throw","finally",
    "true","false","null","override","private","public","protected","internal",
    "data","sealed","enum","companion","suspend","def","self","None","True","False",
    "pass","lambda","yield","with","from","as","const","let","function","async",
    "await","export","default","new","static","void","int","float","double","bool","String"
)
private enum class TType { KEYWORD, STRING, NUMBER, COMMENT, SYMBOL, PLAIN }
private data class Tok(val text: String, val type: TType)

private fun tokenize(line: String): List<Tok> {
    val cs  = line.indexOf("//").takeIf { it >= 0 } ?: line.indexOf("#").takeIf { it >= 0 }
    val eff = if (cs != null) line.substring(0, cs) else line
    val cmt = if (cs != null) line.substring(cs) else ""
    val toks = mutableListOf<Tok>(); var pos = 0
    while (pos < eff.length) {
        val c = eff[pos]
        when {
            c == '"' || c == '\'' -> {
                val e = eff.indexOf(c, pos + 1).takeIf { it >= 0 } ?: eff.lastIndex
                toks += Tok(eff.substring(pos, e + 1), TType.STRING); pos = e + 1
            }
            c.isDigit() -> {
                var e = pos
                while (e < eff.length && (eff[e].isDigit() || eff[e] == '.')) e++
                toks += Tok(eff.substring(pos, e), TType.NUMBER); pos = e
            }
            c.isLetter() || c == '_' -> {
                var e = pos
                while (e < eff.length && (eff[e].isLetterOrDigit() || eff[e] == '_')) e++
                val w = eff.substring(pos, e)
                toks += Tok(w, if (w in KEYWORDS) TType.KEYWORD else TType.PLAIN); pos = e
            }
            c in "(){}[]<>=!+-*/&|^%~@:,.;" -> { toks += Tok(c.toString(), TType.SYMBOL); pos++ }
            else -> { toks += Tok(c.toString(), TType.PLAIN); pos++ }
        }
    }
    if (cmt.isNotEmpty()) toks += Tok(cmt, TType.COMMENT)
    return toks
}

private fun highlight(raw: String) = buildAnnotatedString {
    raw.lines().forEachIndexed { i, line ->
        tokenize(line).forEach { (t, type) ->
            withStyle(SpanStyle(color = when (type) {
                TType.KEYWORD -> Color(0xFF60AAFF)
                TType.STRING  -> Color(0xFF77DD88)
                TType.NUMBER  -> Color(0xFFFFCC66)
                TType.COMMENT -> Color(0xFF666680)
                TType.SYMBOL  -> Color(0xFFCC77FF)
                TType.PLAIN   -> Color(0xFFCCCCDD)
            })) { append(t) }
        }
        if (i < raw.lines().lastIndex) append('\n')
    }
}

@Composable
fun TextViewerContent(file: VaultFile) {
    val t    = LocalNCTheme.current
    var text by remember { mutableStateOf<String?>(null) }
    val ext  = file.name.substringAfterLast('.').lowercase()
    val isCode = ext in CODE_EXTENSIONS && ext !in setOf("txt", "md", "log", "csv")

    LaunchedEffect(file.id) {
        text = withContext(Dispatchers.IO) {
            try { String(Base64.decode(file.data, Base64.NO_WRAP), Charsets.UTF_8) }
            catch (_: Exception) { "Unable to decode file." }
        }
    }
    if (text == null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CircularProgressIndicator(color = NC.Amber, strokeWidth = 2.dp)
            Text("Decoding…", color = t.TextMut)
        }
        return
    }

    val hl    = remember(text, isCode) { if (isCode && text != null) highlight(text!!) else null }
    val lines = text!!.lines()

    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxWidth()
                .background(metalBrush(t.isDark))
                .border(BorderStroke(1.dp, Brush.verticalGradient(
                    listOf(Color.Transparent, t.Border.copy(.3f))
                )))
                .padding(horizontal = 14.dp, vertical = 9.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.shadow(1.dp, RoundedCornerShape(5.dp))
                        .background(raisedBrush(t.isDark), RoundedCornerShape(5.dp))
                        .border(bevelBorder(t.isDark), RoundedCornerShape(5.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)) {
                        Text(".${ext.uppercase()}", color = NC.Amber, fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
                    }
                    if (isCode) Text("Syntax highlighting", color = NC.Green, fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace)
                }
                Text("${lines.size} lines · ${text!!.length} chars", color = t.TextMut, fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace)
            }
        }

        val scroll = rememberScrollState()
        Row(Modifier.fillMaxSize()
            .background(if (isCode) Color(0xFF0A0A12) else t.Bg)
            .verticalScroll(scroll)
        ) {
            if (isCode) {
                Column(Modifier.background(t.SurfaceHi).padding(horizontal = 10.dp, vertical = 14.dp)) {
                    lines.forEachIndexed { i, _ ->
                        Text("%4d".format(i + 1), color = t.TextMut, fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace, lineHeight = 18.sp)
                    }
                }
                Box(Modifier.width(1.dp).fillMaxHeight().background(
                    Brush.verticalGradient(listOf(Color.Black.copy(.5f), Color.White.copy(.04f), Color.Black.copy(.5f)))
                ))
            }
            androidx.compose.foundation.text.selection.SelectionContainer {
                if (hl != null) {
                    Text(hl, fontSize = 11.sp, fontFamily = FontFamily.Monospace,
                        lineHeight = 18.sp, modifier = Modifier.padding(12.dp))
                } else {
                    Text(text!!, color = t.TextPri, fontSize = 13.sp, lineHeight = 20.sp,
                        modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

// ============================================================================
// PDF / HTML / GENERIC VIEWERS
// ============================================================================

@Composable
fun PdfViewerContent(file: VaultFile) {
    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            settings.apply {
                javaScriptEnabled = true; loadWithOverviewMode = true
                useWideViewPort = true; builtInZoomControls = true; displayZoomControls = false
                setSupportZoom(true)
            }
            webViewClient = WebViewClient()
            loadDataWithBaseURL(null,
                """<!DOCTYPE html><html><head>
                <meta name="viewport" content="width=device-width,initial-scale=1">
                <style>body{margin:0;background:#0A0A12;}embed{width:100%;height:100vh;}</style>
                </head><body>
                <embed src="data:application/pdf;base64,${file.data}" type="application/pdf"/>
                </body></html>""",
                "text/html", "utf-8", null
            )
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }, modifier = Modifier.fillMaxSize())
}

@Composable
fun HtmlViewerContent(file: VaultFile) {
    var html by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(file.id) {
        html = withContext(Dispatchers.IO) {
            try { String(Base64.decode(file.data, Base64.NO_WRAP), Charsets.UTF_8) }
            catch (_: Exception) { "<p style='color:red'>Decode failed</p>" }
        }
    }
    if (html == null) { CircularProgressIndicator(color = NC.Cyan, strokeWidth = 2.dp); return }
    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            settings.apply {
                javaScriptEnabled = true; loadWithOverviewMode = true
                useWideViewPort = true; builtInZoomControls = true; displayZoomControls = false
            }
            webViewClient = WebViewClient()
            loadData(html!!, "text/html", "utf-8")
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }, modifier = Modifier.fillMaxSize())
}

@Composable
fun GenericFileContent(file: VaultFile) {
    val t      = LocalNCTheme.current
    val ext    = file.name.substringAfterLast('.').lowercase()
    val accent = NC.accentFor(file.type)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier            = Modifier.padding(36.dp)
    ) {
        // Trophy-case style icon display
        Box(
            Modifier.size(110.dp)
                .shadow(8.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(.6f))
                .background(metalBrush(t.isDark), RoundedCornerShape(22.dp))
                .border(bevelBorder(t.isDark), RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier.size(90.dp)
                    .background(recessedBrush(t.isDark), RoundedCornerShape(16.dp))
                    .border(insetBorder(t.isDark), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(NC.iconFor(file.type), null, tint = accent, modifier = Modifier.size(44.dp))
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                Modifier.shadow(2.dp, RoundedCornerShape(8.dp))
                    .background(raisedBrush(t.isDark), RoundedCornerShape(8.dp))
                    .border(bevelBorder(t.isDark), RoundedCornerShape(8.dp))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(".${ext.uppercase()}", color = accent, fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
            }
            Text(file.name, color = t.TextPri, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center, maxLines = 3, overflow = TextOverflow.Ellipsis,
                style = TextStyle(shadow = Shadow(Color.Black.copy(.4f), Offset(0f, 1.5f), 3f)))
            Box(
                Modifier.background(recessedBrush(t.isDark), RoundedCornerShape(6.dp))
                    .border(insetBorder(t.isDark), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("Export to open in a compatible app", color = t.TextMut, fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace)
            }
        }
    }
}