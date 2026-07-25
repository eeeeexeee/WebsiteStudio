package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColorPickerItem(
    label: String,
    currentHex: String,
    presetSwatches: List<String>,
    onColorChange: (String) -> Unit
) {
    val currentColor = parseHexColor(currentHex)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color(0xFFCBD5E1),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            // Visual Color Pill
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(currentColor)
                    .border(1.dp, Color(0x4DFFFFFF), RoundedCornerShape(6.dp))
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hex Input
            OutlinedTextField(
                value = currentHex,
                onValueChange = { input ->
                    if (input.startsWith("#") || input.length <= 7) {
                        onColorChange(input)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6366F1),
                    unfocusedBorderColor = Color(0x26FFFFFF),
                    focusedContainerColor = Color(0xCC0F172A),
                    unfocusedContainerColor = Color(0x660F172A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Swatches
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                presetSwatches.forEach { hex ->
                    val swatchColor = parseHexColor(hex)
                    val isSelected = currentHex.equals(hex, ignoreCase = true)

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(swatchColor)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color(0xFF38BDF8) else Color(0x33FFFFFF),
                                shape = CircleShape
                            )
                            .clickable { onColorChange(hex) }
                    )
                }
            }
        }
    }
}

fun parseHexColor(hex: String): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = when (cleanHex.length) {
            6 -> (0xFF000000 or cleanHex.toLong(16)).toInt()
            8 -> cleanHex.toLong(16).toInt()
            3 -> {
                val r = cleanHex[0].toString().repeat(2)
                val g = cleanHex[1].toString().repeat(2)
                val b = cleanHex[2].toString().repeat(2)
                (0xFF000000 or "$r$g$b".toLong(16)).toInt()
            }
            else -> 0xFF6366F1.toInt()
        }
        Color(colorInt)
    } catch (e: Exception) {
        Color(0xFF6366F1)
    }
}
