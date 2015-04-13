package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

public class IsFacestabbed extends CheckInSightPartCriterion
{
    public IsFacestabbed(final ArrayList<ParserObject> args) {
        super();
        final byte type = this.checkType(args);
        switch (type) {
            case 0: {
                this.m_target = false;
                break;
            }
            case 1: {
                final String isTarget = args.get(0).getValue();
                if (isTarget.equalsIgnoreCase("target")) {
                    this.m_target = true;
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    protected boolean isInSightPartConcerned(final Part part) {
        return part != null && part.getPartId() == 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_FACE_STABBED;
    }
}
