package controller.update

import controller.Event
import monix.eval.Task
import controller.Event.*
import model.*
import controller.update.Update.*
import model.behavior.Timeable
import model.collisions.Collisionable
import model.ground.Ground


/**
 * Object that return an update function for the world to update the time of its components
 */
object UpdateTime:
  /**
   * Apply function used to update the time of the game components
   *
   * @return An Update that update the time of the game components
   */
  def apply(): Update = on[TimePassed] { (event: TimePassed, world: World) =>
    Task {
      def updateTimeble(collisionable: Collisionable): Collisionable = collisionable match
        case timeable: Timeable => timeable.timeElapsed(event.deltaTime).asInstanceOf[Collisionable]
        case _ => collisionable

      val collisionables = world.collisionables.map(updateTimeble)
      val spawner = world.spawner.timeElapsed(event.deltaTime)
      val ground = Ground(world.ground.cities, world.ground.turrets.map(_.timeElapsed(event.deltaTime)))
      world.copy(collisionables = collisionables, spawner = spawner, ground = ground)
    }
  }
