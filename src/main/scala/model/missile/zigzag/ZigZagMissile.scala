package model.missile.zigzag
import model.{DeltaTime, Scorable}
import model.behavior.Moveable
import model.collisions.{Affiliation, Collisionable, LifePoint, lifePointDeath}
import model.elements2d.{Point2D, Vector2D}
import model.missile.*
import model.missile.zigzag.PimpingByPoint2D.~=
import model.missile.zigzag.PimpingByLazyList.pop

import scala.util.Random

/**
 * This trait models a variation of the normal [[Missile]], that is the ZigZagMissile: the difference is
 * that in this case the path is not linear from the start point towards the destination point, but a sequence
 * subdestinations to reach, and when one is reached change the direction accordingly
 */
trait ZigZagMissile(step: Int = 5, positions: LazyList[Point2D], to: Point2D):
    missile: MissileImpl =>
  /**
   * This method check if the position is equals to the sub destination: the sub destination
   * change every time the ZigZagMissile reach it, taking the next destination available into the list
   * @return a boolean value true or false to models the equivalency
   */
    def subDestinationReached: Boolean = position ~= destination

  /**
   * This method overrides the same method defined in [[Missile]]: in this case we check if the current position is
   * equal to the last position of the path, that is the final destination
   * @return a boolean true or false
   */
    override def destinationReached: Boolean = position ~= to

  /**
   * Copy method that creates a new ZigZagMissile with the new properties specified
   * @param life The new life of the missile
   * @param pos The updated position
   * @param _dt The updated virtual delta time
   * @return the new ZigZagMissile
   */
    override def newMissile(life: LifePoint = missile.lifePoint, pos: Point2D = missile.position, _dt: DeltaTime = missile.dt): MissileImpl & ZigZagMissile & Scorable =
      subDestinationReached match
        case true => ZigZagMissile.apply(missile, pos, step, positions.pop()._2, _dt, to)
        case false => ZigZagMissile.apply(missile, pos, step, positions, _dt, to)

/**
 * Companion object of [[ZigZagMissile]]
 */
object ZigZagMissile:

  /**
   * Factory method that generates the ZigZagMissile, using mixins
   * @param from The starting point
   * @param to The final destination
   * @param step The number of direction changes
   * @param maxWidth The max width after which a missile should not go over
   * @param Random Random object to generate
   * @return the new ZigZagMissile
   */
  def apply(from: Point2D, to: Point2D, step: Int, maxWidth: Double): ZigZagMissile & Scorable & MissileImpl =
    val list = DirectionList(from, to, step, maxWidth = maxWidth)
    new MissileImpl(position = from, destination = list.head, affiliation = Affiliation.Enemy) with Scorable(1) with ZigZagMissile(step, list, to)

  /**
   * This private method changes its signature
   * @param missile The missile that should change its position
   * @param newPosition The final destination
   * @param step The step represents the n sub destinations
   * @param positions Current position
   * @param deltaTime Virtual delta time
   * @param to Fixed destination point
   * @return the newZigZag midssile created
   */
  private def apply(missile: Missile, newPosition: Point2D, step: Int, positions: LazyList[Point2D], deltaTime: DeltaTime, to: Point2D): ZigZagMissile & Scorable & MissileImpl = positions.size match
    case 0 => new MissileImpl(missile.initialLife, newPosition, to, deltaTime, Affiliation.Enemy, missile.damage, missile.velocity) with Scorable(1) with ZigZagMissile(step, positions, to)
    case _ => new MissileImpl(missile.initialLife, newPosition, positions.head, deltaTime, Affiliation.Enemy, missile.damage, missile.velocity) with Scorable(1) with ZigZagMissile(step, positions, to)