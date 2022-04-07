package rules

import pieces.Position

class KingRule: Rule {
    override fun getValidPositions(
        currentPosition: Position,
        friendlyPositions: List<Position>,
        enemyPositions: List<Position>,
    ): Pair<List<Position>, List<Position>> {
        TODO("Not yet implemented")
    }
}