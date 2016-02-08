# laogai

> The Earth King invites you to Lake Laogai...

![Lake Laogai](https://raw.githubusercontent.com/niamu/laogai/master/laogai.gif)

Laogai is a Clojure program designed to automate a home theatre experience by controlling the lights and the TV in the room.

Assumptions for this program:

- Philips Hue Lights
- Raspberry Pi
- Plex server on the network and Plex client on the Raspberry Pi
- CEC capable TV where the Raspberry Pi is connected to

Turning on the light switch in the room will make the lights `:reachable` and this turns on the TV and AVR via SSH commands to the Raspberry Pi which has `cec-client` installed and running. Turning the light switch off again will turn the TV off, but only if the Plex client is not currently watching anything.

This method works for me as the only time I ever turn on the lights in the room is to sit down to watch TV and won't control the lights through an app at any other point. Having the lights disconnected from the network at all other times may not work for you and you'll have to find some alternative logic.

When the Plex client begins to watch something, the lights automatically fade off. Finishing or stopping a show will cause the lights to gradually fade on again. Pausing a show will bring the lights up to a very dim setting.

These behaviours match my needs, but hopefully `core.clj` remains relatively simple so modifying behaviours to meet your own needs is painless.

## Usage

### Plex

You'll want to find out the name of the Plex client that exists on the computer/Raspberry Pi that is connected to the TV. Also find the hostname/IP of the server that runs Plex and place all of that in `config.edn`.

### Raspberry Pi Setup

I presonally run the excellent [RasPlex](http://rasplex.com) distribution on my Raspberry Pi. This comes with `cec-client` installed already and runs Plex beautifully.

Place the `tv.sh` Bash script in the home directory of the Raspberry Pi and note the hostname/IP address in `config.edn`.

### Philips Hue

You'll need to create a new user on your Philips Hue bridge. Documentation for how to do that will follow here at a later date. For now, I'd recommend following the excellent [clhue](https://github.com/Raynes/clhue) library on how to do this.

Get the ID of each light you wish to control. For now I'd also recommend doing this by following along with [clhue](https://github.com/Raynes/clhue).

You'll also need to find the hostname/IP of the bridge and note all of this in the `config.edn` file.

### Running

Finally, with your completed `config.edn` file you are ready to execute `lein run` or otherwise compile an uberjar and setup a system daemon on your server to automate your home theater experience.

Enjoy!

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
