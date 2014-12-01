package console.state

// Created: 10/16/2014

trait StateMachine {
  type StateType <: State

  def initialState: StateType

  private var previousStates: List[StateType] = List()
  private var _currentState = initialState
  def currentState: StateType = _currentState

  trait State {
    def update(elapsed: Int): Unit

    def flipState(newState: StateType): Unit = {
      previousStates = currentState :: previousStates
      updateState(newState)
    }

    def onEnter(): Unit = {}
    def onExit(): Unit = {}

    def previousState(): StateType = previousStates.head

    def revertState(): Unit = {
      val previousState = previousStates.head
      previousStates = previousStates.tail
      updateState(previousState)
    }

    def changeState(newState: StateType): Unit = {
      previousStates = List()
      updateState(newState)
    }

    private def updateState(newState: StateType): Unit = {
      currentState.onExit()
      _currentState = newState
      currentState.onEnter()
    }
  }

  def update(elapsed: Int) = {
    currentState.update(elapsed)
  }
}
