package model.spawner

import model.elements2d.Point2D
import model.missile.Missile
import model.spawner.LazySpawner.Spawnable

import scala.util.Random

extension(r: Random)
  def randomPosition(maxWidth: Double, maxHeight: Double): Point2D = Point2D(r.nextDouble() * maxWidth, r.nextDouble() * maxHeight)
  def randomX(maxWidth: Double): Point2D = Point2D(r.nextDouble() * maxWidth, 0)
  def randomY(maxHeight: Double): Point2D = Point2D(0, r.nextDouble() * maxHeight)

/**
 * Specific spawners strategies to spawn objects
 * missili, plane
 * plane spawnabile in un range witdh1 e width2
 */
object SpecificSpawners:

  def MissileStrategy(maxWidth: Double, maxHeight: Double)(using r: Random): Spawnable[Missile] =
    () => Missile.enemyMissile(position = r.randomX(maxWidth), finalPosition = r.randomX(maxHeight))
    
