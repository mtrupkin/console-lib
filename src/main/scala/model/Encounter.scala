package model

import console.model.{Player, Agent}

/**
 * Created by mtrupkin on 12/6/2014.
 */
class Encounter(
  val player: Player,
  val agents: Seq[Agent]) {
  var round: Int = 0

  def nextTurn(): Unit = {
    for(agent <- activeAgents) {
      agent.act(player)
    }
    round += 1
    println
  }

  def activeAgents: Seq[Agent] = agents.filter(a => a.hitPoints >= 0)

  def simulate(): Unit = {
    do {
      nextTurn()
    } while (player.hitPoints >= 0)
    println(s"death in $round rounds")

    round = 0
    player.hitPoints = player.maxHitPoints
  }
}
