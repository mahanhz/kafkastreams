package com.github.mahanhz.kafkastreams.stateful;

import org.apache.kafka.streams.state.RocksDBConfigSetter;
import org.rocksdb.Options;

import java.util.Map;

public class CustomRocksDBConfig implements RocksDBConfigSetter {
    @Override
    public void setConfig(final String storeName, final Options options, final Map<String, Object> configs) {
        // Workaround: We must ensure that the parallelism is set to >= 2.
        int compactionParallelism = Math.max(Runtime.getRuntime().availableProcessors(), 2);
        // Set number of compaction threads (but not flush threads).
        options.setIncreaseParallelism(compactionParallelism);
    }
}
