package com.mario.tanamin.ui.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mario.tanamin.ui.viewmodel.HomeViewModel
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeView(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val userName by viewModel.userName.collectAsState()
    val coins by viewModel.coins.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val currentLevel by viewModel.currentLevel.collectAsState()
    val mainWalletBalance by viewModel.mainWalletBalance.collectAsState()
    val investmentWalletBalance by viewModel.investmentWalletBalance.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()

    // Reload data when returning to this screen
    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    // Top Gradient - mengikuti pattern CourseView dengan MaterialTheme
    val topGradient = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.surface),
        startY = 0f,
        endY = 520f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Gradient overlay at the top - Hiasan
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(topGradient)
                .align(Alignment.TopCenter)
        ) {}

        // Main Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 1. Header Section
            HomeHeader(userName = userName)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Main Balance Card
            HomeBalanceCard(balance = "Rp${String.format(Locale.US, "%,d", totalBalance).replace(",", ".")}")

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Quick Stats (Course Level & Total Coins)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmallInfoCard(
                    title = "Course",
                    value = currentLevel,
                    icon = Icons.Default.MenuBook,
                    modifier = Modifier.weight(1f)
                )
                SmallInfoCard(
                    title = "Total Coins",
                    value = coins.toString(),
                    icon = Icons.Default.Diamond,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Investment Rate Section
            Text(
                text = "Investment Rate",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Pass data dinamis ke komponen ini
            InvestmentRateCard(
                mainWallet = mainWalletBalance,
                investWallet = investmentWalletBalance
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Course Streak Bar
            CourseStreakBar(streak = streak)

            // Bottom padding for navigation bar spacing
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- COMPONENTS ---

@Composable
fun HomeHeader(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "Welcome Back,",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = userName,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Wednesday, 3 December",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun HomeBalanceCard(balance: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Gradient Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Decorative Circles
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-20).dp, y = 20.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            )

            // Content
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Current Balance",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = balance,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Pager Indicators
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.onPrimary, CircleShape))
                        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f), CircleShape))
                        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f), CircleShape))
                    }
                }

                // Right Arrow Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SmallInfoCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
            // Decorative overlay
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 10.dp, y = 10.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
            )

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = value,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun InvestmentRateCard(
    mainWallet: Long,
    investWallet: Long
) {
    val total = mainWallet + investWallet
    val mainRatio = if (total > 0) (mainWallet.toFloat() / total.toFloat()) else 0.5f
    val investRatio = if (total > 0) (investWallet.toFloat() / total.toFloat()) else 0.5f

    // Menghitung Sudut
    val mainSweepAngle = 360f * mainRatio
    val investSweepAngle = 360f * investRatio

    // Rotasi awal agar visual mirip screenshot (Red di kanan bawah)
    // Kita mulai Green dari sisi kiri sedikit ke atas
    val startAngle = -100f // Trial & Error untuk mencocokkan visual "Green 68%" di kiri

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Gradient Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Decorative Circles (Bubbles)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp)
                    .background(Color.White.copy(alpha = 0.3f), CircleShape)
            )

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Overview",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    // Dropdown mock
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Month", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Chart Area
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Donut Chart Container
                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(140.dp)) {
                            val strokeWidth = 35f

                            // 1. Shadow Layer (untuk efek timbul/3D)
                            // Digambar sedikit offset (x+2, y+2) dengan warna hitam transparan
                            drawArc(
                                color = Color.Black.copy(alpha = 0.15f),
                                startAngle = startAngle,
                                sweepAngle = mainSweepAngle - 2f, // -2f untuk gap visual
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                                topLeft = Offset(4f, 4f)
                            )
                            drawArc(
                                color = Color.Black.copy(alpha = 0.15f),
                                startAngle = startAngle + mainSweepAngle,
                                sweepAngle = investSweepAngle - 2f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                                topLeft = Offset(4f, 4f)
                            )

                            // 2. Main Color Layer
                            // Green Arc (Main Wallet) - menggunakan primary color
                            drawArc(
                                color = Color(0xFF88C057), // Green from screenshot
                                startAngle = startAngle,
                                sweepAngle = mainSweepAngle - 2f, // Gap kecil
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                            )

                            // Red Arc (Investment Wallet)
                            drawArc(
                                color = Color(0xFFE8505B), // Red from screenshot
                                startAngle = startAngle + mainSweepAngle,
                                sweepAngle = investSweepAngle - 2f, // Gap kecil
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                            )
                        }

                        // Center Text (Balance)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Rp${String.format(Locale.US, "%,d", total).replace(",", ".")}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }

                        // --- Dynamic Labels (Trigonometri) ---
                        // Menghitung posisi label agar selalu di tengah lengkungan warna
                        // Rumus: x = r * cos(angle), y = r * sin(angle)

                        // Label Hijau
                        val greenMidAngle = startAngle + (mainSweepAngle / 2)
                        val greenRad = Math.toRadians(greenMidAngle.toDouble())
                        val greenX = (50 * cos(greenRad)).dp // 50 adalah radius text orbit
                        val greenY = (50 * sin(greenRad)).dp

                        Text(
                            text = "${(mainRatio * 100).toInt()}%",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = greenX, y = greenY)
                        )

                        // Label Merah
                        val redMidAngle = startAngle + mainSweepAngle + (investSweepAngle / 2)
                        val redRad = Math.toRadians(redMidAngle.toDouble())
                        val redX = (50 * cos(redRad)).dp
                        val redY = (50 * sin(redRad)).dp

                        Text(
                            text = "${(investRatio * 100).toInt()}%",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = redX, y = redY)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Legend (Right Side)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LegendItem(color = Color(0xFF88C057), text = "Main Wallet")
                        LegendItem(color = Color(0xFFE8505B), text = "Investment Wallet")
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    // Style "Pill" persis screenshot: Background transparan + Teks putih
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.5f), RoundedCornerShape(50)) // Bentuk Kapsul penuh
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Indikator bulat solid di dalam
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CourseStreakBar(streak: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Background
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Course Streak: $streak Days",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomePreview() {
    HomeView(navController = rememberNavController())
}

