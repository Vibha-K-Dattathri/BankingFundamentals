package com.ofss.util;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
public class CustomerIdGenerator {
    private static final Random random = new Random();
    private static final ConcurrentHashMap<String, Boolean> usedIds = new ConcurrentHashMap<>();
    public static String generateUniqueId() {
        String id;
        do {
            int num = 1000 + random.nextInt(9000); // 1000-9999
            id = String.valueOf(num);
        } while (usedIds.putIfAbsent(id, true) != null); // retry if already exists
        return id;
    }
}