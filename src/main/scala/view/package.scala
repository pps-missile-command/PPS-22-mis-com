import model.World
import model.elements2d.Point2D

package object view:
  /**
   * Conversion used for converting a Double to Int
   */
  given Conversion[Double, Int] with
    override def apply(x: Double): Int = x.toInt

  /**
   * Extension of class Int, allowing it to apply a function to the value
   */
  extension(i: Int)
    def use(f: (Int) => Int) = f(i)

  val ratioWidth: Double = ViewConstants.GUI_width.toDouble / World.width.toDouble
  val ratioHeight: Double = ViewConstants.GUI_height.toDouble / World.height.toDouble
  
  /**
   * Lambda value that return a gui mapped Point2D given a logical one
   */
  private val convertPosition: (Point2D) => Point2D = (p) => Point2D(p.x * ratioWidth, p.y * ratioHeight)
  /**
   * Lamda value that return a value converted with the ratio of the width
   */
  private val convertWidth: (Int) => Int = (i) => i * ratioWidth
  /**
   * Lamda value that return a value converted with the ratio of the height
   */
  private val convertHeight: (Int) => Int = (i) => i * ratioHeight