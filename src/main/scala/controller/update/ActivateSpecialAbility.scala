package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.Game
import model.explosion.Explosion
import model.behavior.Moveable
import model.collisions.Collisionable
import model.missile.Missile
import monix.eval.Task

/**
 * Object that return an update function for the world to be update with the special abilities of its components
 */
object ActivateSpecialAbility:

  extension (collisionable: Collisionable)
    private def activateSpecialAbility: Collisionable =
      collisionable match
        case missile: Missile if missile.destinationReached => missile.explode
        case _ => collisionable

    private def isExplosionTerminated: Boolean =
      collisionable match
        case explosion: Explosion => explosion.terminated
        case _ => false

  /**
   * Apply function used to update the world to be update with the special abilities of its components
   *
   * @return An Update that update the world to be update with the special abilities of its components
   */
  def apply(): Update = on[TimePassed] { (_: Event, game: Game) =>
    Task {
      val collisionables = game.world.collisionables.map(activateSpecialAbility)
      val remainedCollisionables = collisionables.filterNot(isExplosionTerminated)
      val (newMissiles, spawner) = game.spawner.spawn()
      game.copy(
        world = game.world.copy(collisionables = remainedCollisionables ++ newMissiles),
        spawner = spawner
      )
    }
  }
