package com.ankamagames.wakfu.client.ui.mru.MRUDecorators;

import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;

public class MRUCriminalParticleDecorator extends ParticleDecorator
{
    public MRUCriminalParticleDecorator(final String criminalParticle, final int level) {
        super();
        this.onCheckOut();
        this.setAlignment(Alignment9.CENTER);
        this.setLevel(level);
        this.setFile(criminalParticle);
    }
    
    public MRUCriminalParticleDecorator(final String criminalParticle) {
        this(criminalParticle, 1);
    }
}
