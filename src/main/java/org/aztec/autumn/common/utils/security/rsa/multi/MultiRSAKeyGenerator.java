package org.aztec.autumn.common.utils.security.rsa.multi;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.security.rsa.RSAEncrypt;
import org.aztec.autumn.common.utils.security.rsa.RSASignature;


public class MultiRSAKeyGenerator {

	/**
	 * 初始化密钥对
	 */
	private static List<String[]> keyPairs = new ArrayList<>();
	/**
	 * 历史生成信息
	 */
	private static List<KeyGenHistory> historys = new ArrayList<>();
	/**
	 * 密钥缓存
	 */
	private static Map<Integer,String[]> keysCache = new ConcurrentHashMap<>();
	private static Map<String,KeyGenHistory> roundTempData = new ConcurrentHashMap<>();
	private final static int MAX_STEP_NUM = 100; 
	private static AtomicInteger round = new AtomicInteger(0);
	private Random random;
	
	public MultiRSAKeyGenerator(List<String[]> keyPairsParam) {
		initialize(keyPairsParam);
	}
	
	private void initialize(List<String[]> keyPairsParam) {
		if(keyPairs.size() > 0)
			return ;
		keyPairs.addAll(keyPairsParam);
		random = new Random(keyPairs.get(0)[RSAEncrypt.SECRET_KEY_INDEX].getBytes()[0]);
	}
	
	public MultiRSAKeyGenerator(List<String[]> keyPairsParam,List<KeyGenHistory> histories) {
		if(!histories.isEmpty())
			return ;
		for(int i = 0;i < histories.size();i++) {
			historys.add(histories.get(i));
			round.incrementAndGet();
		}
		initialize(keyPairsParam);
	}
	
	public String[] generate(String fileName) {
		int keyIndex = 0,randomStep = 0;
		synchronized (random) {
			keyIndex = random.nextInt(keyPairs.size());
			randomStep = random.nextInt(MAX_STEP_NUM);
		}
		return generate(fileName, keyIndex, randomStep);
	}
	
	public String[] generate(String fileName ,int keyIndex, int randomStep) {
		
		SecureRandom secRand = new SecureRandom(keyPairs.get(keyIndex)[RSAEncrypt.SECRET_KEY_INDEX].getBytes());
		for(int i = 0;i < randomStep;i++)
			secRand.nextInt();
		KeyGenHistory genHistory = new KeyGenHistory(round.get(), keyIndex, randomStep);
		String[] newKeyPair = RSAEncrypt.genKeyPair(fileName + "_" + round.get(), secRand);
		roundTempData.put(newKeyPair[RSAEncrypt.PUBLIC_KEY_INDEX], genHistory);
		return newKeyPair;
	}
	
	public String[] genHistoryKey(KeyGenHistory history) {
		SecureRandom secRand = new SecureRandom(keyPairs.get(history.getChooseIndex())[RSAEncrypt.SECRET_KEY_INDEX].getBytes());
		for(int i = 0;i < history.getRandomStep();i++)
			secRand.nextInt();
		String[] newKeyPair = RSAEncrypt.genKeyPair(null, secRand);
		return newKeyPair;
	}
	
	public synchronized void confirm(String pubKey) {
		KeyGenHistory history = roundTempData.get(pubKey);
		if(history == null)
			throw new IllegalArgumentException("history data is missing!");
		int roundVar = round.getAndIncrement();
		historys.add(history);
		roundTempData.clear();
		keysCache.put(roundVar, genHistoryKey(history));
	}
	
	
	public SpecialSignatureResult getSpecialSignature(String text,int chooseIndex) throws UnsupportedEncodingException, Exception {
		
		KeyGenHistory history = historys.get(chooseIndex);
		String signKey = keyPairs.get(history.getChooseIndex())[RSAEncrypt.SECRET_KEY_INDEX],encryptKey;
		if(keysCache.containsKey(chooseIndex)) {
			encryptKey = keysCache.get(chooseIndex)[RSAEncrypt.SECRET_KEY_INDEX];
		} else {
			encryptKey = genHistoryKey(history)[RSAEncrypt.SECRET_KEY_INDEX];
		}
		String originSign = RSASignature.sign(text, signKey,GlobalConst.DEFAULT_CHARSET);
		String tranSign = RSASignature.sign(text, encryptKey,GlobalConst.DEFAULT_CHARSET);
		return new SpecialSignatureResult(tranSign,originSign, history.getChooseIndex());
	}
	
	public boolean isSpecialSignatureValid(String content,String[] signature,String[] publicKeys) throws UnsupportedEncodingException, Exception {
		
		return RSASignature.doCheck(content, signature[0], publicKeys[0]) 
				&& RSASignature.doCheck(content, signature[1], publicKeys[1]);
	}
	
	public KeyGenHistory getHistory(Integer index){
		return historys.get(index);
	}
	
	public KeyGenHistory getRecentHistory() {
		return historys.get(round.get());
	}
	
	public List<KeyGenHistory> getAllHistorires(){
		return historys;
	}
	
	
	public static class KeyGenHistory{
		
		private int index;
		private int chooseIndex;
		private int randomStep;
		public int getChooseIndex() {
			return chooseIndex;
		}
		public void setChooseIndex(int index) {
			this.chooseIndex = index;
		}
		public int getRandomStep() {
			return randomStep;
		}
		public void setRandomStep(int randomStep) {
			this.randomStep = randomStep;
		}
		
		public int getIndex() {
			return index;
		}
		public void setIndex(int blockNo) {
			this.index = blockNo;
		}
		public KeyGenHistory(int index, int chooseIndex, int randomStep) {
			super();
			this.index = index;
			this.chooseIndex = chooseIndex;
			this.randomStep = randomStep;
		}
		public KeyGenHistory() {
			super();
		}
		
	}
	
	public static class SpecialSignatureResult{
		private String signature;
		private String originSignature;
		private int initIndex;
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		public int getInitIndex() {
			return initIndex;
		}
		public void setInitIndex(int initIndex) {
			this.initIndex = initIndex;
		}
		public String getOriginSignature() {
			return originSignature;
		}
		public void setOriginSignature(String originSignature) {
			this.originSignature = originSignature;
		}
		public SpecialSignatureResult(String signature,String oriSignature, int initIndex) {
			super();
			this.originSignature = oriSignature;
			this.signature = signature;
			this.initIndex = initIndex;
		}
		
		
	}
}
