import model.World
import model.collisions.LifePoint
import view.ViewConstants

package object utilities:
    //missile battery informations
    val missileBattery_BaseSize: Int = (World.width*100) / 1400
    val missileBatteryHeightSize: Int = (World.height*58) / 800
    val reloadingTime: Int = 3 //reloading time of the turret (in seconds!)
    val missileBatteryInitialLife: LifePoint = 3 //initial life
    //city informations
    val cityBaseSize: Int = (World.width*100) / 1400
    val cityHeightSize: Int = (World.height*58) / 800
    val cityInitialLife: LifePoint = 3 //initial life
    //missile informations
    val missileHealth: LifePoint = 1
    val missileFriendlyDamage: LifePoint = 1
    //Structure spawning parameters
    val citySpacer: Int = (World.width * 20) / 1400 //spacer between cities
    val turretSpacer: Int = (World.width * 75) / 1400 //spacer between a city and a turret

