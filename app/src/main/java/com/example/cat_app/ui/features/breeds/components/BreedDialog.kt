package com.example.cat_app.ui.features.breeds.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.cat_app.R
import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.ui.features.breeds.model.BreedUi

@Composable
fun BreedDialog(
    breed: BreedUi,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = breed.name,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Icon(
                        imageVector =
                            if (breed.isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint =
                            if (breed.isFavorite)
                                Color.Red
                            else
                                Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = breed.imageUrl.url,
                    contentDescription = breed.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    placeholder = painterResource(R.drawable.placehold_error),
                    error = painterResource(R.drawable.ic_launcher_cat_foreground),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                BreedInfoRow(
                    title = "Origin",
                    value = breed.origin
                )

                BreedInfoRow(
                    title = "Temperament",
                    value = breed.temperament
                )

                BreedInfoRow(
                    title = "Description",
                    value = breed.description
                )

                BreedInfoRow(
                    title = "Life span",
                    value = "${breed.lifeSpan} years"
                )

                BreedInfoRow(
                    title = "Weight",
                    value = "${breed.weight.metric} kg"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = onDismiss
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Close")
                }
            }
        }
    }
}