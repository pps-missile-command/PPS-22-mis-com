package model.collisions.hitbox

import model.collisions.HitBox
import model.elements2d.{Angle, Point2D, Vector2D}
import utilities.MathUtilities.*
import model.collisions.*
import org.scalactic.{Equality, TripleEquals}

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
   * @param step     the step given to calculate the point in the rectangular hit box.
   * @return a new hit box that is a rectangle.
   */
  def apply(center: Point2D, base: Double, height: Double, rotation: Angle)(using step: Step): HitBox = HitBoxRectangular(center, base, height, rotation)

  private case class HitBoxRectangular(center: Point2D, base: Double, height: Double, rotation: Angle)(using step: Step) extends HitBox :
    private val baseVector = Vector2D(base / 2, rotation)
    private val heightVector = Vector2D(height / 2, Angle.Degree(rotation.degree + 90))
    private val vertices: List[Point2D] = List(
      center --> (baseVector + heightVector),
      center --> (-baseVector + heightVector),
      center --> (-baseVector + -heightVector),
      center --> (baseVector + -heightVector)
    )

    /*val (x, y) = (point.x, point.y)
    val (x1, y1) = (vertices(0).x, vertices(0).y)
    val (x2, y2) = (vertices(1).x, vertices(1).y)
    val (x3, y3) = (vertices(2).x, vertices(2).y)
    val (x4, y4) = (vertices(3).x, vertices(3).y)
    val a1 = (y2 - y1) * (x - x1) - (x2 - x1) * (y - y1)
    val a2 = (y3 - y2) * (x - x2) - (x3 - x2) * (y - y2)
    val a3 = (y4 - y3) * (x - x3) - (x4 - x3) * (y - y3)
    val a4 = (y1 - y4) * (x - x4) - (x1 - x4) * (y - y4)
    a1 >= 0 && a2 >= 0 && a3 >= 0 && a4 >= 0*/


    override val xMax: Option[Double] = Option(vertices.map(_.x).max)

    override val yMax: Option[Double] = Option(vertices.map(_.y).max)

    override val xMin: Option[Double] = Option(vertices.map(_.x).min)

    override val yMin: Option[Double] = Option(vertices.map(_.y).min)

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean =
      def lineEquation(p: Point2D, p1: Point2D, p2: Point2D): Double =
        (p2.y - p1.y) * (p.x - p1.x) - (p2.x - p1.x) * (p.y - p1.y)

      (lineEquation(point, vertices(1), vertices(0)) >== 0.0) &&
        (lineEquation(point, vertices(2), vertices(1)) >== 0.0) &&
        (lineEquation(point, vertices(3), vertices(2)) >== 0.0) &&
        (lineEquation(point, vertices(0), vertices(3)) >== 0.0)
