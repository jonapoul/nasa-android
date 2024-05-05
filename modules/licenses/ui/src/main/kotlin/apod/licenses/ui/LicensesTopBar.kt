package apod.licenses.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import apod.core.ui.color.Theme
import apod.core.ui.color.topAppBarColors
import apod.licenses.vm.LicensesAction

@Composable
internal fun LicensesTopBar(
  theme: Theme,
  onAction: (LicensesAction) -> Unit,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    navigationIcon = {
      IconButton(onClick = { onAction(LicensesAction.NavBack) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(id = R.string.licenses_toolbar_back),
        )
      }
    },
    title = {
      Text(
        text = stringResource(id = R.string.licenses_toolbar_title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
  )
}
