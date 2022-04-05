import pieces.PieceInfo
import pieces.Position
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame

class Graphics(
    private val pieceImages: Map<String, Img>, private val blackTileImage: Img,
    private val whiteTileImage: Img, private var positions: Map<Position, String>
) : JFrame() {

    private val offsetX = 3
    private val offsetY = 30
    private val tileSize = 64

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

    fun drawPieces(graphics2D: Graphics2D) {

        positions.forEach {
            drawOnSquare(graphics2D, pieceImages[it.value]!!, it.key.row, it.key.column)
        }
    }

    fun updatePositions(positions: Map<Position, String>) {
        this.positions = positions
        repaint()
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        val graphics2D = (g as? Graphics2D)
        drawBoard(graphics2D!!)
        drawPieces(graphics2D)
    }
}