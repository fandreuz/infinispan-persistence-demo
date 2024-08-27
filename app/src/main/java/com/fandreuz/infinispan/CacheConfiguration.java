package com.fandreuz.infinispan;

import java.nio.file.Path;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class CacheConfiguration {

    public static final int MAX_COUNT = 10;

    public static EmbeddedCacheManager makeCacheManager(Path dataLocation, Path indexLocation) {
        GlobalConfiguration globalConfiguration =
                GlobalConfigurationBuilder.defaultClusteredBuilder().build();
        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration, true);
        cacheManager.defineConfiguration("*", infinispanCacheConfiguration(dataLocation, indexLocation));
        return cacheManager;
    }

    private static Configuration infinispanCacheConfiguration(Path dataLocation, Path indexLocation) {
        return new ConfigurationBuilder() //
                .encoding()
                .key()
                .mediaType("text/plain; charset=UTF-8") //
                .encoding()
                .value()
                .mediaType("application/x-protostream") //
                .persistence() //
                // Write entries to the disk only when they are evicted
                .passivation(true) //
                // Persistent disk storage. This is intended for infrequently used entries when
                // the heap usage is too high, availability is guaranteed by the redundancy of
                // the instances.
                .addSoftIndexFileStore() //
                .purgeOnStartup(true) //
                .preload(false) //
                .shared(false) //
                .dataLocation(dataLocation.toAbsolutePath().toString()) //
                .indexLocation(indexLocation.toAbsolutePath().toString()) //
                .memory() //
                .maxCount(MAX_COUNT) //
                .whenFull(EvictionStrategy.REMOVE) //
                .statistics() //
                .enable() //
                .build();
    }
}
