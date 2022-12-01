package model

import model.collisions.{Collision, Collisionable, _}
import model.ground.Ground
import model.explosion.Explosion
import model.collisions.PimpingByCollisionable._
import model.collisions.PimpingByCollisionables._
import model.collisions.PimpingByCollisions._

/**
 * A support object used to manage the result of the collisions and be able to keep the code fluent and more clear.
 */
private object PimpingByCollisionsResults:

  /**
   * Alias for a tuple of of a set of [[Collisionable]]s, a set of [[Collision]]s and [[Ground]].
   */
  type CollisionablesCollisionsGroundResult = (Set[Collisionable], Set[Collision], Ground)

  extension (tuple: CollisionablesCollisionsGroundResult)

    /**
     * from a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and [[Ground]], 
     * it returns a tuple of [[World]] and a set of [[Collision]]s.
     *
     * @return a tuple of [[World]] and a set of [[Collision]]s created from the tuple.
     */
    def generateWorld(): (World, Set[Collision]) =
      val (newCollisionables, collisions, newGround) = tuple
      val newWorld = World(newGround, newCollisionables)
      (newWorld, collisions)

  extension (tuple: (Set[Collisionable], Set[Collision]))

    /**
     * From a tuple of a set of [[Collisionable]]s and a set of [[Collision]]s,
     * it return a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and a set of [[Explosion]]s.
     * The set of [[Explosion]]s is created from the set of [[Collisionable]]s using the missile explosion.
     * The exploded missiles aren't removed from the set of [[Collisionable]]s.
     *
     * @return a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and a set of [[Explosion]]s.
     */
    def explodeDestroyedMissile(): CollisionablesCollisionsExplosionsResult =
      val (collisionables, collisions) = tuple
      val explosions = collisionables.explosionsOfDestroyedMissiles
      (collisionables, collisions, explosions)

    /**
     * From a tuple of a set of [[Collisionable]]s and a set of [[Collision]]s,
     * it return a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and [[Ground]].
     * The [[Ground]] is created from the set of [[Collisionable]]s removing all the cities and the missiles batteries.
     *
     * @return a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and [[Ground]].
     */

    def splitGroundFromCollisionables(): CollisionablesCollisionsGroundResult =
      val (collisionables, collisions) = tuple
      val (newGround, other) = collisionables.splitGroundFromOther
      (other, collisions, newGround)

  /**
   * Alias for a tuple of of a set of [[Collisionable]]s, a set of [[Collision]]s and a set of [[Explosion]]s.
   */
  type CollisionablesCollisionsExplosionsResult = (Set[Collisionable], Set[Collision], Set[Explosion])
  /**
   * Alias for the type of the function used to calculate the [[CollisionablesCollisionsExplosionsResult]]
   */
  type CheckCollisionWithExplosions = (Set[Collisionable], Set[Explosion]) => (Set[Collision], Set[Explosion]) => CollisionablesCollisionsExplosionsResult

  extension (result: CollisionablesCollisionsExplosionsResult)

    /**
     * From a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and a set of [[Explosion]]s,
     * it remove the destroyed elements from the set of [[Collisionable]]s but keep the missiles batteries and the cities.
     */
    def removeDestroyedCollisionables(): CollisionablesCollisionsExplosionsResult =
      val (collisionables, collisions, explosions) = result
      val notDestroyed =
        collisionables
          .filter(collisionable =>
            !collisionable.isDestroyed || collisionable.isCity || collisionable.isMissileBattery
          )
      (notDestroyed, collisions, explosions)

    /**
     * From a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and a set of [[Explosion]]s,
     * it check the collisions between the set of [[Collisionable]]s and the set of [[Explosion]]s.
     * It also update the elements of the set of [[Collision]]s with the new references of the elements involved in the collisions.
     * If the [[Explosion]]s in the tuple are empty it returns the tuple.
     *
     * @param oldExplosions the set of [[Explosion]]s before the collisions.
     * @param check         the function used to calculate the [[CollisionablesCollisionsExplosionsResult]].
     * @return a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and a set of [[Explosion]]s.
     */
    def checkCollsionsWithNewExplosions(oldExplosions: Set[Explosion])(check: CheckCollisionWithExplosions): CollisionablesCollisionsExplosionsResult =
      val (collisionables, collisions, explosions) = result
      explosions match
        case _ if explosions.isEmpty =>
          (collisionables, collisions, oldExplosions)
        case _ => check(collisionables, explosions)(collisions, explosions ++ oldExplosions)

    /**
     * From a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s and a set of [[Explosion]]s,
     * it adds the new [[Explosion]]s to the set of [[Collisionable]]s.
     *
     * @return a tuple of a set of [[Collisionable]]s, a set of [[Collision]]s.
     */
    def addExplosions(): (Set[Collisionable], Set[Collision]) =
      val (collisionables, collisions, explosions) = result
      val newCollisionables = collisionables ++ explosions
      (newCollisionables, collisions)
