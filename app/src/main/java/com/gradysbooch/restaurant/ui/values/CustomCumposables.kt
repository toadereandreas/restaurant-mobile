package com.gradysbooch.restaurant.ui.values

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.unit.dp
import java.lang.Exception
import kotlin.math.min


@Composable
fun RoundedButtonRowCard(
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
//                backgroundColor = color
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
fun RoundedButtonColumnCard(
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
//                backgroundColor = color
        ) {
            Column(
                    modifier = Modifier.padding(2.dp).fillMaxWidth()
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
fun RoundedColumnCard(
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
        Column(
                modifier = Modifier.padding(2.dp).fillMaxWidth(),
        ) {
            content()
        }
    }
}

@Composable
fun RoundedIconButton(
        border: BorderStroke? = null,
        modifier: Modifier = Modifier.padding(2.dp),
        color: Color = MaterialTheme.colors.onPrimary,
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


@Composable
fun RoundedSearchBar(
        text: MutableState<String>,
        placeholder: String = "search..."
) {
    RoundedRowCard(
            shape = RoundedCornerShape(20)
    ) {
        TextField(
                placeholder = { Text(text = placeholder) },
                value = text.value,
                onValueChange = { newText ->
                    text.value = newText
                })
        Icon(asset = Icons.Filled.Search, modifier = Modifier.padding(24.dp))
    }
}

fun getColorOr(string: String?, replacement: Color = Color.Red) : Color =
        try { Color(android.graphics.Color.parseColor(string!!))
        } catch (e: Exception) { replacement }


fun smartSubstring(string: String, maxLength: Int) : String {
    val limit = min(maxLength, string.length)
    return string.substring(0, limit) +
            if (limit < string.length) "..." else ""
}