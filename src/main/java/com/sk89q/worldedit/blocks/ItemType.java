// $Id$
/*
 * WorldEdit
 * Copyright (C) 2010 sk89q <http://www.sk89q.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.sk89q.worldedit.blocks;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.sk89q.util.StringUtil;

/**
 * ItemType types.
 *
 * @author sk89q
 */
public enum ItemType {
    // Blocks
    AIR(BlockID.AIR, "Воздух", "air"),
    STONE(BlockID.STONE, "Камень", "stone", "rock"),
    GRASS(BlockID.GRASS, "Блок травы", "grass"),
    DIRT(BlockID.DIRT, "Земля", "dirt"),
    COBBLESTONE(BlockID.COBBLESTONE, "Булыжник", "cobblestone", "cobble"),
    WOOD(BlockID.WOOD, "Доски", "wood", "woodplank", "plank", "woodplanks", "planks"),
    SAPLING(BlockID.SAPLING, "Саженец", "sapling", "seedling"),
    BEDROCK(BlockID.BEDROCK, "Коренная порода", "adminium", "bedrock"),
    WATER(BlockID.WATER, "Вода", "watermoving", "movingwater", "flowingwater", "waterflowing"),
    STATIONARY_WATER(BlockID.STATIONARY_WATER, "Вода (стоячая)", "water", "waterstationary", "stationarywater", "stillwater"),
    LAVA(BlockID.LAVA, "Лава", "lavamoving", "movinglava", "flowinglava", "lavaflowing"),
    STATIONARY_LAVA(BlockID.STATIONARY_LAVA, "Лава (стоячая)", "lava", "lavastationary", "stationarylava", "stilllava"),
    SAND(BlockID.SAND, "Песок", "sand"),
    GRAVEL(BlockID.GRAVEL, "Гравий", "gravel"),
    GOLD_ORE(BlockID.GOLD_ORE, "Золотая руда", "goldore"),
    IRON_ORE(BlockID.IRON_ORE, "Железная руда", "ironore"),
    COAL_ORE(BlockID.COAL_ORE, "Угольная руда", "coalore"),
    LOG(BlockID.LOG, "Дерево", "log", "tree", "pine", "oak", "birch", "redwood"),
    LEAVES(BlockID.LEAVES, "Листья", "leaves", "leaf"),
    SPONGE(BlockID.SPONGE, "Губка", "sponge"),
    GLASS(BlockID.GLASS, "Стекло", "glass"),
    LAPIS_LAZULI_ORE(BlockID.LAPIS_LAZULI_ORE, "Лазуритовая руда", "lapislazuliore", "blueore", "lapisore"),
    LAPIS_LAZULI(BlockID.LAPIS_LAZULI_BLOCK, "Блок лазурита", "lapislazuli", "lapislazuliblock", "bluerock"),
    DISPENSER(BlockID.DISPENSER, "Раздатчик", "dispenser"),
    SANDSTONE(BlockID.SANDSTONE, "Песчаник", "sandstone"),
    NOTE_BLOCK(BlockID.NOTE_BLOCK, "Нотный блок", "musicblock", "noteblock", "note", "music", "instrument"),
    BED(BlockID.BED, "Кровать", "bed"),
    POWERED_RAIL(BlockID.POWERED_RAIL, "Электрические рельсы", "poweredrail", "boosterrail", "poweredtrack", "boostertrack", "booster"),
    DETECTOR_RAIL(BlockID.DETECTOR_RAIL, "Нажимные рельсы", "detectorrail", "detector"),
    PISTON_STICKY_BASE(BlockID.PISTON_STICKY_BASE, "Липкий поршень", "stickypiston"),
    WEB(BlockID.WEB, "Паутина", "web", "spiderweb"),
    LONG_GRASS(BlockID.LONG_GRASS, "Высокая трава", "longgrass", "tallgrass"),
    DEAD_BUSH(BlockID.DEAD_BUSH, "Куст", "deadbush", "shrub", "deadshrub", "tumbleweed"),
    PISTON_BASE(BlockID.PISTON_BASE, "Поршень", "piston"),
    PISTON_EXTENSION(BlockID.PISTON_EXTENSION, "Выдвинутый поршень", "pistonextendsion", "pistonhead"),
    CLOTH(BlockID.CLOTH, "Шерсть", "cloth", "wool"),
    PISTON_MOVING_PIECE(BlockID.PISTON_MOVING_PIECE, "Piston moving piece", "movingpiston"),
    YELLOW_FLOWER(BlockID.YELLOW_FLOWER, "Желтый цветок", "yellowflower", "flower"),
    RED_FLOWER(BlockID.RED_FLOWER, "Красная роза", "redflower", "redrose", "rose"),
    BROWN_MUSHROOM(BlockID.BROWN_MUSHROOM, "Коричневый гриб", "brownmushroom", "mushroom"),
    RED_MUSHROOM(BlockID.RED_MUSHROOM, "Красный гриб", "redmushroom"),
    GOLD_BLOCK(BlockID.GOLD_BLOCK, "Золотой блок", "gold", "goldblock"),
    IRON_BLOCK(BlockID.IRON_BLOCK, "Железный блок", "iron", "ironblock"),
    DOUBLE_STEP(BlockID.DOUBLE_STEP, "Двойная плита", "doubleslab", "doublestoneslab", "doublestep"),
    STEP(BlockID.STEP, "Каменная плита", "slab", "stoneslab", "step", "halfstep"),
    BRICK(BlockID.BRICK, "Кирпич", "brick", "brickblock"),
    TNT(BlockID.TNT, "Динамит", "tnt", "c4", "explosive"),
    BOOKCASE(BlockID.BOOKCASE, "Книжная полка", "bookshelf", "bookshelves", "bookcase", "bookcases"),
    MOSSY_COBBLESTONE(BlockID.MOSSY_COBBLESTONE, "Зашмелый булыжника", "mossycobblestone", "mossstone", "mossystone", "mosscobble", "mossycobble", "moss", "mossy", "sossymobblecone"),
    OBSIDIAN(BlockID.OBSIDIAN, "Обсидиан", "obsidian"),
    TORCH(BlockID.TORCH, "Факел", "torch", "light", "candle"),
    FIRE(BlockID.FIRE, "Огонь", "fire", "flame", "flames"),
    MOB_SPAWNER(BlockID.MOB_SPAWNER, "Рассадник монстров", "mobspawner", "spawner"),
    WOODEN_STAIRS(BlockID.WOODEN_STAIRS, "Деревянная ступенька", "woodstair", "woodstairs", "woodenstair", "woodenstairs"),
    CHEST(BlockID.CHEST, "Сундук", "chest", "storage", "storagechest"),
    REDSTONE_WIRE(BlockID.REDSTONE_WIRE, "Красная пыль", "redstone", "redstoneblock"),
    DIAMOND_ORE(BlockID.DIAMOND_ORE, "Алмазная руда", "diamondore"),
    DIAMOND_BLOCK(BlockID.DIAMOND_BLOCK, "Алмазный блок", "diamond", "diamondblock"),
    WORKBENCH(BlockID.WORKBENCH, "Верстак", "workbench", "table", "craftingtable", "crafting"),
    CROPS(BlockID.CROPS, "Семена", "crops", "crop", "plant", "plants"),
    SOIL(BlockID.SOIL, "Грядка", "soil", "farmland"),
    FURNACE(BlockID.FURNACE, "Печь", "furnace"),
    BURNING_FURNACE(BlockID.BURNING_FURNACE, "Печь (горящяя)", "burningfurnace", "litfurnace"),
    SIGN_POST(BlockID.SIGN_POST, "Табличка", "sign", "signpost"),
    WOODEN_DOOR(BlockID.WOODEN_DOOR, "Деревянная дверь", "wooddoor", "woodendoor", "door"),
    LADDER(BlockID.LADDER, "Лестница", "ladder"),
    MINECART_TRACKS(BlockID.MINECART_TRACKS, "Рельсы", "track", "tracks", "minecrattrack", "minecarttracks", "rails", "rail"),
    COBBLESTONE_STAIRS(BlockID.COBBLESTONE_STAIRS, "Булыжниковая ступенька", "cobblestonestair", "cobblestonestairs", "cobblestair", "cobblestairs"),
    WALL_SIGN(BlockID.WALL_SIGN, "Настенная табличка", "wallsign"),
    LEVER(BlockID.LEVER, "Рычаг", "lever", "switch", "stonelever", "stoneswitch"),
    STONE_PRESSURE_PLATE(BlockID.STONE_PRESSURE_PLATE, "Нажимная плита", "stonepressureplate", "stoneplate"),
    IRON_DOOR(BlockID.IRON_DOOR, "Железная дверь", "irondoor"),
    WOODEN_PRESSURE_PLATE(BlockID.WOODEN_PRESSURE_PLATE, "Деревянная плита", "woodpressureplate", "woodplate", "woodenpressureplate", "woodenplate", "plate", "pressureplate"),
    REDSTONE_ORE(BlockID.REDSTONE_ORE, "Руда красного камня", "redstoneore"),
    GLOWING_REDSTONE_ORE(BlockID.GLOWING_REDSTONE_ORE, "Светящияся руда красного камня", "glowingredstoneore"),
    REDSTONE_TORCH_OFF(BlockID.REDSTONE_TORCH_OFF, "Крансый факел (выкл)", "redstonetorchoff", "rstorchoff"),
    REDSTONE_TORCH_ON(BlockID.REDSTONE_TORCH_ON, "Красный факел (вкл)", "redstonetorch", "redstonetorchon", "rstorchon", "redtorch"),
    STONE_BUTTON(BlockID.STONE_BUTTON, "Кнопка", "stonebutton", "button"),
    SNOW(BlockID.SNOW, "Снег", "snow"),
    ICE(BlockID.ICE, "Лёд", "ice"),
    SNOW_BLOCK(BlockID.SNOW_BLOCK, "Снежный блок", "snowblock"),
    CACTUS(BlockID.CACTUS, "Кактус", "cactus", "cacti"),
    CLAY(BlockID.CLAY, "Глина", "clay"),
    SUGAR_CANE(BlockID.REED, "Тростник", "reed", "cane", "sugarcane", "sugarcanes", "vine", "vines"),
    JUKEBOX(BlockID.JUKEBOX, "Проигрыватель", "jukebox", "stereo", "recordplayer"),
    FENCE(BlockID.FENCE, "Забор", "fence"),
    PUMPKIN(BlockID.PUMPKIN, "Тыква", "pumpkin"),
    NETHERRACK(BlockID.NETHERRACK, "Адский камень", "redmossycobblestone", "redcobblestone", "redmosstone", "redcobble", "netherstone", "netherrack", "nether", "hellstone"),
    SOUL_SAND(BlockID.SLOW_SAND, "Песок душ", "slowmud", "mud", "soulsand", "hellmud"),
    GLOWSTONE(BlockID.LIGHTSTONE, "Светокамень", "brittlegold", "glowstone", "lightstone", "brimstone", "australium"),
    PORTAL(BlockID.PORTAL, "Портал", "portal"),
    JACK_O_LANTERN(BlockID.JACKOLANTERN, "Светильник джека", "pumpkinlighted", "pumpkinon", "litpumpkin", "jackolantern"),
    CAKE(BlockID.CAKE_BLOCK, "Торт", "cake", "cakeblock"),
    REDSTONE_REPEATER_OFF(BlockID.REDSTONE_REPEATER_OFF, "Повторитель (выкл)", "diodeoff", "redstonerepeater", "repeateroff", "delayeroff"),
    REDSTONE_REPEATER_ON(BlockID.REDSTONE_REPEATER_ON, "Повторитель (вкл)", "diodeon", "redstonerepeateron", "repeateron", "delayeron"),
    LOCKED_CHEST(BlockID.LOCKED_CHEST, "Запертый сундук", "lockedchest", "steveco", "supplycrate", "valveneedstoworkonep3nottf2kthx"),
    TRAP_DOOR(BlockID.TRAP_DOOR, "Люк", "trapdoor", "hatch", "floordoor"),
    SILVERFISH_BLOCK(BlockID.SILVERFISH_BLOCK, "Камень с монстрами", "silverfish", "silver"),
    STONE_BRICK(BlockID.STONE_BRICK, "Каменный кирпич", "stonebrick", "sbrick", "smoothstonebrick"),
    RED_MUSHROOM_CAP(BlockID.RED_MUSHROOM_CAP, "Красный гриб", "giantmushroomred", "redgiantmushroom", "redmushroomcap"),
    BROWN_MUSHROOM_CAP(BlockID.BROWN_MUSHROOM_CAP, "Коричневый гриб", "giantmushroombrown", "browngiantmushoom", "brownmushroomcap"),
    IRON_BARS(BlockID.IRON_BARS, "Железная решетка", "ironbars", "ironfence"),
    GLASS_PANE(BlockID.GLASS_PANE, "Стеклянная панель", "window", "glasspane", "glasswindow"),
    MELON_BLOCK(BlockID.MELON_BLOCK, "Арбуз (блок)", "melonblock"),
    PUMPKIN_STEM(BlockID.PUMPKIN_STEM, "Росток тыквы", "pumpkinstem"),
    MELON_STEM(BlockID.MELON_STEM, "Росток арбуза", "melonstem"),
    VINE(BlockID.VINE, "Лиана", "vine", "vines", "creepers"),
    FENCE_GATE(BlockID.FENCE_GATE, "Калитка", "fencegate", "gate"),
    BRICK_STAIRS(BlockID.BRICK_STAIRS, "Ступенька из кирпича", "brickstairs", "bricksteps"),
    STONE_BRICK_STAIRS(BlockID.STONE_BRICK_STAIRS, "Ступенька из каменного кирпича", "stonebrickstairs", "smoothstonebrickstairs"),
    MYCELIUM(BlockID.MYCELIUM, "Мицелий", "fungus", "mycel"),
    LILY_PAD(BlockID.LILY_PAD, "Кувшинка", "lilypad", "waterlily"),
    NETHER_BRICK(BlockID.NETHER_BRICK, "Адский кирпич", "netherbrick"),
    NETHER_BRICK_FENCE(BlockID.NETHER_BRICK_FENCE, "Адский забор", "netherbrickfence", "netherfence"),
    NETHER_BRICK_STAIRS(BlockID.NETHER_BRICK_STAIRS, "Ступеньки из адского кирпича", "netherbrickstairs", "netherbricksteps", "netherstairs", "nethersteps"),
    NETHER_WART(BlockID.NETHER_WART, "Адский нарост", "netherwart", "netherstalk"),
    ENCHANTMENT_TABLE(BlockID.ENCHANTMENT_TABLE, "Стол зачарований", "enchantmenttable", "enchanttable"),
    BREWING_STAND(BlockID.BREWING_STAND, "Варочная стойка", "brewingstand"),
    CAULDRON(BlockID.CAULDRON, "Котел"),
    END_PORTAL(BlockID.END_PORTAL, "Портал в край", "endportal", "blackstuff", "airportal", "weirdblackstuff"),
    END_PORTAL_FRAME(BlockID.END_PORTAL_FRAME, "Портал в Энд", "endportalframe", "airportalframe", "crystalblock"),
    END_STONE(BlockID.END_STONE, "Камень Края", "endstone", "enderstone", "endersand"),
    DRAGON_EGG(BlockID.DRAGON_EGG, "Яйцо дракона", "dragonegg", "dragons"),
    REDSTONE_LAMP_OFF(BlockID.REDSTONE_LAMP_OFF, "Лампа (выкл)", "redstonelamp", "redstonelampoff", "rslamp", "rslampoff", "rsglow", "rsglowoff"),
    REDSTONE_LAMP_ON(BlockID.REDSTONE_LAMP_ON, "Лама (вкл)", "redstonelampon", "rslampon", "rsglowon"),
    DOUBLE_WOODEN_STEP(BlockID.DOUBLE_WOODEN_STEP, "Двойная деревянная ступенька", "doublewoodslab", "doublewoodstep"),
    WOODEN_STEP(BlockID.WOODEN_STEP, "Деревянная ступенька", "woodenslab", "woodslab", "woodstep", "woodhalfstep"),
    COCOA_PLANT(BlockID.COCOA_PLANT, "Какао бобы", "cocoplant", "cocoaplant"),
    SANDSTONE_STAIRS(BlockID.SANDSTONE_STAIRS, "Ступенька из песчаника", "sandstairs", "sandstonestairs"),
    EMERALD_ORE(BlockID.EMERALD_ORE, "Изумрудная руда", "emeraldore"),
    ENDER_CHEST(BlockID.ENDER_CHEST, "Эндерсундук", "enderchest"),
    TRIPWIRE_HOOK(BlockID.TRIPWIRE_HOOK, "Крюк", "tripwirehook"),
    TRIPWIRE(BlockID.TRIPWIRE, "Крюк", "tripwire", "string"),
    EMERALD_BLOCK(BlockID.EMERALD_BLOCK, "Изумрудный блок", "emeraldblock", "emerald"),
    SPRUCE_WOOD_STAIRS(BlockID.SPRUCE_WOOD_STAIRS, "Ступеньки из сосновой древесины", "sprucestairs", "sprucewoodstairs"),
    BIRCH_WOOD_STAIRS(BlockID.BIRCH_WOOD_STAIRS, "Ступеньки из древесины берёзы", "birchstairs", "birchwoodstairs"),
    JUNGLE_WOOD_STAIRS(BlockID.JUNGLE_WOOD_STAIRS, "Ступеньки из тропической древесины", "junglestairs", "junglewoodstairs"),
    COMMAND_BLOCK(BlockID.COMMAND_BLOCK, "Командный блок", "commandblock", "cmdblock", "command", "cmd"),
    BEACON(BlockID.BEACON, "Маяк", "beacon", "beaconblock"),
    COBBLESTONE_WALL(BlockID.COBBLESTONE_WALL, "Стена из булыжника", "cobblestonewall", "cobblewall"),
    FLOWER_POT_BLOCK(BlockID.FLOWER_POT, "Цветочный горшок", "flowerpot", "plantpot", "pot", "flowerpotblock"),
    CARROTS_BLOCK(BlockID.CARROTS, "Морковь", "carrots", "carrotsplant", "carrotsblock"),
    POTATOES_BLOCK(BlockID.POTATOES, "Картофель", "patatoes", "potatoesblock"),
    WOODEN_BUTTON(BlockID.WOODEN_BUTTON, "Деревянная кнопка", "woodbutton", "woodenbutton"),
    HEAD_BLOCK(BlockID.HEAD, "Голова", "head", "headmount", "mount", "headblock", "mountblock"),
    ANVIL(BlockID.ANVIL, "Наковальня", "anvil", "blacksmith"),
    TRAPPED_CHEST(BlockID.TRAPPED_CHEST, "Trapped Chest", "trappedchest", "redstonechest"),
    PRESSURE_PLATE_LIGHT(BlockID.PRESSURE_PLATE_LIGHT, "Weighted Pressure Plate (Light)", "lightpressureplate"),
    PRESSURE_PLATE_HEAVY(BlockID.PRESSURE_PLATE_HEAVY, "Weighted Pressure Plate (Heavy)", "heavypressureplate"),
    COMPARATOR_OFF(BlockID.COMPARATOR_OFF, "Redstone Comparator (inactive)", "redstonecomparator", "comparator"),
    COMPARATOR_ON(BlockID.COMPARATOR_ON, "Redstone Comparator (active)", "redstonecomparatoron", "comparatoron"),
    DAYLIGHT_SENSOR(BlockID.DAYLIGHT_SENSOR, "Daylight Sesnor", "daylightsensor", "lightsensor"),
    REDSTONE_BLOCK(BlockID.REDSTONE_BLOCK, "Block of Redstone", "redstoneblock", "blockofredstone"),
    QUARTZ_ORE(BlockID.QUARTZ_ORE, "Nether Quartz Ore", "quartzore", "netherquartzore"),
    HOPPER(BlockID.HOPPER, "Hopper", "hopper"),
    QUARTZ_BLOCK(BlockID.QUARTZ_BLOCK, "Block of Quartz", "quartzblock"),
    QUARTZ_STAIRS(BlockID.QUARTZ_STAIRS, "Quartz Stairs", "quartzstairs"),
    ACTIVATOR_RAIL(BlockID.ACTIVATOR_RAIL, "Activator Rail", "activatorrail", "tntrail", "activatortrack"),
    DROPPER(BlockID.DROPPER, "Dropper", "dropper"),
    STAINED_CLAY(BlockID.STAINED_CLAY, "Stained clay", "stainedclay"),
    HAY_BLOCK(BlockID.HAY_BLOCK, "Hay Block", "hayblock", "haybale", "wheatbale"),
    CARPET(BlockID.CARPET, "Carpet", "carpet"),
    HARDENED_CLAY(BlockID.HARDENED_CLAY, "Hardened Clay", "hardenedclay", "hardclay"),
    COAL_BLOCK(BlockID.COAL_BLOCK, "Block of Coal", "coalblock", "blockofcoal"),

