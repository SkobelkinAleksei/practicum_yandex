package org.example.http;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null || duration.isZero()) {
            jsonWriter.nullValue();
        } else {
            int durationOfMin = (int) duration.toMinutes();
            jsonWriter.value(durationOfMin);
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return Duration.ofMinutes(jsonReader.nextInt());
    }
}