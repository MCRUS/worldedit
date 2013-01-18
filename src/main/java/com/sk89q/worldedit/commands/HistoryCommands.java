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
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.masks.Mask;

/**
 * History little commands.
 * 
 * @author sk89q
 */
public class HistoryCommands {
    private final WorldEdit we;
    
    public HistoryCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
        aliases = { "/undo", "undo" },
        usage = "[times] [player]",
        desc = "Отменяет последнее действие",
        min = 0,
        max = 2
    )
    @CommandPermissions("worldedit.history.undo")
    public void undo(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        int times = Math.max(1, args.getInteger(0, 1));

        Mask mask = session.getMask();
        session.setMask(null);

        for (int i = 0; i < times; ++i) {
            EditSession undone;
            if (args.argsLength() < 2) {
                undone = session.undo(session.getBlockBag(player), player);
            } else {
                player.checkPermission("worldedit.history.undo.other");
                LocalSession sess = we.getSession(args.getString(1));
                if (sess == null) {
                    player.printError("Невозможно найти сессию для " + args.getString(1));
                    break;
                }
                undone = sess.undo(session.getBlockBag(player), player);
            }

            if (undone != null) {
                player.print("Успешно отменено.");
                we.flushBlockBag(player, undone);
            } else {
                player.printError("Отменять нечего.");
                break;
            }
        }

        session.setMask(mask);
    }

    @Command(
        aliases = { "/redo", "redo" },
        usage = "[times] [player]",
        desc = "Возвращает действие (из истории)",
        min = 0,
        max = 2
    )
    @CommandPermissions("worldedit.history.redo")
    public void redo(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        int times = Math.max(1, args.getInteger(0, 1));

        Mask mask = session.getMask();
        session.setMask(null);

        for (int i = 0; i < times; ++i) {
            EditSession redone;
            if (args.argsLength() < 2) {
                redone = session.redo(session.getBlockBag(player), player);
            } else {
                player.checkPermission("worldedit.history.redo.other");
                LocalSession sess = we.getSession(args.getString(1));
                if (sess == null) {
                    player.printError("Невозможно найти сессию для " + args.getString(1));
                    break;
                }
                redone = sess.redo(session.getBlockBag(player), player);
            }

            if (redone != null) {
                player.print("Успешно восстановлено.");
                we.flushBlockBag(player, redone);
            } else {
                player.printError("Восстановливать нечего.");
            }
        }

        session.setMask(mask);
    }

    @Command(
        aliases = { "/clearhistory", "clearhistory" },
        usage = "",
        desc = "Очищает Вашу историю",
        min = 0,
        max = 0
    )
    @CommandPermissions("worldedit.history.clear")
    public void clearHistory(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        session.clearHistory();
        player.print("История очищена.");
    }
}
