import pieces.Piece
import pieces.Position
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JFrame


class Graphics(
    private val pieceImages: Map<String, Img>, private val blackTileImage: Img,
    private val whiteTileImage: Img, private var pieces: List<Piece>,
    private val moveImage: Img
) : JFrame() {

    private val offsetX = 3
    private val offsetY = 30
    private val tileSize = 64
    private var moveList: List<Position> = listOf()
    private var selectedPiece: Piece? = null

    private fun differentColors(piece1: Piece?, piece2: Piece?): Boolean {

        if(piece1 == null || piece2 == null) return false

        val s1 = piece1.getType()
        val s2 = piece2.getType()
        return ! ((s1[0].isUpperCase() && s2[0].isUpperCase()) ||
                (s1[0].isLowerCase() && s2[0].isLowerCase()))
    }

    val listener: MouseListener = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            val x = (e.point.x - offsetX) / tileSize + 1
            val y = (512 - e.point.y + offsetY) / tileSize + 1
            var piece = pieces.filter { it.getPosition() == Position(x, y) }.getOrNull(0)
            if (differentColors(selectedPiece, piece))
                piece = null
            if (piece != null) {
                selectedPiece = piece
                showMoves(listOf(Position(x, y + 1)))
            } else {
                val move = moveList.filter { it.row == x && it.column == y }.getOrNull(0)
                if (move != null) {
                    println("Moving ${selectedPiece?.getType()} to $x $y")
                    selectedPiece = null
                }
                showMoves(listOf())
            }
        }
    }

    init {
        addMouseListener(listener)
    }

    private fun drawOnSquare(graphics2D: Graphics2D, image: Img, row: Int, col: Int) {
        graphics2D.drawImage(
            image,
            offsetX + tileSize * (row - 1),
            offsetY + 512 - tileSize * col,
            tileSize,
            tileSize,
            null
        )
    }

    private fun drawBoard(graphics2D: Graphics2D) {

        var image: Img
        for (i in 1..8) {
            for (j in 1..8) {
                image = if ((i + j) % 2 == 0) whiteTileImage else blackTileImage
                drawOnSquare(graphics2D, image, i, j)
            }
        }
    }

    private fun drawPieces(graphics2D: Graphics2D) {

        pieces.forEach {
            val position = it.getPosition()
            val image = pieceImages[it.getType()]!!
            drawOnSquare(graphics2D, image, position.row, position.column)
        }
    }

    private fun drawMoves(graphics2D: Graphics2D) {

        moveList.forEach {
            drawOnSquare(graphics2D, moveImage, it.row, it.column)
        }
    }

    fun updatepieces(pieces: List<Piece>) {
        this.pieces = pieces
        repaint()
    }

    fun showMoves(moveList: List<Position>) {
        this.moveList = moveList
        repaint()
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        val graphics2D = (g as? Graphics2D)
        drawBoard(graphics2D!!)
        drawPieces(graphics2D)
        drawMoves(graphics2D)
    }


}