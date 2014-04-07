package model

import org.flagship.console.screen.ScreenCharacter
import org.flagship.console.Size

/**
 * Created by mtrupkin on 4/6/2014.
 */
class Map(val size: Size) {
  def apply(x: Int, y: Int): ScreenCharacter = ???
  def move(x: Int, y: Int): Boolean = ???
}
