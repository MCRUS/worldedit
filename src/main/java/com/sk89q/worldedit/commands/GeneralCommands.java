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
import com.sk89q.minecraft.util.commands.Console;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.ItemType;
import com.sk89q.worldedit.masks.Mask;

/**
 * General WorldEdit commands.
 * 
 * @author sk89q
 */
public class GeneralCommands {
    private final WorldEdit we;

    public GeneralCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
        aliases = { "/limit" },
        usage = "<limit>",
        desc = "Изменяет лимит изменения блоков",
        min = 1,
        max = 1
    )
    @CommandPermissions("worldedit.limit")
    public void limit(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
        
        LocalConfiguration config = we.getConfiguration();

        int limit = Math.max(-1, args.getInteger(0));
        if (!player.hasPermission("worldedit.limit.unrestricted")
                && config.maxChangeLimit > -1) {
            if (limit > config.maxChangeLimit) {
                player.printError("Ваш максимально допустимый лимит "
                        + config.maxChangeLimit + ".");
                return;
            }
        }

        session.setBlockChangeLimit(limit);
        player.print("Лимит изменения блоков сменен на " + limit + ".");
    }

    @Command(
        aliases = { "/fast" },
        usage = "[on|off]",
        desc = "Включает скоростной режим.",
        min = 0,
        max = 1
    )
    @CommandPermissions("worldedit.fast")
    public void fast(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        String newState = args.getString(0, null);
        if (session.hasFastMode()) {
            if ("on".equals(newState)) {
                player.printError("Скоростной режим уже включен.");
                return;
            }

            session.setFastMode(false);
            player.print("Скоростной режим отключен.");
        } else {
            if ("off".equals(newState)) {
                player.printError("Скоростной режим отключен.");
                return;
            }

            session.setFastMode(true);
            player.print("Скоростной режим включен. Освещение в чанках может быть неправильным и Вам прийдется перезайти на сервер, чтобы увидеть изменения.");
        }
    }

    @Command(
        aliases = { "/gmask", "gmask" },
        usage = "[mask]",
        desc = "Установить глобальную маску.",
        min = 0,
        max = -1
    )
    @CommandPermissions("worldedit.global-mask")
    public void gmask(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
        if (args.argsLength() == 0) {
            session.setMask(null);
            player.print("Глобальная маска отключена.");
        } else {
            Mask mask = we.getBlockMask(player, session, args.getJoinedStrings(0));
            session.setMask(mask);
            player.print("Глобальная маска задана.");
        }
    }

    @Command(
        aliases = { "/toggleplace", "toggleplace" },
        usage = "",
        desc = "Переключение между вашим положением и pos1 для размещения",
        min = 0,
        max = 0
    )
    public void togglePlace(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        if (session.togglePlacementPosition()) {
            player.print("Размещение на точке #1.");
        } else {
            player.print("Блок размещен на Вашем месторасположении.");
        }
    }

    @Command(
        aliases = { "/searchitem", "/l", "/search", "searchitem" },
        usage = "<query>",
        flags = "bi",
        desc = "Поиск предметов",
        help =
            "Ищет предметы.\n" +
            "Флаги:\n" +
            "  -b поиск только блоков\n" +
            "  -i поиск только предметов",
        min = 1,
        max = 1
    )
    @Console
    public void searchItem(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
        
        String query = args.getString(0).trim().toLowerCase();
        boolean blocksOnly = args.hasFlag('b');
        boolean itemsOnly = args.hasFlag('i');

        try {
            int id = Integer.parseInt(query);

            ItemType type = ItemType.fromID(id);

            if (type != null) {
                player.print("#" + type.getID() + " (" + type.getName() + ")");
            } else {
                player.printError("Предмета под названием " + id + " не найдено");
            }

            return;
        } catch (NumberFormatException e) {
        }

        if (query.length() <= 2) {
            player.printError("В запросе слишком мало символов (должно быть больше двух).");
            return;
        }

        if (!blocksOnly && !itemsOnly) {
            player.print("Результат для: " + query);
        } else if (blocksOnly && itemsOnly) {
            player.printError("Вы не можете одновременно использовать флаги -b и -i.");
            return;
        } else if (blocksOnly) {
            player.print("Результат для блоков: " + query);
        } else {
            player.print("Результат для предметов: " + query);
        }

        int found = 0;

        for (ItemType type : ItemType.values()) {
            if (found >= 15) {
                player.print("Слишком много было найдено!");
                break;
            }

            if (blocksOnly && type.getID() > 255) {
                continue;
            }

            if (itemsOnly && type.getID() <= 255) {
                continue;
            }

            for (String alias : type.getAliases()) {
                if (alias.contains(query)) {
                    player.print("#" + type.getID() + " (" + type.getName() + ")");
                    ++found;
                    break;
                }
            }
        }

        if (found == 0) {
            player.printError("Предметов не найдено.");
        }
    }

    @Command(
        aliases = { "we", "worldedit" },
        desc = "Команды WorldEdit"
    )
    @NestedCommand(WorldEditCommands.class)
    @Console
    public void we(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
    }
}
