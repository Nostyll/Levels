package me.nostyll.Kingdoms.levels.commands.admin;

import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import cloud.commandframework.minecraft.extras.MinecraftHelp;

import cloud.commandframework.CommandHelpHandler;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.commands.CommandManager;
import me.nostyll.Kingdoms.levels.commands.LevelsKingdomsCommand;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import me.nostyll.Kingdoms.levels.utils.CommandUtil;


import java.util.stream.Collectors;

public class HelpCommand extends LevelsKingdomsCommand{

    private final MinecraftHelp<CommandSender> minecraftHelp;

    public HelpCommand(final @NonNull LevelsKingdoms plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
        this.minecraftHelp = new MinecraftHelp<>(
                String.format("/%s help", "LevelsKingdoms"),
                AudienceProvider.nativeAudience(),
                commandManager
        );
        this.minecraftHelp.setHelpColors(MinecraftHelp.HelpColors.of(
                TextColor.color(0x5B00FF),
                NamedTextColor.WHITE,
                TextColor.color(0xC028FF),
                NamedTextColor.GRAY,
                NamedTextColor.DARK_GRAY
        ));
        this.minecraftHelp.setMessage(MinecraftHelp.MESSAGE_HELP_TITLE, "LevelsKingdoms Help");
    }

    @Override
    public void register() {
        final var commandHelpHandler = this.commandManager.getCommandHelpHandler();
        final var helpQueryArgument = StringArgument.<CommandSender>newBuilder("query")
                .greedy()
                .asOptional()
                .withSuggestionsProvider((context, input) -> {
                    final var indexHelpTopic = (CommandHelpHandler.IndexHelpTopic<CommandSender>) commandHelpHandler.queryHelp(context.getSender(), "");
                    return indexHelpTopic.getEntries()
                            .stream()
                            .map(CommandHelpHandler.VerboseHelpEntry::getSyntaxString)
                            .collect(Collectors.toList());
                })
                .build();

        this.commandManager.registerSubcommand(builder ->
                builder.literal("help")
                        .meta(MinecraftExtrasMetaKeys.DESCRIPTION, MiniMessage.miniMessage().deserialize(LevelsLang.HELP_COMMAND_DESCRIPTION))
                        .argument(helpQueryArgument, CommandUtil.description(LevelsLang.HELP_QUERY_ARGUMENT_DESCRIPTION))
                        .permission("lk.command.help")
                        .handler(this::executeHelp));
    }

    private void executeHelp(final @NonNull CommandContext<CommandSender> context) {
        this.minecraftHelp.queryCommands(
                context.<String>getOptional("query").orElse(""),
                context.getSender()
        );
    }
}
