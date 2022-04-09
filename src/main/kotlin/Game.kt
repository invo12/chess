import graphics.Graphics
import pieces.Piece
import pieces.Position
import rules.*

typealias TurnColor = Boolean

class Game(private val graphics: Graphics, private val pieces: MutableList<Piece>) {

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


        fun getFriendlyPositions(): List<Position> {
            return pieces.filter { it.hasTheSameColor(piece) }.map { it.getPosition() }
        }

        fun getEnemyPositions(): List<Position> {
            return pieces.filter { !it.hasTheSameColor(piece) }.map { it.getPosition() }
        }

        return rules[piece.getType()]?.getValidPositions(
            piece.getPosition(),
            getFriendlyPositions(),
            getEnemyPositions()
        ) ?: Pair(listOf(), listOf())
    }

    private fun nextTurn() {
        turn = !turn
    }

    fun notify(x: Int, y: Int) {

        val piece = pieces.find { it.getPosition() == Position(x, y) }
        if (selectedPiece != null) {
            if (piece != null) {
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
            } else {
                val movePositions = selectedPiece?.let {
                    val moves = getMoves(it)
                    moves.first + moves.second
                }
                val positionToMove = movePositions?.find { it.x == x && it.y == y }
                if (positionToMove == null) {
                    graphics.showMoves(listOf())
                } else {
                    selectedPiece?.move(Position(x, y))
                    graphics.updatePieces(pieces)
                    nextTurn()
                }
                selectedPiece = null
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