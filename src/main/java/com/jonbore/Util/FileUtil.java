package com.jonbore.Util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author bo.zhou
 * @date 2019/12/10
 */
public class FileUtil {


    /**
     * gzip compress
     *
     * @param original
     * @return
     */
    public static String gzip(String original) {
        String result = "";
        ByteArrayOutputStream byteArrayOutputStream = null;
        GZIPOutputStream gzipOutputStream = null;
        try {
            if (original == null || original.length() == 0) {
                return original;
            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(original.getBytes());
            try {
                gzipOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //must close gzip stream before operate byte stream
            result = new BASE64Encoder().encode(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * gzip decompress
     *
     * @param compressStr
     * @return
     */
    public static String xgzip(String compressStr) {
        String result = "";
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        GZIPInputStream gzipInputStream = null;
        try {
            byte[] compressBytes = new BASE64Decoder().decodeBuffer(compressStr);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayInputStream = new ByteArrayInputStream(compressBytes);
            gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, offset);
            }
            result = byteArrayOutputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * file read to String
     *
     * @param file
     * @return
     */
    public static String readFile2String(File file) {
        String result = "";
        ByteArrayOutputStream byteArrayOutputStream = null;
        InputStream inputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            inputStream = new FileInputStream(file);
            byte[] content = new byte[inputStream.available()];
            inputStream.read(content);
            byteArrayOutputStream.write(content);
            result = new String(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new BASE64Encoder().encode(result.getBytes());
    }

    /**
     * string write to file
     *
     * @param content
     * @param file
     * @return
     */
    public static boolean write2File(String content, File file) {
        boolean result = true;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            file.getParentFile().mkdirs();
            file.createNewFile();
            inputStream = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(content));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.flush();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * read base64 string from file
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static String read(File file) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputStream.read(buffer);
        String s = new BASE64Encoder().encode(buffer);
        inputStream.close();
        return s;
    }

    /**
     * write base64 into file
     *
     * @param content
     * @param file
     * @throws Exception
     */
    private static void write(String content, File file) throws Exception {
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(new BASE64Decoder().decodeBuffer(content));
        outputStream.flush();
        outputStream.close();
    }

    /**
     * string 2 unicode
     *
     * @param content
     * @return
     */
    private static String toUnicode(String content) {
        char[] strChar = content.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : strChar) {
            stringBuilder.append("\\u").append(Integer.toHexString(c));
        }
        return stringBuilder.toString();
    }

    /**
     * unicode 2 string
     *
     * @param unicodeStr
     * @return
     */
    private static String unicodeToString(String unicodeStr) {
        String[] strings = unicodeStr.split("\\\\u");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < strings.length; i++) {
            stringBuilder.append((char) Integer.parseInt(strings[i], 16));
        }
        return stringBuilder.toString();
    }

    /**
     * read unicode string from file
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String readFile2UnicodeStr(File file) throws Exception {
        String s = read(file);
        return toUnicode(s);
    }

    /**
     * gz file 2 unicode string
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String compressReadFIleUnicodeStr(File file) throws Exception {
        String base64Str = read(file);
        return gzip(toUnicode(base64Str));
    }

    /**
     * write unicode string into file
     *
     * @param content
     * @param file
     * @throws Exception
     */
    public static void writeUnicode2File(String content, File file) throws Exception {
        String s = unicodeToString(content);
        write(s, file);
    }

    /**
     *
     * uncompress unicode write into file
     *
     * @param content
     * @param file
     * @throws Exception
     */
    public static void uncompressUnicodeWite2File(String content, File file) throws Exception {
        String unicodeStr = xgzip(content);
        write(unicodeToString(unicodeStr), file);
    }

}
