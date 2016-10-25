# List

List is an example of add and delete operations in a list, where delete requires entering and exiting an edit mode

## Model

EditMode contains the posible edit states: Normal and Edit. If Edit, it contains the value that was selected to trigger the state change.

## State

Elements are the elements on the list

Selected are the elements in the list that are selected

EditMode is the current editing mode state

## Business logic

handleAdd()

Handles the user clicking on the Add element to list, by adding a new random element

handleEnterEditState()

Handles long clicks on the list when the user isn't in edit mode, by changing the state to edit mode with the current value

handleExitEditState()

Handles changing the editmode to normal after the user pressed delete to delete all selected elements

handleOnCommitDelete()

Removes all selected elements after the edit mode changes back from editing to normal 

handleOnSwitchEditState()

Handles adding or removing the set of selected elements when the edit mode changes

handleSelect()

Handles selecting elements on click only if the current mode is edit

