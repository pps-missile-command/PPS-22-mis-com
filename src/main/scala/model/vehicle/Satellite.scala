package model.vehicle

import model.World.{height, width}
import model.{DeltaTime, Scorable, World}
import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Damageable, HitBox, LifePoint, lifePointDeath}
import model.elements2d.{Angle, Point2D}
import model.missile.Missile
import model.spawner.{GenericSpawner, GenericSpawnerImpl, SpecificSpawners}

import scala.util.Random


trait Satellite extends Damageable, GenericSpawner[Missile]:
    def position: Point2D
    override def timeElapsed(dt: DeltaTime): Satellite


case class SatelliteImpl(actualPosition: Point2D,
                         missileSpawner: GenericSpawnerImpl[Missile],
                         lifePoint: LifePoint = satelliteInitialLife,
                         deltaTime: DeltaTime = 0)
                        (using Random) extends Satellite with Scorable(2):

    override def position: Point2D = actualPosition

    /***
     * Method used for launch a missile from the plane
     * @return Tuple containing a set with missiles and the new plane
     */
    override def spawn(): Tuple2[Set[Missile], Satellite] =
        val spawnerInfos = missileSpawner.spawn()
        Tuple2(
            spawnerInfos._1,
            this.copy(missileSpawner = spawnerInfos._2)
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
    override def initialLife: LifePoint = satelliteInitialLife

    /**
     * The object takes damage.
     *
     * @param damage the damage that the object received.
     * @return the object with the new health.
     */
    override def takeDamage(damage: LifePoint): Satellite = damage match
        case d if(lifePoint - d > 0) => this.copy(lifePoint = lifePoint - d)
        case _ => this.copy(lifePoint = lifePointDeath)

    override protected def hitBox: HitBox =
        val point2D = Point2D(position.x, position.y)
        HitBoxRectangular(position, satelliteBaseSize, satelliteHeightSize, Angle.Degree(0))
    override def affiliation: Affiliation = Affiliation.Enemy
    override def timeElapsed(dt: DeltaTime): Satellite = this.copy(missileSpawner = missileSpawner.timeElapsed(dt), deltaTime = deltaTime + dt)

object Satellite:
    def apply(xCoordinate: Double)(using Random) : Satellite =
        val position = Point2D(xCoordinate, satelliteHeight)
        SatelliteImpl(position,
        GenericSpawner[Missile](spawnInterval, spawnable = SpecificSpawners.FixedMissileStrategy(width, height, position)(using Random)))
