package view.gui

import controller.Event
import model.World
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
     * @param world the world to render
     * @return a task that will render the world
     */
    def render(world: World): Task[Unit]

    /**
     * Render the end of the game.
     *
     * @return a task that will render the end of the game
     */
    def gameOver(world: World): Task[Unit]

