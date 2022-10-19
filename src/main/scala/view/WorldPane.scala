package view

import model.World

import java.awt.{Color, Graphics, Image, Toolkit, event}
import java.awt.event.MouseMotionListener
import javax.swing.JPanel


private class WorldPane(val world: World, width: Int, height: Int) extends JPanel:
    this.setSize(width, height)
    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

    override def paintComponent(graphics: Graphics): Unit =
        super.paintComponent(graphics)
        graphics.clearRect(0, 0, width, height)
        //var i: Image = Toolkit.getDefaultToolkit.getImage(resourceFolderPath +"city_3.png")
        Visualizer.printGround(world.ground).map(
            imageData => graphics.drawImage(imageData._1, imageData._2.toInt, imageData._3.toInt, 150, 100, this)
            )
//        world.all.foreach { entity =>
//            val (x, y) = ((entity.position.x * width).toInt, (entity.position.y * height).toInt)
//            val (widthCircle, heightCircle) = ((entity.diameter * width).toInt, (entity.diameter * height).toInt)
//            graphics.setColor(Color.getHSBColor(entity.size.toFloat, 1, 0.5))
//            graphics.fillOval(x - widthCircle / 2, y - heightCircle / 2, widthCircle, heightCircle)
//            graphics.setColor(Color.BLACK)
//            graphics.drawOval(x - widthCircle / 2, y - heightCircle / 2, widthCircle, heightCircle)
//        }