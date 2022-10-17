package view.audio

import java.net.{MalformedURLException, URL}
import javax.sound.sampled.{AudioSystem, UnsupportedAudioFileException}

object AudioPlayer:
    def playAudio(URL_audio: String): Unit =
        try
            val url = new URL(URL_audio)
            val audioIn = AudioSystem.getAudioInputStream(url)
            val clip = AudioSystem.getClip
            clip.open(audioIn)
            clip.start
        catch
            case ex1: MalformedURLException => println("URL not found")
            case ex2: UnsupportedAudioFileException => println("URL found, but not an audio")
            case ex3: Throwable =>


    //Add audio here
    val explosionSmall = "https://www.shockwave-sound.com/sound-effects/explosion-sounds/Arcade%20Explo%20A.wav"