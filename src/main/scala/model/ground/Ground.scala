package model.ground

import model.elements2d.Point2D
import model.ground.City
import utilities.Constants

case class Ground():
    val cities =
        for y <- List.range(0, 2) //generate all cities in 2 waves.
            x <- List.range(0, 3) //1° wave it generate all left side cities. 2° waves all the right side cities
                yield City(Point2D(Constants.missileBatteryBaseSize + Constants.turretSpacer +
                                    (Constants.cityBaseSize + Constants.citySpacer) * x +
                                    (3 * Constants.cityBaseSize + 2 * Constants.citySpacer + 3 * Constants.turretSpacer + 2 * Constants.missileBatteryBaseSize) * y,
                                    0))
    val turrets =
        for x <- List.range(0, 3)
            yield MissileBattery(Point2D((Constants.missileBatteryBaseSize + 2 * Constants.turretSpacer + 3* Constants.cityBaseSize + 2 * Constants.citySpacer) * x
                                        ,0))

    /***
     * @return all the cities that are still alive
     */
    def citiesAlive =
        for city <- cities if city.currentLife > 0
            yield city

    /***
     * @return the number of cities that are still alive
     */
    def numberOfCitiesAlive: Int = citiesAlive.length

    /***
     * @return all the missile batteries that are still alive
     */
    def missileBatteryAlive =
        for battery <- turrets if battery.currentLife > 0
            yield battery

    /***
     * @return the number of missile batteries that are still alive
     */
    def numberOfMissileBatteryAlive: Int = missileBatteryAlive.length