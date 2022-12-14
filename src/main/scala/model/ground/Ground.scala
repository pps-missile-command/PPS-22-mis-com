package model.ground

import model.{DeltaTime, World}
import model.collisions.{Damageable, LifePoint}
import model.elements2d.Point2D
import model.ground.City
import model.missile.Missile
import view.ViewConstants
import utilities.*



object Ground:
    /**
     * Generate a ground based on given cities and turrets .
     * @param cities Passed cities;
     * @param turrets Passed turrets;
     * @return A ground with the passed cities/turrets.
     */
    def apply(cities: List[City], turrets: List[MissileBattery]): Ground = new Ground(cities, turrets)

    /**
     * Generate a ground with default settings
     * @return A ground with default cities/turrets
     */
    def apply(): Ground =
        val cities =
            for y <- List.range(0, 2) //generate all cities in 2 waves.
                x <- List.range(0, 3) //1° wave it generate all left side cities. 2° waves all the right side cities
                    yield City(Point2D(missileBattery_BaseSize + 2* turretSpacer +
                                (cityBaseSize + citySpacer) * x +
                                (3 * cityBaseSize + 2 * citySpacer + 2 * turretSpacer + missileBattery_BaseSize) * y,
                        World.height - cityHeightSize))
        val turrets =
            for x <- List.range(0, 3)
                yield MissileBattery(Point2D(turretSpacer + (missileBattery_BaseSize + 2 * turretSpacer + 3 * cityBaseSize + 2 * citySpacer) * x,
                    World.height - missileBatteryHeightSize))
        Ground(cities, turrets)

/**
 * Class that describe the ground
 * @param cities cities of the ground
 * @param turrets turrets of the ground
 */
case class Ground(cities: List[City], turrets: List[MissileBattery]):

    /**
     * @return Return all the cities that are still alive
     */
    def citiesAlive =
        for city <- cities if city.currentLife > 0
            yield city

    /**
     * @return Return the number of cities that are still alive
     */
    def numberOfCitiesAlive: Int = citiesAlive.length

    /**
     * @return Return all the missile batteries that are still alive
     */
    def missileBatteryAlive =
        for battery <- turrets if battery.currentLife > 0
            yield battery

    /***
     * @return Return the number of missile batteries that are still alive
     */
    def numberOfMissileBatteryAlive: Int = missileBatteryAlive.length

    /***
     * Method used for check if the player is still alive
     * @return True if there are at least 1 city alive
     */
    def stillAlive: Boolean = numberOfCitiesAlive > 0

    /***
     * Method used for shooting the nearest turret to a determinate point.
     * @param endPoint Point where the missile should explode
     * @return Tuple containing the new updated ground and the missile shooted. Missile is null if all turrets were reloading
     */
    def shootMissile(endPoint: Point2D): Tuple2[Ground, Option[Missile]] =
        def calculatePositions: List[Tuple2[Double, MissileBattery]] =
            for battery <- missileBatteryAlive if battery.isReadyForShoot
                yield (battery.bottomLeft_Position <-> endPoint, battery)

        val batteriesReadyForShoot = calculatePositions
        if(batteriesReadyForShoot.length > 0) then //only if there at least 1 turret ready for shoot, it will procede with shooting
            val missilesBatteryInformations = batteriesReadyForShoot.minBy(_._1)._2.shootRocket(endPoint).get
            val newTurrets = turrets.map( t => if (t == batteriesReadyForShoot.minBy(_._1)._2) missilesBatteryInformations._1 else t)
            (Ground(cities, newTurrets) , Some(missilesBatteryInformations._2))
        else
            (Ground(cities, turrets), None)

    /***
     * Method used for deal damage to a determinate structure.
     * @param structure Object of the structure that have to take damage.
     * @param damageDealed Damage that the structure have to take.
     * @return A new ground containing all the informations updated.
     */
    def dealDamage(structure: Damageable, damageDealed: LifePoint, ground: Ground = this): Ground = structure match
        case c: City => val newCities = ground.cities.map(o => if (o == c) o.takeDamage(damageDealed) else o)
            Ground(newCities, ground.turrets)
        case m: MissileBattery => val newTurrets = ground.turrets.map(o => if (o == m) o.takeDamage(damageDealed) else o)
            Ground(ground.cities, newTurrets)

    /***
     * Method used for deal damage to a determinate set of structures
     * @param structures List of structures that have to take damage.
     * @param damageDealed Damage that the structures have to take.
     * @return A new ground containing all the informations updated.
     */
    def dealDamage(structures: List[Damageable], damageDealed: LifePoint): Ground =
        var newGround = this
        //structures.foreach( structure => newGround = dealDamage(structure, damageDealed, newGround))
        structures.map( s => newGround = dealDamage(s, damageDealed, newGround) )
        newGround

    /***
     * Method used for deal different damages to a determinate set of structures
     * @param structures List of structures that have to take damage.
     * @param damages List of damages that the structures have to take.
     * @return A new ground containing all the informations updated.
     */
    def dealDamage(structures: List[Damageable], damages: List[LifePoint]): Ground =
        var newGround = this
        for ((structure, damage) <- (structures zip damages))
            newGround = dealDamage(structure, damage, newGround)
        newGround