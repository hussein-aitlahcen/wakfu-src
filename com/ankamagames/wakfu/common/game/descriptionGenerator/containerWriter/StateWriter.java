package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;

public class StateWriter extends DefaultContainerWriter<State>
{
    public StateWriter(final State container, final CastableDescriptionGenerator.DescriptionMode mode) {
        this(container, mode, 0);
    }
    
    public StateWriter(final State container, final CastableDescriptionGenerator.DescriptionMode mode, final int freeDescriptionTranslationType) {
        super(container, container.getStateBaseId(), container.getLevel(), true, null, null, mode, freeDescriptionTranslationType);
    }
    
    public StateWriter(final State container, final CastableDescriptionGenerator.DescriptionMode mode, final short level) {
        super(container, container.getStateBaseId(), level, true, null, null, mode, 0);
    }
}
