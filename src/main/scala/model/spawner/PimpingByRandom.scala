package model.spawner

import model.elements2d.Point2D
import model.vehicle.planeTypes

import scala.util.Random

/**
 * Pimp library of a Random object
 */
object PimpingByRandom:

  extension(r: Random)

    /**
     * Method that return a [[Point2D]] with a random x value between 0 and [[maxWidth]] and the same for y value, mapped
     * between 0 and [[maxHeight]]
     * @param maxWidth The max width within to generate the x value
     * @param maxHeight The max height within to generate the y value
     * @return the [[Point2D]] generated
     */
    def nextRandomPosition(maxWidth: Double, maxHeight: Double): Point2D =
      Point2D(r.nextDouble() * maxWidth, r.nextDouble() * maxHeight)

    /**
     * Method that generate a Point2D with the x value generated randomly between 0 and [[maxWidth]], while the y
     * value is fixed and given by input
     * @param maxWidth The max width within to generate the x coordinate
     * @param y The y coordinate
     * @return the new [[Point2D]] generated
     */
    def nextRandomX(maxWidth: Double, y: Double): Point2D = Point2D(r.nextDouble() * maxWidth, y)

    /**
     * Method that generate a Point2D with the y value generated randomly between 0 and [[maxHeight]], while the x
     * value is fixed and given by input
     * @param maxHeight The max height within to generate the y coordinate
     * @param x The x coordinate
     * @return the new random [[Point2D]] generated
     */
    def nextRandomY(maxHeight: Double, x: Double): Point2D = Point2D(x, r.nextDouble() * maxHeight)

    /**
     * Method that generate a random direction for planes, based on a random double number
     * @return the direction randomly generated
     */
    def nextRandomDirection(): planeTypes = r.nextDouble() match
        case n if n < 0.5 => planeTypes.Right_To_Left
        case _ => planeTypes.Left_To_Right
