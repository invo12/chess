import graphics.Graphics
import pieces.Piece
import pieces.Position
import rules.*
import java.util.*

typealias TurnColor = Boolean

class Game(private val graphics: Graphics, private val pieces: MutableList<Piece>) {

    private val whiteKing = pieces.find { it.getType() == "K" }!!
    private val blackKing = pieces.find { it.getType() == "k" }!!
    private var selectedPiece: Piece? = null
    private var turn: TurnColor = true

    private val rules = mapOf(
        "p" to BlackPawnRule(),
        "P" to WhitePawnRule(),
        "r" to RookRule(),
        "R" to RookRule(),
        "b" to BishopRule(),
        "B" to BishopRule(),
        "q" to QueenRule(),
        "Q" to QueenRule(),
        "n" to KnightRule(),
        "N" to KnightRule(),
        "k" to KingRule(),
        "K" to KingRule(),
    )

    private fun getMoves(piece: Piece): Pair<List<Position>, List<Position>> {

        fun getFriendlyPieces(): List<Piece> {
            return pieces.filter { it.hasTheSameColor(piece) }
        }

        fun getEnemyPieces(): List<Piece> {
            return pieces.filter { !it.hasTheSameColor(piece) }
        }

        return rules[piece.getType()]?.getValidPositions(
            piece,
            getFriendlyPieces(),
            getEnemyPieces()
        ) ?: Pair(listOf(), listOf())
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
                nextTurn()
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