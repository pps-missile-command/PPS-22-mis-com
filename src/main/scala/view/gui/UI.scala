package view.gui

import controller.Event
import model.Game
import monix.eval.Task
import monix.reactive.Observable

/**
 * Trait that represents a view of the game.
 */
trait UI:
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
