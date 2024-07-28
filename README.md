# Jupiter

Jupiter is a powerful, auto sync config library.

## Features

1.Simple creation of config instance.

2.Config support `int`, `double`, `string`, `color`, `list` and so on.

3.Can set range for `int`, `double`

4.Automatically sync config with dedicate server.

5.Permission control for dedicate server config.

## Credit

`Masa`: Provide gui code from `malilib` under `LGPL-3.0` license.

## How to use (For developer)

1.Create config class and extend `FileConfigContainer`.

2.Add config in `init` method. ([Example](https://github.com/ArkTechMC/Jupiter/blob/master/common/src/main/java/com/iafenvoy/jupiter/test/TestConfig.java))

3.If your config is for server/common, register it with `ServerConfigManager.registerServerConfig`.

4.Create your config screen. There are 3 types of screen to select.

i.`ConfigSelectScreen`: Create a config select screen. User can select which config to edit. Include permission check.

ii.`ClientConfigScreen`: Create a client config edit screen.

iii.`ServerConfigScreen`: Create a server config edit screen. **Not include permission check.**