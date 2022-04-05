package rules

import pieces.Position

class BlockRule : Rule {

    override fun getValidPositions(
        currentPosition: Position,
        otherPieces: List<Position>,
        validMoves: List<Position>
    ): List<Position> {
        TODO("Not yet implemented")
    }
}