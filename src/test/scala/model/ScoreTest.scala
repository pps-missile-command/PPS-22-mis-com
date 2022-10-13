package model

import model.collisions.{Affiliation, Damageable, DamageableTest, Damager, DamagerTest, checkCollision}
import model.elements2d.Point2D
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class ScoreTest extends AnyFeatureSpec with GivenWhenThen :
  info("When a Scorable object has been destroyed, it increases the score of the player")
  info("If the scorable isn't destroyed, the score doesn't change")

  Feature("Don't add score") {
    Scenario("The objects don't collide") {
      Given("Two objects that don't collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(10, 10), Affiliation.Friendly)
      assert(!damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Calculate the score")
      val update = checkCollision(List(damageable, damager))
      val newScore = calculateNewScore(update, score)

      Then("The score doesn't change")

      assert(newScore == score)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(!damageable.isDestroyed)
          case _: Damager => assert(true)
    }

    Scenario("The objects collide but the damageable is not destroyed") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Calculate the score")
      val update = checkCollision(List(damageable, damager))
      val newScore = calculateNewScore(update, score)

      Then("The score doesn't change")

      assert(newScore == score)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(!damageable.isDestroyed)
          case _: Damager => assert(true)
    }
  }

  Feature("Add score") {
    Scenario("The objects collide and the damageable is destroyed and the score should be increase") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Calculate the score")
      val update = checkCollision(List(damageable, damager))
      val newScore = calculateNewScore(update, score)

      Then("The score changes")

      assert(newScore == 1)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(damageable.isDestroyed)
          case _: Damager => assert(true)
    }

    Scenario("Multiple objects are destroyed") {
      Given("Two objects that collide")
      val score = 0
      val initialLife = 1
      val damageable1 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damageable2 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable1.isCollidingWith(damager)(using distance = 0.1))
      assert(damageable2.isCollidingWith(damager)(using distance = 0.1))

      When("Calculate the score")
      val update = checkCollision(List(damageable1, damageable2, damager))
      val newScore = calculateNewScore(update, score)

      Then("The score changes")

      assert(newScore == 2)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(damageable.isDestroyed)
          case _: Damager => assert(true)
    }

    Scenario("The objects collide and the damageable is destroyed and the score should be increase, starting score is not 0") {
      Given("Two objects that collide")
      val score = 3
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Neutral)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Calculate the score")
      val update = checkCollision(List(damageable, damager))
      val newScore = calculateNewScore(update, score)

      Then("The score changes")

      assert(newScore == 4)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(damageable.isDestroyed)
          case _: Damager => assert(true)
    }


  }





