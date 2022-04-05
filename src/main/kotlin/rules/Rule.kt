package rules

import pieces.Position

interface Rule {

    fun getValidPositions(
        currentPosition: Position, otherPieces: List<Position>,
        validMoves: List<Position>
    ): List<Position>
}