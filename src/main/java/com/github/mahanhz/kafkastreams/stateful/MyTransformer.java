package com.github.mahanhz.kafkastreams.stateful;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

public class MyTransformer<K, V, R> implements Transformer<K, V, KeyValue<K, V>> {

    private final String tranformationType;

    public MyTransformer(final String tranformationType) {
        this.tranformationType = tranformationType;
    }

    @Override
    public void init(final ProcessorContext context) {

    }

    @Override
    public KeyValue<K, V> transform(final K key, final V value) {
        System.out.println("Transforming car with key: " + key + ", value: " + value + ". [" + tranformationType + "].");

        return KeyValue.pair(key, value);
    }

    @Override
    public void close() {

    }
}
