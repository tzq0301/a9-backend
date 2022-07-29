package a9.manager;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.TextCorrectionRequest;
import com.tencentcloudapi.nlp.v20190408.models.TextCorrectionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
public class TencentCloudManager {
    private final NlpClient nlpClient;

    public TencentCloudManager(@Value("${tencent.secretId}") String secretId,
                               @Value("${tencent.secretKey}") String secretKey,
                               @Value("${tencent.endpoint}") String endpoint,
                               @Value("${tencent.region}") String region) {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(endpoint);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        this.nlpClient = new NlpClient(cred, region, clientProfile);
    }

    public TextCorrectionResponse checkTextCorrection(final String text) throws TencentCloudSDKException {
        TextCorrectionRequest req = new TextCorrectionRequest();
        req.setText(text);
        return nlpClient.TextCorrection(req);
    }
}
