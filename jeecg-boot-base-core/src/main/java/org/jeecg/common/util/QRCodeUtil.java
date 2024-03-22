package org.jeecg.common.util;

import cn.hutool.http.HttpUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import sun.font.FontDesignMetrics;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * 二维码生成工具类
 * xiaopeng.wu on 2024/03/21
 * 注意添加字段：
 * 安装字体SimHei.ttf  simsun.ttc
 * 在jdk目录（/usr/local/jdk1.8.0_331/jre/lib/fonts）下创建目录fallback
 *
 * 将window下的宋体字体放到此目录下
 * 安装字体说明：https://blog.csdn.net/fenfenguai/article/details/123135380
 */
public class QRCodeUtil {
    private static final int QRCODE_SIZE = 320; // 二维码尺寸，宽度和高度均是320
    private static final String FORMAT_TYPE = "PNG"; // 二维码图片类型

    /**
     * 默认需要logo,无底部文字
     * 返回 BufferedImage 可以使用ImageIO.write(BufferedImage, "png", outputStream);输出
     *
     * @param dataStr
     * @return 返回 BufferedImage 可以使用ImageIO.write(BufferedImage, "png", outputStream);输出
     */
    @SneakyThrows
    public static BufferedImage getQRCodeImage(String dataStr, String logUri) {
        BufferedImage bufferedImage = getQRCodeImage(dataStr, true, null, null, logUri);
        return bufferedImage;
    }

