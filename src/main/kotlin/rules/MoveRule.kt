package rules

import pieces.Position

class MoveRule : Rule {

    override fun getValidPositions(
        currentPosition: Position,
        otherPieces: List<Position>,
        validMoves: List<Position>
    ): List<Position> {
        TODO("Not yet implemented")
    }
}