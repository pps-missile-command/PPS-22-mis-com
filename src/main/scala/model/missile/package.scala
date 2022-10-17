package model

import collisions.{Affiliation, HitBox, LifePoint, hitbox}
import model.collisions.hitbox.HitBoxRectangular
import model.elements2d.{Angle, Point2D}
import model.missile.Missile
import model.collisions.Affiliation.*

import scala.util.Random

package object missile:

  type DeltaTime = Double

  sealed trait MissileType
  case class BasicMissile(affiliation: Affiliation) extends MissileType
  case object RandomMissile extends MissileType
  case object ZigZagMissile extends MissileType

  val initialLife: LifePoint = 50
  val velocity: Double = 10
  val damage: Int = 1
  val hitboxBase: Double = 10.0
  val hitboxHeight: Double = 10.0
  val maxHeight: Int = 100
  val maxWidth: Int = 50
  val angle: Angle = Angle.Degree(90)
  
  val basicHitBox: (Point2D, Option[Angle]) => HitBox = (point_, angle_) => HitBoxRectangular(point_, hitboxBase, hitboxHeight, angle_.getOrElse(angle))

  type MissileMovement = (DeltaTime) => Point2D

  given affiliation: Affiliation = Affiliation.Enemy
  //generating a random positioned missile of type T
  def GenerateRandomMissile(missileType: MissileType, finalDestination: Point2D)(using random: Random): Option[Missile] = missileType match
    case BasicMissile(affiliation) =>
      for
        x <- Option.apply(Random.nextInt(maxWidth)) //length del campo (vedere monadi)
        y <- Option.apply(maxHeight) //height del campo
      yield Missile(initialLife, damage, velocity, Point2D(x, y), finalDestination)

    case RandomMissile =>
      for
        vel <- Option.apply(Random.nextDouble() * velocity)
        x <- Option.apply(Random.nextInt(maxWidth)) //length del campo (vedere monadi)
        y <- Option.apply(maxHeight)
        damage <- Option.apply(damage)
      yield Missile(initialLife, damage, velocity, Point2D(x, y), Point2D(10,10))

    case _ => Option(Missile(initialLife, damage, velocity, Point2D(0, 0), finalDestination))