    /**
     * 默认需要logo,无底部文字
     *
     * @param dataStr
     * @return 返回字节数组
     */
    @SneakyThrows
    public static byte[] getQRCodeByte(String dataStr, String logUri) {
        BufferedImage bufferedImage = getQRCodeImage(dataStr, true, null,null, logUri);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, FORMAT_TYPE, outputStream);
        byte[] byteData = outputStream.toByteArray();
        return byteData;
    }

    /**
     * 默认需要logo，包含底部文字 文字为空则不显示文字
     * 返回 BufferedImage 可以使用ImageIO.write(BufferedImage, "png", outputStream);输出
     *
     * @param dataStr
     * @return
     */
    @SneakyThrows
    public static String getQRCodeImage(String dataStr, String bottomText,String topText, String logUri) {
        String pngBase64 = "";
        BufferedImage bufferedImage = getQRCodeImage(dataStr, true, bottomText, topText, logUri);

        //转换成base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(bufferedImage, "png", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        pngBase64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        pngBase64 = pngBase64.replaceAll("\n", "").replaceAll("\r", "");
        return "data:image/jpg;base64," + pngBase64;
    }

    /**
     * 默认需要logo，包含底部文字 文字为空则不显示文字
     *
     * @param dataStr
     * @return 返回字节数组
     */
    @SneakyThrows
    public static byte[] getQRCodeByte(String dataStr, String bottomText, String topText, String logUri) {
        BufferedImage bufferedImage = getQRCodeImage(dataStr, true, bottomText, topText, logUri);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, FORMAT_TYPE, outputStream);
        byte[] byteData = outputStream.toByteArray();
        return byteData;
    }

    /**
     * 获取二维码图片
     *
     * @param dataStr    二维码内容
     * @param needLogo   是否需要添加logo
     * @param bottomText 底部文字       为空则不显示
     * @return
     */
    @SneakyThrows
    public static BufferedImage getQRCodeImage(String dataStr, boolean needLogo, String bottomText,String topText, String logUri) {
        if (dataStr == null) {
            throw new RuntimeException("未包含任何信息");
        }
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");    //定义内容字符集的编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);//定义纠错等级
        hints.put(EncodeHintType.MARGIN, 1);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(dataStr, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int tempHeight = height;
        if (StringUtils.hasText(bottomText)) {
            tempHeight = tempHeight + 22;//添加底部高度
        }
        BufferedImage image = new BufferedImage(width, tempHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        // 判断是否添加logo
        if (needLogo) {
            insertLogoImage(image, logUri);
        }
        // 判断是否添加顶部文字
        if (StringUtils.hasText(topText)) {
            addTopTextImage(image, topText);
        }
        // 判断是否添加底部文字
        if (StringUtils.hasText(bottomText)) {
            addBottomTextImage(image, bottomText);
        }
        image = combinedBackground(image);
        //合成背景图
        return image;
    }

    /**
     * @param source 插入logo图片
     * @throws Exception
     */
    private static void insertLogoImage(BufferedImage source, String logUri) throws Exception {
        byte[] files = HttpUtil.downloadBytes(logUri);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(files);
        if (inputStream == null || inputStream.available() == 0) {
            return;
        }
        Image src = ImageIO.read(inputStream);
        int width = 60;
        int height = 60;

        Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        src = image;

        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /*
     *@Description: 添加二维码顶部文字
     *@Param: [source, declareText]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 11:05 2024/3/21
     **/
    private static void addTopTextImage(BufferedImage source, String declareText) {
        //生成image
        int defineWidth = QRCODE_SIZE;
        int defineHeight = 30;

        BufferedImage textImage = new BufferedImage(defineWidth, defineHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) textImage.getGraphics();
        //开启文字抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, defineWidth, defineHeight);
        g2.setPaint(Color.BLACK);
        FontRenderContext context = g2.getFontRenderContext();
        //部署linux需要注意 linux无此字体会显示方块
        Font font = new Font("宋体", Font.BOLD, 15);
        g2.setFont(font);

        LineMetrics lineMetrics = font.getLineMetrics(declareText, context);
        FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
        float offset = (defineWidth - fontMetrics.stringWidth(declareText)) / 2;
        float y = (defineHeight + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;
        g2.drawString(declareText, (int) 8, (int) y);

        Graphics2D graph = source.createGraphics();
        //开启文字抗锯齿
        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //添加image
        int width = textImage.getWidth(null);
        int height = textImage.getHeight(null);

        Image src = textImage;
        graph.drawImage(src, 0, 0, width, height, Color.WHITE, null);
        graph.dispose();
    }

    /*
     *@Description: 添加二维码底部文字
     *@Param: [source, declareText]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 11:05 2024/3/21
    **/
    private static void addBottomTextImage(BufferedImage source, String declareText) {
        //生成image
        int defineWidth = QRCODE_SIZE;
        int defineHeight = 30;
        BufferedImage textImage = new BufferedImage(defineWidth, defineHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) textImage.getGraphics();
        //开启文字抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, defineWidth, defineHeight);
        g2.setPaint(Color.BLACK);
        FontRenderContext context = g2.getFontRenderContext();
        //部署linux需要注意 linux无此字体会显示方块
        Font font = new Font("宋体", Font.BOLD, 25);
        g2.setFont(font);
        LineMetrics lineMetrics = font.getLineMetrics(declareText, context);
        FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
        float offset = (defineWidth - fontMetrics.stringWidth(declareText)) / 2;
        float y = (defineHeight + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;
        g2.drawString(declareText, (int) offset, (int) y);

        Graphics2D graph = source.createGraphics();
        //开启文字抗锯齿
        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //添加image
        int width = textImage.getWidth(null);
        int height = textImage.getHeight(null);

        Image src = textImage;
        graph.drawImage(src, 0, QRCODE_SIZE - 8, width, height, Color.WHITE, null);
        graph.dispose();
    }

    /*
     *@Description: 合成图片背景图
     *@Param:
     *@Return:
     *@author: xiaopeng.wu
     *@DateTime: 16:35 2024/3/21
    **/
    private static BufferedImage combinedBackground(BufferedImage foregroundImage ) throws Exception {
        String background = "https://www.gzjykj.tech/happy/saas/background1.png";
        byte[] files = HttpUtil.downloadBytes(background);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(files);
        if (inputStream == null || inputStream.available() == 0) {
            return null;
        }
        //背景图
        Image backgroundImage  = ImageIO.read(inputStream);

        // 创建合成后的图像
        BufferedImage combinedImage = new BufferedImage(
                backgroundImage.getWidth(null),
                backgroundImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        // 将背景图像绘制到合成图像上
        Graphics2D g2d = combinedImage.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, null);
        // 将待添加的图像绘制到合成图像上
        g2d.drawImage(foregroundImage, 200, 500, null);
        // 释放资源
        g2d.dispose();
        return combinedImage;
    }
}
