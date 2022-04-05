package pieces

import java.awt.Image

typealias Position = Pair<Int, Int>

data class PieceInfo(
    val type: PieceType,
    val image: Image,
    var position: Position
)