    // Items
    IRON_SHOVEL(ItemID.IRON_SHOVEL, "Железная лопата", "ironshovel"),
    IRON_PICK(ItemID.IRON_PICK, "Железная кирка", "ironpick", "ironpickaxe"),
    IRON_AXE(ItemID.IRON_AXE, "Железный топор", "ironaxe"),
    FLINT_AND_TINDER(ItemID.FLINT_AND_TINDER, "Огниво", "flintandtinder", "lighter", "flintandsteel", "flintsteel", "flintandiron", "flintnsteel", "flintniron", "flintntinder"),
    RED_APPLE(ItemID.RED_APPLE, "Яблоко", "redapple", "apple"),
    BOW(ItemID.BOW, "Лук", "bow"),
    ARROW(ItemID.ARROW, "Стрела", "arrow"),
    COAL(ItemID.COAL, "Уголь", "coal"),
    DIAMOND(ItemID.DIAMOND, "Алмаз", "diamond"),
    IRON_BAR(ItemID.IRON_BAR, "Железный слиток", "ironbar", "iron"),
    GOLD_BAR(ItemID.GOLD_BAR, "Золотой слиток", "goldbar", "gold"),
    IRON_SWORD(ItemID.IRON_SWORD, "Железный меч", "ironsword"),
    WOOD_SWORD(ItemID.WOOD_SWORD, "Деревянный меч", "woodsword"),
    WOOD_SHOVEL(ItemID.WOOD_SHOVEL, "Деревянная лопата", "woodshovel"),
    WOOD_PICKAXE(ItemID.WOOD_PICKAXE, "Деревянная кирка", "woodpick", "woodpickaxe"),
    WOOD_AXE(ItemID.WOOD_AXE, "Деревянный топором", "woodaxe"),
    STONE_SWORD(ItemID.STONE_SWORD, "Каменный меч", "stonesword"),
    STONE_SHOVEL(ItemID.STONE_SHOVEL, "Каменная лопата", "stoneshovel"),
    STONE_PICKAXE(ItemID.STONE_PICKAXE, "Каменная кирка", "stonepick", "stonepickaxe"),
    STONE_AXE(ItemID.STONE_AXE, "Каменный топор", "stoneaxe"),
    DIAMOND_SWORD(ItemID.DIAMOND_SWORD, "Алмазный меч", "diamondsword"),
    DIAMOND_SHOVEL(ItemID.DIAMOND_SHOVEL, "Алмазная лопата", "diamondshovel"),
    DIAMOND_PICKAXE(ItemID.DIAMOND_PICKAXE, "Алмазная кирка", "diamondpick", "diamondpickaxe"),
    DIAMOND_AXE(ItemID.DIAMOND_AXE, "Алмазный топор", "diamondaxe"),
    STICK(ItemID.STICK, "Палка", "stick"),
    BOWL(ItemID.BOWL, "Миска", "bowl"),
    MUSHROOM_SOUP(ItemID.MUSHROOM_SOUP, "Грибной суп", "mushroomsoup", "soup", "brbsoup"),
    GOLD_SWORD(ItemID.GOLD_SWORD, "Золотой меч", "goldsword"),
    GOLD_SHOVEL(ItemID.GOLD_SHOVEL, "Золотая лопата", "goldshovel"),
    GOLD_PICKAXE(ItemID.GOLD_PICKAXE, "Золотая кирка", "goldpick", "goldpickaxe"),
    GOLD_AXE(ItemID.GOLD_AXE, "Золотой топор", "goldaxe"),
    STRING(ItemID.STRING, "Нить", "string"),
    FEATHER(ItemID.FEATHER, "Перо", "feather"),
    SULPHUR(ItemID.SULPHUR, "Порох", "sulphur", "sulfur", "gunpowder"),
    WOOD_HOE(ItemID.WOOD_HOE, "Деревянная мотыга", "woodhoe"),
    STONE_HOE(ItemID.STONE_HOE, "Каменная мотыга", "stonehoe"),
    IRON_HOE(ItemID.IRON_HOE, "Железная мотыга", "ironhoe"),
    DIAMOND_HOE(ItemID.DIAMOND_HOE, "Алмазная мотыга", "diamondhoe"),
    GOLD_HOE(ItemID.GOLD_HOE, "Золотая мотыга", "goldhoe"),
    SEEDS(ItemID.SEEDS, "Семена", "seeds", "seed"),
    WHEAT(ItemID.WHEAT, "Пшеница", "wheat"),
    BREAD(ItemID.BREAD, "Хлеб", "bread"),
    LEATHER_HELMET(ItemID.LEATHER_HELMET, "Кожаный шлем", "leatherhelmet", "leatherhat"),
    LEATHER_CHEST(ItemID.LEATHER_CHEST, "Кожаный нагрудник", "leatherchest", "leatherchestplate", "leathervest", "leatherbreastplate", "leatherplate", "leathercplate", "leatherbody"),
    LEATHER_PANTS(ItemID.LEATHER_PANTS, "Кожаные штаны", "leatherpants", "leathergreaves", "leatherlegs", "leatherleggings", "leatherstockings", "leatherbreeches"),
    LEATHER_BOOTS(ItemID.LEATHER_BOOTS, "Кожаные ботинки", "leatherboots", "leathershoes", "leatherfoot", "leatherfeet"),
    CHAINMAIL_HELMET(ItemID.CHAINMAIL_HELMET, "Кольчужный шлем", "chainmailhelmet", "chainmailhat"),
    CHAINMAIL_CHEST(ItemID.CHAINMAIL_CHEST, "Кольчужный нагрудник", "chainmailchest", "chainmailchestplate", "chainmailvest", "chainmailbreastplate", "chainmailplate", "chainmailcplate", "chainmailbody"),
    CHAINMAIL_PANTS(ItemID.CHAINMAIL_PANTS, "Кольчужные штаны", "chainmailpants", "chainmailgreaves", "chainmaillegs", "chainmailleggings", "chainmailstockings", "chainmailbreeches"),
    CHAINMAIL_BOOTS(ItemID.CHAINMAIL_BOOTS, "Кольчужные ботинки", "chainmailboots", "chainmailshoes", "chainmailfoot", "chainmailfeet"),
    IRON_HELMET(ItemID.IRON_HELMET, "Железный шлем", "ironhelmet", "ironhat"),
    IRON_CHEST(ItemID.IRON_CHEST, "Железный нагрудник", "ironchest", "ironchestplate", "ironvest", "ironbreastplate", "ironplate", "ironcplate", "ironbody"),
    IRON_PANTS(ItemID.IRON_PANTS, "Железные штаны", "ironpants", "irongreaves", "ironlegs", "ironleggings", "ironstockings", "ironbreeches"),
    IRON_BOOTS(ItemID.IRON_BOOTS, "Железные ботинки", "ironboots", "ironshoes", "ironfoot", "ironfeet"),
    DIAMOND_HELMET(ItemID.DIAMOND_HELMET, "Алмазный шлем", "diamondhelmet", "diamondhat"),
    DIAMOND_CHEST(ItemID.DIAMOND_CHEST, "Алмазный нагрудник", "diamondchest", "diamondchestplate", "diamondvest", "diamondbreastplate", "diamondplate", "diamondcplate", "diamondbody"),
    DIAMOND_PANTS(ItemID.DIAMOND_PANTS, "Алмазные штаны", "diamondpants", "diamondgreaves", "diamondlegs", "diamondleggings", "diamondstockings", "diamondbreeches"),
    DIAMOND_BOOTS(ItemID.DIAMOND_BOOTS, "Алмазные ботинки", "diamondboots", "diamondshoes", "diamondfoot", "diamondfeet"),
    GOLD_HELMET(ItemID.GOLD_HELMET, "Золотой шлем", "goldhelmet", "goldhat"),
    GOLD_CHEST(ItemID.GOLD_CHEST, "Золотой нагрудник", "goldchest", "goldchestplate", "goldvest", "goldbreastplate", "goldplate", "goldcplate", "goldbody"),
    GOLD_PANTS(ItemID.GOLD_PANTS, "Золотые штаны", "goldpants", "goldgreaves", "goldlegs", "goldleggings", "goldstockings", "goldbreeches"),
    GOLD_BOOTS(ItemID.GOLD_BOOTS, "Золотые ботинки", "goldboots", "goldshoes", "goldfoot", "goldfeet"),
    FLINT(ItemID.FLINT, "Кремень", "flint"),
    RAW_PORKCHOP(ItemID.RAW_PORKCHOP, "Сырая свинина", "rawpork", "rawporkchop", "rawbacon", "baconstrips", "rawmeat"),
    COOKED_PORKCHOP(ItemID.COOKED_PORKCHOP, "Жареная свинина", "pork", "cookedpork", "cookedporkchop", "cookedbacon", "bacon", "meat"),
    PAINTING(ItemID.PAINTING, "Картина", "painting"),
    GOLD_APPLE(ItemID.GOLD_APPLE, "Золотое яблоко", "goldapple", "goldenapple"),
    SIGN(ItemID.SIGN, "Табличка", "sign"),
    WOODEN_DOOR_ITEM(ItemID.WOODEN_DOOR_ITEM, "Деревянная дверь", "wooddoor", "door"),
    BUCKET(ItemID.BUCKET, "Ведро", "bucket", "bukkit"),
    WATER_BUCKET(ItemID.WATER_BUCKET, "Ведро с водой", "waterbucket", "waterbukkit"),
    LAVA_BUCKET(ItemID.LAVA_BUCKET, "Ведро с лавой", "lavabucket", "lavabukkit"),
    MINECART(ItemID.MINECART, "Вагонетка", "minecart", "cart"),
    SADDLE(ItemID.SADDLE, "Седло", "saddle"),
    IRON_DOOR_ITEM(ItemID.IRON_DOOR_ITEM, "Железная дверь", "irondoor"),
    REDSTONE_DUST(ItemID.REDSTONE_DUST, "Красная пыль", "redstonedust", "reddust", "redstone", "dust", "wire"),
    SNOWBALL(ItemID.SNOWBALL, "Снежок", "snowball"),
    WOOD_BOAT(ItemID.WOOD_BOAT, "Лодка", "woodboat", "woodenboat", "boat"),
    LEATHER(ItemID.LEATHER, "Кожа", "leather", "cowhide"),
    MILK_BUCKET(ItemID.MILK_BUCKET, "Ведро с молоком", "milkbucket", "milk", "milkbukkit"),
    BRICK_BAR(ItemID.BRICK_BAR, "Кирпич", "brickbar"),
    CLAY_BALL(ItemID.CLAY_BALL, "Глина", "clay"),
    SUGAR_CANE_ITEM(ItemID.SUGAR_CANE_ITEM, "Сахарный тростник", "sugarcane", "reed", "reeds"),
    PAPER(ItemID.PAPER, "Бумага", "paper"),
    BOOK(ItemID.BOOK, "Книга", "book"),
    SLIME_BALL(ItemID.SLIME_BALL, "Слизь", "slimeball", "slime"),
    STORAGE_MINECART(ItemID.STORAGE_MINECART, "Вагонетка с сунжуком", "storageminecart", "storagecart"),
    POWERED_MINECART(ItemID.POWERED_MINECART, "Вагонетка с двигателем", "poweredminecart", "poweredcart"),
    EGG(ItemID.EGG, "Яйцо", "egg"),
    COMPASS(ItemID.COMPASS, "Компас", "compass"),
    FISHING_ROD(ItemID.FISHING_ROD, "Удочка", "fishingrod", "fishingpole"),
    WATCH(ItemID.WATCH, "Часы", "watch", "clock", "timer"),
    LIGHTSTONE_DUST(ItemID.LIGHTSTONE_DUST, "Светопыль", "lightstonedust", "glowstonedone", "brightstonedust", "brittlegolddust", "brimstonedust"),
    RAW_FISH(ItemID.RAW_FISH, "Сырая рыба", "rawfish", "fish"),
    COOKED_FISH(ItemID.COOKED_FISH, "Жареная рыба", "cookedfish"),
    INK_SACK(ItemID.INK_SACK, "Чернильный мешок", "inksac", "ink", "dye", "inksack"),
    BONE(ItemID.BONE, "Кость", "bone"),
    SUGAR(ItemID.SUGAR, "Сахар", "sugar"),
    CAKE_ITEM(ItemID.CAKE_ITEM, "Торт", "cake"),
    BED_ITEM(ItemID.BED_ITEM, "Кровать", "bed"),
    REDSTONE_REPEATER(ItemID.REDSTONE_REPEATER, "Повторитель", "redstonerepeater", "diode", "delayer", "repeater"),
    COOKIE(ItemID.COOKIE, "Печенье", "cookie"),
    MAP(ItemID.MAP, "Карта", "map"),
    SHEARS(ItemID.SHEARS, "Ножницы", "shears", "scissors"),
    MELON(ItemID.MELON, "Арбуз", "melon", "melonslice"),
    PUMPKIN_SEEDS(ItemID.PUMPKIN_SEEDS, "Семена тыквы", "pumpkinseed", "pumpkinseeds"),
    MELON_SEEDS(ItemID.MELON_SEEDS, "Семена арбуза", "melonseed", "melonseeds"),
    RAW_BEEF(ItemID.RAW_BEEF, "Сырая говядина", "rawbeef", "rawcow", "beef"),
    COOKED_BEEF(ItemID.COOKED_BEEF, "Стейк", "steak", "cookedbeef", "cookedcow"),
    RAW_CHICKEN(ItemID.RAW_CHICKEN, "Сырая курица", "rawchicken"),
    COOKED_CHICKEN(ItemID.COOKED_CHICKEN, "Жареная курица", "cookedchicken", "chicken", "grilledchicken"),
    ROTTEN_FLESH(ItemID.ROTTEN_FLESH, "Гнилая плоть", "rottenflesh", "zombiemeat", "flesh"),
    ENDER_PEARL(ItemID.ENDER_PEARL, "Жемчуг края", "pearl", "enderpearl"),
    BLAZE_ROD(ItemID.BLAZE_ROD, "Стержень ифрита", "blazerod"),
    GHAST_TEAR(ItemID.GHAST_TEAR, "Слеза гаста", "ghasttear"),
    GOLD_NUGGET(ItemID.GOLD_NUGGET, "Золотой самородок", "goldnugget"),
    NETHER_WART_ITEM(ItemID.NETHER_WART_SEED, "Адский нарост", "netherwart", "netherwartseed"),
    POTION(ItemID.POTION, "Зелье", "potion"),
    GLASS_BOTTLE(ItemID.GLASS_BOTTLE, "Пузырёк", "glassbottle"),
    SPIDER_EYE(ItemID.SPIDER_EYE, "Паучий глаз", "spidereye"),
    FERMENTED_SPIDER_EYE(ItemID.FERMENTED_SPIDER_EYE, "Маринованный паучий глаз", "fermentedspidereye", "fermentedeye"),
    BLAZE_POWDER(ItemID.BLAZE_POWDER, "Огненный порошок", "blazepowder"),
    MAGMA_CREAM(ItemID.MAGMA_CREAM, "Сгусток магмы", "magmacream"),
    BREWING_STAND_ITEM(ItemID.BREWING_STAND, "Варочная стойка", "brewingstand"),
    CAULDRON_ITEM(ItemID.CAULDRON, "Котёл", "cauldron"),
    EYE_OF_ENDER(ItemID.EYE_OF_ENDER, "Око эндера", "eyeofender", "endereye"),
    GLISTERING_MELON(ItemID.GLISTERING_MELON, "Сверкающий арбуз", "glisteringmelon", "goldmelon"),
    SPAWN_EGG(ItemID.SPAWN_EGG, "Яйцо призыва", "spawnegg", "spawn", "mobspawnegg"),
    BOTTLE_O_ENCHANTING(ItemID.BOTTLE_O_ENCHANTING, "Колба опыта", "expbottle", "bottleoenchanting", "experiencebottle", "exppotion", "experiencepotion"),
    FIRE_CHARGE(ItemID.FIRE_CHARGE, "Огненный шар", "firecharge", "firestarter", "firerock"),
    BOOK_AND_QUILL(ItemID.BOOK_AND_QUILL, "Книга и перо", "bookandquill", "quill", "writingbook"),
    WRITTEN_BOOK(ItemID.WRITTEN_BOOK, "Написанная книга", "writtenbook"),
    EMERALD(ItemID.EMERALD, "Изумруд", "emeraldingot", "emerald"),
    ITEM_FRAME(ItemID.ITEM_FRAME, "Рамка", "itemframe", "frame", "itempainting"),
    FLOWER_POT(ItemID.FLOWER_POT, "Цветочный горшок", "flowerpot", "plantpot", "pot"),
    CARROT(ItemID.CARROT, "Морковь", "carrot"),
    POTATO(ItemID.POTATO, "Картофель", "potato"),
    BAKED_POTATO(ItemID.BAKED_POTATO, "Печёный картофель", "bakedpotato", "potatobaked"),
    POISONOUS_POTATO(ItemID.POISONOUS_POTATO, "Отравленный картофель", "poisonpotato", "poisonouspotato"),
    BLANK_MAP(ItemID.BLANK_MAP, "Карта", "blankmap", "emptymap"),
    GOLDEN_CARROT(ItemID.GOLDEN_CARROT, "Золотая морковь", "goldencarrot", "goldcarrot"),
    HEAD(ItemID.HEAD, "Голова",  "head", "headmount", "mount"),
    CARROT_ON_A_STICK(ItemID.CARROT_ON_A_STICK, "Морковка на удочке", "carrotonastick", "carrotonstick", "stickcarrot", "carrotstick"),
    NETHER_STAR(ItemID.NETHER_STAR, "Звезда ада", "netherstar", "starnether"),
    PUMPKIN_PIE(ItemID.PUMPKIN_PIE, "Тыквенный пирог", "pumpkinpie"),
    FIREWORK_ROCKET(ItemID.FIREWORK_ROCKET, "Ракета", "firework", "rocket"),
    FIREWORK_STAR(ItemID.FIREWORK_STAR, "Звёздочка", "fireworkstar", "fireworkcharge"),
    ENCHANTED_BOOK(ItemID.ENCHANTED_BOOK, "Книга зачарования", "enchantedbook", "spellbook", "enchantedtome", "tome"),
    DISC_13(ItemID.DISC_13, "Пластинка - 13", "disc_13"),
    DISC_CAT(ItemID.DISC_CAT, "Пластинка - Cat", "disc_cat"),
    DISC_BLOCKS(ItemID.DISC_BLOCKS, "Пластинка - blocks", "disc_blocks"),
    DISC_CHIRP(ItemID.DISC_CHIRP, "Пластинка - chirp", "disc_chirp"),
    DISC_FAR(ItemID.DISC_FAR, "Пластинка - far", "disc_far"),
    DISC_MALL(ItemID.DISC_MALL, "Пластинка - mall", "disc_mall"),
    DISC_MELLOHI(ItemID.DISC_MELLOHI, "Пластинка - mellohi", "disc_mellohi"),
    DISC_STAL(ItemID.DISC_STAL, "Пластинка - stal", "disc_stal"),
    DISC_STRAD(ItemID.DISC_STRAD, "Пластинка - strad", "disc_strad"),
    DISC_WARD(ItemID.DISC_WARD, "Пластинка - ward", "disc_ward"),
    DISC_11(ItemID.DISC_11, "Пластинка - 11", "disc_11"),
    DISC_WAIT(ItemID.DISC_WAIT, "Пластинка - wait", "disc_wait"),

