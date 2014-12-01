package me.mtrupkin.terminal

import me.mtrupkin.console.screen.{Screen, ConsoleKey}
import me.mtrupkin.geometry.Size

// Created on 11/20/2014.

case class Input(
  key: Option[ConsoleKey] = None,
  mouseMove: Option[me.mtrupkin.geometry.Point] = None,
  mouseClick: Option[me.mtrupkin.geometry.Point] = None,
  mouseExit: Option[me.mtrupkin.geometry.Point] = None)

trait Terminal {
  val terminalSize: Size

  var closed = false
  var input: Option[Input] = None

  def render(screen: Screen)
  def close()
}