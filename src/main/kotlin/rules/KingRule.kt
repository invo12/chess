package rules

import pieces.Piece
import pieces.Position

class KingRule : Rule {

    override fun getValidPositions(
        currentPiece: Piece,
        pieces: List<Piece>,
        movePositions: MutableList<Position>,
        capturePositions: MutableList<Position>,
    ) {

        val friendlyPositions = getFriendlyPieces(currentPiece, pieces).map { it.getPosition() }
        val enemyPositions = getEnemyPieces(currentPiece, pieces).map { it.getPosition() }
        val currentPosition = currentPiece.getPosition()

        val allMoves = listOf(-1, 0, 1)
            .flatMap { f -> listOf(-1, 0, 1).map { Position(f, it) } }
            .filter { it != Position(0, 0) }
            .map { currentPosition + it }

        val positions = separateMovementAndCapturePositions(allMoves, friendlyPositions, enemyPositions)

        movePositions.addAll(positions.first)
        capturePositions.addAll(positions.second)
    }
}