    // deprecated
    @Deprecated GOLD_RECORD(ItemID.GOLD_RECORD, "Золотая пластинка", "goldrecord", "golddisc"),
    @Deprecated GREEN_RECORD(ItemID.GREEN_RECORD, "Зеленая пластинка", "greenrecord", "greenddisc");

    /**
     * Stores a map of the IDs for fast access.    Это для //set
     */
    private static final Map<Integer, ItemType> ids = new HashMap<Integer, ItemType>();
    /**
     * Stores a map of the names for fast access.
     */
    private static final Map<String, ItemType> lookup = new LinkedHashMap<String, ItemType>();

    private final int id;
    private final String name;
    private final String[] lookupKeys;

    static {
        for (ItemType type : EnumSet.allOf(ItemType.class)) {
            ids.put(type.id, type);
            for (String key : type.lookupKeys) {
                lookup.put(key, type);
            }
        }
    }


    /**
     * Construct the type.
     *
     * @param id
     * @param name
     */
    ItemType(int id, String name, String lookupKey) {
        this.id = id;
        this.name = name;
        this.lookupKeys = new String[] { lookupKey };
    }

    /**
     * Construct the type.
     *
     * @param id
     * @param name
     */
    ItemType(int id, String name, String... lookupKeys) {
        this.id = id;
        this.name = name;
        this.lookupKeys = lookupKeys;
    }

