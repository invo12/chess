import pieces.Position
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFrame

typealias Img = BufferedImage

fun readImage(path: String): Img? {
    return try {
        ImageIO.read(File(path))
    } catch (e: IOException) {
        null
    }
}

fun loadPieceImages(path: String): Map<String, Img> {

    val pieces: Img? = readImage(path)

    val width = 213
    val height = 210
    val rows = 2
    val cols = 6
    val sprites = mutableListOf<Img>()

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            sprites.add(pieces!!.getSubimage(j * width, i * height, width, height))
        }
    }
    return "KQBNRPkqbnrp".toCharArray().zip(sprites).toMap().mapKeys { it.key.toString() }
}

fun loadBoardTiles(blackTilePath: String, whiteTilePath: String): List<Img?> {

    val blackTile = readImage(blackTilePath)
    val whiteTile = readImage(whiteTilePath)

    return listOf(blackTile, whiteTile)
}

class Graphics(
    private val pieceImages: Map<String, Img>, private val blackTileImage: Img,
    private val whiteTileImage: Img
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

    private fun drawPieces(graphics2D: Graphics2D, positions: Map<Position, String>) {

        positions.forEach {
            drawOnSquare(graphics2D, pieceImages[it.value]!!, it.key.row, it.key.column)
        }
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        val graphics2D = (g as? Graphics2D)
        drawBoard(graphics2D!!)
        drawPieces(graphics2D, mapOf(Position(2, 3) to "Q"))
    }
}

fun main() {

    val path = "src/main/resources/"
    val pieceImages = loadPieceImages("$path/pieces.png")
    val (blackTileImage, whiteTileImage) = loadBoardTiles("$path/blackTile.png", "$path/whiteTile.png")

    val frame = Graphics(pieceImages, blackTileImage!!, whiteTileImage!!)

    frame.layout = null
    frame.setLocationRelativeTo(null)
    frame.setSize(520, 550)
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
}