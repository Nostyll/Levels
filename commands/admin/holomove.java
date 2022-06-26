package me.nostyll.Kingdoms.levels.commands.admin;

import com.cryptomorin.xseries.messages.ActionBar;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import github.scarsz.discordsrv.dependencies.net.kyori.text.format.TextColor;
import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.commands.CommandManager;
import me.nostyll.Kingdoms.levels.commands.LevelsKingdomsCommand;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import me.nostyll.Kingdoms.levels.game.GameManagement;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class holomove extends LevelsKingdomsCommand{

    protected holomove(@NonNull LevelsKingdoms plugin, @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
        builder.literal("holostop")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, MiniMessage.miniMessage().deserialize(LevelsLang.ADMIN_COMMAND_DESCRIPTION))
                .permission("lk.command.holostop")
                .handler(this::executeAdmin));
    }

    private void executeAdmin(final @NonNull CommandContext<CommandSender> context) {
        final CommandSender sender = context.getSender();
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player)sender;
        if (GameManagement.getadminManager().isUserEditing(player)){
            GameManagement.getadminManager().removeUserEditing(player);
            ActionBar.sendActionBar(plugin, player, TextColor.DARK_RED + "You are no longer in HoloEdit.", 5*20);
        }
    }
    
}
