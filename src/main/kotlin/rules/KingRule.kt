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

        val friendlyPieces = getFriendlyPieces(currentPiece, pieces)
        val enemyPieces = getEnemyPieces(currentPiece, pieces)

        val currentPosition = currentPiece.getPosition()
        val friendlyPositions = friendlyPieces.map { it.getPosition() }
        val enemyPositions = enemyPieces.map { it.getPosition() }

        val allMoves = listOf(-1, 0, 1)
            .flatMap { f -> listOf(-1, 0, 1).map { Position(f, it) } }
            .filter { it != Position(0, 0) }
            .map { currentPosition + it }


        val positions = separateMovementAndCapturePositions(allMoves, friendlyPositions, enemyPositions)

        movePositions.addAll(positions.first)
        capturePositions.addAll(positions.second)
    }
}