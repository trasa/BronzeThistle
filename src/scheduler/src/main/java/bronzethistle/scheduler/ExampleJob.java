package bronzethistle.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExampleJob {
    private static final Logger log = LoggerFactory.getLogger(ExampleJob.class);

    private AtomicInteger i = new AtomicInteger();

    @Scheduled(fixedRate = 3000L)
    public void run() {
        log.info("running - {} - {}", i.getAndIncrement(), new Date());
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException ignore) {}
    }
}
