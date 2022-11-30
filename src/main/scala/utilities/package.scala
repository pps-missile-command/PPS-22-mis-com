import model.World
import model.collisions.LifePoint
import view.{ViewConstants, ratioWidth}
import view.given_Conversion_Double_Int

package object utilities:
    //missile battery informations
    val missileBattery_BaseSize: Int = 50
    val missileBatteryHeightSize: Int = 29
    val reloadingTime: Int = 3 //reloading time of the turret (in seconds!)
    val missileBatteryInitialLife: LifePoint = 3 //initial life
    //city informations
    val cityBaseSize: Int = 50
    val cityHeightSize: Int = 29
    val cityInitialLife: LifePoint = 3 //initial life
    //missile informations
    val missileHealth: LifePoint = 1
    val missileFriendlyDamage: LifePoint = 1
    //Structure spawning parameters
    val citySpacer: Int = (World.width.toDouble / ratioWidth) * 40 / 1400 //spacer between cities
    val turretSpacer: Int = (World.width.toDouble / ratioWidth) * 120 / 1400 //spacer between a city and a turret

