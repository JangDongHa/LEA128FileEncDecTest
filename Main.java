package kr.re.nsr.crypto;

import kr.re.nsr.crypto.padding.PKCS5Padding;
import kr.re.nsr.crypto.symm.LEA;

import java.util.ArrayList;

public class Main {
    public static BlockCipherMode cipher = new LEA.CBC();
    public static byte[] key = Seed_CBC.key;
    public static byte[] byteIV = new byte[] { (byte) 0x26, (byte) 0x8D, (byte) 0x66, (byte) 0xA7, (byte) 0x35, (byte) 0xA8, (byte) 0x1A, (byte) 0x81, (byte) 0x6F, (byte) 0xBA, (byte) 0xD9, (byte) 0xFA, (byte) 0x36, (byte) 0x16, (byte) 0x25, (byte) 0x01 };
    public static byte[] encrypt(byte[] plainBytes){
        cipher.init(BlockCipher.Mode.ENCRYPT, key, byteIV);
        cipher.setPadding(new PKCS5Padding(16));
        return cipher.doFinal(plainBytes);
    }

    public static void main(String[] args) {

        String plain = "asdfasdasdfasdasdasd";
        byte[] plainBytes = plain.getBytes();

        /* TODO
            파일 16바이트로 자르기
            복호화된 16바이트 블록 write로 입력하기
        * */

        ArrayList<byte[]> plainByteList = new ArrayList<>(); // 평문 16바이트 블록 List
        ArrayList<byte[]> cts = new ArrayList<>(); // 암호화된 16바이트 블록 List
        byte[] Byte16Block = new byte[16]; // 16바이트 블록 생성
        int len;

        if (plainBytes.length % 16 == 0) // 16바이트로 쪼개는데 나누어떨어지면 그대로, 나누어떨어지지않으면 +1
            len = plainBytes.length / 16;
        else
            len = plainBytes.length / 16 + 1;

        int plainFlag = 0;
        int Byte16BlockFlag = 0;

        for (int i = 0; i < len; i++) { // 블록 갯수만큼
            for (int j = 0; j < 16; j++) { // 16번
                if (plainFlag < plainBytes.length){ // plainBytes 길이를 넘지 않는 선에서
                    Byte16Block[Byte16BlockFlag++] = plainBytes[plainFlag++]; // 새로운 블록 생성 (16바이트 전부 차지 않아도 복호화할때 걸러짐)
                }
            }
            plainByteList.add(Byte16Block); // 16바이트 블록 평문 블록 List 에 삽입
            cts.add(encrypt(Byte16Block)); // 16바이트 블록 암호문 블록 List 에 삽입

            // 초기화
            Byte16BlockFlag = 0;
            Byte16Block = new byte[16];
        }

        // 위까지 암호화

//        System.out.println(plainBytes.length);
//        System.out.println(cts.size());

        // 아래부터 복호화
        
        ArrayList<byte[]> pt1Byte = new ArrayList<>(); // 복호화된 16바이트 블록 List
        for (int i = 0; i < len; i++) { // 블록 갯수만큼
            cipher.init(BlockCipher.Mode.DECRYPT, key, byteIV); // cipher 초기화 (LEA 는 무조건 해야하는듯)
            cipher.setPadding(new PKCS5Padding(16));
            pt1Byte.add(cipher.doFinal(cts.get(i))); // 16바이트 블록 암호문 블록 List 에서 하나씩 빼서 복호화된 블록 List 에 삽입
        }



        byte[] result = new byte[plainBytes.length]; // 결과값
        int resultIndex = 0;
        for (int i = 0; i < pt1Byte.size(); i++) { // 복호화된 16바이트 블록 List 갯수만큼
            for (int j = 0; j < 16; j++) {
                if (resultIndex < plainBytes.length) // 평문 블록 길이를 넘지 않는 선에서 (뒤에 패딩값 지워줘야하므로)
                    result[resultIndex++] = pt1Byte.get(i)[j]; // 결과값 생성
            }
        }

        System.out.println(result.length);

        System.out.println(new String(result));




    }
}
