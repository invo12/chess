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

        val friendlyPositions = getFriendlyPieces(currentPiece, pieces).map { it.getPosition() }
        val enemyPositions = getEnemyPieces(currentPiece, pieces).map { it.getPosition() }
        val currentPosition = currentPiece.getPosition()

        val allMoves = knightJumps.map { it + currentPosition }
        val positions = separateMovementAndCapturePositions(allMoves, friendlyPositions, enemyPositions)

        movePositions.addAll(positions.first)
        capturePositions.addAll(positions.second)
    }
}