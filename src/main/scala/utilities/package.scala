import model.collisions.LifePoint
import view.ViewConstants

package object utilities:
    //missile battery informations
    val missileBatteryBaseSize: Int = 100 //base size
    val missileBatteryHeightSize: Int = (missileBatteryBaseSize/ViewConstants.batteryMissileBaseHeightRatio).toInt //height size
    val reloadingTime: Int = 3 //reloading time of the turret (in seconds!)
    val missileBatteryInitialLife: LifePoint = 3 //initial life
    //city informations
    val cityBaseSize: Int = 100 //base size
    val cityHeightSize: Int = (cityBaseSize/ViewConstants.cityBaseHeightRatio).toInt //height size
    val cityInitialLife: LifePoint = 3 //initial life
    //missile informations
    val missileHealth: LifePoint = 1
    val missileFriendlyDamage: LifePoint = 1
    //Structure spawning parameters
    val citySpacer: Int = 20 //spacer between cities
    val turretSpacer: Int = 75 //spacer between a city and a turret

