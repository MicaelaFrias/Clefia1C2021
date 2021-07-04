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
        SpringApplication.run(ClefiaApplication.class, args);
    }

}
