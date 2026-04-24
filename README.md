# MoMap testbed

We are making MoMap, the generic map application, extensible.
This repo contains code related to this effort.

## Setup

MoMap api (backend for frontend) and frontend repos must be cloned, and
be added in a common workspace with testbed code.

## Extensible backend and frontend

Extensibility is implemented using Java's ServiceLoader, and contributed functionality
is provided through Koin. Various parts can then pick up specific contributions, e.g
routing.

The frontend is served by the backend, and frontend extensions are injected into index.html.
The frontend code can then add placeholders where extension code may contribute React elements.
