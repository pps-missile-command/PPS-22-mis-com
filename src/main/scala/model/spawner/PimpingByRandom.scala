package model.spawner

import model.elements2d.Point2D
import model.vehicle.vehicleTypes

import scala.util.Random

object PimpingByRandom:
  extension(r: Random)
    def nextRandomPosition(maxWidth: Double, maxHeight: Double): Point2D = Point2D(r.nextDouble() * maxWidth, r.nextDouble() * maxHeight)
    def nextRandomX(maxWidth: Double, y: Double): Point2D = Point2D(r.nextDouble() * maxWidth, y)
    def nextRandomY(maxHeight: Double, x: Double): Point2D = Point2D(x, r.nextDouble() * maxHeight)
    def nextRandomDirection(): vehicleTypes = r.nextDouble() match
      case n if n < 0.5 => vehicleTypes.Right_To_Left
      case _ => vehicleTypes.Left_To_Right
