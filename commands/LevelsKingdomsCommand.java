package me.nostyll.Kingdoms.levels.commands;

import org.checkerframework.checker.nullness.qual.NonNull;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;

public abstract class LevelsKingdomsCommand {
    protected final LevelsKingdoms plugin;
    protected final CommandManager commandManager;

    protected LevelsKingdomsCommand(
            final @NonNull LevelsKingdoms plugin,
            final @NonNull CommandManager commandManager
    ) {
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    public abstract void register();
}
