package model.vehicle

import model.World.{height, width}
import model.{DeltaTime, World}
import model.behavior.Moveable
import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Damageable, HitBox, LifePoint, lifePointDeath}
import model.elements2d.{Point2D, Vector2D}
import model.explosion.{Explosion, standardRadius}
import model.missile.{Missile, given_Conversion_Point2D_Point2D_Vector2D, hitboxBase, hitboxHeight}
import model.spawner.GenericSpawnerImpl
import model.spawner.{GenericSpawner, GenericSpawnerImpl, SpecificSpawners}

import scala.util.Random


trait Plane extends Moveable, Damageable, GenericSpawner[Missile]:
    def position: Point2D
    def velocity: Double
    def direction: Vector2D = (position, destination)
    def planeDirection: vehicleTypes
    override def move(): Plane
    override def timeElapsed(dt: DeltaTime): Plane

case class PlaneImpl(actualPosition: Point2D,
                     finalPosition: Point2D,
                     choosedDirection: vehicleTypes,
                     lifePoint: LifePoint = planeInitialLife,
                     deltaTime: DeltaTime = 0,
                     missileSpawnerOpt: Option[GenericSpawnerImpl[Missile]] = Option.empty)(using Random)
                    extends Plane:

    val spawnable = SpecificSpawners.FixedMissileStrategy(width, height, position)

    val missileSpawner = missileSpawnerOpt match
        case Some(value) => value.changeSpawnable(spawnable)
        case _ => GenericSpawner(3, spawnable)

    override def planeDirection: vehicleTypes = choosedDirection
    override def position: Point2D = actualPosition
    override def velocity: Double = planeVelocity
    override def move(): Plane = this match
        case v if(v.isDestroyed) => this.copy(lifePoint = lifePointDeath)
        case _ => this.copy(actualPosition = moveVehicle(this), deltaTime = 0)

    /***
     * Calculate the new position of the vehicle
     * @param vehicle Vehicle that have to be moved
     * @return New position after moved time
     */
    def moveVehicle(vehicle: Plane): Point2D =
        val distanceToMove = vehicle.velocity * deltaTime
        val distanceToFinalPosition = vehicle.position <-> vehicle.destination
        if distanceToMove >= distanceToFinalPosition then
            vehicle.destination
        else
            vehicle.position --> (vehicle.direction * distanceToMove * (-1))

    /***
     * Method used for launch a missile from the plane
     * @return Tuple containing a set with missiles and the new plane
     */
    override def spawn(): Tuple2[Set[Missile], Plane] =
        val spawnerInfos = missileSpawner.spawn()
        Tuple2(
            spawnerInfos._1,
            this.copy(missileSpawnerOpt = Option(spawnerInfos._2))
        )

    /**
     * The current health of the object.
     *
     * @return the current health of the object.
     */
    override def currentLife: LifePoint = lifePoint

    /**
     * The initial health of the object.
     *
     * @return the initial health of the object.
     */
    override def initialLife: LifePoint = planeInitialLife

    /**
     * The object takes damage.
     *
     * @param damage the damage that the object received.
     * @return the object with the new health.
     */
    override def takeDamage(damage: LifePoint): Plane = damage match
        case d if(lifePoint - d > 0) => this.copy(lifePoint = lifePoint - d)
        case _ => this.copy(lifePoint = lifePointDeath)

    /**
     * Return the hit box of the object.
     *
     * @return the hit box of the object.
     */
    override protected def hitBox: HitBox = HitBoxRectangular(position, planeBaseSize, planeHeightSize, (destination <--> position).direction.get)

    /**
     * Return the affiliation of the object.
     *
     * @return the affiliation of the object.
     */
    override def affiliation: Affiliation = Affiliation.Friendly
    override def timeElapsed(dt: DeltaTime): Plane = this.copy(deltaTime = deltaTime + dt, Option(missileSpawner.timeElapsed(dt)))
    override def destinationReached: Boolean = position == destination
    override def destination: Point2D = finalPosition

    override def toString: String = "Vehicle: actualPosition: (" + position.x + "," + position.y + "); " +
                                    "finalPosition: (" + finalPosition.x + "," + finalPosition.y + "); " +
                                    "direction: " + direction.toString + " " +
                                    "actual life: " + currentLife

object Plane:
    def apply(direction: vehicleTypes, height: Double)(using Random) : Plane = direction match
        case vehicleTypes.Left_To_Right => PlaneImpl(Point2D(0, height), Point2D(World.width, height), direction)
        case vehicleTypes.Right_To_Left => PlaneImpl(Point2D(World.width, height), Point2D(0, height), direction)