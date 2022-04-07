package rules

import pieces.Position

class BishopRule : Rule {
    override fun getValidPositions(
        currentPosition: Position, friendlyPositions: List<Position>,
        enemyPositions: List<Position>
    ): Pair<List<Position>, List<Position>> {

        fun takeAllPositionsTillLimit(terminationFunction: (Position) -> Boolean, increment: Position): List<Position> {

            return generateSequence(currentPosition + increment) { it + increment }
                .takeWhile(terminationFunction)
                .toList()
        }

        val allPositions = friendlyPositions + enemyPositions
        val limitFunction = { it: Position -> it.x < 9 && it.x > -1 && it.y > -1 && it.y < 9 }

        val upLeftIncrement = Position(-1, 1)
        val upRightIncrement = Position(1, 1)
        val downRightIncrement = Position(1, -1)
        val downLeftIncrement = Position(-1, -1)

        val upLeftLimit = takeAllPositionsTillLimit(limitFunction, upLeftIncrement)
        val upRightLimit = takeAllPositionsTillLimit(limitFunction, upRightIncrement)
        val downRightLimit = takeAllPositionsTillLimit(limitFunction, downRightIncrement)
        val downLeftLimit = takeAllPositionsTillLimit(limitFunction, downLeftIncrement)

        val limits = listOf(
            upLeftLimit.find { position -> allPositions.find { it == position } != null },
            upRightLimit.find { position -> allPositions.find { it == position } != null },
            downRightLimit.find { position -> allPositions.find { it == position } != null },
            downLeftLimit.find { position -> allPositions.find { it == position } != null }
        )

        val capturePositions = limits.filter { it in enemyPositions }.filterNotNull()
        val movePositions = listOf(
            upLeftLimit.takeWhile { position -> allPositions.find { it == position } == null },
            upRightLimit.takeWhile { position -> allPositions.find { it == position } == null },
            downRightLimit.takeWhile { position -> allPositions.find { it == position } == null },
            downLeftLimit.takeWhile { position -> allPositions.find { it == position } == null }
        ).flatten()

        return Pair(movePositions, capturePositions)
    }
}