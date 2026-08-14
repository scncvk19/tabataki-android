package com.example.tabataki

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale
import java.util.UUID

// --- Language & Strings ---




// --- Design Theme (Camouflage / Earth Tones) ---

val CamoColorScheme = darkColorScheme(
    primary = Color(0xFFA5B076),       // Light Olive / Khaki
    onPrimary = Color(0xFF1E2019),     // Darkest text on primary
    primaryContainer = Color(0xFF4A5D23), // Forest/Olive Green
    onPrimaryContainer = Color(0xFFE8F0C8),
    secondary = Color(0xFF8C7D62),     // Sand / Brown
    onSecondary = Color(0xFF1E2019),
    background = Color(0xFF1E2019),    // Very dark grey-green (Charcoal Base)
    onBackground = Color(0xFFDFE0D5),
    surface = Color(0xFF2C2F24),       // Dark Olive-Grey surface
    onSurface = Color(0xFFDFE0D5),
    surfaceVariant = Color(0xFF3B4031), // Lighter surface for cards
    onSurfaceVariant = Color(0xFFC2C5B5)
)

@Composable
fun CamoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CamoColorScheme,
        content = content
    )
}


