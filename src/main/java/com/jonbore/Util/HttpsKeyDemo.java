package com.jonbore.Util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

public class HttpsKeyDemo {
	// 客户端证书路径，用了本地绝对路径，需要修改
	private final static String PFX_PATHAPI = "/Users/bo.zhou/Downloads/api证书20190709/api_client01.p12";
	private final static String PFX_PATHAPI0 = "/Users/bo.zhou/Downloads/api证书20190709/api0_client.p12";
    private final static String PFX_PWDAPI0 = ""; //客户端证书密码及密钥库密码
    private final static String PFX_PWDAPI = "";

    public static String sslRequestGet(String url) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream instream = new FileInputStream(new File(PFX_PATHAPI));
        try {
            // 这里就指的是KeyStore库的密码
            keyStore.load(instream, PFX_PWDAPI.toCharArray());
        } finally {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, PFX_PWDAPI.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext
                , new String[] { "TLSv1" }  // supportedProtocols ,这里可以按需要设置
                , null  // supportedCipherSuites
                , SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//        CloseableHttpClient httpclient = HttpClients.custom().build();
        try {
            HttpGet httpget = new HttpGet(url);
//            HttpPost httpPost = new HttpPost(url);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("pwd", "123456");
//            jsonObject.put("code", "959607B0F119");
//            jsonObject.put("username", "test");
//            StringEntity stringEntity = new StringEntity(jsonObject.toString());
//            httpPost.setEntity(stringEntity);
            httpget.addHeader("Content-Type", "application/json;charset=UTF-8");
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                // 返回结果
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    public static void importCrt(String keytool,String keyStore, String path, String keyPass, String aliasName) {
    	//"/bin/sh","-c" linux
		String[] cmd = {"cmd","/c",keytool+" -import -keystore "+keyStore+"  -storepass changeit -keypass "+keyPass+" -alias "+aliasName+" -file "+path+" -noprompt"};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static void main(String[] args) throws Exception {
        System.out.println(sslRequestGet("https://api.hirain.com/rest/servicepublish/service/temp/System01/ali_fusion_test/3DE0082E531/get"));
//        System.out.println(sslRequestGet("https://api.XXX.com"));
//        importCrt("keytool", "\"D:/env/Java/jdk1.8.0_201/jre/lib/security/cacerts\"", "C:/Users/bo.zhou1.XXXX/Documents/api证书20190709/rootca.crt", "\"******&*\"", "api0001.XXXX.com");
        //keytool -import -keystore "/Library/Java/JavaVirtualMachines/jdk1.8.0_161.jdk/Contents/Home/jre/lib/security/cacerts" -storepass changeit keypass api_HRdmz12&* -alias api0.hirain.com -file /Users/bo.zhou/Downloads/api证书20190709/rootca.crt
    }

}
