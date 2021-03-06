import graphics.Graphics
import pieces.Piece
import pieces.Position
import pieces.matchCase
import rules.*
import kotlin.math.abs

typealias TurnColor = Boolean

class Game(private val graphics: Graphics, private val pieces: MutableList<Piece>) {

    private var selectedPiece: Piece? = null
    private var turn: TurnColor = true

    private val movementRules = mapOf(
        "p" to listOf(BlackPawnRule(), CheckRule()),
        "P" to listOf(WhitePawnRule(), CheckRule()),
        "r" to listOf(RookRule(), CheckRule()),
        "R" to listOf(RookRule(), CheckRule()),
        "b" to listOf(BishopRule(), CheckRule()),
        "B" to listOf(BishopRule(), CheckRule()),
        "q" to listOf(BishopRule(), RookRule(), CheckRule()),
        "Q" to listOf(BishopRule(), RookRule(), CheckRule()),
        "n" to listOf(KnightRule(), CheckRule()),
        "N" to listOf(KnightRule(), CheckRule()),
        "k" to listOf(KingRule(), CheckRule()),
        "K" to listOf(KingRule(), CheckRule()),
    )

    fun notify(x: Int, y: Int) {

        fun resolvePiece(piece: Piece) {

            if (canMoveDuringThisTurn(piece)) {
                selectedPiece = piece
                getMoves(piece).let {
                    graphics.showMoves(it.first + it.second)
                }
            } else {

                val (_, captures) = getMoves(selectedPiece!!)
                val capture = captures.find { it == Position(x, y) }
                capture?.let {

                    val currentPiece = selectedPiece!!

                    pieces.remove(piece)
                    currentPiece.move(Position(x, y))
                    doAdditionalUpdates(currentPiece)
                    updateGraphics(listOf())

                    nextTurn()
                }
            }
        }

        fun resolveMovePositions() {

            val (moves, captures) = getMoves(selectedPiece!!)
            val positionToMove = moves.find { it == Position(x, y) }

            if (positionToMove == null) {
                if (verifyEnPassant(captures.find { it == Position(x, y) }))
                    return
                graphics.showMoves(listOf())
            } else {
                selectedPiece!!.move(Position(x, y))
                graphics.updatePieces(pieces)
                nextTurn()
            }
            doAdditionalUpdates(selectedPiece!!)
        }

        val piece = pieces.find { it.getPosition() == Position(x, y) }

        if (selectedPiece != null) {
            if (piece != null) {
                resolvePiece(piece)
            } else {
                resolveMovePositions()
            }
        } else {
            if (piece != null && canMoveDuringThisTurn(piece)) {

                selectedPiece = piece
                getMoves(piece).let {
                    graphics.showMoves(it.first + it.second)
                }
            }
        }
    }

    fun start() {
        turn = true
    }

    private fun getMoves(piece: Piece): Pair<List<Position>, List<Position>> {

        val rules = movementRules[piece.getType()]
        val movePositions = mutableListOf<Position>()
        val capturePositions = mutableListOf<Position>()

        rules?.forEach {
            it.getValidPositions(piece, pieces, movePositions, capturePositions)
        }

        return Pair(movePositions, capturePositions)
    }

    private fun getTotalMoves(): Int {

        val friendlyPieces = pieces.filter { canMoveDuringThisTurn(it) }

        friendlyPieces.forEach {
            getMoves(it).let { moves ->
                if (moves.first.size + moves.second.size > 0)
                    return 1
            }
        }

        return 0
    }

    private fun nextTurn() {

        turn = !turn
        checkForEndGame()
    }

    private fun checkForEndGame() {

        if (pieces.size == 2) {
            graphics.showEndGameMessage("STALEMATE")
        } else if (pieces.size == 3) {
            val piece = pieces.find { it.getType().lowercase() != "k" }!!
            if (piece.getType().lowercase() in listOf("n", "b")) {
                graphics.showEndGameMessage("STALEMATE")
            }
        }
        if (getTotalMoves() == 0) {
            val king = pieces.find { it.getType().lowercase() == "k" && canMoveDuringThisTurn(it) }!!
            if (isChecked(king, pieces)) {
                graphics.showEndGameMessage(if (king.getType() == "K") "Black won" else "White won")
            } else {
                graphics.showEndGameMessage("STALEMATE")
            }
        }
    }

    private fun canMoveDuringThisTurn(piece: Piece): Boolean {

        return piece.getType().matchCase(if (turn) "T" else "t")
    }

    private fun doAdditionalUpdates(currentPiece: Piece) {

        // pawn upgrade
        if (currentPiece.getType() == "P" && currentPiece.getPosition().y == 8) {
            currentPiece.setType("Q")
        } else if (currentPiece.getType() == "p" && currentPiece.getPosition().y == 1) {
            currentPiece.setType("q")
        }

        // castle
        if (currentPiece.getType().lowercase() == "k" && abs(currentPiece.getPosition().x - currentPiece.getLastPosition().x) > 1) {

            if (currentPiece.getPosition().x == 7) {
                pieces.find {
                    it.getType().lowercase() == "r" && it.hasTheSameColor(currentPiece) && it.getPosition().x == 8
                }!!.move(Position(6, currentPiece.getPosition().y))
            } else {
                pieces.find {
                    it.getType().lowercase() == "r" && it.hasTheSameColor(currentPiece) && it.getPosition().x == 1
                }!!.move(Position(4, currentPiece.getPosition().y))
            }
        }
        selectedPiece = null
    }

    private fun updateGraphics(moves: List<Position>) {

        graphics.updatePieces(pieces)
        graphics.showMoves(moves)
    }

    private fun verifyEnPassant(positionToMove: Position?): Boolean {

        val currentPiece = selectedPiece!!
        if (currentPiece.getType().lowercase() == "p") {
            positionToMove?.let {
                val piece = pieces.find {
                    val enemyPawnY = when (currentPiece.getType()) {
                        "p" -> positionToMove.y + 1
                        "P" -> positionToMove.y - 1
                        else -> -1
                    }
                    it.getPosition() == Position(positionToMove.x, enemyPawnY)
                }
                pieces.remove(piece)
                currentPiece.move(positionToMove)
                updateGraphics(listOf())
                selectedPiece = null

                nextTurn()
                return true
            }
        }
        return false
    }
}