package controller.update

import monix.eval.Task
import monix.reactive.Observable

import scala.annotation.tailrec
import scala.reflect.ClassTag
import controller.Event
import model.Game

/**
 * A trait for function from pair (Event, World) to [[Game]] World, Update.
 * Used to update the world.
 */
trait Update extends ((Event, Game) => Task[(Game, Update)]):
  /**
   * Allows to update the world to be computed after this one.
   *
   * @param control the update to be computed after this one.
   * @return a new update with the world and the sequence of the two update.
   */
  private def andThen(control: Update): Update = (event: Event, game: Game) =>
    this (event, game)
      .flatMap {
        case (game, left) => control(event, game)
          .map {
            case (game, right) => (left, right, game)
          }
      }
      .map { case (left, right, game) => (game, Update.combineTwo(left, right)) }

/**
 * Companion object for [[Update]].
 */
object Update:

  /**
   * Function that allow the instruction to be executed only if the [[Event]] E happened.
   *
   * @param control the update to be executed.
   * @param ev      the [[Event]] that must happen.
   * @tparam E the type of the [[Event]].
   * @return the update that will be executed only if the [[Event]] E happened.
   */
  def on[E <: Event](control: (E, Game) => Task[Game])(using ev: ClassTag[E]): Update =
    lazy val result: Update = (event: Event, game: Game) =>
      event match
        case event: E => control(event, game).map(game => (game, result))
        case _ => Task((game, result))
    result

  /**
   * Function that allow two [[Update]] to be executed in sequence.
   *
   * @param engineA the first [[Update]] to be executed.
   * @param engineB the second [[Update]] to be executed.
   * @return The [[Update]] that will execute the two [[Update]] in sequence.
   */
  private def combineTwo(engineA: Update, engineB: Update): Update = (event: Event, game: Game) =>
    for
      updateA <- engineA.apply(event, game)
      (newGame, newEngineA) = updateA
      updateB <- engineB(event, newGame)
      (lastGame, newEngineB) = updateB
    yield (lastGame, combineTwo(newEngineA, newEngineB))

  /**
   * Function that allow a sequence of [[Update]] to be executed in sequence.
   *
   * @param engines the sequence of [[Update]] to be executed.
   * @return The [[Update]] that will execute the sequence of [[Update]] in sequence.
   */
  def combine(engines: Update*): Update = engines.reduce(_.andThen(_))
