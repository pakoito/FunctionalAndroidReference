# Rotation

Rotation is an example of Observable operations persisting across destructive operations like rotation

## Model

Transaction is an algebra representing a state machine for the operation states

UserInput is a valid string types by the user

## State

Transaction is the current value of the transaction

User is the current valid user input

## Services

RotationAgotService fetches character information from a real remote API, and transforms the result to a Transaction value

## Business logic

handleUserInput()

Makes sure that the user input state to be valid

handleStart()

Handles the transaction state Idle until a request is available

handleLoad()

Handles the transaction state loading into a network request, until the request comes with a result

handleReload()

Handles the transaction state Success into waiting for another new user input

handleRetryAfterError()

Handles the case Failure to retry an operation once every 5 seconds. It never stops retrying.