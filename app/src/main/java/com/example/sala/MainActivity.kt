package com.example.sala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sala.ui.theme.SalaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SalaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ParkingLotScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

enum class ParkingSpotType { REGULAR, DISABLED }

data class ParkingSpot(val id: Int, val type: ParkingSpotType, val isAvailable: Boolean, val row: Int)

@Composable
fun ParkingLotScreen(modifier: Modifier = Modifier) {
    val initialSpots = remember {
        listOf(
            // 1-қатар
            ParkingSpot(1, ParkingSpotType.DISABLED, true, 1),
            ParkingSpot(2, ParkingSpotType.REGULAR, false, 1),
            ParkingSpot(3, ParkingSpotType.REGULAR, true, 1),
            ParkingSpot(4, ParkingSpotType.REGULAR, true, 1),
            ParkingSpot(5, ParkingSpotType.REGULAR, false, 1),
            ParkingSpot(6, ParkingSpotType.REGULAR, true, 1),
            ParkingSpot(7, ParkingSpotType.REGULAR, true, 1),
            // 2-қатар
            ParkingSpot(8, ParkingSpotType.DISABLED, false, 2),
            ParkingSpot(9, ParkingSpotType.REGULAR, true, 2),
            ParkingSpot(10, ParkingSpotType.REGULAR, false, 2),
            ParkingSpot(11, ParkingSpotType.REGULAR, true, 2),
            ParkingSpot(12, ParkingSpotType.REGULAR, false, 2),
            ParkingSpot(13, ParkingSpotType.REGULAR, true, 2),
            ParkingSpot(14, ParkingSpotType.REGULAR, true, 2),
            // 3-қатар
            ParkingSpot(15, ParkingSpotType.DISABLED, true, 3),
            ParkingSpot(16, ParkingSpotType.REGULAR, false, 3),
            ParkingSpot(17, ParkingSpotType.REGULAR, true, 3),
            ParkingSpot(18, ParkingSpotType.REGULAR, true, 3),
            ParkingSpot(19, ParkingSpotType.REGULAR, false, 3),
            ParkingSpot(20, ParkingSpotType.REGULAR, true, 3),
            ParkingSpot(21, ParkingSpotType.REGULAR, true, 3),
        )
    }

    var parkingSpots by remember { mutableStateOf(initialSpots) }
    var selectedSpotId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Yessenov University Тұрағы",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center // Ортаға туралау
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    parkingSpots.groupBy { it.row }.forEach { (_, spotsInRow) ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            spotsInRow.forEach { spot ->
                                val isSelected = spot.id == selectedSpotId

                                val color = when {
                                    isSelected -> Color.Blue
                                    !spot.isAvailable -> Color.Red
                                    spot.type == ParkingSpotType.DISABLED -> Color(0xFFFBC02D)
                                    else -> Color(0xFF4CAF50)
                                }

                                val isClickable = (spot.isAvailable && selectedSpotId == null) || isSelected

                                ParkingSpotItem(spot = spot, color = color, isClickable = isClickable) {
                                    // Күйді дұрыс жаңарту логикасы
                                    val newSpots = parkingSpots.map {
                                        if (it.id == spot.id) {
                                            it.copy(isAvailable = !it.isAvailable)
                                        } else {
                                            it
                                        }
                                    }
                                    parkingSpots = newSpots
                                    selectedSpotId = if (isSelected) null else spot.id
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParkingSpotItem(spot: ParkingSpot, color: Color, isClickable: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(65.dp)
            .height(45.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable(enabled = isClickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val icon = when (spot.type) {
            ParkingSpotType.DISABLED -> Icons.Filled.Accessible
            ParkingSpotType.REGULAR -> Icons.Filled.DirectionsCar
        }
        Icon(
            imageVector = icon,
            contentDescription = spot.type.name,
            modifier = Modifier.size(30.dp),
            tint = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingLotScreenPreview() {
    SalaTheme {
        ParkingLotScreen()
    }
}
