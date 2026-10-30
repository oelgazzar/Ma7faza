package com.example.ma7faza.ui.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val sentiment_very_dissatisfied: ImageVector
  get() {
    if (_sentiment_very_dissatisfied != null) {
      return _sentiment_very_dissatisfied!!
    }
    _sentiment_very_dissatisfied =
      ImageVector.Builder(
          name = "sentiment_very_dissatisfied",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(8.91f, 14.46f)
            quadTo(7.53f, 15.43f, 6.9f, 17f)
            horizontalLineTo(17.1f)
            quadTo(16.48f, 15.43f, 15.09f, 14.46f)
            reflectiveQuadTo(12f, 13.5f)
            reflectiveQuadTo(8.91f, 14.46f)
            close()
            moveTo(7.8f, 12f)
            lineTo(8.9f, 10.95f)
            lineTo(9.95f, 12f)
            lineTo(11f, 10.95f)
            lineTo(9.95f, 9.9f)
            lineTo(11f, 8.8f)
            lineTo(9.95f, 7.75f)
            lineTo(8.9f, 8.8f)
            lineTo(7.8f, 7.75f)
            lineTo(6.75f, 8.8f)
            lineTo(7.8f, 9.9f)
            lineTo(6.75f, 10.95f)
            lineTo(7.8f, 12f)
            close()
            moveToRelative(6.25f, 0f)
            lineTo(15.1f, 10.95f)
            lineTo(16.2f, 12f)
            lineToRelative(1.05f, -1.05f)
            lineTo(16.2f, 9.9f)
            lineTo(17.25f, 8.8f)
            lineTo(16.2f, 7.75f)
            lineTo(15.1f, 8.8f)
            lineTo(14.05f, 7.75f)
            lineTo(13f, 8.8f)
            lineToRelative(1.05f, 1.1f)
            lineTo(13f, 10.95f)
            lineTo(14.05f, 12f)
            close()
            moveTo(8.1f, 21.21f)
            quadTo(6.28f, 20.43f, 4.93f, 19.08f)
            quadTo(3.58f, 17.73f, 2.79f, 15.9f)
            reflectiveQuadTo(2f, 12f)
            quadTo(2f, 9.92f, 2.79f, 8.1f)
            quadTo(3.58f, 6.27f, 4.93f, 4.93f)
            quadTo(6.28f, 3.57f, 8.1f, 2.79f)
            quadTo(9.93f, 2f, 12f, 2f)
            reflectiveQuadToRelative(3.9f, 0.79f)
            reflectiveQuadToRelative(3.17f, 2.14f)
            quadToRelative(1.35f, 1.35f, 2.14f, 3.17f)
            quadTo(22f, 9.92f, 22f, 12f)
            reflectiveQuadToRelative(-0.79f, 3.9f)
            reflectiveQuadToRelative(-2.14f, 3.17f)
            quadToRelative(-1.35f, 1.35f, -3.17f, 2.14f)
            reflectiveQuadTo(12f, 22f)
            quadTo(9.93f, 22f, 8.1f, 21.21f)
            close()
            moveTo(12f, 12f)
            close()
            moveToRelative(5.68f, 5.68f)
            quadTo(20f, 15.35f, 20f, 12f)
            reflectiveQuadTo(17.68f, 6.32f)
            reflectiveQuadTo(12f, 4f)
            reflectiveQuadTo(6.33f, 6.32f)
            reflectiveQuadTo(4f, 12f)
            reflectiveQuadToRelative(2.33f, 5.68f)
            reflectiveQuadTo(12f, 20f)
            reflectiveQuadToRelative(5.68f, -2.32f)
            close()
          }
        }
        .build()
    return _sentiment_very_dissatisfied!!
  }

private var _sentiment_very_dissatisfied: ImageVector? = null
