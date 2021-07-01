package com.example.clefia;

import com.groupdocs.metadata.Metadata;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ClefiaApplication {

    public static String filePath = "src/main/resources/castle4";
    public static String format = ".png";



    public static void main(String[] args) throws IOException {
        int r;

        //levantamos imagen
        BufferedImage image = ImageIO.read(new File(filePath + format));
        byte[] bufferbyte = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        FileInputStream fis = new FileInputStream(filePath + format);

        byte[] data = new byte[fis.available()];
        Byte[] imageByte;

        // Read the array
        fis.read(data);
        List<Byte> skey = new ArrayList<>();
        List<Byte> pt = Arrays.asList(ArrayUtils.toObject(bufferbyte));
        List<Byte> ct = new ArrayList<>();
        List<Byte> dst = new ArrayList<>();
        List<Byte> rk = new ArrayList<>();

        chargeSkey(skey);

        addSpaceToList(dst, 16, 16);
        addSpaceToList(ct, 16, 16);
        addSpaceToList(rk, (8 * 26 + 16), (8 * 26 + 16));

        System.out.println("plaintext:  ");
        printList(pt, 16);
        System.out.println("secretkey:  ");
        printList(skey, 32);

        // For 128-bit key
        System.out.println("\n--- CLEFIA-128 ---\n");

        /********************************** Encryption*************************************/

        List<Byte> subList = new ArrayList<>();
        List<Byte> encriptedImageList = new ArrayList<>();
        int offset = 0;

        if (pt.size() % 16 != 0) {
            offset = 16 - pt.size() % 16;
        }

        // encripta de a bloques de 16 bytes por lo tanto hay que ir cortando la imagen en bloques de 16 bytes
        for (int i = 0; i < (pt.size() / 16); i++) {

            subList = pt.subList(i * 16, (i * 16) + 16);

            r = ClefiaKeySet(rk, skey, 128);
            ClefiaEncryptTest(dst, subList, rk, r);
            encriptedImageList.addAll(dst);

        }

        if (offset != 0) { // quiere decir que el ultimo bloque de bytes no llega a 16.

            List<Byte> lastList = new ArrayList<>();
            fillLastList(lastList, pt);

            r = ClefiaKeySet(rk, skey, 128);
            ClefiaEncryptTest(dst, lastList, rk, r);

            encriptedImageList.addAll(dst);
            offset = 0;
        }

        // convert the byte[] back to BufferedImage
        byte[] imageBuffer = listToByteArray(encriptedImageList);

        DataBufferByte buffer = new DataBufferByte(imageBuffer, imageBuffer.length);

        WritableRaster wrRaster = Raster.createWritableRaster(image.getSampleModel(), buffer, null);
        BufferedImage encriptedImage = new BufferedImage(image.getColorModel(), wrRaster,
                image.getColorModel().isAlphaPremultiplied(), null);

        ImageIO.write(encriptedImage, format.split("\\.")[1], new File(filePath + "encripted" +format));
        fis.close();

        /************************************Encripted to Desencripted***********************************************/

        fis = new FileInputStream(filePath + "encripted" +format);
        data = new byte[fis.available()];

        // Read the array
        fis.read(data);
        BufferedImage image2 = ImageIO.read(new File(filePath + "encripted" +format));
        byte[] bufferbyte2 = ((DataBufferByte) image2.getRaster().getDataBuffer()).getData();

        pt = Arrays.asList(ArrayUtils.toObject(bufferbyte2));

        /********************************** Decryption *************************************/

        subList = new ArrayList<>();
        List<Byte> desEncriptedImageList = new ArrayList<>();

        if (pt.size() % 16 != 0) {
            offset = 16 - pt.size() % 16;
        }

        // encripta de a bloques de 16 bytes por lo tanto hay que ir cortando la imagen en bloques de 16 bytes
        for (int i = 0; i < (pt.size() / 16); i++) {
            subList = pt.subList(i * 16, (i * 16) + 16);
            r = ClefiaKeySet(rk, skey, 128);
            ClefiaDecryptTest(dst, subList, rk, r);
            desEncriptedImageList.addAll(dst);
        }

        if (offset != 0) { // quiere decir que el ultimo bloque de bytes no llega a 16.
            List<Byte> lastList = new ArrayList<>();
            fillLastList(lastList, pt);
            r = ClefiaKeySet(rk, skey, 128);
            ClefiaDecryptTest(dst, subList, rk, r);
            desEncriptedImageList.addAll(dst);
        }

        // convert the byte[] back to BufferedImage
        imageBuffer = listToByteArray(desEncriptedImageList);
        buffer = new DataBufferByte(imageBuffer, imageBuffer.length);
        wrRaster = Raster.createWritableRaster(image.getSampleModel(), buffer, null);
        BufferedImage desEncriptedImage = new BufferedImage(image.getColorModel(), wrRaster,
                image.getColorModel().isAlphaPremultiplied(), null);

        ImageIO.write(desEncriptedImage, format.split("\\.")[1], new File(filePath + "desencripted" +format));
        fis.close();

        /***********************************************************************************/

//        // For 192-bit key
//        System.out.println("\n--- CLEFIA-192 ---\n");
//
//        // Encryption
//        r = ClefiaKeySet(rk, skey, 192);
//        ClefiaEncryptTest(dst, pt, rk, r);
//        System.out.println("ciphertext: ");
//        printList(dst, 16);
//
//        // Decryption
//        copyList(ct, 0, dst, 0, 16);
//        r = ClefiaKeySet(rk, skey, 192);
//        ClefiaDecryptTest(dst, ct, rk, r);
//        System.out.println("plaintext : ");
//        printList(dst, 16);
//
//        // For 256-bit key
//        System.out.println("\n--- CLEFIA-256 ---\n");
//        // Encryption
//        r = ClefiaKeySet(rk, skey, 256);
//        ClefiaEncryptTest(dst, pt, rk, r);
//        System.out.println("ciphertext: ");
//        printList(dst, 16);
//
//        // Decryption
//        copyList(ct, 0, dst, 0, 16);
//        r = ClefiaKeySet(rk, skey, 256);
//        ClefiaDecryptTest(dst, ct, rk, r);
//        System.out.println("plaintext : ");
//        printList(dst, 16);
    }

    private static void addMetadata(List<Byte> encriptedImageList, List<Byte> dataList, int sizeMetadata) {
        for (int i = 0; i < sizeMetadata; i++) {
            encriptedImageList.add(dataList.get(i));
        }
    }
}
