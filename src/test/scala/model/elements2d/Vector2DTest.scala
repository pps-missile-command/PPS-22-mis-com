package model.elements2d

import org.scalatest.funspec.AnyFunSpec
import org.scalactic.Equality
import Vector2D.GivenEquality.given

object Vector2DTest:

  private val xTest = 3.0
  private val yTest = 4.0
  private val magnitudeTest = 5.0
  private val angleDegreeTest = 53.13
  private val angleStraightDegree = 180.0


class Vector2DTest extends AnyFunSpec :

  import Vector2DTest._
  import Vector2D._

  describe("A Vector 2D") {
    describe("when is a vector") {
      it("should be created from a x and y coordinate and have their values") {
        val vector2D = Vector2D(xTest, yTest)
        assert(vector2D.x === xTest)
        assert(vector2D.y === yTest)
      }

      it("should be created from a magnitude and a direction and have their values 1° quadrant") {
        val vector2D = Vector2D(magnitudeTest, Angle.Degree(angleDegreeTest))
        assert(vector2D.x === xTest)
        assert(vector2D.y === yTest)
      }

      it("should be created from a magnitude and a direction and have their values 2° quadrant") {
        val vector2D = Vector2D(magnitudeTest, Angle.Degree(angleStraightDegree - angleDegreeTest))
        assert(vector2D.x === -xTest)
        assert(vector2D.y === yTest)
      }

      it("should be created from a magnitude and a direction and have their values 3° quadrant") {
        val vector2D = Vector2D(magnitudeTest, Angle.Degree(-angleStraightDegree + angleDegreeTest))
        assert(vector2D.x === -xTest)
        assert(vector2D.y === -yTest)
      }

      it("should be created from a magnitude and a direction and have their values 4° quadrant") {
        val vector2D = Vector2D(magnitudeTest, Angle.Degree(-angleDegreeTest))
        assert(vector2D.x === xTest)
        assert(vector2D.y === -yTest)
      }

      it("shouldn't be created from a null direction") {
        val vector2D = Vector2D(magnitudeTest, null)
        assert(vector2D == Vector2D.Zero)
      }

      it("should have a magnitude") {
        val vector2D = Vector2D(xTest, yTest)
        assert(vector2D.magnitude === magnitudeTest)
      }

      it("should have a direction 1° quadrant") {
        val vector2D = Vector2D(xTest, yTest)
        assert(vector2D.direction.get.degree === angleDegreeTest)
      }

      it("should have a direction 2° quadrant") {
        val vector2D = Vector2D(-xTest, yTest)
        assert(vector2D.direction.get.degree === angleStraightDegree - angleDegreeTest)
      }

      it("should have a direction 3° quadrant") {
        val vector2D = Vector2D(-xTest, -yTest)
        assert(vector2D.direction.get.degree === -angleStraightDegree + angleDegreeTest)
      }

      it("should have a direction 4° quadrant") {
        val vector2D = Vector2D(xTest, -yTest)
        assert(vector2D.direction.get.degree === -angleDegreeTest)
      }

      it("should be able to add another vector") {
        val vector2D = Vector2D(xTest, yTest)
        val otherVector2D = Vector2D(2, 1)
        val expectedVector = Vector2D(xTest + 2, yTest + 1)
        val vector2DSum = vector2D + otherVector2D
        assert(vector2DSum === expectedVector)
      }

      it("should be able to subtract another vector") {
        val vector2D = Vector2D(4, 3)
        val otherVector2D = Vector2D(2, 1)
        val expectedVector = Vector2D(2, 2)
        val vector2DSum = vector2D - otherVector2D
        assert(vector2DSum === expectedVector)
      }

      it("should be able to multiply by a scalar") {
        val vector2D = Vector2D(4, 3)
        val expectedVector = Vector2D(8, 6)
        val vector2DProduct = vector2D * 2
        assert(vector2DProduct === expectedVector)
      }

      it("should be able to divide by a scalar") {
        val vector2D = Vector2D(4, 8)
        val expectedVector = Vector2D(2, 4)
        val vector2DProduct = vector2D / 2
        assert(vector2DProduct === expectedVector)
      }

      it("should be able to calculate his normalized vector") {
        val rad2 = math.sqrt(2) / 2
        val vector2D = Vector2D(4, 4)
        val normalizedVector2D = vector2D.normalize
        assert(normalizedVector2D === Vector2D(rad2, rad2))
      }

      it("opposite should have the same magnitude but opposite direction") {
        val vector2D = Vector2D(xTest, yTest)
        val expectedOpposite = Vector2D(-xTest, -yTest)
        val oppositeVector2D = -vector2D
        val oppositeAngle = Angle.Degree(vector2D.direction.get.degree + 180)
        assert(oppositeVector2D === expectedOpposite)
        assert(oppositeVector2D.magnitude === vector2D.magnitude)
        assert(oppositeVector2D.direction.get.degree === oppositeAngle.degree)
      }

      it("opposite should have the same magnitude but opposite direction angle 180°") {
        val vector2D = Vector2D(-xTest, 0)
        val expectedOpposite = Vector2D(xTest, 0)
        val oppositeVector2D = -vector2D
        assert(oppositeVector2D === expectedOpposite)
        assert(oppositeVector2D.magnitude === vector2D.magnitude)
        assert(oppositeVector2D.direction.get.degree === 0.0)
      }

      it("opposite should have the same magnitude but opposite direction angle 0°") {
        val vector2D = Vector2D(xTest, 0)
        val expectedOpposite = Vector2D(-xTest, 0)
        val oppositeVector2D = -vector2D
        assert(oppositeVector2D === expectedOpposite)
        assert(oppositeVector2D.magnitude === vector2D.magnitude)
        assert(oppositeVector2D.direction.get.degree === 180.0)
      }

      it("must return Zero vector if subtracting itself") {
        val vector2D = Vector2D(xTest, yTest)
        val vector2DSub = vector2D - vector2D
        assert(vector2DSub === Vector2D.Zero)
      }

      it("must return Zero vector if added its opposite") {
        val vector2D = Vector2D(xTest, yTest)
        val vector2DSum = vector2D + (-vector2D)
        assert(vector2DSum === Vector2D.Zero)
      }
    }

    describe("when is a zero vector") {
      it("should have magnitude equal to zero") {
        val vector2D = Vector2D.Zero
        assert(vector2D.magnitude == 0)
      }

      it("shouldn't have direction, because it's undefined") {
        val vector2D = Vector2D.Zero
        assert(vector2D.direction.isEmpty)
      }

      it("should have x y coordinate both equal to 0") {
        val vector2D = Vector2D.Zero
        assert(vector2D.x === 0.0)
        assert(vector2D.y === 0.0)
      }

      it("should be created from a x and y coordinate with both values equal to 0") {
        val vector2D = Vector2D(0, 0)
        assert(vector2D == Vector2D.Zero)
      }

      it("should be created from a magnitude that is 0") {
        val vector2D = Vector2D(0, Angle.Degree(angleDegreeTest))
        assert(vector2D == Vector2D.Zero)
      }

      it("shouldn't be created from a angle 0° and a magnitude != 0") {
        val vector2D = Vector2D(magnitudeTest, Angle.Degree(0))
        assert(vector2D != Vector2D.Zero)
      }

      it("should be created from a angle with value null") {
        val vector2D = Vector2D(magnitudeTest, null)
        assert(vector2D == Vector2D.Zero)
      }

      it("should be able to add another vector and return the other vector") {
        val vector2D = Vector2D.Zero
        val otherVector2D = Vector2D(magnitudeTest, Angle.Degree(angleDegreeTest))
        val vector2DSum = vector2D + otherVector2D
        assert(vector2DSum === Vector2D(magnitudeTest, Angle.Degree(angleDegreeTest)))
      }

      it("should be able to subtract another vector and return the opposite of the other vector") {
        val straightAngleDegree = 180
        val vector2D = Vector2D.Zero
        val otherVector2D = Vector2D(magnitudeTest, Angle.Degree(angleDegreeTest))
        val vector2DSum = vector2D - otherVector2D
        assert(vector2DSum === Vector2D(magnitudeTest, Angle.Degree(-straightAngleDegree + angleDegreeTest)))
      }

      it("should be able to multiply by a scalar") {
        val vector2D = Vector2D.Zero
        val vector2DProduct = vector2D * 2
        assert(vector2DProduct === Vector2D.Zero)
      }

      it("should be able to divide by a scalar") {
        val vector2D = Vector2D.Zero
        val vector2DProduct = vector2D / 2
        assert(vector2DProduct === Vector2D.Zero)
      }

      it("should be able to calculate his normalized vector (Zero)") {
        val vector2D = Vector2D.Zero
        val normalizedVector2D = vector2D.normalize
        assert(normalizedVector2D === Vector2D.Zero)
      }

      it("opposite should be Zero") {
        val vector2D = Vector2D.Zero
        assert(-vector2D === Vector2D.Zero)
      }
    }

    it("shouldn't be equal to a Point2D") {
      val point = Point2D(xTest, yTest)
      val vector = Vector2D(xTest, yTest)
      assert(vector !== point)
    }
  }
