package com.example.ma7faza.ui.expenses.form.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    state: TextFieldState = rememberTextFieldState(),
    placeholder: String = "",
    singleLine: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        state = state,
        lineLimits = if (singleLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.MultiLine(
            2,
            3
        ),
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        leadingIcon = if (leadingIcon == null) null else {
            {
                Icon(leadingIcon, null)
            }
        },
        trailingIcon = if (trailingIcon == null) null else {
            {
                Icon(trailingIcon, null)
            }
        },
        placeholder = {
            Text(placeholder)
        },
        textStyle = textStyle,
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth()
    )
}