package checkers.model.moves.move

/**
 * Represents the players of the game: [w] to identify the player with white checkers
 * and [b] for the player with black checkers.
 */
enum class Player { w, b; // ; marks the end of the constants
    /**
     * Method to retrieve the other [Player].
     */
    fun other() = if (this == w) b else w
    /**
     * Method to retrieve the [Player] label (name).
     * @return The defined string representation of this player.
     */
    fun label() =
        when(this) {
            w -> "White"
            b -> "Black"
        }
}
