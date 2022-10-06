package model.elements2d

import scala.annotation.targetName

/**
 * The representation a point in a 2 dimensional space.
 */
trait Point2D:

  /**
   * The x coordinate of the point.
   *
   * @return the x coordinate of the point.
   */
  def x: Double

  /**
   * The y coordinate of the point.
   *
   * @return the y coordinate of the point.
   */
  def y: Double

  /**
   * Returns a new point translated by the given vector.
   *
   * @param vector the vector that coordinate the point has to bes translated
   * @return returns a new point translated by the given vector
   */
  @targetName("translate")
  def -->(vector: Vector2D): Point2D = Point2D(x + vector.x, y + vector.y)

  /**
   * Returns the distance between this point and the given point.
   *
   * @param other the point to which the distance is calculated
   * @return the distance between this point and the given point
   */
  @targetName("distance")
  def <->(other: Point2D): Double = (this <--> other).magnitude

  /**
   * Returns the vector between this point and the given point.
   *
   * @param other the point to which the vector is calculated
   * @return the vector between this point and the given point
   */
  @targetName("distance")
  def <-->(other: Point2D): Vector2D = Vector2D(other.x - x, other.y - y)

  /**
   * Returns a new point that is the result of the scale of this point of a given vector.
   *
   * @param vector the vector that coordinate the point has to be scaled
   * @return a new point that is the result of the scale of this point of a given vector
   */
  @targetName("scale")
  def *(vector: Vector2D): Point2D = Point2D(x * vector.x, y * vector.y)

  /**
   * Returns a new point that is the result of the scale of this point of a scalar.
   *
   * @param scale the scalar that coordinate the point has to be scaled
   * @return a new point that is the result of the scale of this point of a given scalar
   */
  @targetName("scale")
  def *(scale: Double): Point2D = Point2D(x * scale, y * scale)

/**
 * The companion object of the Point2D trait.
 */
object Point2D:

  import org.scalactic.{Equality, TolerantNumerics}
  import org.scalactic.TripleEquals.convertToEqualizer

  /**
   * Object that provides the equality for the Point2D trait.
   */
  object GivenEquality:

    /**
     * The given for the equality of two Double.
     * The tolerance is 0.01.
     *
     * @return the given for the equality of two Double with tolerance 0.01
     */
    given Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.01)

    /**
     * The given for the equality of two Point2D.
     * The tolerance is used for the x and y coordinates.
     *
     * @param equality the given equality for the Double
     * @return the given for the equality of two Point2D
     */
    given Point2DEquality(using equality: Equality[Double]): Equality[Point2D] with
      def areEqual(point: Point2D, other: Any): Boolean =
        other match
          case otherPoint: Point2D =>
            println(point.x === otherPoint.x)
            println(point.y === otherPoint.y)
            point.x === otherPoint.x && point.y === otherPoint.y
          case _ => false


  /**
   * Creates a new point with the given coordinates.
   *
   * @param xCoordinate the x coordinate of the point
   * @param yCoordinate the y coordinate of the point
   * @return a new point with the given coordinates
   */
  def apply(xCoordinate: Double, yCoordinate: Double): Point2D = new Point2D :
    override def x: Double = xCoordinate

    override def y: Double = yCoordinate