package com.example.clefia;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ClefiaApplication {

    // S0 (8-bit S-box based on four 4-bit S-boxes)
    static int[] clefia_s0 = {
            0x57, 0x49, 0xd1, 0xc6, 0x2f, 0x33, 0x74, 0xfb,
            0x95, 0x6d, 0x82, 0xea, 0x0e, 0xb0, 0xa8, 0x1c,
            0x28, 0xd0, 0x4b, 0x92, 0x5c, 0xee, 0x85, 0xb1,
            0xc4, 0x0a, 0x76, 0x3d, 0x63, 0xf9, 0x17, 0xaf,
            0xbf, 0xa1, 0x19, 0x65, 0xf7, 0x7a, 0x32, 0x20,
            0x06, 0xce, 0xe4, 0x83, 0x9d, 0x5b, 0x4c, 0xd8,
            0x42, 0x5d, 0x2e, 0xe8, 0xd4, 0x9b, 0x0f, 0x13,
            0x3c, 0x89, 0x67, 0xc0, 0x71, 0xaa, 0xb6, 0xf5,
            0xa4, 0xbe, 0xfd, 0x8c, 0x12, 0x00, 0x97, 0xda,
            0x78, 0xe1, 0xcf, 0x6b, 0x39, 0x43, 0x55, 0x26,
            0x30, 0x98, 0xcc, 0xdd, 0xeb, 0x54, 0xb3, 0x8f,
            0x4e, 0x16, 0xfa, 0x22, 0xa5, 0x77, 0x09, 0x61,
            0xd6, 0x2a, 0x53, 0x37, 0x45, 0xc1, 0x6c, 0xae,
            0xef, 0x70, 0x08, 0x99, 0x8b, 0x1d, 0xf2, 0xb4,
            0xe9, 0xc7, 0x9f, 0x4a, 0x31, 0x25, 0xfe, 0x7c,
            0xd3, 0xa2, 0xbd, 0x56, 0x14, 0x88, 0x60, 0x0b,
            0xcd, 0xe2, 0x34, 0x50, 0x9e, 0xdc, 0x11, 0x05,
            0x2b, 0xb7, 0xa9, 0x48, 0xff, 0x66, 0x8a, 0x73,
            0x03, 0x75, 0x86, 0xf1, 0x6a, 0xa7, 0x40, 0xc2,
            0xb9, 0x2c, 0xdb, 0x1f, 0x58, 0x94, 0x3e, 0xed,
            0xfc, 0x1b, 0xa0, 0x04, 0xb8, 0x8d, 0xe6, 0x59,
            0x62, 0x93, 0x35, 0x7e, 0xca, 0x21, 0xdf, 0x47,
            0x15, 0xf3, 0xba, 0x7f, 0xa6, 0x69, 0xc8, 0x4d,
            0x87, 0x3b, 0x9c, 0x01, 0xe0, 0xde, 0x24, 0x52,
            0x7b, 0x0c, 0x68, 0x1e, 0x80, 0xb2, 0x5a, 0xe7,
            0xad, 0xd5, 0x23, 0xf4, 0x46, 0x3f, 0x91, 0xc9,
            0x6e, 0x84, 0x72, 0xbb, 0x0d, 0x18, 0xd9, 0x96,
            0xf0, 0x5f, 0x41, 0xac, 0x27, 0xc5, 0xe3, 0x3a,
            0x81, 0x6f, 0x07, 0xa3, 0x79, 0xf6, 0x2d, 0x38,
            0x1a, 0x44, 0x5e, 0xb5, 0xd2, 0xec, 0xcb, 0x90,
            0x9a, 0x36, 0xe5, 0x29, 0xc3, 0x4f, 0xab, 0x64,
            0x51, 0xf8, 0x10, 0xd7, 0xbc, 0x02, 0x7d, 0x8e
    };

    // S1 (8-bit S-box based on inverse function)
    static int[] clefia_s1 = {
            0x6c, 0xda, 0xc3, 0xe9, 0x4e, 0x9d, 0x0a, 0x3d,
            0xb8, 0x36, 0xb4, 0x38, 0x13, 0x34, 0x0c, 0xd9,
            0xbf, 0x74, 0x94, 0x8f, 0xb7, 0x9c, 0xe5, 0xdc,
            0x9e, 0x07, 0x49, 0x4f, 0x98, 0x2c, 0xb0, 0x93,
            0x12, 0xeb, 0xcd, 0xb3, 0x92, 0xe7, 0x41, 0x60,
            0xe3, 0x21, 0x27, 0x3b, 0xe6, 0x19, 0xd2, 0x0e,
            0x91, 0x11, 0xc7, 0x3f, 0x2a, 0x8e, 0xa1, 0xbc,
            0x2b, 0xc8, 0xc5, 0x0f, 0x5b, 0xf3, 0x87, 0x8b,
            0xfb, 0xf5, 0xde, 0x20, 0xc6, 0xa7, 0x84, 0xce,
            0xd8, 0x65, 0x51, 0xc9, 0xa4, 0xef, 0x43, 0x53,
            0x25, 0x5d, 0x9b, 0x31, 0xe8, 0x3e, 0x0d, 0xd7,
            0x80, 0xff, 0x69, 0x8a, 0xba, 0x0b, 0x73, 0x5c,
            0x6e, 0x54, 0x15, 0x62, 0xf6, 0x35, 0x30, 0x52,
            0xa3, 0x16, 0xd3, 0x28, 0x32, 0xfa, 0xaa, 0x5e,
            0xcf, 0xea, 0xed, 0x78, 0x33, 0x58, 0x09, 0x7b,
            0x63, 0xc0, 0xc1, 0x46, 0x1e, 0xdf, 0xa9, 0x99,
            0x55, 0x04, 0xc4, 0x86, 0x39, 0x77, 0x82, 0xec,
            0x40, 0x18, 0x90, 0x97, 0x59, 0xdd, 0x83, 0x1f,
            0x9a, 0x37, 0x06, 0x24, 0x64, 0x7c, 0xa5, 0x56,
            0x48, 0x08, 0x85, 0xd0, 0x61, 0x26, 0xca, 0x6f,
            0x7e, 0x6a, 0xb6, 0x71, 0xa0, 0x70, 0x05, 0xd1,
            0x45, 0x8c, 0x23, 0x1c, 0xf0, 0xee, 0x89, 0xad,
            0x7a, 0x4b, 0xc2, 0x2f, 0xdb, 0x5a, 0x4d, 0x76,
            0x67, 0x17, 0x2d, 0xf4, 0xcb, 0xb1, 0x4a, 0xa8,
            0xb5, 0x22, 0x47, 0x3a, 0xd5, 0x10, 0x4c, 0x72,
            0xcc, 0x00, 0xf9, 0xe0, 0xfd, 0xe2, 0xfe, 0xae,
            0xf8, 0x5f, 0xab, 0xf1, 0x1b, 0x42, 0x81, 0xd6,
            0xbe, 0x44, 0x29, 0xa6, 0x57, 0xb9, 0xaf, 0xf2,
            0xd4, 0x75, 0x66, 0xbb, 0x68, 0x9f, 0x50, 0x02,
            0x01, 0x3c, 0x7f, 0x8d, 0x1a, 0x88, 0xbd, 0xac,
            0xf7, 0xe4, 0x79, 0x96, 0xa2, 0xfc, 0x6d, 0xb2,
            0x6b, 0x03, 0xe1, 0x2e, 0x7d, 0x14, 0x95, 0x1d
    };

    public static int[] ByteCpy(Byte[] dst, Byte[] src) {
        dst = Arrays.copyOf(src, src.length);
        int[] a = new int[dst.length];
        for(int i = 0; i < dst.length; i++) {
            a[i] = dst[i];
        }
        return a;
    }

    public static Byte ClefiaMul2(Byte x) {
        // multiplication over GF(2^8) (p(x) = '11d')

        if ((getPositiveFromByte(x) & 0x80) != 0)
            x = (byte) (getPositiveFromByte(x) ^ 0x0e);

        return (byte) ((getPositiveFromByte(x) << 1) | (getPositiveFromByte((x)) >> 7));
    }

    public static Byte ClefiaMul4(Byte x) {
        return ClefiaMul2((byte) ClefiaMul2(x));
    }

    public static Byte ClefiaMul8(Byte x) {
        return ClefiaMul2((byte) ClefiaMul4(x));
    }

    public static Byte ClefiaMul6(Byte x) {
        return (byte) (ClefiaMul2(x) ^ ClefiaMul4(x));
    }

    public static Byte ClefiaMulA(Byte x) {
        return (byte) (ClefiaMul2(x) ^ ClefiaMul8(x));
    }

    public static void ByteXorTest(List<Byte> dst, int offsetDst,
                                   List<Byte> a, int offsetA,
                                   List<Byte> b, int offsetB,
                                   int length) {

        for (int i = 0; i < length; i++) {
            dst.set(offsetDst + i, (byte) (a.get(offsetA + i) ^ b.get(offsetB + i)));
        }
    }

    private static void addSpaceToList(List<Byte> dst, int length, int maxSize) {
        if(maxSize >= dst.size() + length) {
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

    public static void ClefiaF0XorTest(List<Byte> dst, int offsetDst,
                                       List<Byte> src, int offsetSrc,
                                       List<Byte> rk, int offsetRk) {

        List<Byte> x = new ArrayList<>();     //tam 4
        List<Byte> y = new ArrayList<>();    // tam 4
        List<Byte> z = new ArrayList<>();    // tam 4

        // Add space to list because size is 0, length = 4 because adding 4 in next function
        addSpaceToList(x, 4, 4);
        // F0
        // Key addition
        ByteXorTest(x, 0, src, offsetSrc, rk, offsetRk, 4);
        // Substitution layer
        z.add((byte) clefia_s0[getPositiveFromByte(x.get(0))]);
        z.add((byte) clefia_s1[getPositiveFromByte(x.get(1))]);
        z.add((byte) clefia_s0[getPositiveFromByte(x.get(2))]);
        z.add((byte) clefia_s1[getPositiveFromByte(x.get(3))]);
        // Diffusion layer (M0)
        y.add((byte) (            z.get(0) ^ ClefiaMul2(z.get(1)) ^ ClefiaMul4(z.get(2)) ^ ClefiaMul6(z.get(3))));
        y.add((byte) (ClefiaMul2(z.get(0)) ^             z.get(1) ^ ClefiaMul6(z.get(2)) ^ ClefiaMul4(z.get(3))));
        y.add((byte) (ClefiaMul4(z.get(0)) ^ ClefiaMul6(z.get(1)) ^             z.get(2) ^ ClefiaMul2(z.get(3))));
        y.add((byte) (ClefiaMul6(z.get(0)) ^ ClefiaMul4(z.get(1)) ^ ClefiaMul2(z.get(2)) ^ z.get(3)));

        // Add space to list because size is 0, length = 4 because adding 4 in next function
        addSpaceToList(dst, 4, 16);
        // Xoring after F0
        copyList(dst, offsetDst, src, offsetSrc, 4);

        // Add space to list because size is 0, length = 4 because adding 4 in next function
        addSpaceToList(dst, 4, 16);
        ByteXorTest(dst, offsetDst + 4, src, offsetSrc + 4, y, 0, 4);
    }

    public static void ClefiaF1XorTest(List<Byte> dst, int offsetDst,
                                       List<Byte> src, int offsetSrc,
                                       List<Byte> rk, int offsetRk) {

        List<Byte> x = new ArrayList<>();     //tam 4
        List<Byte> y = new ArrayList<>();    // tam 4
        List<Byte> z = new ArrayList<>();    // tam 4

        // Add space to list because size is 0, length = 4 because adding 4 in next function
        addSpaceToList(x, 4, 4);
        // F0
        // Key addition
        ByteXorTest(x, 0, src, offsetSrc, rk, offsetRk, 4);
        // Substitution layer
        z.add((byte) clefia_s1[getPositiveFromByte(x.get(0))]);
        z.add((byte) clefia_s0[getPositiveFromByte(x.get(1))]);
        z.add((byte) clefia_s1[getPositiveFromByte(x.get(2))]);
        z.add((byte) clefia_s0[getPositiveFromByte(x.get(3))]);
        // Diffusion layer (M0)
        y.add((byte) (            z.get(0) ^ ClefiaMul8(z.get(1)) ^ ClefiaMul2(z.get(2)) ^ ClefiaMulA(z.get(3))));
        y.add((byte) (ClefiaMul8(z.get(0)) ^             z.get(1) ^ ClefiaMulA(z.get(2)) ^ ClefiaMul2(z.get(3))));
        y.add((byte) (ClefiaMul2(z.get(0)) ^ ClefiaMulA(z.get(1)) ^             z.get(2) ^ ClefiaMul8(z.get(3))));
        y.add((byte) (ClefiaMulA(z.get(0)) ^ ClefiaMul2(z.get(1)) ^ ClefiaMul8(z.get(2)) ^ z.get(3)));

        // Add space to list because size is 0, length = 4 because adding 4 in next function
        addSpaceToList(dst, 4, 16);
        // Xoring after F0
        copyList(dst, offsetDst, src, offsetSrc, 4);

        // Add space to list because size is 0, length = 4 because adding 4 in next function
        addSpaceToList(dst, 4, 16);
        ByteXorTest(dst, offsetDst + 4, src, offsetSrc + 4, y, 0, 4);
    }

    public static Byte[] getBytes(int[] y) {
        Byte[] array = new Byte[y.length];
        for (int i = 0; i < y.length; i++) {
            array[i] = (byte) y[i];
        }
        return array;
    }

    public static void ClefiaGfn4Test(List<Byte> y, List<Byte> x, List<Byte> rk, int offsetRk, int r) {
        List<Byte> fin = new ArrayList<>(); //tam 16
        List<Byte> fout = new ArrayList<>();    // tam 16

        addSpaceToList(fin, 16, 16);
        copyList(fin, 0, x, 0, 16);

        while (r-- > 0) {
            ClefiaF0XorTest(fout, 0, fin, 0, rk, offsetRk);
            ClefiaF1XorTest(fout, 8, fin, 8, rk, offsetRk + 4);

            offsetRk += 8;

            if (r != 0) { // swapping for encryption
                copyList(fin, 0, fout, 4, 12);
                copyList(fin, 12, fout, 0, 4);
            }
        }

        copyList(y, 0, fout, 0, 16);
    }

    public static void ClefiaGfn4InvTest(List<Byte> y, List<Byte> x, List<Byte> rk, int offsetRk, int r) {
        List<Byte> fin = new ArrayList<>(); // tam 16
        List<Byte> fout = new ArrayList<>(); // tam 16

        addSpaceToList(fout, 16, 16);
        addSpaceToList(fin, 16, 16);

        offsetRk += (r - 1) * 8;

        copyList(fin, 0, x, 0, 16);

        while (r-- > 0) {
            ClefiaF0XorTest(fout,0, fin, 0, rk, offsetRk);
            ClefiaF1XorTest(fout, 8, fin, 8, rk, offsetRk + 4);

            offsetRk -= 8;

            if (r != 0) { // swapping for encryption
                copyList(fin, 0, fout, 12, 4);
                copyList(fin, 4, fout, 0, 12);
            }
        }

        copyList(y, 0, fout, 0, 16);
    }

    public static void ClefiaGfn8Test(List<Byte> y, List<Byte> x, List<Byte> rk, int offsetRk, int r) {
        List<Byte> fin = new ArrayList<>(); // tam 32
        List<Byte> fout = new ArrayList<>(); // tam 32

        addSpaceToList(fout, 32, 32);
        addSpaceToList(fin, 32, 32);

        copyList(fin, 0, x, 0, 32);

        while (r-- > 0) {
            ClefiaF0XorTest(fout,  0, fin,  0, rk,        offsetRk      );
            ClefiaF1XorTest(fout,  8, fin,  8, rk, offsetRk + 4 );
            ClefiaF0XorTest(fout, 16, fin, 16, rk, offsetRk + 8 );
            ClefiaF1XorTest(fout, 24, fin, 24, rk, offsetRk + 12);

            offsetRk += 16;

            if (r != 0) { // swapping for encryption
                copyList(fin, 0, fout, 4, 28);
                copyList(fin, 28, fout, 0, 4);
            }
        }

        copyList(y, 0, fout, 0, 32);
    }

    static int ClefiaKeySet(List<Byte> rk, List<Byte> skey, int key_bitlen) {
        if(128 == key_bitlen){
            ClefiaKeySet128(rk, skey);
            return 18;
        } else if(192 == key_bitlen){
            ClefiaKeySet192(rk, skey);
            return 22;
        } else if(256 == key_bitlen){
            ClefiaKeySet256(rk, skey);
            return 26;
        }

        return 0; // invalid key_bitlen
    }

    public static int getPositiveFromByte(int number) {
        return (number<0) ? number + 256 : number;
    }

    public static void ClefiaConSetTest(List<Byte> con, Byte[] iv, int lk) {
        int[] t = new int[2];
        int tmp;

        t = ByteCpy(getBytes(t), iv);

        while (lk-- > 0) {
            con.add((byte) (getPositiveFromByte(t[0]) ^ 0xb7)); // P_16 = 0xb7e1 (natural logarithm)
            con.add((byte) (getPositiveFromByte(t[1]) ^ 0xe1));
            con.add((byte) (~((getPositiveFromByte(t[0]) << 1) | (getPositiveFromByte((t[1])) >> 7))));
            con.add((byte) (~((getPositiveFromByte(t[1]) << 1) | (getPositiveFromByte(t[0]) >> 7))));
            con.add((byte) (~getPositiveFromByte(t[0]) ^ 0x24)); // Q_16 = 0x243f (circle ratio)
            con.add((byte) (~getPositiveFromByte(t[1]) ^ 0x3f));
            con.add((byte) (getPositiveFromByte(t[1])));
            con.add((byte) (getPositiveFromByte(t[0])));
            //con = getSubArray(con, 8);

            // updating T
            if ((getPositiveFromByte(t[1]) & 0x01) != 0) {
                t[0] = getPositiveFromByte(t[0]) ^ 0xa8;
                t[1] = getPositiveFromByte(t[1]) ^ 0x30;
            }
            tmp = (byte) (getPositiveFromByte(t[0]) << 7);
            t[0] = (byte) ((getPositiveFromByte(t[0]) >> 1) | (getPositiveFromByte(t[1]) << 7));
            t[1] = (byte) ((getPositiveFromByte(t[1]) >> 1) | getPositiveFromByte(tmp));
        }
    }

    public static void ClefiaDoubleSwapTest(List<Byte> lk, int offsetLk) {
        List<Byte> t = new ArrayList<>();

        t.add((byte) ((getPositiveFromByte(lk.get(0 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(1 + offsetLk)) >> 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(1 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(2 + offsetLk)) >> 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(2 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(3 + offsetLk)) >> 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(3 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(4 + offsetLk)) >> 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(4 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(5 + offsetLk)) >> 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(5 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(6 + offsetLk)) >> 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(6 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(7 + offsetLk)) >> 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(7 + offsetLk)) << 7) |
                (getPositiveFromByte(lk.get(15 + offsetLk)) & 0x7f)));

        t.add((byte) ((getPositiveFromByte(lk.get(8 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(0 + offsetLk)) & 0xfe)));
        t.add((byte) ((getPositiveFromByte(lk.get(9 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(8 + offsetLk)) << 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(10 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(9 + offsetLk)) << 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(11 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(10 + offsetLk)) << 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(12 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(11 + offsetLk)) << 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(13 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(12 + offsetLk)) << 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(14 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(13 + offsetLk)) << 1)));
        t.add((byte) ((getPositiveFromByte(lk.get(15 + offsetLk)) >> 7) |
                (getPositiveFromByte(lk.get(14 + offsetLk)) << 1)));

        copyList(lk, offsetLk, t, 0, 16);
    }

    public static void ClefiaKeySet128(List<Byte> rk, List<Byte> skey) {
        int i;
        int[] iv = {0x42, 0x8a}; // cubic root of 2
        List<Byte> lk = new ArrayList<>(); // tam 16
        List<Byte> con128 = new ArrayList<>(); // tam 4 * 60

        int offsetRk = 0;
        int offsetConLk = 0;
        int offsetCon128 = 0;
        int offsetSkey = 0;

        addSpaceToList(lk, 16, 16);

        // generating CONi^(128) (0 <= i < 60, lk = 30)
        ClefiaConSetTest(con128, getBytes(iv), 30);
        // GFN_{4,12} (generating L from K)
        ClefiaGfn4Test(lk, skey, con128, offsetRk, 12);

        copyList(rk, offsetRk, skey, offsetSkey, 8); // initial whitening key (WK0, WK1)
        offsetRk += 8;

        for (i = 0; i < 9; i++) { // round key (RKi (0 <= i < 36))
            offsetCon128 = (i * 16) + (4 * 24);
            ByteXorTest(rk, offsetRk, lk, offsetConLk, con128, offsetCon128, 16);
            if ((i % 2) != 0) {
                ByteXorTest(rk, offsetRk, rk, offsetRk, skey, offsetSkey,16); // Xoring K
            }
            ClefiaDoubleSwapTest(lk, 0); // Updating L (DoubleSwap function)
            offsetRk += 16;
        }

        copyList(rk, offsetRk, skey, 8, 8); // final whitening key (WK2, WK3)
    }

    public static void ClefiaKeySet192(List<Byte> rk, List<Byte> skey) {
        int i;
        int[] iv = {0x71, 0x37}; // cubic root of 2
        List<Byte> lk = new ArrayList<>(); // tam 32
        List<Byte> skey256 = new ArrayList<>(); // tam 32
        List<Byte> con192 = new ArrayList<>(); // tam 4 * 84

        int offsetRk = 0;
        int offsetLk = 0;
        int offsetCon192 = 0;
        int offsetSkey256 = 0;

        addSpaceToList(lk, 32, 32);
        addSpaceToList(skey256, 32, 32);

        copyList(skey256, 0, skey, 0, 24);

        for (i = 0; i < 8; i++) {
            skey256.set(i + 24, (byte) ~(skey.get(i)));
        }

        // generating CONi^(192) (0 <= i < 84, lk = 42)
        ClefiaConSetTest(con192, getBytes(iv), 42);
        // GFN_{8,10} (generating L from K)
        ClefiaGfn8Test(lk, skey256, con192, offsetCon192, 10);

        // initial whitening key (WK0, WK1)
        ByteXorTest(rk, offsetRk, skey256, offsetSkey256, skey256, offsetSkey256 + 16, 8);

        offsetRk += 8;

        for (i = 0; i < 11; i++) { // round key (RKi (0 <= i < 44))
            offsetCon192 = (i * 16) + (4 * 40);
            if (((i / 2) % 2) != 0) {
                ByteXorTest(rk, offsetRk, lk, 16, con192, offsetCon192, 16); // LR
                if ((i % 2) != 0) {
                    ByteXorTest(rk, offsetRk, rk, offsetRk, skey256, offsetSkey256, 16); // Xoring KL
                }
                ClefiaDoubleSwapTest(lk, 16); // updating LR
            } else {
                ByteXorTest(rk, offsetRk, lk, offsetLk, con192, offsetCon192, 16); // LL
                if ((i % 2)!=0) {
                    ByteXorTest(rk, offsetRk, rk, offsetRk, skey256, offsetSkey256 + 16, 16); // Xoring KR
                }
                ClefiaDoubleSwapTest(lk, 0);  // updating LL
            }
            offsetRk += 16;
        }
        // final whitening key (WK2, WK3)
        ByteXorTest(rk, offsetRk, skey256,offsetSkey256 + 8, skey256,offsetSkey256 + 24, 8);
    }

    public static void ClefiaKeySet256(List<Byte> rk, List<Byte> skey) {
        int i;
        int[] iv = {0xb5, 0xc0}; // cubic root of 2
        List<Byte> lk = new ArrayList<>(); // tam 32
        List<Byte> con256 = new ArrayList<>(); // tam 4 * 92

        int offsetRk = 0;
        int offsetLk = 0;
        int offsetCon256 = 0;
        int offsetSkey = 0;

        addSpaceToList(lk, 32, 32);

        // generating CONi^(256) (0 <= i < 92, lk = 46)
        ClefiaConSetTest(con256, getBytes(iv), 46);
        // GFN_{8,10} (generating L from K)
        ClefiaGfn8Test(lk, skey, con256, offsetCon256, 10);

        // initial whitening key (WK0, WK1)
        ByteXorTest(rk, offsetRk, skey, offsetSkey, skey, offsetSkey + 16, 8);

        offsetRk += 8;

        for(i = 0; i < 13; i++){ // round key (RKi (0 <= i < 52))
            offsetCon256 = (i * 16) + (4 * 40);
            if(((i / 2) % 2) != 0) {
                ByteXorTest(rk, offsetRk, lk, 16, con256, offsetCon256, 16); // LR
                if((i % 2)!=0) {
                    ByteXorTest(rk, offsetRk, rk, offsetRk, skey, offsetSkey, 16); // Xoring KL
                }
                ClefiaDoubleSwapTest(lk,16); // updating LR
            } else {
                ByteXorTest(rk, offsetRk, lk, offsetLk,  con256, offsetCon256, 16); // LL
                if((i % 2) != 0) {
                    ByteXorTest(rk, offsetRk, rk, offsetRk, skey, offsetSkey + 16, 16); // Xoring KR
                }
                ClefiaDoubleSwapTest(lk, 0);  // updating LL
            }
            offsetRk += 16;
        }
        // final whitening key (WK2, WK3)
        ByteXorTest(rk, offsetRk, skey,offsetSkey + 8, skey,offsetSkey + 24, 8);
    }

    public static void ClefiaEncryptTest(List<Byte> ct, List<Byte> pt, List<Byte> rk, int r) {
        int offsetRk = 0;
        List<Byte> rin = new ArrayList<>(); // tam 16
        List<Byte> rout = new ArrayList<>(); // tam 16

        addSpaceToList(rout, 16, 16);
        addSpaceToList(rin, 16, 16);

        copyList(rin, 0, pt, 0, 16);

        ByteXorTest(rin, 4 , rin, 4, rk, offsetRk, 4); /* initial key whitening */
        ByteXorTest(rin, 12, rin, 12, rk,offsetRk + 4, 4);

        offsetRk += 8;

        ClefiaGfn4Test(rout, rin, rk, offsetRk, r); /* GFN_{4,r} */

        copyList(ct, 0, rout, 0, 16);

        ByteXorTest(ct,4, ct,4, rk,((r * 8) + offsetRk),4); /* final key whitening */
        ByteXorTest(ct,12, ct,12, rk,((r * 8) + offsetRk + 4),4);
    }

    public static void ClefiaDecryptTest(List<Byte> pt, List<Byte> ct, List<Byte> rk, int r) {
        int offsetRk = 0;
        List<Byte> rin = new ArrayList<>(); // tam 16
        List<Byte> rout = new ArrayList<>(); // tam 16

        addSpaceToList(rout, 16, 16);
        addSpaceToList(rin, 16, 16);

        copyList(rin, 0, ct, 0, 16);

        // initial key whitening
        ByteXorTest(rin, 4,  rin,  4, rk, (r * 8) + offsetRk +  8, 4);
        ByteXorTest(rin, 12, rin, 12, rk, (r * 8) + offsetRk + 12, 4);

        offsetRk += 8;

        ClefiaGfn4InvTest(rout, rin, rk, offsetRk, r); // GFN^{-1}_{4,r}

        copyList(pt, 0, rout, 0, 16);

        ByteXorTest(pt,  4, pt,  4, rk, offsetRk - 8, 4); // final key whitening
        ByteXorTest(pt, 12, pt, 12, rk, offsetRk - 4, 4);
    }

    private static void printList(List<Byte> list, int length) {
        while(length > 0){
            System.out.printf("%02x", list.get(list.size()-length));
            length--;
        }
        System.out.println();
    }

    private static void chargeSkey(List<Byte> skey) {
        skey.add((byte) 0xff);
        skey.add((byte) 0xee);
        skey.add((byte) 0xdd);
        skey.add((byte) 0xcc);
        skey.add((byte) 0xbb);
        skey.add((byte) 0xaa);
        skey.add((byte) 0x99);
        skey.add((byte) 0x88);
        skey.add((byte) 0x77);
        skey.add((byte) 0x66);
        skey.add((byte) 0x55);
        skey.add((byte) 0x44);
        skey.add((byte) 0x33);
        skey.add((byte) 0x22);
        skey.add((byte) 0x11);
        skey.add((byte) 0x00);

        skey.add((byte) 0xf0);
        skey.add((byte) 0xe0);
        skey.add((byte) 0xd0);
        skey.add((byte) 0xc0);
        skey.add((byte) 0xb0);
        skey.add((byte) 0xa0);
        skey.add((byte) 0x90);
        skey.add((byte) 0x80);
        skey.add((byte) 0x70);
        skey.add((byte) 0x60);
        skey.add((byte) 0x50);
        skey.add((byte) 0x40);
        skey.add((byte) 0x30);
        skey.add((byte) 0x20);
        skey.add((byte) 0x10);
        skey.add((byte) 0x00);
    }

    private static void chargePt(List<Byte> image) {
//        pt.add((byte) 0x00);
//        pt.add((byte) 0x01);
//        pt.add((byte) 0x02);
//        pt.add((byte) 0x03);
//        pt.add((byte) 0x04);
//        pt.add((byte) 0x05);
//        pt.add((byte) 0x06);
//        pt.add((byte) 0x07);
//        pt.add((byte) 0x08);
//        pt.add((byte) 0x09);
//        pt.add((byte) 0x0a);
//        pt.add((byte) 0x0b);
//        pt.add((byte) 0x0c);
//        pt.add((byte) 0x0d);
//        pt.add((byte) 0x0e);
//        pt.add((byte) 0x0f);



    }
    // convert BufferedImage to byte[]
    public static Byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        Byte[] bytes =  ArrayUtils.toObject(baos.toByteArray());
        return bytes;

    }
    // convert byte[] to BufferedImage
    public static BufferedImage toBufferedImage(byte[] bytes)
            throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }

    public static void main(String[] args) throws IOException {
        int r;
        //levantamos imagen
        BufferedImage bi = ImageIO.read(new File("src/main/resources/test.jpg"));

        // convertimos imagen a array de bytes
        Byte[] image = toByteArray(bi, "jpg");

        List<Byte> skey = new ArrayList<>();
//        List<Byte> pt = new ArrayList<>();
        List<Byte> pt = Arrays.asList(image);
        List<Byte> ct = new ArrayList<>();
        List<Byte> dst = new ArrayList<>();
        List<Byte> rk = new ArrayList<>();

        chargeSkey(skey);
        //chargePt(pt);

        addSpaceToList(dst, 16, 16);
        addSpaceToList(ct, 16, 16);
        addSpaceToList(rk, (8*26 +16), (8*26 +16));

        System.out.println("plaintext:  ");
        printList(pt, 16);
        System.out.println("secretkey:  ");
        printList(skey, 32);

        // For 128-bit key
        System.out.println("\n--- CLEFIA-128 ---\n");

        // Encryption
        r = ClefiaKeySet(rk, skey, 128);
        ClefiaEncryptTest(dst, pt, rk, r);
        System.out.println("ciphertext: ");
        printList(dst, 16);

        // Decryption
        copyList(ct, 0, dst, 0, 16);
        r = ClefiaKeySet(rk, skey, 128);
        ClefiaDecryptTest(dst, ct, rk, r);
        System.out.println("plaintext : ");
        printList(dst, 16);


        // For 192-bit key
        System.out.println("\n--- CLEFIA-192 ---\n");

        // Encryption
        r = ClefiaKeySet(rk, skey, 192);
        ClefiaEncryptTest(dst, pt, rk, r);
        System.out.println("ciphertext: ");
        printList(dst, 16);

        // Decryption
        copyList(ct, 0, dst, 0, 16);
        r = ClefiaKeySet(rk, skey, 192);
        ClefiaDecryptTest(dst, ct, rk, r);
        System.out.println("plaintext : ");
        printList(dst, 16);


        // For 256-bit key
        System.out.println("\n--- CLEFIA-256 ---\n");
        // Encryption
        r = ClefiaKeySet(rk, skey, 256);
        ClefiaEncryptTest(dst, pt, rk, r);
        System.out.println("ciphertext: ");
        printList(dst, 16);

        // Decryption
        copyList(ct, 0, dst, 0, 16);
        r = ClefiaKeySet(rk, skey, 256);
        ClefiaDecryptTest(dst, ct, rk, r);
        System.out.println("plaintext : ");
        printList(dst, 16);
    }
}
