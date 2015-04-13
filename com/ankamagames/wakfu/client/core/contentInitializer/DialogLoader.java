package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.dialog.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class DialogLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new DialogBinaryData(), new LoadProcedure<DialogBinaryData>() {
            @Override
            public void load(final DialogBinaryData data) {
                final Dialog dialog = new Dialog(data.getId());
                DialogManager.INSTANCE.addDialog(dialog);
                for (final DialogBinaryData.Answer answer : data.getAnswers()) {
                    final int choiceId = answer.getId();
                    Label_0152: {
                        SimpleCriterion choiceCriterion;
                        try {
                            choiceCriterion = CriteriaCompiler.compileBoolean(answer.getCriterion());
                        }
                        catch (Exception e) {
                            DialogLoader.m_logger.error((Object)("[LD] Impossible de compiler le crit\u00e8re " + answer.getCriterion() + " sur la r\u00e9ponse " + choiceId + " du dialog " + data.getId()), (Throwable)e);
                            break Label_0152;
                        }
                        final DialogChoice choice = new DialogChoice(choiceId, choiceCriterion, (byte)answer.getType(), answer.isClientOnly());
                        dialog.addChoice(choice);
                    }
                }
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.mcq");
    }
    
    static {
        m_logger = Logger.getLogger((Class)DialogLoader.class);
    }
}
