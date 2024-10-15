package com.onebucket.global.redis;

import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <br>package name   : com.onebucket.global.redis
 * <br>file name      : LuaScrptLoader
 * <br>date           : 2024-10-15
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
public class LuaScriptLoader {
    public static String loadScript(String scriptPath) {
        try {
            ClassPathResource resource = new ClassPathResource(scriptPath);
            Path path = Paths.get(resource.getURI());
            return Files.readString(path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Lua script: " + scriptPath, e);
        }
    }
}
