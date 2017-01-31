package com.kit.game.cache.io;

import java.math.BigInteger;


public final class Utils {

	public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
		return new BigInteger(data).modPow(exponent, modulus).toByteArray();
	}
	
	public static byte[] getArchivePacketData(int indexId, int archiveId,
			byte[] archive) {
		OutputStream stream = new OutputStream(archive.length + 4);
		stream.writeByte(indexId);
		stream.writeShort(archiveId);
		stream.writeByte(0); // priority, no compression
		stream.writeInt(archive.length);
		int offset = 8;
		for (int index = 0; index < archive.length; index++) {
			if (offset == 512) {
				stream.writeByte(-1);
				offset = 1;
			}
			stream.writeByte(archive[index]);
			offset++;
		}
		byte[] packet = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(packet, 0, packet.length);
		return packet;
	}

	public static int getNameHash(String name) {
		return name.toLowerCase().hashCode();
	}

}
