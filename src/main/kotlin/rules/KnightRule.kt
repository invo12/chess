package rules

import pieces.Piece
import pieces.Position

class KnightRule : Rule {

    override fun getValidPositions(
        currentPiece: Piece,
        pieces: List<Piece>,
        movePositions: MutableList<Position>,
        capturePositions: MutableList<Position>
    ) {

        val friendlyPieces = getFriendlyPieces(currentPiece, pieces)
        val enemyPieces = getEnemyPieces(currentPiece, pieces)

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

        val positions = separateMovementAndCapturePositions(allMoves, friendlyPositions, enemyPositions)

        movePositions.addAll(positions.first)
        capturePositions.addAll(positions.second)
    }
}