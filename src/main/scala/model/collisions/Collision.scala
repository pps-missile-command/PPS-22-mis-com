package model.collisions

import model.collisions

/**
 * Companion object for the [[Collision]] type.
 */
object Collision:

  /**
   * Creates a new [[Collision]] from multiple [[Collisionable]]s
   *
   * @param collisionables the collisionables that collide
   * @return a new [[Collision]] from the [[Collisionable]]s
   */
  def apply(collisionables: Collisionable*): Collision =
    collisionables.toSet

  def apply(collisionables: Set[Collisionable]): Collision =
    collisionables

/**
 * A collision is a set of [[Collisionable]]s that collide
 */
type Collision = Set[Collisionable]

extension (collision: Collision)
  /**
   * Given a [[Collisionable]], returns the other collisionable in the collision or empty if the collisionable is not in the collision
   *
   * @param collisionable the [[Collisionable]] to check
   * @return the other [[Collisionable]] in the collision or empty if the collisionable is not in the collision
   */

  def otherElementsOfCollision(collisionable: Collisionable): Set[Collisionable] =
    collision match
      case _ if collision.contains(collisionable) => collision - collisionable
      case _ => Set.empty
