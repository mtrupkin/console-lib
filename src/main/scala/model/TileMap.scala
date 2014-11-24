package console.model

import console.screen.{RGBColor, ScreenChar}
import me.mtrupkin.geometry.{Point, Size}
import me.mtrupkin.tile.{Floor, Tile, TileMap}
import me.mtrupkin.tile.{ScreenMap, TileLegend}



/**
 * Created by mtrupkin on 4/6/2014.
 */

class World {
  var time = 0
  var playerStart: Point = Point(0,0)
  val tileMap: TileMap = TileLegend.load()

  val objectMap = ScreenMap.readXP("C:\\dev\\REXPaint-R9\\images\\object-1.xp")

  for(i <- objectMap.matrix.zipWithIndex) {
    for(j <- (i._1).zipWithIndex) {
      val (x, y, t) = (i._2, j._2, j._1)
      t.c match {
        case 'S' => playerStart = Point(x, y)
        case 'T' =>
        case _ =>
      }
    }
  }

  val player: Player = new Player("player", playerStart, ScreenChar('@'))

  def update(elapsed: Int) {
    time += elapsed
    tileMap.update(elapsed)
  }

  def act(entity: Entity, direction: Point): Unit = {
    val p = entity.position + direction
    tileMap(p.x, p.y) match {
      case f: Floor => move(entity, direction)
    }

  }

  def move(entity: Entity, direction: Point): Unit = {
    val p = entity.position + direction
    if (tileMap.size.inBounds(p) && tileMap(p.x, p.y).move) {
      entity.move(direction)
    }
  }

  tileMap.wipe()
}
