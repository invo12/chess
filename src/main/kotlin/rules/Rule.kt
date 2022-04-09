package rules

import pieces.Piece
import pieces.Position

interface Rule {

    // Returns a pair consisting of move positions and capture positions
    fun getValidPositions(
        currentPiece: Piece, friendlyPieces: List<Piece>, enemyPieces: List<Piece>
    ): Pair<List<Position>, List<Position>>
}