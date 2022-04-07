package rules

import pieces.Position

class KnightRule: Rule {
    override fun getValidPositions(
        currentPosition: Position,
        friendlyPositions: List<Position>,
        enemyPositions: List<Position>
    ): Pair<List<Position>, List<Position>> {

        val allMoves = listOf(
            currentPosition + Position(-2, 1),
            currentPosition + Position(-1, 2),
            currentPosition + Position(1, 2),
            currentPosition + Position(2, 1),
            currentPosition + Position(2, -1),
            currentPosition + Position(1, -2),
            currentPosition + Position(-1, -2),
            currentPosition + Position(-2, -1)
        )

        val validMoves = allMoves.filter { position -> friendlyPositions.find { position == it } == null }.toMutableList()
        val captureMoves = allMoves.filter { position -> enemyPositions.find { position == it } != null }
        validMoves.removeAll(captureMoves)
        return Pair(validMoves, captureMoves)
    }
}