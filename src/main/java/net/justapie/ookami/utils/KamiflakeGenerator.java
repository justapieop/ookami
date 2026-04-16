package net.justapie.ookami.utils;

import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class KamiflakeGenerator {
    private final AtomicReference<BigInteger> machineId = new AtomicReference<>();
    private final AtomicReference<BigInteger> seq = new AtomicReference<>(BigInteger.ZERO);
    private final AtomicReference<BigInteger> lastTimestamp = new AtomicReference<>(BigInteger.valueOf(Instant.now().toEpochMilli()));

    public static void init(int mId) {
        machineId.compareAndSet(null, new BigInteger(String.valueOf(mId)));
    }

    public static synchronized BigInteger nextId() {
        BigInteger now = BigInteger.valueOf(Instant.now().toEpochMilli());

        if (now.equals(lastTimestamp.get())) {
            seq.set(seq.getAndAccumulate(BigInteger.ONE, BigInteger::add));
        } else {
            seq.set(BigInteger.ZERO);
        }

        lastTimestamp.set(now);

        return now.shiftLeft(64).or(machineId.get().shiftLeft(32).or(seq.get()));
    }
}
