package view

import model.World

import java.awt.{Image, Toolkit}
import model.collisions.{Collisionable, Damageable}
import model.elements2d.Point2D
import model.ground.{City, Ground, MissileBattery}
import model.missile.{Missile, hitboxBase, hitboxHeight}
import utilities.*

import javax.imageio.ImageIO

object Visualizer:

    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

    /***
     * Method used for preparing the ImageView of a city.
     * @param structure City to be used.
     * @return ImageView of the city passed.
     */
    def prepareCityImage(structure: City): Tuple4[Image, Point2D, Int, Int] =
        (
            ImageIO.read(getClass.getResource("/city_" + structure.currentLife + ".png")),
            structure.position map convertPosition,
            cityBaseSize use convertWidth,
            cityHeightSize use convertHeight
        )

    /***
     * Method used for preparing the ImageView of a missile turret.
     * @param structure Missile turret to be used.
     * @return ImageView of the missile turret passed
     */
    def prepareBatteryMissileImage(structure: MissileBattery): Tuple4[Image, Point2D, Int, Int] =
        (
            ImageIO.read(getClass.getResource("/Base_" + structure.isReadyForShoot + "_" + structure.currentLife + ".png")),
            structure.bottomLeft_Position map convertPosition,
            missileBattery_BaseSize use convertWidth,
            missileBatteryHeightSize use convertHeight
        )

    /***
     * Method used for preparing a list of ImageView of a given ground.
     * @param ground Ground to be used.
     * @return List of ImageView of all the structures in the ground.
     */
    def printGround(ground: Ground): List[Tuple4[Image, Point2D, Int, Int]] =
        (for structure <- ground.cities yield prepareCityImage(structure))
            ++
            (for structure <- ground.turrets yield prepareBatteryMissileImage(structure))