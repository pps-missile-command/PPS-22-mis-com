package view

import model.World

import java.awt.{Color, Image, Toolkit}
import model.collisions.{Affiliation, Collisionable}
import model.elements2d.{Angle, Point2D}
import model.explosion.Explosion
import model.missile.{Missile, MissileDamageable, MissileImpl, hitboxBase, hitboxHeight}
import model.vehicle.Plane
import model.vehicle.vehicleTypes

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.{File, FileNotFoundException}

/**
 * Extension method for a Point2D, that perform a mapping returning a new Point2D based
 * on the function taken by input
 */
extension(p: Point2D)
  def map(f: (Point2D) => Point2D) = f(p)

/**
 * Case class that represent a graphic collisionable element
 * @param image The image for the given collisionable
 * @param baseWidth The width of the image
 * @param baseHeight The height of the image
 * @param position The position of the image mapped into the GUI space
 * @param angle The angle of the image
 */
case class CollisionableElement(image: BufferedImage, baseWidth: Int, baseHeight: Int,
                                position: Point2D, angle: Angle = Angle.Degree(0))

object CollisionableVisualizer:

  /**
   * Method that creates a graphic set of [[CollisionableElement]] from a set of [[Collisionable]]
   * @param collisionables The set of [[Collisionable]] to convert
   * @param conversion The conversion method to convert double values into integers
   * @return the set of CollisionableElements created
   */
  def printElements(collisionables: Set[Collisionable])(using conversion: Conversion[Double, Int]): Set[CollisionableElement] =

    val conversion: Collisionable => CollisionableElement = (c: Collisionable) => c match
      case m: Missile with MissileDamageable =>
        var optImage:  Option[BufferedImage] = Option.empty
        try {
          m.affiliation match
            case Affiliation.Enemy => optImage = Option(ImageIO.read(getClass.getResource("/enemy_missile.png")))
            case _ => optImage = Option(ImageIO.read(getClass.getResource("/friendly_missile.png")))
        } catch {
          case e: Exception => throw new FileNotFoundException()
        }
        CollisionableElement(optImage.get, hitboxBase use convertWidth, hitboxHeight use convertHeight, m.position map convertPosition, m.angle.getOrElse(Angle.Degree(0)))
      case e: Explosion =>
        val diameter = e.radius * 2 use convertWidth
        val bi = new BufferedImage(diameter,diameter, BufferedImage.TYPE_INT_ARGB)
        val g2d = bi.createGraphics()
        g2d.setColor(Color.RED)
        g2d.drawOval(0, 0, diameter, diameter)
        g2d.dispose()
        CollisionableElement(bi, diameter, diameter, e.position map convertPosition)
      case p: Plane => CollisionableElement(
        ImageIO.read(getClass.getResource("/Plane_" + p.planeDirection.toString + ".png")),
        model.vehicle.hitboxBase use convertWidth,
        model.vehicle.hitboxHeight use convertHeight,
        p.position map convertPosition,
        Angle.Degree(90)
      )

    collisionables.map(conversion)

