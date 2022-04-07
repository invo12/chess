package rules

import pieces.Position

interface Rule {

    // Returns a pair consisting of move positions and capture positions
    fun getValidPositions(
        currentPosition: Position, friendlyPositions: List<Position>, enemyPositions: List<Position>
    ): Pair<List<Position>, List<Position>>
}