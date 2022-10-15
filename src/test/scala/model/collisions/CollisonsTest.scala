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

  given distance: Distance = 0.1

  Feature("Don't inflict damage") {
    Scenario("The objects don't collide") {
      Given("Two objects that don't collide")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(10, 10), Affiliation.Enemy)
      assert(!damageable.isCollidingWith(damager))

      When("Calculate collision and apply damage")
      val update = applyDamage(calculateCollisions(List(damageable, damager)))

      Then("The map should have 2 elements both empty")
      assert(update.size == 2)
      for
        element <- update
      yield
        assert(element._2.isEmpty)
    }

    Scenario("The object collide but they are of the same type") {
      Given("Two objects that collide but are of the same type")
      val initialLife = 3
      val damageable1 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damageable2 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Enemy)
      assert(damageable1.isCollidingWith(damageable2))

      When("Calculate collision and apply damage")
      val update = calculateCollisions(List(damageable1, damageable2))

      Then("The map should have 2 elements both empty")
      assert(update.size == 2)
      for
        element <- update
      yield
        assert(element._2.isEmpty)
    }

    Scenario("The object collide but they are of the same side") {
      Given("Two objects that collide but are of the same side")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))

      When("Calculate collision")
      val update = applyDamage(calculateCollisions(List(damageable, damager)))

      Then("The map should have 2 elements both empty")
      assert(update.size == 2)
      for
        element <- update
      yield
        assert(element._2.isEmpty)
    }
  }

  Feature("Inflict damage") {
    Scenario("The objects collide") {
      Given("Two objects that collide")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager))

      When("Check collision and apply damages")
      val update = applyDamage(calculateCollisions(List(damageable, damager)))

      Then("The map should have 2 elements one not empty")
      assert(update.size == 2)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(element._2.size == 1)
            assert(element._2.contains(damager))
            assert(!damageable.isDestroyed)
            assert(damageable.currentLife == initialLife - damager.damageInflicted)
          case _ => assert(element._2.isEmpty)
    }

    Scenario("The objects collide and the damageable should be destroyed") {
      Given("Two objects that collide of different type and side")
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager))

      When("Check collision and apply damages")
      val update = applyDamage(calculateCollisions(List(damageable, damager)))

      Then("The map should have 2 elements, the damageable should be destroyed and the damager should be empty")
      assert(update.size == 2)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(element._2.size == 1)
            assert(element._2.contains(damager))
            assert(damageable.isDestroyed)
            assert(damageable.currentLife == initialLife - damager.damageInflicted)
          case _ => assert(element._2.isEmpty)
    }

    Scenario("The objects collide one is both damageable and damager") {
      Given("Two objects that collide of different type and side")
      val initialLife = 3
      val both = DamagerDamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(both.isCollidingWith(damager))

      When("Check collision and apply damages")
      val update = applyDamage(calculateCollisions(List(both, damager)))

      Then("The map should have 2 elements, the damageable should have damage and the damager should be empty")
      assert(update.size == 2)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            assert(element._2.size == 1)
            assert(element._2.contains(damager))
            assert(!damageable.isDestroyed)
            assert(damageable.currentLife == initialLife - damager.damageInflicted)
          case _ => assert(element._2.isEmpty)
    }

    Scenario("Multiple objects collide") {
      Given("Three objects that collide of different type and side")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val both = DamagerDamageableTest(Point2D(0, 0), initialLife, Affiliation.Enemy)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Neutral)
      assert(both.isCollidingWith(damager))
      assert(both.isCollidingWith(damageable))
      assert(damageable.isCollidingWith(damager))

      When("Check collision and apply damages")
      val update = applyDamage(calculateCollisions(List(both, damager, damageable)))

      Then("The damageable objects should be damaged 2 times and the object that is both damageable and damager should be damaged 1 time")
      assert(update.size == 3)
      for
        element <- update
      yield
        element._1 match
          case damageable: Damageable =>
            damageable match
              case damager: Damager =>
                assert(element._2.size == 1)
                assert(damageable.currentLife == initialLife - 1)
              case _ =>
                assert(element._2.size == 2)
                assert(damageable.currentLife == initialLife - 2)
            assert(!damageable.isDestroyed)
          case _ => assert(element._2.isEmpty)
    }
  }