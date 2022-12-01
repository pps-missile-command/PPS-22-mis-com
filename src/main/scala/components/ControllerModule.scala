package components
import monix.execution.Scheduler.Implicits.global
import monix.reactive.{Observable, OverflowStrategy}
import controller.GameController

object ControllerModule:
  trait Controller:
    /**
     * Method that starts the game
     */
    def startGame(): Unit
    
  trait Provider:
    val controller: Controller
    
  type Requirements = ViewModule.Provider with ModelModule.Provider
  
  trait Component:
    context: Requirements =>
    
    class ControllerImpl extends Controller:
      override def startGame(): Unit =
        GameController(view, model).start().runAsyncAndForget
  
  trait Interface extends Provider with Component:
    self: Requirements =>