package com.app.desafiodourado.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.app.desafiodourado.theme.BlueDark
import com.app.desafiodourado.theme.BrowLight
import com.app.desafiodourado.theme.Success

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipsCustom(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    title: String,
    onClickChipListener: () -> Unit,
    containerColor: Color = BlueDark,
    selectedContainerColor: Color = Success,
    labelColor: Color = BrowLight,
    selectedLabelColor: Color = Color.White,
    selectedIconColor: Color = Color.White
) {
    FilterChip(
        modifier = modifier,
        onClick = onClickChipListener,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColor,
            selectedContainerColor = selectedContainerColor,
            labelColor = labelColor,
            selectedLabelColor = selectedLabelColor,
            selectedLeadingIconColor = selectedIconColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = containerColor
        ),
        label = {
            Text(text = title)
        },
        selected = isSelected,
        leadingIcon =  {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        },
    )
}

@Preview
@Composable
fun ChipsCustomPreview() {
    Column(Modifier.background(Color.White)) {
        ChipsCustom(
            isSelected = false,
            title = "teste",
            onClickChipListener = {}
        )
    }
}

@Preview
@Composable
fun ChipsCustomSelectedPreview() {
    Column(Modifier.background(Color.White)) {
        ChipsCustom(
            isSelected = true,
            title = "teste",
            onClickChipListener = {}
        )
    }
}