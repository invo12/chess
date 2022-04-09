package rules

import pieces.Piece
import pieces.Position

class RookRule : Rule {
    override fun getValidPositions(
        currentPiece: Piece, friendlyPieces: List<Piece>,
        enemyPieces: List<Piece>
    ): Pair<List<Position>, List<Position>> {

        val currentPosition = currentPiece.getPosition()
        val friendlyPositions = friendlyPieces.map { it.getPosition() }
        val enemyPositions = enemyPieces.map { it.getPosition() }

        fun searchByCriteria(
            positions: List<Position>, xOp: (Int, Int) -> Boolean, yOp: (Int, Int) -> Boolean,
            isMin: Boolean, op: (Position) -> Int
        ): Position? {

            val filteredPositions = positions.filter { xOp(it.x, currentPosition.x) && yOp(it.y, currentPosition.y) }
            return if (isMin) {
                filteredPositions.minByOrNull(op)
            } else {
                filteredPositions.maxByOrNull(op)
            }
        }

        fun takeAllPositionsTillLimit(terminationFunction: (Position) -> Boolean, increment: Position): List<Position> {

            return generateSequence(currentPosition + increment) { it + increment }
                .takeWhile(terminationFunction)
                .toList()
        }

        val possiblePiecesToCollide =
            (friendlyPositions + enemyPositions).filter { it.x == currentPosition.x || it.y == currentPosition.y }

        val upLimit = searchByCriteria(possiblePiecesToCollide, { xOther, xPiece -> xOther == xPiece },
            { yOther, yPiece -> yOther > yPiece }, true, { it.y }) ?: Position(currentPosition.x, 9)
        val rightLimit = searchByCriteria(possiblePiecesToCollide, { xOther, xPiece -> xOther > xPiece },
            { yOther, yPiece -> yOther == yPiece }, true, { it.x }) ?: Position(9, currentPosition.y)
        val downLimit = searchByCriteria(possiblePiecesToCollide, { xOther, xPiece -> xOther == xPiece },
            { yOther, yPiece -> yOther < yPiece }, false, { it.y }) ?: Position(currentPosition.x, 0)
        val leftLimit = searchByCriteria(possiblePiecesToCollide, { xOther, xPiece -> xOther < xPiece },
            { yOther, yPiece -> yOther == yPiece }, false, { it.x }) ?: Position(0, currentPosition.y)

        val limits = listOf(upLimit, rightLimit, downLimit, leftLimit)
        val capturePositions = limits.filter { it in enemyPositions }
        val movePositions = listOf(
            takeAllPositionsTillLimit({ it.y < upLimit.y }, Position(0, 1)),
            takeAllPositionsTillLimit({ it.x < rightLimit.x }, Position(1, 0)),
            takeAllPositionsTillLimit({ it.y > downLimit.y }, Position(0, -1)),
            takeAllPositionsTillLimit({ it.x > leftLimit.x }, Position(-1, 0))
        ).flatten()

        return Pair(movePositions, capturePositions)
    }


}