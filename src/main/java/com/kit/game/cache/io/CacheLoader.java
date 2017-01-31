package com.kit.game.cache.io;


public final class CacheLoader {

    private Decompressor decompressor;
    private Decompressor index255;
    private ReferenceTable table;
    private byte[][][] cachedFiles;

    protected CacheLoader(Decompressor index255, Decompressor Decompressor) {
        this.decompressor = Decompressor;
        this.index255 = index255;
        byte[] archiveData = index255.decompress(getId());
        if (archiveData == null)
            return;
        Archive archive = new Archive(getId(), archiveData);
        table = new ReferenceTable(archive);
        resetCachedFiles();
    }

    public void resetCachedFiles() {
        cachedFiles = new byte[getLastArchiveId() + 1][][];
    }

    public int getLastFileId(int archiveId) {
        if (!archiveExists(archiveId))
            return -1;
        return table.getArchives()[archiveId].getFiles().length - 1;
    }

    public int getLastArchiveId() {
        return table.getArchives().length - 1;
    }

    public int getValidArchivesCount() {
        return table.getValidArchiveIds().length;
    }

    public int getValidFilesCount(int archiveId) {
        if (!archiveExists(archiveId))
            return -1;
        return table.getArchives()[archiveId].getValidFileIds().length;
    }

    public boolean archiveExists(int archiveId) {
        if (archiveId < 0)
            return false;
        ArchiveReference[] archives = table.getArchives();
        return archives.length > archiveId && archives[archiveId] != null;
    }

    public boolean fileExists(int archiveId, int fileId) {
        if (!archiveExists(archiveId) || fileId < 0)
            return false;
        FileReference[] files = table.getArchives()[archiveId].getFiles();
        return files.length > fileId && files[fileId] != null;
    }

    public int getArchiveId(String name) {
        int nameHash = Utils.getNameHash(name);
        ArchiveReference[] archives = table.getArchives();
        int[] validArchiveIds = table.getValidArchiveIds();
        for (int archiveId : validArchiveIds) {
            if (archives[archiveId].getNameHash() == nameHash)
                return archiveId;
        }
        return -1;
    }

    public int getFileId(int archiveId, String name) {
        if (!archiveExists(archiveId))
            return -1;
        int nameHash = Utils.getNameHash(name);
        FileReference[] files = table.getArchives()[archiveId].getFiles();
        int[] validFileIds = table.getArchives()[archiveId].getValidFileIds();
        for (int index = 0; index < validFileIds.length; index++) {
            int fileId = validFileIds[index];
            if (files[fileId].getNameHash() == nameHash)
                return fileId;
        }
        return -1;
    }

    public byte[] getFile(int archiveId) {
        if (!archiveExists(archiveId))
            return null;
        return getFile(archiveId, table.getArchives()[archiveId].getValidFileIds()[0]);
    }

    public byte[] getFile(int archiveId, int fileId) {
        try {
            if (!fileExists(archiveId, fileId))
                return null;
            if (cachedFiles[archiveId] == null || cachedFiles[archiveId][fileId] == null)
                cacheArchiveFiles(archiveId);
            byte[] file = cachedFiles[archiveId][fileId];
            cachedFiles[archiveId][fileId] = null;
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //public void setKeys(int[] keys) {
    //	table.setKeys(keys);
    //}

    //public int[] getKeys() {
    //	return table.getKeys();
    //}

    private void cacheArchiveFiles(int archiveId) {
        Archive archive = getArchive(archiveId);
        int lastFileId = getLastFileId(archiveId);
        cachedFiles[archiveId] = new byte[lastFileId + 1][];
        if (archive == null)
            return;
        byte[] data = archive.getData();
        if (data == null)
            return;
        int filesCount = getValidFilesCount(archiveId);
        if (filesCount == 1)
            cachedFiles[archiveId][lastFileId] = data;
        else {
            int readPosition = data.length;
            int amtOfLoops = data[--readPosition] & 0xff;
            readPosition -= amtOfLoops * (filesCount * 4);
            InputStream stream = new InputStream(data);
            stream.setOffset(readPosition);
            int filesSize[] = new int[filesCount];
            for (int loop = 0; loop < amtOfLoops; loop++) {
                int offset = 0;
                for (int i = 0; i < filesCount; i++)
                    filesSize[i] += offset += stream.readInt();
            }
            byte[][] filesData = new byte[filesCount][];
            for (int i = 0; i < filesCount; i++) {
                filesData[i] = new byte[filesSize[i]];
                filesSize[i] = 0;
            }
            stream.setOffset(readPosition);
            int sourceOffset = 0;
            for (int loop = 0; loop < amtOfLoops; loop++) {
                int dataRead = 0;
                for (int i = 0; i < filesCount; i++) {
                    dataRead += stream.readInt();
                    System.arraycopy(data, sourceOffset, filesData[i], filesSize[i], dataRead);
                    sourceOffset += dataRead;
                    filesSize[i] += dataRead;
                }
            }
            int count = 0;
            for (int fileId : table.getArchives()[archiveId].getValidFileIds())
                cachedFiles[archiveId][fileId] = filesData[count++];
        }
    }

    public int getId() {
        return decompressor.getID();
    }

    public ReferenceTable getTable() {
        return table;
    }

    public Decompressor getDecompressor() {
        return decompressor;
    }

    public Archive getArchive(int id) {
        return decompressor.getArchive(id);
    }
}
