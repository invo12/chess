package pieces

import java.awt.Image

data class Position(var row: Int, var column: Int)

data class PieceInfo(
    val type: PieceType,
    val image: Image,
    var position: Position
)