package model

import model.collisions.DamageableTest
import model.collisions.Affiliation
import model.elements2d.{AngleTest, Point2D}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.funspec.AnyFunSpec

class GameTest extends AnyFunSpec :

  describe("A world") {
    it("in its initial state should be created with 0 elements and a ground") {
      val world = World.initialWorld
      assert(world.collisionables.isEmpty)
      assert(world.ground.cities.size == 6)
      assert(world.ground.cities.forall(city => city.currentLife == city.initialLife))
      assert(world.ground.turrets.size == 3)
      assert(world.ground.turrets.forall(turret => turret.currentLife == turret.initialLife))
    }

    it("should be able to add a new element") {
      val position = Point2D(0, 0)
      val world = World.initialWorld
      val newWorld =
        world
          .updateGround(world.ground)
          .updateCollisionables(
            world.collisionables +
              DamageableTest(position = position, life = 1, affiliation = Affiliation.Enemy)
          )
      assert(newWorld.collisionables.size == 1)
    }
  }

  describe("A player") {
    it("in its initial state should be created with score and timer 0") {
      val player = Player.initialPlayer
      assert(player.score == 0)
      assert(player.timer.time == 0)
    }

    it("should be able to have a new score") {
      val player = Player.initialPlayer
      val newPlayer = player.updateScore(score = 1)
      assert(newPlayer.score == 1)
    }

    it("should be able to have a new timer") {
      val player = Player.initialPlayer
      val newPlayer = player.updateTimer(timer = Timer.Timer(1))
      assert(newPlayer.timer.time == 1)
    }
  }

  describe("A game") {
    it("in its initial state should be created with a world (in its initial state), a player (in its initial state) and a spawner") {
      val game = Game.initialGame
      val initialWorld = World.initialWorld
      val initialPlayer = Player.initialPlayer
      assert(game.world == initialWorld)
      assert(game.player == initialPlayer)
      assert(game.spawner != null)
    }

    it("should be able to have a new world") {
      val game = Game.initialGame
      val position = Point2D(0, 0)
      val newWorld =
        game.world
          .updateGround(game.world.ground)
          .updateCollisionables(
            game.world.collisionables +
              DamageableTest(position = position, life = 1, affiliation = Affiliation.Enemy)
          )
      val newGame = game.updateWorld(newWorld)
      assert(newGame.world.collisionables.size == 1)
    }

    it("should be able to have a new player") {
      val game = Game.initialGame
      val newPlayer = game.player.updateScore(score = 1)
      val newGame = game.updatePlayer(newPlayer)
      assert(newGame.player.score == 1)
    }
  }
