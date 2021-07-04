package com.clefia.controllers;

import com.clefia.services.IClefiaService;
import org.springframework.boot.context.properties.bind.Binder;
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
import javax.servlet.http.HttpSession;
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
@ControllerAdvice
public class ClefiaController {
    @Autowired
    private IClefiaService clefiaService;
    public String key;

    public static String pathEncrypted;
    public static String pathDecrypted;

    @RequestMapping(value = "/", method = GET)
    public String home(HttpSession session) {
        session.setAttribute("pathEncrypted",pathEncrypted);
        return "index.html";
    }

    @PostMapping("/encriptarArchivo")
    public String encriptarArchivo(@RequestParam("uploadedFileName") MultipartFile multipartFile,
                                   Model model,
                                   @ModelAttribute("keySize") String keySize) throws IOException {
        // convertimos multipart file
        int r;
        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        File file = null;
        file = File.createTempFile(fileName, prefix);
        multipartFile.transferTo(file);
        File fileEncrypted = clefiaService.encriptarImagen(file, Integer.valueOf(keySize), fileName);
        pathEncrypted = fileEncrypted.getAbsolutePath();
        File fileDecrypted = clefiaService.desencriptarArchivo(fileEncrypted, Integer.valueOf(keySize), fileEncrypted.getName());
        pathDecrypted =fileDecrypted.getAbsolutePath();
         //bound = Binder.bind("pathDecrypter", pathDecrypted);
        addAttributes(model);

        return "redirect:/";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pathEncrypted",pathEncrypted);
    }


}
