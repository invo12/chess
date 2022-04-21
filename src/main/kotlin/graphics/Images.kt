package graphics

import loadBoardTiles
import loadPieceImages
import readImage

object Images {
    private val path = "src/main/resources/"
    private val tileImages = loadBoardTiles("$path/blackTile.png", "$path/whiteTile.png")

    val blackTileImage = tileImages[0]!!
    val whiteTileImage = tileImages[1]!!
    val pieceImages = loadPieceImages("$path/pieces.png")
    val moveImage = readImage("$path/move.png")!!

    const val offsetX = 3
    const val offsetY = 30
    const val tileSize = 64
}