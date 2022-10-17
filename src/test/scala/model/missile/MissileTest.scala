package model.missile
import model.collisions.Affiliation
import model.elements2d.Point2D
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

object MissileTest:

  //retta passante per due punti
  private val damage = 10
  private val velocity = 10
  private val startPosition = Point2D(0,0)
  private val finalPosition = Point2D(10,10)
  private val dt = 0.1

  //given affiliation: Affiliation = Affiliation.Enemy
  val TestMissile : () => Missile = () => Missile(initialLife, damage, velocity, startPosition, finalPosition)


class MissileTest extends AnyFunSpec :

  import MissileTest._

  describe("A missile") {
    describe("with enemy role") {
      it("should have enemy affiliation") {
        val missile = Missile.enemyMissile(initialLife, damage, velocity, startPosition, finalPosition)
        assert(missile.affiliation == Affiliation.Enemy)
      }
    }
    it("should be scorable") {
      val missile = Missile.enemyMissile(initialLife, damage, velocity, startPosition, finalPosition)
      val mMissile = missile.takeDamage(2)
      assert(missile.isInstanceOf[Scorable])
    }
    describe("with friendly role") {
      it("should have friendly affiliation") {
        given Random()
        val missile = GenerateRandomMissile(BasicMissile(Affiliation.Friendly), finalPosition)
        assert(missile.get.affiliation == Affiliation.Friendly)
      }
    }
    it("should calculate its own direction based on start position and final position") {
      val missile = TestMissile()
      assert(missile.direction == (finalPosition <--> startPosition).normalize)
    }
    it("should move along its direction") {
      val missile = TestMissile()
      val tmpMissile = missile.timeElapsed(dt)
      val movedMissile = missile.move()
      assert(movedMissile.position == (missile.position --> (missile.direction * missile.velocity * dt)))
    }
    it("should decrease its lifepoints when damaged") {
      val missile = TestMissile()
      val damagedMissile = missile.takeDamage(damage)
      assert(damagedMissile.currentLife == initialLife - damage)
    }
    it("should be destroyed if its lifepoints are lower than 0") {
      val missile = TestMissile()
      val damagedMissile = missile.takeDamage(initialLife)
      assert(damagedMissile.isDestroyed)
    }
  }



