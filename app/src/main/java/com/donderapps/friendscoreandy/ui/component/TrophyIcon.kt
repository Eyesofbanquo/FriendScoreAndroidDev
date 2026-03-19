package com.donderapps.friendscoreandy.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donderapps.friendscoreandy.R
import com.donderapps.friendscoreandy.domain.model.Trophy

private val GoldColor = Color(0xFFFFD700)
private val SilverColor = Color(0xFFC0C0C0)
private val BronzeColor = Color(0xFFCD7F32)

@Composable
fun TrophyIcon(
    trophy: Trophy,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (trophy) {
        Trophy.GOLD -> GoldColor to "Gold trophy"
        Trophy.SILVER -> SilverColor to "Silver trophy"
        Trophy.BRONZE -> BronzeColor to "Bronze trophy"
    }

    Image(
        painter = painterResource(id = R.drawable.ic_trophy),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
            .size(24.dp)
            .semantics { contentDescription = label }
    )
}

@Preview(showBackground = true)
@Composable
private fun TrophyIconGoldPreview() {
    TrophyIcon(trophy = Trophy.GOLD)
}

@Preview(showBackground = true)
@Composable
private fun TrophyIconSilverPreview() {
    TrophyIcon(trophy = Trophy.SILVER)
}

@Preview(showBackground = true)
@Composable
private fun TrophyIconBronzePreview() {
    TrophyIcon(trophy = Trophy.BRONZE)
}

@Preview(showBackground = true)
@Composable
private fun TrophyIconAllPreview() {
    Row {
        Trophy.entries.forEach { trophy ->
            TrophyIcon(trophy = trophy)
        }
    }
}
