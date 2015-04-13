package com.ankamagames.framework.fileFormat.io.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage.stream.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.handler.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.descriptor.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.nio.channels.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.regex.*;
import java.io.*;
import gnu.trove.*;

public final class BinaryStorage extends AbstractBinaryStorage
{
    private final TIntObjectHashMap<ArrayList<BinaryDataFileInfo>> m_metadatas;
    private final TIntObjectHashMap<TIntObjectHashMap<ArrayList<IndexEntry>>> m_indexes;
    private static final boolean DEFAULT_COMPRESSED_MODE = false;
    private final String m_workspace;
    private int MAX_SIZE_OF_FILE;
    private int MAX_ENTRIES_IN_FILE;
    private BinaryDataFileInfo m_currentDataFileAccess;
    private final Object m_mutex;
    private final StringBuilder m_sbuilder;
    private static final Pattern m_pathPattern;
    private boolean m_isInit;
    private final File m_tmpIndexFile;
    private final File m_tmpDataFile;
    private final File m_metadataFile;
    private final File m_workspaceDir;
    private final TLongObjectHashMap<File> m_indexesFiles;
    private final TIntObjectHashMap<File> m_dataFiles;
    private static final String DATA_FILE_PREFIX = "data.";
    private static final String DATA_FILE_EXT = ".bdat";
    private static final String INDEX_FILE_PREFIX = "index.";
    private static final String INDEX_FILE_EXT = ".bdat";
    private static final String METADATA_FILE_NAME = "metadata.bdat";
    private final IOStreamWrapper m_ioStreamWrapperData;
    private final IOStreamWrapper m_ioStreamWrapperIndex;
    private final IOStreamWrapper m_ioStreamWrapperMeta;
    
    protected BinaryStorage(final String workspace) {
        this(workspace, false);
    }
    
    private BinaryStorage(final String workspace, final boolean compressed) {
        super();
        this.m_metadatas = new TIntObjectHashMap<ArrayList<BinaryDataFileInfo>>();
        this.m_indexes = new TIntObjectHashMap<TIntObjectHashMap<ArrayList<IndexEntry>>>();
        this.MAX_SIZE_OF_FILE = 20000000;
        this.MAX_ENTRIES_IN_FILE = 500;
        this.m_mutex = new Object();
        this.m_sbuilder = new StringBuilder(20);
        this.m_indexesFiles = new TLongObjectHashMap<File>();
        this.m_dataFiles = new TIntObjectHashMap<File>();
        this.m_ioStreamWrapperIndex = IOStreamWrapper.checkOut(IOStreamWrapper.PLAIN);
        this.m_ioStreamWrapperMeta = IOStreamWrapper.checkOut(IOStreamWrapper.PLAIN);
        this.m_workspace = cleanPath(workspace);
        this.m_workspaceDir = new File(this.m_workspace);
        this.m_tmpIndexFile = new File(this.m_workspace + "~building_index.tmp");
        this.m_tmpDataFile = new File(this.m_workspace + "~building_data.tmp");
        this.m_metadataFile = new File(this.m_workspace + "metadata.bdat");
        this.setName("BinaryStorage (" + this.m_workspace + ")");
        this.m_ioStreamWrapperData = (compressed ? IOStreamWrapper.checkOut(IOStreamWrapper.COMPRESSED) : IOStreamWrapper.checkOut(IOStreamWrapper.PLAIN));
        if (this.init()) {
            this.start();
        }
        else {
            BinaryStorage.m_logger.error((Object)("Echec de l'initialisation du binary storage " + this));
        }
    }
    
    public static boolean existsOnDisk(final String workspace) {
        final File file = new File(cleanPath(workspace) + "metadata.bdat");
        return file.exists();
    }
    
    private static String cleanPath(final String path) {
        String wspExtension = BinaryStorage.m_pathPattern.matcher(path).replaceAll("_");
        if (wspExtension.charAt(0) == '/') {
            wspExtension = wspExtension.substring(1, wspExtension.length());
        }
        if (wspExtension.charAt(wspExtension.length() - 1) != '/') {
            wspExtension += "/";
        }
        return wspExtension;
    }
    
