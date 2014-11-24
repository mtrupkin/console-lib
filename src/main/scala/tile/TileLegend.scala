package me.mtrupkin.tile

import console.screen.ScreenChar
import me.mtrupkin.geometry.Size
import me.mtrupkin.tile._

/**
 * Created by mtrupkin on 11/22/2014.
 */

object TileLegend {
  def toTile(s: ScreenChar): Tile = {
    s.c match {
      case 'S'  => new Floor
      case ' ' | '.' => new Floor
      case 'T'  => new Floor
      case _ => new Wall(s)
    }
  }

  def load(): TileMap = {
    //val screenMap = ScreenMap.readConsole("C:\\dev\\REXPaint-R9\\images\\test2.xpm")
    val screenMap = ScreenMap.readXP("C:\\dev\\REXPaint-R9\\images\\level-1.xp")

    val tileMap = new TileMapImpl(Size(screenMap.width, screenMap.height)) {}
    for(i <- screenMap.matrix.zipWithIndex) {
      for(j <- (i._1).zipWithIndex) {
        val (x, y, t) = (i._2, j._2, j._1)
        tileMap.tiles(x)(y) = TileLegend.toTile(t)
      }
    }

    tileMap
  }

}