    /**
     * Return type from ID. May return null.
     *
     * @param id
     * @return
     */
    public static ItemType fromID(int id) {
        return ids.get(id);
    }

    /**
     * Get a name for the item.
     *
     * @param id
     * @return
     */
    public static String toName(int id) {
        ItemType type = ids.get(id);
        if (type != null) {
            return type.getName();
        } else {
            return "#" + id;
        }
    }

    /**
     * Get a name for a held item.
     *
     * @param id
     * @return
     */
    public static String toHeldName(int id) {
        if (id == 0) {
            return "Hand";
        }
        ItemType type = ids.get(id);
        if (type != null) {
            return type.getName();
        } else {
            return "#" + id;
        }
    }

    /**
     * Return type from name. May return null.
     *
     * @param name
     * @return
     */
    public static ItemType lookup(String name) {
        return lookup(name, true);
    }

    /**
     * Return type from name. May return null.
     *
     * @param name
     * @param fuzzy
     * @return
     */
    public static ItemType lookup(String name, boolean fuzzy) {
        try {
            return fromID(Integer.parseInt(name));
        } catch (NumberFormatException e) {
            return StringUtil.lookup(lookup, name, fuzzy);
        }
    }

    /**
     * Get item numeric ID.
     *
     * @return
     */
    public int getID() {
        return id;
    }

