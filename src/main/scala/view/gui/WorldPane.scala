package view.gui

import model.World
import view.{CollisionableVisualizer, Visualizer}

import java.awt.event.MouseMotionListener
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.{AffineTransformOp, BufferedImage}
import javax.swing.{JButton, JPanel}
import model.elements2d.Angle

import java.io.File
import javax.imageio.ImageIO

given Conversion[Double, Int] with
    override def apply(x: Double): Int = x.toInt

private class WorldPane(val world: World, width: Int, height: Int) extends JPanel:
    this.setSize(width, height)
    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

    override def paintComponent(graphics: Graphics): Unit =
        super.paintComponent(graphics)
        val g2d: Graphics2D = graphics.asInstanceOf[Graphics2D]
        graphics.clearRect(0, 0, width, height)
        graphics.drawString("SCORE: " + world.score, 10, 10)
        graphics.drawString("TIME: " + BigDecimal(world.timer.time).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble, 10, 20)
        Visualizer.printGround(world.ground).map(
            imageData => graphics.drawImage(imageData._1, imageData._2.x, imageData._2.y,
                imageData._3,
                imageData._4, this)
            )

        CollisionableVisualizer.printElements(world.collisionables.toSet) foreach { i =>
            g2d.translate(i.position.x, i.position.y)
            g2d.rotate(i.angle.radiant - Angle.Degree(90).radiant)

            graphics.drawImage(i.image, 0 - (i.baseWidth / 2), 0 - (i.baseHeight / 2), i.baseWidth, i.baseHeight, null)

            g2d.rotate(-1 * (i.angle.radiant - Angle.Degree(90).radiant))
            g2d.translate(-i.position.x, -i.position.y)
        }


//        world.all.foreach { entity =>
//            val (x, y) = ((entity.position.x * width).toInt, (entity.position.y * height).toInt)
//            val (widthCircle, heightCircle) = ((entity.diameter * width).toInt, (entity.diameter * height).toInt)
//            graphics.setColor(Color.getHSBColor(entity.size.toFloat, 1, 0.5))
//            graphics.fillOval(x - widthCircle / 2, y - heightCircle / 2, widthCircle, heightCircle)
//            graphics.setColor(Color.BLACK)
//            graphics.drawOval(x - widthCircle / 2, y - heightCircle / 2, widthCircle, heightCircle)
//        }