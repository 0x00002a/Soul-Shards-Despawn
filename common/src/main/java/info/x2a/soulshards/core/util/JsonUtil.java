package info.x2a.soulshards.core.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A simple utility for reading and writing JSON files. To handle custom (de)serialization, use
 * {@link com.google.gson.annotations.JsonAdapter} on your types.
 */
public class JsonUtil {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface JsonSkip {
    }

    private static class ResourceLocationAdaptor extends TypeAdapter<ResourceLocation> {

        @Override
        public void write(JsonWriter jsonWriter, ResourceLocation resourceLocation) throws IOException {
            jsonWriter.value(resourceLocation.toString());
        }

        @Override
        public ResourceLocation read(JsonReader jsonReader) throws IOException {
            return ResourceLocation.tryParse(jsonReader.nextString());
        }
    }

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting()
                             .disableHtmlEscaping()
                             .serializeNulls()
                             .registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdaptor())
                             .setExclusionStrategies(new ExclusionStrategy() {
                                 @Override
                                 public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                                     return fieldAttributes.getAnnotation(JsonSkip.class) != null;
                                 }

                                 @Override
                                 public boolean shouldSkipClass(Class<?> aClass) {
                                     return false;
                                 }
                             })
                             .create();

    /**
     * Reads a {@link T} back from the given file. If the file does not exist, a new file will be generated with the
     * provided default and the default will be returned.
     *
     * @param token    The token type to use for deserialization.
     * @param file     The file to read the JSON from.
     * @param fallback The default value to use if the file does not exist.
     * @param <T>      The object type to give back.
     * @return a {@link T} that was read from the given file or {@code fallback} if the file did not exist.
     */
    public static <T> T fromJson(TypeToken<T> token, File file, T fallback) {
        T ret = fromJson(token, file);
        if (ret == null) {
            toJson(fallback, token, file);
            ret = fallback;
        }

        return ret;
    }

    /**
     * Reads a {@link T} back from the given file. If the file does not exist, {@code null} is returned. If an exception
     * is thrown during deserialization, {@code null} is also returned.
     *
     * @param token The token type to use for deserialization.
     * @param file  - The file to read the JSON from.
     * @param <T>   The object type to give back.
     * @return a {@link T} that was read from the given file, {@code null} if the file does not exist, or {@code null} if
     * an exception was thrown.
     */
    public static <T> T fromJson(TypeToken<T> token, File file) {
        if (!file.exists())
            return null;

        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T fromJson(TypeToken<T> token, JsonElement json) {
        return GSON.fromJson(json, token.getType());
    }

    public static <T> T fromJson(TypeToken<T> token, String json) {
        return GSON.fromJson(json, token.getType());
    }

    /**
     * Converts a {@link T} to JSON and writes it to file. If the file does not exist, a new one is created. If the file
     * does exist, the contents are overwritten with the new value.
     *
     * @param type  The object to write to JSON.
     * @param token The token type to use for serialization.
     * @param file  The file to write the JSON to.
     * @param <T>   The object type to write.
     */
    public static <T> void toJson(T type, TypeToken<T> token, File file) {
        if (!file.exists()) {
            try {
                FileUtils.forceMkdirParent(file);
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(getJson(type, token));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> String getJson(T type, TypeToken<T> token) {
        return GSON.toJson(type, token.getType());
    }
}
