package com.example.clefia;

import com.groupdocs.metadata.internal.a.By;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.util.Arrays;

@SpringBootApplication
public class ClefiaApplication {

    /* S0 (8-bit S-box based on four 4-bit S-boxes) */
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

    /* S1 (8-bit S-box based on inverse function) */
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

    public static void ByteCpy(Byte[] dst, Byte[] src) {
        dst = Arrays.copyOf(src, src.length);
    }

    public static void ByteShow(Byte[] dst) {
        for (int i = 0; i < dst.length; i++) {
            System.out.println("Hexa is " + String.format("%02x", dst[i]));
        }
        System.out.print("\n");
    }

    public static void ByteXor(Byte[] dst, Byte[] a, Byte[] b) {
        if (a.length > b.length) {
            throw new IllegalArgumentException("El operador1 es de longitud mayor que el operador2");
        }
        for (int cont = 0; cont < a.length; cont++) {
            dst[cont] = (byte) (a[cont] ^ b[cont]);
        }

    }

    public static Byte ClefiaMul2(Byte x) {
        int i = 0;
        int j = 0;

        /* multiplication over GF(2^8) (p(x) = '11d') */
        if ((x & 0x80) == 1)
            x = (byte) (x ^ 0x0e);

        String hex = Integer.toHexString(x & 0xFF);
        return (byte) (Byte.parseByte(hex, 16) & Byte.parseByte(hex, 16));

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

    public static void ClefiaF0Xor(Byte[] dst, Byte[] src, Byte[] rk) {
        int[] x = new int[4], y = new int[4], z = new int[4];

        /* F0 */
        /* Key addition */
        ByteXor(getBytes(x), src, rk);
        /* Substitution layer */
        z[0] = clefia_s0[x[0]];
        z[1] = clefia_s1[x[1]];
        z[2] = clefia_s0[x[2]];
        z[3] = clefia_s1[x[3]];
        /* Diffusion layer (M0) */
        y[0] = z[0] ^ ClefiaMul2((byte) z[1]) ^ ClefiaMul4((byte) z[2]) ^ ClefiaMul6((byte) z[3]);
        y[1] = ClefiaMul2((byte) z[0]) ^ z[1] ^ ClefiaMul6((byte) z[2]) ^ ClefiaMul4((byte) z[3]);
        y[2] = ClefiaMul4((byte) z[0]) ^ ClefiaMul6((byte) z[1]) ^ z[2] ^ ClefiaMul2((byte) z[3]);
        y[3] = ClefiaMul6((byte) z[0]) ^ ClefiaMul4((byte) z[1]) ^ ClefiaMul2((byte) z[2]) ^ z[3];

        /* Xoring after F0 */
        ByteCpy(dst, src);

        ByteXor(getSubArray(dst, 4), getSubArray(src, 4), getBytes(y));
    }

    private static Byte[] getSubArray(Byte[] dst, int indice) {
        Byte[] array = new Byte[dst.length];

        if (indice > 0) {
            for (int i = indice; i < dst.length; i++) {
                array[i - indice] = dst[i];
            }
        }

        return array;
    }

    public static void ClefiaF1Xor(Byte[] dst, Byte[] src, Byte[] rk) {
        int[] x = new int[4], y = new int[4], z = new int[4];

        /* F0 */
        /* Key addition */
        ByteXor(getBytes(x), src, rk);
        /* Substitution layer */
        z[0] = clefia_s0[x[0]];
        z[1] = clefia_s1[x[1]];
        z[2] = clefia_s0[x[2]];
        z[3] = clefia_s1[x[3]];

        /* Diffusion layer (M1) */
        y[0] = z[0] ^ ClefiaMul8((byte) z[1]) ^ ClefiaMul2((byte) z[2]) ^ ClefiaMulA((byte) z[3]);
        y[1] = ClefiaMul8((byte) z[0]) ^ z[1] ^ ClefiaMulA((byte) z[2]) ^ ClefiaMul2((byte) z[3]);
        y[2] = ClefiaMul2((byte) z[0]) ^ ClefiaMulA((byte) z[1]) ^ z[2] ^ ClefiaMul8((byte) z[3]);
        y[3] = ClefiaMulA((byte) z[0]) ^ ClefiaMul2((byte) z[1]) ^ ClefiaMul8((byte) z[2]) ^ z[3];

        /* Xoring after F1 */
        ByteCpy(dst, src);
        ByteXor(getSubArray(dst, 4), getSubArray(src, 4), getBytes(y));
    }

    public static Byte[] getBytes(int[] y) {
        Byte[] array = new Byte[y.length];
        for (int i = 0; i < y.length; i++) {
            array[i] = (byte) y[i];
        }
        return array;
    }

    public static void ClefiaGfn4(Byte[] y, Byte[] x, Byte[] rk, int r) {
        int[] fin = new int[16], fout = new int[16];


        ByteCpy(getBytes(fin), x);
        while (r-- > 0) {
            ClefiaF0Xor(getBytes(fout), getBytes(fin), rk);
            ClefiaF1Xor(getSubArray(getBytes(fout), 8), getSubArray(getBytes(fin), 8), getSubArray(rk, 4));
            //se hace corrimiento sobre array rk, chequear donde se usa para ver cuando se hace
            getSubArray(rk, 8);

            if (r == 1) { /* swapping for encryption */
                ByteCpy(getBytes(fin), getSubArray(getBytes(fout), 4));
                ByteCpy(getSubArray(getBytes(fin), 12), getBytes(fout));
            }
        }
        ByteCpy(y, getSubArray(getBytes(fout), 16));
    }

    public void ClefiaGfn8(Byte[] y, Byte[] x, Byte[] rk, int r) {
        int[] fin = new int[32], fout = new int[32];


        ByteCpy(getBytes(fin), x);
        while (r-- > 0) {
            ClefiaF0Xor(getBytes(fout), getBytes(fin), rk);
            ClefiaF1Xor(getSubArray(getBytes(fout), 8), getSubArray(getBytes(fin), 8), getSubArray(rk, 4));
            ClefiaF0Xor(getSubArray(getBytes(fout), 16), getSubArray(getBytes(fin), 16), getSubArray(rk, 8));
            ClefiaF1Xor(getSubArray(getBytes(fout), 16), getSubArray(getBytes(fin), 24), getSubArray(rk, 12));
            //se hace corrimiento sobre array rk, chequear donde se usa para ver cuando se hace
            getSubArray(rk, 16);
            if (r != 0) { /* swapping for encryption */
                ByteCpy(getBytes(fin), getSubArray(getBytes(fout), 4));
                ByteCpy(getSubArray(getBytes(fin), 28), getBytes(fout));
            }
        }
        ByteCpy(y, getBytes(fout));
    }

    static int ClefiaKeySet(Byte[] rk, Byte[] skey, int key_bitlen)
    {
        if(128 == key_bitlen){
            ClefiaKeySet128(rk, skey);
            return 18;
        }else if(192 == key_bitlen){
            ClefiaKeySet192(rk, skey);
            return 22;
        }else if(256 == key_bitlen){
            ClefiaKeySet256(rk, skey);
            return 26;
        }

        return 0; /* invalid key_bitlen */
    }

    public static void ClefiaGfn4Inv(Byte[] y, Byte[] x, Byte[] rk, int r) {
        int[] fin = new int[16], fout = new int[16];
        getSubArray(rk, (r - 1) * 8);
        ByteCpy(getBytes(fin), x);
        while (r-- > 0) {
            ClefiaF0Xor(getBytes(fout), getBytes(fin), rk);
            ClefiaF1Xor(getSubArray(getBytes(fout), 8), getSubArray(getBytes(fin), 8), getSubArray(rk, 4));
            //se hace corrimiento sobre array rk, chequear donde se usa para ver cuando se hace
            getSubArray(rk, 8);

            if (r == 1) { /* swapping for encryption */
                ByteCpy(getBytes(fin), getSubArray(getBytes(fout), 4));
                ByteCpy(getSubArray(getBytes(fin), 12), getBytes(fout));
            }
        }
        ByteCpy(y, getSubArray(getBytes(fout), 16));
    }

    public static void ClefiaConSet(Byte[] con, Byte[] iv, int lk) {
        int[] t = new int[2];
        int tmp;

        ByteCpy(getBytes(t), iv);
        while (lk-- > 0) {
            con[0] = (byte) (t[0] ^ 0xb7); /* P_16 = 0xb7e1 (natural logarithm) */
            con[1] = (byte) (t[1] ^ 0xe1);
            con[2] = (byte) (~((t[0] << 1) | (t[1] >> 7)));
            con[3] = (byte) (~((t[1] << 1) | (t[0] >> 7)));
            con[4] = (byte) (~t[0] ^ 0x24); /* Q_16 = 0x243f (circle ratio) */
            con[5] = (byte) (~t[1] ^ 0x3f);
            con[6] = (byte) (t[1]);
            con[7] = (byte) (t[0]);
            con = getSubArray(con, 8);

            /* updating T */
            if ((t[1] & 0x01) != 0) {
                t[0] ^= 0xa8;
                t[1] ^= 0x30;
            }
            tmp = t[0] << 7;
            t[0] = (t[0] >> 1) | (t[1] << 7);
            t[1] = (t[1] >> 1) | tmp;
        }
    }

    public static void ClefiaDoubleSwap(Byte[] lk) {
        int[] t = new int[16];

        t[0] = (lk[0] << 7) | (lk[1] >>> 1);
        t[1] = (lk[1] << 7) | (lk[2] >>> 1);
        t[2] = (lk[2] << 7) | (lk[3] >>> 1);
        t[3] = (lk[3] << 7) | (lk[4] >>> 1);
        t[4] = (lk[4] << 7) | (lk[5] >>> 1);
        t[5] = (lk[5] << 7) | (lk[6] >>> 1);
        t[6] = (lk[6] << 7) | (lk[7] >>> 1);
        t[7] = (lk[7] << 7) | (lk[15] & 0x7f);

        t[8] = (lk[8] >>> 7) | (lk[0] & 0xfe);
        t[9] = (lk[9] >>> 7) | (lk[8] << 1);
        t[10] = (lk[10] >>> 7) | (lk[9] << 1);
        t[11] = (lk[11] >>> 7) | (lk[10] << 1);
        t[12] = (lk[12] >> 7) | (lk[11] << 1);
        t[13] = (lk[13] >>> 7) | (lk[12] << 1);
        t[14] = (lk[14] >>> 7) | (lk[13] << 1);
        t[15] = (lk[15] >>> 7) | (lk[14] << 1);

        ByteCpy(lk, getBytes(t));
    }

    public static void ClefiaKeySet128(Byte[] rk, Byte[] skey) {
        int[] iv = {0x42, 0x8a}; /* cubic root of 2 */
        int[] lk = new int[16];
        int[] con128 = new int[4 * 60];
        int i;

        /* generating CONi^(128) (0 <= i < 60, lk = 30) */
        ClefiaConSet(getBytes(con128), getBytes(iv), 30);
        /* GFN_{4,12} (generating L from K) */
        ClefiaGfn4(getBytes(lk), skey, getBytes(con128), 12);

        ByteCpy(rk, skey); /* initial whitening key (WK0, WK1) */
        rk = getSubArray(rk, 8);
        for (i = 0; i < 9; i++) { /* round key (RKi (0 <= i < 36)) */
            ByteXor(rk, getBytes(lk), getSubArray(getBytes(con128), i * 16 + (4 * 24)));
            if ((i % 2) != 0) {
                ByteXor(rk, rk, skey); /* Xoring K */
            }
            ClefiaDoubleSwap(getBytes(lk)); /* Updating L (DoubleSwap function) */
            rk = getSubArray(rk, 16);
        }
        ByteCpy(rk, getSubArray(skey, 8)); /* final whitening key (WK2, WK3) */
    }

    public void ClefiaKeySet192(Byte[] rk, Byte[] skey) {
        int[] iv = {0x71, 0x37}; /* cubic root of 2 */
        int[] skey256 = new int[32];
        int[] lk = new int[32];
        int[] con192 = new int[4 * 84];
        int i;

        ByteCpy(getBytes(skey256), skey);
        for (i = 0; i < 8; i++) {
            skey256[i + 24] = ~skey[i];
        }

        /* generating CONi^(192) (0 <= i < 84, lk = 42) */
        ClefiaConSet(getBytes(con192), getBytes(iv), 42);
        /* GFN_{8,10} (generating L from K) */
        ClefiaGfn8(getBytes(lk), getBytes(skey256), getBytes(con192), 10);

        ByteXor(rk, getBytes(skey256), getSubArray(getBytes(skey256), 16)); /* initial whitening key (WK0, WK1) */
        getSubArray(rk, 8);
        for (i = 0; i < 11; i++) { /* round key (RKi (0 <= i < 44)) */
            if (((i / 2) % 2) != 0) {
                ByteXor(rk, getSubArray(getBytes(lk), 16), getSubArray(getBytes(con192), i * 16 + (4 * 40))); /* LR */
                if ((i % 2) != 0) {
                    ByteXor(rk, rk, getBytes(skey256)); /* Xoring KL */
                }
                ClefiaDoubleSwap(getSubArray(getBytes(lk), 16)); /* updating LR */
            } else {
                ByteXor(rk, getBytes(lk), getSubArray(getBytes(con192), i * 16 + (4 * 40))); /* LL */
                if ((i % 2)!=0) {
                    ByteXor(rk, rk, getSubArray(getBytes(skey256), 16)); /* Xoring KR */
                }
                ClefiaDoubleSwap(getBytes(lk));  /* updating LL */
            }
            rk = getSubArray(rk,16);
        }
        ByteXor(rk,getSubArray(getBytes(skey256),8), getSubArray(getBytes(skey256),24)); /* final whitening key (WK2, WK3) */

    }

    public void ClefiaKeySet256(Byte[] rk, Byte[] skey) {
        int[] iv = {0xb5, 0xc0}; /* cubic root of 5 */
        int[] lk = new int[32];
        int[] con256 = new int[4 * 92];
        int i;

        /* generating CONi^(256) (0 <= i < 92, lk = 46) */
        ClefiaConSet(getBytes(con256), getBytes(iv), 46);
        /* GFN_{8,10} (generating L from K) */
        ClefiaGfn8(getBytes(lk), skey, getBytes(con256), 10);

        ByteXor(rk, skey, getSubArray(skey, 16)); /* initial whitening key (WK0, WK1) */
        rk = getSubArray(rk,8);
        for(i = 0; i < 13; i++){ /* round key (RKi (0 <= i < 52)) */
            if(((i / 2) % 2)!=0)
            {
                ByteXor(rk, getSubArray(getBytes(lk), 16), getSubArray(getBytes(con256), i * 16 + (4 * 40))); /* LR */
                if((i % 2)!=0){
                    ByteXor(rk, rk, skey); /* Xoring KL */
                }
                ClefiaDoubleSwap(getSubArray(getBytes(lk) ,16)); /* updating LR */
            }else{
                ByteXor(rk, getBytes(lk),  getSubArray(getBytes(con256), i * 16 + (4 * 40))); /* LL */
                if((i % 2)!=0){
                    ByteXor(rk, rk, getSubArray(skey, 16)); /* Xoring KR */
                }
                ClefiaDoubleSwap(getBytes(lk));  /* updating LL */
            }
            rk = getSubArray(rk,16);
        }
        ByteXor(rk, getSubArray(skey,8), getSubArray(skey,24)); /* final whitening key (WK2, WK3) */
    }


    public static void ClefiaEncrypt(Byte[] ct, Byte[] pt, Byte[] rk, int r)
    {
        int[] rin = new int[16], rout = new int[16];

        ByteCpy(getBytes(rin),  pt);

        ByteXor(getSubArray(getBytes(rin), 4),  getSubArray(getBytes(rin), 4), getSubArray(rk,0)); /* initial key whitening */
        ByteXor(getSubArray(getBytes(rin), 12), getSubArray(getBytes(rin), 12), getSubArray(rk, 4));
        rk = getSubArray(rk, 8);

        ClefiaGfn4(getBytes(rout), getBytes(rin), rk, r); /* GFN_{4,r} */

        ByteCpy(ct, getBytes(rout));
        ByteXor(getSubArray(ct, 4),  getSubArray(ct, 4),  getSubArray(rk, r * 8 + 0)); /* final key whitening */
        ByteXor(getSubArray(ct, 12), getSubArray(ct, 12), getSubArray(rk, r * 8 + 4));
    }

    public static void ClefiaDecrypt(Byte[] pt, Byte[] ct, Byte[] rk, int r)
    {
        int[] rin = new int[16], rout = new int[16];

        ByteCpy(getBytes(rin), ct);

        ByteXor(getSubArray(getBytes(rin), 4),  getSubArray(getBytes(rin), 4),  getSubArray(rk, r * 8 + 8)); /* initial key whitening */
        ByteXor(getSubArray(getBytes(rin), 12), getSubArray(getBytes(rin), 12), getSubArray(rk, r * 8 + 12));
        rk = getSubArray(rk,8);

        ClefiaGfn4Inv(getBytes(rout), getBytes(rin), rk, r); /* GFN^{-1}_{4,r} */

        ByteCpy(pt, getBytes(rout));
        ByteXor(getSubArray(pt, 4),  getSubArray(pt, 4),  getSubArray(rk , - 8)); /* final key whitening */ // SE LE PASA UN NEGATIVO ??(?)
        ByteXor(getSubArray(pt, 12), getSubArray(pt, 12), getSubArray(rk, - 4)); // Lo mismo que arriba
    }

    static void BytePut(Byte[] data, int bytelen)
    {
        while(bytelen-- > 0){
            System.out.printf("%02x", data = getSubArray(data, 1));
        }
        System.out.printf("\n");
    }

    public static void main(String[] args) {
        Byte[] array_1 = new Byte[]{1, 0, 1, 0, 1, 1};
        Byte[] array_2 = new Byte[]{1, 0, 0, 1, 0, 1};
        Byte[] array_3 = new Byte[6];
        byte[] array_4 = new byte[]{0x7F};
        Byte[] array_5 = new Byte[]{0x7F};

      int[] skey = {
        0xff,0xee,0xdd,0xcc,0xbb,0xaa,0x99,0x88,
                0x77,0x66,0x55,0x44,0x33,0x22,0x11,0x00,
                0xf0,0xe0,0xd0,0xc0,0xb0,0xa0,0x90,0x80,
                0x70,0x60,0x50,0x40,0x30,0x20,0x10,0x00};

        int[] pt = {
        0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,
                0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f};

/*		System.out.println("Array 1");
		ByteShow(array_1);
		System.out.println("Array 2");
		ByteShow(array_2);
		System.out.println("Prueba XOR");
		ByteXor(array_3,array_1,array_2);
		System.out.println("Array 3");
		ByteShow(array_3);*/
        //ByteShow(ClefiaMul2(array_4));
        //System.out.println(ClefiaMul2(array_5[0]));
        //ByteShow(array_5);

        Byte[] ct = new Byte[16];
        Byte[] dst = new Byte[16];
        Byte[] rk = new Byte[8 * 26 + 16]; /* 8 bytes x 26 rounds(max) + whitening keys */
        int r;

        System.out.println("plaintext:  "); BytePut(getBytes(pt), 16);
        System.out.println("secretkey:  "); BytePut(getBytes(skey), 32);

        /* for 128-bit key */
        System.out.println("--- CLEFIA-128 ---\n");
        /* encryption */
        r = ClefiaKeySet(rk, getBytes(skey), 128);
        ClefiaEncrypt(dst, pt, rk, r);
        System.out.println("ciphertext: "); BytePut(dst, 16);
        /* decryption */
        ByteCpy(ct, dst);
        r = ClefiaKeySet(rk, getBytes(skey), 128);
        ClefiaDecrypt(dst, ct, rk, r);
        System.out.println("plaintext : "); BytePut(dst, 16);

        /* for 192-bit key */
        System.out.println("--- CLEFIA-192 ---\n");
        /* encryption */
        r = ClefiaKeySet(rk, getBytes(skey), 192);
        ClefiaEncrypt(dst, getBytes(pt), rk, r);
        System.out.println("ciphertext: "); BytePut(dst, 16);
        /* decryption */
        ByteCpy(ct, dst);
        r = ClefiaKeySet(rk, getBytes(skey), 192);
        ClefiaDecrypt(dst, ct, rk, r);
        System.out.println("plaintext : "); BytePut(dst, 16);

        /* for 256-bit key */
        System.out.println("--- CLEFIA-256 ---\n");
        /* encryption */
        r = ClefiaKeySet(rk, getBytes(skey), 256);
        ClefiaEncrypt(dst, getBytes(pt), rk, r);
        System.out.println("ciphertext: "); BytePut(dst, 16);
        /* decryption */
        ByteCpy(ct, dst);
        r = ClefiaKeySet(rk, getBytes(skey), 256);
        ClefiaDecrypt(dst, ct, rk, r);
        System.out.println("plaintext : "); BytePut(dst, 16);

    }
}
