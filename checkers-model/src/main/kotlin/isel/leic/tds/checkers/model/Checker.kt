package isel.leic.tds.checkers.model

// Checker is a sealed class because this class can only two instances
sealed class Checker(val player: Player)

/**
 * Represents a regular checker on the board belonging to a [player]
 **/
class Piece (player: Player): Checker(player) {
    override fun toString() = player.name
}

/**
 * Represents a regular checker upgraded to King, which occurs when
 * it reaches the opponent's last row. This checker belongs to a [player]
 **/
class King(player: Player): Checker(player) {
    override fun toString() = player.name.uppercase()
}

