package com.laofaner.cq_soccer.utils;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;

/**
 *
 */
public class FileUtil {
    public static String uploadFile(MultipartFile file, String fileFolderPath, String fileName) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("文件未上传");
        }

        if (fileName == null || "".equals(fileName.trim())) {
            // 截取文件后缀名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            // 文件重命名
            fileName = UUID.randomUUID().toString() + "." + suffix;
        }

        String dateStr = DateUtil.getStringDate("yyyyMMdd", null);
        StringBuilder sb = new StringBuilder();
        sb.append(dateStr).append("/").append(fileName);
        // 返回路径
        String path = sb.toString();
        try {
            File dirPath = new File(fileFolderPath + dateStr);
            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
            File uploadFile = new File(dirPath + "/" + fileName);
            if (!uploadFile.getParentFile().exists()) {
                uploadFile.getParentFile().mkdirs();
            }
            FileCopyUtils.copy(file.getBytes(), uploadFile);
        } catch (IOException e) {
            throw new Exception(e);
        }
        return path;
    }

    public static String uploadFile(String url, String fileFolderPath) throws Exception {
        URL request = new URL(url);
        URLConnection urlConnection = request.openConnection();
        InputStream ins = urlConnection.getInputStream();
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String dateStr = DateUtil.getStringDate("yyyyMMdd", null);
        StringBuilder sb = new StringBuilder();
        sb.append(dateStr).append("/").append(fileName);
        // 返回路径
        String path = sb.toString();
        try {
            File dirPath = new File(fileFolderPath + dateStr);
            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
            File uploadFile = new File(dirPath + "/" + fileName);
            if (!uploadFile.getParentFile().exists()) {
                uploadFile.getParentFile().mkdirs();
            }
            FileCopyUtils.copy(inputToByte(ins), uploadFile);
        } catch (IOException e) {
            throw new Exception(e);
        }
        return path;
    }

    public static void downLoadFile(HttpServletResponse response, String filePath, String fileName) throws Exception {
        String downloadFilename = fileName;
        downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");

        File file = new File(filePath + fileName);
        if (file.exists()) {
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            response.setContentType("application/octet-stream");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    fileInputStream);
            byte[] b = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(b);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(b);
            bufferedInputStream.close();
            outputStream.flush();
            outputStream.close();
        }
    }

    private static byte[] inputToByte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

    public static boolean isImage(InputStream imageFile) {
        if (null == imageFile) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }
}
