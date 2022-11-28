import model.World
import model.elements2d.Point2D

package object view:
  given Conversion[Double, Int] with
    override def apply(x: Double): Int = x.toInt

  extension(i: Int)
    def use(f: (Int) => Int) = f(i)

  val ratioWidth: Double = ViewConstants.GUI_width.toDouble / World.width.toDouble
  val ratioHeight: Double = ViewConstants.GUI_height.toDouble / World.height.toDouble
  
  /**
   * Lambda value that return a gui mapped Point2D given a logical one
   */
  private val convertPosition: (Point2D) => Point2D = (p) => Point2D(p.x * ratioWidth, p.y * ratioHeight)

  private val convertWidth: (Int) => Int = (i) => (i * ViewConstants.GUI_width) / World.width
  private val convertHeight: (Int) => Int = (i) => (i * ViewConstants.GUI_height) / World.height