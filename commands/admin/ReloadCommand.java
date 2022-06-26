package me.nostyll.Kingdoms.levels.commands.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.checkerframework.checker.nullness.qual.NonNull;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.commands.CommandManager;
import me.nostyll.Kingdoms.levels.commands.LevelsKingdomsCommand;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import me.nostyll.Kingdoms.levels.game.GameManagement;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class ReloadCommand extends LevelsKingdomsCommand{

    public ReloadCommand(@NonNull LevelsKingdoms plugin, @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
        builder.literal("reload")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, MiniMessage.miniMessage().deserialize(LevelsLang.RELOAD_COMMAND_DESCRIPTION))
                .permission("lk.command.reload")
                .handler(this::reloadFiles));   
    }

    private void reloadFiles(final @NonNull CommandContext<CommandSender> context){
        final CommandSender sender = context.getSender();
        LevelsKingdoms.config.reload();
        LevelsLang.reload();
        GameManagement.getMenuMananger().reloadAll();
        PluginDescriptionFile desc = plugin.getDescription();
        LevelsLang.send(sender, LevelsLang.PLUGIN_RELOADED,
                Placeholder.parsed("name", desc.getName()),
                Placeholder.parsed("version", desc.getVersion())
        );
    }
    
}
