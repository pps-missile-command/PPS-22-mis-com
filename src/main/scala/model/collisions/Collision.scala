package model.collisions

import model.collisions

object Collision:
  def apply(collisionables: Collisionable*): Collision =
    Collision(collisionables.toSet)

case class Collision(involvedCollisionables: Set[Collisionable]):
  def contains(collisionable: Collisionable): Boolean =
    involvedCollisionables
      .contains(collisionable)

  def exists(p: Collisionable => Boolean): Boolean =
    involvedCollisionables
      .exists(p)

  def otherElementsOfCollision(collisionable: Collisionable): Set[Collisionable] =
    involvedCollisionables match
      case _ if contains(collisionable) => involvedCollisionables - collisionable
      case _ => Set.empty