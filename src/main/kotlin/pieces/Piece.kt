package pieces

fun String.matchCase(other: String): Boolean {
    val c: Char = this[0]
    val oc: Char = other[0]
    return (c.isLowerCase() && oc.isLowerCase()) || (c.isUpperCase() && oc.isUpperCase())
}

class Piece(private val pieceInfo: PieceInfo) {

    fun getPosition(): Position {
        return pieceInfo.position
    }

    fun move(position: Position) {
        pieceInfo.position = position
    }

    fun getType(): PieceType {
        return pieceInfo.type
    }

    fun hasTheSameColor(p: Piece): Boolean {
        return this.getType().matchCase(p.getType())
    }
}