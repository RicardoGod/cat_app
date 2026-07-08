package com.example.cat_app.ui.features.breeds.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun BreedInfoRow(
    title: String,
    value: String
){
    Text(
        buildAnnotatedString {

            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("$title: ")
            }

            append(value)
        }
    )

    Spacer(
        modifier = Modifier.height(6.dp)
    )
}