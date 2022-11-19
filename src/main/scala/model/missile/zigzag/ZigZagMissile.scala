package model.missile.zigzag
import model.DeltaTime
import model.behavior.Moveable
import model.collisions.{Affiliation, Collisionable, LifePoint, lifePointDeath}
import model.elements2d.{Point2D, Vector2D}
import model.missile.*
import model.missile.given_Conversion_Point2D_Point2D_Vector2D

import scala.util.Random

object ZigZagMissile:

  trait ZigZagMissile(step: Int = 5, positions: LazyList[Point2D], to: Point2D):
    missile: MissileImpl =>

    def subDestinationReached: Boolean = position == destination

    override def destinationReached: Boolean = position == to

    override def newMissile(life: LifePoint = missile.lifePoint, pos: Point2D = missile.position, _dt: DeltaTime = missile.dt): MissileImpl & ZigZagMissile & Scorable =
      subDestinationReached match
        case true => apply(missile, pos, step, positions.pop()._2, _dt, to)
        case false => apply(missile, pos, step, positions, _dt, to)

  def apply(from: Point2D, to: Point2D, step: Int)(using Random): ZigZagMissile & Scorable & MissileImpl =
    val list = DirectionList(from, to, step)
    new MissileImpl(position = from, destination = list.head, affiliation = Affiliation.Enemy) with Scorable(1) with ZigZagMissile(step, list, to)

  private def apply(missile: Missile, newPosition: Point2D, step: Int, positions: LazyList[Point2D], deltaTime: DeltaTime, to: Point2D): ZigZagMissile & Scorable & MissileImpl = positions.size match
    case 0 => new MissileImpl(missile.initialLife, newPosition, to, deltaTime, Affiliation.Enemy, missile.damage, missile.velocity) with Scorable(1) with ZigZagMissile(step, positions, to)
    case _ => new MissileImpl(missile.initialLife, newPosition, positions.head, deltaTime, Affiliation.Enemy, missile.damage, missile.velocity) with Scorable(1) with ZigZagMissile(step, positions, to)