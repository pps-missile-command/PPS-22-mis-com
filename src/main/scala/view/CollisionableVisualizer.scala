package view

import com.sun.javafx.geom.Ellipse2D

import java.awt.{Image, Toolkit}

import model.collisions.Collisionable
import model.elements2d.Point2D
import model.explosion.Explosion
import model.missile.{Missile, MissileImpl, hitboxBase, hitboxHeight}

import java.awt.Toolkit
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

/*extension (p: ImageElement)
  def map[A](f: ImageElement => A) = f(p)
  def flatMap[A](f: ImageElement => CollisionableElement) = f(p)
*/

case class CollisionableElement(image: Image, position: Point2D, baseWidth: Int, baseHeight: Int)
case class ImageElement(src: String, position: Point2D)

given Conversion[Double, Int] with
  override def apply(x: Double): Int = x.toInt

object CollisionableVisualizer:
  //TODO string resource
  val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

  val conversion: Collisionable => CollisionableElement = (c: Collisionable) =>
    val imageElement = c match
    case m: Missile => ImageElement(" ", m.position)
    case e: Explosion => ImageElement(" ", e.position)

    val img = Toolkit.getDefaultToolkit().getImage(resourceFolderPath + "\\city_" + 2 + ".png") //element.src

    CollisionableElement(img, imageElement.position, hitboxBase, hitboxHeight)

  def printElements(collisionables: List[Collisionable]): List[CollisionableElement] =
    for
      i <- collisionables
    yield conversion(i)

