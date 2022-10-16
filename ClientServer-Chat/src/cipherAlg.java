import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class cipherAlg {
    //viginere encryption
    //private static final String ALPHABETLETTERS="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

//   // public String encrypt(String plainText, String key){
//        plainText= plainText.toUpperCase();
//        String cipherText="";
//        int keyIndex=0;
//        for(int i =0;i<plainText.length();i++){
//            char c= plainText.charAt(i);
//            int index=Math.floorMod(ALPHABETLETTERS.indexOf(c)+
//                    ALPHABET.indexOf(key.charAt(keyIndex)),ALPHABET.length());
//
//            cipherText+=ALPHABETLETTERS.charAt(index);
//            keyIndex++;
//            if(keyIndex==key.length())
//                keyIndex=0;
//
//        }
//        return cipherText;
//    }
//
//


    private SecretKey secretPrivateKey; // the privatekey
    private SecureRandom random; // to generate random values
    private Cipher encryptionCipher;
    private Cipher decryptCipher;
    private IvParameterSpec initilizationVectorParameters;
///DES algorithm was used for this encryption
    public cipherAlg() {
        try {
            // generation of 64 bitlong key
            secretPrivateKey = KeyGenerator.getInstance("DES").generateKey();

            // generating random number

            random = new SecureRandom();
            encryptionCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //cbc relying on previous blocks
            decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            // initialization vector is generated to containing same #bytes as every single block which will
            //be used as first block to come up with the second block and then the rest of the blocks will be generated
            //previous blocks
            byte[] initializatonVector = new byte[encryptionCipher.getBlockSize()];

            random.nextBytes(initializatonVector);
            initilizationVectorParameters = new IvParameterSpec(initializatonVector);

            // same secretkey and IV is used for encryption and decryption
            encryptionCipher.init(Cipher.ENCRYPT_MODE, secretPrivateKey, initilizationVectorParameters);
            decryptCipher.init(Cipher.DECRYPT_MODE, secretPrivateKey, initilizationVectorParameters);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
       } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String plainText) {
// plainTest is transformed to byte code and then encrypted
        byte[] bytes = plainText.getBytes();
        byte[] cipherText = null;

        try {
            cipherText = encryptionCipher.doFinal(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(cipherText); /// to encode will convert the cipher tesxt tp a String
    }

    public String decrypt(String cipherText) {

        byte[] plainText = null;

        try {
            plainText = decryptCipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes()));
            return new String(plainText, "UTF8");
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }






}
