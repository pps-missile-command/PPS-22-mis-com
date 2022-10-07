package model.collisions.hitbox

import model.collisions.{HitBox, Distance}
import model.elements2d.Point2D
import org.scalactic.Equality

/**
 * Object that represents a hit box that is empty.
 */
case object HitBoxEmpty extends HitBox :

  /**
   * The hit box is empty so it doesn't have an interval.
   *
   * @return always option empty
   */
  override val xMax: Option[Double] = Option.empty

  /**
   * The hit box is empty so it doesn't have an interval.
   *
   * @return always option empty
   */
  override val yMax: Option[Double] = Option.empty

  /**
   * The hit box is empty so it doesn't have an interval.
   *
   * @return always option empty
   */
  override val xMin: Option[Double] = Option.empty

  /**
   * The hit box is empty so it doesn't have an interval.
   *
   * @return always option empty
   */
  override val yMin: Option[Double] = Option.empty

  /**
   * The hit box is empty so it doesn't have any point.
   *
   * @return always Iterator empty
   */
  override def area(using step: Distance = 0): Iterator[Point2D] = Iterator.empty

  /**
   * The hit box is empty so it doesn't contains any point.
   *
   * @return always false
   */
  override def contains(point: Point2D)(using equality: Equality[Double] = null): Boolean = false
