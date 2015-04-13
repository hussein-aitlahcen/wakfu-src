package com.ankamagames.framework.fileFormat.io.binaryStorage;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.stream.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.descriptor.*;
import java.nio.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.handler.*;
import java.util.*;
import java.nio.channels.*;
import java.lang.annotation.*;
import java.io.*;
import java.lang.reflect.*;

public class AbstractSimpleBinaryStorage extends AbstractBinaryStorage
{
    private static final BinaryStorable[] EMPTY_STORABLES;
    private String m_baseWorkspace;
    private final TIntObjectHashMap<HashMap<String, ArrayList<ReadOnlyIndexEntry>>> m_indexes;
    private final Object m_fileAccessMutex;
    private final String m_dataFile;
    private final String m_indexFile;
    private final IOStreamWrapper m_ioStreamWrapperData;
    private static final IOStreamWrapper m_ioStreamWrapperIndex;
    private boolean m_isInit;
    
    public AbstractSimpleBinaryStorage(final String dataFile, final String indexFile, final boolean compressed) {
        super();
        this.m_baseWorkspace = null;
        this.m_indexes = new TIntObjectHashMap<HashMap<String, ArrayList<ReadOnlyIndexEntry>>>();
        this.m_fileAccessMutex = new Object();
        this.m_dataFile = dataFile;
        this.m_indexFile = indexFile;
        this.m_ioStreamWrapperData = (compressed ? IOStreamWrapper.checkOut(IOStreamWrapper.COMPRESSED) : IOStreamWrapper.checkOut(IOStreamWrapper.PLAIN));
        this.start();
    }
    
    public AbstractSimpleBinaryStorage(final String dataFile, final String indexFile) {
        this(dataFile, indexFile, false);
    }
    
    @Override
    public BinaryStorable getById(final int value, final BinaryStorable instanceType) {
        if (!this.isInit()) {
            AbstractSimpleBinaryStorage.m_logger.error((Object)"Tentative d'acces au (Simple)BinaryStorage alors qu'il n'est pas initialis\u00e9");
            return null;
        }
        final BinaryStorable[] bstab = this.getByIndex("id", value, instanceType);
        if (bstab != null && bstab.length > 0) {
            return bstab[0];
        }
        return null;
    }
    
