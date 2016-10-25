# Home Screen

## Model

A screen is a union of all the screens available in the app

## State

It uses tha app's global navigation state

## Business logic

pushScreen()
When a new screen is received, tell the view to navigate to it. In this app, that'd be Conductor pushing a new Controller.

backPressed()
When the back key is pressed, tell the navigator's to pop and return the current screen
