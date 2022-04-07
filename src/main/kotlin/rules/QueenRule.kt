package rules

import pieces.Position

class QueenRule : Rule {

    private val bishopRule: Rule = BishopRule()
    private val rookRule: Rule = RookRule()

    override fun getValidPositions(
        currentPosition: Position,
        friendlyPositions: List<Position>,
        enemyPositions: List<Position>
    ): Pair<List<Position>, List<Position>> {

        val (diagonalMovePositions, diagonalCapturePositions) =
            bishopRule.getValidPositions(currentPosition, friendlyPositions, enemyPositions)
        val (straightMovePositions, straightCapturePositions) =
            rookRule.getValidPositions(currentPosition, friendlyPositions, enemyPositions)
        return Pair(straightMovePositions + diagonalMovePositions, straightCapturePositions + diagonalCapturePositions)
    }
}