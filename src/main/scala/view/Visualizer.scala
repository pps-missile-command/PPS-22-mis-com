package view

import java.awt.{Image, Toolkit}
import model.collisions.{Collisionable, Damageable}
import model.ground.{City, Ground, MissileBattery}
import model.missile.{Missile, hitboxBase, hitboxHeight}
import utilities.Constants
import view.MissileView.getClass


case class CollisionableElement(image: Image, x: Double, y: Double)


object Visualizer:
    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

    /***
     * Method used for preparing the ImageView of a city.
     * @param structure City to be used.
     * @return ImageView of the city passed.
     */
    def prepareCityImage(structure: City): Tuple3[Image, Double, Double] =
        (
        Toolkit.getDefaultToolkit().getImage(resourceFolderPath + "\\city_" + structure.currentLife + ".png"),
        structure.position.x,
        structure.position.y
        )

    /***
     * Method used for preparing the ImageView of a missile turret.
     * @param structure Missile turret to be used.
     * @return ImageView of the missile turret passed
     */
    def prepareMissileImageView(structure: MissileBattery): Tuple3[Image, Double, Double] =
        (
        Toolkit.getDefaultToolkit().getImage(resourceFolderPath + "\\Base_" + structure.isReadyForShoot + "_" + structure.currentLife + ".png"),
        structure.bottomLeft_Position.x,
        structure.bottomLeft_Position.y
        )


    /***
     * Method used for preparing a list of ImageView of a given ground.
     * @param ground Ground to be used.
     * @return List of ImageView of all the structures in the ground.
     */
    def printGround(ground: Ground): List[Tuple3[Image, Double, Double]] =
        (for structure <- ground.cities yield prepareCityImage(structure))
            ++
            (for structure <- ground.turrets yield prepareMissileImageView(structure))


//    val conversion: Collisionable => CollisionableElement = _ match
//        case m: Missile => CollisionableElement(
//            ImageView(Image(getClass.getResourceAsStream(""), hitboxBase, hitboxHeight, false, false)),
//            m.position.x,
//            m.position.y)
//
//    def apply(imageView: ImageView, x: Double, y: Double) =
//        CollisionableElement(imageView, x, y)
//
//    def printMissiles(collisionables: List[Collisionable]): List[CollisionableElement] =
//        for
//            i <- collisionables
//        yield conversion(i)