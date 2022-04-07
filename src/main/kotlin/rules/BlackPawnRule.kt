package rules

import pieces.Position

class BlackPawnRule : Rule {
    override fun getValidPositions(
        currentPosition: Position,
        friendlyPositions: List<Position>,
        enemyPositions: List<Position>
    ): Pair<List<Position>, List<Position>> {

        return Pair(
            getMovePositions(currentPosition, friendlyPositions, enemyPositions),
            getCapturePositions(currentPosition, enemyPositions)
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

    private fun getCapturePositions(currentPosition: Position, enemyPositions: List<Position>): List<Position> {

        val lowerLeft = currentPosition + Position(-1, -1)
        val lowerRight = currentPosition + Position(1, -1)
        return enemyPositions.filter { it == lowerLeft || it == lowerRight }
    }
}