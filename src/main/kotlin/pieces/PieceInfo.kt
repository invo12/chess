package pieces

typealias PieceType = String

data class Position(var x: Int, var y: Int)

data class PieceInfo(
    val type: PieceType,
    var position: Position
)