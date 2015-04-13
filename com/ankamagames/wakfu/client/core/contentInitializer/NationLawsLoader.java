package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.parameter.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.client.core.*;

public final class NationLawsLoader implements ContentInitializer
{
    private static final Logger m_logger;
    private static final NationLawsLoader INSTANCE;
    
    public static NationLawsLoader getInstance() {
        return NationLawsLoader.INSTANCE;
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new NationLawBinaryData(), new LoadProcedure<NationLawBinaryData>() {
            @Override
            public void load(final NationLawBinaryData bs) {
                final NationLawModelConstant lawModel = NationLawModelConstant.fromId(bs.getLawConstantId());
                final int lawId = bs.getId();
                final int citizenPointCost = bs.getBasePointsModification();
                final int lawPointCost = bs.getLawPointCost();
                final boolean lawLocked = bs.isLawLocked();
                final Collection<NationLawApplication> nationLawApplications = EnumSet.noneOf(NationLawApplication.class);
                if (bs.isApplicableToCitizen()) {
                    nationLawApplications.add(NationLawApplication.CITIZEN);
                }
                if (bs.isApplicableToAlliedForeigner()) {
                    nationLawApplications.add(NationLawApplication.ALLIED_FOREIGNER);
                }
                if (bs.isApplicableToNeutralForeigner()) {
                    nationLawApplications.add(NationLawApplication.NEUTRAL_FOREIGNER);
                }
                final String[] lawModelParams = bs.getParams();
                final NationLaw law = (NationLaw)lawModel.model.createNewLaw(lawId, citizenPointCost, lawPointCost, lawLocked, nationLawApplications);
                law.setPercentPointsModification(bs.getPercentPointsModification());
                final ArrayList<ParserObject> lawParams = computeLawParameters(lawModelParams);
                if (!ParametersChecker.checkType(lawModel, lawParams)) {
                    NationLawsLoader.m_logger.error((Object)("La loi " + lawModel + " n'a pas des param\u00e8tres du bon type"));
                    return;
                }
                law.initialize(lawParams);
                final int[] restrictedNations = bs.getRestrictedNations();
                if (restrictedNations.length == 0) {
                    ReferenceLawManager.INSTANCE.registerCommonLaw(law);
                    return;
                }
                for (final int nation : restrictedNations) {
                    ReferenceLawManager.INSTANCE.registerRestrictedLaw(nation, law);
                }
            }
        });
        NationManager.INSTANCE.addObserver(ReferenceLawManager.INSTANCE);
        clientInstance.fireContentInitializerDone(this);
    }
    
    private static ArrayList<ParserObject> computeLawParameters(final String[] params) {
        final ArrayList<ParserObject> lawParams = new ArrayList<ParserObject>(params.length);
        for (int k = 0; k < params.length; ++k) {
            try {
                final ArrayList<ParserObject> objects = CriteriaCompiler.compileList(params[k]);
                if (objects != null) {
                    lawParams.addAll(objects);
                }
                else {
                    lawParams.add(null);
                }
            }
            catch (Exception e) {
                NationLawsLoader.m_logger.error((Object)("Erreur lors de la compilation de Param\u00e8tres sur une loi : " + params[k]), (Throwable)e);
            }
        }
        return lawParams;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.citizen.rules");
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationLawsLoader.class);
        INSTANCE = new NationLawsLoader();
    }
}
