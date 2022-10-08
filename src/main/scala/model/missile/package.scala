package model

import collisions.{Affiliation, HitBox, LifePoint, hitbox}
import model.collisions.hitbox.HitBoxRectangular
import model.elements2d.{Angle, Point2D}
import model.missile.Missile

import scala.util.Random

package object missile:

  sealed trait MissileType
  case object BasicMissile extends MissileType
  case object RandomMissile extends MissileType
  case object ZigZagMissile extends MissileType

  val initialLife: LifePoint = 50
  val hitboxBase: Double = 10.0
  val hitboxHeight: Double = 10.0
  val angle: Angle = Angle.Degree(90)
  
  val basicHitBox: (Point2D) => HitBox = HitBoxRectangular(_, hitboxBase, hitboxHeight, angle)

  type MissileMovement = (Double) => Missile

  //generating a random positioned missile of type T
  def GenerateRandomMissile(missileType: MissileType)(using random: Random) = missileType match
    case BasicMissile =>
      for
        x <- Option.apply(Random.nextInt(10)) //length del campo (vedere monadi)
        y <- Option.apply(10) //height del campo
      yield Missile(10, Affiliation.Friendly, 1, 1.0, Point2D(x, y), Point2D(10,10))

    case RandomMissile =>
      for
        vel <- Option.apply(Random.nextInt(10))
        x <- Option.apply(Random.nextInt(10)) //length del campo (vedere monadi)
        y <- Option.apply(10)
        damage <- Option.apply(10)
      yield Missile(10, Affiliation.Friendly, 1, 1.0, Point2D(x, y), Point2D(10,10))

    case _ => ??? //empty missile