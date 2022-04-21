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
        val friendlyPositions = friendlyPieces.map { it.getPosition() }
        val enemyPositions = getEnemyPieces(currentPiece, pieces).map { it.getPosition() }
        val currentPosition = currentPiece.getPosition()

        val allMoves = listOf(-1, 0, 1)
            .flatMap { f -> listOf(-1, 0, 1).map { Position(f, it) } }
            .filter { it != Position(0, 0) }
            .map { currentPosition + it }

        val positions = separateMovementAndCapturePositions(allMoves, friendlyPositions, enemyPositions)
        movePositions.addAll(positions.first)
        capturePositions.addAll(positions.second)

        if (!currentPiece.hasMoved()) {

            val y = currentPiece.getPosition().y
            val friendlyRooks = friendlyPieces.filter { it.getType().lowercase() == "r" }

            val leftRook = friendlyRooks.find { it.getPosition() == Position(1, y) }
            val positionsToBeFreeLeft = listOf(Position(2, y), Position(3, y), Position(4, y))
            if (leftRook != null && !leftRook.hasMoved()) {
                if (friendlyPieces.none { it.getPosition() in positionsToBeFreeLeft }) {
                    movePositions.add(Position(3, y))
                }
            }

            val rightRook = friendlyRooks.find { it.getPosition() == Position(8, y) }
            val positionsToBeFreeRight = listOf(Position(6, y), Position(7, y))
            if (rightRook != null && !rightRook.hasMoved()) {
                if (friendlyPieces.none { it.getPosition() in positionsToBeFreeRight }) {
                    movePositions.add(Position(7, y))
                }
            }
        }
    }
}