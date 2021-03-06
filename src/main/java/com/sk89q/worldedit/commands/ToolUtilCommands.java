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
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.masks.Mask;
import com.sk89q.worldedit.patterns.Pattern;

/**
 * Tool commands.
 * 
 * @author sk89q
 */
public class ToolUtilCommands {
    private final WorldEdit we;

    public ToolUtilCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
        aliases = { "/", "," },
        usage = "[on|off]",
        desc = "Переключет состояние супер-кирки",
        min = 0,
        max = 1
    )
    @CommandPermissions("worldedit.superpickaxe")
    public void togglePickaxe(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        String newState = args.getString(0, null);
        if (session.hasSuperPickAxe()) {
            if ("on".equals(newState)) {
                player.printError("Супер-кирка и так включена.");
                return;
            }

            session.disableSuperPickAxe();
            player.print("Супер-кирка выключена.");
        } else {
            if ("off".equals(newState)) {
                player.printError("Супер-кирка и так выключена.");
                return;
            }
            session.enableSuperPickAxe();
            player.print("Супер-кирка выключена.");
        }

    }

    @Command(
        aliases = { "superpickaxe", "pickaxe", "sp" },
        desc = "Выбирает режим супер-кирки"
    )
    @NestedCommand(SuperPickaxeCommands.class)
    public void pickaxe(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
    }

    @Command(
        aliases = {"tool"},
        desc = "Выбирает предмет для привязки"
    )
    @NestedCommand(ToolCommands.class)
    public void tool(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
    }

    @Command(
        aliases = { "mask" },
        usage = "[mask]",
        desc = "Выставляет маску кисти",
        min = 0,
        max = -1
    )
    @CommandPermissions("worldedit.brush.options.mask")
    public void mask(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
        if (args.argsLength() == 0) {
            session.getBrushTool(player.getItemInHand()).setMask(null);
            player.print("Маска кисти очищена.");
        } else {
            Mask mask = we.getBlockMask(player, session, args.getJoinedStrings(0));
            session.getBrushTool(player.getItemInHand()).setMask(mask);
            player.print("Маска кисти выставлена.");
        }
    }

    @Command(
        aliases = { "mat", "material" },
        usage = "[pattern]",
        desc = "Выставляет материал кисти",
        min = 1,
        max = 1
    )
    @CommandPermissions("worldedit.brush.options.material")
    public void material(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
        Pattern pattern = we.getBlockPattern(player, args.getString(0));
        session.getBrushTool(player.getItemInHand()).setFill(pattern);
        player.print("Материал кисти выставлен.");
    }

    @Command(
            aliases = { "range" },
            usage = "[pattern]",
            desc = "Выставляет диапазон кисти",
            min = 1,
            max = 1
        )
    @CommandPermissions("worldedit.brush.options.range")
    public void range(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {
        int range = args.getInteger(0);
        session.getBrushTool(player.getItemInHand()).setRange(range);
        player.print("Размер диапазон выставлен.");
    }

    @Command(
        aliases = { "size" },
        usage = "[pattern]",
        desc = "Выставляет размер кисти",
        min = 1,
        max = 1
    )
    @CommandPermissions("worldedit.brush.options.size")
    public void size(CommandContext args, LocalSession session, LocalPlayer player,
            EditSession editSession) throws WorldEditException {

        int radius = args.getInteger(0);
        we.checkMaxBrushRadius(radius);

        session.getBrushTool(player.getItemInHand()).setSize(radius);
        player.print("Размер кисти выставлен.");
    }
}
