package state

// Created: 10/16/2014
//trait State[S <: State] {
//  val stateMachine: StateMachine[S]
//
//  def update(elapsed: Int): Unit
//}


trait StateMachine {
  type StateType <: State

  trait State {
    def update(elapsed: Int): Unit = {}
  }
  
  var currentState: StateType = _

  def changeState(newState: StateType): Unit = {
    currentState = newState
  }

  def changeState(newState: StateType, elapsed: Int): Unit = {
    update(elapsed)
    currentState = newState
    update(elapsed)
  }

  def update(elapsed: Int) = {
    currentState.update(elapsed)
  }
}

class ControllerStateMachine extends StateMachine {
  type StateType = ControllerState
  
  val controllerRelatedStuff = 3
  
  class ControllerState extends State {
    def render(): Unit = {
      controllerRelatedStuff
    }
  }
  
  override def update(elapsed: Int): Unit = {

    currentState.render()
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
}

//class EngineStateMachine(var currentState: ControllerState) extends StateMachine {
//}