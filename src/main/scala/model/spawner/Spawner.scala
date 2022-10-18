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

object Spawner:

  given affiliation: Affiliation = Affiliation.Enemy

  def apply(interval: DeltaTime, maxWidth: Double, maxHeight: Double, dt: DeltaTime = 0): Spawner = new Spawner(using Random) {

    var dtSupplier : () => DeltaTime = () => dt

    override def spawn(dt: DeltaTime): List[Missile] =
      val currentDt = dtSupplier()
      this.dtSupplier = () => 0
      dt match
        case n if n >= interval =>
          val step: Int = (n / interval).toInt
          val randomX = (0 until step map { i => (i, Random.nextDouble() * maxWidth) }).toList
          val randomY = (0 until step map { i => (i, Random.nextDouble() * maxWidth) }).toList
          val generator: List[Missile] =
            for
              (i, x) <- randomX
              (j, y) <- randomY
              if i == j
            yield Missile.enemyMissile(initialLife, damage, velocity, Point2D(x, y), Point2D(x, y))
          generator
        case _ => List()

    override def timeElapsed(_dt: DeltaTime): Spawner = Spawner.apply(interval, maxWidth, maxHeight, dtSupplier() + _dt)

  }

  @main def test(): Unit = {
    val spawner = Spawner(5, 10, 10)

    println(spawner.spawn(10))
  }
