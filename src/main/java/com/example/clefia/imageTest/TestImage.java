package com.example.clefia.imageTest;

import com.groupdocs.metadata.Metadata;
import com.groupdocs.metadata.core.IExif;
import com.groupdocs.metadata.core.IReadOnlyList;
import com.groupdocs.metadata.core.MetadataProperty;
import com.groupdocs.metadata.core.TiffTag;
import com.groupdocs.metadata.search.ContainsTagSpecification;
import com.groupdocs.metadata.tagging.Tags;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.*;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.sax.SAXSource;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;

public class TestImage {

    public TestImage() {
        System.out.println("--------------");
        /*
        File f = new File("C:\\Users\\Nicolas\\IdeaProjects\\Clefia1C2021\\src\\main\\resources\\castle.jpg");
        Path file = f.toPath();
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(file, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("creationTime: " + attr.creationTime());
        System.out.println("lastAccessTime: " + attr.lastAccessTime());
        System.out.println("lastModifiedTime: " + attr.lastModifiedTime());

        System.out.println("isDirectory: " + attr.isDirectory());
        System.out.println("isOther: " + attr.isOther());
        System.out.println("isRegularFile: " + attr.isRegularFile());
        System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
        System.out.println(attr.size());
        */

        /*
        try {
            InputStream is = new FileInputStream("C:\\Users\\Nicolas\\IdeaProjects\\Clefia1C2021\\src\\main\\resources\\castle.jpg");


            Metadata metadata = new Metadata(is);
            IExif root = (IExif) metadata.getRootPackage();
            if (root.getExifPackage() != null) {
                String pattern = "%s = %s";
                // Reading all EXIF tags.
                for (TiffTag tag : root.getExifPackage().toList()) {
                    System.out.println(String.format(pattern, tag.getName(), tag.getValue()));
                }

                // Extract all EXIF IFD tags.
                for (TiffTag tag : root.getExifPackage().getExifIfdPackage().toList()) {
                    System.out.println(String.format(pattern, tag.getName(), tag.getValue()));
                }
                // Extract all EXIF GPS tags
                for (TiffTag tag : root.getExifPackage().getGpsPackage().toList()) {
                    System.out.println(String.format(pattern, tag.getName(), tag.getValue()));
                }

            }
            is.close();
        } // fin try metadata
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        // Constants.InputPptx is an absolute or relative path to your document. Ex: @"C:\Docs\source.pptx"
        try (Metadata metadata = new Metadata("C:\\Users\\Nicolas\\IdeaProjects\\Clefia1C2021\\src\\main\\resources\\castle.jpg")) {
            // Fetch all the properties satisfying the predicate:
            // property contains the name of the last document editor OR the date/time the document was last modified
            IReadOnlyList<MetadataProperty> properties = metadata.findProperties(
                    new ContainsTagSpecification(Tags.getPerson().getEditor()).or(new ContainsTagSpecification(Tags.getTime().getModified())));
            for (MetadataProperty property : properties) {
                System.out.println(String.format("Property name: %s, Property value: %s", property.getName(), property.getValue()));
            }
        }
         */

        Byte[] array_5 = new Byte[]{0x7F};

        int numb = 0xFF;

        Byte b = 0x1f;

        /**
         * CUANDO HACE EL 0xFF & 0x80
         * HAY QUE PONER != 0
         * PORQUE EN C SI ES DISTINTO DE 0
         * ENTRA POR EL TRUE
         */
        int result = 0xFF & 0x80;
        System.out.println("0xFF & 0x80 = " + result);
        int result1 = result | 0x0F;
        System.out.println("result | 0x0F = " + result1);

        String s = String.valueOf(b);
        System.out.println("s = " + s);
        System.out.println("short value = " + b.shortValue());
        System.out.println("double value = " + b.doubleValue());
        System.out.println("byte value = " + b.byteValue());
        System.out.println("long = " + b.longValue());
        System.out.println("int = " + b.intValue());
        System.out.println("string = " + b.toString());
        System.out.println(new BigInteger("1f", 16).toString(2));

        //String hex = Integer.toHexString(numb);

        //System.out.println(Byte.parseByte(hex,16));

        //System.out.println(ClefiaMul2(array_5[0]));
        /*
        try {
            File imageFile = new File("C://Users/Nicolas/IdeaProjects/Clefia1C2021/src/main/resources/castle.jpg");
            BufferedImage bi = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
        System.out.println("---------------");
    }

    public static Byte ClefiaMul2(Byte x) {
        int i = 0;
        int j = 0;

        /* multiplication over GF(2^8) (p(x) = '11d') */
        if ((x & 0x80) == 1)
            x = (byte) (x ^ 0x0e);

        x= (byte)((x << 1) | (x >>> 7));
        String hex = Integer.toHexString(x & 0xFF);
        return (byte) (Byte.parseByte(hex,16) & Byte.parseByte(hex,16));
    }

}
