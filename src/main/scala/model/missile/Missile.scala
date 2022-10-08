package model.missile

import model.elements2d.{Angle, Point2D, Vector2D}

trait Missile:

  def damage: Int

  def position: Point2D

  def direction: Vector2D
  
  def velocity: Double

  def dt: Double

  def finalPosition: Point2D

object Missile:

  def apply(myDamage: Int, myVelocity: Double, myPosition: Point2D, myDirection: Vector2D, deltatime: Double, myFinalPosition: Point2D) : Missile = new Missile:
    override def damage: Int = myDamage

    override def position: Point2D = myPosition

    override def direction: Vector2D = myDirection

    override def velocity: Double = myVelocity

    override def dt: Double = deltatime

    override def finalPosition: Point2D = myFinalPosition

    def move: Missile = apply(damage, myVelocity, position --> (direction * velocity * dt), myDirection, deltatime, finalPosition)

    //il missile deve creare un esplosione?


//case class BasicMissile(damage: Int, angle: Angle, position: Point2D, velocity: Vector2D, dt: Double)

/*
object NormalMissile:
  //extension method per aggiungere la move strategy???
  extension (missile: BasicMissile)
    def move(): BasicMissile = BasicMissile(missile.damage, missile.angle,
      missile.position, missile.velocity, missile.dt)
*/