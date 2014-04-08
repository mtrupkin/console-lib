package map

import model.TileMap
import org.flagship.console.control.Control
import org.flagship.console.screen.Screen

/**
 * Created by mtrupkin on 4/6/2014.
 */
class MapWidget(val map: TileMap) extends Control {
  override def minSize = map.size
  override def render(screen: Screen) {
    map.foreach((p, s) => screen(p) = s)
  }
}
