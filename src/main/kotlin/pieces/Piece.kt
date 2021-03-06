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

    fun setPosition(position: Position) {
        pieceInfo.position = position
    }

    fun move(position: Position) {
        pieceInfo.lastPosition = pieceInfo.position
        pieceInfo.hasMoved = true
        pieceInfo.position = position
    }

    fun getType(): PieceType {
        return pieceInfo.type
    }

    fun setType(pieceType: PieceType) {
        pieceInfo.type = pieceType
    }

    fun hasTheSameColor(p: Piece): Boolean {
        return this.getType().matchCase(p.getType())
    }

    fun getLastPosition(): Position {
        return pieceInfo.lastPosition
    }

    fun hasMoved(): Boolean {
        return pieceInfo.hasMoved
    }
}