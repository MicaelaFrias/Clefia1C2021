package com.clefia.services;

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public interface IClefiaService {
    File encriptarImagen(File file, Integer keySize, String fileName) throws IOException;
    void desencriptarArchivo(File file, Integer keySize,String fileName) throws IOException;
}
