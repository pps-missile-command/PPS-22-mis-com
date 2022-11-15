package object view:
  
  val viewMapper: (Double) => Double = _ * (ViewConstants.GUI_width / model.World.width)
