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

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.expression.ExpressionException;
import com.sk89q.worldedit.filtering.GaussianKernel;
import com.sk89q.worldedit.filtering.HeightMapFilter;
import com.sk89q.worldedit.generator.FloraGenerator;
import com.sk89q.worldedit.generator.ForestGenerator;
import com.sk89q.worldedit.operation.GroundScatterFunction;
import com.sk89q.worldedit.masks.Mask;
import com.sk89q.worldedit.operation.FlatRegionApplicator;
import com.sk89q.worldedit.operation.OperationHelper;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.*;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.sk89q.minecraft.util.commands.Logging.LogMode.*;

/**
 * Region related commands.
 *
 * @author sk89q
 */
public class RegionCommands {
    private final WorldEdit we;

    public RegionCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
            aliases = {"/set"},
            usage = "<block>",
            desc = "Заполняет выделенную территорию блоками.",
            min = 1,
            max = 1
    )
    @CommandPermissions("worldedit.region.set")
    @Logging(REGION)
    public void set(CommandContext args, LocalSession session, LocalPlayer player,
                    EditSession editSession) throws WorldEditException {

        Pattern pattern = we.getBlockPattern(player, args.getString(0));

        int affected;

        if (pattern instanceof SingleBlockPattern) {
            affected = editSession.setBlocks(session.getSelection(player.getWorld()),
                    ((SingleBlockPattern) pattern).getBlock());
        } else {
            affected = editSession.setBlocks(session.getSelection(player.getWorld()), pattern);
        }
        player.print(affected + " " + StringUtil.plural(affected, "блок изменен", "блока изменено", "блоков изменено") + ".");
    }

    @Command(
            aliases = { "/line" },
            usage = "<block> [thickness]",
            desc = "Рисует линию между углами кубоида",
            help =
                "Рисует линию между углами выделенного кубоида.\n" +
                "Может быть применено только к кубоиду.\n" +
                "Флаги:\n" +
                "  -h генерирует только поверхность",
            flags = "h",
            min = 1,
            max = 2
    )
    @CommandPermissions("worldedit.region.line")
    @Logging(REGION)
    public void line(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        Region region = session.getSelection(session.getSelectionWorld());
        if (!(region instanceof CuboidRegion)) {
            player.printError("Неверный тип региона");
            return;
        }
        if (args.argsLength() < 2 ? false : args.getDouble(1) < 0) {
            player.printError("Неверная толщина. Не может быть отрицательная.");
            return;
        }

        Pattern pattern = we.getBlockPattern(player, args.getString(0));
        CuboidRegion cuboidregion = (CuboidRegion) region;
        Vector pos1 = cuboidregion.getPos1();
        Vector pos2 = cuboidregion.getPos2();
        int blocksChanged = editSession.drawLine(pattern, pos1, pos2, args.argsLength() < 2 ? 0 : args.getDouble(1), !args.hasFlag('h'));

        player.print(blocksChanged + " " + StringUtil.plural(blocksChanged, "блок был изменен", "блока было изменено", "блоков было изменено")+".");
    }

    @Command(
            aliases = { "/curve" },
            usage = "<block> [thickness]",
            desc = "Рисует кривую через выделенные точки.",
            help =
                "Рисует кривую через выделенные точки.\n" +
                "Может быть примерено только к полигональному веделению.\n" +
                "Флаги:\n" +
                "  -h генерирует только оболочку",
            flags = "h",
            min = 1,
            max = 2
    )
    @CommandPermissions("worldedit.region.curve")
    @Logging(REGION)
    public void curve(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        Region region = session.getSelection(session.getSelectionWorld());
        if (!(region instanceof ConvexPolyhedralRegion)) {
            player.printError("Неверный тип региона");
            return;
        }
        if (args.argsLength() < 2 ? false : args.getDouble(1) < 0) {
            player.printError("Неверная толщина. Не может быть отрицательная.");
            return;
        }

        Pattern pattern = we.getBlockPattern(player, args.getString(0));
        ConvexPolyhedralRegion cpregion = (ConvexPolyhedralRegion) region;
        List<Vector> vectors = new ArrayList<Vector>(cpregion.getVertices());

        int blocksChanged = editSession.drawSpline(pattern, vectors, 0, 0, 0, 10, args.argsLength() < 2 ? 0 : args.getDouble(1), !args.hasFlag('h'));

        player.print(blocksChanged + " " + StringUtil.plural(blocksChanged, "блок был изменен", "блока было изменено", "блоков было изменено")+".");
    }

    @Command(
        aliases = { "/replace", "/re", "/rep" },
        usage = "[from-block] <to-block>",
        desc = "Заменяет все блоки в выделенной территории на другие",
        flags = "f",
        min = 1,
        max = 2
    )
    @CommandPermissions("worldedit.region.replace")
    @Logging(REGION)
    public void replace(CommandContext args, LocalSession session, LocalPlayer player,
                        EditSession editSession) throws WorldEditException {

        Set<BaseBlock> from;
        Pattern to;
        if (args.argsLength() == 1) {
            from = null;
            to = we.getBlockPattern(player, args.getString(0));
        } else {
            from = we.getBlocks(player, args.getString(0), true, !args.hasFlag('f'));
            to = we.getBlockPattern(player, args.getString(1));
        }

        final int affected;
        if (to instanceof SingleBlockPattern) {
            affected = editSession.replaceBlocks(session.getSelection(player.getWorld()), from,
                    ((SingleBlockPattern) to).getBlock());
        } else {
            affected = editSession.replaceBlocks(session.getSelection(player.getWorld()), from, to);
        }
        player.print(affected + " " + StringUtil.plural(affected, "блок заменен", "блока заменено", "блоков заменено") + ".");
    }

    @Command(
            aliases = {"/overlay"},
            usage = "<block>",
            desc = "Накладывает блоки поверх выделенной территории.",
            min = 1,
            max = 1
    )
    @CommandPermissions("worldedit.region.overlay")
    @Logging(REGION)
    public void overlay(CommandContext args, LocalSession session, LocalPlayer player,
                        EditSession editSession) throws WorldEditException {

        Pattern pat = we.getBlockPattern(player, args.getString(0));

        Region region = session.getSelection(player.getWorld());
        int affected = 0;
        if (pat instanceof SingleBlockPattern) {
            affected = editSession.overlayCuboidBlocks(region,
                    ((SingleBlockPattern) pat).getBlock());
        } else {
            affected = editSession.overlayCuboidBlocks(region, pat);
        }
        player.print(affected + " " + StringUtil.plural(affected, "блок наложен", "блока наложено", "блоков наложено") + ".");
    }

    @Command(
            aliases = {"/center", "/middle"},
            usage = "<block>",
            desc = "Задать центральные блок(и)",
            min = 1,
            max = 1
    )
    @Logging(REGION)
    @CommandPermissions("worldedit.region.center")
    public void center(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {
        Pattern pattern = we.getBlockPattern(player, args.getString(0));
        Region region = session.getSelection(player.getWorld());

        int affected = editSession.center(region, pattern);
        player.print("Центр задан (" + affected + " " + StringUtil.plural(affected, "блок изменен", "блока изменено", "блоков изменено") + ")");
    }

    @Command(
            aliases = {"/naturalize"},
            usage = "",
            desc = "Создает 3 слоя грязи сверху и камень под ней.",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.region.naturalize")
    @Logging(REGION)
    public void naturalize(CommandContext args, LocalSession session, LocalPlayer player,
                           EditSession editSession) throws WorldEditException {

        Region region = session.getSelection(player.getWorld());
        int affected = editSession.naturalizeCuboidBlocks(region);
        player.print(affected + " " + StringUtil.plural(affected, "блок был изменен", "блока было изменено", "блоков было изменено") + ".");
    }

    @Command(
            aliases = {"/walls"},
            usage = "<block>",
            desc = "Создает стены в выделенной территории",
            min = 1,
            max = 1
    )
    @CommandPermissions("worldedit.region.walls")
    @Logging(REGION)
    public void walls(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {

        final Pattern pattern = we.getBlockPattern(player, args.getString(0));
        final int affected;
        final Region region = session.getSelection(player.getWorld());
        if (!(region instanceof CuboidRegion)) {
            affected = editSession.makeWalls(region, pattern);
        } else if (pattern instanceof SingleBlockPattern) {
            affected = editSession.makeCuboidWalls(region, ((SingleBlockPattern) pattern).getBlock());
        } else {
            affected = editSession.makeCuboidWalls(region, pattern);
        }

        player.print(affected + " " + StringUtil.plural(affected, "блок изменен", "блока изменено", "блоков изменено") + ".");
    }

    @Command(
            aliases = {"/faces", "/outline"},
            usage = "<block>",
            desc = "Строит стены, пол и потолок в выделенной территории",
            min = 1,
            max = 1
    )
    @CommandPermissions("worldedit.region.faces")
    @Logging(REGION)
    public void faces(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {

        final Pattern pattern = we.getBlockPattern(player, args.getString(0));
        final int affected;
        final Region region = session.getSelection(player.getWorld());
        if (!(region instanceof CuboidRegion)) {
            affected = editSession.makeFaces(region, pattern);
        } else if (pattern instanceof SingleBlockPattern) {
            affected = editSession.makeCuboidFaces(region, ((SingleBlockPattern) pattern).getBlock());
        } else {
            affected = editSession.makeCuboidFaces(region, pattern);
        }

        player.print(affected + " " + StringUtil.plural(affected, "блок изменен", "блока изменено", "блоков изменено") + ".");
    }

    @Command(
            aliases = {"/smooth"},
            usage = "[iterations]",
            flags = "n",
            desc = "Сглаживание возвышенностей в выделенной территории.",
            help =
                    "Сглаживает возвышенности в выделенной территории.\n" +
                            "Флаг -n применяет сглаживание только на натуральные блоки(природа).",
            min = 0,
            max = 1
    )
    @CommandPermissions("worldedit.region.smooth")
    @Logging(REGION)
    public void smooth(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {

        int iterations = 1;
        if (args.argsLength() > 0) {
            iterations = args.getInteger(0);
        }

        HeightMap heightMap = new HeightMap(editSession, session.getSelection(player.getWorld()), args.hasFlag('n'));
        HeightMapFilter filter = new HeightMapFilter(new GaussianKernel(5, 1.0));
        int affected = heightMap.applyFilter(filter, iterations);
        player.print("Высоты сглажены. " + affected + " " + StringUtil.plural(affected, "блок изменен", "блока изменено", "блоков изменено") + ".");

    }

    @Command(
            aliases = {"/move"},
            usage = "[count] [direction] [leave-id]",
            flags = "s",
            desc = "Перемещение содержимого выделенной территории",
            help =
                    "Перемещает содержимое выделенной территории.\n" +
                            "Флаг -s смещает выделенную территорию на нужное место.\n" +
                            "При необходимости заполняется в старом месте с <id>.",
            min = 0,
            max = 3
    )
    @CommandPermissions("worldedit.region.move")
    @Logging(ORIENTATION_REGION)
    public void move(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        int count = args.argsLength() > 0 ? Math.max(1, args.getInteger(0)) : 1;
        Vector dir = we.getDirection(player,
                args.argsLength() > 1 ? args.getString(1).toLowerCase() : "me");
        BaseBlock replace;

        // Replacement block argument
        if (args.argsLength() > 2) {
            replace = we.getBlock(player, args.getString(2));
        } else {
            replace = new BaseBlock(BlockID.AIR);
        }

        int affected = editSession.moveRegion(session.getSelection(player.getWorld()),
                dir, count, true, replace);

        if (args.hasFlag('s')) {
            try {
                Region region = session.getSelection(player.getWorld());
                region.shift(dir.multiply(count));

                session.getRegionSelector(player.getWorld()).learnChanges();
                session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
            } catch (RegionOperationException e) {
                player.printError(e.getMessage());
            }
        }

        player.print(affected + " " + StringUtil.plural(affected, "блок перемещен", "блока перемещено", "блоков перемещено") + ".");
    }

    @Command(
            aliases = {"/stack"},
            usage = "[count] [direction]",
            flags = "sa",
            desc = "Повторение содержимого выделенной территории",
            help =
                    "Повторяет содержимое выделенной территории.\n" +
                            "Флаги:\n" +
                            "  -s смещает выделенную территорию на последнюю копию\n" +
                            "  -a пропускает блоки воздуха.",
            min = 0,
            max = 2
    )
    @CommandPermissions("worldedit.region.stack")
    @Logging(ORIENTATION_REGION)
    public void stack(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {

        int count = args.argsLength() > 0 ? Math.max(1, args.getInteger(0)) : 1;
        Vector dir = we.getDiagonalDirection(player,
                args.argsLength() > 1 ? args.getString(1).toLowerCase() : "me");

        int affected = editSession.stackCuboidRegion(session.getSelection(player.getWorld()),
                dir, count, !args.hasFlag('a'));

        if (args.hasFlag('s')) {
            try {
                final Region region = session.getSelection(player.getWorld());
                final Vector size = region.getMaximumPoint().subtract(region.getMinimumPoint());

                final Vector shiftVector = dir.multiply(count * (Math.abs(dir.dot(size)) + 1));
                region.shift(shiftVector);

                session.getRegionSelector(player.getWorld()).learnChanges();
                session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
            } catch (RegionOperationException e) {
                player.printError(e.getMessage());
            }
        }

        player.print(affected + " " + StringUtil.plural(affected, "блок изменен", "блока изменено", "блоков изменено") + ". Отмена с помощью команды //undo");
    }

    @Command(
            aliases = {"/regen"},
            usage = "",
            desc = "Регенерация содержимого выделенной территории",
            help =
                    "Регенерирует содержимое выделенной территории.\n" +
                            "Эта команда может повлиять на вещи вне выделенной территории,\n" +
                            "если они находятся в одном чанке.",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.regen")
    @Logging(REGION)
    public void regenerateChunk(CommandContext args, LocalSession session, LocalPlayer player,
                                EditSession editSession) throws WorldEditException {

        Region region = session.getSelection(player.getWorld());
        Mask mask = session.getMask();
        session.setMask(null);
        player.getWorld().regenerate(region, editSession);
        session.setMask(mask);
        player.print("Регион регенерирован.");
    }

    @Command(
            aliases = {"/deform"},
            usage = "<expression>",
            desc = "Искажение выделенной территории с выражением.",
            help =
                    "Искажает выделенную территорию с выражением\n" +
                            "Выражение выполняется для каждого блока, и \n" +
                            "для изменения переменных x, y, r, чтобы указать новые блоки\n" +
                            "для извлечения. Смотрите также tinyurl.com/wesyntax.",
            flags = "ro",
            min = 1,
            max = -1
    )
    @CommandPermissions("worldedit.region.deform")
    @Logging(ALL)
    public void deform(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {

        final Region region = session.getSelection(player.getWorld());

        final String expression = args.getJoinedStrings(0);

        final Vector zero;
        Vector unit;

        if (args.hasFlag('r')) {
            zero = Vector.ZERO;
            unit = Vector.ONE;
        } else if (args.hasFlag('o')) {
            zero = session.getPlacementPosition(player);
            unit = Vector.ONE;
        } else {
            final Vector min = region.getMinimumPoint();
            final Vector max = region.getMaximumPoint();

            zero = max.add(min).multiply(0.5);
            unit = max.subtract(zero);

            if (unit.getX() == 0) unit = unit.setX(1.0);
            if (unit.getY() == 0) unit = unit.setY(1.0);
            if (unit.getZ() == 0) unit = unit.setZ(1.0);
        }

        try {
            final int affected = editSession.deformRegion(region, zero, unit, expression);
            player.findFreePosition();
            player.print(affected + " " + StringUtil.plural(affected, "блок был деформирован", "блока было деформировано", "блоков было деформировано") + ".");
        } catch (ExpressionException e) {
            player.printError(e.getMessage());
        }
    }

    @Command(
            aliases = {"/hollow"},
            usage = "[<thickness>[ <block>]]",
            desc = "Создание впадин из объектов, содержащихся в этой территории",
            help =
                    "Создает вадины из объектов, содержащихся в этой территории.\n" +
                            "При необходимости заполняет выдолбленные части блоками.\n" +
                            "Толщина измеряется на расстоянии Манхэттена.",
            min = 0,
            max = 2
    )
    @CommandPermissions("worldedit.region.hollow")
    @Logging(REGION)
    public void hollow(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {

        final int thickness = args.argsLength() >= 1 ? Math.max(1, args.getInteger(0)) : 1;
        final Pattern pattern = args.argsLength() >= 2 ? we.getBlockPattern(player, args.getString(1)) : new SingleBlockPattern(new BaseBlock(BlockID.AIR));

        final int affected = editSession.hollowOutRegion(session.getSelection(player.getWorld()), thickness, pattern);

        player.print(affected + " " + StringUtil.plural(affected, "блок изменен", "блока изменено", "блоков изменено") + ".");
    }

    @Command(
            aliases = { "/forest" },
            usage = "[type] [density]",
            desc = "Make a forest within the region",
            min = 0,
            max = 2
    )
    @CommandPermissions("worldedit.region.forest")
    @Logging(REGION)
    public void forest(CommandContext args, LocalSession session, LocalPlayer player,
                           EditSession editSession) throws WorldEditException {
        TreeGenerator.TreeType type = args.argsLength() > 0 ? TreeGenerator.lookup(args.getString(0)) : TreeGenerator.TreeType.TREE;
        double density = args.argsLength() > 1 ? args.getDouble(1) / 100 : 0.05;

        if (type == null) {
            player.printError("Tree type '" + args.getString(0) + "' is unknown.");
            return;
        }

        Region region = session.getSelection(player.getWorld());

        // We want to generate trees
        ForestGenerator generator = new ForestGenerator(editSession, new TreeGenerator(type));

        // And we want to scatter them
        GroundScatterFunction scatter = new GroundScatterFunction(editSession, generator);
        scatter.setDensity(density);
        scatter.setRange(region);

        // Generate that forest
        FlatRegionApplicator operation = new FlatRegionApplicator(region, scatter);
        OperationHelper.complete(operation);

        player.print(operation.getAffected() + " trees created.");
    }

    @Command(
            aliases = { "/flora" },
            usage = "[density]",
            desc = "Make flora within the region",
            min = 0,
            max = 1
    )
    @CommandPermissions("worldedit.region.flora")
    @Logging(REGION)
    public void flora(CommandContext args, LocalSession session, LocalPlayer player, EditSession editSession) throws WorldEditException {
        double density = args.argsLength() > 0 ? args.getDouble(0) / 100 : 0.1;

        Region region = session.getSelection(player.getWorld());

        // We want to generate flora
        FloraGenerator generator = new FloraGenerator(editSession);

        // And we want to scatter them
        GroundScatterFunction scatter = new GroundScatterFunction(editSession, generator);
        scatter.setDensity(density);
        scatter.setRange(region);

        // Generate that flora
        FlatRegionApplicator operation = new FlatRegionApplicator(region, scatter);
        OperationHelper.complete(operation);

        player.print(operation.getAffected() + " flora created.");
    }

}
