# Soul Shards Despawn

![](https://github.com/0x00002a/Soul-Shards-Despawn/blob/b84d3609e99c6f5fc41979b8236c22450000b994/images/soul-shards-banner.png)

Adds balanced & configurable spawners for any mob, modded or vanilla!

It is **highly** recommended you use a mod like JEI (or REI) for exploring the recipes in this mod.

## Progression

This mod requires getting to the nether before it can be started.

### Getting started

To get started with this mod you will need to create a soul shard. You will need a diamond and a multiblock of the form:

![JEI crafting screen](https://github.com/0x00002a/Soul-Shards-Despawn/blob/b84d3609e99c6f5fc41979b8236c22450000b994/images/soul-shards-jei.png)

Once you have placed these blocks simply right-click the glowstone with the diamond in your hand.

### Getting souls

Now that you have a shard you need some souls to put in it. You can fill the shard by having it in your offhand or
hotbar when killing mobs. The first mob you kill will bind the shard to that mob type and from then on it will only
count kills of that type. You can craft it on its own to remove the binding (and kills).

There are 5 tiers of soul shard by default:
| Tier | Kills | Doesn't need nearby player | Ignores light level | Controllable via redstone | Spawn interval (
seconds) | Spawn amount |
| ---- | ----- | ---------------------- | ---------------------------- | ---------------------------- | ------------------------ | -------- |
| 1 | 64 | ❌ | ❌ | ❌ | 20 | 2 |
| 2 | 128 | ❌ | ❌ | ❌ | 10 | 4 |
| 3 | 256 |✔️| ❌ | ❌ | 5 | 4 |
| 4 | 512 |✔️|✔️| ❌ | 5 | 4 |
| 5 | 1024 |✔️|✔️|✔️| 2 | 6 |

To speed up the process there are a couple options available: the soul stealer enchantment, which increases souls
gained from each kill exponentially per level (i.e. level 3 gives you `2^3 = 8` extra souls per kill), and the cursed
sword which doubles all souls gained when a mob is killed with it. Additionally, the cursed sword has a much higher
affinity for the soul stealer enchantment, and is more than 10x more likely roll it when enchanting.

### Spawning mobs

OK so now you've got your shard, filled with the ~~screams of your victims you monster~~ souls of a mob. The next step
is crafting a soul cage. Once you have a cage, simply right click it with your shard and you can kick back and relax
while hords of monsters appear out of thin air (unless you have a tier that needs a nearby player, then you need to stay
within 16 blocks).

If you have a tier with redstone support then you can power the cage to stop it spawning.

## History

In case you are not familer with the history of soul shards, allow me to give you a rundown: This mod is an update
of [Soul Shards Respawn by TehNut](https://github.com/TehNut-Mods/Soul-Shards-Respawn) which is an update
of [Soul Shards: The Old Ways by Team Whammich](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2329877-soul-shards-the-old-ways-rc9-update)
which is an update
of  [Soul Shards: Reborn by Moze_Intel](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/wip-mods/1445947-1-7-10-soul-shards-reborn-original-soul-shards)
which is finally an update of the 1.4.7
mod [Soul Shards](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1285901-1-6-4-forgeirc-v1-0-18-soul-shards-v2-0-15-and#soulshards).

I forked Respawn because it was not updated past 1.15 and the repository has been archived. I ported the 1.15 fabric
version to the mojang mappings and 1.19 (also adding back in features from 1.12.2 that were removed in the 1.15 version)

## Thanks and credits

Some of the art in this mod was done by the wonderful [divisibledeer](https://modrinth.com/user/divisibledeer),
specifically:

- Corrupted essence sprite
- Vile sword frame sprite
- Cursed sword sprite
- Hammer and quartz sprite
- Corrupted ingot sprite

