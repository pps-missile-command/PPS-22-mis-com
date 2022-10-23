package model.spawner

import model.behavior.*
import model.collisions.Affiliation
import model.elements2d.Point2D
import model.missile.{Missile, damage, initialLife, velocity}
import model.missile.Missile
import model.DeltaTime

import scala.util.Random

trait Spawner(using Random) extends Timeable:

  def spawn(dt: DeltaTime): List[Missile]

  override def timeElapsed(dt: DeltaTime): Spawner

object Spawner:

  given affiliation: Affiliation = Affiliation.Enemy

  def apply(interval: DeltaTime, maxWidth: Double, maxHeight: Double, dt: DeltaTime = 0): Spawner = new Spawner(using Random) {

    private var dtSupplier : () => DeltaTime = () => dt

    override def spawn(dt: DeltaTime): List[Missile] =
      val currentDt = dtSupplier()
      this.dtSupplier = () => 0
      dt match
        case n if n >= interval =>
          val step: Int = (n / interval).toInt
          val randomX_start = (0 until step map { i => (i, Random.nextDouble() * maxWidth) }).toList
          val randomX_end = (0 until step map { i => (i, Random.nextDouble() * maxWidth) }).toList
          val generator: List[Missile] =
            for
              (i, x_start) <- randomX_start
              (j, x_end) <- randomX_end
              if i == j
            yield Missile.enemyMissile(initialLife, damage, velocity, Point2D(x_start, maxHeight), Point2D(x_end, 0)) //TODO coordinate campo
          generator
        case _ => List()

    override def timeElapsed(_dt: DeltaTime): Spawner = Spawner.apply(interval, maxWidth, maxHeight, dtSupplier() + _dt)

  }
