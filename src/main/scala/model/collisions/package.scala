package model

import model.collisions.{Collisionable, Damageable}
import model.collisions.PimpingByCollisionable._
import model.collisions.PimpingByCollisions._


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
  extension(collisionables: Set[Collisionable])
    private def totalDamageFor(collisionable: Collisionable): LifePoint =
      collisionables
        .filter(_.affiliation != collisionable.affiliation)
        .toSeq
        .map(_.damage)
        .sum

    /**
     * Apply the damage to the damageable object in the set.
     *
     * @param collisions a set with all the detected collisions
     * @return a tuple with the updated collisionables and the collisions
     */
    def applyDamagesBasedOn(collisions: Set[Collision]): (Set[Collisionable], Set[Collision]) =
      val mapping: Map[Collisionable, Collisionable] =
        (for
          collisionable <- collisionables
          allCollidingCollisionables = collisions allCollisionablesThatCollideWith collisionable
          if allCollidingCollisionables.nonEmpty
          totalDamage = allCollidingCollisionables totalDamageFor collisionable
          newCollisionable = collisionable applyDamage totalDamage
        yield
          collisionable -> newCollisionable)
          .toMap
      val unUpdateCollisionables = collisions.allCollisionablesThatDoesntCollideWith(collisionables, mapping.keySet)
      (mapping.values.toSet ++ unUpdateCollisionables, collisions updateCollisionablesContained mapping)

    /**
     * Check the collision between the set of collisionables,
     * return a set that has the collisions of the collisionables.
     * The methods only check the collision between the collisionables that aren't on the same side .
     *
     * @return a set with all the collisions
     */
    def calculateCollisions: Set[Collision] =
      collisionables calculateCollisionsWith collisionables

    /**
     * Check the collision between the current collisionables and the the set of other collisionables,
     * return a set that has the collisions of the collisionables.
     * The methods only check the collision between the collisionables that aren't on the same side .
     *
     * @param otherCollisionables the set of collisionable objects to check if collide with the current one set
     * @tparam CO the type of the other set of collisionables
     * @return a set with all the collisions between the two sets
     */
    def calculateCollisionsWith[CO <: Collisionable](otherCollisionables: Set[CO]): Set[Collision] =
      given Distance = 5.0

      def areOnTheSameSide(collisionable: Collisionable, otherCollisionable: Collisionable): Boolean =
        collisionable.affiliation == otherCollisionable.affiliation

      for
        collisionable <- collisionables
        if collisionable != null
        if collisionable.isNotDestroyed
        collisionable2 <- otherCollisionables
        if collisionable2 != null
        if collisionable != collisionable2
        if collisionable2.isNotDestroyed
        if !areOnTheSameSide(collisionable, collisionable2)
        if collisionable isCollidingWith collisionable2
      yield
        Collision(collisionable, collisionable2)
