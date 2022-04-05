package pieces

class Piece(private val pieceInfo: PieceInfo) {

    fun getPosition(): Position {
        return pieceInfo.position
    }

    fun draw() {}
    fun move(position: Position) {
        pieceInfo.position = position
    }
}