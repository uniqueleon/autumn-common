package org.aztec.autumn.common.utils.bytes;

public class ByteNeighborInfo {
	
	private byte neighbor;
	private int distance;
	private long frequence;

	public ByteNeighborInfo() {
		// TODO Auto-generated constructor stub
	}

	public byte getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(byte neighbor) {
		this.neighbor = neighbor;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public long getFrequence() {
		return frequence;
	}

	public void setFrequence(long frequence) {
		this.frequence = frequence;
	}

	public ByteNeighborInfo(byte neighborhood, int distance, long frequence) {
		super();
		this.neighbor = neighborhood;
		this.distance = distance;
		this.frequence = frequence;
	}

	@Override
	public String toString() {
		return "ByteNeighborInfo [neighbor=" + neighbor + ", distance=" + distance + ", frequence=" + frequence + "]";
	}

}
