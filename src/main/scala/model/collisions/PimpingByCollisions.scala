package model.collisions

import model.collisions.Collision
import model.collisions.PimpingByCollisionable._

/**
 * Object that contains pimping methods for a [[Set]] of [[Collision]]
 */
object PimpingByCollisions:

  extension (collisions: Set[Collision])

    /**
     * Return all the Collionables that are destroyed and are Scorable
     *
     * @return a set of collionsionables that are scorable and destroyed in the collisions
     */
    def allScorableDestroyedThatCollide: Set[Collisionable] =
      collisions
        .flatten
        .filter(_.isScorable)
        .filter(_.isDestroyed)

    /**
     * Return all the Collionables that destroyed the Scorable
     *
     * @param scorableDestroyed the scorable destroyed
     * @return a set of collisionables that collide with the scorable
     */

    def allElementsThatDestroyed(scorableDestroyed: Collisionable): Set[Collisionable] =
      collisions
        .flatMap(_.otherElementsOfCollision(scorableDestroyed))
        .filter(_.isDamager)

    /**
     * Return all the Collsions that involve the collisionable
     *
     * @param collisionable the collisionable to search in the collisions
     * @return a set of collisions that involve the collisionable
     */
    def allCollisionsThatInvolves(collisionable: Collisionable): Set[Collision] =
      collisions
        .filter(_.contains(collisionable))

    /**
     * Return all the collisionables that collide with the given collisionable
     *
     * @param collisionable the collisionable to search in the collisions
     * @return a set of collisionables that collide with the given collisionable
     */
    def allCollisionablesThatCollideWith(collisionable: Collisionable): Set[Collisionable] =
      collisions
        .allCollisionsThatInvolves(collisionable)
        .flatMap(_
          .otherElementsOfCollision(collisionable)
        )

    /**
     * Return all the collisionables that collide with the given set of collisionables
     * It's possible that, if the collionable collide with each other, they are returned even if they are in the given set
     *
     * @param collisionables the set of collisionables to search in the collisions
     * @return a set of collisionables that collide with the given set of collisionables
     */
    def allCollisionablesThatCollideWith(collisionables: Set[Collisionable]): Set[Collisionable] =
      collisionables
        .flatMap(collisionable =>
          collisions
            .allCollisionablesThatCollideWith(collisionable)
        )

    /**
     * Return all the collisionables that doesn't collide with the given set of collisionables
     *
     * @param all           the set of collisionables all collisionables
     * @param doesntCollide the set of collisionables that we want than none of the return collisonables collide
     * @return a set of collisionables that doesn't collide with the given set of collisionables form all
     */
    def allCollisionablesThatDoesntCollideWith(all: Set[Collisionable], doesntCollide: Set[Collisionable]): Set[Collisionable] =
      all.filter(_ != null) -- collisions.allCollisionablesThatCollideWith(doesntCollide) -- doesntCollide

    /**
     * Return the collisions with the new values of the collisionables
     *
     * @param mapping the mapping of the collisionables
     * @return the collisions with the new values of the collisionables
     */
    def updateCollisionablesContained(mapping: Map[Collisionable, Collisionable]): Set[Collision] =
      collisions
        .map(collision =>
            collision
              .map(collisionable =>
                mapping
                  .getOrElse(collisionable, collisionable)
              )
        )
