package model

import collisions.{Affiliation, HitBox, LifePoint, hitbox}
import model.collisions.hitbox.HitBoxRectangular
import model.elements2d.{Angle, Point2D, Vector2D}
import model.missile.Missile
import model.collisions.Affiliation.*
import model.behavior.*

import scala.util.Random

package object missile:

  sealed trait MissileType
  case class BasicMissile(affiliation: Affiliation) extends MissileType
  case object RandomMissile extends MissileType
  case object ZigZagMissile extends MissileType

  val initialLife: LifePoint = 1
  val velocity: Double = 100
  val damage: Int = 1
  val hitboxBase: Double = 10.0
  val hitboxHeight: Double = 30.0
  val maxHeight: Int = 100
  val maxWidth: Int = 50
  val angle: Angle = Angle.Degree(90)
  
  val basicHitBox: (Point2D, Option[Angle]) => HitBox = (point_, angle_) => HitBoxRectangular(point_, hitboxBase, hitboxHeight, angle_.getOrElse(angle))

  given Conversion[(Point2D, Point2D), Vector2D] with
    override def apply(x: (Point2D, Point2D)): Vector2D = (x._2 <--> x._1).normalize

  def BasicMove(missile: Missile)(dt: DeltaTime): Point2D =
    val distanceToMove = missile.velocity * dt
    val distanceToFinalPosition = missile.position <-> missile.destination
    if distanceToMove >= distanceToFinalPosition
    then missile.destination
    else missile.position --> (missile.direction * distanceToMove * (-1))
