package com.gradysbooch.restaurant.ui.values

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.unit.dp


@Composable
fun RoundedButtonCard(
    border: BorderStroke? = null,
    modifier: Modifier = Modifier.padding(8.dp).fillMaxWidth(),
    color: Color = MaterialTheme.colors.surface,
    shape: Shape = CircleShape,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        border = border,
        modifier = modifier,
        shape = shape,
        color = color
    ) {
        Button(
                onClick = onClick,
                backgroundColor = color
        ) {
            Row(
                modifier = Modifier.padding(2.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                content()
            }
        }
    }
}

@Composable
fun RoundedRowCard(
    border: BorderStroke? = null,
    modifier: Modifier = Modifier.padding(8.dp).fillMaxWidth(),
    color: Color = MaterialTheme.colors.surface,
    shape: Shape = CircleShape,
    content: @Composable() () -> Unit
) {
    Surface(
            border = border,
            modifier = modifier,
            shape = shape,
            color = color
    ) {
        Row(
                modifier = Modifier.padding(2.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}

@Composable
fun RoundedIconButton(
    border: BorderStroke? = null,
    modifier: Modifier = Modifier.padding(2.dp),
    color: Color = MaterialTheme.colors.secondary,
    shape: Shape = CircleShape,
    asset: VectorAsset,
    tint: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    Surface(
            border = border,
            modifier = modifier,
            color = color,
            shape = shape
    ) {
        IconButton(onClick = onClick) {
            Icon(asset = asset, tint = tint)
        }
    }
}