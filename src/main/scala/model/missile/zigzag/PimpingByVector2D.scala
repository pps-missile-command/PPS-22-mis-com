package model.missile.zigzag
import model.missile.zigzag.PimpingByDouble.mapToSign

import model.elements2d.{Angle, Vector2D}

object PimpingByVector2D:
  
  extension(v: Vector2D)

    /**
     * Extension method that define a 90 degree rotation of a given [[Vector2D]], taking the direction ([[Left]], [[Right]] or [[Rand]])
     * and return the vector rotated by 90° respectively to the left, to the right or a random one between the two types
     * @param d The direction of the missile
     * @param magnitude The magnitude to use in order to rotate the vector
     * @return the new vector rotated
     */
    def -|-(d: Direction, magnitude: Int = defaultMagnitude): Vector2D = d match
        case Right => Vector2D(magnitude, Angle.Degree(v.direction.get.degree + 90))
        case Left => Vector2D(magnitude, Angle.Degree(v.direction.get.degree - 90))
        case Rand(rand) =>
          val value = rand.nextDouble()
          Vector2D(1, Angle.Degree(v.direction.get.degree + 90 * value.mapToSign))
        case _ => v