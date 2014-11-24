package console.model

import console.screen.{RGBColor, ScreenChar}
import scala.util.Random

// Created: 4/10/2014

object Animations {
  import RGBColor._
  val flip = List(
//    ScreenChar('\\', fg = LightGrey),
//    ScreenChar('|', fg = LightGrey),
//    ScreenChar('/', fg = LightGrey),
//    ScreenChar('-', fg = LightGrey))
    ScreenChar('.', fg = LightGrey),
    ScreenChar('\\', fg = LightGrey),
    ScreenChar('\u2502', fg = LightGrey),
    ScreenChar('/', fg = LightGrey),
    ScreenChar('\u2500', fg = LightGrey))
}

trait TileAnime {
  var totalElapsedTime = 0
  def apply(elapsed: Int): ScreenChar = {
    totalElapsedTime += elapsed
    animate(elapsed)
  }

  def animate(elapsed: Int): ScreenChar
}

class StaticAnime(val sc: ScreenChar) extends TileAnime {
  def animate(elapsed: Int): ScreenChar = sc
}

class TempAnime(val oldAnim: TileAnime, val newAnim: TileAnime, val time: Int) extends TileAnime {
  def animate(elapsed: Int): ScreenChar = {
    if ( totalElapsedTime < time ) newAnim(elapsed) else oldAnim(elapsed)
  }
}

class DelayedAnime(val currAnim: TileAnime, val newAnim: TileAnime, val delay: Int) extends TileAnime {
  def animate(elapsed: Int): ScreenChar = {
    if ( totalElapsedTime < delay ) currAnim(elapsed) else newAnim(elapsed)
  }
}

class FrameAnime(val frames: List[ScreenChar], val cycleTime: Int = 500) extends TileAnime {
  def animate(elapsed: Int): ScreenChar = {
    val timeSlice = totalElapsedTime / cycleTime
    val frameIndex = timeSlice % frames.size
    frames(frameIndex)
  }
}

class SparkleAnime(val sc: ScreenChar, val frequency: Int = 1, val duration: Int = 500) extends TileAnime {
  var sparkleOn: Boolean = false
  val sparkleOnChar: ScreenChar = ScreenChar('.', RGBColor.LightGrey)
  val delay = Random.nextInt(10000)

  def animate(elapsed: Int): ScreenChar = {
    if (sparkleOn) {
      if (totalElapsedTime > duration) {
        totalElapsedTime = 0
        sparkleOn = false
      }
      sparkleOnChar
    } else {
      if (totalElapsedTime + delay > 10000) {
        if (Random.nextInt(1000) < frequency) sparkleOn = true
        totalElapsedTime = 0
      }
      sc
    }
  }
}