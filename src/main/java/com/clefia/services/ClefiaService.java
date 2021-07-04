package com.clefia.services;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

@Service
public class ClefiaService implements IClefiaService {

    public File encriptarImagen(File file, Integer keySize, String fileName) throws IOException {
        int r;
        //levantamos imagen
        BufferedImage image = ImageIO.read(file);
        byte[] bufferByte = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        List<Byte> skey = new ArrayList<>();
        List<Byte> pt = Arrays.asList(ArrayUtils.toObject(bufferByte));
        List<Byte> ct = new ArrayList<>();
        List<Byte> dst = new ArrayList<>();
        List<Byte> rk = new ArrayList<>();

        ClefiaCipher.chargeSkey(skey);

        FunctionHelper.addSpaceToList(dst, 16, 16);
        FunctionHelper.addSpaceToList(ct, 16, 16);
        FunctionHelper.addSpaceToList(rk, (8 * 26 + 16), (8 * 26 + 16));

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

            r = ClefiaCipher.ClefiaKeySet(rk, skey, keySize);
            ClefiaCipher.ClefiaEncryptTest(dst, subList, rk, r);
            encryptedImageList.addAll(dst);

        }

        if (offset != 0) { // quiere decir que el ultimo bloque de bytes no llega a 16.

            List<Byte> lastList = new ArrayList<>();
            FunctionHelper.fillLastList(lastList, pt);

            r = ClefiaCipher.ClefiaKeySet(rk, skey, keySize);
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
        String extension = fileName.split("\\.")[1];
        String name = fileName.split("\\.")[0];
        File fileEncrypted = new File("src/main/resources/static/images/encrypted/" +
                name + "-" + keySize.toString() + "." + extension);
        ImageIO.write(encryptedImage, extension,
                fileEncrypted);
        return fileEncrypted;
    }

    public File desencriptarArchivo(File file, Integer keySize, String fileName) throws IOException {
        int r;
        List<Byte> skey = new ArrayList<>();
        List<Byte> ct = new ArrayList<>();
        List<Byte> dst = new ArrayList<>();
        List<Byte> rk = new ArrayList<>();
        List<Byte> subList;
        List<Byte> encryptedImageList = new ArrayList<>();
        int offset = 0;


        BufferedImage image2 = ImageIO.read(file);
        byte[] bufferByte2 = ((DataBufferByte) image2.getRaster().getDataBuffer()).getData();
        List<Byte> pt = Arrays.asList(ArrayUtils.toObject(bufferByte2));

        ClefiaCipher.chargeSkey(skey);

        FunctionHelper.addSpaceToList(dst, 16, 16);
        FunctionHelper.addSpaceToList(ct, 16, 16);
        FunctionHelper.addSpaceToList(rk, (8 * 26 + 16), (8 * 26 + 16));

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
            r = ClefiaCipher.ClefiaKeySet(rk, skey, keySize);
            ClefiaCipher.ClefiaDecryptTest(dst, subList, rk, r);
            desEncryptedImageList.addAll(dst);
        }

        if (offset != 0) { // quiere decir que el ultimo bloque de bytes no llega a 16.
            List<Byte> lastList = new ArrayList<>();
            FunctionHelper.fillLastList(lastList, pt);
            r = ClefiaCipher.ClefiaKeySet(rk, skey, keySize);
            ClefiaCipher.ClefiaDecryptTest(dst, subList, rk, r);
            desEncryptedImageList.addAll(dst);
        }

        // convert the byte[] back to BufferedImage
        byte[] imageBuffer = FunctionHelper.listToByteArray(desEncryptedImageList);
        DataBufferByte buffer = new DataBufferByte(imageBuffer, imageBuffer.length);
        WritableRaster wrRaster = Raster.createWritableRaster(image2.getSampleModel(), buffer, null);
        BufferedImage decryptedImage = new BufferedImage(image2.getColorModel(), wrRaster,
                image2.getColorModel().isAlphaPremultiplied(), null);

        String extension = fileName.split("\\.")[1];
        String name = fileName.split("\\.")[0];
        File fileDecrypted = new File("src/main/resources/static/images/decrypted/" +
                name + "." + extension);

        ImageIO.write(decryptedImage, extension,
                fileDecrypted);
        return fileDecrypted;
    }
}
