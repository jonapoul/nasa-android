package nasa.licenses.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.licenses.res.LicensesStrings
import nasa.licenses.vm.SearchBarState

@Composable
internal fun LicensesTopBar(
  state: SearchBarState,
  theme: Theme,
  onAction: (LicensesAction) -> Unit,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    navigationIcon = {
      IconButton(onClick = { onAction(LicensesAction.NavBack) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = LicensesStrings.toolbarBack,
        )
      }
    },
    title = {
      Text(
        text = LicensesStrings.toolbarTitle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    actions = {
      IconButton(onClick = { onAction(LicensesAction.ToggleSearchBar) }) {
        Icon(
          imageVector = when (state) {
            SearchBarState.Gone -> Icons.Filled.Search
            is SearchBarState.Visible -> Icons.Filled.SearchOff
          },
          contentDescription = LicensesStrings.toolbarSearch,
        )
      }
    },
  )
}
