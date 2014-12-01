package controller

import me.mtrupkin.console.control.Composite
import me.mtrupkin.console.controller.ControllerStateMachine
/**
 * Created by mtrupkin on 11/29/2014.
 */
trait Options  { self: ControllerStateMachine =>
  class OptionsController extends ControllerState {
    override val window: Composite = defaultWindow()

    override def update(elapsed: Int): Unit = ???
  }
}

