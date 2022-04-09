package rules

import pieces.Piece
import pieces.Position

class BlackPawnRule : Rule {
    override fun getValidPositions(
        currentPiece: Piece,
        friendlyPieces: List<Piece>,
        enemyPieces: List<Piece>
    ): Pair<List<Position>, List<Position>> {

        val currentPosition = currentPiece.getPosition()
        val friendlyPositions = friendlyPieces.map { it.getPosition() }
        val enemyPositions = enemyPieces.map { it.getPosition() }

        return Pair(
            getMovePositions(currentPosition, friendlyPositions, enemyPositions),
            getCapturePositions(currentPosition, enemyPieces)
        )
    }

    private fun getMovePositions(
        currentPosition: Position, friendlyPositions: List<Position>,
        enemyPositions: List<Position>
    ): List<Position> {

        val forwardPosition = currentPosition + Position(0, -1)
        val otherPiece =
            friendlyPositions.find { it == forwardPosition } ?: enemyPositions.find { it == forwardPosition }
        return if (otherPiece != null) {
            listOf()
        } else {
            val result = mutableListOf(forwardPosition)
            if (currentPosition.y == 7) {
                val doubleForwardPosition = currentPosition + Position(0, -2)
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
            if (enemyPieces.find { it.getPosition() == left }?.getLastPosition() == Position(left.x, 2))
                return listOf(Position(left.x, 3))
            else if (enemyPieces.find { it.getPosition() == right }?.getLastPosition() == Position(right.x, 2))
                return listOf(Position(right.x, 3))
            return listOf()
        }

        val lowerLeft = currentPosition + Position(-1, -1)
        val lowerRight = currentPosition + Position(1, -1)
        return enemyPieces
            .map { it.getPosition() }
            .filter { it == lowerLeft || it == lowerRight } + getEnPassantPositions()
    }
}