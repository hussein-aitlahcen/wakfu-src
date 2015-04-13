package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.exporter.*;
import com.ankamagames.framework.sound.openAL.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.framework.sound.group.field.*;

public class FieldSoundContainer extends SoundContainer<FieldSourceGroup>
{
    private boolean m_isPseudoLocalized;
    private ObservedSource m_soundSource;
    private int m_rollOff;
    
    public FieldSoundContainer() {
        super();
        this.m_isPseudoLocalized = false;
    }
    
    @Override
    protected SoundContainer newInstance() {
        return new FieldSoundContainer();
    }
    
    @Override
    protected void copy(final AbstractSoundContainer container) {
        super.copy(container);
        final FieldSoundContainer fieldSoundContainer = (FieldSoundContainer)container;
        fieldSoundContainer.m_isPseudoLocalized = this.m_isPseudoLocalized;
        fieldSoundContainer.m_rollOff = this.m_rollOff;
        fieldSoundContainer.m_soundSource = this.m_soundSource;
    }
    
    public ObservedSource getSoundSource() {
        return this.m_soundSource;
    }
    
    public void setSoundSource(final ObservedSource soundSource) {
        this.m_soundSource = soundSource;
    }
    
    public int getRollOff() {
        return this.m_rollOff;
    }
    
    public void setRollOff(final int rollOff) {
        this.m_rollOff = rollOff;
    }
    
    public boolean isPseudoLocalized() {
        return this.m_isPseudoLocalized;
    }
    
    public void setPseudoLocalized(final boolean pseudoLocalized) {
        this.m_isPseudoLocalized = pseudoLocalized;
    }
    
    @Override
    protected void fillRawSoundContainer(final RawSoundContainer rsc) {
        super.fillRawSoundContainer(rsc);
        rsc.m_rollOffId = this.m_rollOff;
    }
    
    @Override
    protected AudioSource createAudioSource(final long sourceId, final boolean loop) {
        if (!((FieldSourceGroup)this.m_group).canPrepareSounds()) {
            return null;
        }
        AudioStreamProvider asp;
        try {
            if (((FieldSourceGroup)this.m_group).getHelper() == null) {
                return null;
            }
            asp = ((FieldSourceGroup)this.m_group).getHelper().fromId(sourceId);
        }
        catch (IOException e) {
            return null;
        }
        if (asp == null) {
            return null;
        }
        ObservedSource source;
        if (this.m_isPseudoLocalized) {
            final float x = MathHelper.random(6, 9) * (MathHelper.randomBoolean() ? -1 : 1);
            final float y = MathHelper.random(6, 9) * (MathHelper.randomBoolean() ? -1 : 1);
            source = new StaticPositionProvider(x, y, 0.0f, true, 0);
        }
        else {
            source = this.m_soundSource;
            if (source == null) {
                FieldSoundContainer.m_logger.error((Object)("On essaye de cr\u00e9er une source audio sans avoir d'ObservedSource dans le container " + this.getName() + " (" + this.getId() + "). V\u00e9rifier le param\u00e9trage du container"));
                return null;
            }
        }
        RollOffParameter param = null;
        if (this.m_rollOff >= 0) {
            param = ((FieldSourceGroup)this.m_group).getManager().getRollOffParameter(this.m_rollOff);
        }
        if (param == null) {
            param = RollOffParameter.DEFAULT;
        }
        final PositionedSound ps = ((FieldSourceGroup)this.m_group).addSource(asp, 1.0f, source, param.getRefDistance(), param.getMaxDistance(), param.getRollOffFactor(), 0, 0, loop, false, false, 0.0f, -1L);
        if (ps == null) {
            return null;
        }
        return ps.getSource();
    }
}
