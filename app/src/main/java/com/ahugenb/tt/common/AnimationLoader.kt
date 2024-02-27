package com.ahugenb.tt.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AnimationLoader(res: Int, speed: Float) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(res))

    LottieAnimation(
        composition = composition,
        speed = speed,
        iterations = LottieConstants.IterateForever
    )
}