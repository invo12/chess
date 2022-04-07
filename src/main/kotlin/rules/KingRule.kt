package rules

import pieces.Position

class KingRule : Rule {
    override fun getValidPositions(
        currentPosition: Position,
        friendlyPositions: List<Position>,
        enemyPositions: List<Position>,
    ): Pair<List<Position>, List<Position>> {

        val allMoves = listOf(-1, 0, 1)
            .flatMap { f -> listOf(-1, 0, 1).map { Position(f, it) } }
            .filter { it != Position(0, 0) }
            .map { currentPosition + it }

        val validMoves = allMoves.filter { position -> friendlyPositions.find { position == it } == null }.toMutableList()
        val captureMoves = allMoves.filter { position -> enemyPositions.find { position == it } != null }
        validMoves.removeAll(captureMoves)
        return Pair(validMoves, captureMoves)
    }
}