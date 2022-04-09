package pieces

typealias PieceType = String

data class Position(var x: Int, var y: Int) {
    operator fun plus(position: Position): Position {
        return Position(this.x + position.x, this.y + position.y)
    }
}



data class PieceInfo(
    val type: PieceType,
    var position: Position,
    var lastPosition: Position
)