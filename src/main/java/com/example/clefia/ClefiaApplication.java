package com.example.clefia;

import com.groupdocs.metadata.internal.a.By;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.util.Arrays;

@SpringBootApplication
public class ClefiaApplication {

    public void ByteCpy(Byte[] dst, Byte[] src) {
        dst = Arrays.copyOf(src, src.length);
    }

    //	public static void ByteShow(Byte[] dst) {
//
//		for (int i = 0; i < dst.length; i++) {
//			System.out.println("Hexa is " + String.format("%02x", dst[i]));
//		}
//		System.out.print("\n");
//	}
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

        x= (byte)((x << 1) | (x >>> 7));
        String hex = Integer.toHexString(x & 0xFF);
        return Byte.parseByte(hex,16);
    }

//    public static Byte[] ClefiaMul2(Byte[] x) {
//        int i = 0;
//        int j = 0;
//        byte[] x1 = new byte[6];
//        // Associating Byte array values with bytes. (byte[] to Byte[])
//        for (Byte b : x)
//            x1[i++] = b;  // Autoboxing.
//
//        // create from array
//        BigInteger bigInt = new BigInteger(x1);
//
//        /* multiplication over GF(2^8) (p(x) = '11d') */
//        if ((x[0] & 0x80) == 1)
//            x[0] = (byte) (x[0] ^ 0x0e);
//
//        // shift
//        BigInteger shiftInt = bigInt.shiftRight(7);
//        BigInteger shiftInt2 = bigInt.shiftLeft(1);
//
//        // back to array
//        byte[] shifted = shiftInt.toByteArray();
//        byte[] shifted2 = shiftInt2.toByteArray();
//        byte[] result = new byte[6];
//        Byte[] result2 = new Byte[6];
//
//        for (int c = 0; c < shifted.length; c++) {
//            result[c] = (byte) (shifted[c] | shifted2[c]);
//        }
//
//        // Associating Byte array values with bytes. (byte[] to Byte[])
//        for (byte b : result)
//            result2[j++] = b;  // Autoboxing.
//
//        return result2;
//    }

    public static void main(String[] args) {
        Byte[] array_1 = new Byte[]{1, 0, 1, 0, 1, 1};
        Byte[] array_2 = new Byte[]{1, 0, 0, 1, 0, 1};
        Byte[] array_3 = new Byte[6];
        byte[] array_4 = new byte[]{0x7F};
        Byte[] array_5 = new Byte[]{0x7F};

/*		System.out.println("Array 1");
		ByteShow(array_1);
		System.out.println("Array 2");
		ByteShow(array_2);
		System.out.println("Prueba XOR");
		ByteXor(array_3,array_1,array_2);
		System.out.println("Array 3");
		ByteShow(array_3);*/
        //ByteShow(ClefiaMul2(array_4));
        System.out.println(ClefiaMul2(array_5[0]));
        //ByteShow(array_5);

    }
}
