package console.map

import console.model.TileMap
import me.mtrupkin.console.control.Control
import console.screen.Screen

/**
 * Created by mtrupkin on 4/6/2014.
 */
abstract class MapWidget(val map: TileMap) extends Control {
  override def minSize = map.size
  override def render(screen: Screen) {
    map.foreach((x, y, s) => screen(x, y) = s)
  }

  def onSelection
}
