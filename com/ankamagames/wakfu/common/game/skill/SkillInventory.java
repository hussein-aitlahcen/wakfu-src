package com.ankamagames.wakfu.common.game.skill;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;

public class SkillInventory<S extends AbstractSkill> extends StackInventory<S, RawSkill> implements RawConvertible<RawSkillInventory>
{
    private static final Logger m_logger;
    
    public SkillInventory(final InventoryContentProvider<S, RawSkill> contentProvider) {
        super((short)32767, contentProvider, null, false, false, false);
    }
    
    @Nullable
    public S getSkillByAssociatedItem(final SkillType skillType, final short itemTypeId) {
        for (final S skill : this) {
            if (skill.getReferenceSkill().getType() == skillType && skill.getReferenceSkill().getAssociatedItemTypes().contains(itemTypeId)) {
                return skill;
            }
        }
        return null;
    }
    
    public List<S> getSkillsFromType(final SkillType skillType) {
        final ArrayList<S> skills = new ArrayList<S>();
        for (final S skill : this) {
            if (skill.getReferenceSkill().getType() == skillType) {
                skills.add(skill);
            }
        }
        return skills;
    }
    
    @Override
    public boolean fromRaw(final RawSkillInventory raw) {
        if (this.m_serializeQuantity) {
            SkillInventory.m_logger.warn((Object)"Impossible d'ajouter les quantit\u00e9s depuis un RawSkillInventory qui ne connait pas cette information");
        }
        this.destroyAll();
        boolean bOk = true;
        S skill = null;
        for (final RawSkillInventory.Content content : raw.contents) {
            try {
                skill = (S)this.m_contentProvider.unSerializeContent((R)content.skill);
                if (skill != null) {
                    if (this.add(skill)) {
                        continue;
                    }
                    SkillInventory.m_logger.error((Object)("Impossible d'ajouter un skill (" + skill.getReferenceId() + ") au SkillInventory"));
                    bOk = false;
                    skill.release();
                }
                else {
                    bOk = false;
                }
            }
            catch (InventoryCapacityReachedException e) {
                SkillInventory.m_logger.error((Object)ExceptionFormatter.toString(e));
                bOk = false;
                skill.release();
            }
            catch (ContentAlreadyPresentException e2) {
                SkillInventory.m_logger.error((Object)ExceptionFormatter.toString(e2));
                bOk = false;
                skill.release();
            }
        }
        return bOk;
    }
    
    @Override
    public boolean toRaw(final RawSkillInventory raw) {
        if (this.m_serializeQuantity) {
            SkillInventory.m_logger.warn((Object)"Impossible d'ajouter l'information de quantit\u00e9 \u00e0 un RawSkillInventory qui n'est pas pr\u00e9vu pour");
        }
        raw.clear();
        for (final S skill : this) {
            if (skill.shouldBeSerialized()) {
                final RawSkillInventory.Content content = new RawSkillInventory.Content();
                if (!skill.toRaw(content.skill)) {
                    return false;
                }
                raw.contents.add(content);
            }
        }
        return true;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId) {
        return 0;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId, final int count) {
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SkillInventory.class);
    }
}
