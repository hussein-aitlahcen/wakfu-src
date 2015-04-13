package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;
import java.util.concurrent.*;
import com.ankamagames.framework.kernel.utils.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.Index.*;
import java.util.*;

public final class AnmManager
{
    public static final Logger m_logger;
    private static final boolean DEBUG_DISPLAY_PRELOADING = true;
    private final HashMap<String, Anm> m_anms;
    private boolean m_reloadFailedAnms;
    private boolean m_asyncMode;
    private boolean m_keepTextureData;
    private final Semaphore m_mutex;
    private static final AnmManager m_instance;
    
    private AnmManager() {
        super();
        this.m_anms = new HashMap<String, Anm>();
        this.m_mutex = new Semaphore(1);
        this.m_asyncMode = true;
        this.m_keepTextureData = false;
        this.m_reloadFailedAnms = false;
    }
    
    public static AnmManager getInstance() {
        return AnmManager.m_instance;
    }
    
    public void setAsyncMode(final boolean async) {
        this.m_asyncMode = async;
    }
    
    public boolean getAsyncMode() {
        return this.m_asyncMode;
    }
    
    public boolean keepTextureData() {
        return this.m_keepTextureData;
    }
    
    public void setKeepTextureData(final boolean keepTextureData) {
        this.m_keepTextureData = keepTextureData;
    }
    
    public void loadAllCommonAnimations(final String indexFileName) {
        try {
            final ExtendedDataInputStream input = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(indexFileName));
            long fileSize = 0L;
            int fileCount = 0;
            for (int count = input.readInt(), i = 0; i < count; ++i) {
                try {
                    final int length = input.readShort() & 0xFFFF;
                    final byte[] name = input.readBytes(length);
                    final String filePath = StringUtils.fromUTF8(name);
                    final Anm anm = this.loadAnmFile(filePath, false);
                    anm.addReference();
                    AnmManager.m_logger.trace((Object)("pr\u00e9chargement des anms: " + filePath));
                    fileSize += ContentFileHelper.readFile(filePath).length;
                    ++fileCount;
                }
                catch (IOException e) {
                    AnmManager.m_logger.error((Object)"", (Throwable)e);
                }
            }
            input.close();
            AnmManager.m_logger.trace((Object)("pr\u00e9chargement des anms: " + fileCount + " fichiers (" + fileSize / 1024L + "ko)"));
        }
        catch (IOException e2) {
            AnmManager.m_logger.error((Object)"Error while loading Common Animations :", (Throwable)e2);
        }
    }
    
    public AnmInstance createInstanceFromAnm(final String fileName, final boolean async) throws IOException {
        final Anm defintion = this.loadAnmFile(fileName, async);
        if (defintion == null) {
            return null;
        }
        final String baseName = FileHelper.getNameWithoutExt(fileName);
        final String path = FileHelper.getPathWithSeparator(fileName);
        return new AnmInstance(defintion, baseName, path);
    }
    
    public Anm loadAnmFile(final String fileName, final boolean async) throws IOException {
        this.m_mutex.acquireUninterruptibly();
        Anm defintion;
        try {
            defintion = this.m_anms.get(fileName);
            if (defintion == null) {
                defintion = Anm.Factory.newPooledInstance();
                defintion.load(fileName, async);
                this.m_anms.put(fileName, defintion);
            }
        }
        finally {
            this.m_mutex.release();
        }
        return defintion;
    }
    
    public void forceDelete(final String fileName) {
        final Anm anm = this.m_anms.remove(fileName);
        if (anm == null) {
            return;
        }
        anm.removeReference();
    }
    
    public void setAnimation(final String anmFile, final AnmAnimationFileRecord animationFileRecord, final AnmInstance anmInstance) {
        assert anmInstance != null;
        assert animationFileRecord != null;
        final String filePath = anmInstance.getPath() + anmFile + ".anm";
        Anm anm = this.m_anms.get(filePath);
        if (anm == null) {
            try {
                anm = this.loadAnmFile(filePath, this.m_asyncMode);
            }
            catch (IOException e) {
                AnmManager.m_logger.error((Object)("Unable to load file " + filePath), (Throwable)e);
            }
        }
        if (anm == null) {
            anmInstance.setAnimation(null, null);
            return;
        }
        if (anm.isReady()) {
            anmInstance.setAnimation(anm, anm.getSpriteDefinitionByCRC(animationFileRecord.m_crc));
        }
        else {
            anmInstance.m_requestedAnimName = animationFileRecord.m_name;
            anmInstance.setAnimation(null, null);
        }
    }
    
    public void waitForAllReady() {
        this.update(0);
    }
    
    public void reloadFailedAnms() {
        this.m_mutex.acquireUninterruptibly();
        this.m_reloadFailedAnms = true;
        this.m_mutex.release();
    }
    
    public void update(final int timeIncrement) {
        this.m_mutex.acquireUninterruptibly();
        final Set<Map.Entry<String, Anm>> entries = this.m_anms.entrySet();
        final Iterator<Map.Entry<String, Anm>> iterator = entries.iterator();
        for (int i = this.m_anms.size(); i > 0; --i) {
            final Map.Entry<String, Anm> entry = iterator.next();
            final Anm anm = entry.getValue();
            if (this.m_reloadFailedAnms && anm.asyncLoadFailed()) {
                try {
                    anm.reload(true);
                }
                catch (IOException e) {
                    AnmManager.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            if (!anm.isReady()) {
                try {
                    anm.update();
                }
                catch (IOException e) {
                    AnmManager.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            else {
                anm.reduceLife();
            }
            if (anm.getLife() <= 0) {
                assert anm.getNumReferences() == 0;
                anm.removeReference();
                iterator.remove();
            }
        }
        this.m_reloadFailedAnms = false;
        this.m_mutex.release();
    }
    
    public void updateAndRemoveNow() {
        this.m_mutex.acquireUninterruptibly();
        final Set<Map.Entry<String, Anm>> entries = this.m_anms.entrySet();
        final Iterator<Map.Entry<String, Anm>> iterator = entries.iterator();
        for (int i = this.m_anms.size(); i > 0; --i) {
            final Map.Entry<String, Anm> entry = iterator.next();
            final Anm anm = entry.getValue();
            if (!anm.isReady()) {
                try {
                    anm.update();
                }
                catch (IOException e) {
                    AnmManager.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            else if (anm.getNumReferences() == 0) {
                anm.removeReference();
                iterator.remove();
            }
        }
        this.m_mutex.release();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmManager.class);
        m_instance = new AnmManager();
    }
}
