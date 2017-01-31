package com.kit.game.cache.io;

import java.util.Arrays;

public final class ReferenceTable {

	private Archive archive;
	private int revision;
	private boolean named;
	private boolean usesWhirpool;
	private ArchiveReference[] archives;
	public int[] validArchiveIds;

	// editing
	private boolean updatedRevision;
	private boolean needsArchivesSort;

	protected ReferenceTable(Archive archive) {
		this.archive = archive;
		decodeHeader();
	}

	// public void setKeys(int[] keys) {
	// archive.setKeys(keys);
	// }

	// public int[] getKeys() {
	// return archive.getKeys();
	// }

	public void sortArchives() {
		Arrays.sort(validArchiveIds);
		needsArchivesSort = false;
	}

	public void addEmptyArchiveReference(int archiveId) {
		needsArchivesSort = true;
		int[] newValidArchiveIds = Arrays.copyOf(validArchiveIds, validArchiveIds.length + 1);
		newValidArchiveIds[newValidArchiveIds.length - 1] = archiveId;
		validArchiveIds = newValidArchiveIds;
		ArchiveReference reference;
		if (archives.length <= archiveId) {
			ArchiveReference[] newArchives = Arrays.copyOf(archives, archiveId + 1);
			reference = newArchives[archiveId] = new ArchiveReference();
			archives = newArchives;
		} else
			reference = archives[archiveId] = new ArchiveReference();
		reference.reset();
	}

	public void sortTable() {
		if (needsArchivesSort)
			sortArchives();
		for (int index = 0; index < validArchiveIds.length; index++) {
			ArchiveReference archive = archives[validArchiveIds[index]];
			if (archive.isNeedsFilesSort())
				archive.sortFiles();
		}
	}

	public int getProtocol() {
		if (archives.length > 65535)
			return 7;
		for (int index = 0; index < validArchiveIds.length; index++) {
			if (index > 0)
				if (validArchiveIds[index] - validArchiveIds[index - 1] > 65535)
					return 7;
			if (archives[validArchiveIds[index]].getValidFileIds().length > 65535)
				return 7;
		}
		return revision == 0 ? 5 : 6;
	}

	public void setRevision(int revision) {
		updatedRevision = true;
		this.revision = revision;
	}

	public void updateRevision() {
		if (updatedRevision)
			return;
		revision++;
		updatedRevision = true;
	}

	private void decodeHeader() {
		InputStream stream = new InputStream(archive.getData());
		int protocol = stream.readUnsignedByte();
		if (protocol < 5 || protocol > 7)
			throw new RuntimeException("INVALID PROTOCOL");
		if (protocol >= 6)
			revision = stream.readInt();
		int hash = stream.readUnsignedByte();
		named = (0x1 & hash) != 0;
		usesWhirpool = (0x2 & hash) != 0;
		int validArchivesCount = protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort();
		validArchiveIds = new int[validArchivesCount];
		int lastArchiveId = 0;
		int biggestArchiveId = 0;
		for (int index = 0; index < validArchivesCount; index++) {
			int archiveId = lastArchiveId += protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort();
			if (archiveId > biggestArchiveId)
				biggestArchiveId = archiveId;
			validArchiveIds[index] = archiveId;
		}
		archives = new ArchiveReference[biggestArchiveId + 1];
		for (int index = 0; index < validArchivesCount; index++) {
			archives[validArchiveIds[index]] = new ArchiveReference();
		}
		if (named) {
			for (int index = 0; index < validArchivesCount; index++) {
				archives[validArchiveIds[index]].setNameHash(stream.readInt());
			}
		}
		if (usesWhirpool) {
			for (int index = 0; index < validArchivesCount; index++) {
				byte[] whirpool = new byte[64];
				stream.getBytes(whirpool, 0, 64);
				// archives[validArchiveIds[index]].setWhirpool(whirpool);
			}
		}

		for (int index = 0; index < validArchivesCount; index++) {
			archives[validArchiveIds[index]].setCrc(stream.readInt());
		}

		for (int index = 0; index < validArchivesCount; index++) {
			archives[validArchiveIds[index]].setRevision(stream.readInt());
		}

		for (int index = 0; index < validArchivesCount; index++) {
			archives[validArchiveIds[index]].setValidFileIds(new int[protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort()]);
		}

		for (int index = 0; index < validArchivesCount; index++) {
			int lastFileId = 0;
			int biggestFileId = 0;
			ArchiveReference archive = archives[validArchiveIds[index]];
			for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++) {
				int fileId = lastFileId += protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort();
				if (fileId > biggestFileId)
					biggestFileId = fileId;
				archive.getValidFileIds()[index2] = fileId;
			}
			archive.setFiles(new FileReference[biggestFileId + 1]);
			for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++)
				archive.getFiles()[archive.getValidFileIds()[index2]] = new FileReference();
		}
		if (named) {
			for (int index = 0; index < validArchivesCount; index++) {
				ArchiveReference archive = archives[validArchiveIds[index]];
				for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++)
					archive.getFiles()[archive.getValidFileIds()[index2]].setNameHash(stream.readInt());
			}
		}
	}

	public int getRevision() {
		return revision;
	}

	public ArchiveReference[] getArchives() {
		return archives;
	}

	public ArchiveReference getArchive(int idx) {
		return archives[idx];
	}

	public int[] getValidArchiveIds() {
		return validArchiveIds;
	}

	public boolean isNamed() {
		return named;
	}

	public boolean usesWhirpool() {
		return usesWhirpool;
	}

	public int getCompression() {
		return archive.getCompression();
	}

}
