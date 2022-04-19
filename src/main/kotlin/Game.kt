import graphics.Graphics
import pieces.Piece
import pieces.Position
import rules.*
import java.util.*
import kotlin.math.abs

typealias TurnColor = Boolean

class Game(private val graphics: Graphics, private val pieces: MutableList<Piece>) {

    private val whiteKing = pieces.find { it.getType() == "K" }!!
    private val blackKing = pieces.find { it.getType() == "k" }!!
    private var selectedPiece: Piece? = null
    private var turn: TurnColor = true

    private val movementRules = mapOf(
        "p" to listOf(BlackPawnRule()),
        "P" to listOf(WhitePawnRule()),
        "r" to listOf(RookRule()),
        "R" to listOf(RookRule()),
        "b" to listOf(BishopRule()),
        "B" to listOf(BishopRule()),
        "q" to listOf(BishopRule(), RookRule()),
        "Q" to listOf(BishopRule(), RookRule()),
        "n" to listOf(KnightRule()),
        "N" to listOf(KnightRule()),
        "k" to listOf(KingRule()),
        "K" to listOf(KingRule()),
    )

    private fun getMoves(piece: Piece): Pair<List<Position>, List<Position>> {

        val rules = movementRules[piece.getType()]
        if (rules != null) {
            val movePositions = mutableListOf<Position>()
            val capturePositions = mutableListOf<Position>()
            for (rule in rules) {
                rule.getValidPositions(selectedPiece!!, pieces, movePositions, capturePositions)
            }
            return Pair(movePositions, capturePositions)
        }
        return Pair(listOf(), listOf())
    }

    private fun nextTurn() {
        turn = !turn
    }

    fun notify(x: Int, y: Int) {

        fun resolvePiece(piece: Piece) {
            if (piece.hasTurnColor(turn)) {
                selectedPiece = piece
                val moves = getMoves(piece)
                graphics.showMoves(moves.first + moves.second)
            } else {
                val (_, captures) = getMoves(selectedPiece!!)
                val capture = captures.find { it == Position(x, y) }
                if (capture != null) {
                    pieces.remove(piece)
                    selectedPiece!!.move(Position(x, y))
                    if (selectedPiece!!.getType() == "P" && selectedPiece!!.getPosition().y == 8) {
                        selectedPiece!!.setType("Q")
                    } else if (selectedPiece!!.getType() == "p" && selectedPiece!!.getPosition().y == 1) {
                        selectedPiece!!.setType("q")
                    }
                    graphics.updatePieces(pieces)
                    graphics.showMoves(listOf())
                    selectedPiece = null
                    nextTurn()
                }
            }
        }

        fun resolveMovePositions() {
            val (moves, captures) = selectedPiece!!.let {
                val moves = getMoves(it)
                Pair(moves.first, moves.second)
            }
            val positionToMove = moves.find { it.x == x && it.y == y }
            if (positionToMove == null) {
                // en passant
                if (selectedPiece!!.getType().lowercase(Locale.getDefault()) == "p") {
                    val capturePosition = captures.find { it.x == x && it.y == y }
                    if (capturePosition != null) {
                        if (selectedPiece!!.getType() == "p") {
                            val piece = pieces.find { it.getPosition() == Position(x, y + 1) }
                            pieces.remove(piece)
                            selectedPiece!!.move(Position(x, y))
                            graphics.updatePieces(pieces)
                            graphics.showMoves(listOf())
                            selectedPiece = null
                        } else if (selectedPiece!!.getType() == "P") {
                            val piece = pieces.find { it.getPosition() == Position(x, y - 1) }
                            pieces.remove(piece)
                            selectedPiece!!.move(Position(x, y))
                            graphics.updatePieces(pieces)
                            graphics.showMoves(listOf())
                            selectedPiece = null
                        }
                        nextTurn()
                        println(pieces.size)
                    }
                }
                graphics.showMoves(listOf())
            } else {
                selectedPiece?.move(Position(x, y))
                graphics.updatePieces(pieces)
                println(isChecked())
                nextTurn()
            }
            if (selectedPiece!!.getType() == "P" && selectedPiece!!.getPosition().y == 8) {
                selectedPiece!!.setType("Q")
            } else if (selectedPiece!!.getType() == "p" && selectedPiece!!.getPosition().y == 1) {
                selectedPiece!!.setType("q")
            }
            selectedPiece = null
        }

        val piece = pieces.find { it.getPosition() == Position(x, y) }
        if (selectedPiece != null) {
            if (piece != null) {
                resolvePiece(piece)
            } else {
                resolveMovePositions()
            }
        } else {
            if (piece != null && piece.hasTurnColor(turn)) {
                selectedPiece = piece
                val moves = getMoves(piece)
                graphics.showMoves(moves.first + moves.second)
            }
        }
    }

