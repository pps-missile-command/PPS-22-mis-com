package model.spawner

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.Collisionable
import model.ground.*
import model.elements2d.Point2D
import model.spawner.LazySpawner.*

import scala.util.Random

trait GenericSpawner[A <: Collisionable] extends Timeable:

  def spawn(): (Set[A], GenericSpawner[A])

  override def timeElapsed(dt: DeltaTime): GenericSpawner[A]

case class GenericSpawnerImpl[R <: Collisionable](interval: DeltaTime,
                                                  timeFromStart: DeltaTime = 0,
                                                  dt: DeltaTime = 0,
                                                  spawnable: Spawnable[R], streamOpt: Option[LazyList[R]] = Option.empty[LazyList[R]])(using Random) extends GenericSpawner[R]:

  val stream: LazyList[R] = streamOpt.getOrElse(LazySpawner(spawnable))

  override def spawn(): (Set[R], GenericSpawnerImpl[R]) = dt match
    case n if n >= interval =>
      val step: Int = (n / currentInterval).toInt
      val (elements, newStream) = stream.popN(step)
      (elements.toSet, this.copy(dt = 0, streamOpt = Option(newStream)))
    case _ => (Set(), this)

  private def currentInterval = (interval mapIf isThresholdExceeded) (
    _ ~ timeFromStart
  )

  private val isThresholdExceeded: () => Boolean = () => (timeFromStart) >= threshold

  override def timeElapsed(_dt: DeltaTime): GenericSpawnerImpl[R] = this.copy(timeFromStart = this.timeFromStart + _dt, dt = this.dt + _dt)

  def changeSpawnable(newSpawnable: Spawnable[R]) = this.copy(spawnable = newSpawnable)

object GenericSpawner:

  def apply[R <: Collisionable](newInterval: DeltaTime,
                                spawnable: Spawnable[R])(using Random): GenericSpawnerImpl[R] = GenericSpawnerImpl(interval = newInterval, spawnable = spawnable)