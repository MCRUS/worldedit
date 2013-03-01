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
import com.sk89q.worldedit.expression.ExpressionException;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.StringUtil;
import com.sk89q.worldedit.util.TreeGenerator;

import static com.sk89q.minecraft.util.commands.Logging.LogMode.*;

/**
 * Generation commands.
 *
 * @author sk89q
 */
public class GenerationCommands {
    private final WorldEdit we;

    public GenerationCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
            aliases = {"/hcyl"},
            usage = "<block> <radius>[,<radius>] [height]",
            desc = "Создает полый цилиндр.",
            help =
                    "Создает полый цилиндр.\n" +
                            "Укажите 2 радиуса через запятую(Высота и ширина)\n" +
                            "Вы можете генерировать эллипсовые цилиндры.\n" +
                            "1-й радиус север/юг, 2-й радиус восток/запад.",
            min = 2,
            max = 3
    )
    @CommandPermissions("worldedit.generation.cylinder")
    @Logging(PLACEMENT)
    public void hcyl(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        Pattern block = we.getBlockPattern(player, args.getString(0));
        String[] radiuses = args.getString(1).split(",");
        final double radiusX, radiusZ;
        switch (radiuses.length) {
            case 1:
                radiusX = radiusZ = Math.max(1, Double.parseDouble(radiuses[0]));
                break;

            case 2:
                radiusX = Math.max(1, Double.parseDouble(radiuses[0]));
                radiusZ = Math.max(1, Double.parseDouble(radiuses[1]));
                break;

            default:
                player.printError("Вы должны указать одно или два значения радиуса.");
                return;
        }
        int height = args.argsLength() > 2 ? args.getInteger(2) : 1;

        Vector pos = session.getPlacementPosition(player);
        int affected = editSession.makeCylinder(pos, block, radiusX, radiusZ, height, false);
        player.print(affected + " " + StringUtil.plural(affected, "блок был создан", "блока было создано", "блоков было создано") + ".");
    }

    @Command(
            aliases = {"/cyl"},
            usage = "<block> <radius>[,<radius>] [height]",
            desc = "Создание цилиндра.",
            help =
                    "Создает цилиндр.\n" +
                            "Укажите 2 радиуса через запятую\n" +
                            "Вы можете создавать эллипсовые цилиндры.\n" +
                            "1-й радиус север/юг, 2-й радиус восток/запад.",
            min = 2,
            max = 3
    )
    @CommandPermissions("worldedit.generation.cylinder")
    @Logging(PLACEMENT)
    public void cyl(CommandContext args, LocalSession session, LocalPlayer player,
                    EditSession editSession) throws WorldEditException {

        Pattern block = we.getBlockPattern(player, args.getString(0));
        String[] radiuses = args.getString(1).split(",");
        final double radiusX, radiusZ;
        switch (radiuses.length) {
            case 1:
                radiusX = radiusZ = Math.max(1, Double.parseDouble(radiuses[0]));
                break;

            case 2:
                radiusX = Math.max(1, Double.parseDouble(radiuses[0]));
                radiusZ = Math.max(1, Double.parseDouble(radiuses[1]));
                break;

            default:
                player.printError("Вы должны указать одно или два значения радиуса.");
                return;
        }
        int height = args.argsLength() > 2 ? args.getInteger(2) : 1;

        Vector pos = session.getPlacementPosition(player);
        int affected = editSession.makeCylinder(pos, block, radiusX, radiusZ, height, true);
        player.print(affected + " " + StringUtil.plural(affected, "блок был создан", "блока было создано", "блоков было создано") + ".");
    }

    @Command(
            aliases = {"/hsphere"},
            usage = "<block> <radius>[,<radius>,<radius>] [raised?]",
            desc = "Генерация полой сферы.",
            help =
                    "Генерирует полую сферу.\n" +
                            "Укажите три радиуса через запятую\n" +
                            "Вы можете сгенерировать эллипсоид.Порядок радиусов эллипсоида\n" +
                            "это север/юг, вверх/вниз, восток/запад.",
            min = 2,
            max = 3
    )
    @CommandPermissions("worldedit.generation.sphere")
    @Logging(PLACEMENT)
    public void hsphere(CommandContext args, LocalSession session, LocalPlayer player,
                        EditSession editSession) throws WorldEditException {

        final Pattern block = we.getBlockPattern(player, args.getString(0));
        String[] radiuses = args.getString(1).split(",");
        final double radiusX, radiusY, radiusZ;
        switch (radiuses.length) {
            case 1:
                radiusX = radiusY = radiusZ = Math.max(1, Double.parseDouble(radiuses[0]));
                break;

            case 3:
                radiusX = Math.max(1, Double.parseDouble(radiuses[0]));
                radiusY = Math.max(1, Double.parseDouble(radiuses[1]));
                radiusZ = Math.max(1, Double.parseDouble(radiuses[2]));
                break;

            default:
                player.printError("Вы должны указать одно или три значения радиуса.");
                return;
        }
        final boolean raised;
        if (args.argsLength() > 2) {
            raised = args.getString(2).equalsIgnoreCase("true") || args.getString(2).equalsIgnoreCase("yes");
        } else {
            raised = false;
        }

        Vector pos = session.getPlacementPosition(player);
        if (raised) {
            pos = pos.add(0, radiusY, 0);
        }

        int affected = editSession.makeSphere(pos, block, radiusX, radiusY, radiusZ, false);
        player.findFreePosition();
        player.print(affected + " " + StringUtil.plural(affected, "блок был создан", "блока было создано", "блоков было создано") + ".");
    }

    @Command(
            aliases = {"/sphere"},
            usage = "<block> <radius>[,<radius>,<radius>] [raised?]",
            desc = "Генерация сферы.",
            help =
                    "Генерирует сферу.\n" +
                            "Укажите три радуса через запятую,\n" +
                            "Вы можете генерировать эллипсоид. Порядок радиусов эллипсоида\n" +
                            "это север/юг, вверх/вниз, восток/запад.",
            min = 2,
            max = 3
    )
    @CommandPermissions("worldedit.generation.sphere")
    @Logging(PLACEMENT)
    public void sphere(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {

        Pattern block = we.getBlockPattern(player, args.getString(0));
        String[] radiuses = args.getString(1).split(",");
        final double radiusX, radiusY, radiusZ;
        switch (radiuses.length) {
            case 1:
                radiusX = radiusY = radiusZ = Math.max(1, Double.parseDouble(radiuses[0]));
                break;

            case 3:
                radiusX = Math.max(1, Double.parseDouble(radiuses[0]));
                radiusY = Math.max(1, Double.parseDouble(radiuses[1]));
                radiusZ = Math.max(1, Double.parseDouble(radiuses[2]));
                break;

            default:
                player.printError("Вы должны указать одно или три значения радиуса.");
                return;
        }
        final boolean raised;
        if (args.argsLength() > 2) {
            raised = args.getString(2).equalsIgnoreCase("true") || args.getString(2).equalsIgnoreCase("yes");
        } else {
            raised = false;
        }

        Vector pos = session.getPlacementPosition(player);
        if (raised) {
            pos = pos.add(0, radiusY, 0);
        }

        int affected = editSession.makeSphere(pos, block, radiusX, radiusY, radiusZ, true);
        player.findFreePosition();
        player.print(affected + " " + StringUtil.plural(affected, "блок был создан", "блока было создано", "блоков было создано") + ".");
    }

    @Command(
            aliases = {"forestgen"},
            usage = "[size] [type] [density]",
            desc = "Генерирует лес.",
            min = 0,
            max = 3
    )
    @CommandPermissions("worldedit.generation.forest")
    @Logging(POSITION)
    @SuppressWarnings("deprecation")
    public void forestGen(CommandContext args, LocalSession session, LocalPlayer player,
                          EditSession editSession) throws WorldEditException {

        int size = args.argsLength() > 0 ? Math.max(1, args.getInteger(0)) : 10;
        TreeGenerator.TreeType type = args.argsLength() > 1 ?
                type = TreeGenerator.lookup(args.getString(1))
                : TreeGenerator.TreeType.TREE;
        double density = args.argsLength() > 2 ? args.getDouble(2) / 100 : 0.05;

        if (type == null) {
            player.printError("Типа деревьев '" + args.getString(1) + "' не существует.");
            return;
        }

        int affected = editSession.makeForest(session.getPlacementPosition(player),
                size, density, new TreeGenerator(type));
        player.print(affected + " " + StringUtil.plural(affected, "дерево было создано", "дерева было создано", "деревьев было создано") + ".");
    }

    @Command(
            aliases = {"pumpkins"},
            usage = "[size]",
            desc = "Генерирует тыквы",
            min = 0,
            max = 1
    )
    @CommandPermissions("worldedit.generation.pumpkins")
    @Logging(POSITION)
    public void pumpkins(CommandContext args, LocalSession session, LocalPlayer player,
                         EditSession editSession) throws WorldEditException {

        int size = args.argsLength() > 0 ? Math.max(1, args.getInteger(0)) : 10;

        int affected = editSession.makePumpkinPatches(session.getPlacementPosition(player), size);
        player.print(affected + " " + StringUtil.plural(affected, "тыква была создано", "тыквы было создано", "тыкв было создано") + ".");
    }

    @Command(
            aliases = {"/pyramid"},
            usage = "<block> <size>",
            desc = "Генерирует пирамиду.",
            min = 2,
            max = 2
    )
    @CommandPermissions("worldedit.generation.pyramid")
    @Logging(PLACEMENT)
    public void pyramid(CommandContext args, LocalSession session, LocalPlayer player,
                        EditSession editSession) throws WorldEditException {

        Pattern block = we.getBlockPattern(player, args.getString(0));
        int size = Math.max(1, args.getInteger(1));
        Vector pos = session.getPlacementPosition(player);

        int affected = editSession.makePyramid(pos, block, size, true);

        player.findFreePosition();
        player.print(affected + " " + StringUtil.plural(affected, "блок был создан", "блока было создано", "блоков было создано") + ".");
    }

    @Command(
            aliases = {"/hpyramid"},
            usage = "<block> <size>",
            desc = "Генерирует полую пирамиду.",
            min = 2,
            max = 2
    )
    @CommandPermissions("worldedit.generation.pyramid")
    @Logging(PLACEMENT)
    public void hpyramid(CommandContext args, LocalSession session, LocalPlayer player,
                         EditSession editSession) throws WorldEditException {

        Pattern block = we.getBlockPattern(player, args.getString(0));
        int size = Math.max(1, args.getInteger(1));
        Vector pos = session.getPlacementPosition(player);

        int affected = editSession.makePyramid(pos, block, size, false);

        player.findFreePosition();
        player.print(affected + " " + StringUtil.plural(affected, "блок был создан", "блока было создано", "блоков было создано") + ".");
    }

    @Command(
            aliases = {"/generate", "/gen", "/g"},
            usage = "<block> <expression>",
            desc = "Создает форму в соответствии с формулой.",
            help =
                    "Создает форму в соответствии с формулой, которая \n" +
                            "вернет положительные числа(истина), если точка находится внутри формы \n" +
                            "По желанию можно указать тип/данные на нужный блок..\n" +
                            "Флаги:\n" +
                            "  -h для генерации полой формы\n" +
                            "  -r для использования координат Minecraft\n" +
                            "  -o как флаг -r, кроме смещения от размещения.\n" +
                            "  -c как флаг -r, кроме выбора смещения центра.\n" +
                            "Если нет ни флага -r, ни -o, то выбор отображается до -1 .. 1\n" +
                            "Также смотрите на tinyurl.com/wesyntax.",
            flags = "hroc",
            min = 2,
            max = -1
    )
    @CommandPermissions("worldedit.generation.shape")
    @Logging(ALL)
    public void generate(CommandContext args, LocalSession session, LocalPlayer player,
                         EditSession editSession) throws WorldEditException {

        final Pattern pattern = we.getBlockPattern(player, args.getString(0));
        final Region region = session.getSelection(player.getWorld());

        final boolean hollow = args.hasFlag('h');

        final String expression = args.getJoinedStrings(1);

        final Vector zero;
        Vector unit;

        if (args.hasFlag('r')) {
            zero = new Vector(0, 0, 0);
            unit = new Vector(1, 1, 1);
        } else if (args.hasFlag('o')) {
            zero = session.getPlacementPosition(player);
            unit = new Vector(1, 1, 1);
        } else if (args.hasFlag('c')) {
            final Vector min = region.getMinimumPoint();
            final Vector max = region.getMaximumPoint();

            zero = max.add(min).multiply(0.5);
            unit = new Vector(1, 1, 1);
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
            final int affected = editSession.makeShape(region, zero, unit, pattern, expression, hollow);
            player.findFreePosition();
            player.print(affected + " " + StringUtil.plural(affected, "блок был создан", "блока было создано", "блоков было создано") + ".");
        } catch (ExpressionException e) {
            player.printError(e.getMessage());
        }
    }
}
