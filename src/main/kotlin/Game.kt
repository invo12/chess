import graphics.Graphics
import pieces.Piece
import pieces.Position
import rules.BlackPawnRule
import rules.RookRule
import rules.WhitePawnRule

class Game(private val graphics: Graphics, private val pieces: MutableList<Piece>) {

    private var selectedPiece: Piece? = null
    private val rules = mapOf(
        "p" to BlackPawnRule(),
        "P" to WhitePawnRule(),
        "r" to RookRule(),
        "R" to RookRule()
    )

    private fun getPositions(piece: Piece): Pair<List<Position>, List<Position>> {

        fun String.matchCase(other: String): Boolean {
            val c: Char = this[0]
            val oc: Char = other[0]
            return (c.isLowerCase() && oc.isLowerCase()) || (c.isUpperCase() && oc.isUpperCase())
        }

        fun getFriendlyPositions(): List<Position> {
            return pieces.filter { it.getType().matchCase(piece.getType()) }.map { it.getPosition() }
        }

        fun getEnemyPositions(): List<Position> {
            return pieces.filter { !it.getType().matchCase(piece.getType()) }.map { it.getPosition() }
        }

        return rules[piece.getType()]?.getValidPositions(
            piece.getPosition(),
            getFriendlyPositions(),
            getEnemyPositions()
        ) ?: Pair(listOf(), listOf())
    }

    private fun nextTurn() {

    }

    fun notify(x: Int, y: Int) {

        val piece = pieces.find { it.getPosition() == Position(x, y) }
        if (piece != null) {
            selectedPiece = piece
            graphics.showMoves(getPositions(piece).first + getPositions(piece).second)
        } else {
            val movePositions = selectedPiece?.let {
                getPositions(it).first + getPositions(it).second
            }
            val positionToMove = movePositions?.find { it.x == x && it.y == y }
            if (positionToMove == null) {
                graphics.showMoves(listOf())
            } else {
                selectedPiece?.getPosition()?.x = x
                selectedPiece?.getPosition()?.y = y
                graphics.updatePieces(pieces)
            }
            selectedPiece = null
        }
    }

    fun start() {

    }

}