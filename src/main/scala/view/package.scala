import model.World

package object view:
  given Conversion[Double, Int] with
    override def apply(x: Double): Int = x.toInt

  val viewMapper: (Double) => Double = _ * (ViewConstants.GUI_width / model.World.width)
  val ratioWidth: Double = ViewConstants.GUI_width / World.width
  val ratioHeight: Double = ViewConstants.GUI_height / World.height
