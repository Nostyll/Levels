package me.nostyll.Kingdoms.levels.commands.admin;

import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import net.kyori.adventure.text.minimessage.MiniMessage;
import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.commands.CommandManager;
import me.nostyll.Kingdoms.levels.commands.LevelsKingdomsCommand;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import me.nostyll.Kingdoms.levels.game.GameManagement;

public class ForceGui extends LevelsKingdomsCommand{

    public ForceGui(@NonNull LevelsKingdoms plugin, @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
        builder.literal("gui")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, MiniMessage.miniMessage().deserialize(LevelsLang.ADMIN_COMMAND_DESCRIPTION))
                .permission("lk.command.admin.gui")
                .handler(this::executeAdmin));
        
    }

    private void executeAdmin(final @NonNull CommandContext<CommandSender> context) {
        GameManagement.getMenuMananger().buildMenus();
    }
    
}
