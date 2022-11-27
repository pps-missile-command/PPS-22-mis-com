package model.spawner

import model.elements2d.Point2D

import scala.util.Random

/**
 * Module for a spawnable object: it is a simple object capable to define a way to generate a
 * specified type of elements, used by [[GenericSpawner]] to spawn elements over time
 */
object Spawnable:
  /**
   * A generic spawnable, that is a generator of a specified type of entities
   * @tparam A The type of element to generate
   */
  trait Spawnable[+A]:
    /**
     * Generator method that creates a new A-typed element
     * @return the new element created
     */
    def generate(): A

  /**
   * Factory method to generate a set of n elements from the given spawnable
   * @param spawnable The spawnable that dictates how to generate the elements
   * @param n The number of elements to generate
   * @tparam A The type of elements to generate
   * @return the set of generated elements
   */
  def apply[A](spawnable: Spawnable[A])(n: Int): Set[A] =
    (for
      i <- 0 until n
    yield spawnable.generate()).toSet