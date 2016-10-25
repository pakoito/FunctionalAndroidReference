## LibLogic

LibLogic contains all the logic, models, and services corresponding to the business domain layer of the app.

### Packages

#### Architecture.reactive

It contains all the interfaces and classes used to layout the app's skeleton in a reactive way

* ReactiveActivity is a delegate class for activities to call into with lifecycle and callback methods, i.e. permission results
* ReactiveController is a delegate class for Conductor's controllers to delegate their lifecycle into
* ActivityReactiveBuddy is a proxy object created by ReactiveActivity to access its exposed Observables, like lifecycle or activity results
* ControllerReactiveBuddy is a proxy object created by ReactiveController to access its exposed Observables, like lifecycle

#### Architecture.ui

Contains the constructs used to do one-way binding between state and view

* StateHolder is a typealias representing a wrapped state into a SerializedRelay backed by a BehaviourRelay
* BoundView is an interface to be implemented by a view that allows binding by using `binding()`
* `binding()` is a short abstraction used to handle one-way binding between state and views respecting the Android lifecycle and main thread requirements

#### Features

Each package contains a README description of its contents, including description of the solution it proposes and the layers required for it