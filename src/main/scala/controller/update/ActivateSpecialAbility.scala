package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.World
import model.explosion.Explosion
import model.behavior.Moveable
import model.collisions.Collisionable
import model.missile.Missile
import monix.eval.Task

/**
 * Object that return an update function for the world to be update with the special abilities of its components
 */
object ActivateSpecialAbility:
  /**
   * Apply function used to update the world to be update with the special abilities of its components
   *
   * @return An Update that update the world to be update with the special abilities of its components
   */
  def apply(): Update = on[TimePassed] { (_: Event, world: World) =>
    Task {
      def activateSpecialAbility(collisionable: Collisionable): Collisionable = collisionable match
        case missile: Missile if missile.destinationReached => missile.explode
        case _ => collisionable

      def isTerminated(collisionable: Collisionable): Boolean = collisionable match
        case explosion: Explosion => explosion.terminated
        case _ => false

      val collisionables = world.collisionables.map(activateSpecialAbility)
      val remainedCollisionables = collisionables.filterNot(isTerminated)
      val (newMissiles, spawner) = world.spawner.spawn()
      world.copy(collisionables = remainedCollisionables ++ newMissiles, spawner = spawner)
    }
  }
