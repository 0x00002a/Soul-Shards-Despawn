# Soul Shards Despawn

Ever wanted to create your own mob spawners? Now you can!

## Information

This is a fan continuation of the popular 1.4.7
mod, [Soul Shards](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1285901-1-6-4-forgeirc-v1-0-18-soul-shards-v2-0-15-and#soulshards).

This version of the mod is based on the sources
of [Soul Shards Respawn](https://github.com/TehNut-Mods/Soul-Shards-Respawn), which itself is based
on [Soul Shards: Reborn by Moze_Intel](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/wip-mods/1445947-1-7-10-soul-shards-reborn-original-soul-shards)
and [Soul Shards: The Old Ways by Team Whammich](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2329877-soul-shards-the-old-ways-rc9-update).

This version is an update on the original with slight progression changes.

## License

Soul Shards Despawn is licensed under the [MIT](https://tldrlegal.com/license/mit-license) license.

The art is a little more complex.

All the art from Soul Shards Respawn is property of [BBoldt](https://github.com/BBoldt/). The art is released into the
public domain. This art is:

- `icon.png`
- `soul_cage.png`
- `soul_cage_active.png`
- `corrupted_essence.png`
- `corrupted_ingot.png`
- `soul_shard.png`
- `soul_shard_tier_n.png`
- `vile_dust.png`
- `vile_sword.png`
- Banner image + icon

I (0x00002a) have also modified some of this art to create new assets:

- `vile_sword_base.png`: `vile_sword.png`

Additionally, there are my entirely my own work assets in here:

- `quartz_and_steel.png`

My own assets and my modifications to public domain assets are CC0 (public domain).

And then there are modified versions of the vanilla assets (recolours and such). I am not entirely sure the licensing on
these but I believe it is
allowed as I am not charging for them:

- `cursed_fire_n.png`
- `hallowed_fire_n.png`

## Custom Builds

**Custom builds are *unsupported*. If you have an issue while using an unofficial build, it is not guaranteed that you
will get support.**

### How to make a custom build:

1. Clone directly from this repository to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew build`
3. Once it completes, your new build will be found at `../build/libs/SoulShards-TOW-*.jar`. You can ignore
   the `api`, `sources`, and `javadoc` jars.