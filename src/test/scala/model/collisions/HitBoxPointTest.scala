package model.collisions

import model.collisions.hitbox._
import model.elements2d.Point2D
import org.scalatest.funspec.AnyFunSpec
import math.BigDecimal.double2bigDecimal
import org.scalactic.{Equality, TolerantNumerics}
import utilities.MathUtilities._
import model.elements2d.Point2D.GivenEquality.given
import utilities.MathUtilities.DoubleEquality.given

object HitBoxPointTest:
  private given distance: Distance = 1.0

  private val pointX = 1.0
  private val pointY = 2.0
  private val hitBox = HitBoxPoint(Point2D(pointX, pointY))

class HitBoxPointTest extends AnyFunSpec :

  import HitBoxPointTest._
  import HitBoxPointTest.given

  describe("An hit box") {
    describe("that is a point") {

      it("should only have an iterator with one point") {
        val hitBoxIterator = hitBox.area
        assert(hitBoxIterator.hasNext)
        assert(hitBoxIterator.next() === Point2D(pointX, pointY))
        assert(!hitBoxIterator.hasNext)
      }

      it("should be usable multiple times") {
        for _ <- hitBox.area
          yield ()
        assert(hitBox.area.hasNext)
      }

      it("should contain the point") {
        assert(hitBox.contains(Point2D(pointX, pointY)))
      }

      it("should have the point as interval") {
        assert(hitBox.xMax == Option(pointX))
        assert(hitBox.xMin == Option(pointX))
        assert(hitBox.yMax == Option(pointY))
        assert(hitBox.yMin == Option(pointY))
      }

      it("shouldn't have other points inside") {
        for
          x <- -1.0 to 3.0 by distance
          y <- 0.0 to 4.0 by distance
          if (x !== pointX) && (y !== pointY)
        yield
          assert(!hitBox.contains(Point2D(x.doubleValue, y.doubleValue)))
      }

      it("should have the points in the double tolerance") {
        val tolerance: Double = 0.1

        given equality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(tolerance)

        val otherPoint = Point2D(pointX + tolerance / 2, pointY + tolerance / 2)
        assert(Point2D(pointX, pointY) === otherPoint)
        assert(hitBox.contains(Point2D(pointX, pointY)))
      }
    }
  }