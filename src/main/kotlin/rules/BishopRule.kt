package rules

import pieces.Piece
import pieces.Position

class BishopRule : Rule {
    override fun getValidPositions(
        currentPiece: Piece, friendlyPieces: List<Piece>,
        enemyPieces: List<Piece>
    ): Pair<List<Position>, List<Position>> {

        val currentPosition = currentPiece.getPosition()
        val friendlyPositions = friendlyPieces.map { it.getPosition() }
        val enemyPositions = enemyPieces.map { it.getPosition() }

        val allPositions = friendlyPositions + enemyPositions
        val limitFunction = { it: Position -> it.x < 9 && it.x > -1 && it.y > -1 && it.y < 9 }

        val upLeftIncrement = Position(-1, 1)
        val upRightIncrement = Position(1, 1)
        val downRightIncrement = Position(1, -1)
        val downLeftIncrement = Position(-1, -1)

        return getLineMoves(
            currentPosition,
            enemyPositions,
            allPositions,
            limitFunction,
            listOf(upLeftIncrement, upRightIncrement, downRightIncrement, downLeftIncrement)
        )
    }
}