    fun start() {
        turn = true
    }

    fun isChecked(): Boolean {

        val currentKing = if (turn) blackKing else whiteKing
        val kingPosition = currentKing.getPosition()
        val friendlyPieces = pieces.filter { it.hasTheSameColor(currentKing) }
        val enemyPieces = pieces.filter { !it.hasTheSameColor(currentKing) }

        fun checkStraight(): Boolean {

            val enemyStraightPieces = enemyPieces.filter {
                it.getType().lowercase(Locale.getDefault()) == "q" && it.getType().lowercase(Locale.getDefault()) == "r"
            }

            fun checkPieces(x: Int, y: Int): Int {

                val position = Position(x, y)
                val friendlyPiece = friendlyPieces.find { it.getPosition() == position }

                if (friendlyPiece != null) {
                    return 0
                }

                val enemyPiece = enemyStraightPieces.find { it.getPosition() == position }
                return if (enemyPiece != null) 1 else 2
            }

            for (y in kingPosition.y + 1..8) {
                if (checkPieces(kingPosition.x, y) == 1) {
                    return true
                }
            }

            for (y in (1 until kingPosition.y).reversed()) {
                if (checkPieces(kingPosition.x, y) == 1) {
                    return true
                }
            }

            for (x in kingPosition.x + 1..8) {
                if (checkPieces(x, kingPosition.y) == 1) {
                    return true
                }
            }

            for (x in (1 until kingPosition.x).reversed()) {
                if (checkPieces(x, kingPosition.y) == 1) {
                    return true
                }
            }

            return false
        }

        fun checkDiagonals(): Boolean {

            val enemyDiagonalPieces = enemyPieces.filter {
                it.getType().lowercase(Locale.getDefault()) == "q" || it.getType().lowercase(Locale.getDefault()) == "b"
            }

            fun checkPieces(x: Int, y: Int): Int {

                val position = Position(x, y)
                val friendlyPiece = friendlyPieces.find { it.getPosition() == position }

                if (friendlyPiece != null) {
                    return 0
                }

                val enemyPiece = enemyDiagonalPieces.find { it.getPosition() == position }
                return if (enemyPiece != null) 1 else 2
            }

            for (i in 1..8) {

                val leftUpPosition = Position(kingPosition.x - i, kingPosition.y + i)
                if (leftUpPosition.x < 1 || leftUpPosition.y > 8) {
                    break
                }
                if (checkPieces(leftUpPosition.x, leftUpPosition.y) == 1) {
                    return true
                }
            }

            for (i in 1..8) {

                val rightUpPosition = Position(kingPosition.x + i, kingPosition.y + i)
                if (rightUpPosition.x > 8 || rightUpPosition.y > 8) {
                    break
                }
                if (checkPieces(rightUpPosition.x, rightUpPosition.y) == 1) {
                    return true
                }
            }

            for (i in 1..8) {

                val rightDownPosition = Position(kingPosition.x + i, kingPosition.y - i)
                if (rightDownPosition.x > 8 || rightDownPosition.y < 1) {
                    break
                }
                if (checkPieces(rightDownPosition.x, rightDownPosition.y) == 1) {
                    return true
                }
            }

            for (i in 1..8) {

                val leftDownPosition = Position(kingPosition.x - i, kingPosition.y - i)
                if (leftDownPosition.x < 1 || leftDownPosition.y < 1) {
                    break
                }
                if (checkPieces(leftDownPosition.x, leftDownPosition.y) == 1) {
                    return true
                }
            }

            return false
        }

        fun checkPawns(): Boolean {

            val (leftPawnPosition, rightPawnPosition) = if (currentKing == whiteKing) {
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

            val distanceX = abs(whiteKing.getPosition().x - blackKing.getPosition().x)
            val distanceY = abs(whiteKing.getPosition().y - blackKing.getPosition().y)
            val distanceBetweenKings = distanceX + distanceY
            return distanceBetweenKings == 1 || (distanceBetweenKings == 2 && distanceX != 0 && distanceY != 0)
        }

        fun checkKnights(): Boolean {

            val allMoves = listOf(
                kingPosition + Position(-2, 1),
                kingPosition + Position(-1, 2),
                kingPosition + Position(1, 2),
                kingPosition + Position(2, 1),
                kingPosition + Position(2, -1),
                kingPosition + Position(1, -2),
                kingPosition + Position(-1, -2),
                kingPosition + Position(-2, -1)
            )
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
}