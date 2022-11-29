package components

import view.gui.UI

object MVC
  extends ModelModule.Interface
  with ViewModule.Interface
  with ControllerModule.Interface:

  override val model: ModelModule.Model = new ModelImpl()
  override val view: UI & ViewModule.View = new ViewImpl()
  override val controller: ControllerModule.MainController = new ControllerImpl()

  @main def main(): Unit =
    controller.initializeGame()
