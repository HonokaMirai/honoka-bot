package com.honoka.config;

import cn.hutool.core.util.URLUtil;
import com.honoka.HonokaBotPlugin;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

public class FileConfig {

    private static final String DATA_PATH = HonokaBotPlugin.INSTANCE.getDataFolder().getAbsolutePath();

    private static final String AUDIO_DIR = "/audio";

    private static final String VTUBER_DIR = "/vtuber";

    private static final String TEMP_DIR = "/temp";

    private static final String MUSIC_DIR = "/music";

    private static final String PICTURE_DIR = "/picture";

    private static final String EMOTION_DIR = "/emotion";

    private static final String CONFIG = "/config";

    public static final String VTUBER_PATH = DATA_PATH + AUDIO_DIR + VTUBER_DIR;

    public static final String TEMP_PATH = DATA_PATH + AUDIO_DIR + TEMP_DIR;

    public static final String MUSIC_PATH = DATA_PATH + AUDIO_DIR + MUSIC_DIR;

    public static final String PICTURE_PATH = DATA_PATH + PICTURE_DIR;

    public static final String TEMP_PICTURE_PATH = DATA_PATH + PICTURE_DIR + TEMP_DIR;

    public static final String CONFIG_PATH = DATA_PATH + CONFIG;

    public static final String BOT_CONFIG_FILENAME = "botConfig.yaml";

    public static final String BOT_CONFIG_PATH = CONFIG_PATH + "/" + BOT_CONFIG_FILENAME;

    /**
     * 初始化文件夹
     */
    public static void checkDirectory() {
        createFiles(VTUBER_PATH);
        createFiles(TEMP_PATH);
        createFiles(MUSIC_PATH);
        createFiles(PICTURE_DIR);
        createFiles(TEMP_PICTURE_PATH);
        createFiles(CONFIG_PATH);
    }

    /**
     * 初始化文件
     */
    public static void checkFile() {
        File file = new File(BOT_CONFIG_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                BotConfig.logger.error("初始化文件失败: " + e);
            }
        }
    }

    /**
     * 文件夹不存在则创建
     * @param path 文件路径
     */
    public static void createFiles(String path) {
        File file = new File(path);
        if (!file.exists() || !file.canRead() || !file.canWrite()) {
            file.mkdirs();
            file.setReadable(true);
            file.setWritable(true);
        }
    }

    /**
     * 获取一个文件夹下的随机一个File文件对象
     * @param folderPath 文件夹路径
     * @return File 随机文件对象
     */
    public static File getRandomFile(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            BotConfig.logger.error("该路径不是一个文件夹路径: " + folderPath);
            // 创建该文件夹
            folder.mkdirs();
        }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            BotConfig.logger.error("该文件夹为空");
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(files.length);
        return files[index];
    }

    /**
     * 将一个网络url资源转成File对象
     * @param url 资源地址
     * @param suffix 文件后缀
     * @return file 文件对象
     */
    public static File urlToFile(String url, String suffix) {
        String fileName = TEMP_PATH + "/" + System.currentTimeMillis() + suffix;
        try {
            InputStream in = URLUtil.getStream(new URL(url));
            FileOutputStream out = new FileOutputStream(fileName);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            BotConfig.logger.error("url转File失败: " + e);
        }

        return new File(fileName);
    }

    /**
     * 获取一个本地File对象
     * @param path 资源地址
     * @return file 文件对象
     */
    public static File getFileByPath (String path) {
        return new File(path);
    }

    /**
     * 获取情绪表情包
     */
    public static File getFileByEmotion (String emotion) {
        String character = Optional.ofNullable(ChatGPTConfig.character).orElse("default");
        // 暂时随机取文件
        return getRandomFile(PICTURE_PATH + "/" + character + "/" + emotion);
    }

    /**
     * 根据宽度等比例压缩图片
     * @param url 源文件url路径
     * @param targetWidth 目标宽度
     * @return
     */
    public static File compressImageFile(URL url, Integer targetWidth) {
        File outputFile = null;
        try {
            BotConfig.logger.info("图片压缩开始: " + url);
            // 读取原始图片
            BufferedImage inputImage = ImageIO.read(url);

            // 计算目标高度
            int width = inputImage.getWidth();
            int height = inputImage.getHeight();
            int targetHeight = (int) ((double) targetWidth / width * height);

            // 创建缩放后的图片对象
            Image scaledImage = inputImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

            // 创建缩放后的BufferedImage对象
            BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

            // 绘制缩放后的图片到BufferedImage对象
            outputImage.getGraphics().drawImage(scaledImage, 0, 0, null);

            // 写入压缩后的图片文件
            // 创建一个临时File对象
            outputFile = new File(TEMP_PICTURE_PATH + "/" + System.currentTimeMillis() + ".jpg");
            ImageIO.write(outputImage, "jpg", outputFile);
        } catch (Exception e) {
            BotConfig.logger.error("图片压缩失败: " + e);
        } finally {
            // 删除源文件
            //outputFile.deleteOnExit();
        }
        return outputFile;
    }

    /**
     * 解析配置文件
     * @return
     */
    public static BotConfig getBotConfig() {
        // 读取本地配置文件
        LoaderOptions options = new LoaderOptions();
        Constructor constructor = new Constructor(BotConfig.class, options);
        Yaml yaml = new Yaml(constructor);        BotConfig botConfig = new BotConfig();
        try (InputStream inputStream = Files.newInputStream(Paths.get(BOT_CONFIG_PATH))) {
            // 解析配置文件为java对象
            botConfig = yaml.loadAs(inputStream, BotConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return botConfig;
    }
}
