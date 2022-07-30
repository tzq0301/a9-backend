package a9.schedule;

import a9.manager.BaiduManager;
import a9.repository.ContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootConfiguration
@EnableScheduling
@Slf4j
public class BaiduContentSchedule {
    private final ContentRepository contentRepository;

    private final BaiduManager baiduManager;

    public BaiduContentSchedule(ContentRepository contentRepository,
                                BaiduManager baiduManager) {
        this.contentRepository = contentRepository;
        this.baiduManager = baiduManager;
    }

    @Async
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 0)
    public void fetchDataAndSaveToDatabase() {
        log.info("Fetching data");
        contentRepository.save(baiduManager.getContents());
        log.info("Finish fetching data");
    }
}
