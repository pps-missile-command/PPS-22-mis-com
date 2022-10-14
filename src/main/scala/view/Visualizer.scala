package view

import javafx.scene.image.ImageView
import javafx.scene.image.Image
import model.ground.Ground
import utilities.Constants

object Visualizer:
    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")

    def printGround(ground: Ground): List[ImageView] =

        (for structure <- ground.cities //TODO manca set posizione
            yield ImageView(Image(getClass.getResourceAsStream("/satellite.png"),
                                    Constants.cityBaseSize,
                                    Constants.cityHeightSize,
                                    false,
                                    false))
            )
            ++
            (for structure <- ground.turrets //TODO manca set posizione
                yield ImageView(Image(getClass.getResourceAsStream("/satellite.png"),
                    Constants.cityBaseSize,
                    Constants.cityHeightSize,
                    false,
                    false))
                )