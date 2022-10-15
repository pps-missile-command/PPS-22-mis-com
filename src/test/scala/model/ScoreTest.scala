package model

import model.collisions.{Affiliation, Damageable, DamageableTest, Damager, DamagerTest, Distance, applyDamage, calculateCollisions}
import model.elements2d.Point2D
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class ScoreTest extends AnyFeatureSpec with GivenWhenThen :
  info("When a Scorable object has been destroyed, it increases the score of the player")
  info("If the scorable isn't destroyed, the score doesn't change")

  given distance: Distance = 0.1

  Feature("Don't add score") {
    Scenario("The objects don't collide") {
      Given("Two objects that don't collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(10, 10), Affiliation.Friendly)
      assert(!damageable.isCollidingWith(damager))

      When("Calculate the score")
      val update = applyDamage(calculateCollisions(List(damager, damageable)))
      val newScore = calculateNewScore(update, score)

      Then("The score doesn't change")

      assert(newScore == score)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(!damageable.isDestroyed)
          case _ => assert(element._2.isEmpty)
    }

    Scenario("The objects collide but the damageable is not destroyed") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))

      When("Calculate the score")
      val update = applyDamage(calculateCollisions(List(damager, damageable)))
      val newScore = calculateNewScore(update, score)

      Then("The score doesn't change")

      assert(newScore == score)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(!damageable.isDestroyed)
          case _ => assert(element._2.isEmpty)
    }

    Scenario("The objects collide but the damager is not owed to the player") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager))

      When("Calculate the score")
      val update = applyDamage(calculateCollisions(List(damager, damageable)))
      val newScore = calculateNewScore(update, score)

      Then("The score doesn't change")

      assert(newScore == score)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(damageable.isDestroyed)
          case _ => assert(element._2.isEmpty)
    }
  }

  Feature("Add score") {
    Scenario("The objects collide and the damageable is destroyed and the score should be increase") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))

      When("Calculate the score")
      val update = applyDamage(calculateCollisions(List(damager, damageable)))
      val newScore = calculateNewScore(update, score)

      Then("The score changes")

      assert(newScore == 1)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(damageable.isDestroyed)
          case _ => assert(element._2.isEmpty)
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

      When("Calculate the score")
      val update = applyDamage(calculateCollisions(List(damager, damageable1, damageable2)))
      val newScore = calculateNewScore(update, score)

      Then("The score changes")

      assert(update.size == 3)
      assert(newScore == 2)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(damageable.isDestroyed)
          case _ => assert(element._2.isEmpty)
    }

    Scenario("The objects collide and the damageable is destroyed and the score should be increase, starting score is not 0") {
      Given("Two objects that collide")
      val score = 3
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))

      When("Calculate the score")
      val update = applyDamage(calculateCollisions(List(damager, damageable)))
      val newScore = calculateNewScore(update, score)

      Then("The score changes")

      assert(newScore == 4)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(damageable.isDestroyed)
          case _ => assert(element._2.isEmpty)
    }
  }





