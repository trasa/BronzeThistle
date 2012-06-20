package bronzethistle.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ExampleJob {
    private static final Logger log = LoggerFactory.getLogger(ExampleJob.class);

    private AtomicInteger i = new AtomicInteger();
    private AtomicLong lastRun = new AtomicLong();

    @Scheduled(fixedRate = 3000L)
    public void run() {
        long timeSinceLastRun = System.currentTimeMillis() - lastRun.get();
        if (timeSinceLastRun > 3000) {
            log.warn("Scheduled job is falling behind!! Time since last run: {}", timeSinceLastRun);
        }
        lastRun.set(System.currentTimeMillis());
        // this is where you'd have "stuff happen" ..
        // this is just sleeping and log output for testing purposes...
        doWork();
    }


    private void doWork() {
        log.info("running - {} - {}", i.getAndIncrement(), new Date());
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException ignore) {}
    }
}
