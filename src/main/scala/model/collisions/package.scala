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
   * Check the collision between multiple collidables and apply the damages.
   *
   * @param collisionables the collidables to check
   * @return a list of the collidables with their life updated
   */
  def checkCollision(collisionables: List[Collisionable]): List[Collisionable] =
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

    def calculateDamage(possibleDamager: Collisionable, damageable: Collisionable): (Collisionable, LifePoint) =
      possibleDamager match
        case damager: Damager => damageable match
          case _: Damageable => (damageable, damager.damageInflicted)

    def applyDamage(collisionable: Collisionable, damage: LifePoint): Collisionable =
      collisionable match
        case damageable: Damageable => damageable.takeDamage(damage)
        case _ => collisionable
        
    val damagesToInflict: Map[Collisionable, LifePoint] =
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
        calculateDamage(damager, damageable))
        .foldLeft(Map[Collisionable, LifePoint]()
          .withDefaultValue(0))((res, v) => {
          val key = v._1
          res + (key -> (res(key) + v._2))
        })
    for
      collisionable <- collisionables
    yield
      applyDamage(collisionable, damagesToInflict(collisionable))