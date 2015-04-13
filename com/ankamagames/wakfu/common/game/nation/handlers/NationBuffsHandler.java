package com.ankamagames.wakfu.common.game.nation.handlers;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.data.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import java.nio.*;
import gnu.trove.*;

public abstract class NationBuffsHandler extends NationHandler<NationBuffEventHandler>
{
    protected static final Logger m_logger;
    @Nullable
    private ProtectorBuffsPart m_protectorBuffsPart;
    @Nullable
    private final TIntObjectHashMap<IntArray> m_buffsPerProtector;
    private int m_disableBuffEventsDispatch;
    private final List<NationBuffEventHandler> m_buffsEventHandlers;
    
    protected NationBuffsHandler(final Nation nation) {
        super(nation);
        this.m_protectorBuffsPart = null;
        this.m_buffsPerProtector = new TIntObjectHashMap<IntArray>();
        this.m_disableBuffEventsDispatch = 0;
        this.m_buffsEventHandlers = new ArrayList<NationBuffEventHandler>();
    }
    
    @Override
    public void finishInitialization() {
    }
    
    void dispatchBuffsUpdateEvent(final int protectorId) {
        if (this.m_disableBuffEventsDispatch > 0) {
            return;
        }
        final NationBuffEventHandler[] handlers = new NationBuffEventHandler[this.m_buffsEventHandlers.size()];
        this.m_buffsEventHandlers.toArray(handlers);
        for (int i = handlers.length - 1; i >= 0; --i) {
            handlers[i].onNationBuffsChanged(protectorId, this.getNation());
        }
    }
    
    @Override
    public void registerEventHandler(final NationBuffEventHandler handler) {
        if (handler == null) {
            return;
        }
        this.m_buffsEventHandlers.add(handler);
    }
    
    @Override
    public void unregisterEventHandler(final NationBuffEventHandler handler) {
        this.m_buffsEventHandlers.remove(handler);
    }
    
    public abstract void requestAddProtectorBuffs(final int p0, final int[] p1);
    
    public abstract void requestClearProtectorBuffs(final int p0);
    
    public abstract void requestReplaceProtectorBuffs(final int p0, final int[] p1);
    
    public abstract void requestRemoveProtectorBuffs(final int p0, final int[] p1);
    
    void disableEventsDispatch() {
        ++this.m_disableBuffEventsDispatch;
    }
    
    void enableEventsDispatch() {
        --this.m_disableBuffEventsDispatch;
    }
    
    @Nullable
    public IntArray getProtectorBuffs(final int protectorId) {
        return this.m_buffsPerProtector.get(protectorId);
    }
    
    @Nullable
    public IntArray getAllProtectorsBuffs() {
        if (this.m_buffsPerProtector.isEmpty()) {
            return null;
        }
        return this.getAllFilteredProtectorBuffs();
    }
    
    private IntArray getAllFilteredProtectorBuffs() {
        final IntArray nationBuffsList = new IntArray(this.m_buffsPerProtector.size());
        int score = 0;
        final boolean isRiktus = this.getNation().getNationId() == 34;
        final TIntObjectIterator<IntArray> it = this.m_buffsPerProtector.iterator();
        while (it.hasNext()) {
            it.advance();
            final IntArray subList = it.value();
            if (isRiktus) {
                for (int i = 0, size = subList.size(); i < size; ++i) {
                    final int buffId = subList.getQuick(i);
                    if (!this.isGeneralBuff(buffId) || score++ < 8) {
                        nationBuffsList.put(buffId);
                    }
                }
            }
            else {
                nationBuffsList.put(subList);
            }
        }
        return nationBuffsList;
    }
    
    private boolean isGeneralBuff(final int buffId) {
        final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(buffId);
        final Iterator<WakfuEffect> it = buff.iterator();
        int score = 0;
        while (it.hasNext()) {
            final WakfuEffect effect = it.next();
            if (effect.getActionId() == RunningEffectConstants.DMG_GAIN_IN_PERCENT.getId() || effect.getActionId() == RunningEffectConstants.HP_BOOST.getId()) {
                ++score;
            }
        }
        return score >= 2;
    }
    
    protected void addProtectorBuffs(final int protectorId, final int[] buffsId) {
        final IntArray buffs = this.getOrCreateProtectorBuffs(protectorId);
        for (int buffsCount = buffsId.length, i = 0; i < buffsCount; ++i) {
            buffs.put(buffsId[i]);
        }
        this.forceBuffsPartReserialization();
        this.dispatchBuffsUpdateEvent(protectorId);
    }
    
    protected void removeProtectorBuffs(final int protectorId, final int[] buffsId) {
        final IntArray buffs = this.getProtectorBuffs(protectorId);
        if (buffs == null) {
            return;
        }
        final int buffsToRemoveCount = buffsId.length;
        final int buffsCount = buffs.size();
        for (int i = buffsCount - 1; i >= 0; --i) {
            for (int j = 0; j < buffsToRemoveCount; ++j) {
                if (buffs.getQuick(i) == buffsId[j]) {
                    buffs.remove(i);
                    break;
                }
            }
        }
        this.forceBuffsPartReserialization();
        this.dispatchBuffsUpdateEvent(protectorId);
    }
    
    protected void clearProtectorBuffs(final int protectorId) {
        final IntArray buffs = this.getProtectorBuffs(protectorId);
        if (buffs == null || buffs.size() == 0) {
            return;
        }
        buffs.clear();
        this.forceBuffsPartReserialization();
        this.dispatchBuffsUpdateEvent(protectorId);
    }
    
