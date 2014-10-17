package state

// Created: 10/16/2014
//trait State[S <: State] {
//  val stateMachine: StateMachine[S]
//
//  def update(elapsed: Int): Unit
//}

trait State {
  def update(machine: StateMachine, elapsed: Int): Unit = {}
}

trait StateMachine {
  var currentState: State

  def changeState(newState: State): Unit = {
    currentState = newState
  }

  def changeState(newState: State, elapsed: Int): Unit = {
    update(elapsed)
    currentState = newState
    update(elapsed)
  }

  def update(elapsed: Int) = {
    currentState.update(this, elapsed)
  }
}


class ControllerState extends State {
  def render() = {}
}

class ControllerState1 extends ControllerState {
}

class ControllerState2 extends ControllerState {
//  def update(machine: EngineStateMachine, elapsed: Int): Unit = {
//    machine.changeState(new ControllerState1)
//  }
}

class ControllerState3 extends ControllerState {
}

//class EngineStateMachine(var currentState: ControllerState) extends StateMachine {
//}