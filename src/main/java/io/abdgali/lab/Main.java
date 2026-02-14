package io.abdgali.lab;

import io.abdgali.lab.distributed.CfSingleFight;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        CfSingleFight<String, Integer> cs = new CfSingleFight<>();
    }
}
