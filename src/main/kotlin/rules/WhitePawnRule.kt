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

        val friendlyPieces = getFriendlyPieces(currentPiece, pieces)
        val enemyPieces = getEnemyPieces(currentPiece, pieces)

        val currentPosition = currentPiece.getPosition()
        val friendlyPositions = friendlyPieces.map { it.getPosition() }
        val enemyPositions = enemyPieces.map { it.getPosition() }

        movePositions.addAll(getMovePositions(currentPosition, friendlyPositions, enemyPositions))
        capturePositions.addAll(getCapturePositions(currentPosition, enemyPieces))
    }

    private fun getMovePositions(
        currentPosition: Position, friendlyPositions: List<Position>,
        enemyPositions: List<Position>
    ): List<Position> {

        val forwardPosition = currentPosition + Position(0, 1)
        val otherPiece =
            friendlyPositions.find { it == forwardPosition } ?: enemyPositions.find { it == forwardPosition }
        return if (otherPiece != null) {
            listOf()
        } else {
            val result = mutableListOf(forwardPosition)
            if (currentPosition.y == 2) {
                val doubleForwardPosition = currentPosition + Position(0, 2)
                val other =
                    friendlyPositions.find { it == doubleForwardPosition }
                        ?: enemyPositions.find { it == doubleForwardPosition }
                if (other == null)
                    result.add(doubleForwardPosition)
            }
            result.toList()
        }
    }

    private fun getCapturePositions(currentPosition: Position, enemyPieces: List<Piece>): List<Position> {

        fun getEnPassantPositions(): List<Position> {

            val left = currentPosition + Position(-1, 0)
            val right = currentPosition + Position(1, 0)
            if (enemyPieces.find { it.getPosition() == left }?.getLastPosition() == Position(left.x, 7))
                return listOf(Position(left.x, 6))
            else if (enemyPieces.find { it.getPosition() == right }?.getLastPosition() == Position(right.x, 7))
                return listOf(Position(right.x, 6))
            return listOf()
        }

        val upperLeft = currentPosition + Position(-1, 1)
        val upperRight = currentPosition + Position(1, 1)
        return enemyPieces
            .map { it.getPosition() }
            .filter { it == upperLeft || it == upperRight } + getEnPassantPositions()

    }
}