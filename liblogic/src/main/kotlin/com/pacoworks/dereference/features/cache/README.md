# Cache

Cache is an example of representing an in-memory cache using unions

It is recommended to be used along the Debug Drawer network module to check operations do not happen on the network

The way it works is by selecting characters on a list of ids, and the information for that character will be displayed as soon as it's available

If the information has been retrieved before it'll be taken from memory, else it'll start a network request

## Model

AgotCharacter is a union representing a character we don't know about, or a character who's information is already available and cached

UnknownReason is, for an unavailable character (a cache miss), the reason why it's unavailable

## State

Ids is the list of all character ids we don't know about

Character is the currently selected id on the dropdown

CharacterCache is our cache of characters, which can be found by their ids

CurrentCharacter is the character we're displaying information from

## Services

CacheExample is a real network request that goes to the server and returns an AgotCharacter

If the network request is an error or the character is not valid, it returns the cause of the problem

## Business logic

handleFilterChange()

Whenever a new id is selected, check the cache. If the character is unavailable, request it to the services layer

handleSelected()

Handles changing the currently active id when selecting from the dropdown list