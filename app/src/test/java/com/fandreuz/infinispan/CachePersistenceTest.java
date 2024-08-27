package com.fandreuz.infinispan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.infinispan.Cache;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;
import org.junitpioneer.jupiter.SetSystemProperty;

@SetSystemProperty(key = "org.slf4j.simpleLogger.defaultLogLevel", value = "trace")
public class CachePersistenceTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);
    private static final String VALUE = "value";

    @TempDir
    private Path dataLocation;

    @TempDir
    private Path indexLocation;

    private Cache<String, String> cache;

    @BeforeAll
    static void beforeAll() {
        Awaitility.setDefaultPollInterval(Duration.ofMillis(500));
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        var cacheManager = CacheConfiguration.makeCacheManager(dataLocation, indexLocation);
        cache = cacheManager.getCache(testInfo.getDisplayName(), true);
    }

    @Test
    void testNoEviction() {
        // Given

        // When
        addEntries(CacheConfiguration.MAX_COUNT);

        // Then
        Awaitility.await() //
                .during(TIMEOUT) //
                .until(() -> cache.getAdvancedCache().getStats().getEvictions() == 0);
    }

    @Test
    void testEviction() {
        // Given
        int overflow = 3;

        // When
        addEntries(CacheConfiguration.MAX_COUNT + overflow);

        // Then
        Awaitility.await() //
                .timeout(TIMEOUT) //
                .until(() -> cache.getAdvancedCache().getStats().getEvictions() == overflow);

        for (int i = 0; i < CacheConfiguration.MAX_COUNT + overflow; ++i) {
            assertEquals(VALUE, cache.get(makeKey(i)));
        }
    }

    private void addEntries(int count) {
        for (int i = 0; i < count; ++i) {
            cache.put(makeKey(i), VALUE);
        }
    }

    private String makeKey(int i) {
        return "key" + i;
    }
}
