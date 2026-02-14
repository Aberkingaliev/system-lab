package io.abdgali.lab.distributed;

import java.util.function.Supplier;

public interface SingleFight<K,V> {
    V fight(K key, Supplier<V> function);
}
