package model.collisions.hitbox

import model.collisions.*
import model.collisions.hitbox.HitBoxSymmetric
import model.elements2d.{Angle, Point2D, Vector2D}
import org.scalactic.{Equality, TripleEquals}
import utilities.MathUtilities.*

/**
 * Factory for an hit box that is a rectangle.
 */
object HitBoxRectangular:

  /**
   * Returns a new hit box that is a rectangle.
   *
   * @param center   the center of the rectangle.
   * @param base     the base of the rectangle.
   * @param height   the height of the rectangle.
   * @param rotation the rotation of the rectangle.
   * @return a new hit box that is a rectangle.
   */
  def apply(center: Point2D, base: Double, height: Double, rotation: Angle): HitBox =
    if (base <= 0 || height <= 0 || rotation == null)
      HitBoxEmpty
    else
      HitBoxRectangular(center, base, height, rotation)

  private case class HitBoxRectangular(center: Point2D, base: Double, height: Double, rotation: Angle) extends HitBoxSymmetric :
    private val baseVector = Vector2D(base / 2, rotation)
    private val heightVector = Vector2D(height / 2, Angle.Degree(rotation.degree + 90))
    private val vertices: List[Point2D] = List(
      center --> (baseVector + heightVector),
      center --> (-baseVector + heightVector),
      center --> (-baseVector + -heightVector),
      center --> (baseVector + -heightVector)
    )

    /*override val xMax: Option[Double] = Option(vertices.map(_.x).max)

    override val yMax: Option[Double] = Option(vertices.map(_.y).max)

    override val xMin: Option[Double] = Option(vertices.map(_.x).min)

    override val yMin: Option[Double] = Option(vertices.map(_.y).min)*/
    protected val x: Iterable[Double] =
      vertices.map(_.x)
    protected def y: Iterable[Double] =
      vertices.map(_.y)

    protected def max(values: Iterable[Double]): Option[Double] = Option(values.max)

    protected def min(values: Iterable[Double]): Option[Double] = Option(values.min)

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean =
      def lineEquation(p: Point2D, p1: Point2D, p2: Point2D): Double =
        (p2.y - p1.y) * (p.x - p1.x) - (p2.x - p1.x) * (p.y - p1.y)

      (lineEquation(point, vertices(1), vertices(0)) >== 0.0) &&
        (lineEquation(point, vertices(2), vertices(1)) >== 0.0) &&
        (lineEquation(point, vertices(3), vertices(2)) >== 0.0) &&
        (lineEquation(point, vertices(0), vertices(3)) >== 0.0)
