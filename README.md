# Jupiter

Jupiter is a powerful, auto sync config library.

**IMPORTANT NOTICE: Jupiter V2 is not compatible with mods which based on V1!**

## Features

1.Simple creation of config instance.

2.Config support `int`, `double`, `string`,`list` and so on.

3.Can set range for `int`, `double`

4.Automatically sync config with dedicate server.

5.Permission control for dedicate server config.

## How to use (For developer)

1.Create config class and extend `FileConfigContainer`.

2.Add config in `init`
method. ([Example](https://github.com/ArkTechMC/Jupiter/blob/master/common/src/main/java/com/iafenvoy/jupiter/test/TestConfig.java))

3.If your config is for server/common, register it with `ServerConfigManager.registerServerConfig`.

4.Create your config screen. There are 3 types of screen to select.

i.`ConfigSelectScreen`: Create a config select screen. User can select which config to edit. Include permission check.

ii.`ClientConfigScreen`: Create a client config edit screen.

iii.`ServerConfigScreen`: Create a server config edit screen. **Not include permission check.**

## Other Version

We will only update to newer versions when our other mods are needed. Request if you want other versions. (Except 1.16
and below.)

## Discord

https://discord.gg/NDzz2upqAk