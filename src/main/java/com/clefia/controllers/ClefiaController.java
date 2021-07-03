package com.clefia.controllers;

import com.clefia.services.IClefiaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import utils.ClefiaCipher;
import utils.FunctionHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ClefiaController {
    @Autowired
    private IClefiaService clefiaService;

    @RequestMapping(value = "/", method = GET)
    public String home() {
        return "index.html";
    }

    @PostMapping("/encriptarArchivo")
    public String encriptarArchivo(@RequestParam("uploadedFileName") MultipartFile multipartFile
            , ModelMap model) throws IOException {
        // convertimos multipart file
        int r;
        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        File file = null;
        file = File.createTempFile(fileName, prefix);
        multipartFile.transferTo(file);
        //levantamos imagen
        BufferedImage image = ImageIO.read(file);
        byte[] bufferByte = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        FileInputStream fis = new FileInputStream(file.getAbsoluteFile());

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

        ImageIO.write(encryptedImage, multipartFile.getOriginalFilename().split("\\.")[1],
                new File("src/main/resources/images/encrypted/" +
                        multipartFile.getOriginalFilename()));
        fis.close();

        //retornamos model and view
//        ModelAndView mav = new ModelAndView();
//
//        mav.addObject("path", "src/main/resources/images/encrypted/" +
//                multipartFile.getOriginalFilename());
//        mav.setViewName("src/main/resources/images/encrypted/" +
////                multipartFile.getOriginalFilename());
        model.addAttribute("path","src/main/resources/images/encrypted/" +
                multipartFile.getOriginalFilename());
//        return mav;
        return "redirect:/";
    }


}
