package org.clefia.module;

import java.util.List;

public class Utils {

    private static void addSpaceToList(List<Byte> dst, int length, int maxSize) {
        if (maxSize >= dst.size() + length) {
            for (int i = 0; i < length; i++) {
                dst.add(null);
            }
        }
    }

    private static void copyList(List<Byte> dst, int offsetDst, List<Byte> src, int offsetSrc, int to) {
        for (int i = 0; i < to; i++) {
            dst.set(offsetDst + i, src.get(offsetSrc + i));
        }
    }


    private static void printList(List<Byte> list, int length) {
        while (length > 0) {
            System.out.printf("%02x", list.get(list.size() - length));
            length--;
        }
        System.out.println();
    }




    // convert BufferedImage to byte[]
    public static Byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        Byte[] bytes = ArrayUtils.toObject(baos.toByteArray());
        return bytes;

    }

    // convert byte[] to BufferedImage
//    public static BufferedImage toBufferedImage(byte[] bytes)
//            throws IOException {
//        InputStream is = new ByteArrayInputStream(bytes);
//        BufferedImage bi = ImageIO.read(is);
//        return bi;
//
//    }
//
//    public static byte[] listToByteArray(List<Byte> list) {
//
//        Byte[] byteUpper = new Byte[list.size()];
//        list.toArray(byteUpper);
//
//        return ArrayUtils.toPrimitive(byteUpper);
//
//    }

    private static void fillLastList(List<Byte> toFill, List<Byte> pt) {
        int size = pt.size();
        int n = size % 16;
        int index = size - 1 - n;

        while (n > 0 && index != size) {
            toFill.add(pt.get(index));
            index++;
            n--;
        }

        for (int i = toFill.size(); i < (16 - n); i++) {
            toFill.add((byte) 0x00);
        }
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }
}
