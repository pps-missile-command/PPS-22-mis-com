package model.missile.zigzag

import model.elements2d.Point2D

object PimpingByPoint2D:
  /**
   * Extension method that check if the point is within the world (between 0 and max width), and
   * adjust the x component if the point is out of bound
   */
  extension(p: Point2D)
    def ~=(p1: Point2D): Boolean =
      (Math.round(p.x * 100) / 100) == (Math.round(p1.x * 100) / 100) && (Math.round(p.y * 100) / 100) == (Math.round(p1.y * 100) / 100)

    def filterMap(maxWidth: Double): Point2D = p.x match
      case n if n > maxWidth => Point2D(maxWidth, p.y)
      case n if n < 0 => Point2D(0, p.y)
      case _ => p