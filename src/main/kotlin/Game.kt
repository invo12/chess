import graphics.Graphics
import pieces.Piece
import pieces.Position
import rules.*
import java.util.*

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

    private fun getMoves(piece: Piece): Pair<List<Position>, List<Position>> {

        val rules = movementRules[piece.getType()]
        if (rules != null) {
            val movePositions = mutableListOf<Position>()
            val capturePositions = mutableListOf<Position>()
            for (rule in rules) {
                rule.getValidPositions(piece, pieces, movePositions, capturePositions)
            }
            return Pair(movePositions, capturePositions)
        }
        return Pair(listOf(), listOf())
    }

    private fun nextTurn() {

        turn = !turn
        if (pieces.size == 2) {
            graphics.showEndGameMessage("STALEMATE")
        } else if (pieces.size == 3) {
            val piece = pieces.find { it.getType().lowercase() != "k" }!!
            if (piece.getType().lowercase() in listOf("n", "b")) {
                graphics.showEndGameMessage("STALEMATE")
            }
        }
        if (getTotalMoves() == 0) {
            val king = pieces.find { it.getType().lowercase() == "k" && it.hasTurnColor(turn) }!!
            if (isChecked(king, pieces)) {
                graphics.showEndGameMessage(if(king.getType() == "K") "Black won" else "White won")
            } else {
                graphics.showEndGameMessage("STALEMATE")
            }
        }
    }

    private fun getTotalMoves(): Int {

        val friendlyPieces = pieces.filter { it.hasTurnColor(turn) }
        for (piece in friendlyPieces) {
            val moves = getMoves(piece)
            if (moves.first.size + moves.second.size > 0)
                return 1
        }
        return 0
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
                        return
                    }
                }
                graphics.showMoves(listOf())
            } else {
                selectedPiece?.move(Position(x, y))
                graphics.updatePieces(pieces)
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
}