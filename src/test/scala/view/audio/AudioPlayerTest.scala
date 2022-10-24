package view.audio

import org.scalatest.funspec.AnyFunSpec


class AudioPlayerTest extends AnyFunSpec {
    describe("What the audioplayer should do") {
        ignore ("should play a specified audio") {
            AudioPlayer.playAudio(AudioPlayer.explosionSmall)
        }
        ignore ("should play even if resource is not available") {
            AudioPlayer.playAudio("broken URL")
        }
    }
}