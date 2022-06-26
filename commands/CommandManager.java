package me.nostyll.Kingdoms.levels.commands;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.commands.admin.AdminCommand;
import me.nostyll.Kingdoms.levels.commands.admin.ForceGui;
import me.nostyll.Kingdoms.levels.commands.admin.HelpCommand;
import me.nostyll.Kingdoms.levels.commands.admin.ReloadCommand;
import me.nostyll.Kingdoms.levels.commands.exception.CompletedSuccessfullyException;
import me.nostyll.Kingdoms.levels.commands.exception.ConsoleMustProvideWorldException;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import cloud.commandframework.Command;
import cloud.commandframework.brigadier.CloudBrigadierManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.exceptions.CommandExecutionException;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.UnaryOperator;

public final class CommandManager extends PaperCommandManager<CommandSender> {

    public CommandManager(final @NonNull LevelsKingdoms plugin) throws Exception {

        super(
                plugin,
                CommandExecutionCoordinator.simpleCoordinator(),
                UnaryOperator.identity(),
                UnaryOperator.identity()
        );

        if (this.queryCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            this.registerBrigadier();
            final CloudBrigadierManager<?, ?> brigManager = this.brigadierManager();
            if (brigManager != null) {
                brigManager.setNativeNumberSuggestions(false);
            }
        }

        if (this.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.registerAsynchronousCompletions();
        }

        this.registerExceptionHandlers(plugin);

        ImmutableList.of(
                new HelpCommand(plugin, this),
                new AdminCommand(plugin, this),
                new ReloadCommand(plugin, this),
                new ForceGui(plugin, this)
        ).forEach(LevelsKingdomsCommand::register);

    }

    private void registerExceptionHandlers(final @NonNull LevelsKingdoms plugin) {
        new MinecraftExceptionHandler<CommandSender>()
                .withDefaultHandlers()
                .withDecorator(component -> Component.text()
                        .append(MiniMessage.miniMessage().deserialize(LevelsLang.COMMAND_PREFIX)
                                .hoverEvent(MiniMessage.miniMessage().deserialize(LevelsLang.CLICK_FOR_HELP))
                                .clickEvent(ClickEvent.runCommand(String.format("/%s help", LevelsKingdoms.config.MAIN_COMMAND_LABEL))))
                        .append(component)
                        .build())
                .apply(this, AudienceProvider.nativeAudience());

                final var minecraftExtrasDefaultHandler = Objects.requireNonNull(this.getExceptionHandler(CommandExecutionException.class));
                this.registerExceptionHandler(CommandExecutionException.class, (sender, exception) -> {
                    final Throwable cause = exception.getCause();
        
                    if (cause instanceof CompletedSuccessfullyException) {
                        return;
                    } else if (cause instanceof ConsoleMustProvideWorldException) {
                        LevelsKingdoms.logDebug("No world found");
                        return;
                    }
        
                    minecraftExtrasDefaultHandler.accept(sender, exception);
                });
            }

    public void registerSubcommand(UnaryOperator<Command.Builder<CommandSender>> builderModifier) {
        this.command(builderModifier.apply(this.rootBuilder()));
    }

    private Command.@NonNull Builder<CommandSender> rootBuilder() {
        return this.commandBuilder(LevelsKingdoms.config.MAIN_COMMAND_LABEL, LevelsKingdoms.config.MAIN_COMMAND_ALIASES.toArray(String[]::new))
                /* MinecraftHelp uses the MinecraftExtrasMetaKeys.DESCRIPTION meta, this is just so we give Bukkit a description
                 * for our commands in the Bukkit and EssentialsX '/help' command */
                .meta(CommandMeta.DESCRIPTION, String.format("LevelsKingdoms command. '/%s help'", LevelsKingdoms.config.MAIN_COMMAND_LABEL));
    }

}
