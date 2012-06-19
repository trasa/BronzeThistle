package bronzethistle.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ExampleJob {
    private static final Logger log = LoggerFactory.getLogger(ExampleJob.class);

    @Scheduled(fixedDelay = 1000L)
    public void run() {
        System.out.println("running");
        log.info("running - {}", System.currentTimeMillis());
    }
}
