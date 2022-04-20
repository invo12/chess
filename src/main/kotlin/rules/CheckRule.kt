package rules

import pieces.Piece
import pieces.Position
import java.util.*
import kotlin.math.abs

class CheckRule : Rule {
    override fun getValidPositions(
        currentPiece: Piece,
        pieces: List<Piece>,
        movePositions: MutableList<Position>,
        capturePositions: MutableList<Position>
    ) {
        val king =
            pieces.find { it.hasTheSameColor(currentPiece) && it.getType().lowercase() == "k" }!!

        val positionsToRemove = mutableListOf<Position>()
        val oldPosition = currentPiece.getPosition()

        for (position in movePositions) {
            currentPiece.move(position)
            if (isChecked(king, pieces))
                positionsToRemove.add(position)
            currentPiece.move(oldPosition)
        }
        movePositions.removeAll { it in positionsToRemove }

        positionsToRemove.clear()
        for (position in capturePositions) {
            currentPiece.move(position)
            if (isChecked(king, pieces))
                positionsToRemove.add(position)
            currentPiece.move(oldPosition)
        }
        capturePositions.removeAll { it in positionsToRemove }
    }
}

fun isChecked(king: Piece, pieces: List<Piece>): Boolean {

    val kingPosition = king.getPosition()
    val friendlyPieces = pieces.filter { it.hasTheSameColor(king) }
    val enemyPieces = pieces.filter { !it.hasTheSameColor(king) }

    fun checkPieces(x: Int, y: Int, enemyPieces: List<Piece>): Int {

        val position = Position(x, y)
        val friendlyPiece = friendlyPieces.find { it.getPosition() == position }

        if (friendlyPiece != null) {
            return 0
        }

        val enemyPiece = enemyPieces.find { it.getPosition() == position }
        return if (enemyPiece != null) 1 else 2
    }

    fun checkStraight(): Boolean {

        val enemyStraightPieces = enemyPieces.filter {
            it.getType().lowercase(Locale.getDefault()) == "q" || it.getType().lowercase(Locale.getDefault()) == "r"
        }
        val verticalIntervals = listOf(kingPosition.y + 1..8, (1 until kingPosition.y).reversed())
        val horizontalIntervals = listOf(kingPosition.x + 1..8, (1 until kingPosition.x).reversed())

        for (interval in verticalIntervals) {
            for (y in interval) {
                when (checkPieces(kingPosition.x, y, enemyStraightPieces)) {
                    1 -> return true
                    0 -> break
                }
            }
        }

        for (interval in horizontalIntervals) {
            for (x in interval) {
                when (checkPieces(x, kingPosition.y, enemyStraightPieces)) {
                    1 -> return true
                    0 -> break
                }
            }
        }

        return false
    }

    fun checkDiagonals(): Boolean {

        val enemyDiagonalPieces = enemyPieces.filter {
            it.getType().lowercase(Locale.getDefault()) == "q" || it.getType().lowercase(Locale.getDefault()) == "b"
        }

        val plus = { a: Int, b: Int -> a + b }
        val minus = { a: Int, b: Int -> a - b }
        val operationsPairs = listOf(Pair(minus, plus), Pair(plus, plus), Pair(plus, minus), Pair(minus, minus))

        for (operationPair in operationsPairs) {

            for (i in 1..8) {

                val position = Position(
                    operationPair.first(kingPosition.x, i),
                    operationPair.second(kingPosition.y, i)
                )

                if (position.x < 1 || position.y < 1 || position.x > 8 || position.y > 8) break

                when (checkPieces(position.x, position.y, enemyDiagonalPieces)) {
                    1 -> return true
                    0 -> break
                }
            }
        }

        return false
    }

    fun checkPawns(): Boolean {

        val (leftPawnPosition, rightPawnPosition) = if (king.getType() == "K") {
            Pair(kingPosition + Position(-1, 1), kingPosition + Position(1, 1))
        } else {
            Pair(kingPosition + Position(-1, -1), kingPosition + Position(1, -1))
        }

        val enemyPawn = enemyPieces.find {
            it.getType() == "p"
                    && (it.getPosition() == leftPawnPosition || it.getPosition() == rightPawnPosition)
        }

        return enemyPawn != null
    }

    fun checkKing(): Boolean {

        val otherKingPosition = enemyPieces.find { it.getType().lowercase() == "k" }!!.getPosition()

        val distanceX = abs(otherKingPosition.x - kingPosition.x)
        val distanceY = abs(otherKingPosition.y - kingPosition.y)
        val distanceBetweenKings = distanceX + distanceY
        return distanceBetweenKings == 1 || (distanceBetweenKings == 2 && distanceX != 0 && distanceY != 0)
    }

    fun checkKnights(): Boolean {

        val allMoves = knightJumps.map { it + kingPosition }
        val enemyKnights = enemyPieces.filter { it.getType().lowercase(Locale.getDefault()) == "n" }

        return !allMoves.none { position -> enemyKnights.find { it.getPosition() == position } != null }
    }

    // check lines
    val isStraight = checkStraight()
    if (isStraight) return true

    // check diagonals
    val isDiagonal = checkDiagonals()
    if (isDiagonal) return true

    // check pawns
    val isPawns = checkPawns()
    if (isPawns) return true

    // check king
    val isKing = checkKing()
    if (isKing) return true

    // check knights
    val isKnight = checkKnights()
    if (isKnight) return true

    return false
}