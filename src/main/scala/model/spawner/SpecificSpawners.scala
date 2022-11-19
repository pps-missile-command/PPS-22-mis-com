package model.spawner

import model.elements2d.Point2D
import model.missile
import model.missile.Missile
import model.missile.zigzag.ZigZagMissile.ZigZagMissile
import model.spawner.LazySpawner.Spawnable

import scala.util.Random

extension(r: Random)
  def randomPosition(maxWidth: Double, maxHeight: Double): Point2D = Point2D(r.nextDouble() * maxWidth, r.nextDouble() * maxHeight)
  def randomX(maxWidth: Double, y: Double): Point2D = Point2D(r.nextDouble() * maxWidth, y)
  def randomY(maxHeight: Double, x: Double): Point2D = Point2D(x, r.nextDouble() * maxHeight)

/**
 * Specific spawners strategies to spawn objects
 * missili, plane
 * plane spawnabile in un range witdh1 e width2
 */
object SpecificSpawners:

  def MissileStrategy(maxWidth: Double, maxHeight: Double)(using r: Random): Spawnable[Missile] =
    () => Missile.enemyMissile(position = r.randomX(maxWidth, 0), finalPosition = r.randomX(maxWidth, maxHeight))

  def ZigZagStrategy(maxWidth: Double, maxHeight: Double)(using r: Random): Spawnable[Missile] =
    () => missile.zigzag.ZigZagMissile(r.randomX(maxWidth, 0), r.randomX(maxWidth, maxHeight), 5)
    
