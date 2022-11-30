package controller

import components.ModelModule.Model
import components.ViewModule.View
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.{Observable, OverflowStrategy}
import model.Game
import controller.update.*

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/**
 * Case class that represents the controller of the game.
 */
case class GameController(view: View, model: Model):

  private def attachListenerToViewEvents(): Unit =
    view.events
      .doOnNext {
      case Event.StartGame => gameLoop()
      case _ => Task.unit
    }
      .completedL
      .runAsyncAndForget

  private def gameLoop(): Task[Unit] =
    given OverflowStrategy[Event] = OverflowStrategy.Default

    val time: Observable[Event] = TimeFlow
      .tickEach(50 milliseconds)
      .map(_.toDouble / 1000)
      .map(Event.TimePassed.apply)

    val game = model.initializeGame()
    val controls: Update = Update.combine(
      UpdateTime(),
      UpdatePosition(),
      ActivateSpecialAbility(),
      CollisionsDetection(),
      LaunchNewMissile()
    )

    val init = Task((game, controls))
    val events =
      Observable(time, view.events).merge
    events
      .scanEval(init) { case ((game, controls), event) => controls(event, game) }
      .doOnNext { case (game, _) => view.render(game) }
      .takeWhile { case (game, _) => game.world.ground.stillAlive }
      .last
      .doOnNext { case (game, _) => view.gameOver(game) }
      .completedL


  /**
   * Attach the listener to the view events and start the game.
   *
   * @return a task that will execute the game.
   */
  def start(): Task[Unit] =
    attachListenerToViewEvents()
    view.initialGame()
