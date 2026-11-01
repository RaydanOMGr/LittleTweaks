package me.andreasmelone.littletweaks;

import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class LittleTweaksOptions {
    public interface Opt {
        Option<Boolean> ALLOW_EXT_BUFFER_STORAGE = Option.bool("allow_ext_buffer_storage", false);
        Option<Boolean> ALLOW_READ_OS_RELEASE = Option.bool("allow_read_os_release", false);
        Option<Boolean> ALLOW_READ_LSB_RELEASE = Option.bool("allow_read_lsb_release", false);
        Option<Boolean> ALLOW_SERVICE_DISCOVERY = Option.bool("allow_service_discovery", false);
        Option<Boolean> ALLOW_NARRATOR = Option.bool("allow_narrator", false);
    }

    public static final Logger LOGGER = LogUtils.getLogger();

    private static Properties props = new Properties();

    public static <T> T get(String opt, Option.Type<T> type) {
        return get(opt, type, null);
    }

    public static <T> T get(String opt, Option.Type<T> type, T def) {
        // properties have priority, config is second
        String javaOpt = "littletweaks." + opt;
        if(System.getProperties().contains(javaOpt)) {
            T parse = type.parse(opt, System.getProperty(javaOpt));
            if(parse == null) return def;
            return parse;
        }

        if(props.containsKey(opt)) {
            T parse = type.parse(opt, props.getProperty(opt));
            if(parse == null) return def;
            return parse;
        }

        return def;
    }

    public static boolean bool(Option<Boolean> booleanOpt) {
        Boolean result = get(booleanOpt.name(), booleanOpt.type());
        if(result == null) return false;
        return result;
    }

    public static int integer(Option<Integer> integerOpt) {
        Integer result = get(integerOpt.name(), integerOpt.type());
        if(result == null) return 0;
        return result;
    }

    public static String string(Option<String> stringOpt) {
        return get(stringOpt.name(), stringOpt.type());
    }

    public sealed interface Option<T> {
        String name();
        Type<T> type();
        T defaultValue();

        default T get() {
            return LittleTweaksOptions.get(name(), type(), defaultValue());
        }

        static Option<Boolean> bool(String opt, boolean def) {
            return new Impl<>(opt, Type.BOOLEAN, def);
        }

        static Option<Integer> integer(String opt, int def) {
            return new Impl<>(opt, Type.INT, def);
        }

        static Option<String> string(String opt, String def) {
            return new Impl<>(opt, Type.STRING, def);
        }

        record Impl<T>(String name, Type<T> type, T defaultValue) implements Option<T> {
        }

        interface Type<T> {
            Type<Integer> INT = (opt, input) -> {
                try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    LOGGER.warn("Failed to parse option {}", opt, e);
                    return null;
                }
            };
            Type<String> STRING = (opt, input) -> input;
            Type<Boolean> BOOLEAN = (opt, input) -> input.equalsIgnoreCase("true");

            T parse(String opt, String input);
        }
    }

    static void initialize() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(LittleTweaksMod.MOD_ID + ".properties");
        if(Files.notExists(path)) {
            LOGGER.info("Config {} does not exist yet, creating!", path.getFileName().toString());
            try {
                Files.createFile(path);
            } catch (IOException e) {
                LOGGER.error("Failed to create properties file {}", path, e);
            }
            return;
        }

        props = loadProperties(path);
    }

    private static Properties loadProperties(Path path) {
        try {
            try (InputStream is = Files.newInputStream(path)) {
                CharsetDecoder reportingUtf8Decoder = StandardCharsets.UTF_8
                        .newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT);
                Properties properties = new Properties();
                properties.load(new InputStreamReader(is, reportingUtf8Decoder));
                return properties;
            } catch (CharacterCodingException e) {
                LOGGER.info("Failed to load properties as UTF-8 from file {}, trying ISO_8859_1", path);

                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
                    Properties properties = new Properties();
                    properties.load(reader);
                    return properties;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load properties from file: {}", path, e);
            return new Properties();
        }
    }


}
