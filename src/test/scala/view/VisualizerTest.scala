package view

import javafx.embed.swing.JFXPanel
import model.ground.Ground
import org.scalatest.funspec.AnyFunSpec

class VisualizerTest extends AnyFunSpec {
    var ground = Ground()
    val resourceFolderPath: String = (System.getProperty("user.dir").toString + "\\src\\main\\resources\\")
    //val jfxPanel: JFXPanel = new JFXPanel

    describe("The visualizer") {
        ignore ("should generate the required amount of new items when called") {
            assert(Visualizer.printGround(ground).length === 9)
        }
        ignore ("should generate the correct image") {
            assert(Visualizer.printGround(ground)(0).getImage.getUrl === resourceFolderPath + "\\city_3.png")
            assert(Visualizer.printGround(ground)(3).getImage.getUrl === resourceFolderPath + "\\city_3.png")
            assert(Visualizer.printGround(ground)(8).getImage.getUrl === resourceFolderPath + "\\Base_false_3.png")
        }
        ignore ("should change the printable image in case of damages") {
            assert(Visualizer.printGround(ground)(0).getImage.getUrl === resourceFolderPath + "\\city_3.png")
            ground = ground.dealDamage(ground.cities(0), 2)
            assert(Visualizer.printGround(ground)(0).getImage.getUrl === resourceFolderPath + "\\city_1.png")
        }
        ignore ("should change the printable image in case of full reload of the turret") {
            assert(Visualizer.printGround(ground)(8).getImage.getUrl === resourceFolderPath + "\\Base_false_3.png")
            Thread.sleep(3000)
            assert(Visualizer.printGround(ground)(8).getImage.getUrl === resourceFolderPath + "\\Base_true_3.png")
        }
    }
}
