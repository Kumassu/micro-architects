package song.pan.toolkit.qrcode.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author Song Pan
 * @version 1.0.0
 */
public class QRCodeUtils {


    public static void generate(int width, int height, String content, String format, OutputStream stream) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
    }


    public static byte[] toBytes(String content) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300);
        Path path = Paths.get("/temp/qrcode." + System.currentTimeMillis() + ".png");
        path.getParent().toFile().mkdirs();
        MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
        return IOUtils.toString(new FileInputStream(path.toFile()), StandardCharsets.UTF_8).getBytes();
    }

    public static BufferedImage toImage(String content) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }




    public static void generate(String content, OutputStream stream) throws WriterException, IOException {
        generate(300, 300, content, "jpg", stream);
    }


    public static String read(InputStream inputStream) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(inputStream);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Result result = new MultiFormatReader().decode(binaryBitmap, Map.of(DecodeHintType.CHARACTER_SET, "utf-8"));
        return result.getText();
    }

    public static void main(String[] args) throws IOException, WriterException {
        toBytes("123");
    }


}
