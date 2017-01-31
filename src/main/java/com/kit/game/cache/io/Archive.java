package com.kit.game.cache.io;

import com.kit.game.cache.util.bzip2.BZ2Decompressor;
import com.kit.game.cache.util.gzip.GZipDecompressor;
import com.kit.game.cache.util.bzip2.BZ2Decompressor;

public class Archive {

	private int id;
	private int revision;
	private int compression;
	private byte[] data;

	protected Archive(int id, byte[] archive) {
		this.id = id;
		decompress(archive);
	}

	public Archive(int id, int compression, int revision, byte[] data) {
		this.id = id;
		this.compression = compression;
		this.revision = revision;
		this.data = data;
	}

	private void decompress(byte[] compressed) {
		InputStream stream = new InputStream(compressed);
		compression = stream.readUnsignedByte();
		int compressedLength = stream.readInt();
		if (compressedLength < 0 || compressedLength > Constants.MAX_VALID_ARCHIVE_LENGTH) {
			throw new RuntimeException("Cache header is not valid");
		}
		switch (compression) {
			case Constants.NO_COMPRESSION: // no compression
				data = new byte[compressedLength];
				checkRevision(compressedLength, compressed, stream.getOffset());
				stream.readBytes(data, 0, compressedLength);
				break;
			case Constants.BZIP2_COMPRESSION: // bzip2
				int length = stream.readInt();
				if (length <= 0) {
					data = null;
					break;
				}
				data = new byte[length];
				checkRevision(compressedLength, compressed, stream.getOffset());
				BZ2Decompressor.decompress(data, data.length, compressed, compressedLength, 9);
				break;
			default: // gzip
				length = stream.readInt();
				if (length <= 0 || length > 1000000000) {
					data = null;
					break;
				}
				data = new byte[length];
				checkRevision(compressedLength, compressed, stream.getOffset());
				if (!GZipDecompressor.decompress(stream, data))
					data = null;
				break;
		}
	}

	private void checkRevision(int compressedLength, byte[] archive, int o) {
		InputStream stream = new InputStream(archive);
		int offset = stream.getOffset();
		if (stream.getLength() - (compressedLength + o) >= 2) {
			stream.setOffset(stream.getLength() - 2);
			revision = stream.readUnsignedShort();
			stream.setOffset(offset);
		} else
			revision = -1;

	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

	public int getCompression() {
		return compression;
	}

}
