package model.collisions

import model.collisions.Collision
import model.collisions.PimpingByCollisionable._

object PimpingByCollisions:

  extension (collisions: Set[Collision])

    def allScorableDestroyedThatCollide: Set[Collisionable] =
      collisions
        .flatMap(_.involvedCollisionables)
        .filter(_.isScorable)
        .filter(_.isDestroyed)

    def allElementsThatDestroyed(scorableDestroyed: Collisionable): Set[Collisionable] =
      collisions
        .flatMap(_.otherElementsOfCollision(scorableDestroyed))
        .filter(_.isDamager)

    def allCollisionsThatInvolves(collisionable: Collisionable): Set[Collision] =
      collisions
        .filter(_.contains(collisionable))

    def allCollisionablesThatCollideWith(collisionable: Collisionable): Set[Collisionable] =
      collisions
        .allCollisionsThatInvolves(collisionable)
        .flatMap(_
          .otherElementsOfCollision(collisionable)
        )

    def allCollisionablesThatCollideWith(collisionables: Set[Collisionable]): Set[Collisionable] =
      collisionables
        .flatMap(collisionable =>
          collisions
            .allCollisionablesThatCollideWith(collisionable)
        )

    def allCollisionablesThatDoesntCollideWith(all: Set[Collisionable], doesntCollide: Set[Collisionable]): Set[Collisionable] =
      all.filter(_ != null) -- collisions.allCollisionablesThatCollideWith(doesntCollide)

    def updateCollisionablesContained(mapping: Map[Collisionable, Collisionable]): Set[Collision] =
      collisions
        .map(collision =>
          Collision(
            collision
              .involvedCollisionables
              .map(mapping)
          )
        )
