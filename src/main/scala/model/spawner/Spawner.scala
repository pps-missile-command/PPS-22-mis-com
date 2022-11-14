package model.spawner

import model.behavior.*
import model.collisions.Affiliation
import model.elements2d.Point2D
import model.missile.{Missile, damage, initialLife, velocity}
import model.missile.Missile
import model.{DeltaTime, World}
import view.ViewConstants

import scala.util.Random

extension(v: Double)
  def map(f: Double => Double) = f(v)

trait Spawner(using Random) extends Timeable:

  def spawn(): (List[Missile], Spawner)

  override def timeElapsed(dt: DeltaTime): Spawner

object Spawner:

  def apply(interval: DeltaTime, maxWidth: Double, maxHeight: Double, timeFromStart: DeltaTime = 0, dt: DeltaTime = 0, mapper: Double => Double = ???): Spawner = new Spawner(using Random) {
    
    override def spawn(): (List[Missile], Spawner) =
      dt match
        case n if n >= interval =>
          val step: Int = (n / interval).toInt
          val randomX_start = (0 until step map { i => (i, (Random.nextDouble() * maxWidth) map mapper) }).toList
          val randomX_end = (0 until step map { i => (i, (Random.nextDouble() * maxWidth) map mapper) }).toList
          val generator: List[Missile] =
            for
              (i, x_start) <- randomX_start
              (j, x_end) <- randomX_end
              if i == j
            yield Missile.enemyMissile(initialLife, damage, _velocity = velocity, Point2D(x_start, 0),
                Point2D(x_end, ViewConstants.GUI_height)) //TODO coordinate campo
          (generator, Spawner(interval, maxWidth, maxHeight, timeFromStart, mapper = mapper))
        case _ => (List(), this)

    override def timeElapsed(_dt: DeltaTime): Spawner = (timeFromStart + dt) match
      case v if v < threshold => Spawner.apply(interval, maxWidth, maxHeight, v, dt + _dt)
      case v if v >= threshold => Spawner.apply(interval - step, maxWidth, maxHeight, v, dt + _dt)
      

  }
