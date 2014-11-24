package console.map


import me.mtrupkin.console.control.Control
import console.screen.Screen
import me.mtrupkin.tile.TileMap

/**
 * Created by mtrupkin on 4/6/2014.
 */
abstract class MapWidget(val map: TileMap) extends Control {
  override def minSize = map.size
  override def render(screen: Screen): Unit = {
    map.foreach((x, y, t) => screen(x, y) = t.sc)
  }

  def onSelection
}
