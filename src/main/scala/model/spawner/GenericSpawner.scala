package model.spawner

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.Collisionable
import model.ground.*
import model.elements2d.Point2D
import model.spawner.Spawnable.*

import scala.util.Random

/**
 * This Trait models a generic spawner of A elements, that must be at least Collisionables,
 * that extends Timeable to be time aware
 * @tparam A The type of elements to generate
 */
trait GenericSpawner[A <: Collisionable] extends Timeable:
  /**
   * Method that generates a set of A elements as the time pass through and the new GenericSpawner
   * @return
   */
  def spawn(): (Set[A], GenericSpawner[A])

  /**
   *
   * @param dt This is the virtual delta time thas has been passed since the last update
   *  @return the new Timeable object with the current virtual time updated
   */
  override def timeElapsed(dt: DeltaTime): GenericSpawner[A]

/**
 * Case class that implements the GenericSpawner trait
 * @param interval The initial interval that model the frequency of spawning (1 element each interval time)
 * @param timeFromStart The total time passed since the creation of the spawner
 * @param dt The delta time passed since the last spawn
 * @param spawnable The spawner strategies: it dictates how the elements are generated and perform the generation
 * @tparam R The type of elements to generate
 */
case class GenericSpawnerImpl[R <: Collisionable](interval: DeltaTime,
                                                  timeFromStart: DeltaTime = 0,
                                                  dt: DeltaTime = 0,
                                                  spawnable: Spawnable[R]) extends GenericSpawner[R]:
  /**
   * This is a "shortcut" method using Currying to generate elements specifying only the number
   * of elements to generate
   */
  val generator: (Int) => Set[R] = Spawnable(spawnable)

  override def spawn(): (Set[R], GenericSpawnerImpl[R]) = dt match
    case n if n >= currentInterval =>
      val step: Int = (n / currentInterval).toInt
      val elements = generator(step)
      (elements, this.copy(dt = 0))
    case _ => (Set(), this)

  /**
   * The current interval: it is a value based to the hypebolic function to increase the
   * difficulty decreasing the interval time over the time (as the time exceeds a threshold)
   * @return the interval mapped if a threshold of time is exceeded
   */
  private def currentInterval = (interval mapIf isThresholdExceeded) (
    _ ~ timeFromStart
  )

  private val isThresholdExceeded: () => Boolean = () => (timeFromStart) >= threshold

  override def timeElapsed(_dt: DeltaTime): GenericSpawnerImpl[R] = this.copy(timeFromStart = this.timeFromStart + _dt, dt = this.dt + _dt)

  /**
   * Method to create a new spawner changing its spawnable strategy
   * @param newSpawnable The new spawnable to use
   * @return the new spawner
   */
  def changeSpawnable(newSpawnable: Spawnable[R]) = this.copy(spawnable = newSpawnable)

object GenericSpawner:

  def apply[R <: Collisionable](newInterval: DeltaTime,
                                spawnable: Spawnable[R])(using Random): GenericSpawnerImpl[R] = GenericSpawnerImpl(interval = newInterval, spawnable = spawnable)