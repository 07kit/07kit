package com.kit.game.cache.io;

public abstract class Stream {

	public int offset;
	protected int length;
	protected byte[] buffer;
	protected int bitPosition;

	public int getLength() {
		return length;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public int getOffset() {
		return offset;
	}

	public void decodeXTEA(int keys[]) {
		decodeXTEA(keys, 5, length);
	}

	public void decodeXTEA(int keys[], int start, int end) {
		int l = offset;
		offset = start;
		int i1 = (end - start) / 8;
		for (int j1 = 0; j1 < i1; j1++) {
			int k1 = readInt();
			int l1 = readInt();
			int sum = 0xc6ef3720;
			int delta = 0x9e3779b9;
			for (int k2 = 32; k2-- > 0;) {
				l1 -= keys[(sum & 0x1c84) >>> 11] + sum ^ (k1 >>> 5 ^ k1 << 4) + k1;
				sum -= delta;
				k1 -= (l1 >>> 5 ^ l1 << 4) + l1 ^ keys[sum & 3] + sum;
			}
			offset -= 8;
			writeInt(k1);
			writeInt(l1);
		}
		offset = l;
	}

	public final void encodeXTEA(int keys[], int start, int end) {
		int o = offset;
		int j = (end - start) / 8;
		offset = start;
		for (int k = 0; k < j; k++) {
			int l = readInt();
			int i1 = readInt();
			int sum = 0;
			int delta = 0x9e3779b9;
			for (int l1 = 32; l1-- > 0;) {
				l += sum + keys[3 & sum] ^ i1 + (i1 >>> 5 ^ i1 << 4);
				sum += delta;
				i1 += l + (l >>> 5 ^ l << 4) ^ keys[(0x1eec & sum) >>> 11] + sum;
			}

			offset -= 8;
			writeInt(l);
			writeInt(i1);
		}
		offset = o;
	}

	public byte ag() {
		return this.buffer[(this.offset += 1) - 1];
	}

	public int readUnsignedInt(int var1) {
		this.offset += 4;
		return (this.buffer[this.offset - 1] & 255) + ((this.buffer[this.offset - 3] & 255) << 16) + ((this.buffer[this.offset - 4] & 255) << 24) + ((this.buffer[this.offset - 2] & 255) << 8);
	}

	public String readString(int var1) {
		int var2 = this.offset;
		while (true) {
			if (this.buffer[(this.offset += 1) - 1] == 0) {
				int var3 = this.offset - var2 - 1;
				if (0 == var3) {
					return "";
				}
				return convertToJagexString(this.buffer, var2, var3, -2043911383);
			}
		}
	}

	static final char[] fieldE = new char[] {
			'\u20ac',
			'\u0000',
			'\u201a',
			'\u0192',
			'\u201e',
			'\u2026',
			'\u2020',
			'\u2021',
			'\u02c6',
			'\u2030',
			'\u0160',
			'\u2039',
			'\u0152',
			'\u0000',
			'\u017d',
			'\u0000',
			'\u0000',
			'\u2018',
			'\u2019',
			'\u201c',
			'\u201d',
			'\u2022',
			'\u2013',
			'\u2014',
			'\u02dc',
			'\u2122',
			'\u0161',
			'\u203a',
			'\u0153',
			'\u0000',
			'\u017e',
			'\u0178' };

	public static String convertToJagexString(byte[] var0, int var1, int var2, int var3) {
		char[] var4 = new char[var2];
		int var5 = 0;
		for (int var6 = 0; var6 < var2; ++var6) {
			int var7 = var0[var6 + var1] & 255;
			if (var7 != 0) {
				if (var7 >= 128) {
					if (var7 < 160) {
						char var8 = fieldE[var7 - 128];
						if (var8 == 0) {
							var8 = 63;
						}
						var7 = var8;
					}
				}
				var4[var5++] = (char) var7;
			}
		}
		return new String(var4, 0, var5);
	}

	private final int readInt() {
		this.offset += 4;
		return ((0xff & buffer[-3 + offset]) << 16) + ((((0xff & buffer[-4 + offset]) << 24) + ((buffer[-2 + offset] & 0xff) << 8)) + (buffer[-1 + offset] & 0xff));
	}

	public int readUnsignedByte() {
		this.offset++;
		return this.buffer[this.offset - 1] & 255;
	}

	public void writeInt(int value) {
		buffer[offset++] = (byte) (value >> 24);
		buffer[offset++] = (byte) (value >> 16);
		buffer[offset++] = (byte) (value >> 8);
		buffer[offset++] = (byte) value;
	}

	public final void getBytes(byte data[], int off, int len) {
		for (int k = off; k < len + off; k++) {
			data[k] = buffer[offset++];
		}
	}

	public int aj(int var1) {
		this.offset += 2;
		int var2 = ((this.buffer[this.offset - 2] & 255) << 8) + (this.buffer[this.offset - 1] & 255);
		if (var2 > 32767) {
			var2 -= 65536;
		}
		return var2;
	}

	public int ab(int var1) {
		this.offset += 3;
		return (this.buffer[this.offset - 1] & 255) + ((this.buffer[this.offset - 3] & 255) << 16) + ((this.buffer[this.offset - 2] & 255) << 8);
	}

	public int readUnsignedShort() {
		this.offset += 2;
		return ((this.buffer[this.offset - 2] & 255) << 8) + (this.buffer[this.offset - 1] & 255);
	}

}
