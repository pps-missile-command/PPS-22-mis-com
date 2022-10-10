package model.explosion

import model.collisions.{Affiliation, Collisionable, HitBox}
import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D
import model.collisions.Distance
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object ExplosionTest:
  val damage = 10
  val radius = 5
  val position = Point2D(0, 0)

class ExplosionTest extends AnyFunSpec :
  import ExplosionTest.*
  import model.missile.MissileTest.TestMissile

  describe("An explosion") {
    describe("circular") {
      describe("when created") {
        it("should have damage specified in input") {
          val explosion = Explosion(damage, radius, position, Affiliation.Friendly)
          assert(explosion.damageInflicted == damage)
        }
        it("should have valid damage value even if a negative one is specified") {
          val explosion = Explosion(-damage, radius, position, Affiliation.Friendly)
          assert(explosion.damageInflicted == damage)
        }
        it("should have valid radius value even if a negative one is specified") {
          val explosion = Explosion(damage, -radius, position, Affiliation.Friendly)
          assert(explosion.radius == radius)
        }
        it("should throw IllegalArgumentException when damage is assigned to 0") {
          assertThrows[IllegalArgumentException] {
            Explosion(0, radius, position, Affiliation.Friendly)
          }
        }
        it("should throw IllegalArgumentException when radius is assigned to 0") {
          assertThrows[IllegalArgumentException] {
            Explosion(damage, 0, position, Affiliation.Friendly)
          }
        }
      }
      describe("when a point is inside its action radius") {
        it("should recognize a collision") {
          given distance : Distance = 1
          val explosion = Explosion(damage, radius, position, Affiliation.Friendly)
          val startPosition = Point2D(3,3)
          val point = new Collisionable {

            override protected def hitBox: HitBox = HitBoxPoint(startPosition)

            override def affiliation: Affiliation = Affiliation.Friendly
          }

          assert(explosion.isCollidingWith(point))
        }
      }
    }
  }