package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.client.binaryStorage.*;

public class ProtectorBuffLoader implements ContentInitializer
{
    protected static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        ProtectorBuffManager.INSTANCE.setBuffFactory(new ProtectorBuffFactory());
        this.loadBuffs();
        this.loadBuffLists();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private void loadBuffs() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ProtectorBuffBinaryData(), new LoadProcedure<ProtectorBuffBinaryData>() {
            @Override
            public void load(final ProtectorBuffBinaryData buffBs) {
                final int id = buffBs.getBuffId();
                final String criteria = buffBs.getCriteria();
                final byte origin = buffBs.getOrigin();
                final int[] effects = buffBs.getEffects();
                ProtectorBuffLoader.this.loadEffects(buffBs);
                try {
                    final SimpleCriterion criterion = CriteriaCompiler.compileBoolean(criteria);
                    final ProtectorBuff buff = (ProtectorBuff)ProtectorBuffManager.INSTANCE.addBuff(id, criterion, origin, effects);
                    buff.setGfxId(buffBs.getGfxId());
                    buff.setName(WakfuTranslator.getInstance().getString(50, id, new Object[0]));
                    buff.setDescription(WakfuTranslator.getInstance().getString(51, id, new Object[0]));
                }
                catch (Exception e) {
                    ProtectorBuffLoader.m_logger.error((Object)("Exception lev\u00e9e lors de l'interpr\u00e9tation du crit\u00e8re : " + criteria));
                }
            }
        });
    }
    
    private void loadEffects(final ProtectorBuffBinaryData bs) {
        for (final int effectId : bs.getEffects()) {
            final WakfuStandardEffect effect = (WakfuStandardEffect)EffectManager.getInstance().loadAndAddEffect(effectId);
            if (effect != null) {
                ProtectorBuffManager.INSTANCE.addEffect(effect);
            }
            else {
                ProtectorBuffLoader.m_logger.error((Object)("Probl\u00e8me de chargmeent de ProtectorBuff " + bs.getBuffId()));
            }
        }
    }
    
    private void loadBuffLists() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ProtectorBuffListBinaryData(), new LoadProcedure<ProtectorBuffListBinaryData>() {
            @Override
            public void load(final ProtectorBuffListBinaryData buffList) {
                final int listId = buffList.getBuffListId();
                final int[] buffIds = buffList.getBuffLists();
                ProtectorBuffManager.INSTANCE.addBuffList(listId, buffIds);
            }
        });
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.nationBuffs");
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorBuffLoader.class);
    }
}