    protected void replaceProtectorBuffs(final int protectorId, final int[] buffsId) {
        if (buffsId == null || buffsId.length == 0) {
            this.clearProtectorBuffs(protectorId);
            return;
        }
        final IntArray currentBuffs = this.getOrCreateProtectorBuffs(protectorId);
        if (this.checkBuffUnchanged(buffsId, currentBuffs)) {
            return;
        }
        currentBuffs.clear();
        for (int buffsCount = buffsId.length, i = 0; i < buffsCount; ++i) {
            currentBuffs.put(buffsId[i]);
        }
        this.forceBuffsPartReserialization();
        this.dispatchBuffsUpdateEvent(protectorId);
    }
    
    private boolean checkBuffUnchanged(final int[] newBuffs, @NotNull final IntArray oldBuffs) {
        if (oldBuffs.size() == 0) {
            return newBuffs == null || newBuffs.length == 0;
        }
        if (newBuffs == null || newBuffs.length != oldBuffs.size()) {
            return false;
        }
        boolean buffUnchanged = true;
        for (int i = 0; i < oldBuffs.size(); ++i) {
            final int buffId = oldBuffs.get(i);
            boolean buffFound = false;
            for (int j = 0; j < newBuffs.length; ++j) {
                if (newBuffs[j] == buffId) {
                    buffFound = true;
                    break;
                }
            }
            if (!buffFound) {
                buffUnchanged = false;
                break;
            }
        }
        return buffUnchanged;
    }
    
    @NotNull
    protected IntArray getOrCreateProtectorBuffs(final int protectorId) {
        IntArray stack = this.m_buffsPerProtector.get(protectorId);
        if (stack != null) {
            return stack;
        }
        stack = new IntArray();
        this.m_buffsPerProtector.put(protectorId, stack);
        return stack;
    }
    
    private void forceBuffsPartReserialization() {
        if (this.m_protectorBuffsPart != null) {
            this.m_protectorBuffsPart.forceReserialization();
        }
    }
    
    @NotNull
    public NationPart getProtectorBuffsPart() {
        if (this.m_protectorBuffsPart == null) {
            this.m_protectorBuffsPart = new ProtectorBuffsPart();
        }
        return this.m_protectorBuffsPart;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationBuffsHandler.class);
    }
    
    public class ProtectorBuffsPart extends NationPart
    {
        private byte[] m_serializedData;
        
        public ProtectorBuffsPart() {
            super();
            this.m_serializedData = null;
        }
        
        void forceReserialization() {
            this.m_serializedData = null;
        }
        
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            NationBuffsHandler.this.disableEventsDispatch();
            final short protectorsCount = buffer.getShort();
            final TIntArrayList protectorIds = new TIntArrayList();
            for (int p = 0; p < protectorsCount; ++p) {
                final int protectorId = buffer.getInt();
                final int buffsCount = buffer.getInt();
                final int[] buffs = new int[buffsCount];
                for (int i = 0; i < buffsCount; ++i) {
                    buffs[i] = buffer.getInt();
                }
                final IntArray currentBuffs = NationBuffsHandler.this.m_buffsPerProtector.get(protectorId);
                if (currentBuffs == null || !NationBuffsHandler.this.checkBuffUnchanged(buffs, currentBuffs)) {
                    NationBuffsHandler.this.m_buffsPerProtector.remove(protectorId);
                    NationBuffsHandler.this.addProtectorBuffs(protectorId, buffs);
                    if (!protectorIds.contains(protectorId)) {
                        protectorIds.add(protectorId);
                    }
                }
            }
            NationBuffsHandler.this.enableEventsDispatch();
            for (int j = 0, size = protectorIds.size(); j < size; ++j) {
                NationBuffsHandler.this.dispatchBuffsUpdateEvent(protectorIds.getQuick(j));
            }
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            if (this.m_serializedData == null) {
                this.m_serializedData = new byte[this.serializedSize()];
                final ByteBuffer tmpBuffer = ByteBuffer.wrap(this.m_serializedData);
                tmpBuffer.putShort((short)NationBuffsHandler.this.m_buffsPerProtector.size());
                final TIntObjectIterator<IntArray> iterator = NationBuffsHandler.this.m_buffsPerProtector.iterator();
                while (iterator.hasNext()) {
                    iterator.advance();
                    tmpBuffer.putInt(iterator.key());
                    final IntArray buffs = iterator.value();
                    final int buffsCount = buffs.size();
                    tmpBuffer.putInt(buffsCount);
                    for (int i = 0; i < buffsCount; ++i) {
                        tmpBuffer.putInt(buffs.getQuick(i));
                    }
                }
                tmpBuffer.flip();
                buffer.put(tmpBuffer);
                return;
            }
            assert buffer.remaining() >= this.m_serializedData.length : "Probl\u00e8me \u00e0 la s\u00e9rialisation : on veut mettre des data s\u00e9rialis\u00e9es d'une taille de " + this.m_serializedData.length + " dans un buffer ou il ne reste que " + buffer.remaining() + " octets";
            buffer.put(this.m_serializedData);
        }
        
        @Override
        public int serializedSize() {
            if (this.m_serializedData != null) {
                return this.m_serializedData.length;
            }
            int size = 2 + NationBuffsHandler.this.m_buffsPerProtector.size() * 8;
            final TIntObjectIterator<IntArray> iterator = NationBuffsHandler.this.m_buffsPerProtector.iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                final IntArray buffs = iterator.value();
                size += 4 * buffs.size();
            }
            return size;
        }
    }
}
