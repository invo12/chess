package pieces

import Img

typealias PieceType = String

data class Position(var row: Int, var column: Int)

data class PieceInfo(
    val type: PieceType,
    val image: Img,
    var position: Position
)