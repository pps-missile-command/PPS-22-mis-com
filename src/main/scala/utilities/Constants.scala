package utilities

import model.collisions.LifePoint
import model.elements2d.Point2D

object Constants:
    //missile battery informations
    val missileBatteryBaseSize: Double = 30 //base size
    val missileBatteryHeightSize: Double = 30 //height size
    val reloadingTime: Int = 3 //reloading time of the turret (in seconds!)
    val missileBatteryInitialLife: LifePoint = 3 //initial life
    //city informations
    val cityBaseSize: Double = 30 //base size
    val cityHeightSize: Double = 30 //height size
    val cityInitialLife: LifePoint = 3 //initial life
    //missile informations
    val missileFriendlySpeed: Double = 5.0 //basic fiendly missile speed
    val missileHealth: LifePoint = 1
    val missileFriendlyDamage: LifePoint = 1
    //Structure spawning parameters
    val citySpacer: Double = 20 //spacer between cities
    val turretSpacer: Double = 50 //spacer between a city and a turret

