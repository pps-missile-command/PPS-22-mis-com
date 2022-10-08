package model.explosion

import model.elements2d.Point2D

/**
 * esplosione aerea e terrestre: ci sono differenze???
 */

trait Explosion:

  def position: Point2D

  def radius: Int



object Explosion:



  def apply(): Explosion = ???

