package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.skill.*;

public class SkillLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new SkillBinaryData(), new LoadProcedure<SkillBinaryData>() {
            @Override
            public void load(final SkillBinaryData data) {
                final ReferenceSkill skill = SkillLoader.loadFromBinaryForm(data);
                ReferenceSkillManager.getInstance().addReferenceSkill(skill);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.skill");
    }
    
    public static ReferenceSkill loadFromBinaryForm(final SkillBinaryData bs) {
        final int skillId = bs.getId();
        final int skillTypeId = bs.getType();
        final int[] associatedItemTypes = bs.getAssociatedItemTypes();
        final int[] associatedItems = bs.getAssociatedItems();
        final boolean skillInnate = bs.isInnate();
        final int scriptId = bs.getScriptId();
        final int maxLevel = bs.getMaxLevel();
        final SkillType skillType = SkillType.getFromId(skillTypeId);
        if (skillType == null) {
            SkillLoader.m_logger.error((Object)("Impossible de cr\u00e9er un skill de type " + skillTypeId + " : id de skill inconnu"));
        }
        return new ReferenceSkill(skillId, skillType, associatedItemTypes, associatedItems, maxLevel, skillInnate, scriptId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SkillLoader.class);
    }
}
