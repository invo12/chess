import pieces.Piece
import pieces.PieceType
import pieces.Position
import rules.Rule
import java.awt.Image

typealias Board = List<Image>
typealias RulesMap = Map<PieceType, List<Rule>>

class Game(private val board: Board, private val rules: RulesMap, private val pieces: MutableList<Piece>) {

    private fun getMovePositions(piece: Piece): List<Position> {
        /// TODO
        return listOf()
    }

    private fun nextTurn() {

    }

    private fun start() {

    }

}