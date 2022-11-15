package model.elements2d

import model.elements2d.Point2DTest.{xTest, yTest}
import org.scalactic.{Equality, TolerantNumerics}
import org.scalactic.TripleEquals.convertToEqualizer
import org.scalatest.funspec.AnyFunSpec
import Point2D.GivenEquality.given


object Point2DTest:

  private val xTest = 3.0
  private val yTest = 4.0

class Point2DTest extends AnyFunSpec :

  import Point2DTest._

  describe("A Point2D") {
    it("should be created with a x and y coordinate") {
      val point = Point2D(xTest, yTest)
      assert(point.x === xTest)
      assert(point.y === yTest)
    }

    it("should be able to be translated") {
      val point = Point2D(xTest, yTest)
      val vector = Vector2D(1, 1)
      val expectedPoint = Point2D(xTest + 1, yTest + 1)
      val translatedPoint = point --> vector
      assert(translatedPoint === expectedPoint)
    }

    it("shouldn't be able to be translated with a zero vector") {
      val point = Point2D(xTest, yTest)
      val translatedPoint = point --> Vector2D.Zero
      assert(translatedPoint === point)
    }

    it("should be able to scale") {
      val point = Point2D(xTest, yTest)
      val scale = Vector2D(2, 2)
      val expectedPoint = Point2D(xTest * 2, yTest * 2)
      val scaledPoint = point * scale
      assert(scaledPoint === expectedPoint)
    }

    it("should be able to scale with a single value") {
      val point = Point2D(xTest, yTest)
      val scale = 2
      val expectedPoint = Point2D(xTest * 2, yTest * 2)
      val scaledPoint = point * scale
      assert(scaledPoint === expectedPoint)
    }

    it("should be able to calculate the distance between two points as vector") {
      val point1 = Point2D(0, 0)
      val point2 = Point2D(xTest, yTest)
      val expectedDistance = Vector2D(xTest, yTest)
      val distance = point1 <--> point2
      assert(distance === expectedDistance)
    }

    it("should be able to calculate the distance between two points as double") {
      val point1 = Point2D(0, 0)
      val point2 = Point2D(xTest, yTest)
      val expectedDistance = 5.0
      val distance = point1 <-> point2
      assert(distance === expectedDistance)
    }

    it("shouldn't be equal to a vector2D") {
      val point = Point2D(xTest, yTest)
      val vector = Vector2D(xTest, yTest)
      assert(point !== vector)
    }
  }
