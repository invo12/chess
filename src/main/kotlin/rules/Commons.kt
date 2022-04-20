package rules

import pieces.Piece
import pieces.Position

val knightJumps = listOf(
    Position(-2, 1), Position(-1, 2), Position(1, 2), Position(2, 1),
    Position(2, -1), Position(1, -2), Position(-1, -2), Position(-2, -1)
)

// for pawns
fun getMovePositionsForPawns(
    currentPosition: Position, friendlyPositions: List<Position>,
    enemyPositions: List<Position>, up: Boolean
): List<Position> {

    val forwardPosition = currentPosition + (if (up) Position(0, 1) else Position(0, -1))
    val otherPiece =
        friendlyPositions.find { it == forwardPosition } ?: enemyPositions.find { it == forwardPosition }
    return if (otherPiece != null) {
        listOf()
    } else {
        val result = mutableListOf(forwardPosition)
        if (currentPosition.y == (if (up) 2 else 7)) {
            val doubleForwardPosition = currentPosition + (if (up) Position(0, 2) else Position(0, -2))
            val other =
                friendlyPositions.find { it == doubleForwardPosition }
                    ?: enemyPositions.find { it == doubleForwardPosition }
            if (other == null)
                result.add(doubleForwardPosition)
        }
        result.toList()
    }
}

// for pawns
fun getCapturePositions(currentPosition: Position, enemyPieces: List<Piece>, up: Boolean): List<Position> {

    fun getEnPassantPositions(): List<Position> {

        val left = currentPosition + Position(-1, 0)
        val right = currentPosition + Position(1, 0)
        if (enemyPieces.find { it.getPosition() == left }?.getLastPosition() == Position(left.x, if (up) 7 else 2))
            return listOf(Position(left.x, if (up) 6 else 3))
        else if (enemyPieces.find { it.getPosition() == right }?.getLastPosition() == Position(
                right.x,
                if (up) 7 else 2
            )
        )
            return listOf(Position(right.x, if (up) 6 else 3))
        return listOf()
    }

    val upperLeft = currentPosition + (if (up) Position(-1, 1) else Position(-1, -1))
    val upperRight = currentPosition + (if (up) Position(1, 1) else Position(1, -1))
    return enemyPieces
        .map { it.getPosition() }
        .filter { it == upperLeft || it == upperRight } + getEnPassantPositions()

}

// for king and knight
fun separateMovementAndCapturePositions(
    allMoves: List<Position>, friendlyPositions: List<Position>,
    enemyPositions: List<Position>
): Pair<List<Position>, List<Position>> {

    val movementPositions = allMoves
        .filter { position -> friendlyPositions.find { position == it } == null }
        .filter { it.x in 1..8 && it.y in 1..8 }.toMutableList()
    val captureMoves = allMoves.filter { position -> enemyPositions.find { position == it } != null }
    movementPositions.removeAll(captureMoves)

    return Pair(movementPositions, captureMoves)
}

fun takeAllPositionsTillLimit(
    currentPosition: Position,
    terminationFunction: (Position) -> Boolean,
    increment: Position
): List<Position> {

    return generateSequence(currentPosition + increment) { it + increment }
        .takeWhile(terminationFunction)
        .toList()
}

fun getLineMoves(
    currentPosition: Position,
    enemyPositions: List<Position>,
    allPositions: List<Position>,
    limitFunction: (Position) -> Boolean,
    increments: List<Position>
): Pair<List<Position>, List<Position>> {

    assert(increments.size == 4)
    val maxLimitLists = increments.map { takeAllPositionsTillLimit(currentPosition, limitFunction, it) }
    val limits = maxLimitLists
        .map { limitList -> limitList.find { position -> allPositions.find { it == position } != null } }

    val capturePositions = limits.filter { it in enemyPositions }.filterNotNull()
    val movePositions = maxLimitLists
        .map { limitList -> limitList.takeWhile { position -> allPositions.find { it == position } == null } }
        .flatten()

    return Pair(movePositions, capturePositions)
}

fun getFriendlyPieces(currentPiece: Piece, pieces: List<Piece>): List<Piece> {
    return pieces.filter { it.hasTheSameColor(currentPiece) }
}

fun getEnemyPieces(currentPiece: Piece, pieces: List<Piece>): List<Piece> {
    return pieces.filter { !it.hasTheSameColor(currentPiece) }
}