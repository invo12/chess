package rules

import pieces.Piece
import pieces.Position

class BlackPawnRule : Rule {
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

        movePositions.addAll(getMovePositionsForPawns(currentPosition, friendlyPositions, enemyPositions, false))
        capturePositions.addAll(getCapturePositions(currentPosition, enemyPieces, false))
    }
}