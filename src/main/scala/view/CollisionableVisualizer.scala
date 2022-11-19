package view

import model.World

import java.awt.{Color, Image, Toolkit}
import model.collisions.{Affiliation, Collisionable}
import model.elements2d.{Angle, Point2D}
import model.explosion.Explosion
import model.missile.{Missile, MissileDamageable, MissileImpl, hitboxBase, hitboxHeight}

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

extension(p: Point2D)
  def map(f: (Point2D) => Point2D) = f(p)

case class CollisionableElement(image: BufferedImage, baseWidth: Int, baseHeight: Int,
                                position: Point2D, angle: Angle = Angle.Degree(0))

object CollisionableVisualizer:

  private val convertPosition: (Point2D) => Point2D = (p) => Point2D((p.x * ViewConstants.GUI_width) / World.width, (p.y * ViewConstants.GUI_height) / World.height)

  def printElements(collisionables: List[Collisionable])(using conversion: Conversion[Double, Int]): List[CollisionableElement] =

    val conversion: Collisionable => CollisionableElement = (c: Collisionable) => c match
      case m: Missile with MissileDamageable =>
        var optImage:  Option[BufferedImage] = Option.empty
        try {
          m.affiliation match
            case Affiliation.Enemy => optImage = Option(ImageIO.read(getClass.getResource("/enemy_missile.png")))
            case _ => optImage = Option(ImageIO.read(getClass.getResource("/friendly_missile.png")))
        }
        CollisionableElement(optImage.getOrElse(null), hitboxBase, hitboxHeight, m.position map convertPosition, m.angle.getOrElse(Angle.Degree(0)))
      case e: Explosion =>
        val diameter = e.radius * 2
        val bi = new BufferedImage(diameter,diameter, BufferedImage.TYPE_INT_ARGB)
        val g2d = bi.createGraphics()
        g2d.setColor(Color.RED)
        g2d.drawOval(0, 0, diameter, diameter)
        g2d.dispose()
        CollisionableElement(bi, diameter, diameter, e.position map convertPosition)

    collisionables.map(conversion)

