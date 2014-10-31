package state

// Created: 10/16/2014
//trait State[S <: State] {
//  val stateMachine: StateMachine[S]
//
//  def update(elapsed: Int): Unit
//}


trait StateMachine {
  type StateType <: State

  var currentState: StateType = _

  trait State {
    def update(elapsed: Int): Unit
  }

  def changeState(newState: StateType): Unit = {
    currentState = newState
  }

  def update(elapsed: Int) = {
    currentState.update(elapsed)
  }
}

class ControllerStateMachine extends StateMachine {
  type StateType = ControllerState

  var controllerCounter = 0

  trait ControllerState extends State {

    def render(): Unit = {
      controllerCounter += 1
    }
  }

  def mainLoop(): Unit = {
    while(true) {
      currentState.render()
      currentState.update(100)
    }
  }
}

object ControllerStateMachine extends ControllerStateMachine {
}

class ControllerState1 extends ControllerStateMachine.ControllerState {
  def update(elapsed: Int): Unit = {}
}

class ControllerState2 extends ControllerStateMachine.ControllerState {
  def update(elapsed: Int): Unit = {
    ControllerStateMachine.changeState(new ControllerState1)
  }
}

class ControllerState3 extends ControllerStateMachine.ControllerState {
  def update(elapsed: Int): Unit = {}
}