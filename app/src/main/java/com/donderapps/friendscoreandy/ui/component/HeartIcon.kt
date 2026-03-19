package com.donderapps.friendscoreandy.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.donderapps.friendscoreandy.R

enum class HeartSize(val size: Dp) {
    SMALL(16.dp),
    MEDIUM(24.dp),
    STANDARD(32.dp),
    LARGE(48.dp)
}

private val Purple = Color(0xFF6A0DAD)
private val Green = Color(0xFF4CAF50)
private val BorderColor = Color(0xFF440F1C)

@Composable
fun HeartIcon(
    score: Float?,
    modifier: Modifier = Modifier,
    size: HeartSize = HeartSize.MEDIUM
) {
    val fraction = ((score ?: 0f) / 5f).coerceIn(0f, 1f)
    val fillColor = lerp(Purple, Green, fraction)
    val description = if (score != null) "Friendship score: ${"%.1f".format(score)}" else "No score"

    Box(
        modifier = modifier
            .size(size.size)
            .semantics { contentDescription = description }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_heart_border),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(BorderColor),
            modifier = Modifier.matchParentSize()
        )
        Image(
            painter = painterResource(id = R.drawable.ic_heart_fill),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(fillColor),
            modifier = Modifier.matchParentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeartIconNullPreview() {
    HeartIcon(score = null, size = HeartSize.LARGE)
}

@Preview(showBackground = true)
@Composable
private fun HeartIconZeroPreview() {
    HeartIcon(score = 0f, size = HeartSize.LARGE)
}

@Preview(showBackground = true)
@Composable
private fun HeartIconMidPreview() {
    HeartIcon(score = 2.5f, size = HeartSize.LARGE)
}

@Preview(showBackground = true)
@Composable
private fun HeartIconMaxPreview() {
    HeartIcon(score = 5.0f, size = HeartSize.LARGE)
}

@Preview(showBackground = true)
@Composable
private fun HeartIconAllSizesPreview() {
    Row {
        HeartSize.entries.forEach { heartSize ->
            HeartIcon(score = 4.0f, size = heartSize)
        }
    }
}
