package view.audio

import java.net.{MalformedURLException, URL}
import javax.sound.sampled.{AudioSystem, UnsupportedAudioFileException}

object AudioPlayer:
    def playAudio(URL_audio: String): Unit =
        val clip = AudioSystem.getClip
        try
            val url = new URL(URL_audio)
            val audioIn = AudioSystem.getAudioInputStream(url)
            clip.open(audioIn)
        catch
            case ex1: MalformedURLException => println("URL not found")
            case ex2: UnsupportedAudioFileException => println("URL found, but not an audio")
            case ex3: Throwable =>
        finally
            clip.start

    //Add audio here
    val explosionSmall = "https://www.shockwave-sound.com/sound-effects/explosion-sounds/Arcade%20Explo%20A.wav"