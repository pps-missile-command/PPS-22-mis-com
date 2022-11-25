package model

import model.behavior.Timeable
import model.collisions.{Collisionable, Collision}
import model.ground.Ground
import model.collisions.PimpingByCollisionable._
import model.collisions.PimpingByCollisionables._
import model.collisions.PimpingByCollisions._
import model.collisions._
import model.ScorePoint
import model.elements2d.Point2D

/**
 * Class that represents the world.
 *
 */
trait World extends WorldActions[World] :
  /**
   * The ground of the player in the world.
   */
  val ground: Ground
  /**
   * The collisionables in the world.
   */
  val collisionables: Set[Collisionable]

  /**
   * Change the ground of the world.
   *
   * @param newGround the new ground instance
   * @return a world with the new ground
   */
  def updateGround(newGround: Ground): World

  /**
   * Change the collisionables of the world.
   *
   * @param newCollisionables the new collisionables instance
   * @return a world with the new collisionables
   */
  def updateCollisionables(newCollisionables: Set[Collisionable]): World

  /**
   * Add a set of new collisionables to the world.
   *
   * @param collisionablesToAdd the new collisionables to add
   * @return a world with the new collisionables added
   */
  def addCollisionables[C <: Collisionable](collisionablesToAdd: Set[C]): World =
    updateCollisionables(collisionables ++ collisionablesToAdd)


/**
 * Object with the values of the world and its initial state.
 */
object World:

  /**
   * The width of the world.
   */
  val width = 700

  /**
   * The height of the world.
   */
  val height = 300

  /**
   * The initial state of the world.
   *
   * @return the initial state of the world
   */
  def initialWorld: World = World(Ground(), Set.empty[Collisionable])

  /**
   * Create a new world with the given ground and collisionables.
   *
   * @param ground         the ground of the world
   * @param collisionables the collisionables in the world
   * @return a new world with the given ground and collisionables
   */
  def apply(ground: Ground, collisionables: Set[Collisionable]): World =
    case class WorldImpl(ground: Ground, collisionables: Set[Collisionable]) extends World :

      override def timeElapsed(dt: DeltaTime): World =
        val newCollisionables = collisionables.map(_.updateTimebleTime(dt))
        val newGround = Ground(ground.cities, ground.turrets.map(_.timeElapsed(dt)))
        WorldImpl(newGround, newCollisionables)


      override def checkCollisions: (World, Set[Collision]) =
        val allCollisionables = collisionables ++ ground.cities ++ ground.turrets
        val (tmpNewCollisionables, collisionsUpdate) =
          allCollisionables applyDamagesBasedOn (allCollisionables calculateCollisionsWith collisionables)
        val newExplosion = tmpNewCollisionables.explosionsOfDestroyedMissiles
        val (collisionableAfterSecondCollisions, _) =
          tmpNewCollisionables applyDamagesBasedOn (tmpNewCollisionables calculateCollisionsWith newExplosion)
        val (newGround, newCollisionables) = collisionableAfterSecondCollisions.splitGroundFromOther
        val newNotDestroyedCollisionables = newCollisionables.filterNot(isDestroyed) ++ newExplosion
        val newWorld = WorldImpl(newGround, newNotDestroyedCollisionables)
        (newWorld, collisionsUpdate)

      override def activateSpecialAbility: World =
        updateCollisionables(
          collisionables
            .activateSpecialAbility
            .filterNot(isExplosionTerminated)
        )

      override def shootMissile(destination: Point2D): World =
        ground.shootMissile(destination) match
          case (newGround, Some(missile)) =>
            updateGround(newGround)
              .addCollisionables(Set(missile))
          case _ => this

      override def moveElements: World =
        updateCollisionables(collisionables.map(_.updateMovablePosition()))

      def updateGround(newGround: Ground): World =
        WorldImpl(newGround, collisionables)

      def updateCollisionables(newCollisionables: Set[Collisionable]): World =
        WorldImpl(ground, newCollisionables)

    WorldImpl(ground, collisionables)