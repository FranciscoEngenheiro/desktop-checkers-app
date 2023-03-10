package checkers.model

import checkers.model.board.Board
import checkers.model.board.BoardRun
import checkers.model.board.initialBoard
import checkers.model.board.util.play
import checkers.model.moves.move.Player
import checkers.model.moves.move.Square
import checkers.storage.BoardStorage

/**
 * Represents a game instance.
 * @param id Game unique identifier.
 * @param localPlayer Player assigned to this game.
 * @param board Current state of the board.
 */
data class Game(val id: String, val localPlayer: Player, val board: Board)

/**
 * Assigns a player to the white checkers if they are the first to enter the game,
 * or the black checkers if they are the second, and begins the game with a given [id].
 * If the storage didn't include a board or if more than one white checker has been changed
 * from its initial starting position in an existing game, a new game is constructed
 * from scratch.
 * @param id Game unique identifier.
 * @param storage Storage where the game data will be stored.
 */
suspend fun createGame(
    id: String,
    storage: BoardStorage
): Game {
    // Check if a board is in the specified storage
    val board = storage.read(id)
    // If the storage has a board:
    if (board != null) {
        // Check if the already created board has a maximum of one move done
        // by the first player to enter
        if (board is BoardRun && board.numberOfMoves in 0..1) {
            // If it does, the game will assign the second player
            return Game(id, Player.b, board)
        } else {
            // Delete the previous board from the storage
            storage.delete(id)
        }
    }
    // Create a new game
    return Game(id, Player.w, initialBoard()).also {
        // Create a storage to store the new board
        storage.create(id, initialBoard())
    }
}

/**
 * Resumes a game identified by an [id] with a given [localPlayer].
 * @param id Game unique identifier.
 * @param localPlayer PLayer to join the game.
 * @param storage Storage where the game data is.
 * @throws [IllegalArgumentException] If the game does not exist.
 */
suspend fun resumeGame(
    id: String,
    localPlayer: Player,
    storage: BoardStorage
): Game {
    // Check if a board is in the specified storage
    val newBoard = storage.read(id)
    requireNotNull(newBoard) { "Game with id $id does not exist." }
    // If the storage has a previously created board, the game will resume with
    // the specified player
    return Game(id, localPlayer, newBoard)
}

/**
 * Ensures the correct requirements are in order before calling [Board.play].
 * @param fromSqr Square to move a checker from.
 * @param toSqr Square to move a checker to.
 * @param storage Storage where the game data is.
 * @return A new board on the respective storage and a copy of the game with the
 * updated board.
 * @throws [IllegalStateException] If the board is not of [BoardRun] type or
 * if the current board turn does not belong to the player trying to make a move.
 */
suspend fun Game.play(
    fromSqr: Square,
    toSqr: Square,
    storage: BoardStorage
): Game {
    check(board is BoardRun) { "Game is over." }
    check(localPlayer == board.turn) { "Not your turn." }
    // Create a new board with the recent play
    val newBoard = board.play(fromSqr, toSqr)
    // Update the board in the storage
    storage.update(this@play.id, newBoard)
    // Replace the old board with the new board
    return copy(board = newBoard)
}