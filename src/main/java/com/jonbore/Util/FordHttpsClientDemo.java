package com.jonbore.Util;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.util.EntityUtils;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;

public class FordHttpsClientDemo {
    private final static char[] c = {};

    /**
     * 客户端PEM证书路径
     */
    private final static String TLS_PEM_PATH = "";
    /**
     * 客户端证书KEY文件
     */
    private final static String PK_PATH = "";

    public static CloseableHttpClient getHttpsClient(boolean hasCrt) {
        if (!hasCrt) {
            return HttpClients.custom().build();
        }
        try {

            SSLContext sslcontext = getSocketFactoryPEM(TLS_PEM_PATH, PK_PATH);

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext
                    // supportedProtocols
                    , new String[]{"TLSv1.2"}
                    // supportedCipherSuites
                    , null
                    , SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            //proxy
            HttpHost proxy = new HttpHost("10.10.0.110", 12345);
            DefaultRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            return HttpClients.custom().setRoutePlanner(routePlanner).setSSLSocketFactory(sslsf).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SSLContext getSocketFactoryPEM(String pemPath, String keyPath) throws Exception {
        byte[] certFile = fileToBytes(pemPath);
        byte[] keyFile = fileToBytes(keyPath);

        byte[] certBytes = parseDERFromPEM(certFile, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
        byte[] keyBytes = parseDERFromPEM(keyFile, "-----BEGIN RSA PRIVATE KEY-----", "-----END RSA PRIVATE KEY-----");

        X509Certificate cert = generateCertificateFromDER(certBytes);
        RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);

        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(null);
        keystore.setCertificateEntry("cert-alias", cert);
        keystore.setKeyEntry("key-alias", key, c, new Certificate[]{cert});

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keystore, c);

        KeyManager[] km = kmf.getKeyManagers();

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(km, null, null);

        return context;


    }

    protected static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
        String data = new String(pem);
        String[] tokens = data.split(beginDelimiter);
        tokens = tokens[1].split(endDelimiter);
        return DatatypeConverter.parseBase64Binary(tokens[0]);
    }

    protected static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws
            GeneralSecurityException, IOException {

        DerInputStream derReader = new DerInputStream(keyBytes);

        DerValue[] seq = derReader.getSequence(0);

        if (seq.length < 9) {
            throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
        }

        // skip version seq[0];
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();

        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2,
                exp1, exp2, crtCoef);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) factory.generatePrivate(keySpec);
    }

    private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    private static byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        File file = new File(filePath);

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException ex) {
            } finally {
                try {
                    if (null != fis) {
                        fis.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return buffer;
    }

    public static void httpsGet(String url, boolean useCrt) {
        //测试路径，实际使用请根据环境确认
        CloseableHttpClient httpclient = getHttpsClient(useCrt);
        try {
            HttpGet httpGet = new HttpGet(url);
            //headers
            httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
            CloseableHttpResponse response = httpclient.execute(httpGet);
            //response
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        httpsGet("", true);
    }

}

