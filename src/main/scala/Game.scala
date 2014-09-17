package org.flagship.game

import org.flagship.console.Point
import org.flagship.console.screen.{ConsoleKey, Screen}

// Created: 9/17/2014

trait Game {
  def update()
  def render(screen: Screen)

  def keyPressed(key: ConsoleKey)
  def mouseClicked(mouse: Point)
}
