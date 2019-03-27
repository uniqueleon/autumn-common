package org.aztec.autumn.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aztec.autumn.common.math.equations.DESolutionConstraint;
import org.aztec.autumn.common.math.equations.DESolutionConstraint.DESolutionConstraintType;
import org.aztec.autumn.common.math.equations.DiophantineEquation;
import org.aztec.autumn.common.math.equations.DiophantineResult;
import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolution;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolver;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.Location;
import org.aztec.autumn.common.math.modeling.packing.SaleOrder;
import org.aztec.autumn.common.math.modeling.packing.SolutionEvaluator;
import org.aztec.autumn.common.math.modeling.packing.impl.FillRatioEvaluator;
import org.aztec.autumn.common.utils.FileUtils;
import org.aztec.autumn.common.utils.jxl.MSExcelUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BinPackingTest {

	public BinPackingTest() {

		// ITEM_A

		DiophantineEquation de = new DiophantineEquation();
		DiophantineResult dr = DiophantineEquation.getSolution(new Long[] { 10l, 110l, 120l }, 480l);
		Map<DESolutionConstraintType, DESolutionConstraint> constraints = Maps.newHashMap();
		DESolutionConstraint.addRangeConstraint(constraints, new Long[][] { { 0l, 2l }, { 0l, 5l }, { 0l, 2l } });
		dr.constrain(constraints);
		System.out.println(dr);

	}

	public static void main(String[] args) {
		// LBX0329941778510906
		// runCaoLiaoSample(1,"LBX0329941778476144");
		// runCaoLiaoSample(1,"LBX0329941778510906");
		// runCaoLiaoSample(1,"LBX0329941778510906");
		// String testStr =
		// "xxxefqewfqwfewfeqwfeqcqcfqewxfqewxfwqefxqefefweffqxqewxfqwexwfx@#$&^&*";

		// System.out.println(testStr.hashCode());
		// runCaoLiaoSample(1, null);
		// runTestBugCase();
		try {
			compareResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void compareResult() throws IOException {

		File shearBoxResultDir = new File("E:\\work\\test\\boxing\\result2");
		File shearBoxResultExlFile = new File("C:\\Users\\10064513\\Desktop\\H1_1.xls");
		Map<String, ShearResultDetail> WRC_Results = Maps.newHashMap();
		Map<String, ShearResultDetail> WMP_Results = Maps.newHashMap();

		List<List<List<String>>> resultData = MSExcelUtil.read(shearBoxResultExlFile);
		WRC_Results = toWRC_ShearResultDetail(resultData);
		WMP_Results = toWMP_ShearResultDetail(shearBoxResultDir);
		compare(WRC_Results, WMP_Results);
	}

	public static void compare(Map<String, ShearResultDetail> wrcResults, Map<String, ShearResultDetail> wmpResults)
			throws IOException {
		int totalSize = wmpResults.size();
		File compareResultFile = new File("E:\\work\\test\\boxing\\compareResult.txt");
		StringBuilder fileContext = new StringBuilder();
		if (compareResultFile.exists()) {
			compareResultFile.createNewFile();
		}
		fileContext.append("ORDER--WMP--WRC \n");
		for (String orderNo : wmpResults.keySet()) {
			ShearResultDetail srd = wmpResults.get(orderNo);
			ShearResultDetail srd2 = wrcResults.get(orderNo);
			boolean fast = false;
			boolean good = false;
			boolean equal = false;
			boolean realGood = false;
			int boxNo = Integer.parseInt(srd.getBoxNo()) - 100;
			int boxNo2 = Integer.parseInt(srd2.getBoxNo().replace("#", "").trim());
			if(boxNo > boxNo2) {
				realGood = true;
			}
			if(srd.getItemNum() >= srd2.getItemNum() && srd.getFillRate() > srd2.getFillRate()) {
				good = true;
			}
			if(srd.getItemNum().equals(srd2.getItemNum()) && Math.abs(srd.getFillRate() - srd2.getFillRate()) < 0.001) {
				equal = true;
			}
			if(srd.getUseTime() <= srd2.getUseTime() ) {
				fast = true;
			}
			fileContext.append(orderNo + "--" + (realGood ? "*" : "") + (!equal && good ? "G" : " ") + (fast ? "F" : " ") 
					+ "--" + (!equal && !good ? "G" : " ") + (!fast ? "F" : "") + "\n");
		}
		FileUtils.writeFile(compareResultFile, fileContext.toString().getBytes("UTF-8"));
	}

	public static Map<String, ShearResultDetail> toWMP_ShearResultDetail(File shearBoxDir) throws IOException {

		Map<String, ShearResultDetail> details = Maps.newHashMap();
		for (File sResultFile : shearBoxDir.listFiles()) {
			ShearResultDetail srd = readResultDetail(sResultFile);
			details.put(srd.getOrderNo(), srd);
		}
		return details;
	}

	public static ShearResultDetail readResultDetail(File sResultFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(sResultFile));
		String line = br.readLine();
		Double score = 0d;
		Pattern pattern = Pattern.compile("Box \\[id=\\d+\\,");
		Pattern orderPattern = Pattern.compile("SaleOrder \\[id=\\S+\\,");
		String boxNo = null, orderNo = null;
		Long useTime = null;
		Integer itemNum = 0;
		Double fillRate = 0d;
		while (line != null) {
			Matcher matcher = pattern.matcher(line);
			Matcher orderMatcher = orderPattern.matcher(line);
			if (line.contains("score=")) {
				String scoreline = line.replace("score=", "");
				score = Double.parseDouble(scoreline.trim());
				itemNum = score.intValue();
				fillRate = score - itemNum;
			} else if (matcher.find()) {
				String idLine = matcher.group();
				boxNo = idLine.replace("Box [id=", "").replace(",", "");
			} else if (orderMatcher.find()) {
				String orderLine = orderMatcher.group();
				orderNo = orderLine.replace("SaleOrder [id=", "").replace(",", "");
			} else if (line.contains("using time ")) {
				String usedTimeStr = line.replace("]]using time :", "");
				useTime = Long.parseLong(usedTimeStr);
			}
			line = br.readLine();
		}
		ShearResultDetail detail = new ShearResultDetail(orderNo, useTime, boxNo, fillRate, itemNum);

		return detail;
	}

	public static Map<String, ShearResultDetail> toWRC_ShearResultDetail(List<List<List<String>>> resData) {
		List<List<String>> pageDatas = resData.get(0);
		Map<String, ShearResultDetail> details = Maps.newHashMap();
		for (int i = 1; i < pageDatas.size(); i++) {
			ShearResultDetail detail = new ShearResultDetail(pageDatas.get(i).get(0),
					new Double(Double.parseDouble(pageDatas.get(i).get(12).trim()) * 1000).longValue(),
					pageDatas.get(i).get(8), Double.parseDouble(pageDatas.get(i).get(11)),
					Integer.parseInt(pageDatas.get(i).get(13)));
			details.put(detail.getOrderNo(), detail);
		}
		return details;
	}

	public static class ShearResultDetail {
		private String orderNo;
		private Long useTime;
		private String boxNo;
		private Double fillRate;
		private Integer itemNum;

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public Long getUseTime() {
			return useTime;
		}

		public void setUseTime(Long useTime) {
			this.useTime = useTime;
		}

		public String getBoxNo() {
			return boxNo;
		}

		public void setBoxNo(String boxNo) {
			this.boxNo = boxNo;
		}

		public Double getFillRate() {
			return fillRate;
		}

		public void setFillRate(Double fillRate) {
			this.fillRate = fillRate;
		}

		public Integer getItemNum() {
			return itemNum;
		}

		public void setItemNum(Integer itemNum) {
			this.itemNum = itemNum;
		}

		public ShearResultDetail(String orderNo, Long useTime, String boxNo, Double fillRate, Integer itemNum) {
			super();
			this.orderNo = orderNo;
			this.useTime = useTime;
			this.boxNo = boxNo;
			this.fillRate = fillRate;
			this.itemNum = itemNum;
		}

	}

	public static void runTestBugCase() {
		Box box = new Box("#11", new Long[] { 65l, 65l, 160l }, 1l, 1l);
		box.setLocation(new Location(0d, 70d, 0d));
		List<Item> items = Lists.newArrayList();
		Item item = new Item("6927462206297", new Long[] { 28l, 65l, 70l }, 1l, 5l);
		items.add(item);
		BinPackingConfig config = new BinPackingConfig(items, box, 1, null);
		BinPackingSolver solver = new BinPackingSolver();
		BinPackingSolution solution = solver.solve(config);

		System.out.println(solution);
	}

	public static void runCaoLiaoSample(int testNum, String orderNo) {
		try {
			// readBoxesData(new File("test/tjTmallBoxes.csv"));
			List<SaleOrder> orders = readOrderData(new File("test/CNNJ1500+.csv"), new File("test/tjTmallBoxes.csv"));
			/*
			 * for(SaleOrder order : orders) {
			 * System.out.println("$$$$$$$$$$$$$$$$$ORDER INFO$$$$$$$$$$$$$$$$$");
			 * System.out.println(order);
			 * System.out.println("$$$$$$$$$$$$$$$$$ORDER INFO$$$$$$$$$$$$$$$$$"); }
			 */

			SolutionEvaluator sEvaluator = new FillRatioEvaluator();
			BinPackingSolver solver = new BinPackingSolver();
			testNum = orders.size();
			Long beginTime = System.currentTimeMillis();
			for (int i = 0; i < testNum; i++) {
				SaleOrder order = orders.get(i);
				if (orderNo != null && !order.getId().equals(orderNo)) {
					continue;
				}
				/*
				 * if(!order.getId().equals("LBX0329941778510906")) { continue; }
				 */
				// LBX0329941778132064 LBX0329941778142996
				System.out.println("generating order:[" + order.getId() + "] 's data result........");
				StringBuilder builder = new StringBuilder();
				builder.append("$$$$$$$$$$$$$$$$$ORDER INFO$$$$$$$$$$$$$$$$$\n");
				builder.append(order);
				builder.append("$$$$$$$$$$$$$$$$$ORDER INFO$$$$$$$$$$$$$$$$$\n");
				Long curTime = System.currentTimeMillis();
				BinPackingSolution solution = solver.solve(order, new BinPackingConfig());
				builder.append(solution);
				Long elapseTime = System.currentTimeMillis() - curTime;
				System.out.println("using time :" + elapseTime);
				builder.append("using time :" + elapseTime);
				File resultFile = new File("test/bp/" + order.getId() + ".txt");
				if (!resultFile.exists()) {
					resultFile.createNewFile();
				}
				FileUtils.writeFile(resultFile, builder.toString().getBytes("UTF-8"));
				System.out.println("order:[" + order.getId() + "] 's data result generate finished");
			}
			System.out.println(">>>>>>>>>>>TOTAL USE TIME:" + (System.currentTimeMillis() - beginTime));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Box> readBoxesData(File boxDataFile) throws IOException {
		String text = FileUtils.readFileAsString(boxDataFile);
		List<Box> boxes = Lists.newArrayList();
		String[] boxLines = text.split("\r\n");
		for (int i = 1; i < boxLines.length; i++) {
			String boxLine = boxLines[i];
			String[] boxDatas = boxLine.split(",");

			Box box = new Box(boxDatas[0],
					new Long[] { new Double(Double.parseDouble(boxDatas[2]) * 10d).longValue(),
							new Double(Double.parseDouble(boxDatas[3]) * 10d).longValue(),
							new Double(Double.parseDouble(boxDatas[4]) * 10d).longValue() },
					1l, 1l);
			box.setLocation(new Location(0d, 0d, 0d));
			boxes.add(box);
			System.out.println(box);
		}
		return boxes;
	}

	public static List<SaleOrder> readOrderData(File orderFile, File boxFile) throws IOException {
		String text = FileUtils.readFileAsString(orderFile);
		List<Box> boxes = readBoxesData(boxFile);
		String[] orderLines = text.split("\r\n");
		String currentOrderNo = null;
		List<Item> currentOrderItems = Lists.newArrayList();
		List<SaleOrder> saleOrders = Lists.newArrayList();
		Item lastItem = null;
		for (int i = 1; i < orderLines.length; i++) {
			String orderLine = orderLines[i];
			String[] orderData = orderLine.split(",");
			if (currentOrderNo == null) {
				currentOrderNo = orderData[0];
			} else if (!currentOrderNo.equals(orderData[0])) {

				SaleOrder currentOrder = new SaleOrder(currentOrderNo, currentOrderItems, boxes);
				saleOrders.add(currentOrder);
				currentOrderNo = orderData[0];
				currentOrderItems = Lists.newArrayList();
			}
			Item newItem = new Item(orderData[1],
					new Long[] { new Double(Double.parseDouble(orderData[4]) * 10d).longValue(),
							new Double(Double.parseDouble(orderData[5]) * 10d).longValue(),
							new Double(Double.parseDouble(orderData[6]) * 10d).longValue() },
					1l, Long.parseLong(orderData[2]));
			if (lastItem != null && lastItem.getId().equals(newItem.getId())) {
				newItem = lastItem;
				newItem.setNumber(newItem.getNumber() + 1);
			} else {
				currentOrderItems.add(newItem);
			}
			lastItem = newItem;
			if (i == orderLines.length - 1) {
				SaleOrder currentOrder = new SaleOrder(currentOrderNo, currentOrderItems, boxes);
				saleOrders.add(currentOrder);
			}
		}
		return saleOrders;
	}

	private static void runSample1() {
		Box box = new Box("#1", new Long[] { 5l, 7l, 6l }, 1l, 1l);
		List<Item> items = Lists.newArrayList();
		Item itemA = new Item("A", new Long[] { 2l, 2l, 2l }, 1l, 6l);
		Item itemB = new Item("B", new Long[] { 1l, 4l, 6l }, 1l, 3l);
		Item itemC = new Item("C", new Long[] { 3l, 1l, 6l }, 1l, 5l);
		items.add(itemA);
		items.add(itemB);
		items.add(itemC);
		BinPackingConfig config = new BinPackingConfig();
		config.setBox(box);
		config.setItems(items);
		BinPackingSolver solver = new BinPackingSolver();
		BinPackingSolution solution = solver.solve(config);
		System.out.println(solution);
	}
}
