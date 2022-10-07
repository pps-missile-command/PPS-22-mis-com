package model.collisions

import model.collisions.hitbox.HitBoxCircular
import model.elements2d.Point2D
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funspec.AnyFunSpec
import math.BigDecimal.double2bigDecimal
import model.elements2d.Point2D.GivenEquality.given
import utilities.MathUtilities._

object HitBoxCircularTest:
  private val tolerance: Double = 0.1

  private given equality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(tolerance)

  private given distance: Distance = 1.0

  private val xCenter = 1.0
  private val yCenter = 2.0
  private val radius = 1.0
  private val center = Point2D(xCenter, yCenter)
  private val hitBox = HitBoxCircular(center, radius)

class HitBoxCircularTest extends AnyFunSpec :

  import HitBoxCircularTest._
  import HitBoxCircularTest.given

  describe("An hit box") {

    describe("with a circular shape") {

      it("should be usable multiple times") {
        for _ <- hitBox.area
          yield ()
        assert(hitBox.area.hasNext)
      }

      it("should have an interval from center - radius to center + radius") {
        assert(hitBox.xMax.get === (center.x + radius))
        assert(hitBox.xMin.get === (center.x - radius))
        assert(hitBox.yMax.get === (center.y + radius))
        assert(hitBox.yMin.get === (center.y - radius))
      }

      it("should be empty if the radius is 0") {
        val hitBox = HitBoxCircular(center, 0)
        assert(hitBox.area.isEmpty)
      }

      it("should be empty if the radius is negative") {
        val hitBox = HitBoxCircular(center, -1)
        assert(hitBox.area.isEmpty)
      }

      it("should have the points in the circle with radius inside") {
        for
          x <- xCenter - radius to xCenter + radius by distance
          y <- yCenter - radius to yCenter + radius by distance
          if ((x.doubleValue - center.x) ** 2.0) + ((y.doubleValue - center.y) ** 2.0) <= radius ** 2.0
        yield
          assert(hitBox.contains(Point2D(x.doubleValue, y.doubleValue)))
      }

      it("shouldn't have the points outside the circle radius") {
        for
          x <- xCenter - radius - 1 to xCenter + radius + 1 by distance
          y <- yCenter - radius - 1 to yCenter + radius + 1 by distance
          if ((x.doubleValue - center.x) ** 2.0) + ((y.doubleValue - center.y) ** 2.0) > radius ** 2.0
        yield
          assert(!hitBox.contains(Point2D(x.doubleValue, y.doubleValue)))
      }

      it("shouldn't have the points outside the circle radius explicit test") {
        val point = Point2D(xCenter + radius, yCenter + radius)
        assert(!hitBox.contains(point))
      }
    }
  }
