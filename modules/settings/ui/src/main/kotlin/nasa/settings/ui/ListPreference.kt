package nasa.settings.ui

import alakazam.android.ui.compose.OnDispose
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.radioButton
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn
import nasa.settings.res.R

@Composable
internal fun ListPreference(
  key: String,
  title: String,
  icon: ImageVector,
  defaultValue: String?,
  entries: ImmutableList<String>,
  entryValues: ImmutableList<String>,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
  enabled: Boolean = true,
  preferences: SharedPreferences = rememberSharedPreferences(),
) {
  require(defaultValue == null || defaultValue in entryValues) {
    "Default value $defaultValue is not in allowed options: $entryValues"
  }
  require(entries.size == entryValues.size) {
    "Mismatched input sizes: $entries vs $entryValues"
  }

  var currentValue by remember { mutableStateOf(preferences.getString(key, defaultValue)) }
  val listener = remember {
    SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
      if (changedKey == key) {
        currentValue = preferences.getString(key, defaultValue)
      }
    }
  }
  LaunchedEffect(key) { preferences.registerOnSharedPreferenceChangeListener(listener) }
  OnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }

  var showDialog by remember { mutableStateOf(false) }
  val prefColors = theme.preference(enabled)

  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(prefColors.background)
      .clickable(enabled = enabled) { showDialog = true },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = icon,
      modifier = Modifier.padding(16.dp),
      contentDescription = title,
      tint = prefColors.foreground,
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .padding(top = 8.dp, bottom = 8.dp, end = 16.dp),
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        maxLines = 1,
        color = prefColors.foreground,
      )

      Text(
        modifier = Modifier.fillMaxWidth(),
        text = entries.getOrNull(entryValues.indexOf(currentValue))
          ?: entries.getOrNull(entryValues.indexOf(defaultValue))
          ?: stringResource(id = R.string.settings_not_set),
        maxLines = 1,
        color = prefColors.subtitle,
        fontSize = 14.sp,
      )
    }
  }

  if (showDialog) {
    ListPreferenceDialog(
      key = key,
      title = title,
      selectedValue = currentValue ?: defaultValue,
      entries = entries,
      entryValues = entryValues,
      onDismiss = { showDialog = false },
      theme = theme,
      preferences = preferences,
    )
  }
}

@Composable
internal fun ListPreferenceDialog(
  key: String,
  title: String,
  selectedValue: String?,
  entries: ImmutableList<String>,
  entryValues: ImmutableList<String>,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
  preferences: SharedPreferences = rememberSharedPreferences(),
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    content = {
      ListPreferenceDialogContent(
        key = key,
        title = title,
        selectedValue = selectedValue,
        entries = entries,
        entryValues = entryValues,
        onDismiss = onDismiss,
        theme = theme,
        preferences = preferences,
      )
    },
  )
}

@Composable
private fun ListPreferenceDialogContent(
  key: String,
  title: String,
  selectedValue: String?,
  entries: ImmutableList<String>,
  entryValues: ImmutableList<String>,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
  preferences: SharedPreferences = rememberSharedPreferences(),
) {
  val entriesMap = entries.zip(entryValues)

  DialogContent(
    modifier = modifier,
    theme = theme,
    title = title,
    content = {
      LazyColumn {
        items(entriesMap) { (entry, value) ->
          ListPreferenceDialogItem(
            entry = entry,
            value = value,
            isSelected = value == selectedValue,
            theme = theme,
            onValueChange = { newValue ->
              preferences.edit { putString(key, newValue) }
              onDismiss()
            },
          )
        }
      }
    },
    buttons = {
      TextButton(onClick = onDismiss) {
        Text(
          text = stringResource(id = R.string.settings_dialog_cancel),
          color = theme.pageTextPrimary,
        )
      }
    },
  )
}

@Composable
private fun ListPreferenceDialogItem(
  entry: String,
  value: String,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
  onValueChange: (String) -> Unit,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onValueChange(value) },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    RadioButton(
      selected = isSelected,
      onClick = { onValueChange(value) },
      colors = theme.radioButton(),
    )

    Text(
      modifier = Modifier.fillMaxWidth(),
      text = entry,
      color = theme.pageText,
    )
  }
}

@Preview
@Composable
private fun ListSet() = PreviewColumn {
  ListPreference(
    key = "abc",
    icon = Icons.Filled.Search,
    title = "List Preference",
    defaultValue = "c",
    entries = persistentListOf("Alpha", "Bravo", "Charlie"),
    entryValues = persistentListOf("a", "b", "c"),
  )
}

@Preview
@Composable
private fun ListUnset() = PreviewColumn {
  ListPreference(
    key = "abc",
    icon = Icons.Filled.Search,
    title = "List Preference",
    defaultValue = null,
    entries = persistentListOf("Alpha", "Bravo", "Charlie"),
    entryValues = persistentListOf("a", "b", "c"),
  )
}

@Preview
@Composable
private fun ListDisabled() = PreviewColumn {
  ListPreference(
    key = "abc",
    icon = Icons.Filled.Search,
    title = "List Preference",
    defaultValue = null,
    entries = persistentListOf("Alpha", "Bravo", "Charlie"),
    entryValues = persistentListOf("a", "b", "c"),
    enabled = false,
  )
}

@Preview
@Composable
private fun DialogContentNotSelected() = PreviewColumn {
  ListPreferenceDialogContent(
    key = "abc",
    title = "List Preference",
    selectedValue = null,
    entries = persistentListOf("Alpha", "Bravo", "Charlie"),
    entryValues = persistentListOf("a", "b", "c"),
    onDismiss = {},
  )
}

@Preview
@Composable
private fun DialogContentSelected() = PreviewColumn {
  ListPreferenceDialogContent(
    key = "abc",
    title = "List Preference",
    selectedValue = "b",
    entries = persistentListOf("Alpha", "Bravo", "Charlie"),
    entryValues = persistentListOf("a", "b", "c"),
    onDismiss = {},
  )
}
