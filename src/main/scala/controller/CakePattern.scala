package controller

import controller.Controller.time
import controller.update.{ActivateSpecialAbility, CollisionsDetection, LaunchNewMissile, Update, UpdatePosition, UpdateTime}
import model.{Game, World}
import monix.eval.Task
import view.ViewConstants
import view.gui.{GUI, UI}
import monix.execution.Scheduler.Implicits.global
import monix.reactive.{Observable, OverflowStrategy}

import scala.language.postfixOps
import scala.concurrent.duration.DurationInt

object ModelModule:
  trait Model:
    def initializeGame(): Game
  trait Provider:
    val model: Model
  trait Component:
    class ModelImpl extends Model:
      override def initializeGame(): Game = Game.initialGame
  trait Interface extends Provider with Component

object ViewModule:
  trait Provider:
    val view: UI
  type Requirements = ControllerModule.Provider
  trait Component:
    context: Requirements =>
    class ViewImpl extends UI:

      private val gui: GUI = new GUI(ViewConstants.GUI_width, ViewConstants.GUI_height + 39)

      override def render(game: Game): Task[Unit] = gui.render(game)

      override def gameOver(game: Game): Task[Unit] = gui.gameOver(game)

      override def events: Observable[Event] = gui.events
  trait Interface extends Provider with Component:
    self: Requirements =>

object ControllerModule:
  trait MainController:
    def startGame(): Unit
  trait Provider:
    val controller: MainController
  type Requirements = ViewModule.Provider with ModelModule.Provider
  trait Component:
    context: Requirements =>
    class ControllerImpl extends MainController:

      override def startGame(): Unit = Controller.start(view).runAsyncAndForget

  trait Interface extends Provider with Component:
    self: Requirements =>


object MVC
  extends ModelModule.Interface
  with ViewModule.Interface
  with ControllerModule.Interface:

  override val model: ModelModule.Model = new ModelImpl()
  override val view: UI = new ViewImpl()
  override val controller: ControllerModule.MainController = new ControllerImpl()

  @main def main(): Unit =
    controller.startGame()
