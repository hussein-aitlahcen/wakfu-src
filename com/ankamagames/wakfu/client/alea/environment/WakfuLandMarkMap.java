package com.ankamagames.wakfu.client.alea.environment;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;

public class WakfuLandMarkMap extends LandMarkMap
{
    public static final int FILE_VERSION = 0;
    public static final int NUM_INTERACTIVE_ELEMENTS_MAX = 255;
    private static final Logger m_logger;
    private final TLongObjectHashMap<InteractiveElementDef> m_interactiveElements;
    
    public WakfuLandMarkMap() {
        super();
        this.m_interactiveElements = new TLongObjectHashMap<InteractiveElementDef>();
    }
    
    public byte getVersion() {
        return 0;
    }
    
    public TLongObjectHashMap<InteractiveElementDef> getInteractiveElements() {
        return this.m_interactiveElements;
    }
    
    public InteractiveElementDef getIE(final long id) {
        return this.m_interactiveElements.get(id);
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream, final boolean clear) throws IOException {
        super.load(istream, clear);
        this.loadInteractiveElements(istream, clear);
        this.compileAllCriteria();
    }
    
    private void loadInteractiveElements(final ExtendedDataInputStream istream, final boolean clear) throws IOException {
        if (clear) {
            this.m_interactiveElements.clear();
        }
        final int numInteractiveElt = istream.readShort();
        if (numInteractiveElt == 0) {
            return;
        }
        for (int i = 0; i < numInteractiveElt; ++i) {
            final InteractiveElementDef interactiveElements = new InteractiveElementDef();
            interactiveElements.load(istream);
            this.m_interactiveElements.put(interactiveElements.m_id, interactiveElements);
        }
    }
    
    private void compileAllCriteria() {
        final TIntObjectHashMap<LandMarkDef> defs = this.getLandMarkDef();
        final TIntObjectIterator<LandMarkDef> it = defs.iterator();
        while (it.hasNext()) {
            it.advance();
            final LandMarkDef def = it.value();
            if (def.m_descriptions == null) {
                continue;
            }
            for (int i = 0, size = def.m_descriptions.size(); i < size; ++i) {
                final LandMarkDescriptionDef descriptionDef = def.m_descriptions.get(i);
                try {
                    descriptionDef.m_criterion = CriteriaCompiler.compileBoolean(descriptionDef.m_criterionString);
                }
                catch (Exception e) {
                    WakfuLandMarkMap.m_logger.warn((Object)("Impossible de compiler le crit\u00e8re " + descriptionDef.m_criterionString + " sur la description de landMark d'id " + descriptionDef.m_id));
                }
            }
        }
    }
    
    public void addInteractiveElement(final ArrayList<InteractiveElementDef> list) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            final InteractiveElementDef def = list.get(i);
            this.m_interactiveElements.put(def.m_id, def);
        }
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        super.save(ostream);
        this.saveInteractiveElements(ostream);
    }
    
    private void saveInteractiveElements(final OutputBitStream ostream) throws IOException {
        final int numInteractiveElementsSystem = this.m_interactiveElements.size();
        if (numInteractiveElementsSystem > 32767) {
            throw new IllegalArgumentException("Nombre d'elements interactifs superieur \u00e0 32767");
        }
        ostream.writeShort((short)numInteractiveElementsSystem);
        final TLongObjectIterator<InteractiveElementDef> it = this.m_interactiveElements.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().save(ostream);
        }
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_interactiveElements.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuLandMarkMap.class);
    }
}
