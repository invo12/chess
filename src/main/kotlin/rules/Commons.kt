package rules

import pieces.Position

// for king and knight
fun separateMovementAndCapturePositions(
    allMoves: List<Position>, friendlyPositions: List<Position>,
    enemyPositions: List<Position>
): Pair<List<Position>, List<Position>> {

    val movementPositions =
        allMoves.filter { position -> friendlyPositions.find { position == it } == null }.toMutableList()
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