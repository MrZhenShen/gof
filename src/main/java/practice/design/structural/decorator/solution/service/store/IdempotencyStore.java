package practice.design.structural.decorator.solution.service.store;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IdempotencyStore {
    private final Set<String> seenIdempotency;

    public IdempotencyStore() {
        seenIdempotency = ConcurrentHashMap.newKeySet();
    }

    public boolean addKey(String key) {
        return seenIdempotency.add(key);
    }

    public void resetSeen() {
        seenIdempotency.clear();
    }
}
