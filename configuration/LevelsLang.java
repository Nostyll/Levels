package me.nostyll.Kingdoms.levels.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;

public final class LevelsLang {

    // MiniMessage formatted strings, to be sent using Logger.info(String, Template...) or Lang.send(CommandSender, String, Template...)
    @LangKey("command.message.console-must-specify-player")
    public static String CONSOLE_MUST_SPECIFY_PLAYER = "<red>You must specify a target player when running this command from console";
    @LangKey("command.message.player-not-found-for-input")
    public static String PLAYER_NOT_FOUND_FOR_INPUT = "<red>No player found for input '<input>'";
    @LangKey("command.message.confirmation-required")
    public static String CONFIRMATION_REQUIRED_MESSAGE = "<red>Confirmation required. Confirm using /<command> confirm.";
    @LangKey("command.message.no-pending-commands")
    public static String NO_PENDING_COMMANDS_MESSAGE = "<red>You don't have any pending commands.";

    @LangKey("click-for-help")
    public static String CLICK_FOR_HELP = "Click for help";
    @LangKey("click-to-confirm")
    public static String CLICK_TO_CONFIRM = "Click to confirm";

    @LangKey("command.prefix")
    public static String COMMAND_PREFIX = "<white>[<gradient:#C028FF:#5B00FF>LevelsKingdoms</gradient>]</white> ";
    @LangKey("command.description.help")
    public static String HELP_COMMAND_DESCRIPTION = "Get help for LevelsKingdoms commands";
    @LangKey("command.description.confirm")
    public static String CONFIRM_COMMAND_DESCRIPTION = "Confirm a pending command";
    @LangKey("command.description.reload")
    public static String RELOAD_COMMAND_DESCRIPTION = "Reloads the plugin";
    @LangKey("command.description.reload")
    public static String ADMIN_COMMAND_DESCRIPTION = "Admin command to controle every aspect of the levels";
    @LangKey("command.argument.help-query")
    public static String HELP_QUERY_ARGUMENT_DESCRIPTION = "Help Query";

    @LangKey("plugin-reloaded")
    public static String PLUGIN_RELOADED = "<green><name> v<version> reloaded";


    private static void init() {
        Arrays.stream(LevelsLang.class.getDeclaredFields())
                .filter(LevelsLang::annotatedString)
                .forEach(LevelsLang::loadValue);
    }

    private static boolean annotatedString(final @NonNull Field field) {
        return field.getType().equals(String.class)
                && field.getDeclaredAnnotation(LangKey.class) != null;
    }

    private static void loadValue(final @NonNull Field field) {
        final LangKey langKey = field.getDeclaredAnnotation(LangKey.class);
        try {
            field.set(null, getString(langKey.value(), (String) field.get(null)));
        } catch (IllegalAccessException e) {
            LevelsKingdoms.logDebug("ERROR: " + e);
        }
    }

    private static YamlConfiguration config;

    public static void reload() {
        File configFile = new File(LevelsKingdoms.getInstance().getDataFolder(), "lang.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            LevelsKingdoms.logDebug("Could not load lang.yml , please correct your syntax errors" +  ex);
            throw new RuntimeException(ex);
        }
        config.options().copyDefaults(true);

        init();

        try {
            config.save(configFile);
        } catch (IOException ex) {
            LevelsKingdoms.logDebug("Could not save " + configFile + "Error: " + ex);
        }
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    public static @NonNull Component parse(final @NonNull String miniMessage) {
        return MiniMessage.miniMessage().deserialize(miniMessage);
    }

    public static @NonNull Component parse(final @NonNull String miniMessage, final @NonNull TagResolver @NonNull ... placeholders) {
        return MiniMessage.miniMessage().deserialize(miniMessage, placeholders);
    }

    public static void send(final @NonNull Audience recipient, final @NonNull String miniMessage, final @NonNull TagResolver @NonNull ... placeholders) {
        recipient.sendMessage(parse(miniMessage, placeholders));
    }

    public static void send(final @NonNull Audience recipient, final @NonNull String miniMessage) {
        recipient.sendMessage(parse(miniMessage));
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface LangKey {
        @NonNull String value();
    }
}