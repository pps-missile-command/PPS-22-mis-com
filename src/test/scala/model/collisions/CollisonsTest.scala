package model.collisions

import model.elements2d.Point2D
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import model.collisions._

class CollisonsTest extends AnyFeatureSpec with GivenWhenThen :
  info("When a Damageable object collides with an Damager object, it should be damaged, if they aren't on the same affiliation")
  info("If they are on the same affiliation, nothing should happen")
  info("If the Damageable object is already dead, nothing should happen")
  info("If the Damageable object has not enough life, it should be destroyed")

  Feature("Don't inflict damage") {
    Scenario("The objects don't collide") {
      Given("Two objects that don't collide")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(10, 10), Affiliation.Enemy)
      assert(!damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Check collision")
      val update = checkCollision(List(damageable, damager))

      Then("The damageable object should not be damaged")
      assert(update.size == 2)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(damageable.currentLife == initialLife)
          case damager: Damager => assert(damager.damageInflicted == 1)

    }

    Scenario("The object collide but they are of the same type") {
      Given("Two objects that collide but are of the same type")
      val initialLife = 3
      val damageable1 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damageable2 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Enemy)
      assert(damageable1.isCollidingWith(damageable2)(using distance = 0.1))

      When("Check collision")
      val update = checkCollision(List(damageable1, damageable2))

      Then("The damageable object shouldn't be damaged")
      assert(update.size == 2)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(damageable.currentLife == initialLife)
    }

    Scenario("The object collide but they are of the same side") {
      Given("Two objects that collide but are of the same side")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Check collision")
      val update = checkCollision(List(damageable, damager))

      Then("The damageable object shouldn't be damaged")
      assert(update.size == 2)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(damageable.currentLife == initialLife)
          case _ => assert(true)
    }
  }

  Feature("Inflict damage") {
    Scenario("The objects collide") {
      Given("Two objects that collide")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Check collision")
      val update = checkCollision(List(damageable, damager))

      Then("The damageable object should be damaged")
      assert(update.size == 2)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable => assert(damageable.currentLife == initialLife - 1)
          case damager: Damager => assert(damager.damageInflicted == 1)

    }

    Scenario("The objects collide and the damageable should be destroyed") {
      Given("Two objects that collide of different type and side")
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Check collision")
      val update = checkCollision(List(damageable, damager))

      Then("The damageable object should be damaged and destroyed")
      assert(update.size == 2)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable =>
            assert(damageable.currentLife == 0)
            assert(damageable.isDestroyed)
          case damager: Damager => assert(damager.damageInflicted == 1)

    }

    Scenario("The objects collide one is both damageable and damager") {
      Given("Two objects that collide of different type and side")
      val initialLife = 3
      val both = DamagerDamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(both.isCollidingWith(damager)(using distance = 0.1))

      When("Check collision")
      val update = checkCollision(List(both, damager))

      Then("The object that is both damageable and damager should be damaged")
      assert(update.size == 2)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable =>
            assert(damageable.currentLife == initialLife - 1)
            assert(!damageable.isDestroyed)
          case damager: Damager => assert(damager.damageInflicted == 1)
    }

    Scenario("Multiple objects collide") {
      Given("Three objects that collide of different type and side")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val both = DamagerDamageableTest(Point2D(0, 0), initialLife, Affiliation.Enemy)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Neutral)
      assert(both.isCollidingWith(damager)(using distance = 0.1))
      assert(both.isCollidingWith(damageable)(using distance = 0.1))
      assert(damageable.isCollidingWith(damager)(using distance = 0.1))

      When("Check collision")
      val update = checkCollision(List(both, damager, damageable))

      Then("The damageable objects should be damaged 2 times and the object that is both damageable and damager should be damaged 1 time")
      assert(update.size == 3)
      for
        elem <- update
      yield
        elem match
          case damageable: Damageable =>
            damageable match
              case damager: Damager =>
                assert(damageable.currentLife == initialLife - 1)
                assert(damager.damageInflicted == 1)
              case _ => assert(damageable.currentLife == initialLife - 2)
            assert(!damageable.isDestroyed)
          case damager: Damager => assert(damager.damageInflicted == 1)
    }
  }