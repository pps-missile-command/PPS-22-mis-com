package model.missile
import model.collisions.Affiliation
import model.elements2d.Point2D
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object MissileTest:

  //retta passante per due punti
  private val damage = 10
  private val velocity = 10
  private val startPosition = Point2D(0,0)
  private val finalPosition = Point2D(10,10)
  private val dt = 0.1

  val TestMissile : (Affiliation) => Missile = Missile(initialLife, _, damage, velocity, startPosition, finalPosition)


class MissileTest extends AnyFunSpec :

  import MissileTest._

  describe("A missile") {
    describe("with enemy role") {
      it("should have enemy affiliation") {
        val missile = TestMissile(Affiliation.Enemy)
        assert(missile.affiliation == Affiliation.Enemy)
      }
    }
    describe("with friendly role") {
      it("should have friendly affiliation") {
        val missile = TestMissile(Affiliation.Friendly)
        assert(missile.affiliation == Affiliation.Friendly)
      }
    }
    describe("with neutral role") {
      it("should have neutral affiliation") {
        val missile = TestMissile(Affiliation.Neutral)
        assert(missile.affiliation == Affiliation.Neutral)
      }
    }
    it("should calculate its own direction based on start position and final position") {
      val missile = TestMissile(Affiliation.Neutral)
      assert(missile.direction == (finalPosition <--> startPosition).normalize)
    }
    it("should move along its direction") {
      val missile = TestMissile(Affiliation.Neutral)
      val movedMissile = missile.move(dt)
      assert(movedMissile.position == (missile.position --> (missile.direction * missile.velocity * dt)))
    }
    it("should decrease its lifepoints when damaged") {
      val missile = TestMissile(Affiliation.Neutral)
      val damagedMissile = missile.takeDamage(damage)
      assert(damagedMissile.currentLife == initialLife - damage)
    }
    it("should be destroyed if its lifepoints are lower than 0") {
      val missile = TestMissile(Affiliation.Neutral)
      val damagedMissile = missile.takeDamage(initialLife)
      assert(damagedMissile.isDestroyed)
    }
  }



