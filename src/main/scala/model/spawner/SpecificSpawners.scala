package model.spawner

import model.elements2d.Point2D
import model.missile
import model.missile.Missile
import model.missile.zigzag
import model.missile.zigzag.ZigZagMissile.*
import model.spawner.LazySpawner.Spawnable
import model.spawner.PimpingByRandom.*
import model.vehicle.Plane

import scala.util.Random

/**
 * Specific spawners strategies to spawn objects
 * missili, plane
 * plane spawnabile in un range witdh1 e width2
 */
object SpecificSpawners:

  def MissileStrategy(maxWidth: Double, maxHeight: Double)(using r: Random): Spawnable[Missile] =
    () => Missile.enemyMissile(position = r.nextRandomX(maxWidth, 0), finalPosition = r.nextRandomX(maxWidth, maxHeight))

  def ZigZagStrategy(maxWidth: Double, maxHeight: Double)(using r: Random): Spawnable[Missile] =
    () => missile.zigzag.ZigZagMissile(r.nextRandomX(maxWidth, 0), r.nextRandomX(maxWidth, maxHeight), 5, maxWidth = maxWidth)

  def PlaneStrategy(maxHeight: Double)(using r: Random): Spawnable[Plane] =
    () => Plane(r.nextRandomDirection(), r.nextDouble() * model.missile.maxHeight)
