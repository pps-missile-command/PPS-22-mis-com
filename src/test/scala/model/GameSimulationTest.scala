package model

import model.collisions.{Affiliation, DamageableTest, DamagerTest}
import model.vehicle.Plane
import model.elements2d.Point2D
import model.explosion.Explosion
import model.missile.*
import model.missile.Missile.*
import model.ground.MissileBattery
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import utilities.{missileHealth, reloadingTime}
import scala.util.Random

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
      When("The game executes a game loop, without special ability")
      val (_, collisions) =
        initialGame
          .timeElapsed(dt)
          .moveElements()
          .checkCollisions()

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
          .moveElements()
          .activateSpecialAbility()
          .checkCollisions()
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
          .moveElements()

      Then("The enemy missiles should be moving")
      val expectedMissile = missiles.map(m => m.timeElapsed(dt).move())
      assert(updatedGame.world.collisionables.size == 4)
      assert(updatedGame.world.collisionables == expectedMissile)
    }

    Scenario("In the game there are one friendly missile, one friendly explosion and one enemy missile that collides") {
      val destination = Point2D(70, 0)
      val enemyMissile: Missile = Missile.enemyMissile(position = Point2D(50, 50), finalPosition = destination)
      val friendlyMissile = Missile(1, 1, velocity, Point2D(40, 50), destination)
      val friendlyExplosion = Explosion(damageToInflict = 1, expPosition = Point2D(60, 50), dt = 10)(using Affiliation.Friendly)
      Given("An initial game with some missile")
      val game =
        Game
          .initialGame
          .updateWorld(
            _.addCollisionables(
              Set(
                enemyMissile,
                friendlyMissile,
                friendlyExplosion
              )
            )
          )

      When("The game executes collisions")
      val (updatedGame, collisions) =
        game
          .checkCollisions()
      val gameScore = (updatedGame, collisions).updateScore()

      Then("The collisionables should be 3 explosion")
      assert(collisions.size == 3)
      assert(updatedGame.world.collisionables.size == 3)
      assert(updatedGame.world.collisionables.count(_.isInstanceOf[Explosion]) == 3)
      Then("The score should be 1")
      assert(gameScore.player.score == 1)
    }

    Scenario("In the game the elements that has reached the destinations are removed") {
      val plane = Plane(vehicle.vehicleTypes.Right_To_Left, 20)(using Random())
      Given("An initial game with a plane")
      val game =
        Game
          .initialGame
          .updateWorld(
            _.addCollisionables(
              Set(
                plane
              )
            )
          )

      When("The game executes loop without special ability")
      val newGame = game
        .timeElapsed((World.width / vehicle.planeVelocity) + 1)
        .moveElements()
        .removeElementsThatReachedDestinations()
        .checkCollisions()
        .updateScore()

      Then("The collisionables should be empty")
      assert(newGame.world.collisionables.isEmpty)
    }

    Scenario("In the game there are one friendly explosion and one zigzag enemy missile that collides") {
      val destination = Point2D(70, 0)
      val enemyMissile: Missile = zigzag.ZigZagMissile(from = Point2D(50, 50), to = destination, step = 10, maxWidth = 10)
      val friendlyExplosion = Explosion(damageToInflict = 1, expPosition = Point2D(60, 50), dt = 10)(using Affiliation.Friendly)
      Given("An initial game with some missile")
      val game =
        Game
          .initialGame
          .updateWorld(
            _.addCollisionables(
              Set(
                enemyMissile,
                friendlyExplosion
              )
            )
          )

      When("The game executes collisions")
      val (updatedGame, collisions) =
        game
          .checkCollisions()
      val gameScore = (updatedGame, collisions).updateScore()

      Then("The collisionables should be 2 explosion")
      assert(collisions.size == 2)
      assert(updatedGame.world.collisionables.size == 2)
      println(updatedGame.world.collisionables)
      assert(updatedGame.world.collisionables.count(_.isInstanceOf[Explosion]) == 2)
      Then("The score should be 1")
      assert(gameScore.player.score == 1)
    }
  }
