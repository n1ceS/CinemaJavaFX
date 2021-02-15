package pl.marczuk.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageConverter {
    public static byte[] imgToByte(File file) throws IOException {
        BufferedImage bufferimage = ImageIO.read(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferimage, "jpg", output );
        byte [] data = output.toByteArray();
        return data;
    }
    public static void byteToJpg(byte[] data, String outputName) throws IOException {
        BufferedImage image = ImageIO.read( new ByteArrayInputStream( data ) );
        ImageIO.write(image, "JPG", new File("src\\pl\\marczuk\\images\\dashboard\\"+outputName));
    }
}
