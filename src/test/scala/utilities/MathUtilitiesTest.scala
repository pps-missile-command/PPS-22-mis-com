package utilities

import org.scalatest.funspec.AnyFunSpec
import org.scalactic.TripleEquals.*
import MathUtilities.*
import MathUtilities.DoubleEquality.given
import org.scalactic.Tolerance.convertNumericToPlusOrMinusWrapper
import org.scalactic.{Equality, TolerantNumerics}

class MathUtilitiesTest extends AnyFunSpec :

  private val value = 1.0

  describe("The operator <==") {

    describe("with the given tolerance") {

      val tolerance = 0.01
      val difference = 0.1

      given Equality[Double] = TolerantNumerics.tolerantDoubleEquality(tolerance)

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.01 (default)") {
        val comparedValue = value + difference
        assert(value <== comparedValue)
        assert(value !== comparedValue)
      }

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.01 (in tolerance)") {
        val comparedValue = value + tolerance
        assert(value <== comparedValue)
        assert(value === comparedValue)
      }

      it("should return false if the first number is greater than the second number but out side of a tolerance of 0.01") {
        val comparedValue = value - difference
        assert(!(value <== comparedValue))
        assert(value !== comparedValue)
      }

      it("should return true if the first number is greater than the second number but in a tolerance of 0.01") {
        val comparedValue = value - tolerance
        assert(value <== comparedValue)
        assert(value === comparedValue)
      }
    }

    describe("with the explicit tolerance") {

      val tolerance = 0.2
      val difference = 0.1

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.2 (default)") {
        val comparedValue = value + difference + tolerance
        assert(value <== comparedValue +- tolerance)
        assert(value !== comparedValue)
      }

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.2 (in tolerance)") {
        val comparedValue = value + tolerance
        assert(value <== comparedValue +- tolerance)
        assert(value === comparedValue +- tolerance)
      }

      it("should return false if the first number is greater than the second number but out side of a tolerance of 0.2") {
        val comparedValue = value - tolerance - difference
        assert(!(value <== comparedValue +- tolerance))
        assert(value !== comparedValue +- tolerance)
      }

      it("should return true if the first number is greater than the second number but in a tolerance of 0.2") {
        val comparedValue = value - tolerance
        assert(value <== comparedValue +- tolerance)
        assert(value === comparedValue +- tolerance)
      }

      it("should return false if the second element is not a double") {
        val comparedValue = "test"
        assert(!(value <== comparedValue))
      }
    }
  }

  describe("The operator >==") {

    describe("with the given tolerance") {

      val tolerance = 0.01
      val difference = 0.1

      given Equality[Double] = TolerantNumerics.tolerantDoubleEquality(tolerance)

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.01 (default)") {
        val comparedValue = value + difference
        assert(comparedValue >== value)
        assert(value !== comparedValue)
      }

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.01 (in tolerance)") {
        val comparedValue = value + tolerance
        assert(comparedValue >== value)
        assert(value === comparedValue)
      }

      it("should return false if the first number is greater than the second number but out side of a tolerance of 0.01") {
        val comparedValue = value - difference
        assert(!(comparedValue >== value))
        assert(value !== comparedValue)
      }

      it("should return true if the first number is greater than the second number but in a tolerance of 0.01") {
        val comparedValue = value - tolerance
        assert(comparedValue >== value)
        assert(value === comparedValue)
      }

      it("should return false if the second element is not a double") {
        val comparedValue = "test"
        assert(!(value >== comparedValue))
      }
    }

    describe("with the explicit tolerance") {

      val tolerance = 0.2
      val difference = 0.1

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.2 (default)") {
        val comparedValue = value + tolerance + difference
        assert(comparedValue >== value +- tolerance)
        assert(value !== comparedValue +- tolerance)
      }

      it("should return true if the first number is less than or equal to the second number with a tolerance of 0.2 (in tolerance)") {
        val comparedValue = value + tolerance
        assert(comparedValue >== value +- tolerance)
        assert(value === comparedValue +- tolerance)
      }

      it("should return false if the first number is greater than the second number but out side of a tolerance of 0.2") {
        val comparedValue = value - tolerance - difference
        assert(!(comparedValue >== value +- tolerance))
        assert(value !== comparedValue +- tolerance)
      }

      it("should return true if the first number is greater than the second number but in a tolerance of 0.2") {
        val comparedValue = value - tolerance
        assert(comparedValue >== value +- tolerance)
        assert(value === comparedValue +- tolerance)
      }
    }
  }

  describe("The operator **") {

    val base = 2.0

    it("should return the square of a number") {
      val expected = 4.0
      val exponent = 2.0
      assert(expected === base ** exponent)
    }

    it("should return the cube of a number") {
      val expected = 8.0
      val exponent = 3.0
      assert(expected === base ** exponent)
    }

    it("should return the square root of a number") {
      val expected = math.sqrt(base)
      val exponent = 0.5
      assert(expected === base ** exponent)
    }
  }

