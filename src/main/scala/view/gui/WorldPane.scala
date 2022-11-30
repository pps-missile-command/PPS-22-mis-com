package view.gui

import model.Game
import view.{CollisionableVisualizer, Visualizer}

import java.awt.event.MouseMotionListener
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.{AffineTransformOp, BufferedImage}
import javax.swing.{JButton, JPanel}
import model.elements2d.Angle
import view.given_Conversion_Double_Int

import java.io.File
import javax.imageio.ImageIO

/**
 * This class represent the World panel where the entire game is drawn
 * @param game The game to draw
 * @param width The width of the panel
 * @param height The height of the panel
 */
private class WorldPane(val game: Game, width: Int, height: Int) extends JPanel:
    this.setSize(width, height)
    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

    override def paintComponent(graphics: Graphics): Unit =
        super.paintComponent(graphics)
        val g2d: Graphics2D = graphics.asInstanceOf[Graphics2D]
        graphics.clearRect(0, 0, width, height)
        graphics.drawString("SCORE: " + game.player.score, 10, 10)
        graphics.drawString("TIME: " + BigDecimal(game.player.timer.time).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble, 10, 20)
        Visualizer.printGround(game.world.ground).map(
            imageData => graphics.drawImage(imageData._1, imageData._2.x, imageData._2.y,
                imageData._3,
                imageData._4, this)
            )

        CollisionableVisualizer.printElements(game.world.collisionables.toSet) foreach { i =>
            g2d.translate(i.position.x, i.position.y)
            g2d.rotate(i.angle.radiant - Angle.Degree(90).radiant)

            graphics.drawImage(i.image, 0 - (i.baseWidth / 2), 0 - (i.baseHeight / 2), i.baseWidth, i.baseHeight, null)

            g2d.rotate(-1 * (i.angle.radiant - Angle.Degree(90).radiant))
            g2d.translate(-i.position.x, -i.position.y)
        }