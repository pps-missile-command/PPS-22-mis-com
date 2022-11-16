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

  def damageableHasExpectedLife(collisionable: Collisionable, expectedLife: Int): Boolean =
    damageableCheckCondition(collisionable, _.currentLife == expectedLife)

  def damageableCheckCondition(collisionable: Collisionable, check: Damageable => Boolean): Boolean =
    collisionable match
      case d: Damageable => check(d)
      case _ => true

  given distance: Distance = 0.1

  Feature("Don't inflict damage") {
    Scenario("The objects don't collide") {
      Given("Two objects that don't collide")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(10, 10), Affiliation.Enemy)
      assert(!damageable.isCollidingWith(damager))
      val expectedCollisions = Set.empty[Collision]

      When("Calculate collision")
      val entities = Set(damageable, damager)
      val collisions = entities.calculateCollisions
      Then("The collisions should be empty an empty set")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, updateCollisions) = entities applyDamagesBasedOn collisions
      assert(updateCollisions == expectedCollisions)

      Then("the damageable should have the same life as before")
      assert(updateEntities.size == 2)
      for
        element <- updateEntities
      yield
        assert(damageableHasExpectedLife(element, initialLife))
    }

    Scenario("The object collide but they are of the same type") {
      Given("Two objects that collide but are of the same type")
      val initialLife = 3
      val damageable1 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damageable2 = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Enemy)
      assert(damageable1.isCollidingWith(damageable2))
      val expectedCollisions = Set(Collision(damageable1, damageable2))

      When("Calculate collision")
      val entities = Set[Collisionable](damageable1, damageable2)
      val collisions = entities.calculateCollisions
      Then("The collisions of the two objects should be in the set")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.size == 1)

      Then("the damageable should have the same life as before")
      assert(updateEntities.size == 2)
      for
        element <- updateEntities
      yield
        assert(damageableHasExpectedLife(element, initialLife))
    }

    Scenario("The object collide but they are of the same side") {
      Given("Two objects that collide but are of the same side")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Friendly)
      assert(damageable.isCollidingWith(damager))
      val expectedCollisions = Set.empty[Collision]

      When("Calculate collision")
      val entities = Set(damageable, damager)
      val collisions = entities.calculateCollisions
      Then("The collisions should be an empty set")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.isEmpty)

      Then("the damageable should have the same life as before")
      assert(updateEntities.size == 2)
      for
        element <- updateEntities
      yield
        assert(damageableHasExpectedLife(element, initialLife))
    }

    Scenario("There are no objects") {
      Given("No objects")
      val entities = Set.empty[Collisionable]
      val expectedCollisions = Set.empty[Collision]

      When("Calculate collision")
      val collisions = entities.calculateCollisions
      Then("The collisions should be an empty set")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.isEmpty)
      Then("the set should be empty")
      assert(updateEntities.isEmpty)
    }

    Scenario("The object is null"){
      Given("An object null")
      val entities = Set[Collisionable](null)
      val expectedCollisions = Set.empty[Collision]

      When("Calculate collision")
      val collisions = entities.calculateCollisions
      Then("The collisions should be an empty set")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.isEmpty)
      Then("the set should be empty")
      assert(updateEntities.isEmpty)
    }
  }

  Feature("Inflict damage") {
    Scenario("The objects collide") {
      Given("Two objects that collide")
      val initialLife = 3
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager))
      val expectedCollisions = Set(Collision(damageable, damager))

      When("Calculate collision")
      val entities = Set(damageable, damager)
      val collisions = entities.calculateCollisions
      Then("The collisions should set with the collision of the two objects")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.size == 1)

      Then("the damageable shouldn't have the same life as before")
      assert(updateEntities.size == 2)
      for
        element <- updateEntities
      yield
        assert(damageableHasExpectedLife(element, initialLife - damager.damageInflicted))
        assert(damageableCheckCondition(element, !_.isDestroyed))
    }

    Scenario("The objects collide and the damageable should be destroyed") {
      Given("Two objects that collide of different type and side")
      val initialLife = 1
      val damageable = DamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(damageable.isCollidingWith(damager))
      val expectedCollisions = Set(Collision(damageable, damager))

      When("Calculate collision")
      val entities = Set(damageable, damager)
      val collisions = entities.calculateCollisions
      Then("The collisions should set with the collision of the two objects")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.size == 1)

      Then("the damageable shouldn't have the same life as before and should be destroyed")
      assert(updateEntities.size == 2)
      for
        element <- updateEntities
      yield
        assert(damageableHasExpectedLife(element, initialLife - damager.damageInflicted))
        assert(damageableCheckCondition(element, _.isDestroyed))
    }

    Scenario("The objects collide one is both damageable and damager") {
      Given("Two objects that collide of different type and side")
      val initialLife = 3
      val both = DamagerDamageableTest(Point2D(0, 0), initialLife, Affiliation.Friendly)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Enemy)
      assert(both.isCollidingWith(damager))

      val expectedCollisions = Set(Collision(both, damager))
      When("Calculate collision")
      val entities = Set(both, damager)
      val collisions = entities.calculateCollisions
      Then("The collisions should set with the collision of the two objects")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.size == 1)

      Then("the damageable should have the same life as before")
      assert(updateEntities.size == 2)
      for
        element <- updateEntities
      yield
        assert(damageableHasExpectedLife(element, initialLife - damager.damageInflicted))
        assert(damageableCheckCondition(element, !_.isDestroyed))
    }

    Scenario("Multiple objects collide") {
      Given("Three objects that collide of different type and side")
      val damageableInitialLife = 4
      val bothInitialLife = 3
      val expectedLife = 2
      val damageable = DamageableTest(Point2D(0, 0), damageableInitialLife, Affiliation.Friendly)
      val both = DamagerDamageableTest(Point2D(0, 0), bothInitialLife, Affiliation.Enemy)
      val damager = DamagerTest(Point2D(0, 0), Affiliation.Neutral)
      assert(both.isCollidingWith(damager))
      assert(both.isCollidingWith(damageable))
      assert(damageable.isCollidingWith(damager))

      val expectedCollisions = Set(Collision(both, damager), Collision(both, damageable), Collision(damageable, damager))
      When("Calculate collision")
      val entities = Set(both, damager, damageable)
      val collisions = entities.calculateCollisions
      Then("The collisions should set with the collision of the two objects")
      assert(collisions == expectedCollisions)

      When("Calculate damage")
      val (updateEntities, collisionsUpdate) = entities applyDamagesBasedOn collisions
      assert(collisionsUpdate.size == 3)

      Then("the damageable should have the same life as before")
      assert(updateEntities.size == 3)
      for
        element <- updateEntities
      yield
        assert(damageableHasExpectedLife(element, expectedLife))
        assert(damageableCheckCondition(element, !_.isDestroyed))
    }
  }