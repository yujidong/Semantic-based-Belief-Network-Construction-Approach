package org.cloudbus.cloudsim.network;

public class DataPacket implements Packet {
	
	private int dataPacketId;
	private long size;
	private int srcId;
	private int destId;
	private int netServiceType;
	private int last;
	private int tag;

	
	public DataPacket() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DataPacket(int dataPacketId, long size, int srcId, int destId, int netServiceType, int last, int tag) {
		super();
		this.dataPacketId = dataPacketId;
		this.size = size;
		this.srcId = srcId;
		this.destId = destId;
		this.netServiceType = netServiceType;
		this.last = last;
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "DataPacket [dataPacketId=" + dataPacketId + ", size=" + size + ", srcId=" + srcId + ", destId=" + destId
				+ ", netServiceType=" + netServiceType + ", last=" + last + ", tag=" + tag + "]";
	}

	public int getDataPacketId() {
		return dataPacketId;
	}

	public void setDataPacketId(int dataPacketId) {
		this.dataPacketId = dataPacketId;
	}

	public void setSrcId(int srcId) {
		this.srcId = srcId;
	}

	public void setDestId(int destId) {
		this.destId = destId;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public boolean setSize(long size) {
		this.size = size;
		return true;
	}

	@Override
	public int getDestId() {
		return destId;
	}

	@Override
	public int getId() {
		return dataPacketId;
	}

	@Override
	public int getSrcId() {
		return srcId;
	}

	@Override
	public int getNetServiceType() {
		return netServiceType;
	}

	@Override
	public void setNetServiceType(int serviceType) {
		this.netServiceType = serviceType;
		
	}

	@Override
	public int getLast() {
		return last;
	}

	@Override
	public void setLast(int last) {
		this.last = last;
		
	}

	@Override
	public int getTag() {
		return tag;
	}


}
