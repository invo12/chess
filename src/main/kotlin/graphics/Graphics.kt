package graphics

import Img
import pieces.Piece
import pieces.Position
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.JOptionPane.showMessageDialog
import kotlin.system.exitProcess


class Graphics(
    private var pieces: List<Piece>
) : JFrame() {

    private var moveList: List<Position> = listOf()

    private fun drawOnSquare(graphics2D: Graphics2D, image: Img, x: Int, y: Int) {
        graphics2D.drawImage(
            image,
            Images.offsetX + Images.tileSize * (x - 1),
            Images.offsetY + 512 - Images.tileSize * y,
            Images.tileSize,
            Images.tileSize,
            null
        )
    }

    private fun drawBoard(graphics2D: Graphics2D) {

        var image: Img
        for (i in 1..8) {
            for (j in 1..8) {
                image = if ((i + j) % 2 == 1) Images.whiteTileImage else Images.blackTileImage
                drawOnSquare(graphics2D, image, i, j)
            }
        }
    }

    private fun drawPieces(graphics2D: Graphics2D) {

        pieces.forEach {
            val position = it.getPosition()
            val image = Images.pieceImages[it.getType()]!!
            drawOnSquare(graphics2D, image, position.x, position.y)
        }
    }

    private fun drawMoves(graphics2D: Graphics2D) {

        moveList.forEach {
            drawOnSquare(graphics2D, Images.moveImage, it.x, it.y)
        }
    }

    fun updatePieces(pieces: List<Piece>) {

        this.pieces = pieces
        this.moveList = listOf()
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

    fun showEndGameMessage(message: String) {

        showMessageDialog(null, message)
        exitProcess(0)
    }
}