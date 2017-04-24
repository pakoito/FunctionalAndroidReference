# FunctionalAndroidReference

FunctionalAndroidReference is a showcase project of Functional Reactive Programming on Android, using RxJava.

It's a companion app to the presentation ["Fully Reactive Apps"](https://speakerdeck.com/pakoito/fully-reactive-apps) at Droidcon UK 2016.

[![Build Status](https://travis-ci.org/pakoito/FunctionalAndroidReference.svg?branch=master)](https://travis-ci.org/pakoito/FunctionalAndroidReference) [![codecov](https://codecov.io/gh/pakoito/FunctionalAndroidReference/branch/master/graph/badge.svg)](https://codecov.io/gh/pakoito/FunctionalAndroidReference)

It is not meant to be a canonical reference, but as an example of how far functional programming can be taken. It's also a collection of patterns and ideas about how to express use cases, business features, and UX on a FRP paradigm.

The project has multiple self-imposed limitations:

### Full separation between UI and business logic.

The project is split into several modules. Every module has its own README file.

#### app

The UI layer is written purely in Java 7 with Android dependencies.

It depends on all modules below.

#### liblogic

The business logic that controls the views. It doesn't contain any Android dependency.

It is written in Kotlin for convenience, but it could be rewritten in Java 7 with ease, although it will be a bit verbose without lambdas (see [retrolambda](https://github.com/orfjackal/retrolambda)).

It depends on the modules below.

#### libservices

Any network services, POJOs, and communications that aren't in the Android framework. Again, it's not dependent on any Android.

Written in Kotlin too.

It depends on the module below.

#### libcore

Helpers and common general types. No Android.

Written in Kotlin, with no Android dependencies.

### Pragmatically functional

* liblogic and libservices must contain as few classes as pragmatically possible. Favour functions instead.

* Every function must be written as an expression body.

* Every function must be as pure as possible.

* Every parameter in a function must be passed explicitly. No globals, no fields.

* Prefer encapsulating variables in closures rather than fields. If using fields, final fields will be mandated whenever possible.

* Collections must be immutable.

* No nullable types outside the UI and service layers.

* Use functional patterns like unions, laziness, or higher order functions, instead of classic OOP Gang of Four patterns.

### Fully reactive

The architecture is reminiscent of [Flux](https://facebook.github.io/flux/docs/overview.html), [Redux](http://redux.js.org/), or [Elm](https://guide.elm-lang.org/architecture/). This is no coincidence.

Every method in the **UI layer** is either:

* a stream/signal, represented by a method returning an `rx.Observable`.

* a new UI state: new text value, new element on a RecyclerView, show a dialog... represented by a void/`Unit` method.

Every function in the **business layer** is:

* a `rx.Subscription` encompassing all the behaviour for one or many use cases.

### Testable

Every use case must be accompanied of a test suite covering its complete behaviour.

### Moderately documented

Every public function must be documented.

Inlined comments only when intent isn't clear.

### No lifecycle

Separate the business logic from the Android lifecycle at the earliest layer possible.

### No magic

Avoid DI frameworks like Dagger, and hand-roll injection instead.

Avoid code generation outside Kotlin helpers.

## License

Copyright (c) pakoito 2016

The Apache Software License, Version 2.0

See LICENSE.md
