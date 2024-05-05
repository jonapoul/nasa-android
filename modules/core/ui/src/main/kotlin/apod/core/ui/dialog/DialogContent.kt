package apod.core.ui.dialog

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme

@Composable
fun DialogContent(
    title: String?,
    content: @Composable ColumnScope.() -> Unit,
    buttons: (@Composable RowScope.() -> Unit)?,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    theme: Theme = LocalTheme.current,
    titleColor: Color = theme.pageTextPrimary,
) {
  Column(
    modifier = modifier
      .wrapContentWidth()
      .background(theme.dialogBackground)
      .padding(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    Row(
      modifier = Modifier,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      icon?.let {
        Icon(
          imageVector = it,
          contentDescription = null,
          tint = titleColor,
        )

        HorizontalSpacer(10.dp)
      }

      title?.let {
        Text(
          modifier = Modifier.padding(vertical = 8.dp),
          text = title,
          color = titleColor,
        )
      }
    }

    CompositionLocalProvider(LocalContentColor provides theme.pageText) {
      content()
    }

    buttons?.let {
      CompositionLocalProvider(LocalContentColor provides theme.pageTextPrimary) {
        Row(
          modifier = Modifier.align(Alignment.End),
          content = buttons,
        )
      }
    }
  }
}
