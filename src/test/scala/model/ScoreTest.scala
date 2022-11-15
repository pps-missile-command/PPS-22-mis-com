package model

import model.collisions.{Affiliation, Collisionable, Damageable, DamageableTest, Damager, DamagerTest, Distance, applyDamagesBasedOn, calculateCollisions}
import model.elements2d.Point2D
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class ScoreTest extends AnyFeatureSpec with GivenWhenThen :
  info("When a Scorable object has been destroyed, it increases the score of the player")
  info("If the scorable isn't destroyed, the score doesn't change")

  extension (collisionables: Set[Collisionable])
    def allDestroyed: Int =
      collisionables.
        filter(_.isInstanceOf[Damageable]).
        map(_.asInstanceOf[Damageable]).
        count(_.isDestroyed)


  given distance: Distance = 0.1

  Feature("Don't increases score") {
    Scenario("The objects don't collide") {
      Given("Two objects that don't collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(10, 10), Affiliation.Friendly)
      assert(!damageable.isCollidingWith(damager))

      When("Calculate the score")
      val entities = Set(damager, damageable)
      val (entitiesUpdate, collisionsUpdate) = entities applyDamagesBasedOn entities.calculateCollisions
      val newScore = score calculateNewScoreBasedOn collisionsUpdate

      Then("The score doesn't change")
      assert(newScore == score)
      assert(entitiesUpdate.allDestroyed == 0)
    }

    Scenario("The objects collide but the damageable is not destroyed") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))

      When("Calculate the score")
      val entities = Set(damager, damageable)
      val (entitiesUpdate, collisionsUpdate) = entities applyDamagesBasedOn entities.calculateCollisions
      val newScore = score calculateNewScoreBasedOn collisionsUpdate

      Then("The score doesn't change")
      assert(newScore == score)
      assert(entitiesUpdate.allDestroyed == 0)
    }

    Scenario("The objects collide but the damager is not owed by the player") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager))

      When("Calculate the score")
      val entities = Set(damager, damageable)
      val (entitiesUpdate, collisionsUpdate) = entities applyDamagesBasedOn entities.calculateCollisions
      val newScore = score calculateNewScoreBasedOn collisionsUpdate

      Then("The score doesn't change")
      assert(newScore == score)
      assert(entitiesUpdate.allDestroyed == 1)
    }
  }

  Feature("Increases score") {
    Scenario("The objects collide and the damageable is destroyed and the score should be increase") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))
      val expectedScore = 1

      When("Calculate the score")
      val entities = Set(damager, damageable)
      val (entitiesUpdate, collisionsUpdate) = entities applyDamagesBasedOn entities.calculateCollisions
      val newScore = score calculateNewScoreBasedOn collisionsUpdate

      Then("The score doesn't change")
      assert(newScore == expectedScore)
      assert(entitiesUpdate.allDestroyed == 1)
    }

    Scenario("Multiple objects are destroyed") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 1
      val damageable1 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damageable2 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable1.isCollidingWith(damager))
      assert(damageable2.isCollidingWith(damager))

      val expectedScore = 2

      When("Calculate the score")
      val entities = Set(damager, damageable1, damageable2)
      val (entitiesUpdate, collisionsUpdate) = entities applyDamagesBasedOn entities.calculateCollisions
      val newScore = score calculateNewScoreBasedOn collisionsUpdate

      Then("The score doesn't change")
      assert(newScore == expectedScore)
      assert(entitiesUpdate.allDestroyed == 2)
    }

    Scenario("The objects collide and the damageable is destroyed and the score should be increase, starting score is not 0") {
      Given("Two objects that collide")
      val score = 3
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))

      val expectedScore = 4

      When("Calculate the score")
      val entities = Set(damager, damageable)
      val (entitiesUpdate, collisionsUpdate) = entities applyDamagesBasedOn entities.calculateCollisions
      val newScore = score calculateNewScoreBasedOn collisionsUpdate

      Then("The score doesn't change")
      assert(newScore == expectedScore)
      assert(entitiesUpdate.allDestroyed == 1)
    }
  }





