package console.model

import me.mtrupkin.console.screen.{RGBColor, ScreenChar}
import me.mtrupkin.geometry.{Point, Size}
import me.mtrupkin.tile.TileLegend._
import me.mtrupkin.tile.{Floor, Tile, TileMap}
import me.mtrupkin.tile.{ScreenMap, TileLegend}
import model.Encounter


/**
 * Created by mtrupkin on 4/6/2014.
 */
class World(
  var time: Long = 0,
  val player: Player,
  var agents: List[Agent]) {

  val tileMap: TileMap = TileLegend.load()

  tileMap.wipe()

  val encounter: Encounter = new Encounter(player, agents)

  def update(elapsed: Int) {
    time += elapsed
    tileMap.update(elapsed)
  }
  def act(direction: Point): Boolean = {
    val action = tryAct(direction)
    if (action) {
      //encounter.nextTurn()
      encounter.simulate
    }

    action
  }
  def tryAct(direction: Point): Boolean = {
    val p = player.position + direction
    for (a <- agents.find(a => a.position == p) ) {
      attack(a)
      return true
    }

    if (tileMap.move(p.x, p.y)) {
      player.move(direction)
      true
    } else false
  }

  def attack(a: Agent): Unit = {
    Combat.attackMelee(player, a)
  }
}

object World {
  def apply(): World = {
    var playerStart: Point = Point(0,0)
    var agents: List[Agent] = Nil

    val levelName = "object-1"
    val is = getClass.getResourceAsStream(s"/maps/$levelName.xp")
    val objectMap = ScreenMap.readXP(levelName, is)

    for((i, x) <- objectMap.matrix.zipWithIndex) {
      for((t, y) <- i.zipWithIndex) {
        t.c match {
          case 'S' => playerStart = Point(x, y)
          case 'T' => agents = Agent.createTurret(Point(x, y)) :: agents
          case _ =>
        }
      }
    }

    val player: Player = Player("player", ScreenChar('@'), playerStart)
    new World(0, player, agents)
  }
}
