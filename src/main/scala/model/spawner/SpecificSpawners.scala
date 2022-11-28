package model.spawner

import model.elements2d.Point2D
import model.missile
import model.missile.Missile
import model.missile.zigzag
import model.missile.zigzag.ZigZagMissile.*
import model.spawner.PimpingByRandom.*
import model.spawner.Spawnable.Spawnable
import model.vehicle.Plane

import scala.util.Random

/**
 * Strategies for spawning a specific entity as a library of Spawnable factories
 */
object SpecificSpawners:
  /**
   * Factory method to spawn linear missiles
   * @param maxWidth The max width starting from 0 to generate the missile
   * @param maxHeight The max height for the destination point
   * @param r The random object to generate random coordinates
   * @return the new linear missile random generated
   */
  def MissileStrategy(maxWidth: Double, maxHeight: Double)(using r: Random): Spawnable[Missile] =
    () => Missile.enemyMissile(position = r.nextRandomX(maxWidth, 0), finalPosition = r.nextRandomX(maxWidth, maxHeight))

  /**
   * Factory method to spawn a missile in a given position and a random destination position
   * @param maxWidth The max width starting from 0 to generate the missile
   * @param maxHeight The max height for the destination point
   * @param spawnPosition The position where to spawn the missile
   * @param r The random object to generate random coordinates
   * @return the new missile
   */
  def FixedMissileStrategy(maxWidth: Double, maxHeight: Double, spawnPosition: Point2D)(using r: Random): Spawnable[Missile] =
    () => Missile.enemyMissile(position = spawnPosition, finalPosition = r.nextRandomX(maxWidth, maxHeight))

  /**
   * Factory method to generate a zigzag missile
   * @param maxWidth The max width starting from 0 to generate the missile
   * @param maxHeight The max height for the destination point
   * @param r The random object to generate random coordinates
   * @return the new zig zag missile
   */
  def ZigZagStrategy(maxWidth: Double, maxHeight: Double)(using r: Random): Spawnable[Missile] =
    () => missile.zigzag.ZigZagMissile(r.nextRandomX(maxWidth, 0), r.nextRandomX(maxWidth, maxHeight), 5, maxWidth = maxWidth)

  /**
   * Factory method for a plane generator
   * @param maxHeight The max height where to spawn the new plane starting from 0
   * @param r The random object to generate random coordinates
   * @return the new plane
   */
  def PlaneStrategy(maxHeight: Double)(using r: Random): Spawnable[Plane] =
    () => Plane(r.nextRandomDirection(), r.nextDouble() * model.missile.maxHeight)
