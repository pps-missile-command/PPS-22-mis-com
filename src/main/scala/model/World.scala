package model

import model.behavior.Timeable
import model.collisions.{Collision, Collisionable, _}
import model.ground.Ground
import model.collisions.PimpingByCollisionable._
import model.collisions.PimpingByCollisionables._
import model.collisions.PimpingByCollisions._
import PimpingByCollisionsResults._
import model.ScorePoint
import model.elements2d.Point2D
import model.explosion.Explosion

/**
 * Class that represents the world.
 *
 */
trait World extends WorldActions[World] :
  /**
   * The [[Ground]] of the player in the world.
   */
  val ground: Ground
  /**
   * The [[Collisionable]]s in the world.
   */
  val collisionables: Set[Collisionable]

  /**
   * Change the [[Ground]] of the world.
   *
   * @param newGround the new [[Ground]] instance
   * @return a world with the new [[Ground]]
   */
  def updateGround(newGround: Ground): World

  /**
   * Change the [[Collisionable]]s of the world.
   *
   * @param newCollisionables the new [[Collisionable]]s instance
   * @return a world with the new [[Collisionable]]s
   */
  def updateCollisionables(newCollisionables: Set[Collisionable]): World

  /**
   * Add a set of new [[Collisionable]]s to the world.
   *
   * @param collisionablesToAdd the new [[Collisionable]]s to add
   * @return a world with the new [[Collisionable]]s added
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
   * Create a new world with the given [[Ground]] and [[Collisionable]]s.
   *
   * @param ground         the [[Ground]] of the world
   * @param collisionables the [[Collisionable]]s in the world
   * @return a new world with the given [[Ground]] and [[Collisionable]]s
   */
  def apply(ground: Ground, collisionables: Set[Collisionable]): World =
    case class WorldImpl(ground: Ground, collisionables: Set[Collisionable]) extends World :

      override def timeElapsed(dt: DeltaTime): World =
        updateCollisionables(collisionables.map(_.updateTimebleTime(dt)))
          .updateGround(Ground(ground.cities, ground.turrets.map(_.timeElapsed(dt))))

      override def checkCollisions: (World, Set[Collision]) =

        def checkCollisionWithExplosions[C <: Collisionable]
        (allCollisionables: Set[Collisionable], collisionables: Set[C])
        (oldCollisions: Set[Collision], oldExplosions: Set[Explosion]): CollisionablesCollisionsExplosionsResult =
          allCollisionables
            .applyDamagesBasedOnWithOld(
              allCollisionables calculateCollisionsWith collisionables,
              oldCollisions
            )
            .explodeDestroyedMissile()
            .removeDestroyedCollisionables()
            .checkCollsionsWithNewExplosions(oldExplosions)(checkCollisionWithExplosions)

        def checkCollision[C <: Collisionable](allCollisionables: Set[Collisionable], collisionables: Set[C]): (Set[Collisionable], Set[Collision]) =
          checkCollisionWithExplosions(allCollisionables, collisionables)(Set.empty[Collision], Set.empty[Explosion])
            .addExplosions()

        val allCollisionables = collisionables ++ ground.cities ++ ground.turrets
        checkCollision(allCollisionables, collisionables)
          .splitGroundFromCollisionables()
          .generateWorld()

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