{
  description = "A very basic flake";

  outputs = { self, nixpkgs }: {

    packages.x86_64-linux.battlesnake-cli =
      with import nixpkgs { system = "x86_64-linux"; };

      buildGoModule rec {
        pname = "battlesnake-cli";
        version = "2.0.0";

        src = fetchFromGitHub {
          owner = "BattleSnakeOfficial";
          repo = "rules";
          rev = "v${version}";
          sha256 = "sha256-Ly8/g3Q+ybKGOvyORoYm3YeVrHdgtGij0uFFYh9ex9w=";
        };

        vendorSha256 = "sha256-tGOxBhyOPwzguRZY4O2rLoOMaT3EyryjYcO2/2GnVIU=";
        subPackages = [ "cli/battlesnake" ];
      };

    apps.x86_64-linux.battlesnake = {
      type = "app";
      program = "${self.packages.x86_64-linux.battlesnake-cli}/bin/battlesnake";
    };
  };
}
