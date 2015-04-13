package com.ankamagames.framework.sound.group.field;

import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class PositionedMonoSound extends PositionedSound
{
    public PositionedMonoSound(final AudioSource source, final ObservedSource position, final float maxDistance, final boolean autoRelease, final float fadeDuration) {
        super(source, position, maxDistance, autoRelease, fadeDuration);
    }
    
    @Override
    public void setRelativePosition(final Vector3 position) {
        final float x = position.getX();
        final float pan = (x > 2.0f) ? (x - 2.0f) : ((x < -2.0f) ? (x + 2.0f) : 0.0f);
        final float y = position.getY();
        final float vectY = (y > 2.0f) ? (y - 2.0f) : ((y < -2.0f) ? (y + 2.0f) : 0.0f);
        final float z = position.getZ();
        final float vectZ = (z > 2.0f) ? (z - 2.0f) : ((z < -2.0f) ? (z + 2.0f) : 1.0f);
        this.m_source.setPosition(pan, vectY, vectZ);
    }
}
