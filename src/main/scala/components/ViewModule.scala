package components

import controller.Event
import model.Game
import monix.eval.Task
import monix.reactive.Observable
import view.ViewConstants
import view.gui.{GUI, MainPane, UI}

object ViewModule:
  trait View:
    def initialGame(): Unit
  trait Provider:
    val view: UI & View
  type Requirements = ControllerModule.Provider
  trait Component:
    context: Requirements =>

    class ViewImpl extends UI with View:

      private lazy val gui: GUI = new GUI(ViewConstants.GUI_width, ViewConstants.GUI_height + 39)

      override def initialGame(): Unit = new MainPane(() => controller.startGame(), ViewConstants.GUI_width, ViewConstants.GUI_height)

      override def render(game: Game): Task[Unit] = gui.render(game)

      override def gameOver(game: Game): Task[Unit] = gui.gameOver(game)

      override def events: Observable[Event] = gui.events
  trait Interface extends Provider with Component:
    self: Requirements =>