package org.aztec.autumn.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.aztec.autumn.common.utils.MathUtils;
import org.aztec.autumn.common.utils.security.rsa.RSAEncrypt;
import org.aztec.autumn.common.utils.security.rsa.RSASignature;
import org.aztec.autumn.common.zk.ZkConfig;
import org.aztec.autumn.common.zk.ZkConfig.ConfigFormat;
import org.glassfish.grizzly.http.util.Base64Utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    private static List<String> testDataList = new ArrayList<>();
    
    public static void main(String[] args) {
    	try {
			/*Integer testTime = Integer.parseInt(args[0]);
			Long interval = Long.parseLong(args[1]);
			
			for(int i = 0;i < testTime;i ++) {
				testDataList.add("Test String---" + i);
				//System.out.println(testDataList.get(i));
				Thread.currentThread().sleep(interval);
			}*/
    		//testCmain(args);
    		/*testSecureRandom();
    		testSecureRandom();
    		testSecureRandom();
    		testSecureRandom();*/
    		//genKey(2, 10);
    		//System.out.println(StringUtils.getRamdonNumberString(100).hashCode());
    		//System.out.println(StringUtils.getRamdonNumberString(10).hashCode());
    		//System.out.println(StringUtils.getRamdonNumberString(20).hashCode());
    		//testMultiRSA();
    		//testZkNode();
    		//testEncryptSign();
    		testMultiRSA();
    		//System.out.println(findMaxDiff());
    		//testSign();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
    public static void testMultiRSA() {
    	
    	
    	try {
			int maxRound = 20,keyPairNum = 20;
			
			List<String[]> keypairs = new ArrayList<>();
			SecureRandom sRandom = new SecureRandom();
			Random random = new Random();
			List<String> historyPubKeys = new ArrayList<>();
			for(int i = 0;i < keyPairNum;i++) {
				keypairs.add(RSAEncrypt.genKeyPair(null, sRandom));
				
			}
			/*MultiRSAKeyGenerator generator = new MultiRSAKeyGenerator(keypairs);
			long roundStartTime = System.currentTimeMillis();
			long[] allGenKeyTimes = new long[maxRound];
			long[] allValidTimes = new long[maxRound];
			for(int i = 0;i < maxRound;i++) {
				System.out.println("####################  ROUND " + i + " #####################");
				long beginTime = System.currentTimeMillis();
				String[] keyPairs = generator.generate(null);
				allGenKeyTimes[i] = System.currentTimeMillis() - beginTime;
				generator.confirm(keyPairs[RSAEncrypt.PUBLIC_KEY_INDEX]);
				historyPubKeys.add(keyPairs[RSAEncrypt.PUBLIC_KEY_INDEX]);
				String text = "I am gensis coin!";
				System.out.println("secret key:" + keyPairs[RSAEncrypt.SECRET_KEY_INDEX]);
				System.out.println("public key:" + keyPairs[RSAEncrypt.PUBLIC_KEY_INDEX]);
				System.out.println("origin text:" + text);
				int randomChoice = random.nextInt(i + 1);
				beginTime = System.currentTimeMillis();
				SpecialSignatureResult signResult = generator.getSpecialSignature(text, randomChoice);
				boolean verifyResult = generator.isSpecialSignatureValid(text,
						new String[]{signResult.getOriginSignature(),signResult.getSignature()},
						new String[] {keypairs.get(signResult.getInitIndex())[RSAEncrypt.PUBLIC_KEY_INDEX], historyPubKeys.get(randomChoice)});
				System.out.println(verifyResult);
				if(!verifyResult) {
					System.err.println("verify fail:randomChoice-" + randomChoice + ",beginTime:" + beginTime);
					break;
				}
				allValidTimes[i] = System.currentTimeMillis() - beginTime;
				System.out.println("####################  ROUND " + i + " END  ################");
			}
			long avgGenTime = 0;
			long avgValidTime = 0;
			for(int i = 0 ;i < maxRound;i++) {
				avgGenTime += allGenKeyTimes[i];
				avgValidTime += allValidTimes[i];
			}
			avgGenTime = avgGenTime/maxRound;
			avgValidTime = avgValidTime / maxRound;
			System.out.println("############ AVAGE GEN TIME :" + avgGenTime);
			System.out.println("############ AVAGE VALID TIME:" + avgValidTime);
			long allUseTime = System.currentTimeMillis() - roundStartTime;
			System.out.println("####### TOTAL USE :" + allUseTime + "");*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    public static double standardDiviation(int randNum) {
    	Random randon = new Random();
    	//Math.
    	//String[] nonStrs = random
    	return 0d;
    }
    
    public static void genKey(int startIndex,int keyNum) {
    	for(int i = startIndex; i < keyNum ;i++) {
    		String filepath="E:\\lm\\tmp\\key" + i;
    		File pathDir = new File(filepath);
    		if(!pathDir.exists())pathDir.mkdirs();
            RSAEncrypt.genKeyPair(filepath,new SecureRandom()); 
    	}
    }
    
    
    public static void testCmain(String[] args) throws Exception {  
        String filepath="E:\\lm\\tmp";  
  
        
        String targetFilePath = "E:\\lm\\tmp";
        RSAEncrypt.genKeyPair(filepath,null);  
          
          
        System.out.println("--------------公钥加密私钥解密过程-------------------");  
        String plainText="ihep_公钥加密私钥解密";  
        //公钥加密过程  
        byte[] cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)),plainText.getBytes());  
        String cipher=Base64Utils.encodeToString(cipherData,true);  
        //私钥解密过程  
        byte[] res=RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)), Base64Utils.decode(cipher));  
        String restr=new String(res);  
        System.out.println("原文："+plainText);  
        System.out.println("加密："+cipher);  
        System.out.println("解密："+restr);  
        System.out.println();  
        
        System.out.println("--------------公钥加密公钥解密过程-------------------");  
        /*String plainText2="ihep_公钥加密私钥解密";  
        //公钥加密过程  
        byte[] cipherData2=RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)),plainText.getBytes());  
        String cipher2=Base64Utils.encodeToString(cipherData,true);  
        //私钥解密过程  
        byte[] res2=RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)), Base64Utils.decode(cipher));  
        String restr2=new String(res);  
        System.out.println("原文："+plainText2);  
        System.out.println("加密："+cipher2);  
        System.out.println("解密："+restr2);  
        System.out.println();*/
          
        System.out.println("--------------私钥加密公钥解密过程-------------------");  
        plainText="ihep_私钥加密公钥解密";  
        //私钥加密过程  
        cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)),plainText.getBytes());  
        cipher=Base64Utils.encodeToString(cipherData,true);  
        //公钥解密过程  
        res=RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)), Base64Utils.decode(cipher));  
        restr=new String(res);  
        System.out.println("原文："+plainText);  
        System.out.println("加密："+cipher);  
        System.out.println("解密："+restr);  
        System.out.println();  
          
        System.out.println("---------------私钥签名过程------------------");  
        String content="ihep_这是用于签名的原始数据";  
        String signstr=RSASignature.sign(content,RSAEncrypt.loadPrivateKeyByFile(filepath));  
        System.out.println("签名原串："+content);  
        System.out.println("签名串："+signstr);  
        System.out.println();  
          
        System.out.println("---------------公钥校验签名------------------");  
        System.out.println("签名原串："+content);  
        System.out.println("签名串："+signstr);  
          
        System.out.println("验签结果："+RSASignature.doCheck(content, signstr, RSAEncrypt.loadPublicKeyByFile(filepath)));  
        System.out.println();  
          
    }
    
    public static void testSecureRandom() {
    	String text =  "textStr";
    	SecureRandom random = new SecureRandom();
    	random.setSeed(text.getBytes());
    	System.out.println("r1:" + random.nextInt());
    	System.out.println("r2:" + random.nextInt());
    	System.out.println("r3:" + random.nextInt());
    }
    
    public static void testZkNode() {
    	try {
			
    		ZkConfig config = new ZkConfig("test.lm",ConfigFormat.TEXT);
    		ZkConfig config2 = new ZkConfig("test.lm2",ConfigFormat.TEXT);
			System.out.println(config.getDataStr());
			System.out.println(config2.getDataStr());
			System.out.println(config.getDataStrWithVersion(1));
			//config.rewrite();
			Scanner scaner = new Scanner(System.in);
			scaner.nextLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void testEncryptSign() throws Exception {
    	String rawString = "liming is honest";
    	rawString = "" + rawString.hashCode();
    	String pk1 = RSAEncrypt.loadPrivateKeyByFile("test/key_0.key");
    	String pbk1 = RSAEncrypt.loadPublicKeyByFile("test/key_0.pub");
    	String pk2 = RSAEncrypt.loadPrivateKeyByFile("test/key_1.key");
    	String pbk2 = RSAEncrypt.loadPublicKeyByFile("test/key_1.pub");
    	String encryData = new String(RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(pbk1), rawString.getBytes(GlobalConst.DEFAULT_CHARSET)),GlobalConst.DEFAULT_CHARSET);
    	System.out.println(encryData);
    	String signature = RSASignature.sign(encryData, pk2);
    	boolean checkResult = RSASignature.doCheck(encryData, signature, pbk2);
    	System.out.println(checkResult);
    }
    
    public static Integer findMaxDiff() {
    	List<Integer> maxDiffFactory = MathUtils.findPrimeFactor(100);
    	Integer maxDiff = 1;
    	Integer testNum = 1;
    	for(int i = 0;i < maxDiffFactory.size();i++) {
    		Integer primeNum = maxDiffFactory.get(i);
    		Integer upperTime = Integer.MAX_VALUE / primeNum;
    		if(upperTime < testNum) {
    			return i;
    		}
    		testNum *= primeNum;
    	}
    	return maxDiffFactory.size();
    }
    
    
    public static void testSign() {
    	String content = "I am gensis coin!";
    	String signature = "QnV08YhzNBwFq9AhcoOA840PqEEv5EsZ6xvzQipjL0GxRTu6NIv6Bbazf41yRV4FbyKw8M9FShulIYkpe8C51RaI9OUIfaCWdz1FR51SL6gx7G2k3BkOR5OgLJ1W/X+CkaKBV1n2lCYK0gyyobz5CQ7/783gvAcWTzVBMReIM0E=";
    	String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFQTl/YM21k+/o4ypa2rPoAgMOLcyjgoUiwHcLgepXuEWWECBkjyTJku/Uwh0UICeOP6hB+3PZeQWlxjiP4bbBJSO5ug6aY8kq+1cr2OIuLM10wly8Gk1A9Usw0ODWlUNgS75z/OpYElXsfWiRaPsxzqfcpRb2cu/33PCTnf7v7wIDAQAB";
    	boolean verifyRes = RSASignature.doCheck(content, signature, pubKey);
    	System.out.println(verifyRes);
    }
}
