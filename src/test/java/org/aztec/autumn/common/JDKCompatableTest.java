package org.aztec.autumn.common;

import java.util.List;

import com.google.common.collect.Lists;


public class JDKCompatableTest {

	public JDKCompatableTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> testList = Lists.newArrayList();
		testList.add("Heallo");
		testList.removeAll(null);
		System.out.println(testList);
	}

}