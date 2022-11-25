# cljssss

A Battlesnake written in Clojure.


## Development

Run the web server:

```
nix develop --command clojure -M -m thewagner.cljssss.web
```

Start the game:

```
nix run .#battlesnake -- play --name cljssss --url http://localhost:8080 --viewmap
```
