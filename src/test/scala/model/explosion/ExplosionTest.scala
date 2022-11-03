package model.explosion

import model.collisions.{Affiliation, Collisionable, HitBox}
import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D
import model.collisions.Distance
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import model.missile.*

object ExplosionTest:
  val damage = 10
  val radius = 5
  val position = Point2D(0, 0)

class ExplosionTest extends AnyFunSpec :
  import ExplosionTest.*
  import model.missile.MissileTest.TestMissile
  import model.explosion.MaxTime
  given maxTime: MaxTime = 10

  describe("An explosion") {
    describe("circular") {
      describe("when created") {
        it("should have damage specified in input") {
          given affiliation: Affiliation = Affiliation.Friendly
          val explosion = Explosion(damage, radius, position)
          assert(explosion.damageInflicted == damage)
        }
        it("should have valid damage value even if a negative one is specified") {
          given affiliation: Affiliation = Affiliation.Friendly
          val explosion = Explosion(-damage, radius, position)
          assert(explosion.damageInflicted == damage)
        }
        it("should have valid radius value even if a negative one is specified") {
          given affiliation: Affiliation = Affiliation.Friendly
          val explosion = Explosion(damage, -radius, position)
          assert(explosion.radius == radius)
        }
        it("should throw IllegalArgumentException when damage is assigned to 0") {
          assertThrows[IllegalArgumentException] {
            given affiliation: Affiliation = Affiliation.Friendly
            Explosion(0, radius, position)
          }
        }
        it("should throw IllegalArgumentException when radius is assigned to 0") {
          assertThrows[IllegalArgumentException] {
            given affiliation: Affiliation = Affiliation.Friendly
            Explosion(damage, 0, position)
          }
        }
      }
      describe("when a point is inside its action radius") {
        it("should recognize a collision") {
          given distance : Distance = 1
          given affiliation: Affiliation = Affiliation.Friendly
          val explosion = Explosion(damage, radius, position)
          val startPosition = Point2D(3,3)
          val point = new Collisionable {

            override protected def hitBox: HitBox = HitBoxPoint(startPosition)

            override def affiliation: Affiliation = Affiliation.Friendly
          }

          assert(explosion.isCollidingWith(point))

        }
      }
      describe("when exploding") {
        it("should be into exploding state until the max time") {
          given affiliation: Affiliation = Affiliation.Friendly
          val explosion = Explosion(damage, radius, position)
          val newExplosion = explosion.timeElapsed(14)
          assert(newExplosion.terminated, true)
        }
      }
    }
    describe("A missile") {
      describe("when exploding") {
        it("should generate an explosion with its own affiliation type") {
          val missile = Missile(0,
            damage,
            velocity,
            Point2D(0, 0), Point2D(0, 0))
          val explosion = missile.explode
          assert(missile.affiliation == Affiliation.Friendly)
          assert(explosion.affiliation == Affiliation.Friendly)
        }
      }
    }
  }