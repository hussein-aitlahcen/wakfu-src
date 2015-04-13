package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.core.utils.*;
import gnu.trove.*;

public class ResourceZoneComputer
{
    public static void execute() {
        FamilyZoneComputer.execute(EventType.FLORA_EVENT, new FamilyZoneComputer.Select() {
            @Override
            protected void fill() {
                ResourceManager.getInstance().foreachResource(new TObjectProcedure<Resource>() {
                    @Override
                    public boolean execute(final Resource resource) {
                        final int familyId = resource.getReferenceResource().getResourceType();
                        Select.this.add(familyId);
                        return true;
                    }
                });
            }
        });
    }
}
