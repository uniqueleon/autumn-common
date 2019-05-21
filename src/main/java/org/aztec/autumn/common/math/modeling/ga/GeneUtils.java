package org.aztec.autumn.common.math.modeling.ga;

import static  org.aztec.autumn.common.math.modeling.packing.AlgorithmContextFactory.ContextKeys.ITEM_NUMS;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aztec.autumn.common.algorithm.genetic.Gene;
import org.aztec.autumn.common.math.modeling.packing.AlgorithmContext;
import org.aztec.autumn.common.math.modeling.packing.AlgorithmContextFactory;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.Location;

import com.google.common.collect.Lists;

public class GeneUtils {

	private static final Random random = new Random();
	
	public static FillInfo multiFill(Box targetBox,List<Item> items){
		Item fillItem = null;
		int tryTime = items.size();
		List<Integer> tried = Lists.newArrayList();
		int index = -1;
		while (tryTime >= 0) {
			Integer tryIndex = random.nextInt(items.size());
			while (tried.contains(tryIndex)) {
				tryIndex = random.nextInt(items.size());
			}
			Item item = items.get(tryIndex);
			if (targetBox.isFillable(item)) {
				fillItem = item.clone();
				index = tryIndex;
				break;
			}
			tryTime --;
		}
		if (fillItem == null)
			return null;
		int shape = fillItem.randomSelectFillableShape(targetBox);
		Item mergedItem = targetBox.getRandomMultipleFillItem(random, shape, fillItem);
		if(mergedItem == null)
			return null;
		List<Box> boxes = caculateRemainSpaces(targetBox,0, mergedItem);
		return new FillInfo(boxes, mergedItem, index, mergedItem.getNumber().intValue());
	}
	

	public static FillInfo fill(Box targetBox, List<Item> items) {

		Item fillItem = null;
		int tryTime = items.size();
		List<Integer> tried = Lists.newArrayList();
		int index = -1;
		while (tryTime >= 0) {
			Integer tryIndex = random.nextInt(items.size());
			while (tried.contains(tryIndex)) {
				tryIndex = random.nextInt(items.size());
			}
			Item item = items.get(tryIndex);
			if (targetBox.isFillable(item)) {
				fillItem = item.clone();
				index = tryIndex;
				break;
			}
			tryTime --;
		}
		if (fillItem == null)
			return null;

		int shape = fillItem.randomSelectFillableShape(targetBox);
		Location location = targetBox.getLocation().clone();
		fillItem.setLocation(location);
		List<Box> boxes = caculateRemainSpaces(targetBox, shape,fillItem);
		return new FillInfo(boxes, fillItem, index, 1);
	}

	private static List<Box> caculateRemainSpaces(Box targetBox,int shape, Item fillItem) {
		List<Box> boxes = Lists.newArrayList();

		Double[] edges = fillItem.getShapeData(shape);
		long itemLength = edges[0].longValue();
		long itemWidth = edges[1].longValue();
		long itemHeight = edges[2].longValue();
		Location boxLocation = targetBox.getLocation();
		if (shape != -1) {
			int schemeIndex = random.nextInt(4);
			fillItem.setShape(shape);
			return getBoxShearScheme(targetBox, boxLocation, itemLength,
					itemWidth, itemHeight, schemeIndex);
		}
		return boxes;
	}

