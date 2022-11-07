package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.*

/**
 * Receives a variable amount of strings, each one indicating a square, and returns a list
 * of all given squares.
 */
fun squaresToListOf(vararg s: String) =
    s.fold(listOf<Square>()) { list, sqr ->
        list + validateSqr(sqr)
    }

/**
 * Retrieves the amount of regular checkers each player has at the start of the game,
 * according to the current board dimension: [BOARD_DIM].
 */
fun getPlayerPieces(dim: Int): Int = dim/2 * (dim/2 - 1)

/**
 * Receives a variable amount of strings, each one indicating a play, and plays them
 * on the board. If the strings are not in the expected format or if the squares they
 * represent are not valid then throws [IllegalArgumentException].
 * @return A new board updated with all the plays made.
 */
fun Board.plays(vararg s: String) =
    // Format expected example: "<Square1> <Square2>"
    s.fold(this) { board, string ->
        val list = string.split(" ")
        require(list.size == 2) { "Incorrect expected arguments" }
        val toSqr = validateSqr(list[0])
        val fromSqr = validateSqr(list[1])
        board.play(toSqr, fromSqr)
    }

/**
 * Validates if a string corresponds to a valid Square or throws
 * [IllegalArgumentException] if it doesn't.
 */
fun validateSqr(s: String): Square {
    val sqr = s.toSquareOrNull()
    requireNotNull(sqr) { "Square $sqr does not exist on the board" }
    return sqr
}