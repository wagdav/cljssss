{
  description = "A Clojure Battlesnake";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-22.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        packages.battlesnake-cli =

          pkgs.buildGoModule rec {
            pname = "battlesnake-cli";
            version = "2.0.0";

            src = pkgs.fetchFromGitHub {
              owner = "BattleSnakeOfficial";
              repo = "rules";
              rev = "v${version}";
              sha256 = "sha256-Ly8/g3Q+ybKGOvyORoYm3YeVrHdgtGij0uFFYh9ex9w=";
            };

            vendorSha256 = "sha256-tGOxBhyOPwzguRZY4O2rLoOMaT3EyryjYcO2/2GnVIU=";
            subPackages = [ "cli/battlesnake" ];
          };

        apps.battlesnake = {
          type = "app";
          program = "${self.packages.x86_64-linux.battlesnake-cli}/bin/battlesnake";
        };

        devShell = pkgs.mkShell {
          buildInputs = with pkgs; [
            clojure
            clj-kondo
          ];
        };
      });
}