    /**
     * Get user-friendly item name.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get a list of aliases.
     *
     * @return
     */
    public String[] getAliases() {
        return lookupKeys;
    }

    private static final Set<Integer> shouldNotStack = new HashSet<Integer>();
    static {
        shouldNotStack.add(ItemID.IRON_SHOVEL);
        shouldNotStack.add(ItemID.IRON_PICK);
        shouldNotStack.add(ItemID.IRON_AXE);
        shouldNotStack.add(ItemID.FLINT_AND_TINDER);
        shouldNotStack.add(ItemID.BOW);
        shouldNotStack.add(ItemID.IRON_SWORD);
        shouldNotStack.add(ItemID.WOOD_SWORD);
        shouldNotStack.add(ItemID.WOOD_SHOVEL);
        shouldNotStack.add(ItemID.WOOD_PICKAXE);
        shouldNotStack.add(ItemID.WOOD_AXE);
        shouldNotStack.add(ItemID.STONE_SWORD);
        shouldNotStack.add(ItemID.STONE_SHOVEL);
        shouldNotStack.add(ItemID.STONE_PICKAXE);
        shouldNotStack.add(ItemID.STONE_AXE);
        shouldNotStack.add(ItemID.DIAMOND_SWORD);
        shouldNotStack.add(ItemID.DIAMOND_SHOVEL);
        shouldNotStack.add(ItemID.DIAMOND_PICKAXE);
        shouldNotStack.add(ItemID.DIAMOND_AXE);
        shouldNotStack.add(ItemID.BOWL);
        shouldNotStack.add(ItemID.GOLD_SWORD);
        shouldNotStack.add(ItemID.GOLD_SHOVEL);
        shouldNotStack.add(ItemID.GOLD_PICKAXE);
        shouldNotStack.add(ItemID.GOLD_AXE);
        shouldNotStack.add(ItemID.WOOD_HOE);
        shouldNotStack.add(ItemID.STONE_HOE);
        shouldNotStack.add(ItemID.IRON_HOE);
        shouldNotStack.add(ItemID.DIAMOND_HOE);
        shouldNotStack.add(ItemID.GOLD_HOE);
        shouldNotStack.add(ItemID.LEATHER_HELMET);
        shouldNotStack.add(ItemID.LEATHER_CHEST);
        shouldNotStack.add(ItemID.LEATHER_PANTS);
        shouldNotStack.add(ItemID.LEATHER_BOOTS);
        shouldNotStack.add(ItemID.CHAINMAIL_CHEST);
        shouldNotStack.add(ItemID.CHAINMAIL_HELMET);
        shouldNotStack.add(ItemID.CHAINMAIL_BOOTS);
        shouldNotStack.add(ItemID.CHAINMAIL_PANTS);
        shouldNotStack.add(ItemID.IRON_HELMET);
        shouldNotStack.add(ItemID.IRON_CHEST);
        shouldNotStack.add(ItemID.IRON_PANTS);
        shouldNotStack.add(ItemID.IRON_BOOTS);
        shouldNotStack.add(ItemID.DIAMOND_HELMET);
        shouldNotStack.add(ItemID.DIAMOND_PANTS);
        shouldNotStack.add(ItemID.DIAMOND_CHEST);
        shouldNotStack.add(ItemID.DIAMOND_BOOTS);
        shouldNotStack.add(ItemID.GOLD_HELMET);
        shouldNotStack.add(ItemID.GOLD_CHEST);
        shouldNotStack.add(ItemID.GOLD_PANTS);
        shouldNotStack.add(ItemID.GOLD_BOOTS);
        shouldNotStack.add(ItemID.WOODEN_DOOR_ITEM);
        shouldNotStack.add(ItemID.WATER_BUCKET);
        shouldNotStack.add(ItemID.LAVA_BUCKET);
        shouldNotStack.add(ItemID.MINECART);
        shouldNotStack.add(ItemID.SADDLE);
        shouldNotStack.add(ItemID.IRON_DOOR_ITEM);
        shouldNotStack.add(ItemID.WOOD_BOAT);
        shouldNotStack.add(ItemID.MILK_BUCKET);
        shouldNotStack.add(ItemID.STORAGE_MINECART);
        shouldNotStack.add(ItemID.POWERED_MINECART);
        shouldNotStack.add(ItemID.WATCH);
        shouldNotStack.add(ItemID.CAKE_ITEM);
        shouldNotStack.add(ItemID.BED_ITEM);
        shouldNotStack.add(ItemID.MAP);
        shouldNotStack.add(ItemID.SHEARS);
        shouldNotStack.add(ItemID.HEAD);
        shouldNotStack.add(ItemID.FIREWORK_ROCKET);
        shouldNotStack.add(ItemID.FIREWORK_STAR);
        shouldNotStack.add(ItemID.ENCHANTED_BOOK);
        shouldNotStack.add(ItemID.TNT_MINECART);
        shouldNotStack.add(ItemID.HOPPER_MINECART);
        shouldNotStack.add(ItemID.HORSE_ARMOR_IRON);
        shouldNotStack.add(ItemID.HORSE_ARMOR_GOLD);
        shouldNotStack.add(ItemID.HORSE_ARMOR_DIAMOND);
        shouldNotStack.add(ItemID.DISC_13);
        shouldNotStack.add(ItemID.DISC_CAT);
        shouldNotStack.add(ItemID.DISC_BLOCKS);
        shouldNotStack.add(ItemID.DISC_CHIRP);
        shouldNotStack.add(ItemID.DISC_FAR);
        shouldNotStack.add(ItemID.DISC_MALL);
        shouldNotStack.add(ItemID.DISC_MELLOHI);
        shouldNotStack.add(ItemID.DISC_STAL);
        shouldNotStack.add(ItemID.DISC_STRAD);
        shouldNotStack.add(ItemID.DISC_WARD);
        shouldNotStack.add(ItemID.DISC_11);
        shouldNotStack.add(ItemID.DISC_WAIT);
    }

