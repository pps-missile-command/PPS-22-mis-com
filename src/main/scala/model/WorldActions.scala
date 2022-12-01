package model

import model.behavior.{Timeable, Moveable}
import model.elements2d.Point2D
import model.collisions.{Collision, Collisionable}

/**
 * A trait that represents the actions that can be performed on a [[World]].
 *
 * @tparam T the type of the object that execute the actions on the [[World]].
 */
trait WorldActions[+T <: Timeable] extends Timeable :

  /**
   * The time elapsed in the world on every [[Timeable]]
   *
   * @param dt the time elapsed since the last update
   * @return the object that execute the actions on the world with the time elapsed for every [[Timeable]].
   */
  override def timeElapsed(dt: DeltaTime): T

  /**
   * Move all the [[Moveable]] in the world.
   *
   * @return the object that execute the actions on the world with the [[Moveable]] moved.
   */
  def moveElements(): T

  /**
   * Check the collisions between the [[Collisionable]] in the world.
   *
   * @return the object that execute the actions on the world and the [[Collision]] checked.
   */
  def checkCollisions(): (T, Set[Collision])

  /**
   * Activate the special effects of the [[Collisionable]].
   *
   * @return the object that execute the actions on the world with the special effects activated.
   */
  def activateSpecialAbility(): T

  /**
   * Remove the elements that has reached the destination
   *
   * @return the object that execute the actions on the world without the elements have reached the destination
   */
  def removeElementsThatReachedDestinations(): T

  /**
   * Shoot a [[model.missile.Missile]] from the [[model.ground.Ground]]
   *
   * @param destination the destination of the missile
   * @return the object that execute the actions on the world with the missile shot.
   */
  def shootMissile(destination: Point2D): T

