package com.terryscape.cache.region;

import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.world.WorldRegionCoordinate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class WorldRegionCacheLoader {

    public Map<WorldRegionCoordinate, Object> readWorldRegionsFromCache() throws IOException, URISyntaxException {

//        for (var regionPath : getAllRegionPaths()) {
//            try (var input = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(regionPath.toString())))) {
//                var reader = JsonParser.parseReader(input);
//                var jsonObject = reader.getAsJsonObject();
//            }
//        }

        return null;
    }

    // https://mkyong.com/java/java-read-a-file-from-resources-folder/
    private List<Path> getAllRegionPaths() throws IOException, URISyntaxException {
        var regionFolder = getClass().getResource(Config.WORLD_REGION_CACHE_LOCATION_DIRECTORY);

        try (var fileSystem = FileSystems.newFileSystem(regionFolder.toURI(), Map.of())) {
            return Files.walk(fileSystem.getPath(Config.WORLD_REGION_CACHE_LOCATION_DIRECTORY))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        }
    }

    private WorldRegionCoordinate getWorldRegionCoordinateFromRegionFile(Path regionFilePath) {
        var fileName = regionFilePath.getFileName().toString().replace(".json", "");
        var nameParts = fileName.split(",");

        return new WorldRegionCoordinate(
            Integer.parseInt(nameParts[0]),
            Integer.parseInt(nameParts[1])
        );
    }

}
