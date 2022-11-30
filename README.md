# cljssss

A Battlesnake written in Clojure.

# Challenges

See https://play.battlesnake.com/challenges/

## Training program

- [x] Avoid walls ([commit](https://github.com/wagdav/cljssss/commit/4e6e6290))
- [x] Avoid yourself ([commit](https://github.com/wagdav/cljssss/commit/4e6e6290))
- [ ] Avoid other battlesnakes
- [ ] Find food

## Solo Survival

Survive N turns on a 7x7 board.

- [x] Survive 100: Done with iterative deepening depth-first search using the number of turns as a utility function ([commit](https://github.com/wagdav/cljssss/commit/d1d9c637)).

## Development

Run the web server:

```
nix develop --command clojure -M -m thewagner.cljssss.web
```

Start the game by running [battlesnake engine](https://github.com/BattlesnakeOfficial/rules/tree/v2.0.0/cli) locally:

```
nix run .#battlesnake -- play --name cljssss --url http://localhost:8080 --viewmap
```
