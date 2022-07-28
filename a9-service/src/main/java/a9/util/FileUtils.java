package a9.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
public class FileUtils {

    private final String bucketName;

    private final OSS ossClient;

    private final String rootPath;

    private final String baseRoot;

    public FileUtils(@Value("${oss.endpoint}") String endpoint,
                     @Value("${oss.accessKeyId}") String accessKeyId,
                     @Value("${oss.accessKeySecret}") String accessKeySecret,
                     @Value("${oss.bucketName}") String bucketName,
                     @Value("${oss.rootPath}") String rootPath) {
        this.bucketName = bucketName;
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        this.rootPath = rootPath;
        this.baseRoot = "https://" + bucketName + "." + endpoint.replaceFirst("https://", "")
                + "/" + rootPath + "/";
    }

    /**
     * 上传文件
     *
     * @param filepath    文件路径（不 / 开头）
     * @param inputStream 内容流
     * @return 文件路径
     * @throws OSSException    means your request made it to OSS
     *                         but was rejected with an error response for some reason.
     * @throws ClientException means the client encountered a serious internal problem
     *                         while trying to communicate with OSS, such as not being able to access the network.
     */
    public String upload(String filepath, InputStream inputStream) throws OSSException, ClientException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, rootPath + "/" + filepath, inputStream);

        ossClient.putObject(putObjectRequest);

        return baseRoot + filepath;
    }

}
