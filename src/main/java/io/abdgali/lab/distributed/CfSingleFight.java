package io.abdgali.lab.distributed;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CfSingleFight<K,V> implements SingleFight<K,V> {
    private final Map<K, CompletableFuture<V>> processingMap = new ConcurrentHashMap<>();

    @Override
    public V fight(K key, Supplier<V> function) {
        while (true) {
            CompletableFuture<V> existing = processingMap.get(key);
            if (existing != null) {
                return existing.join();
            }

            CompletableFuture<V> newFuture = new CompletableFuture<>();
            existing = processingMap.putIfAbsent(key, newFuture);

            if (existing == null) {
                try {
                    V v = function.get();
                    newFuture.complete(v);
                    return v;
                } finally {
                    processingMap.remove(key, existing);
                }
            }

        }
    }
}
