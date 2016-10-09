## App

The app module contains all the code in the view layer, including:

* Main Application class
* Single Activity forwarding ReactiveActivity calls
* Navigation using Conductor
* Implementation of the feature Screens
* Provision of any Android or platform-specific service: i.e. SharedPreferences, Network Connectivity, Databases...
* Modules to hold state and services
* Any UI widget implementation

### Packages

#### features.global

Contains the architectural pillars:

* DereferenceApplication: it's the only singleton in the app and holds the global injection module called Injector
* Injector: it's the global dependencies module, and holds all global state (i.e. state of navigation) and retained services (i.e. OkHttp)
* MainActivity: delegates its callbacks to the helper ReactiveActivity. It's also in charge of initializing Conductor and DebugDrawer. Lastly, it initializes the MainOrchestrator.
* MainOrchestrator: makes sure that all use cases that apply for the whole application are subscribed and unsubscribed correctly, along with passing the correct dependencies.
* MainNavigator: it's a use case for navigation based off Conductor. It implements the liblogic requirements for screen transitions.
* BaseController: contains the delegation code for lifecycle events on any screen, along with the base implementation of view binding.

#### features

Contains the implementations for screens and services.

Each package contains the screen, and any Adapter and ViewHolder classes required by it.

A screen is defined as: 

* A class inheriting from BaseConductor
* Holds a screen-scoped copy of states and services
* Implements an interface declaring all its possible inputs as void methods
* Implements another interface declaring all possible interactions in the screen as Observables
* Binds itself to the current state
* Subscribes itself to the interactor that guides the business logic

A README with what each screen does can be found in their packages on the liblogic module.

#### widgets

Contains reactive versions of common widgets, like RecyclerView adapter, or TouchHelper.