package com.kit.game.cache.util.bzip2;

public class BZ2Block {
	BZ2Block() {
		unzftab = new int[256];
		cftab = new int[257];
		inUse = new boolean[256];
		inUse16 = new boolean[16];
		seqToUnseq = new byte[256];
		yy = new byte[4096];
		mtf16 = new int[16];
		selector = new byte[18002];
		selectorMtf = new byte[18002];
		len = new byte[6][258];
		limit = new int[6][258];
		base = new int[6][258];
		perm = new int[6][258];
		minLens = new int[6];
	}

	byte input[];
	int nextIn;
	int compressedSize;
	int totalInLo32;
	int totalInHi32;
	byte output[];
	int availOut;
	int decompressedSize;
	int totalOutLo32;
	int totalOutHi32;
	byte state_out_ch;
	int state_out_len;
	boolean randomized;
	int bsBuff;
	int bsLive;
	int blockSize_100k;
	int blockNo;
	int origPtr;
	int nextOut;
	int k0;
	final int[] unzftab;
	int nBlock_used;
	final int[] cftab;
	public static int ll8[];
	int nInUse;
	final boolean[] inUse;
	final boolean[] inUse16;
	final byte[] seqToUnseq;
	final byte[] yy;
	final int[] mtf16;
	final byte[] selector;
	final byte[] selectorMtf;
	final byte[][] len;
	final int[][] limit;
	final int[][] base;
	final int[][] perm;
	final int[] minLens;
	int nBlock_pp;
}