    public boolean isInit() {
        return this.m_isInit;
    }
    
    @Override
    protected String getCurrentWorkspacePath() {
        return this.m_workspace;
    }
    
    @Override
    protected boolean init() {
        synchronized (this.m_mutex) {
            if (this.m_isInit) {
                BinaryStorage.m_logger.error((Object)("Binary storage already initialize : " + this.m_workspace));
                return false;
            }
            try {
                if (this.m_workspaceDir.exists() && !this.m_workspaceDir.isDirectory()) {
                    BinaryStorage.m_logger.error((Object)("Tentative de changement de workspace [" + this.m_workspace + "] vers un fichier [not directory] existant [Aborted & Shutdown]"));
                    return false;
                }
                if (!this.m_workspaceDir.exists() && !this.m_workspaceDir.mkdirs()) {
                    BinaryStorage.m_logger.error((Object)("Impossible de creer l'arborescence de repertoire [" + this.m_workspace + "] lors d'un changement de workspace inexistant [Aborted & Shutdown]"));
                    return false;
                }
                this.m_metadatas.clear();
                if (!this.m_metadataFile.exists()) {
                    this.m_metadataFile.createNewFile();
                    BinaryStorage.m_logger.info((Object)"Fichier de meta donn\u00e9es non trouv\u00e9 pour le chargement de la source binaire : Creation d'une nouvelle source");
                }
                else {
                    FileInputStream fin = null;
                    DataInputStream in = null;
                    try {
                        fin = new FileInputStream(this.m_metadataFile);
                        in = this.m_ioStreamWrapperMeta.wrapInputStream(fin);
                        try {
                            while (true) {
                                final int typeId = in.readInt();
                                final int idFile = in.readInt();
                                final int size = in.readInt();
                                final int nb = in.readInt();
                                final int type = typeId;
                                ArrayList<BinaryDataFileInfo> files = this.m_metadatas.get(type);
                                if (files == null) {
                                    files = new ArrayList<BinaryDataFileInfo>();
                                    this.m_metadatas.put(type, files);
                                }
                                files.add(new BinaryDataFileInfo(type, idFile, size, nb));
                            }
                        }
                        catch (EOFException e4) {}
                    }
                    finally {
                        if (in != null) {
                            try {
                                in.close();
                            }
                            catch (IOException e) {
                                BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                            }
                        }
                        if (fin != null) {
                            try {
                                fin.close();
                            }
                            catch (IOException e) {
                                BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                            }
                        }
                    }
                }
            }
            catch (FileNotFoundException e2) {
                BinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
                return false;
            }
            catch (IOException e3) {
                BinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
                return false;
            }
            this.m_isInit = true;
            for (final BinaryStorageHandler hand : this.m_handlers) {
                hand.onInit(this, this.getCurrentWorkspacePath());
            }
            return true;
        }
    }
    
