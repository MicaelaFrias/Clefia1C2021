package com.clefia;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utils.ClefiaCipher;
import utils.FunctionHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ClefiaApplication {
    public static String filePath = "src/main/resources/images/*/castle4";
    public static String format = ".png";

    public static void main(String[] args) throws IOException {
        int r;

        //levantamos imagen
        BufferedImage image = ImageIO.read(new File(filePath.replace("*", "source") + format));
        byte[] bufferByte = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        FileInputStream fis = new FileInputStream(filePath.replace("*", "source") + format);

        byte[] data = new byte[fis.available()];

        // Read the array
        int readResult = fis.read(data);
        System.out.println("Read Result: " + readResult);
        List<Byte> skey = new ArrayList<>();
        List<Byte> pt = Arrays.asList(ArrayUtils.toObject(bufferByte));
        List<Byte> ct = new ArrayList<>();
        List<Byte> dst = new ArrayList<>();
        List<Byte> rk = new ArrayList<>();

        ClefiaCipher.chargeSkey(skey);

        FunctionHelper.addSpaceToList(dst, 16, 16);
        FunctionHelper.addSpaceToList(ct, 16, 16);
        FunctionHelper.addSpaceToList(rk, (8 * 26 + 16), (8 * 26 + 16));

        System.out.println("Plain Text:  ");
        FunctionHelper.printList(pt, 16);
        System.out.println("Secret Key:  ");
        FunctionHelper.printList(skey, 32);

        // For 128-bit key
        System.out.println("\n--- CLEFIA-128 ---\n");

        /*
        ********************************* Encryption ************************************
        */

        List<Byte> subList;
        List<Byte> encryptedImageList = new ArrayList<>();
        int offset = 0;

        if (pt.size() % 16 != 0) {
            offset = 16 - pt.size() % 16;
        }

        // encripta de a bloques de 16 bytes por lo tanto hay que ir cortando la imagen en bloques de 16 bytes
        for (int i = 0; i < (pt.size() / 16); i++) {

            subList = pt.subList(i * 16, (i * 16) + 16);

            r = ClefiaCipher.ClefiaKeySet(rk, skey, 128);
            ClefiaCipher.ClefiaEncryptTest(dst, subList, rk, r);
            encryptedImageList.addAll(dst);

        }

        if (offset != 0) { // quiere decir que el ultimo bloque de bytes no llega a 16.

            List<Byte> lastList = new ArrayList<>();
            FunctionHelper.fillLastList(lastList, pt);

            r = ClefiaCipher.ClefiaKeySet(rk, skey, 128);
            ClefiaCipher.ClefiaEncryptTest(dst, lastList, rk, r);

            encryptedImageList.addAll(dst);
            offset = 0;
        }

        // convert the byte[] back to BufferedImage
        byte[] imageBuffer = FunctionHelper.listToByteArray(encryptedImageList);

        DataBufferByte buffer = new DataBufferByte(imageBuffer, imageBuffer.length);

        WritableRaster wrRaster = Raster.createWritableRaster(image.getSampleModel(), buffer, null);
        BufferedImage encryptedImage = new BufferedImage(image.getColorModel(), wrRaster,
                image.getColorModel().isAlphaPremultiplied(), null);

        ImageIO.write(encryptedImage, format.split("\\.")[1], new File(filePath.replace("*", "encrypted") + "-encrypted" + format));
        fis.close();

        /*
        *********************************** Encrypted to Decrypted **********************************************
        */

        fis = new FileInputStream(filePath.replace("*", "encrypted") + "-encrypted" + format);
        data = new byte[fis.available()];

        // Read the array
        readResult = fis.read(data);
        System.out.println("Read Result: " + readResult);
        BufferedImage image2 = ImageIO.read(new File(filePath.replace("*", "encrypted") + "-encrypted" + format));
        byte[] bufferByte2 = ((DataBufferByte) image2.getRaster().getDataBuffer()).getData();

        pt = Arrays.asList(ArrayUtils.toObject(bufferByte2));

        /*
        ********************************* Decryption ************************************
        */

        subList = new ArrayList<>();
        List<Byte> desEncryptedImageList = new ArrayList<>();

        if (pt.size() % 16 != 0) {
            offset = 16 - pt.size() % 16;
        }

        // encripta de a bloques de 16 bytes por lo tanto hay que ir cortando la imagen en bloques de 16 bytes
        for (int i = 0; i < (pt.size() / 16); i++) {
            subList = pt.subList(i * 16, (i * 16) + 16);
            r = ClefiaCipher.ClefiaKeySet(rk, skey, 128);
            ClefiaCipher.ClefiaDecryptTest(dst, subList, rk, r);
            desEncryptedImageList.addAll(dst);
        }

        if (offset != 0) { // quiere decir que el ultimo bloque de bytes no llega a 16.
            List<Byte> lastList = new ArrayList<>();
            FunctionHelper.fillLastList(lastList, pt);
            r = ClefiaCipher.ClefiaKeySet(rk, skey, 128);
            ClefiaCipher.ClefiaDecryptTest(dst, subList, rk, r);
            desEncryptedImageList.addAll(dst);
        }

        // convert the byte[] back to BufferedImage
        imageBuffer = FunctionHelper.listToByteArray(desEncryptedImageList);
        buffer = new DataBufferByte(imageBuffer, imageBuffer.length);
        wrRaster = Raster.createWritableRaster(image.getSampleModel(), buffer, null);
        BufferedImage desEncryptedImage = new BufferedImage(image.getColorModel(), wrRaster,
                image.getColorModel().isAlphaPremultiplied(), null);

        ImageIO.write(desEncryptedImage, format.split("\\.")[1], new File(filePath.replace("*", "decrypted") + "-decrypted" + format));
        fis.close();

        /*
          **********************************************************************************/

//        // For 192-bit key
//        System.out.println("\n--- CLEFIA-192 ---\n");
//
//        // Encryption
//        r = ClefiaCipher.ClefiaKeySet(rk, skey, 192);
//        ClefiaEncryptTest(dst, pt, rk, r);
//        System.out.println("ciphertext: ");
//        printList(dst, 16);
//
//        // Decryption
//        copyList(ct, 0, dst, 0, 16);
//        r = ClefiaCipher.ClefiaKeySet(rk, skey, 192);
//        ClefiaCipher.ClefiaDecryptTest(dst, ct, rk, r);
//        System.out.println("plaintext : ");
//        printList(dst, 16);
//
//        // For 256-bit key
//        System.out.println("\n--- CLEFIA-256 ---\n");
//        // Encryption
//        r = ClefiaCipher.ClefiaKeySet(rk, skey, 256);
//        ClefiaEncryptTest(dst, pt, rk, r);
//        System.out.println("ciphertext: ");
//        printList(dst, 16);
//
//        // Decryption
//        copyList(ct, 0, dst, 0, 16);
//        r = ClefiaCipher.ClefiaKeySet(rk, skey, 256);
//        ClefiaCipher.ClefiaDecryptTest(dst, ct, rk, r);
//        System.out.println("plaintext : ");
//        printList(dst, 16);

        SpringApplication.run(ClefiaApplication.class, args);
    }

//    private static void addMetadata(List<Byte> encryptedImageList, List<Byte> dataList, int sizeMetadata) {
//        for (int i = 0; i < sizeMetadata; i++) {
//            encryptedImageList.add(dataList.get(i));
//        }
//    }
}
