package org.aztec.autumn.common.utils.compress.base;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.List;

import org.aztec.autumn.common.utils.compress.code.HuffmanCode;

import com.google.common.collect.Lists;


public class HuffmanCoder {
	
	public List<HuffmanCode> transfer(List<HuffmanCode> rawCodes) {
		
		List<HuffmanCode> retList = Lists.newArrayList();
		HuffmanTree tree = new HuffmanTree(rawCodes);
		List<HuffmanTreeNode> leafs = tree.getLeafs();
		for(HuffmanTreeNode leaf : leafs) {
			retList.add(leaf.getData());
		}
		return retList;
	}
	
	public byte[] coding(List<HuffmanCode> rawCodes) {
		
		return new byte[0];
	}
	
	
	public class HuffmanTree{
		private HuffmanTreeNode root;
		private Long frequence;
		private List<HuffmanTreeNode> leafs;
		
		public HuffmanTree(List<HuffmanCode> huffmanCodes) {
			if(huffmanCodes == null || huffmanCodes.size() == 0)
				throw new IllegalArgumentException("No code to build tree!");
			leafs = Lists.newArrayList();
			for(HuffmanCode huffmanCode : huffmanCodes) {
				leafs.add(new HuffmanTreeNode(huffmanCode.getOldCode().getFrequence(), true, huffmanCode));
			}
			init();
		}
		
		private void init() {
			buildTree();
			refreshLeafs();
		}
		
		private void buildTree() {
			
			if(leafs.size() == 1) {
				root = leafs.get(0);
				return ;
			}
			List<HuffmanTreeNode> workingNodes = Lists.newArrayList();
			for(HuffmanTreeNode workNode : leafs) {
				workingNodes.add(workNode);
			}
			HuffmanComparator comparator = new HuffmanComparator();
			while(workingNodes.size() > 1) {
				workingNodes.sort(comparator);
				HuffmanTreeNode node1 = workingNodes.get(0);
				HuffmanTreeNode node2 = workingNodes.get(1);
				node1.setBranch(false);
				node2.setBranch(true);
				List<HuffmanTreeNode> childrens = Lists.newArrayList();
				childrens.add(node1);
				childrens.add(node2);
				HuffmanTreeNode newNode = new HuffmanTreeNode(node1.getFrequence() + node2.getFrequence(), false, childrens);
				workingNodes.remove(0);
				workingNodes.remove(0);
				workingNodes.add(newNode);
				//workingNodes.
			}
			root = workingNodes.get(0);
			
		}
		
		private void refreshLeafs() {
			BitSet path = new BitSet();
			leafs = refreshLeafs(root, path, 0);
		}
		
		private BitSet reorder(BitSet bs,int lastIndex) {

			BitSet newBs = (BitSet) bs.clone();
			for(int i = 0 ;i <= lastIndex;i++) {
				newBs.set(i,bs.get(lastIndex - i));
			}
			return newBs;
		}
		
		public List<HuffmanTreeNode> refreshLeafs(HuffmanTreeNode treeNode,BitSet path,Integer dept) {
			List<HuffmanTreeNode> retList = Lists.newArrayList();
			if(treeNode.isLeaf()) {
				path.set(dept, treeNode.branch);
				treeNode.getData().getNewCode().setLength(new Long(dept));
				treeNode.getData().getNewCode().setCode(reorder(path,dept));
				retList.add(treeNode);
				return retList;
			}
			else  {
				for(HuffmanTreeNode child : treeNode.getChildrens()) {
					path.set(dept,child.isBranch());
					retList.addAll(refreshLeafs(child, path, dept + 1));
				}
			}
			return retList;
		}

		public HuffmanTreeNode getRoot() {
			return root;
		}

		public void setRoot(HuffmanTreeNode root) {
			this.root = root;
		}

		public Long getFrequence() {
			return frequence;
		}

		public void setFrequence(Long frequence) {
			this.frequence = frequence;
		}

		public List<HuffmanTreeNode> getLeafs() {
			return leafs;
		}

		public void setLeafs(List<HuffmanTreeNode> leafs) {
			this.leafs = leafs;
		}
		
		
	}
	
	
	public class HuffmanTreeNode{
		
		private Long frequence;
		private boolean branch;
		private boolean leaf;
		private HuffmanCode data;
		private List<HuffmanTreeNode> childrens;
		
		public Long getFrequence() {
			return frequence;
		}
		public void setFrequence(Long frequence) {
			this.frequence = frequence;
		}
		public boolean isBranch() {
			return branch;
		}
		public void setBranch(boolean branch) {
			this.branch = branch;
		}
		public boolean isLeaf() {
			return leaf;
		}
		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}
		public HuffmanTreeNode(Long sequence, boolean leaf, HuffmanCode code) {
			super();
			this.frequence = sequence;
			this.leaf = leaf;
			this.data = code;
		}
		
		public HuffmanTreeNode(Long frequence, boolean leaf, List<HuffmanTreeNode> childrens) {
			super();
			this.frequence = frequence;
			this.leaf = leaf;
			this.childrens = childrens;
		}
		public HuffmanCode getData() {
			return data;
		}
		public void setData(HuffmanCode code) {
			this.data = code;
		}
		public List<HuffmanTreeNode> getChildrens() {
			return childrens;
		}
		public void setChildrens(List<HuffmanTreeNode> childrens) {
			this.childrens = childrens;
		}
		
		
	}
	public static void main(String[] args) {
		try {
			HuffmanCoder coder = new HuffmanCoder();
			List<HuffmanCode> codes = Lists.newArrayList();
			/*codes.add(new HuffmanCode(1, 2l));
			codes.add(new HuffmanCode(2, 2l));
			codes.add(new HuffmanCode(3, 2l));
			codes.add(new HuffmanCode(4, 2l));
			codes.add(new HuffmanCode(5, 2l));
			codes.add(new HuffmanCode(6, 2l));
			codes.add(new HuffmanCode(7, 2l));*/
			for(int i = 0;i < 15;i++) {

				codes.add(new HuffmanCode(i, 1l));
			}
			BitSet bs1 = BitSet.valueOf(new byte[] {3});
			BitSet bs2 = BitSet.valueOf(new byte[] {40});
			System.out.println(bs1.intersects(bs2));
			//codes.add(new HuffmanCode('d', 1l));
			List<HuffmanCode> tranCodes = coder.transfer(codes);
			for(HuffmanCode tranCode : tranCodes) {
				String text = "" + tranCode.getOldCodeAsInt() + "(" + tranCode.getOldCodeAsString() + ")";
				System.out.println(text + "-" + tranCode.getNewCode().getAsBinaryString());
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
