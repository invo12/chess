package rules

import pieces.Piece
import pieces.Position

class QueenRule : Rule {

    private val bishopRule: Rule = BishopRule()
    private val rookRule: Rule = RookRule()

    override fun getValidPositions(
        currentPiece: Piece,
        friendlyPieces: List<Piece>,
        enemyPieces: List<Piece>
    ): Pair<List<Position>, List<Position>> {

        val (diagonalMovePositions, diagonalCapturePositions) =
            bishopRule.getValidPositions(currentPiece, friendlyPieces, enemyPieces)
        val (straightMovePositions, straightCapturePositions) =
            rookRule.getValidPositions(currentPiece, friendlyPieces, enemyPieces)
        return Pair(straightMovePositions + diagonalMovePositions, straightCapturePositions + diagonalCapturePositions)
    }
}