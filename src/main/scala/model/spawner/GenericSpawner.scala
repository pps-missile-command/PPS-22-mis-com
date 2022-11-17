package model.spawner

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.Collisionable
import model.ground.*
import model.elements2d.Point2D
import model.spawner.LazySpawner.*

import scala.util.Random

extension[A](list: LazyList[A])
  def popN(n: Int): (List[A], LazyList[A]) = (list.take(n).toList, list.drop(n))

extension(time: DeltaTime)

  def ~(interval: DeltaTime, timePassed: DeltaTime) : DeltaTime = time - interval * (1 / ( 1 + Math.exp((-interval / 4) * timePassed)) - interval / 2)

  def mapIf(condition: () => Boolean)(mapper: (DeltaTime) => DeltaTime): DeltaTime = condition match
    case _ if condition() => mapper(time)
    case _ => time

trait GenericSpawner[+A](using Random) extends Timeable:

  def spawn(): (List[A], GenericSpawner[A])

  override def timeElapsed(dt: DeltaTime): GenericSpawner[A]

object GenericSpawner:

  def apply[R <: Collisionable](interval: DeltaTime,
            timeFromStart: DeltaTime = 0,
            dt: DeltaTime = 0,
            spawnable: Spawnable[R], streamOpt: Option[LazyList[R]] = Option.empty[LazyList[R]]): GenericSpawner[R] = new GenericSpawner[R](using Random) {

    val stream: LazyList[R] = streamOpt.getOrElse(LazySpawner(spawnable))

    override def spawn(): (List[R], GenericSpawner[R]) = dt match
      case n if n >= currentInterval =>
        val step: Int = (n / currentInterval).toInt
        val (elements, newStream) = stream.popN(step)
        (elements.toList, copy(newDt = 0, Option(newStream)))
      case _ => (List(), this)

    private def currentInterval = (interval mapIf isThresholdExceeded) {
      i => i ~ (i, timeFromStart + dt)
    }

    private val isThresholdExceeded: () => Boolean = () => (timeFromStart + dt) >= threshold

    override def timeElapsed(_dt: DeltaTime): GenericSpawner[R] = copy(newTimeFromStart = timeFromStart + dt, newDt = dt + _dt)

    private def copy(newTimeFromStart: DeltaTime = timeFromStart, newDt: DeltaTime = dt, streamOpt: Option[LazyList[R]] = streamOpt): GenericSpawner[R] =
      GenericSpawner(interval, newTimeFromStart, newDt, spawnable, streamOpt)
  }
