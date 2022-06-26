package me.nostyll.Kingdoms.levels.commands.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.kingdoms.constants.player.KingdomPlayer;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import net.kyori.adventure.text.minimessage.MiniMessage;
import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.commands.CommandManager;
import me.nostyll.Kingdoms.levels.commands.LevelsKingdomsCommand;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import me.nostyll.Kingdoms.levels.gui.adminMode.AdminModeGui;
import me.nostyll.Kingdoms.levels.utils.CommandUtil;

public class AdminCommand extends LevelsKingdomsCommand{

    public AdminCommand(@NonNull LevelsKingdoms plugin, @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
        builder.literal("admin")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, MiniMessage.miniMessage().deserialize(LevelsLang.ADMIN_COMMAND_DESCRIPTION))
                .permission("lk.command.admin")
                .handler(this::executeAdmin));
        
    }

    private void executeAdmin(final @NonNull CommandContext<CommandSender> context) {
        final Player player = CommandUtil.resolvePlayer(context);

        KingdomPlayer kPlayer = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
        boolean toggle = kPlayer.isAdmin();
        if (toggle){
            AdminModeGui.openAdminMenu(player, kPlayer);
        }
    }
}