    @Override
    public BinaryStorable[] getByIndex(final String indexname, final Object indexvalue, final BinaryStorable instanceType) {
        if (!this.isInit()) {
            AbstractSimpleBinaryStorage.m_logger.error((Object)"Tentative d'acces au (Simple)BinaryStorage alors qu'il n'est pas initialis\u00e9");
            return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
        }
        final LinkedList<BinaryStorable> list = new LinkedList<BinaryStorable>();
        synchronized (this.m_fileAccessMutex) {
            final HashMap<String, ArrayList<ReadOnlyIndexEntry>> index = this.m_indexes.get(instanceType.getBinaryType());
            if (index == null) {
                return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
            }
            if (index.get(indexname) == null) {
                return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
            }
            try {
                final File file = new File(this.m_baseWorkspace + this.m_dataFile);
                if (!file.exists()) {
                    return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
                }
                final String indexValueAsString = indexvalue.toString();
                for (final ReadOnlyIndexEntry entry : index.get(indexname)) {
                    if (entry.getIndexValue().equals(indexValueAsString)) {
                        FileInputStream fin = null;
                        DataInputStream in = null;
                        try {
                            fin = new FileInputStream(file);
                            in = this.m_ioStreamWrapperData.wrapInputStream(fin);
                            final FileChannel channel = fin.getChannel();
                            channel.position(entry.getPosition());
                            final DataEntry dentry = new DataEntry();
                            dentry.read(in);
                            final BinaryStorable nbs = instanceType.createInstance();
                            final ByteBuffer data = ByteBuffer.wrap(dentry.getData());
                            nbs.build(data, dentry.getId(), dentry.getVersion());
                            if (data.remaining() != 0) {
                                AbstractSimpleBinaryStorage.m_logger.warn((Object)("Objet restaur\u00e9 du simple binary storage : " + data.remaining() + " bytes restants non lus [type:" + instanceType.getBinaryType() + " | id:" + dentry.getId() + "]"));
                            }
                            list.add(nbs);
                            for (final BinaryStorageHandler hand : this.m_handlers) {
                                hand.onLoad(this, nbs);
                            }
                        }
                        finally {
                            if (in != null) {
                                try {
                                    in.close();
                                }
                                catch (IOException e) {
                                    AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                                }
                            }
                            if (fin != null) {
                                try {
                                    fin.close();
                                }
                                catch (IOException e) {
                                    AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                                }
                            }
                        }
                    }
                }
            }
            catch (FileNotFoundException e2) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
            }
            catch (IOException e3) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
            }
        }
        if (!list.isEmpty()) {
            return list.toArray(new BinaryStorable[list.size()]);
        }
        return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
    }
    
    @Override
    public BinaryStorable[] getAll(final BinaryStorable instanceType) {
        final long time = System.nanoTime();
        if (!this.isInit()) {
            AbstractSimpleBinaryStorage.m_logger.error((Object)"Tentative d'acces au (Simple)BinaryStorage alors qu'il n'est pas initialis\u00e9");
            return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
        }
        final LinkedList<BinaryStorable> list = new LinkedList<BinaryStorable>();
        synchronized (this.m_fileAccessMutex) {
            final HashMap<String, ArrayList<ReadOnlyIndexEntry>> index = this.m_indexes.get(instanceType.getBinaryType());
            if (index == null) {
                return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
            }
            try {
                final File file = new File(this.m_baseWorkspace + this.m_dataFile);
                if (!file.exists()) {
                    return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
                }
                for (final ReadOnlyIndexEntry entry : index.get("id")) {
                    FileInputStream fin = null;
                    DataInputStream in = null;
                    DataEntry dataentry;
                    try {
                        fin = new FileInputStream(file);
                        in = this.m_ioStreamWrapperData.wrapInputStream(fin);
                        fin.getChannel().position(entry.getPosition());
                        dataentry = new DataEntry();
                        dataentry.read(in);
                    }
                    finally {
                        if (in != null) {
                            try {
                                in.close();
                            }
                            catch (IOException e) {
                                AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                            }
                        }
                        if (fin != null) {
                            try {
                                fin.close();
                            }
                            catch (IOException e) {
                                AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                            }
                        }
                    }
                    final BinaryStorable nbs = instanceType.createInstance();
                    final ByteBuffer data = ByteBuffer.wrap(dataentry.getData());
                    nbs.build(data, dataentry.getId(), dataentry.getVersion());
                    if (data.remaining() != 0) {
                        AbstractSimpleBinaryStorage.m_logger.warn((Object)("Objet restaur\u00e9 du simple binary storage : " + data.remaining() + " bytes restants non lus [type:" + instanceType.getBinaryType() + " | id:" + dataentry.getId() + "]"));
                    }
                    list.add(nbs);
                    for (final BinaryStorageHandler hand : this.m_handlers) {
                        hand.onLoad(this, nbs);
                    }
                }
            }
            catch (FileNotFoundException e2) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
            }
            catch (IOException e3) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
            }
        }
        if (!list.isEmpty()) {
            return list.toArray(new BinaryStorable[list.size()]);
        }
        return AbstractSimpleBinaryStorage.EMPTY_STORABLES;
    }
    
    public void setWorkspace(final String wsp) {
        if (wsp != null) {
            String param = wsp;
            if (param.charAt(param.length() - 1) != '/') {
                param += "/";
            }
            this.m_isInit = false;
            this.m_baseWorkspace = param;
        }
    }
    
    public boolean isInit() {
        return this.m_isInit;
    }
    
    public boolean init() {
        assert this.m_baseWorkspace != null : "Il faut initialiser m_baseWorkspace";
        synchronized (this.m_fileAccessMutex) {
            try {
                final File dir = new File(this.m_baseWorkspace);
                if (dir.exists() && !dir.isDirectory()) {
                    throw new RuntimeException("Tentative de changement de workspace [" + this.m_baseWorkspace + "] vers un fichier [not directory] existant [Aborted & Shutdown]");
                }
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new RuntimeException("Impossible de creer l'arborescence de repertoire [" + this.m_baseWorkspace + "] lors d'un changement de workspace inexistant [Aborted & Shutdown]");
                }
                this.m_indexes.clear();
                final File file = new File(this.m_baseWorkspace + this.m_indexFile);
                if (!file.exists()) {
                    file.createNewFile();
                    AbstractSimpleBinaryStorage.m_logger.info((Object)"Fichier d'index non trouv\u00e9 pour le chargement de la source binaire : Creation d'une nouvelle source");
                    this.initialisationDone();
                    return true;
                }
                DataInputStream in = null;
                try {
                    in = AbstractSimpleBinaryStorage.m_ioStreamWrapperIndex.wrapInputStream(new FileInputStream(file));
                    try {
                        while (true) {
                            final ReadOnlyIndexEntry ientry = new ReadOnlyIndexEntry();
                            ientry.read(in);
                            this.registerEntry(ientry);
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
                            AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                        }
                    }
                }
            }
            catch (FileNotFoundException e2) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
            }
            catch (IOException e3) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
            }
            this.initialisationDone();
            return true;
        }
    }
    
    private void initialisationDone() {
        this.m_isInit = true;
        for (final BinaryStorageHandler hand : this.m_handlers) {
            hand.onInit(this, this.getCurrentWorkspacePath());
        }
    }
    
    private void registerEntry(final ReadOnlyIndexEntry entry) {
        HashMap<String, ArrayList<ReadOnlyIndexEntry>> indexes = this.m_indexes.get(entry.getType());
        if (indexes == null) {
            indexes = new HashMap<String, ArrayList<ReadOnlyIndexEntry>>(5);
            this.m_indexes.put(entry.getType(), indexes);
        }
        ArrayList<ReadOnlyIndexEntry> entries = indexes.get(entry.getIndexName());
        if (entries == null) {
            entries = new ArrayList<ReadOnlyIndexEntry>(300);
            indexes.put(entry.getIndexName(), entries);
        }
        entries.add(entry);
    }
    
    @Override
    protected void remove(final BinaryStorable bs) {
        AbstractSimpleBinaryStorage.m_logger.error((Object)"Remove call on a ReadOnlyBinaryStorage");
    }
    
    @Override
    protected void save(final BinaryStorable bs) {
        synchronized (this.m_fileAccessMutex) {
            final byte[] data = bs.getBinaryData();
            if (data == null) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)("Tentative de sauvegarde d'un binary storable qui n'a aucun bloc de donn\u00e9es " + bs));
                return;
            }
            try {
                final File file = new File(this.m_baseWorkspace + this.m_dataFile);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fout = null;
                DataOutputStream out = null;
                long positionInFile;
                try {
                    fout = new FileOutputStream(this.m_baseWorkspace + this.m_dataFile, true);
                    out = this.m_ioStreamWrapperData.wrapOutputStream(fout);
                    positionInFile = fout.getChannel().size();
                    final DataEntry dentry = new DataEntry(bs.getGlobalId(), bs.getVersion(), data);
                    dentry.write(out);
                }
                finally {
                    if (out != null) {
                        try {
                            out.close();
                        }
                        catch (IOException e) {
                            AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                        }
                    }
                    if (fout != null) {
                        try {
                            fout.close();
                        }
                        catch (IOException e) {
                            AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                        }
                    }
                }
                this.addIndex("id", bs.getGlobalId(), bs.getBinaryType(), positionInFile);
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
                        this.addIndex(annotation.name(), value, bs.getBinaryType(), positionInFile);
                    }
                }
            }
            catch (IOException e2) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e2.getMessage(), (Throwable)e2);
            }
            catch (IllegalAccessException e3) {
                AbstractSimpleBinaryStorage.m_logger.error((Object)e3.getMessage(), (Throwable)e3);
            }
        }
    }
    
    private void addIndex(final String name, final Object value, final int type, final long position) {
        try {
            DataOutputStream out = null;
            try {
                out = AbstractSimpleBinaryStorage.m_ioStreamWrapperIndex.wrapOutputStream(new FileOutputStream(this.m_baseWorkspace + this.m_indexFile, true));
                final ReadOnlyIndexEntry entry = new ReadOnlyIndexEntry(type, name, value.toString(), position);
                entry.write(out);
                this.registerEntry(entry);
            }
            finally {
                if (out != null) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {
                        AbstractSimpleBinaryStorage.m_logger.fatal((Object)"Impossible de fermer le descripteur ouvert sur un fichier !", (Throwable)e);
                    }
                }
            }
        }
        catch (IOException e2) {
            AbstractSimpleBinaryStorage.m_logger.error((Object)e2.getMessage());
        }
    }
    
    @Override
    protected String getCurrentWorkspacePath() {
        return this.m_baseWorkspace;
    }
    
    public void cleanUpFiles() {
        synchronized (this.m_fileAccessMutex) {
            System.out.println("cleanUpFiles m_baseWorkspace " + this.m_baseWorkspace);
            final File indexFile = new File(this.m_baseWorkspace + this.m_indexFile);
            final File dataFile = new File(this.m_baseWorkspace + this.m_dataFile);
            indexFile.delete();
            dataFile.delete();
            this.m_indexes.clear();
        }
    }
    
    static {
        EMPTY_STORABLES = new BinaryStorable[0];
        m_ioStreamWrapperIndex = IOStreamWrapper.checkOut(IOStreamWrapper.PLAIN);
    }
}
