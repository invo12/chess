import graphics.Graphics
import pieces.Piece
import pieces.PieceType
import pieces.Position
import rules.Rule


typealias RulesMap = Map<PieceType, List<Rule>>

class Game(private val graphics: Graphics, private val rules: RulesMap, private val pieces: MutableList<Piece>) {

    var selectedPiece: Piece? = null

    private fun getMovePositions(piece: Piece): List<Position> {
        /// TODO
        val position = piece.getPosition()
        return listOf(Position(position.x, position.y + 1))
    }

    private fun nextTurn() {

    }

    fun notify(x: Int, y: Int) {

        val piece = pieces.find { it.getPosition() == Position(x, y) }
        if (piece != null) {
            selectedPiece = piece
            graphics.showMoves(listOf(Position(x, y + 1)))
        } else {
            val movePositions = selectedPiece?.let {
                getMovePositions(it)
            }
            val positionToMove = movePositions?.find { it.x == x && it.y == y }
            if(positionToMove == null){
                graphics.showMoves(listOf())
            } else {
                selectedPiece?.getPosition()?.y = selectedPiece?.getPosition()?.y?.plus(1)!!
                graphics.updatePieces(pieces)
            }
            selectedPiece = null
        }
    }

    fun start() {

    }

}