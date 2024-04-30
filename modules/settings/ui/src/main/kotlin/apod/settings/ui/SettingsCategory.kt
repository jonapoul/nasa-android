package apod.settings.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToQueue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewColumn

@Composable
internal fun SettingsCategory(
  icon: ImageVector,
  title: String,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
      imageVector = icon,
      contentDescription = title,
      tint = theme.pageTextPositive,
    )

    Text(
      modifier = Modifier.weight(1f),
      text = title,
      fontSize = 16.sp,
      color = theme.pageTextPositive,
    )
  }
}

@Preview
@Composable
private fun PreviewCategory() = PreviewColumn {
  SettingsCategory(
    title = "List Preference",
    icon = Icons.Filled.AddToQueue,
  )
}
