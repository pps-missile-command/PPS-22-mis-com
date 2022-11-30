package components

import controller.Event
import model.Game
import monix.eval.Task
import monix.reactive.Observable
import view.ViewConstants
import view.gui.GUI

object ViewModule:
  trait View:
    /**
     * Create the initial view
     *
     * @return a task that will create the view
     */
    def initialGame(): Task[Unit]

    /**
     * The event source produced by this UI.
     *
     * @return an observable of events
     */
    def events: Observable[Event]

    /**
     * Render the world representation.
     *
     * @param game the game to render
     * @return a task that will render the world
     */
    def render(game: Game): Task[Unit]

    /**
     * Render the end of the game.
     *
     * @param game the game to render at the end
     * @return a task that will render the end of the game
     */
    def gameOver(game: Game): Task[Unit]

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>

    class ViewImpl extends View :
      private lazy val gui: View = new GUI(ViewConstants.GUI_width, ViewConstants.GUI_height + 39)

      override def initialGame(): Task[Unit] = gui.initialGame()

      override def render(game: Game): Task[Unit] = gui.render(game)

      override def gameOver(game: Game): Task[Unit] = gui.gameOver(game)

      override def events: Observable[Event] = gui.events

  trait Interface extends Provider with Component :
    self: Requirements =>