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

package com.sk89q.worldedit.commands;

import static com.sk89q.minecraft.util.commands.Logging.LogMode.POSITION;
import static com.sk89q.minecraft.util.commands.Logging.LogMode.REGION;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandAlias;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.Countable;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.data.ChunkStore;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegionSelector;
import com.sk89q.worldedit.regions.CuboidRegionSelector;
import com.sk89q.worldedit.regions.CylinderRegionSelector;
import com.sk89q.worldedit.regions.EllipsoidRegionSelector;
import com.sk89q.worldedit.regions.ExtendingCuboidRegionSelector;
import com.sk89q.worldedit.regions.Polygonal2DRegionSelector;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.SphereRegionSelector;
import com.sk89q.worldedit.blocks.*;
import com.sk89q.worldedit.regions.CylinderRegionSelector;
import com.sk89q.worldedit.util.StringUtil;

/**
 * Selection commands.
 *
 * @author sk89q
 */
public class SelectionCommands {
    private final WorldEdit we;

    public SelectionCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
            aliases = {"/pos1"},
            usage = "[coordinates]",
            desc = "Задает первую позицию",
            min = 0,
            max = 1
    )
    @Logging(POSITION)
    @CommandPermissions("worldedit.selection.pos")
    public void pos1(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        Vector pos;

        if (args.argsLength() == 1) {
            if (args.getString(0).matches("-?\\d+,-?\\d+,-?\\d+")) {
                String[] coords = args.getString(0).split(",");
                pos = new Vector(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
            } else {
                player.printError("Неверные координаты " + args.getString(0));
                return;
            }
        } else {
            pos = player.getBlockIn();
        }

        if (!session.getRegionSelector(player.getWorld()).selectPrimary(pos)) {
            player.printError("Позиция уже задана.");
            return;
        }

        session.getRegionSelector(player.getWorld())
                .explainPrimarySelection(player, session, pos);
    }

    @Command(
            aliases = {"/pos2"},
            usage = "[coordinates]",
            desc = "Задает вторую позицию",
            min = 0,
            max = 1
    )
    @Logging(POSITION)
    @CommandPermissions("worldedit.selection.pos")
    public void pos2(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        Vector pos;
        if (args.argsLength() == 1) {
            if (args.getString(0).matches("-?\\d+,-?\\d+,-?\\d+")) {
                String[] coords = args.getString(0).split(",");
                pos = new Vector(Integer.parseInt(coords[0]),
                        Integer.parseInt(coords[1]),
                        Integer.parseInt(coords[2]));
            } else {
                player.printError("Неверные координаты " + args.getString(0));
                return;
            }
        } else {
            pos = player.getBlockIn();
        }

        if (!session.getRegionSelector(player.getWorld()).selectSecondary(pos)) {
            player.printError("Позиция уже задана.");
            return;
        }

        session.getRegionSelector(player.getWorld())
                .explainSecondarySelection(player, session, pos);
    }

    @Command(
            aliases = {"/hpos1"},
            usage = "",
            desc = "Задает первую позицию на блок, на который Вы смотрите.",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.selection.hpos")
    public void hpos1(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {

        Vector pos = player.getBlockTrace(300);

        if (pos != null) {
            if (!session.getRegionSelector(player.getWorld())
                    .selectPrimary(pos)) {
                player.printError("Позиция уже задана.");
                return;
            }

            session.getRegionSelector(player.getWorld())
                    .explainPrimarySelection(player, session, pos);
        } else {
            player.printError("Нет блоков в поле зрения!");
        }
    }

    @Command(
            aliases = {"/hpos2"},
            usage = "",
            desc = "Задает вторую позицию на блок, на который Вы смотрите.",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.selection.hpos")
    public void hpos2(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {

        Vector pos = player.getBlockTrace(300);

        if (pos != null) {
            if (!session.getRegionSelector(player.getWorld())
                    .selectSecondary(pos)) {
                player.printError("Позиция уже задана.");
                return;
            }

            session.getRegionSelector(player.getWorld())
                    .explainSecondarySelection(player, session, pos);
        } else {
            player.printError("Нет блоков в поле зрения!");
        }
    }

    @Command(
        aliases = { "/chunk" },
        usage = "[x,z координаты]",
        flags = "sc",
        desc = "Выделение текущего чанка.",
        help =
            "Set the selection to the chunk you are currently in.\n" +
            "With the -s flag, your current selection is expanded\n" +
            "to encompass all chunks that are part of it.\n\n" +
            "Specifying coordinates will use those instead of your\n"+
            "current position. Use -c to specify chunk coordinates,\n" +
            "otherwise full coordinates will be implied.\n" +
            "(for example, the coordinates 5,5 are the same as -c 0,0)",
        min = 0,
        max = 1
    )
    @Logging(POSITION)
    @CommandPermissions("worldedit.selection.chunk")
    public void chunk(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {

        final Vector min;
        final Vector max;
        final LocalWorld world = player.getWorld();
        if (args.hasFlag('s')) {
            Region region = session.getSelection(world);

            final Vector2D min2D = ChunkStore.toChunk(region.getMinimumPoint());
            final Vector2D max2D = ChunkStore.toChunk(region.getMaximumPoint());

            min = new Vector(min2D.getBlockX() * 16, 0, min2D.getBlockZ() * 16);
            max = new Vector(max2D.getBlockX() * 16 + 15, world.getMaxY(), max2D.getBlockZ() * 16 + 15);

            player.print("Чанк выделен: ("
                    + min2D.getBlockX() + ", " + min2D.getBlockZ() + ") - ("
                    + max2D.getBlockX() + ", " + max2D.getBlockZ() + ")");
        } else {
            final Vector2D min2D;
            if (args.argsLength() == 1) {
                // coords specified
                String[] coords = args.getString(0).split(",");
                if (coords.length != 2) {
                    throw new InsufficientArgumentsException("Invalid coordinates specified.");
                }
                int x = Integer.parseInt(coords[0]);
                int z = Integer.parseInt(coords[1]);
                Vector2D pos = new Vector2D(x, z);
                min2D = (args.hasFlag('c')) ? pos : ChunkStore.toChunk(pos.toVector());
            } else {
                // use player loc
                min2D = ChunkStore.toChunk(player.getBlockIn());
            }

            min = new Vector(min2D.getBlockX() * 16, 0, min2D.getBlockZ() * 16);
            max = min.add(15, world.getMaxY(), 15);

            player.print("Чанк выбран: "
                    + min2D.getBlockX() + ", " + min2D.getBlockZ());
        }

        final CuboidRegionSelector selector;
        if (session.getRegionSelector(world) instanceof ExtendingCuboidRegionSelector) {
            selector = new ExtendingCuboidRegionSelector(world);
        } else {
            selector = new CuboidRegionSelector(world);
        }
        selector.selectPrimary(min);
        selector.selectSecondary(max);
        session.setRegionSelector(world, selector);

        session.dispatchCUISelection(player);

    }

    @Command(
            aliases = {"/wand"},
            usage = "",
            desc = "Получение предмета для выделения позиций",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.wand")
    public void wand(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        player.giveItem(we.getConfiguration().wandItem, 1);
        player.print("Левый клик: выделение первой точки; Правый клик: выделение второй точки");
    }

    @Command(
            aliases = {"toggleeditwand"},
            usage = "",
            desc = "Перекючает функциональность предмета для выделения позиций.",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.wand.toggle")
    public void toggleWand(CommandContext args, LocalSession session, LocalPlayer player,
                           EditSession editSession) throws WorldEditException {

        session.setToolControl(!session.isToolControlEnabled());

        if (session.isToolControlEnabled()) {
            player.print("Теперь предмет выделяет позиции.");
        } else {
            player.print("Теперь предмет не выделяет позиции и у него стандартное применение.");
        }
    }

    @Command(
            aliases = {"/expand"},
            usage = "<amount> [reverse-amount] <direction>",
            desc = "Расширяет выделенную территорию",
            min = 1,
            max = 3
    )
    @Logging(REGION)
    @CommandPermissions("worldedit.selection.expand")
    public void expand(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {

        // Special syntax (//expand vert) to expand the selection between
        // sky and bedrock.
        if (args.getString(0).equalsIgnoreCase("vert")
                || args.getString(0).equalsIgnoreCase("vertical")) {
            Region region = session.getSelection(player.getWorld());
            try {
                int oldSize = region.getArea();
                region.expand(
                        new Vector(0, (player.getWorld().getMaxY() + 1), 0),
                        new Vector(0, -(player.getWorld().getMaxY() + 1), 0));
                session.getRegionSelector(player.getWorld()).learnChanges();
                int newSize = region.getArea();
                session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
                player.print("Регион расширен на " + (newSize - oldSize)
                        + " " + StringUtil.plural((newSize - oldSize), "блок", "блока", "блоков") + ".");
            } catch (RegionOperationException e) {
                player.printError(e.getMessage());
            }

            return;
        }

        List<Vector> dirs = new ArrayList<Vector>();
        int change = args.getInteger(0);
        int reverseChange = 0;

        switch (args.argsLength()) {
            case 2:
                // Either a reverse amount or a direction
                try {
                    reverseChange = args.getInteger(1);
                    dir = we.getDirection(player, "me");
                } catch (NumberFormatException e) {
                    dir = we.getDirection(player,
                            args.getString(1).toLowerCase());
                }
                break;

            case 3:
                // Both reverse amount and direction
                reverseChange = args.getInteger(1);
                dirs.add(we.getDirection(player, "me"));
            } catch (NumberFormatException e) {
                if (args.getString(1).contains(",")) {
                    String[] split = args.getString(1).split(",");
                    for (String s : split) {
                        dirs.add(we.getDirection(player, s.toLowerCase()));
                    }
                } else {
                    dirs.add(we.getDirection(player, args.getString(1).toLowerCase()));
                }
            }
            break;

        case 3:
            // Both reverse amount and direction
            reverseChange = args.getInteger(1);
            if (args.getString(2).contains(",")) {
                String[] split = args.getString(2).split(",");
                for (String s : split) {
                    dirs.add(we.getDirection(player, s.toLowerCase()));
                }
            } else {
                dirs.add(we.getDirection(player, args.getString(2).toLowerCase()));
            }
            break;

        default:
            dirs.add(we.getDirection(player, "me"));
            break;

        }

        Region region = session.getSelection(player.getWorld());
        int oldSize = region.getArea();

        if (reverseChange == 0) {
            for (Vector dir : dirs) {
                region.expand(dir.multiply(change));
            }
        } else {
            for (Vector dir : dirs) {
                region.expand(dir.multiply(change), dir.multiply(-reverseChange));
            }
        }

        session.getRegionSelector(player.getWorld()).learnChanges();
        int newSize = region.getArea();

        session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);

        player.print("Регион расширен на " + (newSize - oldSize) + " " + StringUtil.plural((newSize - oldSize), "блок", "блока", "блоков") + ".");
    }

    @Command(
            aliases = {"/contract"},
            usage = "<amount> [reverse-amount] [direction]",
            desc = "Уменьшает выделенную область в заданом направлении.",
            min = 1,
            max = 3
    )
    @Logging(REGION)
    @CommandPermissions("worldedit.selection.contract")
    public void contract(CommandContext args, LocalSession session, LocalPlayer player,
                         EditSession editSession) throws WorldEditException {

        List<Vector> dirs = new ArrayList<Vector>();
        int change = args.getInteger(0);
        int reverseChange = 0;

        switch (args.argsLength()) {
            case 2:
                // Either a reverse amount or a direction
                try {
                    reverseChange = args.getInteger(1);
                    dir = we.getDirection(player, "me");
                } catch (NumberFormatException e) {
                    dir = we.getDirection(player, args.getString(1).toLowerCase());
                }
                break;

            case 3:
                // Both reverse amount and direction
                reverseChange = args.getInteger(1);
                dirs.add(we.getDirection(player, "me"));
            } catch (NumberFormatException e) {
                if (args.getString(1).contains(",")) {
                    String[] split = args.getString(1).split(",");
                    for (String s : split) {
                        dirs.add(we.getDirection(player, s.toLowerCase()));
                    }
                } else {
                    dirs.add(we.getDirection(player, args.getString(1).toLowerCase()));
                }
            }
            break;

        case 3:
            // Both reverse amount and direction
            reverseChange = args.getInteger(1);
            if (args.getString(2).contains(",")) {
                String[] split = args.getString(2).split(",");
                for (String s : split) {
                    dirs.add(we.getDirection(player, s.toLowerCase()));
                }
            } else {
                dirs.add(we.getDirection(player, args.getString(2).toLowerCase()));
            }
            break;

        default:
            dirs.add(we.getDirection(player, "me"));
            break;
        }

        try {
            Region region = session.getSelection(player.getWorld());
            int oldSize = region.getArea();
            if (reverseChange == 0) {
                for (Vector dir : dirs) {
                    region.contract(dir.multiply(change));
                }
            } else {
                for (Vector dir : dirs) {
                    region.contract(dir.multiply(change), dir.multiply(-reverseChange));
                }
            }
            session.getRegionSelector(player.getWorld()).learnChanges();
            int newSize = region.getArea();

            session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);


            player.print("Регион уменьшен на " + (oldSize - newSize) + " " + StringUtil.plural((newSize - oldSize), "блок", "блока", "блоков") + ".");
        } catch (RegionOperationException e) {
            player.printError(e.getMessage());
        }
    }

    @Command(
            aliases = {"/shift"},
            usage = "<amount> [direction]",
            desc = "Сдвигает выделенную территорию в заданном направлении.",
            min = 1,
            max = 2
    )
    @Logging(REGION)
    @CommandPermissions("worldedit.selection.shift")
    public void shift(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        List<Vector> dirs = new ArrayList<Vector>();
        int change = args.getInteger(0);
        if (args.argsLength() == 2) {
            if (args.getString(1).contains(",")) {
                for (String s : args.getString(1).split(",")) {
                    dirs.add(we.getDirection(player, s.toLowerCase()));
                }
            } else {
                dirs.add(we.getDirection(player, args.getString(1).toLowerCase()));
            }
        } else {
            dirs.add(we.getDirection(player, "me"));
        }

        try {
            Region region = session.getSelection(player.getWorld());

            for (Vector dir : dirs) {
                region.shift(dir.multiply(change));
            }

            session.getRegionSelector(player.getWorld()).learnChanges();

            session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);

            player.print("Регион сдвинут.");
        } catch (RegionOperationException e) {
            player.printError(e.getMessage());
        }
    }

    @Command(
            aliases = {"/outset"},
            usage = "<amount>",
            desc = "Расширение выделеннрй области во всех направлениях.",
            help =
                    "Расширяет выделенную область во всех направлениях.\n" +
                            "Флаги:\n" +
                            "  -h только горизонтально\n" +
                            "  -v только вертикально\n",
            flags = "hv",
            min = 1,
            max = 1
    )
    @Logging(REGION)
    @CommandPermissions("worldedit.selection.outset")
    public void outset(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {
        Region region = session.getSelection(player.getWorld());
        region.expand(getChangesForEachDir(args));
        session.getRegionSelector(player.getWorld()).learnChanges();
        session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
        player.print("Регион расширен.");
    }

    @Command(
            aliases = {"/inset"},
            usage = "<amount>",
            desc = "Сузить выделенную территорию.",
            help =
                    "Сужает выделенну территорию во всех направлениях.\n" +
                            "Флаги:\n" +
                            "  -h тоько горизонтально\n" +
                            "  -v только вертикально\n",
            flags = "hv",
            min = 1,
            max = 1
    )
    @Logging(REGION)
    @CommandPermissions("worldedit.selection.inset")
    public void inset(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {
        Region region = session.getSelection(player.getWorld());
        region.contract(getChangesForEachDir(args));
        session.getRegionSelector(player.getWorld()).learnChanges();
        session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
        player.print("Регион сужен.");
    }

    private Vector[] getChangesForEachDir(CommandContext args) {
        List<Vector> changes = new ArrayList<Vector>(6);
        int change = args.getInteger(0);

        if (!args.hasFlag('h')) {
            changes.add((new Vector(0, 1, 0)).multiply(change));
            changes.add((new Vector(0, -1, 0)).multiply(change));
        }

        if (!args.hasFlag('v')) {
            changes.add((new Vector(1, 0, 0)).multiply(change));
            changes.add((new Vector(-1, 0, 0)).multiply(change));
            changes.add((new Vector(0, 0, 1)).multiply(change));
            changes.add((new Vector(0, 0, -1)).multiply(change));
        }

        return changes.toArray(new Vector[0]);
    }

    @Command(
            aliases = {"/size"},
            flags = "c",
            usage = "",
            desc = "Получает информацию о выделенной территории",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.selection.size")
    public void size(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        if (args.hasFlag('c')) {
            CuboidClipboard clipboard = session.getClipboard();
            Vector size = clipboard.getSize();
            Vector offset = clipboard.getOffset();

        player.print("Размер: " + size);
        player.print("Смещение: " + offset);
        player.print("Диагональ: " + size.distance(Vector.ONE);
        player.print("Количество блоков: "
                         + (int) (size.getX() * size.getY() * size.getZ()));
            return;
        }

        Region region = session.getSelection(player.getWorld());
        Vector size = region.getMaximumPoint()
                .subtract(region.getMinimumPoint())
                .add(1, 1, 1);

        player.print("Тип: " + session.getRegionSelector(player.getWorld())
                .getTypeName());

        for (String line : session.getRegionSelector(player.getWorld())
                .getInformationLines()) {
            player.print(line);
        }

        player.print("Размер: " + size);
        player.print("Диагональ кубоида: " + region.getMaximumPoint()
                .distance(region.getMinimumPoint()));
        player.print("Количество блоков: " + region.getArea());
    }


    @Command(
            aliases = {"/count"},
            usage = "<block>",
            desc = "Подсчет количества определенного типа блока",
            flags = "d",
            min = 1,
            max = 1
    )
    @CommandPermissions("worldedit.analysis.count")
    public void count(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        boolean useData = args.hasFlag('d');
        if (args.getString(0).contains(":")) {
            useData = true; //override d flag, if they specified data they want it
        }
        if (useData) {
            Set<BaseBlock> searchBlocks = we.getBlocks(player, args.getString(0), true);
            int count = editSession.countBlocks(session.getSelection(player.getWorld()), searchBlocks);
            player.print("В выделенной территории " +
                    count + StringUtil.plural(count, " блок", " блока", " блоков") + " заданного типа.");
        } else {
            Set<Integer> searchIDs = we.getBlockIDs(player, args.getString(0), true);
            int count = editSession.countBlock(session.getSelection(player.getWorld()), searchIDs);
            player.print("В выделенной территории " +
                    count + StringUtil.plural(count, " блок", " блока", " блоков") + " заданного типа.");
        }
    }

    @Command(
        aliases = { "/distr" },
        usage = "",
        desc = "Подсчет колличества блоков в выделенной территории.",
        help =
            "Считает колличество блоков в выделенной территории.\n" +
            "Флаг -c считет блоки в буфере обмена.\n" +
            "Флаг -d разбивает блоки по данным",
        flags = "cd",
        min = 0,
        max = 0
    )
    @CommandPermissions("worldedit.analysis.distr")
    public void distr(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        int size;
        boolean useData = args.hasFlag('d');
        List<Countable<Integer>> distribution = null;
        List<Countable<BaseBlock>> distributionData = null;

        if (args.hasFlag('c')) {
            CuboidClipboard clip = session.getClipboard();
            if (useData) {
                distributionData = clip.getBlockDistributionWithData();
            } else {
                distribution = clip.getBlockDistribution();
            }
            size = clip.getHeight() * clip.getLength() * clip.getWidth(); 
        } else {
            if (useData) {
                distributionData = editSession.getBlockDistributionWithData(session.getSelection(player.getWorld()));
            } else {
                distribution = editSession.getBlockDistribution(session.getSelection(player.getWorld()));
            }
            size = session.getSelection(player.getWorld()).getArea();
        }

        if ((useData && distributionData.size() <= 0)
                || (!useData && distribution.size() <= 0)) {  // *Should* always be true
            player.printError("В выделенной территории блоки не найдены.");
            return;
        }
        
        player.print("В выделенной территории " + size + StringUtil.plural(size, " блок", " блока", " блоков"));

        if (useData) {
            for (Countable<BaseBlock> c : distributionData) {
                String name = BlockType.fromID(c.getID().getId()).getName();
                String str = String.format("%-7s (%.3f%%) %s #%d:%d",
                        String.valueOf(c.getAmount()),
                        c.getAmount() / (double) size * 100,
                        name == null ? "Неизвестно" : name,
                        c.getID().getType(), c.getID().getData());
                player.print(str);
            }
        } else {
            for (Countable<Integer> c : distribution) {
                BlockType block = BlockType.fromID(c.getID());
                String str = String.format("%-7s (%.3f%%) %s #%d",
                        String.valueOf(c.getAmount()),
                        c.getAmount() / (double) size * 100,
                        block == null ? "Неизвестно" : block.getName(), c.getID());
                player.print(str);
            }
        }
    }

    @Command(
        aliases = { "/sel", ";" },
        usage = "[cuboid|extend|poly|ellipsoid|sphere|cyl|convex]",
        desc = "Выбирает тип выделения региона",
        min = 0,
        max = 1
    )
    public void select(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {

        final LocalWorld world = player.getWorld();
        if (args.argsLength() == 0) {
            session.getRegionSelector(world).clear();
            session.dispatchCUISelection(player);
            player.print("Выделение очищено.");
            return;
        }

        final String typeName = args.getString(0);
        final RegionSelector oldSelector = session.getRegionSelector(world);

        final RegionSelector selector;
        if (typeName.equalsIgnoreCase("cuboid")) {
            selector = new CuboidRegionSelector(oldSelector);
            player.print("Кубоид: левый клик для первой позиции,правый для второй");
        } else if (typeName.equalsIgnoreCase("extend")) {
            selector = new ExtendingCuboidRegionSelector(oldSelector);
            player.print("Кубоид: левый клик для стартовой точки,правый для конечной");
        } else if (typeName.equalsIgnoreCase("poly")) {
            int maxPoints = we.getMaximumPolygonalPoints(player);
            selector = new Polygonal2DRegionSelector(oldSelector, maxPoints);
            player.print("2D Полигон: левый/правый клик для добавление точки.");
            if (maxPoints > -1) {
                player.print(maxPoints + " точек(а) максимум.");
            }
        } else if (typeName.equalsIgnoreCase("ellipsoid")) {
            selector = new EllipsoidRegionSelector(oldSelector);
            player.print("Эллипсоид: левый клик=центр, правый клик для расширения");
        } else if (typeName.equalsIgnoreCase("sphere")) {
            selector = new SphereRegionSelector(oldSelector);
            player.print("Сфера: левый клик=центр, правый клик для расширения");
        } else if (typeName.equalsIgnoreCase("cyl")) {
            selector = new CylinderRegionSelector(oldSelector);
            player.print("Циллиндр: левый клик=центр, правый клик для расширения.");
        } else if (typeName.equalsIgnoreCase("convex") || typeName.equalsIgnoreCase("hull") || typeName.equalsIgnoreCase("polyhedron")) {
            int maxVertices = we.getMaximumPolyhedronPoints(player);
            selector = new ConvexPolyhedralRegionSelector(oldSelector, maxVertices);
            player.print("Выпуклый полигон: Левый клик=Первая вершина, правый для добавления.");
        } else {
            player.printError("Доступный только такие значение: cuboid(кубоид)|extend(расширение)|poly(полигон)|ellipsoid(эллипсоид)|sphere(сфера)|cyl(цилиндр)|convex(выпуклый полигон).");
            return;
        }

        session.setRegionSelector(world, selector);
        session.dispatchCUISelection(player);
    }

    @Command(aliases = {"/desel", "/deselect"}, desc = "Сбрасывает выделение")
    @CommandAlias("/sel")
    public void deselect() {

    }
}
