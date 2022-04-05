import pieces.Piece
import pieces.PieceInfo
import pieces.PieceType
import pieces.Position
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.lang.Thread.sleep
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

fun initPieces(state: String, pieceImages: Map<String, Img>): List<Piece> {

    val pieces = mutableListOf<Piece>()
    var rank = 8
    var file = 1

    for (c in state) {

        when {
            c == '/' -> { //new row
                rank--
                file = 1
            }
            c in '1'..'8' -> { //empty
                file += c - '1'
            }
            else -> {
                val pieceType: PieceType = c.toString()
                pieces.add(Piece(PieceInfo(pieceType, pieceImages[pieceType]!!, Position(file, rank))))
                file++
            }
        }
    }

    return pieces.toList()
}

fun main() {

    val path = "src/main/resources/"
    val pieceImages = loadPieceImages("$path/pieces.png")
    val (blackTileImage, whiteTileImage) = loadBoardTiles("$path/blackTile.png", "$path/whiteTile.png")

    val pieces = initPieces("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", pieceImages)

    val frame = Graphics(pieceImages, blackTileImage!!, whiteTileImage!!, pieces.associate { it.getPosition() to it.getType() })

    frame.layout = null
    frame.setLocationRelativeTo(null)
    frame.setSize(520, 550)
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
}