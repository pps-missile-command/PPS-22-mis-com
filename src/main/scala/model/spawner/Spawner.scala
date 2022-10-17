package model.spawner

import model.collisions.Affiliation
import model.elements2d.Point2D
import model.missile.{DeltaTime, Missile, damage, initialLife, velocity}
import model.missile.Missile
import scala.util.Random

/**
 * Fare uno spawner che genera missili ad un intervallo di tempo variabile.
 * Deve poter essere modificato il tempo, e deve essere timeable.
 * Il dt deve essere minore o superiore al dt: se superiore allora genero più di un missile.
 * Ritorno una lista di missili. Sfrutto il generatore del singolo missile.
 * Devo prendere in input i parametri riguardo a altezza e larghezza (range) in cui generare i missili,
 * per poter essere riutilizzabile anche per il satellite ad esempio.
 *
 * classe lista di missili
 */

trait Spawner(using Random):

  def spawn(dt: DeltaTime): List[Missile]

  def changeInterval(newInterval: DeltaTime, spawner: Spawner): Spawner

object Spawner:

  given affiliation: Affiliation = Affiliation.Enemy

  def apply(interval: DeltaTime, maxWidth: Double, maxHeight: Double) = new Spawner(using Random) {

    override def spawn(dt: DeltaTime): List[Missile] = dt match
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

    override def changeInterval(newInterval: DeltaTime, spawner: Spawner) = ???

  }

  @main def test(): Unit = {
    val spawner = Spawner(5, 10, 10)

    println(spawner.spawn(10))
  }
