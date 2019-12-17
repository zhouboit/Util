package com.jonbore.Util;

import org.junit.Test;

import java.io.File;

/**
 * @author bo.zhou
 * @date 2019/12/10
 */
public class FileUtilTest {

    @Test
    public void read() {
        String s = FileUtil.readFile2String(new File("/Users/bo.zhou/Desktop/work/1XB0000K-0003B-20190603114510.tar.gz"));
//        String s = FileUtil.readFile2String(new File("/Users/bo.zhou/Desktop/work/root.cer"));
        System.out.println(s);
        FileUtil.write2File(s, new File("/Users/bo.zhou/Desktop/workspace/1XB0000K-0003B-20190603114510.tar.gz"));

    }

    @Test
    public void write() {
        String s = FileUtil.xgzip("H4sIAAAAAAAAAL1US09TURDubW9LH5QWWiiKQAUUFMVzQRF5yVNoaZCA2AoqlIIKFoqkUooSluqG\n" +
                "nY+FK9mQaGShCxNjXJAYDdHEIEZjxJ/Qv+B8zE2t6MKVzf1mzsycx8x35lQkXr/SEQRBS4CplbRa\n" +
                "u7GOhmcJLafYfX2CxDkee4XVYDpIg062rZKk12tEULEIk0HXLxsc2v4+JVs4YJgd6W3jl8dbx2ai\n" +
                "bs9USNklXHBbHPZYLFYxSqEQhSpCkUnFLQoQ0jlcyRUd4chIMOzujUSi7tZmUeCyimpFUQT/BlzW\n" +
                "KiXF/B8pUL1S0Z8ESHoNiJRsmu0ZZrApSRqSYDJzZmuF1Mayn2QPL7LESLx7xEbWh3uDksmNRezo\n" +
                "xjZ9hFbCuMbP3qLvtwxN2NTEdh4r/uquqXd1iaAnzJbmkLxBKCAsqPkaCZ9X59NPGMYWvRcKyXKl\n" +
                "7lJGiC7Oz6nXfWXqfHFlFxLi8MAkiWlCzM4ONd+vK232QCUaJZfER5TVQSid4HAVwWMcZmOIUJt6\n" +
                "pte30Ejq9FB49iLpruPJSBEBJzp3k1D3ev/MOYqUBw+QqEzdB6y2fHpMqhn+9NTA5ksSgZMqaZ4y\n" +
                "tQxFTs4AWT5zd5qaUwNSWWRjbbVUq5M0qDUkgiKDeiTfJsGUZXoweEE2+Bzs0wvd9kDkw5kh58hO\n" +
                "NEfPMbwon3pDyCRv7T6pBkK/KMTcYjlXII9/mP+3N6jjrlt/CAY7HVlmDraB3F4e/3iKiuNxtEEJ\n" +
                "7qqrnAOHdzBJX2Fg/QXYn4+SANuZ7J9OzvDtWIGIbU+qJ8QqbXhjmUeD6N3NpQS3N8K4lG/PUeOI\n" +
                "WuhU8285xbysA4RydGVTMYK4w/bG+BlS+5PngY9f/RMfztl6QjoDm4IJV80RD4faG0FfNZKo7TGQ\n" +
                "rGC/3xMO7gUx4Gmu/hA69SqHsp0kvtxhoyZMIszjUSS5z483m1eyg5KOm6xLjHqhlmTB8YnkPwF/\n" +
                "UhaJt7ffPDiK92GuZ+/m3QS/gJ+TRLi1qwUAAA==");
        FileUtil.write2File(s, new File("/Users/bo.zhou/Desktop/workspace/root2.cer"));
    }


    @Test
    public void fileMove() {
        File file = new File("/Users/bo.zhou/Desktop/work/1XB0000K-0003B-20190603114510.tar.gz");
        File target = new File("/Users/bo.zhou/Desktop/work/1/1XB0000K-0003B-20190603114510.tar.gz");
        try {
            String s = FileUtil.readFile2UnicodeStr(file);
            System.out.println(s);
            FileUtil.writeUnicode2File(s, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileExt() throws Exception {
        File file = new File("/Users/bo.zhou/Desktop/work/1XB0000K-0003B-20190603114510.tar.gz");
        File target = new File("/Users/bo.zhou/Desktop/work/3/1XB0000K-0003B-20190603114510.tar.gz");
        String s = FileUtil.compressReadFIleUnicodeStr(file);
        System.out.println(s.getBytes().length);
        FileUtil.uncompressUnicodeWite2File(s, target);
    }
}