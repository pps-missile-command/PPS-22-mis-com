package controller

import monix.eval.Task
import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.PublishSubject
import monix.reactive.{Observable, Observer, OverflowStrategy}
import org.reactivestreams.Subscriber
import view.gui.UI
import model.Game
import controller.update._

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.Random

/**
 * Object that represents the controller of the game.
 */
object Controller:
  /**
   * The observable that will be used to schedule time in the game.
   */
  private val time: Observable[Event] = TimeFlow
    .tickEach(50 milliseconds)
    .map(_.toDouble / 1000)
    .map(Event.TimePassed.apply)

  /**
   * start the game loop.
   *
   * @param ui the ui that will be used to show the game.
   * @return a task that will execute the game.
   */
  def start(ui: UI): Task[Unit] =

    given OverflowStrategy[Event] = OverflowStrategy.Default

    val game = Game.initialGame
    val controls: Update = Update.combine(
      UpdateTime(),
      UpdatePosition(),
      ActivateSpecialAbility(),
      CollisionsDetection(),
      LaunchNewMissile()
    )

    val init = Task((game, controls))
    val events =
      Observable(time, ui.events).merge
    events
      .scanEval(init) { case ((game, controls), event) => controls(event, game) }
      .doOnNext { case (game, _) => ui.render(game) }
      .takeWhile { case (game, _) => game.world.ground.stillAlive }
      .last
      .doOnNext { case (game, _) => ui.gameOver(game) }
      .completedL
