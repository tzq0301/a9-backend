package a9.manager;

import a9.entity.baidu.Contents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@Slf4j
public class BaiduManager {
    private final RestTemplate restTemplate;

    private final String accessToken;

    public BaiduManager(RestTemplate restTemplate,
                        @Value("${baidu.access_token}") String accessToken) {
        this.restTemplate = restTemplate;
        this.accessToken = accessToken;
    }

    public Contents getContents() {
        return restTemplate.postForObject(
                "https://aip.baidubce.com/rpc/2.0/creation/v1/event/vein_list?access_token=" + this.accessToken,
                null, Contents.class);
    }
}
