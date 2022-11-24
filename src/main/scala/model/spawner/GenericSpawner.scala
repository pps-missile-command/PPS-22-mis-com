package model.spawner

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.Collisionable
import model.ground.*
import model.elements2d.Point2D
import model.spawner.LazySpawner.*

import scala.util.Random

trait GenericSpawner[A <: Collisionable](using Random) extends Timeable:

  def spawn(): (Set[A], GenericSpawner[A])

  override def timeElapsed(dt: DeltaTime): GenericSpawner[A]

object GenericSpawner:

  def apply[R <: Collisionable](interval: DeltaTime,
            timeFromStart: DeltaTime = 0,
            dt: DeltaTime = 0,
            spawnable: Spawnable[R], streamOpt: Option[LazyList[R]] = Option.empty[LazyList[R]]): GenericSpawner[R] = new GenericSpawner[R](using Random) {

    val stream: LazyList[R] = streamOpt.getOrElse(LazySpawner(spawnable))

    override def spawn(): (Set[R], GenericSpawner[R]) = dt match
      case n if n >= currentInterval =>
        val step: Int = (n / currentInterval).toInt
        val (elements, newStream) = stream.popN(step)
        (elements.toSet, copy(newDt = 0, Option(newStream)))
      case _ => (Set(), this)

    private def currentInterval = (interval mapIf isThresholdExceeded) (
      _ ~ (timeFromStart + dt)
    )

    private val isThresholdExceeded: () => Boolean = () => (timeFromStart + dt) >= threshold

    override def timeElapsed(_dt: DeltaTime): GenericSpawner[R] = copy(newTimeFromStart = timeFromStart + dt, newDt = dt + _dt)

    private def copy(newTimeFromStart: DeltaTime = timeFromStart, newDt: DeltaTime = dt, streamOpt: Option[LazyList[R]] = streamOpt): GenericSpawner[R] =
      GenericSpawner(interval, newTimeFromStart, newDt, spawnable, streamOpt)
  }
