package com.ahugenb.tt.common

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AnimationLoader(res: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(res))

    LottieAnimation(
        modifier = Modifier.size(80.dp),
        speed = 2f,
        composition = composition,
    )
}