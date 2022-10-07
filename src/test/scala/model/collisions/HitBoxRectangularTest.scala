package model.collisions

import model.collisions.hitbox.HitBoxRectangular
import model.elements2d.{Point2D, Vector2D}
import org.scalatest.funspec.AnyFunSpec
import math.BigDecimal.double2bigDecimal
import model.elements2d.Point2D.GivenEquality.given
import org.scalactic.{Equality, TolerantNumerics}
import model.elements2d.Angle
import utilities.MathUtilities._

object HitBoxRectangularTest:
  private given step: Step = 1.0

  private val tolerance: Double = 0.1

  private given equality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(tolerance)

  private val xCenter = 1.0
  private val yCenter = 2.0
  private val base = 2.0
  private val height = 1.0
  private val angle = 0.0
  private val center = Point2D(xCenter, yCenter)

class HitBoxRectangularTest extends AnyFunSpec :

  import HitBoxRectangularTest._
  import HitBoxRectangularTest.given

  describe("An hit box") {

    describe("with a rectangular shape") {
      val hitBox = HitBoxRectangular(center, base, height, Angle.Degree(angle))

      it("should be usable multiple times") {
        for _ <- hitBox
          yield ()
        assert(hitBox.iterator.hasNext)
      }

      describe(" without rotation") {

        it("should have an interval from center - base / 2 to center + base / 2 and center - height / 2 to center + height / 2") {
          assert(hitBox.xMax.get === (center.x + base / 2))
          assert(hitBox.xMin.get === (center.x - base / 2))
          assert(hitBox.yMax.get === (center.y + height / 2))
          assert(hitBox.yMin.get === (center.y - height / 2))
        }

        it("should have the points in the rectangle with the given base and height with the given center inside") {
          for
            x <- xCenter - base / 2 to xCenter + base / 2 by step
            y <- yCenter - height / 2 to yCenter + height / 2 by step
          yield
            assert(hitBox.contains(Point2D(x.doubleValue, y.doubleValue)))
        }

        it("shouldn't have the points outside the rectangle with the given base and height with the given center") {
          for
            x <- xCenter - base / 2 - 1 to xCenter + base / 2 + 1 by step
            y <- yCenter - height / 2 - 1 to yCenter + height / 2 + 1 by step
            if x < xCenter - base / 2 || x > xCenter + base / 2 || y < yCenter - height / 2 || y > yCenter + height / 2
          yield
            assert(!hitBox.contains(Point2D(x.doubleValue, y.doubleValue)))
        }
      }

      describe("with 90° rotation") {
        val angle = 90.0
        val hitBoxRotated = HitBoxRectangular(center, base, height, Angle.Degree(angle))

        it("should have an interval from center - base / 2 to center + base / 2 and center - height / 2 to center + height / 2") {
          assert(hitBoxRotated.xMax.get === (center.x + height / 2))
          assert(hitBoxRotated.xMin.get === (center.x - height / 2))
          assert(hitBoxRotated.yMax.get === (center.y + base / 2))
          assert(hitBoxRotated.yMin.get === (center.y - base / 2))
        }

        it("should have the points in the rectangle with the given base and height with the given center inside") {
          for
            x <- xCenter - height / 2 to xCenter + height / 2 by step
            y <- yCenter - base / 2 to yCenter + base / 2 by step
          yield
            assert(hitBoxRotated.contains(Point2D(x.doubleValue, y.doubleValue)))
        }

        it("shouldn't have the points outside the rectangle with the given base and height with the given center") {
          for
            x <- xCenter - height / 2 - 1 to xCenter + height / 2 + 1 by step
            y <- yCenter - base / 2 - 1 to yCenter + base / 2 + 1 by step
            if x < xCenter - height / 2 || x > xCenter + height / 2 || y < yCenter - base / 2 || y > yCenter + base / 2
          yield
            assert(!hitBoxRotated.contains(Point2D(x.doubleValue, y.doubleValue)))
        }
      }

      describe("with 45° rotation") {
        val angle = 45.0
        val side = base
        val hitBox = HitBoxRectangular(center, side, side, Angle.Degree(angle))

        it("should have an interval based on its diagonal") {
          val diagonal = side * math.sqrt(2.0)
          assert(hitBox.xMax.get === (center.x + diagonal / 2))
          assert(hitBox.xMin.get === (center.x - diagonal / 2))
          assert(hitBox.yMax.get === (center.y + diagonal / 2))
          assert(hitBox.yMin.get === (center.y - diagonal / 2))
        }

        it("should have the points in the rectangle with the given area the given center inside") {
          for
            x <- hitBox.xMin.get to hitBox.xMax.get by step
            y <- hitBox.yMin.get to hitBox.yMax.get by step
            if (math.abs(x.doubleValue - center.x) + math.abs(y.doubleValue - center.y)) <== (side * math.sqrt(2.0) / 2)
          yield
            assert(hitBox.contains(Point2D(x.doubleValue, y.doubleValue)))
        }

        it("shouldn't have the points outside the rectangle with the given area the given center") {
          for
            x <- hitBox.xMin.get - 1 to hitBox.xMax.get + 1 by step
            y <- hitBox.yMin.get - 1 to hitBox.yMax.get + 1 by step
            if (math.abs(x.doubleValue - center.x) + math.abs(y.doubleValue - center.y)) >== (side * math.sqrt(2.0) / 2)
          yield
            assert(!hitBox.contains(Point2D(x.doubleValue, y.doubleValue)))
        }
      }
    }
  }
