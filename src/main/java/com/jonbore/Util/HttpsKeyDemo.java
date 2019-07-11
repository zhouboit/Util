package com.jonbore.Util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.SSLContext;
import javax.swing.event.ListSelectionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class HttpsKeyDemo {
	// 客户端证书路径，用了本地绝对路径，需要修改
	private final static String PFX_PATHAPI = "C:/Users/bo.zhou1.XXXX/Documents/api证书20190709/api_client01.p12";
	private final static String PFX_PATHAPI0 = "C:/Users/bo.zhou1.XXX/Documents/api证书20190709/api0_client.p12";
    private final static String PFX_PWDAPI0 = "*****"; //客户端证书密码及密钥库密码
    private final static String PFX_PWDAPI = "****@#";

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
        try {
//            HttpGet httpget = new HttpGet(url);
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pwd", "123456");
            jsonObject.put("code", "959607B0F119");
            jsonObject.put("username", "test");
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
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
        System.out.println(sslRequestGet("https://api.XXXX.com/rest/servicepublish/service/temp/System01/menu/post"));
//        System.out.println(sslRequestGet("https://api.XXX.com"));
//        importCrt("keytool", "\"D:/env/Java/jdk1.8.0_201/jre/lib/security/cacerts\"", "C:/Users/bo.zhou1.XXXX/Documents/api证书20190709/rootca.crt", "\"******&*\"", "api0001.XXXX.com");
    }

}
