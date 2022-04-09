package rules

import pieces.Piece
import pieces.Position

class KnightRule : Rule {
    override fun getValidPositions(
        currentPiece: Piece,
        friendlyPieces: List<Piece>,
        enemyPieces: List<Piece>
    ): Pair<List<Position>, List<Position>> {

        val currentPosition = currentPiece.getPosition()
        val friendlyPositions = friendlyPieces.map { it.getPosition() }
        val enemyPositions = enemyPieces.map { it.getPosition() }

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

        val validMoves =
            allMoves.filter { position -> friendlyPositions.find { position == it } == null }.toMutableList()
        val captureMoves = allMoves.filter { position -> enemyPositions.find { position == it } != null }
        validMoves.removeAll(captureMoves)
        return Pair(validMoves, captureMoves)
    }
}