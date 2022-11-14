package view

import java.awt.{Color, Image, Toolkit}
import model.collisions.Collisionable
import model.elements2d.{Angle, Point2D}
import model.explosion.Explosion
import model.missile.{Missile, MissileDamageable, MissileImpl, hitboxBase, hitboxHeight}

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

case class CollisionableElement(image: BufferedImage, baseWidth: Int, baseHeight: Int,
                                position: Point2D, angle: Angle = Angle.Degree(0))

object CollisionableVisualizer:

  def printElements(collisionables: List[Collisionable])(using conversion: Conversion[Double, Int]): List[CollisionableElement] =

    val conversion: Collisionable => CollisionableElement = (c: Collisionable) => c match
      case m: Missile with MissileDamageable =>
        var optImage:  Option[BufferedImage] = Option.empty
        try {
          val img = ImageIO.read(getClass.getResource("/city_" + 2 + ".png"))
          optImage = Option(img)
        }
        CollisionableElement(optImage.getOrElse(null), hitboxBase, hitboxHeight, m.position, m.angle.getOrElse(Angle.Degree(0)))
      case e: Explosion =>
        val diameter = e.radius * 2
        val bi = new BufferedImage(diameter,diameter, BufferedImage.TYPE_INT_ARGB)
        val g2d = bi.createGraphics()
        g2d.setColor(Color.RED)
        g2d.drawOval(0, 0, diameter, diameter)
        g2d.dispose()
        CollisionableElement(bi, diameter, diameter, e.position)

    collisionables.map(conversion)

