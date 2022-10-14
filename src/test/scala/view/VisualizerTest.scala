package view

import javafx.embed.swing.JFXPanel
import model.ground.Ground
import org.scalatest.funspec.AnyFunSpec

class VisualizerTest extends AnyFunSpec {
    val ground = Ground()
    val jfxPanel: JFXPanel = new JFXPanel

    describe("The visualizer") {
        it("should generate the required amount of new items when called") {
            assert(Visualizer.printGround(ground).length === 9)
        }
    }
}
