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

import static com.sk89q.minecraft.util.commands.Logging.LogMode.*;

import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.StringUtil;

/**
 * Clipboard commands.
 *
 * @author sk89q
 */
public class ClipboardCommands {
    private final WorldEdit we;

    public ClipboardCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
            aliases = {"/copy"},
            flags = "e",
            desc = "Копирует выделенную территорию в буфер обмена",
            help = "Копирует выделенную территорию в буфер обмен\n" +
                    "Флаги:\n" +
                    "  -e определяет, будут ли объекты копироваться в буфер обмена\n" +
                    "ПРЕДУПРЕЖДЕНИЕ: Вставленные объекты не могут быть отменены!",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.clipboard.copy")
    public void copy(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        Region region = session.getSelection(player.getWorld());
        Vector min = region.getMinimumPoint();
        Vector max = region.getMaximumPoint();
        Vector pos = session.getPlacementPosition(player);

        CuboidClipboard clipboard = new CuboidClipboard(
                max.subtract(min).add(new Vector(1, 1, 1)),
                min, min.subtract(pos));
        clipboard.copy(editSession);
        if (args.hasFlag('e')) {
            for (LocalEntity entity : player.getWorld().getEntities(region)) {
                clipboard.storeEntity(entity);
            }
        }
        session.setClipboard(clipboard);

        player.print("Блок(и) скопирован(ы).");
    }

    @Command(
            aliases = {"/cut"},
            usage = "[leave-id]",
            desc = "Вырезает выделенную территорию в буфер обмена",
            help = "Вырезает выделенную территорию в буфер обмена\n" +
                    "Флаги:\n" +
                    "  -e controls определяет, будут ли объекты копироваться в буфер обмена\n" +
                    "ПРЕДУПРЕЖДЕНИЕ: Вырезанные и вставленные объекты не могут быть отменены!",
            flags = "e",
            min = 0,
            max = 1
    )
    @CommandPermissions("worldedit.clipboard.cut")
    @Logging(REGION)
    public void cut(CommandContext args, LocalSession session, LocalPlayer player,
                    EditSession editSession) throws WorldEditException {

        BaseBlock block = new BaseBlock(BlockID.AIR);
        LocalWorld world = player.getWorld();

        if (args.argsLength() > 0) {
            block = we.getBlock(player, args.getString(0));
        }

        Region region = session.getSelection(world);
        Vector min = region.getMinimumPoint();
        Vector max = region.getMaximumPoint();
        Vector pos = session.getPlacementPosition(player);

        CuboidClipboard clipboard = new CuboidClipboard(
                max.subtract(min).add(new Vector(1, 1, 1)),
                min, min.subtract(pos));
        clipboard.copy(editSession);
        if (args.hasFlag('e')) {
            LocalEntity[] entities = world.getEntities(region);
            for (LocalEntity entity : entities) {
                clipboard.storeEntity(entity);
            }
            world.killEntities(entities);
        }
        session.setClipboard(clipboard);

        int affected = editSession.setBlocks(session.getSelection(world), block);
        player.print(affected + " " + StringUtil.plural(affected, "блок вырезан", "блока вырезано", "блоков вырезано") + ".");
    }

    @Command(
            aliases = {"/paste"},
            usage = "",
            flags = "ao",
            desc = "Вставляет содержимое буфера обмена",
            help =
                    "Вставить содержимое буфера обмена.\n" +
                            "Флаги:\n" +
                            "  -a пропускает блоки воздуха\n" +
                            "  -o вставляет на позициях, которые были скопированы/вырезаны",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.clipboard.paste")
    @Logging(PLACEMENT)
    public void paste(CommandContext args, LocalSession session, LocalPlayer player,
                      EditSession editSession) throws WorldEditException {

        boolean atOrigin = args.hasFlag('o');
        boolean pasteNoAir = args.hasFlag('a');

        if (atOrigin) {
            Vector pos = session.getClipboard().getOrigin();
            session.getClipboard().place(editSession, pos, pasteNoAir);
            session.getClipboard().pasteEntities(pos);
            player.findFreePosition();
            player.print("Вставлено. Для отмены напишите //undo");
        } else {
            Vector pos = session.getPlacementPosition(player);
            session.getClipboard().paste(editSession, pos, pasteNoAir, true);
            player.findFreePosition();
            player.print("Вставлено. Для отмены напишите //undo");
        }
    }

    @Command(
            aliases = {"/rotate"},
            usage = "<angle-in-degrees>",
            desc = "Поворот содержимого буфера обмена",
            min = 1,
            max = 1
    )
    @CommandPermissions("worldedit.clipboard.rotate")
    public void rotate(CommandContext args, LocalSession session, LocalPlayer player,
                       EditSession editSession) throws WorldEditException {

        int angle = args.getInteger(0);

        if (angle % 90 == 0) {
            CuboidClipboard clipboard = session.getClipboard();
            clipboard.rotate2D(angle);
            player.print("Содержимое буфера обмена повернуто на " + angle + " градусов.");
        } else {
            player.printError("Углы должны делиться на 90 градусов.");
        }
    }

    @Command(
            aliases = {"/flip"},
            usage = "[dir]",
            flags = "p",
            desc = "Переворачивает содержимое буфера обмена.",
            help =
                    "Переворачивает содержимое буфера обмена.\n" +
                            "Флаг -p переворачивает выделенную территорию вокруг игрока,\n" +
                            "а не центра выделения.",
            min = 0,
            max = 1
    )
    @CommandPermissions("worldedit.clipboard.flip")
    public void flip(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {

        CuboidClipboard.FlipDirection dir = we.getFlipDirection(player,
                args.argsLength() > 0 ? args.getString(0).toLowerCase() : "me");

        CuboidClipboard clipboard = session.getClipboard();
        clipboard.flip(dir, args.hasFlag('p'));
        player.print("Содержимое буфера обмена перевернуто.");
    }

    @Command(
            aliases = {"/load"},
            usage = "<filename>",
            desc = "Загружает схематический файл в буфер обмена.",
            min = 0,
            max = 1
    )
    @Deprecated
    @CommandPermissions("worldedit.clipboard.load")
    public void load(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {
        player.printError("Эта команда больше не используется. Пишите //schematic load.");
    }

    @Command(
            aliases = {"/save"},
            usage = "<filename>",
            desc = "Сохраняет буфер обмена в схематический файл.",
            min = 0,
            max = 1
    )
    @Deprecated
    @CommandPermissions("worldedit.clipboard.save")
    public void save(CommandContext args, LocalSession session, LocalPlayer player,
                     EditSession editSession) throws WorldEditException {
        player.printError("Эта команда больше не используется. Пишите //schematic save.");
    }

    @Command(
            aliases = {"/schematic", "/schem"},
            desc = "Команды, связанные с буфером обмена и схематическими файлами."
    )
    @NestedCommand(SchematicCommands.class)
    public void schematic() {
    }

    @Command(
            aliases = {"clearclipboard"},
            usage = "",
            desc = "Очищает Ваш буфер обмена",
            min = 0,
            max = 0
    )
    @CommandPermissions("worldedit.clipboard.clear")
    public void clearClipboard(CommandContext args, LocalSession session, LocalPlayer player,
                               EditSession editSession) throws WorldEditException {

        session.setClipboard(null);
        player.print("Буфер обмена очищен.");
    }
}
