package nasa.home.ui

import alakazam.android.ui.compose.HorizontalSpacer
import alakazam.kotlin.core.noOp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn
import nasa.home.res.R

@Composable
internal fun ApiUsageDialog(
  state: ApiUsageState,
  theme: Theme,
  onDismiss: () -> Unit,
  onClickRegister: () -> Unit,
  modifier: Modifier = Modifier,
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    content = {
      ApiUsageDialogContent(
        state = state,
        theme = theme,
        onDismiss = onDismiss,
        onClickRegister = onClickRegister,
      )
    },
  )
}

@Composable
private fun ApiUsageDialogContent(
  state: ApiUsageState,
  onDismiss: () -> Unit,
  onClickRegister: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  DialogContent(
    modifier = modifier,
    theme = theme,
    icon = Icons.Filled.Api,
    title = stringResource(id = R.string.home_usage_dialog_title),
    content = {
      ApiUsageDialogContentImpl(
        state = state,
        onClickRegister = onClickRegister,
      )
    },
    buttons = {
      TextButton(onClick = onDismiss) {
        Text(
          text = stringResource(id = R.string.home_usage_dialog_ok),
          color = theme.pageTextPrimary,
        )
      }
    },
  )
}

@Composable
private fun ApiUsageDialogContentImpl(
  state: ApiUsageState,
  onClickRegister: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    val warningModifier = Modifier.fillMaxWidth()
    when (state) {
      ApiUsageState.NoApiKey -> NoKey(onClickRegister, warningModifier)
      is ApiUsageState.HasDemoKey -> HasDemoKey(onClickRegister, warningModifier)
      is ApiUsageState.NoUsage -> NoUsage(warningModifier)
      else -> noOp()
    }

    if (state is ApiUsageState.HasUsage) {
      ApiUsageTable(
        state = state,
      )
    }
  }
}

@Composable
private fun NoKey(
  onClickRegister: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = stringResource(id = R.string.home_usage_dialog_no_key))
    RegisterButton(onClickRegister)
  }
}

@Composable
private fun HasDemoKey(
  onClickRegister: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = stringResource(id = R.string.home_usage_dialog_demo_key))
    RegisterButton(onClickRegister)
  }
}

@Composable
private fun NoUsage(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
  ) {
    Text(text = stringResource(id = R.string.home_usage_dialog_no_usage))
  }
}

@Composable
private fun RegisterButton(
  onClickRegister: () -> Unit,
  modifier: Modifier = Modifier,
) {
  PrimaryTextButton(
    modifier = modifier,
    text = stringResource(id = R.string.home_usage_dialog_register),
    onClick = onClickRegister,
  )
}

@Composable
private fun ApiUsageTable(
  state: ApiUsageState.HasUsage,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    ApiUsageRow(
      title = stringResource(R.string.home_usage_dialog_remaining),
      value = state.remaining.toString(),
    )
    ApiUsageRow(
      title = stringResource(R.string.home_usage_dialog_limit),
      value = state.upperLimit.toString(),
    )
  }
}

@Composable
fun ApiUsageRow(
  title: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
  ) {
    Text(
      modifier = Modifier.weight(1f),
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.End,
      text = title,
    )

    HorizontalSpacer(32.dp)

    Text(
      modifier = Modifier.weight(1f),
      text = value,
      textAlign = TextAlign.Start,
    )
  }
}

@Preview
@Composable
private fun PreviewNoKey() = PreviewColumn {
  ApiUsageDialogContent(
    state = ApiUsageState.NoApiKey,
    onDismiss = {},
    onClickRegister = {},
  )
}

@Preview
@Composable
private fun PreviewDemoKeyNoUsage() = PreviewColumn {
  ApiUsageDialogContent(
    state = ApiUsageState.DemoKeyNoUsage,
    onDismiss = {},
    onClickRegister = {},
  )
}

@Preview
@Composable
private fun PreviewRealKeyNoUsage() = PreviewColumn {
  ApiUsageDialogContent(
    state = ApiUsageState.RealKeyNoUsage,
    onDismiss = {},
    onClickRegister = {},
  )
}

@Preview
@Composable
private fun PreviewDemoKeyWithUsage() = PreviewColumn {
  ApiUsageDialogContent(
    state = ApiUsageState.DemoKeyHasUsage(remaining = 69, upperLimit = 420),
    onDismiss = {},
    onClickRegister = {},
  )
}

@Preview
@Composable
private fun PreviewRealKeyWithUsage() = PreviewColumn {
  ApiUsageDialogContent(
    state = ApiUsageState.RealKeyHasUsage(remaining = 69, upperLimit = 420),
    onDismiss = {},
    onClickRegister = {},
  )
}
