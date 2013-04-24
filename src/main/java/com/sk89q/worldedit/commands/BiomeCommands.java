package com.sk89q.worldedit.commands;

import static com.sk89q.minecraft.util.commands.Logging.LogMode.REGION;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.BiomeType;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.masks.BiomeTypeMask;
import com.sk89q.worldedit.masks.InvertedMask;
import com.sk89q.worldedit.masks.Mask;
import com.sk89q.worldedit.regions.FlatRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.StringUtil;

public class BiomeCommands {

    private WorldEdit we;

    public BiomeCommands(WorldEdit we) {
        this.we = we;
    }

    @Command(
            aliases = {"biomelist", "biomels"},
            usage = "[page]",
            desc = "Получает список всех биомов.",
            max = 1
    )
    @CommandPermissions("worldedit.biome.list")
    public void biomeList(CommandContext args, LocalSession session, LocalPlayer player,
                          EditSession editSession) throws WorldEditException {

        int page;
        int offset;
        int count = 0;

        if (args.argsLength() == 0 || (page = args.getInteger(0)) < 2) {
            page = 1;
            offset = 0;
        } else {
            offset = (page - 1) * 19;
        }

        List<BiomeType> biomes = we.getServer().getBiomes().all();
        int totalPages = biomes.size() / 19 + 1;
        player.print("Доступные биомы (страница " + page + "/" + totalPages + ") :");
        for (BiomeType biome : biomes) {
            if (offset > 0) {
                offset--;
            } else {
                player.print(" " + biome.getName());
                if (++count == 19) {
                    break;
                }
            }
        }
    }

    @Command(
            aliases = {"biomeinfo"},
            flags = "pt",
            desc = "Получает биом блока на который Вы смотрите.",
            help =
                    "Получение биома блока.\n" +
                            "По умолчанию получает все блоки, которые Вы выделили.\n" +
                            "-t использует блок,на который Вы смотрите.\n" +
                            "-p использует блок,на котором Вы стоите",
            max = 0
    )
    @CommandPermissions("worldedit.biome.info")
    public void biomeInfo(CommandContext args, LocalSession session, LocalPlayer player,
                          EditSession editSession) throws WorldEditException {

        if (args.hasFlag('t')) {
            Vector blockPosition = player.getBlockTrace(300);
            if (blockPosition == null) {
                player.printError("Нет блоков в поле зрения!");
                return;
            }

            BiomeType biome = player.getWorld().getBiome(blockPosition.toVector2D());
            player.print("Биом: " + biome.getName());
        } else if (args.hasFlag('p')) {
            BiomeType biome = player.getWorld().getBiome(player.getPosition().toVector2D());
            player.print("Биом: " + biome.getName());
        } else {
            LocalWorld world = player.getWorld();
            Region region = session.getSelection(world);
            Set<BiomeType> biomes = new HashSet<BiomeType>();

            if (region instanceof FlatRegion) {
                for (Vector2D pt : ((FlatRegion) region).asFlatRegion()) {
                    biomes.add(world.getBiome(pt));
                }
            } else {
                for (Vector pt : region) {
                    biomes.add(world.getBiome(pt.toVector2D()));
                }
            }

            player.print("Биомы:");
            for (BiomeType biome : biomes) {
                player.print(" " + biome.getName());
            }
        }
    }

    @Command(
            aliases = {"/setbiome"},
            usage = "<biome>",
            flags = "p",
            desc = "Устанавливает биом текущего блока,на котором Вы стоите или региона.",
            help =
                    "Устанавливает биом региона.\n" +
                            "По умолчанию все блоки в регионе.\n" +
                            "-p использовать блок, на котором Вы сейчас стоите.",
            min = 1,
            max = 1
    )
    @Logging(REGION)
    @CommandPermissions("worldedit.biome.set")
    public void setBiome(CommandContext args, LocalSession session, LocalPlayer player,
                         EditSession editSession) throws WorldEditException {

        final BiomeType target = we.getServer().getBiomes().get(args.getString(0));
        if (target == null) {
            player.printError("Биома '" + args.getString(0) + "' не существует!");
            return;
        }

        Mask mask = editSession.getMask();
        BiomeTypeMask biomeMask = null;
        boolean inverted = false;
        if (mask instanceof BiomeTypeMask) {
            biomeMask = (BiomeTypeMask) mask;
        } else if (mask instanceof InvertedMask && ((InvertedMask) mask).getInvertedMask() instanceof BiomeTypeMask) {
            inverted = true;
            biomeMask = (BiomeTypeMask) ((InvertedMask) mask).getInvertedMask();
        }

        if (args.hasFlag('p')) {
            Vector2D pos = player.getPosition().toVector2D();
            if (biomeMask == null || (biomeMask.matches2D(editSession, pos) ^ inverted)) {
                player.getWorld().setBiome(pos, target);
                player.print("Биом в твоем расположении изменен на " + target.getName() + ".");
            } else {
                player.print("Ваша глобальная макса не соответствует биому. Для отключения маски введите //gmask.");
            }
        } else {
            int affected = 0;
            LocalWorld world = player.getWorld();
            Region region = session.getSelection(world);

            if (region instanceof FlatRegion) {
                for (Vector2D pt : ((FlatRegion) region).asFlatRegion()) {
                    if (biomeMask == null || (biomeMask.matches2D(editSession, pt) ^ inverted)) {
                        world.setBiome(pt, target);
                        ++affected;
                    }
                }
            } else {
                HashSet<Long> alreadyVisited = new HashSet<Long>();
                for (Vector pt : region) {
                    if (!alreadyVisited.contains((long)pt.getBlockX() << 32 | pt.getBlockZ())) {
                        alreadyVisited.add(((long)pt.getBlockX() << 32 | pt.getBlockZ()));
                        if (biomeMask == null || (biomeMask.matches(editSession, pt) ^ inverted)) {
                            world.setBiome(pt.toVector2D(), target);
                            ++affected;
                        }
                    }
                }
            }
            player.print("Биом изменен на " + target.getName() + ". " + affected + " " + StringUtil.plural(affected, "чанк заменен", "чанка заменено", "чанков заменено") + ".");
        }
    }
}
