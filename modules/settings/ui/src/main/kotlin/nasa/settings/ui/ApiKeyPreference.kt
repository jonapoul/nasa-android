package nasa.settings.ui

import alakazam.android.ui.compose.OnDispose
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import nasa.core.model.SettingsKeys
import nasa.core.res.CoreDimens
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.textFieldDialog
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.text.NasaTextField
import nasa.core.ui.text.keyboardFocusRequester
import nasa.settings.res.SettingsStrings

@Composable
internal fun ApiKeyPreference(
  modifier: Modifier = Modifier,
  key: String = SettingsKeys.API_KEY,
  theme: Theme = LocalTheme.current,
  preferences: SharedPreferences = rememberSharedPreferences(),
) {
  var currentValue by remember { mutableStateOf(preferences.getString(key, null)) }
  val listener = remember {
    SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
      if (changedKey == key) {
        currentValue = preferences.getString(key, null)
      }
    }
  }
  LaunchedEffect(key) { preferences.registerOnSharedPreferenceChangeListener(listener) }
  OnDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }

  var showDialog by remember { mutableStateOf(false) }
  val prefColors = theme.preference(enabled = true)
  val title = SettingsStrings.keyTitle

  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(prefColors.background)
      .clickable { showDialog = true },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = Icons.Filled.Key,
      modifier = Modifier.padding(CoreDimens.large),
      contentDescription = title,
      tint = prefColors.foreground,
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .padding(top = CoreDimens.large, bottom = CoreDimens.large, end = CoreDimens.large),
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        maxLines = 1,
        color = prefColors.foreground,
      )

      val isSet = currentValue != null
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = if (isSet) SettingsStrings.keySet else SettingsStrings.keyNotSet,
        color = if (isSet) prefColors.subtitle else theme.errorText,
        lineHeight = 18.sp,
        fontSize = 14.sp,
      )
    }
  }

  if (showDialog) {
    ApiKeyPreferenceDialog(
      key = key,
      title = title,
      currentValue = currentValue,
      onDismiss = { showDialog = false },
      theme = theme,
      preferences = preferences,
    )
  }
}

@Composable
internal fun ApiKeyPreferenceDialog(
  key: String,
  title: String,
  currentValue: String?,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
  preferences: SharedPreferences = rememberSharedPreferences(),
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    content = {
      ApiKeyPreferenceDialogContent(
        key = key,
        title = title,
        currentValue = currentValue,
        onDismiss = onDismiss,
        theme = theme,
        preferences = preferences,
      )
    },
  )
}

@Composable
private fun ApiKeyPreferenceDialogContent(
  key: String,
  title: String,
  currentValue: String?,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
  preferences: SharedPreferences = rememberSharedPreferences(),
) {
  var mutableValue by remember { mutableStateOf(currentValue.orEmpty()) }
  val keyboard = LocalSoftwareKeyboardController.current

  DialogContent(
    modifier = modifier,
    theme = theme,
    title = title,
    content = {
      NasaTextField(
        modifier = Modifier.focusRequester(keyboardFocusRequester(keyboard)),
        value = mutableValue,
        onValueChange = { mutableValue = it },
        placeholderText = SettingsStrings.keyHint,
        theme = theme,
        colors = theme.textFieldDialog(),
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          capitalization = KeyboardCapitalization.None,
          autoCorrectEnabled = false,
          imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            preferences.edit { putString(key, mutableValue) }
            keyboard?.hide()
            onDismiss()
          },
        ),
      )
    },
    buttons = {
      TextButton(
        onClick = { onDismiss() },
        content = { Text(text = SettingsStrings.dialogCancel, color = theme.pageTextPrimary) },
      )
      TextButton(
        onClick = {
          preferences.edit { putString(key, mutableValue) }
          onDismiss()
        },
        content = { Text(text = SettingsStrings.dialogOk, color = theme.pageTextPrimary) },
      )
    },
  )
}

@Preview
@Composable
private fun DialogContentEmpty() = PreviewColumn {
  ApiKeyPreferenceDialogContent(
    key = "abc",
    title = "Edit Text Preference",
    currentValue = null,
    onDismiss = {},
  )
}

@Preview
@Composable
private fun DialogContentFilled() = PreviewColumn {
  ApiKeyPreferenceDialogContent(
    key = "abc",
    title = "Edit Text Preference",
    currentValue = "Some text being entered, also showing how it looks when wrapping lines",
    onDismiss = {},
  )
}

@Preview
@Composable
private fun RegularPreference() = PreviewColumn {
  ApiKeyPreference()
}
