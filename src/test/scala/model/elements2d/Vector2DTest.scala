package model.elements2d

import org.scalatest.funspec.AnyFunSpec
import org.scalactic.{Equality, TolerantNumerics}

class Vector2DTest extends AnyFunSpec :
  private implicit val doubleEquality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.01)
  private implicit val vectorEquality: Equality[Vector2D] = (vector: Vector2D, other: Any) => other match
    case otherVector: Vector2D => vector.x === otherVector.x && vector.y === otherVector.y
    case _ => false

  describe("A Vector 2D") {
    describe("when is a vector") {
      it("should be created from a x y coordinate and have their values") {
        val vector2D = Vector2D(4, 3)
        assert(vector2D.x === 4.0)
        assert(vector2D.y === 3.0)
      }

      it("should be created from magnitude and a direction and have their values") {
        val vector2D = Vector2D(5, Angle.Degree(36.86))
        assert(vector2D.x === 4.0)
        assert(vector2D.y === 3.0)
      }

      it("should have a magnitude") {
        val vector2D = Vector2D(4, 3)
        assert(vector2D.magnitude === 5.0)
      }
      it("should have a direction") {
        val vector2D = Vector2D(4, 3)
        assert(vector2D.direction.degree === 36.86)
      }

      it("should be able to add another vector") {
        val vector2D = Vector2D(4, 3)
        val otherVector2D = Vector2D(2, 1)
        val vector2DSum = vector2D + otherVector2D
        assert(vector2DSum === Vector2D(6, 4))
      }

      it("should be able to subtract another vector") {
        val vector2D = Vector2D(4, 3)
        val otherVector2D = Vector2D(2, 1)
        val vector2DSum = vector2D - otherVector2D
        assert(vector2DSum === Vector2D(2, 2))
      }

      it("should be able to multiply by a scalar") {
        val vector2D = Vector2D(4, 3)
        val vector2DProduct = vector2D * 2
        assert(vector2DProduct === Vector2D(8, 6))
      }

      it("should be able to divide by a scalar") {
        val vector2D = Vector2D(4, 8)
        val vector2DProduct = vector2D / 2
        assert(vector2DProduct === Vector2D(2, 4))
      }

      it("should be able to calculate his normalized vector") {
        val rad2 = math.sqrt(2) / 2
        val vector2D = Vector2D(4, 4)
        val normalizedVector2D = vector2D.normalize
        assert(normalizedVector2D === Vector2D(rad2, rad2))
      }
    }

    describe("when is a zero vector") {
      it("should have magnitude equal to zero") {
        val vector2D = Vector2D.Zero
        assert(vector2D.magnitude == 0)
      }

      it("should have direction equal to zero") {
        val vector2D = Vector2D.Zero
        assert(vector2D.direction.degree == 0)
      }

      it("should have x y coordinate both equal to 0") {
        val vector2D = Vector2D.Zero
        assert(vector2D.x === 0.0)
        assert(vector2D.y === 0.0)
      }

      it("should be created from a x y coordinate with both values equal to 0") {
        val vector2D = Vector2D(0, 0)
        assert(vector2D == Vector2D.Zero)
      }

      it("should be created from a magnitude is 0") {
        val vector2D = Vector2D(0, Angle.Degree(10))
        assert(vector2D == Vector2D.Zero)
      }

      it("shouldn't be created from a angle 0° and a magnitude != 0") {
        val vector2D = Vector2D(1, Angle.Degree(0))
        assert(vector2D != Vector2D.Zero)
      }

      it("should be able to add another vector and return the other vector") {
        val vector2D = Vector2D.Zero
        val otherVector2D = Vector2D(2, Angle.Degree(36.86))
        val vector2DSum = vector2D + otherVector2D
        assert(vector2DSum === Vector2D(2, Angle.Degree(36.86)))
      }

      it("should be able to subtract another vector and return the opposite of the other vector") {
        val vector2D = Vector2D.Zero
        val otherVector2D = Vector2D(2, Angle.Degree(36.86))
        val vector2DSum = vector2D - otherVector2D
        assert(vector2DSum === Vector2D(2, Angle.Degree(-180 + 36.86)))
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

      it("should be able to calculate his normalized vector") {
        val vector2D = Vector2D.Zero
        val normalizedVector2D = vector2D.normalize
        assert(normalizedVector2D === Vector2D.Zero)
      }
    }
  }