    /**
     * Returns true if an item should not be stacked.
     *
     * @param id
     * @return
     */
    public static boolean shouldNotStack(int id) {
        return shouldNotStack.contains(id);
    }

    private static final Set<Integer> usesDamageValue = new HashSet<Integer>();
    static {
        usesDamageValue.add(BlockID.WOOD);
        usesDamageValue.add(BlockID.SAPLING);
        usesDamageValue.add(BlockID.LOG);
        usesDamageValue.add(BlockID.LEAVES);
        usesDamageValue.add(BlockID.SANDSTONE);
        usesDamageValue.add(BlockID.LONG_GRASS);
        usesDamageValue.add(BlockID.CLOTH);
        usesDamageValue.add(BlockID.DOUBLE_STEP);
        usesDamageValue.add(BlockID.STEP);
        usesDamageValue.add(BlockID.SILVERFISH_BLOCK);
        usesDamageValue.add(BlockID.STONE_BRICK);
        usesDamageValue.add(BlockID.BROWN_MUSHROOM_CAP);
        usesDamageValue.add(BlockID.RED_MUSHROOM_CAP);
        usesDamageValue.add(BlockID.DOUBLE_WOODEN_STEP);
        usesDamageValue.add(BlockID.WOODEN_STEP);
        usesDamageValue.add(BlockID.COBBLESTONE_WALL);
        usesDamageValue.add(BlockID.ANVIL);
        usesDamageValue.add(BlockID.QUARTZ_BLOCK);
        usesDamageValue.add(BlockID.STAINED_CLAY);
        usesDamageValue.add(BlockID.CARPET);

        usesDamageValue.add(ItemID.COAL);
        usesDamageValue.add(ItemID.INK_SACK);
        usesDamageValue.add(ItemID.POTION);
        usesDamageValue.add(ItemID.SPAWN_EGG);
        usesDamageValue.add(ItemID.MAP);
        usesDamageValue.add(ItemID.HEAD);
        usesDamageValue.add(ItemID.GOLD_APPLE);
    }

    /**
     * Returns true if an item uses its damage value for something
     * other than damage.
     *
     * @param id
     * @return
     */
    public static boolean usesDamageValue(int id) {
        return usesDamageValue.contains(id);
    }
}
