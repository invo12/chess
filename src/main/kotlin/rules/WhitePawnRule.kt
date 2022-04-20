package rules

import pieces.Piece
import pieces.Position

class WhitePawnRule : Rule {
    override fun getValidPositions(
        currentPiece: Piece,
        pieces: List<Piece>,
        movePositions: MutableList<Position>,
        capturePositions: MutableList<Position>
    ) {

        val enemyPieces = getEnemyPieces(currentPiece, pieces)

        val friendlyPositions = getFriendlyPieces(currentPiece, pieces).map { it.getPosition() }
        val enemyPositions = enemyPieces.map { it.getPosition() }
        val currentPosition = currentPiece.getPosition()

        movePositions.addAll(getMovePositionsForPawns(currentPosition, friendlyPositions, enemyPositions, true))
        capturePositions.addAll(getCapturePositions(currentPosition, enemyPieces, true))
    }
}