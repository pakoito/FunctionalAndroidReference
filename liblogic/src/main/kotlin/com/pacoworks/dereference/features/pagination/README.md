# Pagination

Rotation is an example of an infinite list in reactive style

## Model

The list uses simple values

## State

Elements are the elements on the list

Pages is the current pagination page

## Services

PaginationService is a mock network service that returns a new page worth of values after a delay

## Business logic

handleLoading()

It handles a signal received to fetch new elements after the current page, and appends them at the end of the list

