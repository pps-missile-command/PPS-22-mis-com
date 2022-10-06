package utilities

import org.scalactic.TripleEqualsSupport.Spread
import org.scalactic.{Equality, TolerantNumerics}
import math.Numeric.Implicits.infixNumericOps
import scala.annotation.targetName
import org.scalactic.TripleEquals.Equalizer
import scala.math.pow

/**
 * The Object that provides the extension methods `>==`, `<==` and `**`.
 * It also provides the given `Equality` that is used to have a tolerance with the double.
 */
object MathUtilities:

  /**
   * Object that contains the givens for the Double for class equality (===).
   */
  object DoubleEquality:
    /**
     * The given for the equality of two Double.
     * The tolerance is 0.01.
     *
     * @return the given for the equality of two Double with tolerance 0.01
     */
    given Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.01)


  extension[T: Numeric] (value: T)

    /**
     * Returns the value of the specified number raised to the power of exponent.
     *
     * @tparam exponent the exponent
     * @return the value of the specified number raised to the power of exponent
     */
    @targetName("pow")
    def **(exponent: T): Double = pow(value.toDouble, exponent.toDouble)


  extension (leftSide: Equalizer[Double])

    /**
     * Returns true if the left side is less than or equal the right side in the given tolerance.
     *
     * @param rightSide the right side
     * @return true if the left side is less than or equal the right side in the given tolerance
     */
    @targetName("lessEqual")
    def <==(rightSide: Any)(implicit equality: Equality[Double]): Boolean = rightSide match
      case double: Double => leftSide === double || leftSide.leftSide < double
      case _ => false

    /**
     * Returns true if the left side is greater than or equal the right side in the given tolerance.
     *
     * @param rightSide the right side
     * @return true if the left side is greater than or equal the right side in the given tolerance
     */
    @targetName("greaterEqual")
    def >==(rightSide: Any)(implicit equality: Equality[Double]): Boolean = rightSide match
      case double: Double => leftSide === double || leftSide.leftSide > double
      case _ => false

    /**
     * Returns true if the left side is less than the right side in the tolerance.
     *
     * @param spread the right side and the tolerance
     * @return true if the left side is less than or equal the right side in the tolerance
     */
    @targetName("lessEqual")
    def <==(spread: Spread[Double]): Boolean = leftSide === spread || leftSide.leftSide < spread.pivot

    /**
     * Returns true if the left side is greater than the right side in the tolerance.
     *
     * @param spread the right side and the tolerance
     * @return true if the left side is greater than or equal the right side in the tolerance
     */
    @targetName("greaterEqual")
    def >==(spread: Spread[Double]): Boolean = leftSide === spread || leftSide.leftSide > spread.pivot
