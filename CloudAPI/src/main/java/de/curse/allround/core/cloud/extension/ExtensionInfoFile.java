package de.curse.allround.core.cloud.extension;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionInfoFile {
    private String name;
    private String version;
    private String[] authors;
    private String mainClass;
}
