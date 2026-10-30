package com.example.test

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val attach_money: ImageVector
  get() {
    if (_attach_money != null) {
      return _attach_money!!
    }
    _attach_money =
      ImageVector.Builder(
          name = "attach_money",
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
            moveTo(11.03f, 21f)
            verticalLineTo(18.85f)
            quadTo(9.7f, 18.55f, 8.74f, 17.7f)
            reflectiveQuadTo(7.33f, 15.3f)
            lineTo(9.18f, 14.55f)
            quadToRelative(0.38f, 1.2f, 1.11f, 1.83f)
            reflectiveQuadTo(12.23f, 17f)
            quadToRelative(1.02f, 0f, 1.74f, -0.46f)
            reflectiveQuadTo(14.68f, 15.1f)
            quadToRelative(0f, -0.88f, -0.55f, -1.39f)
            reflectiveQuadTo(11.58f, 12.55f)
            quadTo(9.43f, 11.88f, 8.63f, 10.94f)
            quadTo(7.83f, 10f, 7.83f, 8.65f)
            quadToRelative(0f, -1.63f, 1.05f, -2.52f)
            quadTo(9.93f, 5.22f, 11.03f, 5.1f)
            verticalLineTo(3f)
            horizontalLineToRelative(2f)
            verticalLineTo(5.1f)
            quadToRelative(1.25f, 0.2f, 2.06f, 0.91f)
            reflectiveQuadToRelative(1.19f, 1.74f)
            lineToRelative(-1.85f, 0.8f)
            quadToRelative(-0.3f, -0.8f, -0.85f, -1.2f)
            reflectiveQuadToRelative(-1.5f, -0.4f)
            quadToRelative(-1.1f, 0f, -1.68f, 0.49f)
            quadTo(9.83f, 7.93f, 9.83f, 8.65f)
            quadToRelative(0f, 0.83f, 0.75f, 1.3f)
            reflectiveQuadToRelative(2.6f, 1f)
            quadToRelative(1.72f, 0.5f, 2.61f, 1.59f)
            reflectiveQuadToRelative(0.89f, 2.51f)
            quadToRelative(0f, 1.78f, -1.05f, 2.7f)
            reflectiveQuadToRelative(-2.6f, 1.15f)
            verticalLineTo(21f)
            horizontalLineToRelative(-2f)
            close()
          }
        }
        .build()
    return _attach_money!!
  }

private var _attach_money: ImageVector? = null
