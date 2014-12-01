package me.mtrupkin.tile

import java.nio.file.{Files, Paths}
import java.util.zip.GZIPInputStream

import me.mtrupkin.console.screen.ScreenChar
import me.mtrupkin.geometry.Size
import me.mtrupkin.tile._

/**
 * Created by mtrupkin on 11/22/2014.
 */

object TileLegend {
  def toTile(s: ScreenChar): Tile = {
    s.c match {
      case ' ' | '.' => new Floor
      case _ => new Wall(s)
    }
  }

  def load(): TileMap = {
    val levelName = "level-1"
    val is = getClass.getResourceAsStream(s"/maps/$levelName.xp")
    val screenMap = ScreenMap.readXP(levelName, is)

    val tileMap = new TileMapImpl(Size(screenMap.width, screenMap.height)) {}
    for((i, x) <- screenMap.matrix.zipWithIndex) {
      for((t, y) <- i.zipWithIndex) {
        tileMap.tiles(x)(y) = TileLegend.toTile(t)
      }
    }

    tileMap
  }

}