    private void writeMetadataFile() {
        try {
            FileOutputStream fout = null;
            DataOutputStream out = null;
            try {
                fout = new FileOutputStream(this.m_metadataFile, false);
                final DataOutputStream finalOut;
                out = (finalOut = this.m_ioStreamWrapperMeta.wrapOutputStream(fout));
                if (!this.m_metadatas.isEmpty()) {
                    this.m_metadatas.forEachEntry(new TIntObjectProcedure<ArrayList<BinaryDataFileInfo>>() {
                        @Override
                        public boolean execute(final int type, final ArrayList<BinaryDataFileInfo> binaryDataFileInfos) {
                            for (int i = 0; i < binaryDataFileInfos.size(); ++i) {
                                final BinaryDataFileInfo bfi = binaryDataFileInfos.get(i);
                                try {
                                    finalOut.writeInt(type);
                                    finalOut.writeInt(bfi.idFile);
                                    finalOut.writeInt(bfi.size);
                                    finalOut.writeInt(bfi.nbEntries);
                                }
                                catch (IOException e) {
                                    AbstractBinaryStorage.m_logger.error((Object)e.getMessage(), (Throwable)e);
                                }
                            }
                            return true;
                        }
                    });
                }
            }
            finally {
                if (out != null) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
                if (fout != null) {
                    try {
                        fout.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
            }
        }
        catch (FileNotFoundException e2) {
            BinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
        }
        catch (IOException e3) {
            BinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
        }
    }
    
    @Override
    protected void save(final BinaryStorable bs) {
        synchronized (this.m_mutex) {
            final ArrayList<IndexEntry> entries = this.search(BinaryStorage.DEFAULT_ID_INDEX_HASHCODE, bs.getBinaryType(), bs.getGlobalId(), 1);
            if (entries.size() <= 0) {
                this.saveNewEntry(bs);
            }
            else {
                this.updateEntry(bs, entries.get(0));
            }
        }
    }
    
    private void saveNewEntry(final BinaryStorable bs) {
        final byte[] data = bs.getBinaryData();
        if (data == null) {
            BinaryStorage.m_logger.error((Object)("Tentative de sauvegarde d'un binary storable qui n'a aucun bloc de donn\u00e9es " + bs));
            return;
        }
        final long checksum = bs.computeCheckSum(data);
        final int size = data.length + 4 + 2 + 4;
        this.selectAvailableDataFile(bs.getBinaryType(), size);
        try {
            final File file = this.m_currentDataFileAccess.file;
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fout = null;
            DataOutputStream out = null;
            long positionInFile;
            try {
                fout = new FileOutputStream(this.m_currentDataFileAccess.file, true);
                out = this.m_ioStreamWrapperData.wrapOutputStream(fout);
                positionInFile = fout.getChannel().size();
                final DataEntry entry = new DataEntry(bs.getGlobalId(), bs.getVersion(), data);
                entry.write(out);
            }
            finally {
                if (out != null) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
                if (fout != null) {
                    try {
                        fout.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
            }
            final BinaryDataFileInfo currentDataFileAccess = this.m_currentDataFileAccess;
            ++currentDataFileAccess.nbEntries;
            final BinaryDataFileInfo currentDataFileAccess2 = this.m_currentDataFileAccess;
            currentDataFileAccess2.size += size;
            this.addIndex(BinaryStorage.DEFAULT_ID_INDEX_HASHCODE, bs.getGlobalId(), bs.getBinaryType(), this.m_currentDataFileAccess.idFile, positionInFile, checksum);
            final Field[] arr$;
            final Field[] fields = arr$ = bs.getClass().getDeclaredFields();
            for (final Field field : arr$) {
                if (field.isAnnotationPresent(BinaryStorageIndex.class)) {
                    final BinaryStorageIndex annotation = field.getAnnotation(BinaryStorageIndex.class);
                    Object value;
                    if (field.isAccessible()) {
                        value = field.get(bs);
                    }
                    else {
                        field.setAccessible(true);
                        value = field.get(bs);
                        field.setAccessible(false);
                    }
                    this.addIndex(annotation.name().hashCode(), value, bs.getBinaryType(), this.m_currentDataFileAccess.idFile, positionInFile, checksum);
                }
            }
            this.writeMetadataFile();
        }
        catch (IOException e2) {
            BinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
        }
        catch (IllegalAccessException e3) {
            BinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
        }
    }
    
    private void updateEntry(final BinaryStorable bs, final IndexEntry entry) {
        final byte[] data = bs.getBinaryData();
        if (data == null) {
            BinaryStorage.m_logger.error((Object)("Tentative de sauvegarde d'un binary storable qui n'a aucun bloc de donn\u00e9es " + bs));
            return;
        }
        if (entry.checksum != bs.computeCheckSum(data)) {
            this.remove(bs, entry);
            this.saveNewEntry(bs);
        }
    }
    
    @Override
    protected void remove(final BinaryStorable bs) {
        synchronized (this.m_mutex) {
            final ArrayList<IndexEntry> entries = this.search(BinaryStorage.DEFAULT_ID_INDEX_HASHCODE, bs.getBinaryType(), bs.getGlobalId(), 1);
            if (!entries.isEmpty()) {
                this.remove(bs, entries.get(0));
            }
        }
    }
    
    private void remove(final BinaryStorable bs, final IndexEntry bse) {
        this.selectDataFile(bs.getBinaryType(), bse.idFile);
        try {
            FileInputStream fin = null;
            DataInputStream in = null;
            FileOutputStream fout = null;
            int sizeOfRemovedEntry;
            try {
                this.m_sbuilder.setLength(0);
                fin = new FileInputStream(this.m_currentDataFileAccess.file);
                in = this.m_ioStreamWrapperData.wrapInputStream(fin);
                final int fileSize = (int)fin.getChannel().size();
                fout = new FileOutputStream(this.m_tmpDataFile, false);
                fin.getChannel().position(bse.position);
                sizeOfRemovedEntry = DataEntry.skipEntry(in);
                final long positionToCuteOff = bse.position + sizeOfRemovedEntry;
                fin.getChannel().transferTo(0L, bse.position, fout.getChannel());
                fin.getChannel().transferTo(positionToCuteOff, fileSize - positionToCuteOff, fout.getChannel());
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
                if (fin != null) {
                    try {
                        fin.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
                if (fout != null) {
                    try {
                        fout.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
            }
            this.m_sbuilder.setLength(0);
            final File dataFile = this.m_currentDataFileAccess.file;
            if (dataFile.exists()) {
                dataFile.delete();
            }
            this.m_tmpDataFile.renameTo(dataFile);
            final BinaryDataFileInfo currentDataFileAccess = this.m_currentDataFileAccess;
            --currentDataFileAccess.nbEntries;
            final BinaryDataFileInfo currentDataFileAccess2 = this.m_currentDataFileAccess;
            currentDataFileAccess2.size -= sizeOfRemovedEntry;
            this.updateIndexAfterRemove(bse.idFile, bse.position, sizeOfRemovedEntry, bs);
            this.writeMetadataFile();
        }
        catch (FileNotFoundException e2) {
            BinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
        }
        catch (IOException e3) {
            BinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
        }
    }
    
    private void updateIndexAfterRemove(final int idFile, final long positionRemovedEntry, final long sizeRemovedEntry, final BinaryStorable removedBs) {
        try {
            final int type = removedBs.getBinaryType();
            final TIntObjectHashMap<ArrayList<IndexEntry>> localIndexes = this.m_indexes.get(type);
            if (localIndexes != null) {
                final TIntObjectIterator<ArrayList<IndexEntry>> it = localIndexes.iterator();
                while (it.hasNext()) {
                    it.advance();
                    FileOutputStream fout = null;
                    DataOutputStream out = null;
                    try {
                        fout = new FileOutputStream(this.m_tmpIndexFile, false);
                        out = this.m_ioStreamWrapperIndex.wrapOutputStream(fout);
                        final Iterator<IndexEntry> entries = it.value().iterator();
                        while (entries.hasNext()) {
                            final IndexEntry ientry = entries.next();
                            if (ientry.idFile == idFile && ientry.position > positionRemovedEntry) {
                                final IndexEntry indexEntry = ientry;
                                indexEntry.position -= sizeRemovedEntry;
                                ientry.write(out);
                            }
                            else if (ientry.idFile == idFile && ientry.position == positionRemovedEntry) {
                                entries.remove();
                                ientry.release();
                            }
                            else {
                                if (idFile == ientry.idFile && (idFile != ientry.idFile || ientry.position >= positionRemovedEntry)) {
                                    continue;
                                }
                                ientry.write(out);
                            }
                        }
                    }
                    finally {
                        if (out != null) {
                            try {
                                out.close();
                            }
                            catch (IOException e) {
                                BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                            }
                        }
                    }
                    final File indexFile = this.getIndexFile(it.key(), type);
                    if (indexFile.exists()) {
                        indexFile.delete();
                    }
                    this.m_tmpIndexFile.renameTo(indexFile);
                }
            }
            else {
                BinaryStorage.m_logger.error((Object)"Situation anormale : on met a jour des indexes qu'on a pas encore mont\u00e9 en memoire");
            }
        }
        catch (IOException e2) {
            BinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
        }
    }
    
    private BinaryStorable[] getByIndex(final ArrayList<IndexEntry> infos, final BinaryStorable bs) {
        if (infos.size() == 0) {
            return null;
        }
        final ArrayList<BinaryStorable> list = new ArrayList<BinaryStorable>();
        final int type = bs.getBinaryType();
        for (final IndexEntry bsentry : infos) {
            final int idFile = bsentry.idFile;
            final long position = bsentry.position;
            this.selectDataFile(type, idFile);
            try {
                this.m_sbuilder.setLength(0);
                final File file = this.m_currentDataFileAccess.file;
                if (!file.exists()) {
                    return null;
                }
                FileInputStream fin = null;
                DataInputStream in = null;
                DataEntry ientry = null;
                try {
                    fin = new FileInputStream(file);
                    in = this.m_ioStreamWrapperData.wrapInputStream(fin);
                    if (position < 0L) {
                        BinaryStorage.m_logger.fatal((Object)"position n\u00e9gative");
                        return null;
                    }
                    fin.getChannel().position(position);
                    ientry = new DataEntry();
                    ientry.read(in);
                }
                finally {
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (IOException e) {
                            BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                        }
                    }
                    if (fin != null) {
                        try {
                            fin.close();
                        }
                        catch (IOException e) {
                            BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                        }
                    }
                }
                final BinaryStorable nbs = bs.createInstance();
                final ByteBuffer data = ByteBuffer.wrap(ientry.getData());
                nbs.build(data, ientry.getId(), ientry.getVersion());
                if (data.remaining() != 0) {
                    BinaryStorage.m_logger.warn((Object)("Objet restaur\u00e9 du binary storage : " + data.remaining() + " bytes restants non lus [type:" + bs.getBinaryType() + " | id:" + ientry.getId() + "]"));
                }
                list.add(nbs);
                for (final BinaryStorageHandler hand : this.m_handlers) {
                    hand.onLoad(this, nbs);
                }
            }
            catch (IOException e2) {
                BinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
            }
        }
        if (list.size() > 0) {
            return list.toArray(new BinaryStorable[list.size()]);
        }
        return null;
    }
    
    @Override
    public BinaryStorable[] getByIndex(final String indexname, final Object value, final BinaryStorable bs) {
        return this.getByIndex(indexname.hashCode(), value, bs);
    }
    
    public BinaryStorable[] getByIndex(final int indexHashcode, final Object value, final BinaryStorable bs) {
        return this.getByIndex(indexHashcode, value, bs, Integer.MAX_VALUE);
    }
    
    public BinaryStorable[] getByIndex(final int indexHashcode, final Object value, final BinaryStorable bs, final int count) {
        synchronized (this.m_mutex) {
            return this.getByIndex(this.search(indexHashcode, bs.getBinaryType(), value, count), bs);
        }
    }
    
    @Override
    public BinaryStorable getById(final int value, final BinaryStorable bs) {
        final BinaryStorable[] bstab = this.getByIndex(BinaryStorage.DEFAULT_ID_INDEX_HASHCODE, value, bs, 1);
        if (bstab != null && bstab.length > 0) {
            return bstab[0];
        }
        return null;
    }
    
    @Override
    public BinaryStorable[] getAll(final BinaryStorable bs) {
        synchronized (this.m_mutex) {
            TIntObjectHashMap<ArrayList<IndexEntry>> type = this.m_indexes.get(bs.getBinaryType());
            if (type == null) {
                this.loadIndexesForType(bs.getBinaryType());
                type = this.m_indexes.get(bs.getBinaryType());
            }
            final ArrayList<IndexEntry> entries = type.get(BinaryStorage.DEFAULT_ID_INDEX_HASHCODE);
            if (entries == null || entries.isEmpty()) {
                return null;
            }
            return this.getByIndex(entries, bs);
        }
    }
    
    public void cleanUpFiles() {
        synchronized (this.m_mutex) {
            final File[] arr$;
            final File[] files = arr$ = this.m_workspaceDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(final File file, final String name) {
                    final int lastPointIndex = name.lastIndexOf(".");
                    if (lastPointIndex == -1) {
                        return false;
                    }
                    final String ext = name.substring(lastPointIndex);
                    return ".bdat".equalsIgnoreCase(ext) || ".bdat".equalsIgnoreCase(ext);
                }
            });
            for (final File f : arr$) {
                f.delete();
            }
        }
    }
    
    private void selectAvailableDataFile(final int type, final int size) {
        ArrayList<BinaryDataFileInfo> files = this.m_metadatas.get(type);
        BinaryDataFileInfo bfinfo = null;
        if (files == null) {
            bfinfo = new BinaryDataFileInfo(type, 1, 0, 0);
            files = new ArrayList<BinaryDataFileInfo>();
            files.add(bfinfo);
            this.m_metadatas.put(type, files);
        }
        int max_id = 0;
        for (final BinaryDataFileInfo bfi : files) {
            if (bfi.idFile > max_id) {
                max_id = bfi.idFile;
            }
            if ((this.MAX_ENTRIES_IN_FILE < 0 || bfi.nbEntries + 1 <= this.MAX_ENTRIES_IN_FILE) && (this.MAX_SIZE_OF_FILE < 0 || size + bfi.size <= this.MAX_SIZE_OF_FILE)) {
                bfinfo = bfi;
                break;
            }
        }
        if (bfinfo == null) {
            bfinfo = new BinaryDataFileInfo(type, max_id + 1, 0, 0);
            files.add(bfinfo);
        }
        this.selectDataFile(bfinfo);
    }
    
    private boolean selectDataFile(final int type, final int idFile) {
        ArrayList<BinaryDataFileInfo> files = this.m_metadatas.get(type);
        BinaryDataFileInfo bfinfo = null;
        if (files == null) {
            bfinfo = new BinaryDataFileInfo(type, 1, 0, 0);
            files = new ArrayList<BinaryDataFileInfo>();
            files.add(bfinfo);
            this.m_metadatas.put(type, files);
        }
        for (final BinaryDataFileInfo bfi : files) {
            if (bfi.idFile == idFile) {
                this.selectDataFile(bfi);
                return true;
            }
        }
        return false;
    }
    
    private void selectDataFile(final BinaryDataFileInfo bfi) {
        this.m_currentDataFileAccess = bfi;
    }
    
    private File getIndexFile(final int indexHashCode, final int type) {
        final long hashCode = MathHelper.getLongFromTwoInt(type, indexHashCode);
        File indexFile = this.m_indexesFiles.get(hashCode);
        if (indexFile != null) {
            return indexFile;
        }
        this.m_sbuilder.setLength(0);
        final String indexFileName = this.m_sbuilder.append(this.m_workspace).append("index.").append(type).append("_").append(indexHashCode).append(".bdat").toString();
        indexFile = new File(indexFileName);
        this.m_indexesFiles.put(hashCode, indexFile);
        return indexFile;
    }
    
    private void addIndex(final int hashCodeIndex, final Object value, final int type, final int idFileReference, final long position, final long checksum) {
        try {
            final File indexFile = this.getIndexFile(hashCodeIndex, type);
            if (!indexFile.exists()) {
                indexFile.createNewFile();
            }
            FileOutputStream fout = null;
            DataOutputStream out = null;
            IndexEntry entry = null;
            try {
                fout = new FileOutputStream(indexFile, true);
                out = this.m_ioStreamWrapperIndex.wrapOutputStream(fout);
                entry = IndexEntry.checkOut(value.toString(), idFileReference, position, checksum);
                entry.write(out);
            }
            finally {
                if (out != null) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
                if (fout != null) {
                    try {
                        fout.close();
                    }
                    catch (IOException e) {
                        BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
            }
            TIntObjectHashMap<ArrayList<IndexEntry>> indexes = this.m_indexes.get(type);
            if (indexes == null) {
                this.loadIndexesForType(type);
                indexes = this.m_indexes.get(type);
            }
            ArrayList<IndexEntry> entries = indexes.get(hashCodeIndex);
            if (entries == null) {
                entries = new ArrayList<IndexEntry>(50);
                indexes.put(hashCodeIndex, entries);
            }
            entries.add(entry);
        }
        catch (IOException e2) {
            BinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
        }
    }
    
    private ArrayList<IndexEntry> search(final int indexHashcode, final int type, final Object value, final int count) {
        TIntObjectHashMap<ArrayList<IndexEntry>> indexes = this.m_indexes.get(type);
        if (indexes == null) {
            this.loadIndexesForType(type);
            indexes = this.m_indexes.get(type);
        }
        final ArrayList<IndexEntry> found = new ArrayList<IndexEntry>();
        if (indexes != null) {
            final ArrayList<IndexEntry> entries = indexes.get(indexHashcode);
            if (entries != null) {
                final int size = entries.size();
                final String valueString = value.toString();
                for (int i = 0; i < size; ++i) {
                    final IndexEntry entry = entries.get(i);
                    if (entry.value.equals(valueString)) {
                        found.add(entry);
                        if (found.size() >= count) {
                            break;
                        }
                    }
                }
            }
        }
        return found;
    }
    
    private void loadIndexesForType(final int type) {
        final Pattern indexFilePattern = Pattern.compile("index.".replaceAll("\\.", "\\\\\\.") + type + "_([a-zA-Z0-9_.-]+)" + ".bdat".replaceAll("\\.", "\\\\\\."));
        final File[] files = this.m_workspaceDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File file, final String name) {
                return indexFilePattern.matcher(name).matches();
            }
        });
        TIntObjectHashMap<ArrayList<IndexEntry>> indexes = this.m_indexes.get(type);
        if (indexes == null) {
            indexes = new TIntObjectHashMap<ArrayList<IndexEntry>>();
            this.m_indexes.put(type, indexes);
        }
        for (final File file : files) {
            final Matcher m = indexFilePattern.matcher(file.getName());
            Label_0426: {
                if (m.matches()) {
                    int indexHashCode;
                    try {
                        indexHashCode = Integer.parseInt(m.group(1));
                    }
                    catch (NumberFormatException e4) {
                        BinaryStorage.m_logger.error((Object)("Nom de fichier d'index mal form\u00e9 : " + file.getName()));
                        break Label_0426;
                    }
                    try {
                        FileInputStream fin = null;
                        DataInputStream in = null;
                        try {
                            fin = new FileInputStream(file);
                            in = this.m_ioStreamWrapperIndex.wrapInputStream(fin);
                            try {
                                while (true) {
                                    final IndexEntry ientry = IndexEntry.checkOut();
                                    ientry.read(in);
                                    ArrayList<IndexEntry> entries = indexes.get(indexHashCode);
                                    if (entries == null) {
                                        entries = new ArrayList<IndexEntry>();
                                        indexes.put(indexHashCode, entries);
                                    }
                                    entries.add(ientry);
                                }
                            }
                            catch (EOFException e5) {}
                        }
                        finally {
                            if (in != null) {
                                try {
                                    in.close();
                                }
                                catch (IOException e) {
                                    BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                                }
                            }
                            if (fin != null) {
                                try {
                                    fin.close();
                                }
                                catch (IOException e) {
                                    BinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                                }
                            }
                        }
                    }
                    catch (FileNotFoundException e2) {
                        BinaryStorage.m_logger.error((Object)e2.getMessage());
                    }
                    catch (IOException e3) {
                        BinaryStorage.m_logger.error((Object)e3.getMessage());
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "BinaryStorage working under " + this.m_workspace;
    }
    
    static {
        m_pathPattern = Pattern.compile("[^a-zA-Z0-9-_/\\.]");
    }
    
    private class BinaryDataFileInfo
    {
        public final int idFile;
        public int size;
        public int nbEntries;
        public final File file;
        
        public BinaryDataFileInfo(final int type, final int id, final int size, final int nb) {
            super();
            this.idFile = id;
            this.size = size;
            this.nbEntries = nb;
            BinaryStorage.this.m_sbuilder.setLength(0);
            final String fileName = BinaryStorage.this.m_sbuilder.append(BinaryStorage.this.m_workspace).append("data.").append(type).append("_").append(this.idFile).append(".bdat").toString();
            final File fileAlreadyExists = BinaryStorage.this.m_dataFiles.get(fileName.hashCode());
            if (fileAlreadyExists != null) {
                this.file = fileAlreadyExists;
            }
            else {
                this.file = new File(fileName);
                BinaryStorage.this.m_dataFiles.put(fileName.hashCode(), this.file);
            }
        }
    }
}
