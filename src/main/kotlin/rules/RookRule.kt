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

        val limitFunction = { it: Position -> it.x < 9 && it.x > -1 && it.y > -1 && it.y < 9 }

        val upIncrement = Position(0, 1)
        val rightIncrement = Position(1, 0)
        val downIncrement = Position(0, -1)
        val leftIncrement = Position(-1, 0)

        val allPositions = friendlyPositions + enemyPositions

        return getLineMoves(
            currentPosition,
            enemyPositions,
            allPositions,
            limitFunction,
            listOf(upIncrement, rightIncrement, downIncrement, leftIncrement)
        )
    }
}