	private static List<Box> getBoxShearScheme(Box targetBox,
			Location boxLocation, long itemLength, long itemWidth,
			long itemHeight, int index) {
		List<Box> boxes = Lists.newArrayList();
		Box newBox1, newBox2, newBox3;
		switch (index) {
		// || - t
		case 0:
			newBox1 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue() - itemLength,
					targetBox.getWidth().longValue(),
					targetBox.getHeight().longValue() }, 1l, 1l);
			if(newBox1.isExist()){
				newBox1.setLocation(new Location(boxLocation.getX() + itemLength,
						boxLocation.getY(), boxLocation.getZ()));
				boxes.add(newBox1);
			}
			newBox2 = new Box(targetBox.getId(), new Long[] { itemLength,
					targetBox.getWidth().longValue() - itemWidth,
					targetBox.getHeight().longValue() }, 1l, 1l);
			if(newBox2.isExist()){
				newBox2.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY() + itemWidth, boxLocation.getZ()));
				boxes.add(newBox2);
			}
			newBox3 = new Box(targetBox.getId(),
					new Long[] { itemLength, itemWidth,
							targetBox.getHeight().longValue() - itemHeight },
					1l, 1l);
			if(newBox3.isExist()){
				newBox3.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY(), boxLocation.getZ() + itemHeight));
				boxes.add(newBox3);
			}
			break;
		case 1:
			// | -- t
			newBox1 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue() - itemLength, itemWidth,
					targetBox.getHeight().longValue() }, 1l, 1l);
			if(newBox1.isExist()){
				newBox1.setLocation(new Location(boxLocation.getX() + itemLength,
						boxLocation.getY(), boxLocation.getZ()));
				boxes.add(newBox1);
			}
			newBox2 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue(),
					targetBox.getWidth().longValue() - itemWidth,
					targetBox.getHeight().longValue() }, 1l, 1l);
			if(newBox2.isExist()){
				newBox2.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY() + itemWidth, boxLocation.getZ()));
				boxes.add(newBox2);
			}
			newBox3 = new Box(targetBox.getId(),
					new Long[] { itemLength, itemWidth,
							targetBox.getHeight().longValue() - itemHeight },
					1l, 1l);
			if(newBox3.isExist()){
				newBox3.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY(), boxLocation.getZ() + itemHeight));
				boxes.add(newBox3);
			}
			break;
		case 2:
			// || - T
			newBox1 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue() - itemLength,
					targetBox.getWidth().longValue(), itemHeight }, 1l, 1l);
			if(newBox1.isExist()){
				newBox1.setLocation(new Location(boxLocation.getX() + itemLength,
						boxLocation.getY(), boxLocation.getZ()));
				boxes.add(newBox1);
			}
			newBox2 = new Box(targetBox.getId(), new Long[] { itemLength,
					targetBox.getWidth().longValue() - itemWidth, itemHeight },
					1l, 1l);
			if(newBox2.isExist()){
				newBox2.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY() + itemWidth, boxLocation.getZ()));
				boxes.add(newBox2);
			}
			newBox3 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue(),
					targetBox.getWidth().longValue(),
					targetBox.getHeight().longValue() - itemHeight }, 1l, 1l);
			if(newBox3.isExist()){
				newBox3.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY(), boxLocation.getZ() + itemHeight));
				boxes.add(newBox3);
			}
			break;
		case 3:
			// | -- T
			
			newBox1 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue() - itemLength, itemWidth,
					itemHeight }, 1l, 1l);
			if(newBox1.isExist()){
				newBox1.setLocation(new Location(boxLocation.getX() + itemLength,
						boxLocation.getY(), boxLocation.getZ()));
				boxes.add(newBox1);
			}
			newBox2 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue(),
					targetBox.getWidth().longValue() - itemWidth, itemHeight },
					1l, 1l);
			if(newBox2.isExist()){
				newBox2.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY() + itemWidth, boxLocation.getZ()));
				boxes.add(newBox2);
			}
			newBox3 = new Box(targetBox.getId(), new Long[] {
					targetBox.getLength().longValue(),
					targetBox.getWidth().longValue(),
					targetBox.getHeight().longValue() - itemHeight }, 1l, 1l);
			if(newBox3.isExist()){
				newBox3.setLocation(new Location(boxLocation.getX(), boxLocation
						.getY(), boxLocation.getZ() + itemHeight));
				boxes.add(newBox3);
			}
			break;
		}
		return boxes;
	}

	public static boolean isCompatible(PackingStep gene1, PackingStep gene2) {
		
		if(gene1.getX() == gene2.getX() && gene1.getY() == gene2.getY()
				&& gene1.getZ() == gene2.getZ()){
			return false;
		}
		if (gene1.getX() != gene2.getX() && 
				((gene1.getX() > gene2.getX() && (gene2.getX() + gene2.getLength()) <= gene1.getX()) || 
				(gene1.getX() < gene2.getX()  && gene1.getX() + gene1.getLength() <= gene2.getX()))) {
			return true;
		}
		if (gene1.getY() != gene2.getY() && 
				((gene1.getY() > gene2.getY() && (gene2.getY() + gene2.getWidth()) <= gene1.getY()) ||
				(gene1.getY() < gene2.getY() && (gene1.getY() + gene1.getWidth()) <= gene2.getY()))) {
			return true;
		}
		if (gene1.getZ() != gene2.getZ() && 
				((gene1.getZ() > gene2.getZ() && (gene2.getZ() + gene2.getHeight()) <= gene1.getZ()) ||
				(gene1.getZ() < gene2.getZ() && (gene1.getZ() + gene1.getHeight()) <= gene2.getZ()))) {
			return true;
		}
		return false;
	}

	public static List<Gene> getCompatibleGenes(
			PackingPlan plan1, PackingPlan plan2) throws CloneNotSupportedException {

		List<Gene> compatibleGenes = Lists.newArrayList();
		Gene[] genes1 = plan1.getGenes();
		Gene[] genes2 = plan2.getGenes();
		for (Gene gene : genes1) {
			PackingStep ps1 = (PackingStep) gene;
			boolean compatible = true;
			for (Gene gene2 : genes2) {
				PackingStep ps2 = (PackingStep) gene2;
				if (!isCompatible(ps1, ps2)) {
					compatible = false;
				}
			}
			if(compatible){
				compatibleGenes.add(gene.clone());
			}
		}
		return compatibleGenes;
	}

	public static Gene[] rebuildGenes(PackingPlan plan1, List<Gene> genes)
			throws CloneNotSupportedException {
		Gene[] newGenes = plan1.cloneGenes();
		Gene[] retGene = new Gene[newGenes.length + genes.size()];
		int totalLength = newGenes.length + genes.size();
		int oriLength = newGenes.length;
		for(int i = 0;i < totalLength;i++){
			PackingStep ps = null;
			if(i < newGenes.length){
				ps = (PackingStep) newGenes[i];
			}
			else{
				ps = (PackingStep) genes.get(i - oriLength);
			}
			ps.setNo(i);
			retGene[i] = ps;
		}
		return retGene;
	}

	public static Map<String,Long> getContextItemNums(){

		AlgorithmContext context = AlgorithmContextFactory.getContext();
		Map<String,Long> itemNums = context.get(ITEM_NUMS);
		return itemNums;
	}
	
	
	public static Integer countPossiblePlan(Box box,List<Item> items){
		Integer count = 0;
		for(int i = 0;i < items.size();i++){
			Item item = items.get(i);
			Integer shapeCount = items.get(i).getFillableShapeNum(box);
			Integer schemeNum = (items.size() == 1 && item.getNumber().intValue() == 1) ? 1 : 4;
			count += shapeCount * schemeNum * item.getNumber().intValue();
		}
		return count;
	}

}
