package model.elements2d

import scala.annotation.targetName

/**
 * The representation of a vector in 2 dimensional space.
 */
enum Vector2D:

  /**
   * Creates a new vector with the given coordinates.
   *
   * @param xComponent the x coordinate
   * @param yComponent the y coordinate
   */
  @targetName("vector2D")
  case <>(xComponent: Double, yComponent: Double)

  /**
   * Return the Zero vector (additive identity).
   */
  case Zero

  /**
   * The x component of the vector.
   *
   * @return the x component of the vector
   */
  def x: Double = this match
    case <>(x, _) => x
    case Zero => 0

  /**
   * The y component of the vector.
   *
   * @return the y component of the vector
   */
  def y: Double = this match
    case <>(_, y) => y
    case Zero => 0


  /**
   * The magnitude of the vector.
   *
   * @return the magnitude of the vector
   */
  def magnitude: Double = this match
    case <>(x, y) => math.sqrt(x * x + y * y)
    case Zero => 0

  /**
   * The direction of the vector.
   *
   * @return an option with the angle of vector's direction if the vector is not zero, otherwise a empty option
   */
  def direction: Option[Angle] = this match
    case <>(x, y) => Option(Angle.Radian(math.atan2(y, x)))
    case Zero => Option.empty

  /**
   * Calculates the sum of this vector and another vector.
   *
   * @param other the other vector
   * @return a new vector that is  the sum of the two vectors if sum of the two vectors is the vector Zero returns the vector Zero
   */
  @targetName("plus")
  def +(other: Vector2D): Vector2D = <>(x + other.x, y + other.y) match
    case <>(0, 0) => Zero
    case vector => vector

  /**
   * Calculates the difference of this vector and another vector.
   *
   * @param other the other vector
   * @return a new vector that is the difference of the two vectors if difference of the two vectors is the vector Zero returns the vector Zero
   */
  @targetName("minus")
  def -(other: Vector2D): Vector2D = <>(x - other.x, y - other.y) match
    case <>(0, 0) => Zero
    case vector => vector

  /**
   * Calculates the product of this vector and a scalar.
   *
   * @param scalar the scalar value to multiply the vector with
   * @return a new vector that is the product of the vector and the scalar
   */
  @targetName("multiplyByScalar")
  def *(scalar: Double): Vector2D = <>(x * scalar, y * scalar) match
    case <>(0, 0) => Zero
    case vector => vector

  /**
   * Calculates the quotient of this vector and a scalar.
   *
   * @param scalar the scalar value to divide the vector with
   * @return a new vector that is the quotient of the vector and the scalar
   */
  @targetName("divideByScalar")
  def /(scalar: Double): Vector2D = this * (1 / scalar)

  /**
   * Calculates the normalized vector (magnitude =1) of the current vector.
   *
   * @return a new vector that is the normalized vector of the current vector or Zero
   */
  def normalize: Vector2D = this match
    case Zero => Zero
    case vector => vector / magnitude

  /**
   * Calculates the opposite vector of the current vector.
   *
   * @return a new vector that is the opposite vector of the current vector
   */
  @targetName("opposite")
  def unary_- : Vector2D = this * -1

/**
 * Companion object for the Vector2D class.
 */
object Vector2D:

  /**
   * Object that contains the givens for the Vector2D class equality (===).
   */
  object GivenEquality:

    import org.scalactic.{Equality, TolerantNumerics}
    import org.scalactic.TripleEquals.convertToEqualizer

    /**
     * The given for the equality of two Double.
     * The tolerance is 0.01.
     *
     * @return the given for the equality of two Double with tolerance 0.01
     */
    given Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.01)

    /**
     * The given for the equality of two Vector2D.
     * The tolerance is used for the x and y components.
     *
     * @param equality the given equality for the Double
     * @return the given for the equality of two Vector2D
     */
    given Vector2DEquality(using equality: Equality[Double]): Equality[Vector2D] with
      def areEqual(vector: Vector2D, other: Any): Boolean =
        other match
          case otherVector: Vector2D => vector.x === otherVector.x && vector.y === otherVector.y
          case _ => false


  /**
   * Creates a new vector with the given x and y, if the x and y are 0 return the vector Zero.
   *
   * @param x the x component of the vector
   * @param y the y component of the vector
   * @return a new vector with the given x and y, if the x and y are 0 return the vector Zero
   */
  def apply(x: Double, y: Double): Vector2D = (x, y) match
    case (0, 0) => Zero
    case _ => <>(x, y)

  /**
   * Creates a new vector with the given magnitude and direction, if the magnitude is 0 return the vector Zero.
   *
   * @param magnitude the magnitude of the vector
   * @param direction the angle of vector's direction
   * @return a new vector with the given magnitude and direction, if the magnitude is 0 or the direction is null return the vector Zero
   */
  def apply(magnitude: Double, direction: Angle): Vector2D = magnitude match
    case 0 => Zero
    case _ if direction == null => Zero
    case _ => <>(math.cos(direction.radiant), math.sin(direction.radiant)) * magnitude

