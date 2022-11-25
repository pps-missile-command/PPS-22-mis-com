package model

import model.collisions.{Affiliation, DamageableTest, DamagerTest}
import model.elements2d.Point2D
import model.missile._
import model.missile.Missile._
import model.ground.MissileBattery
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import utilities.reloadingTime

class GameSimulationTest extends AnyFeatureSpec with GivenWhenThen :

  Feature("Game initial state simulation ") {
    Scenario("The game should be able to update the time of the elements in it") {
      val dt = 10
      Given("An initial game")
      val initialGame = Game.initialGame
      When("The time of game is updated")
      val updatedGame = initialGame.timeElapsed(dt)
      Then("The time of the elements the game should be updated")
      assert(updatedGame.player.timer.time == dt)
    }

    Scenario("The game shouldn't have elements after movement") {
      val dt = 10
      Given("An initial game")
      val initialGame = Game.initialGame
      When("The time of game is updated")
      val updatedGame = initialGame.timeElapsed(dt)

      Then("The elements of the game should be empty")
      assert(updatedGame.world.collisionables == Set.empty)
    }

    Scenario("The game shouldn't have collisions") {
      val dt = 10
      Given("An initial game")
      val initialGame = Game.initialGame
      When("The game executes a game loop")
      val (_, collisions) =
        initialGame
          .timeElapsed(dt)
          .moveElements
          .activateSpecialAbility
          .checkCollisions

      Then("The collisions should be empty")
      assert(collisions == Set.empty)
    }

    Scenario("The game shouldn't update the score") {
      val dt = 10
      Given("An initial game")
      val initialGame = Game.initialGame
      When("The game executes a game loop")
      val updatedGame =
        initialGame
          .timeElapsed(dt)
          .moveElements
          .activateSpecialAbility
          .checkCollisions
          .updateScore()

      Then("The score should be 0")
      assert(updatedGame.player.score == 0)
    }

    Scenario("The game should be able to create a friendly missile") {
      val dt = reloadingTime
      val destination = Point2D(0, 0)
      Given("An initial game")
      val initialGame = Game.initialGame
      When(s"The game, after $dt time, shoots a friendly missile, if asked")
      val newGame =
        initialGame
          .timeElapsed(dt)
          .shootMissile(destination)

      Then("The collisionable should have a friendly missile with the given destination")
      assert(newGame.world.collisionables.size == 1)
      assert(newGame.world.collisionables.head.isInstanceOf[Missile])
      assert(newGame.world.collisionables.head.asInstanceOf[Missile].affiliation == Affiliation.Friendly)
      assert(newGame.world.collisionables.head.asInstanceOf[Missile].destination == destination)
      Then("One missile battery should be reloading")
      assert(newGame.world.ground.turrets.count(!_.isReadyForShoot) == 1)
    }
  }

  Feature("Game going") {
    Scenario("In the game there are some enemy missile that are moving") {
      val dt = 10
      val destination = Point2D(missile.velocity * (dt + 1), 0)
      Given("An initial game with some enemy missile")
      val missiles = (0 until 4).map(i => enemyMissile(position = Point2D(0, i * 10), finalPosition = destination)).toSet
      val initialGame = Game.initialGame.updateWorld(_.addCollisionables(missiles))

      When("The game executes a game loop without collisions")
      val updatedGame =
        initialGame
          .timeElapsed(dt)
          .moveElements

      Then("The enemy missiles should be moving")
      val expectedMissile = missiles.map(m => m.timeElapsed(dt).move())
      assert(updatedGame.world.collisionables.size == 4)
      assert(updatedGame.world.collisionables == expectedMissile)
    }
  }
