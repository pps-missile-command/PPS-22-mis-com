package view

import javafx.scene.image.ImageView
import javafx.scene.image.Image
import model.collisions.Damageable
import model.ground.{City, Ground, MissileBattery}
import utilities.Constants

object Visualizer:
    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

    /***
     * Method used for preparing the ImageView of a city.
     * @param structure City to be used.
     * @return ImageView of the city passed.
     */
    def prepareCityImageView(structure: City): ImageView =
        var image = ImageView(Image(resourceFolderPath + "\\city_" + structure.currentLife + ".png",
            Constants.cityBaseSize,
            Constants.cityHeightSize,
            false,
            false))
        image.setX(structure.position.x)
        image.setY(structure.position.y)
        image

    /***
     * Method used for preparing the ImageView of a missile turret.
     * @param structure Missile turret to be used.
     * @return ImageView of the missile turret passed
     */
    def prepareMissileImageView(structure: MissileBattery): ImageView =
        var image = ImageView(Image(resourceFolderPath + "\\Base_" + structure.isReadyForShoot + "_" + structure.currentLife + ".png",
            Constants.missileBatteryBaseSize,
            Constants.missileBatteryHeightSize,
            false,
            false))
        image.setX(structure.bottomLeft_Position.x)
        image.setY(structure.bottomLeft_Position.y)
        image


    /***
     * Method used for preparing a list of ImageView of a given ground.
     * @param ground Ground to be used.
     * @return List of ImageView of all the structures in the ground.
     */
    def printGround(ground: Ground): List[ImageView] =
        (for structure <- ground.cities yield prepareCityImageView(structure))
            ++
            (for structure <- ground.turrets yield prepareMissileImageView(structure))