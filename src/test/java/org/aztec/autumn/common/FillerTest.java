package org.aztec.autumn.common;

import java.util.List;

import org.aztec.autumn.common.constant.unit.LengthUnits;
import org.aztec.autumn.common.constant.unit.WeightUnits;
import org.aztec.autumn.common.math.modeling.packing.BinPackingConfig;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolution;
import org.aztec.autumn.common.math.modeling.packing.BinPackingSolver;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.RectangleSurfaceFiller;
import org.aztec.autumn.common.math.modeling.packing.impl.FillParameter;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResult;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResult.FillType;

import com.google.common.collect.Lists;

public class FillerTest {

	
	public static void test1() {

		
		Box testBox = new Box(67d, 42d, 6d, LengthUnits.MM, 1000d, 1000d, WeightUnits.G, 1l);
		testBox.setSurfaceChoosed(0);
		List<Item> items = Lists.newArrayList();
		Item itemA = new Item(4d, 8d, 2d, LengthUnits.MM, 1000d, WeightUnits.G, 200l);
		// 1600 + 80 + 162
		itemA.setId("ITEM_A");
		itemA.setSurfaceChoosed(0);
		Item itemB = new Item(2d, 5d, 6d, LengthUnits.MM, 1000d, WeightUnits.G, 10l);
		itemB.setId("ITEM_B");
		itemB.setSurfaceChoosed(0);

		Item itemC = new Item(6d, 9d, 6d, LengthUnits.MM, 1000d, WeightUnits.G, 200l);
		itemC.setId("ITEM_C");
		itemC.setSurfaceChoosed(0);
		items.add(itemA);
		items.add(itemB);
		items.add(itemC);
		FillParameter fp = new FillParameter(testBox, items, FillType.SURFACE);
		RectangleSurfaceFiller filler = new RectangleSurfaceFiller();
		Long startTime = System.currentTimeMillis();
		FillResult result = filler.fill(fp);
		Long elapseTime = System.currentTimeMillis();
		System.out.println(result);
		System.out.println("used:" + (elapseTime - startTime));
	}
	
	
	/**
	 * 5# 可以装3个, 6#可以装全部
	 */
	public static void test_OS20180730004120() {

		//OS20180730004120,OS20180730000405
		System.out.println("order no : OS20180730004120");
		Long[][] itemDatas = new Long[][] {{170l,75l,45l},{225l,95l,55l},{225l,90l,48l},{225l,90l,50l}};
		String[] serialNo = new String[] {"8850007812470","6907376821045","8850339309334","8850007812609"};
		Long[][] numbers = new Long[][] {{0l,1l},{0l,1l},{0l,1l},{0l,1l}};
		List<Item> items = Lists.newArrayList();
		for(int i = 0;i < itemDatas.length;i++) {
			items.add(new Item(serialNo[i],itemDatas[i], 1l, numbers[i][1]));
		}
		Box box1 = new Box("1#",new Long[] {530l,365l,180l},1l,1l);

		box1 = new Box("6#",new Long[] {280l,180l,117l},1l,1l);
		Long curTime = System.currentTimeMillis();
		//box1 = new Box("5#",new Long[] {182l,222l,302l},1l,1l);
		box1.setSurfaceChoosed(0);
		BinPackingSolver solver = new BinPackingSolver();
		BinPackingSolution packSolution = solver.solve(new BinPackingConfig(items, box1, 1,null));
		System.out.println(packSolution);
		System.out.println("use time :" + (System.currentTimeMillis() - curTime));
	}
	
	/**
	 * 5# 可以装5个, 6#可以装全部
	 */
	public static void test_OS20180730000405() {
		//OS20180730004120,OS20180730000405
		Long[][] itemDatas = new Long[][] {{122l,40l,40l},{225l,90l,48l}};
		String[] serialNo = new String[] {"6907376820796","6907376821045","8850339309334","8850007812609"};
		Long[][] numbers = new Long[][] {{0l,3l},{0l,3l}};
		List<Item> items = Lists.newArrayList();
		for(int i = 0;i < itemDatas.length;i++) {
			items.add(new Item(serialNo[i],itemDatas[i], 1l, numbers[i][1]));
		}
		Box box1 = new Box("1#",new Long[] {530l,365l,180l},1l,1l);

		//box1 = new Box("6#",new Long[] {280l,177l,117l},1l,1l);
		Long curTime = System.currentTimeMillis();
		box1 = new Box("5#",new Long[] {182l,222l,302l},1l,1l);
		box1.setSurfaceChoosed(0);
		BinPackingSolver solver = new BinPackingSolver();

		BinPackingSolution solution = solver.solve(new BinPackingConfig(items, box1, 1,null));
		System.out.println(solution);
		
		System.out.println("use time :" + (System.currentTimeMillis() - curTime));
	}
	
	public static void main(String[] args) {

		//test1();
		test_OS20180730004120();
		//test_OS20180730000405();
	}

}
