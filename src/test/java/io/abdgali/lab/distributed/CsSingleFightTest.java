package io.abdgali.lab.distributed;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CsSingleFightTest {


    @RepeatedTest(15)
    @DisplayName("shouldCompleteSuccess")
    void shouldCompleteSuccess() {
        SingleFight<String, Integer> cfSingleFight = new CfSingleFight<>();
        int threads = 100;

        AtomicInteger executed = new AtomicInteger();
        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            CountDownLatch ready = new CountDownLatch(threads);
            CountDownLatch start = new CountDownLatch(1);
            CountDownLatch done = new CountDownLatch(threads);

            for (int i = 0; i < threads; i++) {
                pool.submit(() -> {
                    try {
                        ready.countDown();
                        start.await();

                        cfSingleFight.fight("key-1", () -> {
                            try {
                                executed.incrementAndGet();
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return -1;
                        });

                    } catch (InterruptedException e) {

                    } finally {
                        done.countDown();
                    }
                });
            }
            ready.await();
            start.countDown();
            done.await();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(1, executed.get());
    }

}
