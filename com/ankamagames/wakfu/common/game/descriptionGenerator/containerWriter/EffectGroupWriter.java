package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;

public class EffectGroupWriter extends DefaultContainerWriter<AbstractEffectGroup>
{
    public EffectGroupWriter(final AbstractEffectGroup container, final int effectId, final short containerLevel, final int freeDescriptionTranslationType) {
        super(container, effectId, containerLevel, true, null, null, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, freeDescriptionTranslationType);
    }
}
