package rules

import pieces.Piece
import pieces.Position

interface Rule {

    // Returns a pair consisting of move positions and capture positions
    fun getValidPositions(
        currentPiece: Piece,
        pieces: List<Piece>,
        movePositions: MutableList<Position>,
        capturePositions: MutableList<Position>
    )
}