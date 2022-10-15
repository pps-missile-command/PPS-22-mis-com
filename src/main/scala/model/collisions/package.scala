package model

import model.collisions.Damageable


/**
 * Trait, class, object and constant for the representation of element that collide.
 */
package object collisions:

  import model.collisions._

  /**
   * Alias for life measure
   */
  type LifePoint = Int

  /**
   * The life constant for destroy an object
   */
  val lifePointDeath = 0

  /**
   * Alias for the distance between points in the hit box area
   */
  type Distance = Double

  /**
   * Apply the damage to the damageable object as key in the map.
   *
   * @param collisionResults a map that has as keys the damageable objects and as values a list with the damaeger that collides with them
   * @return a map that has as keys the damageable objects update with the new damage and as values a list with the damaeger that collides with them
   */
  def applyDamage(collisionResults: Map[Collisionable, List[Collisionable]]): Map[Collisionable, List[Collisionable]] =
    def getDamage(collisionable: Collisionable): LifePoint =
      collisionable match
        case damager: Damager => damager.damageInflicted
        case _ => 0

    collisionResults.map(
      (collisionable, damagers) =>
        collisionable match
          case damageable: Damageable => (damageable.takeDamage(damagers.map(getDamage _).sum), damagers)
          case _ => (collisionable, damagers)
    )


  /**
   * Check the collision between multiple collidables,
   * return a map that has as keys the damageable objects and as values list with the damaeger that collides with them.
   * The methods only check the collision between the damager and damageable that aren' on the same side .
   *
   * @param collisionables the collidables to check
   * @return a map that has as keys the damageable objects and as values a list with the damaeger that collides with them
   */
  def calculateCollisions(collisionables: List[Collisionable]): Map[Collisionable, List[Collisionable]] =
    given Distance = 0.1

    def isDestroyed(collisionable: Collisionable): Boolean =
      collisionable match
        case damageable: Damageable => damageable.isDestroyed
        case _: Damager => false


    def areOnTheSameSide(collisionable1: Collisionable, collisionable2: Collisionable): Boolean =
      collisionable1.affiliation == collisionable2.affiliation

    def isADamager(collisionable: Collisionable): Boolean =
      collisionable match
        case _: Damager => true
        case _ => false

    def isADamageable(collisionable: Collisionable): Boolean =
      collisionable match
        case _: Damageable => true
        case _ => false

    val realCollision =
      (for
        damager <- collisionables
        if !isDestroyed(damager)
        if isADamager(damager)
        damageable <- collisionables
        if !isDestroyed(damageable)
        if isADamageable(damageable)
        if !areOnTheSameSide(damager, damageable)
        if damager.isCollidingWith(damageable)
      yield
        (damageable, damager))
        .foldLeft(Map[Collisionable, List[Collisionable]]()
          .withDefaultValue(List.empty))((res, v) => {
          val key = v._1
          res + (key -> (res(key) :+ v._2))
        })
    (for
      collisionable <- collisionables
    yield
      (collisionable, if realCollision.contains(collisionable) then realCollision(collisionable) else List.empty)).toMap