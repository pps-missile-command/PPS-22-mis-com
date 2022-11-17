package model.spawner

import model.elements2d.Point2D

import scala.util.Random

object LazySpawner:

  trait Spawnable[+A]:
    def generate(): A
  
  def apply[A](spawnable: Spawnable[A]): LazyList[A] = LazyList.continually(spawnable.generate())