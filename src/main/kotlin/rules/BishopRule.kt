package rules

import pieces.Piece
import pieces.Position

class BishopRule : Rule {
    override fun getValidPositions(
        currentPiece: Piece,
        pieces: List<Piece>,
        movePositions: MutableList<Position>,
        capturePositions: MutableList<Position>
    ) {

        val friendlyPositions = getFriendlyPieces(currentPiece, pieces).map { it.getPosition() }
        val enemyPositions = getEnemyPieces(currentPiece, pieces).map { it.getPosition() }
        val currentPosition = currentPiece.getPosition()

        val allPositions = friendlyPositions + enemyPositions
        val limitFunction = { it: Position -> it.x < 9 && it.x > -1 && it.y > -1 && it.y < 9 }

        val upLeftIncrement = Position(-1, 1)
        val upRightIncrement = Position(1, 1)
        val downRightIncrement = Position(1, -1)
        val downLeftIncrement = Position(-1, -1)

        val positions = getLineMoves(
            currentPosition, enemyPositions, allPositions, limitFunction,
            listOf(upLeftIncrement, upRightIncrement, downRightIncrement, downLeftIncrement)
        )

        movePositions.addAll(positions.first)
        capturePositions.addAll(positions.second)
    }
}