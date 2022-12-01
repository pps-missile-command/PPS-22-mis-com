package model.collisions

import model.elements2d.Point2D
import model.collisions._
import model.collisions.PimpingByCollisions._
import org.scalatest.GivenWhenThen
import org.scalatest.funspec.AnyFunSpec

class PimpingByCollisionsTest extends AnyFunSpec :

  private val initialLife = 1
  private val damageable1 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
  private val damageable2 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
  private val damageable3 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Enemy)

  describe("Some collisions") {

    describe("that are empty") {

      val collisions = Set.empty[Collision]

      it("shouldn't have any collisionables") {
        assert(collisions.isEmpty)
      }

      it("should return empty if asked for the collisionables that collide with a collisionable") {
        assert(collisions.allCollisionablesThatCollideWith(damageable1).isEmpty)
      }

      it("should return the set given if asked for the collisionables that doesn't collide collisiobable, except the given one") {
        val other = Set[Collisionable](damageable1, damageable3)
        assert(collisions.allCollisionablesThatDoesntCollideWith(other, Set(damageable1)) == Set(damageable3))
      }
    }

    describe("that are not empty") {

      val collisions = Set[Collision](Collision(damageable1, damageable2))

      it("should have collisionables") {
        assert(collisions.nonEmpty)
      }

      it("should return the collisionables that collide with a collisionable") {
        assert(collisions.allCollisionablesThatCollideWith(damageable1) == Set(damageable2))
      }

      it("should return the collisionables that doesn't collide collisiobable, except the given one") {
        val other = Set[Collisionable](damageable1, damageable3)
        assert(collisions.allCollisionablesThatDoesntCollideWith(other, Set(damageable1)) == Set(damageable3))
      }
    }
  }

