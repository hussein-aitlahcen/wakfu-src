package com.ankamagames.framework.sound.group.field;

import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class PositionedStereoSound extends PositionedSound
{
    public PositionedStereoSound(final AudioSource source, final ObservedSource position, final float maxDistance, final boolean autoRelease, final float fadeDuration) {
        super(source, position, maxDistance, autoRelease, fadeDuration);
    }
    
    @Override
    public void setRelativePosition(final Vector3 position) {
        this.m_source.setPosition(0.0f, 0.0f, position.length());
    }
}
