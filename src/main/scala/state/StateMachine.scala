package state

// Created: 10/16/2014
//trait State[S <: State] {
//  val stateMachine: StateMachine[S]
//
//  def update(elapsed: Int): Unit
//}


trait StateMachine {
  type StateType <: State

  private var _currentState = initialState

  def currentState: StateType = _currentState

  def initialState: StateType

  trait State {
    def update(elapsed: Int): Unit

    def changeState(newState: StateType): Unit = {
      _currentState = newState
    }
  }

  def update(elapsed: Int) = {
    currentState.update(elapsed)
  }
}

object ControllerStateMachine extends StateMachine {
  type StateType = ControllerState

  def initialState = new ControllerState {
    override def update(elapsed: Int): Unit = ???
  }

//  currentState = new ControllerState {
//    override def update(elapsed: Int): Unit = ???
//  }

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


class ControllerState1 extends ControllerStateMachine.ControllerState {
  def update(elapsed: Int): Unit = {}
}

class ControllerState2 extends ControllerStateMachine.ControllerState {
  def update(elapsed: Int): Unit = {
    changeState(new ControllerState1)
  }
}

class ControllerState3 extends ControllerStateMachine.ControllerState {
  def update(elapsed: Int): Unit = {}
}

abstract class Key(name: String) {
  type Value
}

class AwesomeDB {
  import collection.mutable.Map
  val data = Map.empty[Key, Any]
  def get(key: Key): Option[key.Value] = data.get(key).asInstanceOf[Option[key.Value]]
  def set(key: Key)(value: key.Value): Unit = data.update(key, value)
}

trait IntValued extends Key {
  type Value = Int
}

object Stuff {
  val foo = new Key("foo") with IntValued

  val dataStore = new AwesomeDB
  val i: Option[Int] = dataStore.get(foo)
  dataStore.set(foo)(23)
}

trait State2 {
  type T <: State2
  type S <: StateMachine2[T]

  def update(machine: S, elapsed: Int): Unit = {
    //machine.changeState(this)
    machine.changeState(???)
  }
}


trait StateMachine2[StateType <: State2] {
  //type StateType <: State2

  var currentState: StateType = _

  def changeState(newState: StateType): Unit = {
    currentState = newState
  }

  def update(elapsed: Int) = {
    currentState.update(???, elapsed)
  }
}