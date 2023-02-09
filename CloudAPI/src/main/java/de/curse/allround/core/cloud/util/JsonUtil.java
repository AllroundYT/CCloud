package de.curse.allround.core.cloud.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
    public final static Gson GSON = new Gson();
    public final static Gson GSON_BEAUTIFUL = new GsonBuilder().setPrettyPrinting().create();
}
