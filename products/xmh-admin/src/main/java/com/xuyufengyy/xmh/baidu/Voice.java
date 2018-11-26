package com.xuyufengyy.xmh.baidu;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.xuyufengyy.xmh.utils.FtpJSch;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.*;

@Component
public class Voice {

    protected static Logger logger = LoggerFactory.getLogger(Voice.class);

    @Value("${baidu.app.id}")
    private String appId;

    @Value("${baidu.api.key}")
    private String apiKey;

    @Value("${baidu.secret.key}")
    private String secretKey;

    @Resource
    private FtpJSch ftpJSch;

    @Value(value = "${remote.server.upload.voice.address}")
    private String voiceAddress;

    /**
     * 语音合成
     *
     * @param content  文本内容
     * @param fileName 输出文件名
     */
    public String synthesis(String content, String fileName) {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(appId, apiKey, secretKey);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 设置可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5");//语速，取值0-9，默认为5中语速
        options.put("pit", "5");//音调，取值0-9，默认为5中语调
        options.put("vol", "5");//音量，取值0-15，默认为5中音量
        options.put("per", "4");//发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女

        // 调用接口
        List<String> list = getStrList(content, 256);
        List<InputStream> list2 = new ArrayList<InputStream>();

        for(String str:list){
            TtsResponse res = client.synthesis(str, "zh", 1, options);
            InputStream inputStream = new ByteArrayInputStream(res.getData());
            list2.add(inputStream);

            JSONObject res1 = res.getResult();
            if (res1 != null) {
                logger.info(res1.toString(2));
            }
        }

        if (!list2.isEmpty()) {
            try {
                ftpJSch.getConnect();

                Enumeration<InputStream> e = Collections.enumeration(list2);
                SequenceInputStream sis = new SequenceInputStream(e);

                fileName = ftpJSch.upload(voiceAddress, fileName, sis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString
     *            原始字符串
     * @param length
     *            指定长度
     * @return
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString
     *            原始字符串
     * @param length
     *            指定长度
     * @param size
     *            指定列表大小
     * @return
     */
    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str
     *            原始字符串
     * @param f
     *            开始位置
     * @param t
     *            结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }
}
