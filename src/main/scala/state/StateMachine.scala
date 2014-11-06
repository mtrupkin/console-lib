package state

// Created: 10/16/2014



trait StateMachine {
  outer =>
  type StateType <: State

  def initialState: StateType

  private var _currentState = initialState
  def currentState: StateType = _currentState

  trait State {
    def update(elapsed: Int): Unit
    def changeState(newState: StateType): Unit = {
      currentState.onExit()
      _currentState = newState
      currentState.onEnter()
    }
    def onEnter(): Unit = {}
    def onExit(): Unit = {}
  }


  def update(elapsed: Int) = {
    currentState.update(elapsed)
  }


}
