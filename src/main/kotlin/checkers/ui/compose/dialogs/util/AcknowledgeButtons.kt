package checkers.ui.compose.dialogs.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import checkers.model.moves.move.Player
import composables.DismissButton

@Composable
fun AcknowledgeButtons(
    gameName: String,
    selectedPlayer: Player,
    enable: Boolean,
    confirmButtonText: String,
    onConfirm: (gameName: String, selectedPlayer: Player) -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().absolutePadding(top = 5.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = { onConfirm(gameName, selectedPlayer) },
            enabled = enable,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) { Text(confirmButtonText) }
        DismissButton(
            onButtonText = "Cancel",
            enable = enable,
            onDismiss = onDismiss
        )
    }
}