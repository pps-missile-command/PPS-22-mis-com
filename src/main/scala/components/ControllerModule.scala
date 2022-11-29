package components
import monix.execution.Scheduler.Implicits.global
import monix.reactive.{Observable, OverflowStrategy}
import controller.Controller

object ControllerModule:
  trait MainController:
    def startGame(): Unit
    def initializeGame(): Unit
  trait Provider:
    val controller: MainController
  type Requirements = ViewModule.Provider with ModelModule.Provider
  trait Component:
    context: Requirements =>
    class ControllerImpl extends MainController:

      override def startGame(): Unit = Controller.start(view).runAsyncAndForget

      override def initializeGame(): Unit = view.initialGame()

  trait Interface extends Provider with Component:
    self: Requirements =>