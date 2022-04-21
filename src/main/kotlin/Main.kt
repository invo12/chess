import graphics.Graphics
import graphics.Images
import pieces.Piece
import pieces.PieceInfo
import pieces.PieceType
import pieces.Position
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
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
            sprites.add(pieces!!.getSubimage(j * width, i * height, width, height)!!)
        }
    }
    return "KQBNRPkqbnrp".toCharArray().zip(sprites).toMap().mapKeys { it.key.toString() }
}

fun loadBoardTiles(blackTilePath: String, whiteTilePath: String): List<Img?> {

    val blackTile = readImage(blackTilePath)
    val whiteTile = readImage(whiteTilePath)

    return listOf(blackTile, whiteTile)
}

private fun initPieces(state: String): MutableList<Piece> {

    val pieces = mutableListOf<Piece>()
    var rank = 8
    var file = 1

    for (c in state) {

        when (c) {
            '/' -> { //new row
                rank--
                file = 1
            }
            in '1'..'8' -> { //empty
                file += c - '1'
            }
            else -> {
                val pieceType: PieceType = c.toString()
                pieces.add(Piece(PieceInfo(pieceType, Position(file, rank), Position(file, rank))))
                file++
            }
        }
    }

    return pieces
}

fun main() {

    val path = "src/main/resources/configuration.txt"
    val pieces = initPieces(File(path).readText())

    val graphics = Graphics(pieces)

    graphics.layout = null
    graphics.setLocationRelativeTo(null)
    graphics.setSize(520, 550)
    graphics.isVisible = true
    graphics.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    val game = Game(graphics, pieces)

    val listener: MouseListener = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            val x = (e.point.x - Images.offsetX) / Images.tileSize + 1
            val y = (512 - e.point.y + Images.offsetY) / Images.tileSize + 1
            game.notify(x, y)
        }
    }

    graphics.addMouseListener(listener)

    game.start()
}