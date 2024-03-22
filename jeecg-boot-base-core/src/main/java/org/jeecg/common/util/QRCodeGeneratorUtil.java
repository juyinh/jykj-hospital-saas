package org.jeecg.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @Description: 二维码生成工具类
 * @author: xiaopeng.wu
 * @create: 2023/11/27 16:28
 **/
@Slf4j
public class QRCodeGeneratorUtil {
    private static final int BLACK = 0xFF000000;//颜色
    private static final int WHITE = 0xFFFFFFFF; //背景色
    private static final int QRCODE_SIZE = 320; // 二维码尺寸，宽度和高度均是320

    // 生成QR码的方法
    public static void generateQRCode(String data, int width, int height, String filePath) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符编码
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H); // 错误纠正级别
            hints.put(EncodeHintType.MARGIN, 1); // 二维码边距

            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建BufferedImage对象来表示QR码
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            // 将QR码保存到文件
            File qrCodeFile = new File(filePath);
            ImageIO.write(image, "png", qrCodeFile);
            System.out.println("QR码已生成并保存到: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 二维码参数设定
     *
     * @param content 二维码内url/内容
     * @param logUri  二维码中间logo的地址
     */
    public static String creatQrImage(String content, String logUri, String bottomText) throws IOException, WriterException {
        int width = 430; // 二维码图片宽度 430
        int height = 430; // 二维码图片高度430
        //二维码参数设置-- 纠错级别（L 7%、M 15%、Q 25%、H 30%）--字符集编码
        Hashtable<EncodeHintType, Object> setMap = new Hashtable<EncodeHintType, Object>();
        setMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        setMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        setMap.put(EncodeHintType.MARGIN, 1);//设置二维码边的空度，非负数
        //构造矩形--要编码的内容，编码类型，宽度，高度，生成条形码时的一些配置,此项可选
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, //编码类型 ：
                width, // 宽度
                height, // 高度
                setMap);
        // 构造要生成路径的file对象
        return writeToFile(bitMatrix, logUri, bottomText);
    }

    /**
     * @Author:tuzhi-cai
     * @Description:生成二维码的方法，根据File对象
     * @time:2018年2月2日下午2:00:19
     */
    public static String writeToFile(BitMatrix matrix, String logUri,String bottomText) throws IOException {
        String pngBase64 = "";
        try {
            BufferedImage image = toBufferedImage(matrix);//new一个Image对象，对当前的矩阵对象进行绘画
            //设置logo图标,返回带log的二维码,判断是否需要设置二维码的logo
            if (logUri != null && logUri != "") {
                image = setMatrixLogo(image, logUri);//设置logo图标,返回带log的二维码
            }
            if (StrUtil.isNotBlank(bottomText)) {
                addFontImage(image, bottomText);
            }
            image = combinedBackground(image);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
            ImageIO.write(image, "png", baos);//写入流中
            byte[] bytes = baos.toByteArray();//转换成字节
            BASE64Encoder encoder = new BASE64Encoder();
            pngBase64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
            pngBase64 = pngBase64.replaceAll("\n", "").replaceAll("\r", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成二维码异常:{}", e);
        }
        return "data:image/jpg;base64," + pngBase64;
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
        g2d.drawImage(foregroundImage, 170, 272, null);
        // 释放资源
        g2d.dispose();
        return combinedImage;
    }

    /**
     * @Author:tuzhi-cai
     * @Description: 二维码构画
     * @time:2018年2月2日下午2:03:26
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (matrix.get(x, y) ? BLACK : WHITE));
            }
        }
        return image;
    }

    /**
     * @param matrixImage 生成的二维码
     * @param logUri      logo地址
     * @return 带有logo的二维码
     * @Author:tuzhi-cai
     * @Description: 传递二维码，设置带logo的二维码
     * @time:2018年2月2日下午1:58:01
     */
    public static BufferedImage setMatrixLogo(BufferedImage matrixImage, String logUri) throws IOException {
        /**
         * 读取二维码图片，并构建绘图对象
         */
        Graphics2D g2 = matrixImage.createGraphics();
        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();
        //中间图
        byte[] files = HttpUtil.downloadBytes(logUri);
        ByteArrayInputStream in = new ByteArrayInputStream(files);
        BufferedImage logo = ImageIO.read(in);//读取logo图片

        //开始绘制图片前两个、/5*2/5*2 后/5
        g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, null);
        //绘制边框
        BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // 设置笔画对象
        g2.setStroke(stroke);
        //指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, 20, 20);
        g2.setColor(Color.white);
        // 绘制圆弧矩形
        g2.draw(round);

        //设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // 设置笔画对象
        g2.setStroke(stroke2);
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeigh / 5 * 2 + 2, matrixWidth / 5 - 4, matrixHeigh / 5 - 4, 20, 20);
        g2.setColor(new Color(128, 128, 128));
        g2.draw(round2);// 绘制圆弧矩形
        g2.dispose(); //执行刷出返回带logo二维码
        matrixImage.flush();
        return matrixImage;

    }

    /*
     *@Description: 生成base64二维码
     *@Param: [data, width, height]
     *@Return: java.lang.String
     *@author: xiaopeng.wu
     *@DateTime: 11:08 2023/12/25
     **/
    public static String generateQRCodeBase64(String data, int width, int height) {
        String pngBase64 = "";
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符编码
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H); // 错误纠正级别
            hints.put(EncodeHintType.MARGIN, 1); // 二维码边距

            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建BufferedImage对象来表示QR码
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
            ImageIO.write(image, "png", baos);//写入流中
            byte[] bytes = baos.toByteArray();//转换成字节
            BASE64Encoder encoder = new BASE64Encoder();
            pngBase64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
            pngBase64 = pngBase64.replaceAll("\n", "").replaceAll("\r", "");
            log.info("值为：" + "data:image/jpg;base64," + pngBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "data:image/jpg;base64," + pngBase64;
    }

    /*
     *@Description: 生成条形码
     *@Param: [data, width, height, filePath]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 16:32 2023/11/27
     **/
    public static void generateBarcode(String data, int width, int height, String filePath) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符编码

            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.CODE_128, width, height, hints);

            // 创建BufferedImage对象来表示条形码
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0 : 0xFFFFFF); // 生成黑色条和白色背景的条形码
                }
            }

            // 将条形码保存到文件
            File barcodeFile = new File(filePath);
            ImageIO.write(image, "png", barcodeFile);

            System.out.println("条形码已生成并保存到: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addFontImage(BufferedImage source, String declareText) {
        //生成image
        int defineWidth = QRCODE_SIZE;
        int defineHeight = 20;
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


    public static void main(String[] args) {
        String data = "https://todoitbo.fun"; // 要存储在QR码中的数据
        int width = 300; // QR码的宽度
        int height = 300; // QR码的高度
        String filePath = "qrcode.png"; // 生成的QR码文件的路径

        generateQRCode(data, width, height, filePath);


        String data1 = "123456789"; // 要存储在条形码中的数据
        int width1 = 200; // 条形码的宽度
        int height1 = 100; // 条形码的高度
        String filePath1 = "barcode.png"; // 生成的条形码文件的路径

        generateBarcode(data1, width1, height1, filePath1);
    }
}
