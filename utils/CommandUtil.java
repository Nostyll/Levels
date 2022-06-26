package me.nostyll.Kingdoms.levels.utils;

import cloud.commandframework.bukkit.arguments.selector.SinglePlayerSelector;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.RichDescription;
import me.nostyll.Kingdoms.levels.commands.exception.CompletedSuccessfullyException;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CommandUtil {
    private CommandUtil() {
    }

    public static @NonNull Player resolvePlayer(final @NonNull CommandContext<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final SinglePlayerSelector selector = context.getOrDefault("player", null);

        if (selector == null) {
            if (sender instanceof Player) {
                return (Player) sender;
            }
            LevelsLang.send(sender, LevelsLang.CONSOLE_MUST_SPECIFY_PLAYER);
            throw new CompletedSuccessfullyException();
        }

        final Player targetPlayer = selector.getPlayer();
        if (targetPlayer == null) {
            LevelsLang.send(sender, LevelsLang.PLAYER_NOT_FOUND_FOR_INPUT, Placeholder.parsed("input", selector.getSelector()));
            throw new CompletedSuccessfullyException();
        }

        return targetPlayer;
    }

    public static @NonNull RichDescription description(final @NonNull String miniMessage, @NonNull TagResolver @NonNull ... placeholders) {
        return RichDescription.of(MiniMessage.miniMessage().deserialize(miniMessage, placeholders));
    }

}
