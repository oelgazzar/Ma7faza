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
public val receipt_long: ImageVector
  get() {
    if (_receipt_long != null) {
      return _receipt_long!!
    }
    _receipt_long =
      ImageVector.Builder(
          name = "receipt_long",
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
            moveTo(6f, 22f)
            quadTo(4.75f, 22f, 3.88f, 21.13f)
            reflectiveQuadTo(3f, 19f)
            verticalLineTo(16f)
            horizontalLineTo(6f)
            verticalLineTo(2f)
            lineTo(7.5f, 3.5f)
            lineTo(9f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(12f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(15f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(18f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(21f, 2f)
            verticalLineTo(19f)
            quadToRelative(0f, 1.25f, -0.88f, 2.13f)
            reflectiveQuadTo(18f, 22f)
            horizontalLineTo(6f)
            close()
            moveTo(18f, 20f)
            quadToRelative(0.43f, 0f, 0.71f, -0.29f)
            quadTo(19f, 19.43f, 19f, 19f)
            verticalLineTo(5f)
            horizontalLineTo(8f)
            verticalLineTo(16f)
            horizontalLineToRelative(9f)
            verticalLineToRelative(3f)
            quadToRelative(0f, 0.43f, 0.29f, 0.71f)
            reflectiveQuadTo(18f, 20f)
            close()
            moveTo(9f, 9f)
            verticalLineTo(7f)
            horizontalLineToRelative(6f)
            verticalLineTo(9f)
            horizontalLineTo(9f)
            close()
            moveToRelative(0f, 3f)
            verticalLineTo(10f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(2f)
            horizontalLineTo(9f)
            close()
            moveTo(17f, 9f)
            quadTo(16.58f, 9f, 16.29f, 8.71f)
            reflectiveQuadTo(16f, 8f)
            quadTo(16f, 7.57f, 16.29f, 7.29f)
            reflectiveQuadTo(17f, 7f)
            reflectiveQuadToRelative(0.71f, 0.29f)
            reflectiveQuadTo(18f, 8f)
            quadToRelative(0f, 0.42f, -0.29f, 0.71f)
            reflectiveQuadTo(17f, 9f)
            close()
            moveToRelative(0f, 3f)
            quadToRelative(-0.43f, 0f, -0.71f, -0.29f)
            quadTo(16f, 11.43f, 16f, 11f)
            reflectiveQuadToRelative(0.29f, -0.71f)
            reflectiveQuadTo(17f, 10f)
            reflectiveQuadToRelative(0.71f, 0.29f)
            reflectiveQuadTo(18f, 11f)
            reflectiveQuadToRelative(-0.29f, 0.71f)
            reflectiveQuadTo(17f, 12f)
            close()
            moveTo(6f, 20f)
            horizontalLineToRelative(9f)
            verticalLineTo(18f)
            horizontalLineTo(5f)
            verticalLineToRelative(1f)
            quadToRelative(0f, 0.43f, 0.29f, 0.71f)
            reflectiveQuadTo(6f, 20f)
            close()
            moveTo(5f, 20f)
            quadToRelative(0f, 0f, 0f, -0.29f)
            quadTo(5f, 19.43f, 5f, 19f)
            verticalLineTo(18f)
            verticalLineToRelative(2f)
            close()
          }
        }
        .build()
    return _receipt_long!!
  }

private var _receipt_long: ImageVector? = null
