package controller.update

import monix.eval.Task
import monix.reactive.Observable

import scala.annotation.tailrec
import scala.reflect.ClassTag
import controller.Event
import model.World

/**
 * A trait for function from pair (Event, World) to [[Task]] World, Update.
 * Used to update the world.
 */
trait Update extends ((Event, World) => Task[(World, Update)]) :
  /**
   * Allows to update the world to be computed after this one.
   *
   * @param control the update to be computed after this one.
   * @return a new update with the world and the sequence of the two update.
   */
  def andThen(control: Update): Update = (event: Event, world: World) =>
    this (event, world)
      .flatMap { case (world, left) => control(event, world).map { case (world, right) => (left, right, world) } }
      .map { case (left, right, world) => (world, Update.combineTwo(left, right)) }

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
  def on[E <: Event](control: (E, World) => Task[World])(using ev: ClassTag[E]): Update =
    lazy val result: Update = (event: Event, world: World) =>
      event match
        case event: E => (control(event, world).map(world => (world, result)))
        case _ => Task((world, result))
    result

  /**
   * Function that allow two [[Update]] to be executed in sequence.
   *
   * @param engineA the first [[Update]] to be executed.
   * @param engineB the second [[Update]] to be executed.
   * @return The [[Update]] that will execute the two [[Update]] in sequence.
   */
  def combineTwo(engineA: Update, engineB: Update): Update = (event: Event, world: World) =>
    for
      updateA <- engineA.apply(event, world)
      (newWorld, newEngineA) = updateA
      updateB <- engineB(event, newWorld)
      (lastWorld, newEngineB) = updateB
    yield (lastWorld, combineTwo(newEngineA, newEngineB))

  /**
   * Function that allow a sequence of [[Update]] to be executed in sequence.
   * @param engines the sequence of [[Update]] to be executed.
   * @return The [[Update]] that will execute the sequence of [[Update]] in sequence.
   */
  def combine(engines: Update*): Update = engines.reduce(_.andThen(_))
