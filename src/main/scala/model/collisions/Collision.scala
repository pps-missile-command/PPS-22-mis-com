package model.collisions

import model.collisions

/**
 * Companion object for the [[Collision]] class.
 */
object Collision:

  /**
   * Creates a new [[Collision]] from multiple collisionables
   *
   * @param collisionables the collisionables that collide
   * @return a new [[Collision]] from the collisionables
   */
  def apply(collisionables: Collisionable*): Collision =
    Collision(collisionables.toSet)

/**
 * A collision is a set of [[Collisionable]]s that collide
 *
 * @param involvedCollisionables the collisionables that collide
 */
case class Collision(involvedCollisionables: Set[Collisionable]):

  /**
   * Checks if the collision contains a specific collisionable
   *
   * @param collisionable the collisionable to check
   * @return true if the collision contains the collisionable, false otherwise
   */
  def contains(collisionable: Collisionable): Boolean =
    involvedCollisionables
      .contains(collisionable)

  /**
   * Checks if one of the collisonable  in the collision has a specific property
   *
   * @param p the property to check
   * @return true if one of the collisionable has the property, false otherwise
   */
  def exists(p: Collisionable => Boolean): Boolean =
    involvedCollisionables
      .exists(p)

  /**
   * Given a collisionable, returns the other collisionable in the collision or empty if the collisionable is not in the collision
   *
   * @param collisionable the collisionable to check
   * @return the other collisionable in the collision or empty if the collisionable is not in the collision
   */

  def otherElementsOfCollision(collisionable: Collisionable): Set[Collisionable] =
    involvedCollisionables match
      case _ if contains(collisionable) => involvedCollisionables - collisionable
      case _ => Set.empty