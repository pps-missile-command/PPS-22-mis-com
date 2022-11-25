package model

import model.behavior.Timeable
import model.collisions.Collision

/**
 * Trait that represents the actions that can be performed on a [[Player]].
 *
 * @tparam T the type of the object that execute the actions on the [[Player]].
 */
trait PlayerActions[+T <: Timeable] extends Timeable :

  /**
   * The time elapsed for the [[Player]]
   *
   * @param dt the time elapsed since the last update
   * @return the object that execute the actions on the [[Player]] with the time elapsed.
   */
  override def timeElapsed(dt: DeltaTime): T

  /**
   * update the [[Player]] score using the collisions given.
   *
   * @param collisions the collisions detected in the world.
   * @return the object that execute the actions on the [[Player]] with the updated score.
   */
  def updateScoreByCollisions(collisions: Set[Collision]): T
