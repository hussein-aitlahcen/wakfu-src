package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public class FightTargetValidator implements TargetValidator<EffectUser>
{
    private static final Logger m_logger;
    public static final long CONDITION_IN_AOE = 1L;
    public static final long CONDITION_IS_CASTER = 2L;
    public static final long CONDITION_IS_ALLY = 4L;
    public static final long CONDITION_IS_ENEMY = 8L;
    public static final long CONDITION_IS_HUMAN = 16L;
    public static final long CONDITION_IS_SUMMONED = 32L;
    public static final long CONDITION_IS_EFFECT_AREA = 64L;
    public static final long CONDITION_IS_ALLY_EXCEPT_CASTER = 128L;
    public static final long CONDITION_IS_NOT_CASTER = 256L;
    public static final long CONDITION_IS_ON_HOUR_CELL = 512L;
    public static final long CONDITION_CASTER_IS_BEHIND_TARGET = 1024L;
    public static final long CONDITION_IS_HOUR_CELL = 2048L;
    public static final long CONDITION_IS_OWN_SUMMON = 4096L;
    public static final long CONDITION_ACCEPT_CARRIED = 8192L;
    public static final long CONDITION_IS_KO = 16384L;
    public static final long CONDITION_CELL_HAS_NO_OBSTACLE = 32768L;
    private static final long BREEDS_CONDITIONS_SHIFT = 16L;
    public static final long CONDITION_BREED_FECA = 65536L;
    public static final long CONDITION_BREED_OSAMODAS = 131072L;
    public static final long CONDITION_BREED_ENUTROF = 262144L;
    public static final long CONDITION_BREED_SRAM = 524288L;
    public static final long CONDITION_BREED_XELOR = 1048576L;
    public static final long CONDITION_BREED_ECAFLIP = 2097152L;
    public static final long CONDITION_BREED_ENIRIPSA = 4194304L;
    public static final long CONDITION_BREED_IOP = 8388608L;
    public static final long CONDITION_BREED_CRA = 16777216L;
    public static final long CONDITION_BREED_SADIDA = 33554432L;
    public static final long CONDITION_BREED_SACRIER = 67108864L;
    public static final long CONDITION_BREED_PANDAWA = 134217728L;
    public static final long CONDITION_SAME_BREED = 1073741824L;
    public static final long CONDITION_SAME_FAMILY = 2147483648L;
    public static final long CONDITION_IS_NOT_OWN_SUMMON = 17179869184L;
    public static final long CONDITION_SADIDA_PUPPET = 34359738368L;
    public static final long CONDITION_NOT_SADIDA_PUPPET = 68719476736L;
    public static final long CONDITION_SADIDA_TOTEM = 137438953472L;
    public static final long CONDITION_NOT_SADIDA_TOTEM = 274877906944L;
    public static final long CONDITION_IS_KILLER = 549755813888L;
    public static final long CONDITION_IS_IN_PLAY = 1099511627776L;
    public static final long CONDITION_IS_MONSTER = 2199023255552L;
    public static final long CONDITION_IS_CONTROLLER = 4398046511104L;
    public static final long CONDITION_IS_OWN_EFFECT_AREA = 8796093022208L;
    public static final long CONDITION_IS_NOT_SUMMONED = 17592186044416L;
    public static final long CONDITION_IS_ORIGINAL_CONTROLLER = 35184372088832L;
    public static final long CONDITION_IS_AREA_OWNER = 70368744177664L;
    public static final long CONDITION_IS_FIGHTER = 140737488355328L;
    public static final long CONDITION_IS_NOT_AREA_OWNER = 281474976710656L;
    public static final long CONDITION_IS_NOT_EFFECT_AREA = 562949953421312L;
    public static final long CONDITION_IS_OBSTACLE = 1125899906842624L;
    protected final long[] m_conditions;
    private static final long[] NO_CONDITIONS;
    
    public FightTargetValidator(final long[] conditions) {
        super();
        if (conditions == null || conditions.length == 0) {
            this.m_conditions = FightTargetValidator.NO_CONDITIONS;
        }
        else {
            this.m_conditions = conditions;
        }
    }
    
    @Override
    public boolean isConditionActive(final long condition) {
        for (int i = 0; i < this.m_conditions.length; ++i) {
            if ((this.m_conditions[i] & condition) != 0x0L) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public ObjectPair<TargetValidity, ArrayList<EffectUser>> getTargetValidity(final Target target, final Target applicant) {
        TargetValidity bestAnswer = TargetValidity.INVALID;
        if (this.m_conditions == null || this.m_conditions == FightTargetValidator.NO_CONDITIONS) {
            return new ObjectPair<TargetValidity, ArrayList<EffectUser>>(TargetValidity.VALID, new ArrayList<EffectUser>());
        }
        if (!target.canBeTargeted()) {
            return new ObjectPair<TargetValidity, ArrayList<EffectUser>>(TargetValidity.INVALID, new ArrayList<EffectUser>());
        }
        final ArrayList<EffectUser> subTarget = new ArrayList<EffectUser>();
        for (int i = 0; i < this.m_conditions.length; ++i) {
            final long condition = this.m_conditions[i];
            if ((0x2L & condition) == 0x0L || target == applicant) {
                if ((0x100L & condition) == 0x0L || target != applicant) {
                    if ((0x80L & condition) != 0x0L) {
                        if (target == applicant) {
                            continue;
                        }
                        if (applicant == null) {
                            continue;
                        }
                        if (!(target instanceof FightEffectUser)) {
                            continue;
                        }
                        if (!(applicant instanceof FightEffectUser)) {
                            continue;
                        }
                        if (((FightEffectUser)target).getTeamId() != ((FightEffectUser)applicant).getTeamId()) {
                            continue;
                        }
                    }
                    if ((0x40000000000L & condition) != 0x0L) {
                        if (applicant == null) {
                            continue;
                        }
                        if (!(applicant instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (target != ((BasicCharacterInfo)applicant).getController()) {
                            continue;
                        }
                    }
                    if ((0x200000000000L & condition) != 0x0L) {
                        if (applicant == null) {
                            continue;
                        }
                        if (!(applicant instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (target != ((BasicCharacterInfo)applicant).getOriginalController()) {
                            continue;
                        }
                    }
                    if ((0x4L & condition) != 0x0L) {
                        if (applicant == null) {
                            continue;
                        }
                        if (!(target instanceof FightEffectUser)) {
                            continue;
                        }
                        if (!(applicant instanceof FightEffectUser)) {
                            continue;
                        }
                        if (((FightEffectUser)target).getTeamId() != ((FightEffectUser)applicant).getTeamId()) {
                            continue;
                        }
                    }
                    if ((0x8L & condition) != 0x0L) {
                        if (applicant == null) {
                            continue;
                        }
                        if (!(target instanceof FightEffectUser)) {
                            continue;
                        }
                        if (!(applicant instanceof FightEffectUser)) {
                            continue;
                        }
                        if (((FightEffectUser)target).getTeamId() == ((FightEffectUser)applicant).getTeamId()) {
                            continue;
                        }
                    }
                    if ((0x10L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (target.getId() <= 0L) {
                            continue;
                        }
                    }
                    if ((0x20000000000L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (target.getId() > 0L) {
                            continue;
                        }
                    }
                    if ((0x20L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (!((BasicCharacterInfo)target).isSummoned()) {
                            continue;
                        }
                    }
                    if ((0x100000000000L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (((BasicCharacterInfo)target).isSummoned()) {
                            continue;
                        }
                    }
                    if ((0x800L & condition) != 0x0L) {
                        if (!(target instanceof AbstractHourEffectArea)) {
                            continue;
                        }
                        if (((AbstractHourEffectArea)target).getOwner() != applicant) {
                            continue;
                        }
                    }
                    else if (target instanceof AbstractHourEffectArea) {
                        continue;
                    }
                    if ((0x200L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        final EffectContext context = ((BasicCharacterInfo)target).getEffectContext();
                        if (context == null) {
                            continue;
                        }
                        boolean inHour = false;
                        if (context != null && context.getEffectAreaManager().getActiveEffectAreas() != null) {
                            for (final BasicEffectArea area : context.getEffectAreaManager().getActiveEffectAreas()) {
                                if (area.contains(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude())) {
                                    inHour = true;
                                    break;
                                }
                            }
                        }
                        if (!inHour) {
                            continue;
                        }
                    }
                    if ((0x400L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (!(applicant instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        final Part part = ((BasicCharacterInfo)target).getPartLocalisator().getMainPartInSightFromPosition(applicant.getWorldCellX(), applicant.getWorldCellY(), applicant.getWorldCellAltitude());
                        if (part.getPartId() != 2) {
                            continue;
                        }
                    }
                    if ((0x1000L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        final BasicCharacterInfo characterInfo = (BasicCharacterInfo)target;
                        if (!characterInfo.isSummoned()) {
                            continue;
                        }
                        if (characterInfo.getController() != applicant) {
                            continue;
                        }
                        if (((BasicCharacterInfo)target).isOffPlay()) {
                            continue;
                        }
                        if (((BasicCharacterInfo)target).isOutOfPlay()) {
                            continue;
                        }
                    }
                    if ((0x4000L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (!((BasicCharacterInfo)target).isOffPlay()) {
                            continue;
                        }
                    }
                    if ((0x10000000000L & condition) != 0x0L) {
                        if (!(target instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (!((BasicCharacterInfo)target).isInPlay()) {
                            continue;
                        }
                    }
                    if ((0x400000000L & condition) != 0x0L) {
                        if (!(applicant instanceof BasicCharacterInfo)) {
                            continue;
                        }
                        if (target instanceof BasicCharacterInfo) {
                            final BasicCharacterInfo characterInfo = (BasicCharacterInfo)target;
                            if (characterInfo.isSummoned() && characterInfo.getController() == applicant) {
                                continue;
                            }
                        }
                    }
                    if ((0x40L & condition) == 0x0L || target instanceof BasicEffectArea) {
                        if ((0x800000000000L & condition) == 0x0L || target instanceof BasicCharacterInfo) {
                            if ((0x80000000000L & condition) != 0x0L) {
                                if (!(target instanceof BasicEffectArea)) {
                                    continue;
                                }
                                if (((BasicEffectArea)target).getOwner() != applicant) {
                                    continue;
                                }
                            }
                            if ((0x400000000000L & condition) != 0x0L) {
                                if (!(applicant instanceof BasicEffectArea)) {
                                    continue;
                                }
                                if (((BasicEffectArea)applicant).getOwner() != target) {
                                    continue;
                                }
                            }
                            if ((0x1000000000000L & condition) != 0x0L) {
                                if (!(applicant instanceof BasicEffectArea)) {
                                    continue;
                                }
                                if (((BasicEffectArea)applicant).getOwner() == target) {
                                    continue;
                                }
                            }
                            if ((0x10000L & condition) == 0x0L || checkBreed(target, AvatarBreed.FECA.getBreedId())) {
                                if ((0x20000L & condition) == 0x0L || checkBreed(target, AvatarBreed.OSAMODAS.getBreedId())) {
                                    if ((0x40000L & condition) == 0x0L || checkBreed(target, AvatarBreed.ENUTROF.getBreedId())) {
                                        if ((0x80000L & condition) == 0x0L || checkBreed(target, AvatarBreed.SRAM.getBreedId())) {
                                            if ((0x100000L & condition) == 0x0L || checkBreed(target, AvatarBreed.XELOR.getBreedId())) {
                                                if ((0x200000L & condition) == 0x0L || checkBreed(target, AvatarBreed.ECAFLIP.getBreedId())) {
                                                    if ((0x400000L & condition) == 0x0L || checkBreed(target, AvatarBreed.ENIRIPSA.getBreedId())) {
                                                        if ((0x800000L & condition) == 0x0L || checkBreed(target, AvatarBreed.IOP.getBreedId())) {
                                                            if ((0x1000000L & condition) == 0x0L || checkBreed(target, AvatarBreed.CRA.getBreedId())) {
                                                                if ((0x2000000L & condition) == 0x0L || checkBreed(target, AvatarBreed.SADIDA.getBreedId())) {
                                                                    if ((0x4000000L & condition) == 0x0L || checkBreed(target, AvatarBreed.SACRIER.getBreedId())) {
                                                                        if ((0x8000000L & condition) == 0x0L || checkBreed(target, AvatarBreed.PANDAWA.getBreedId())) {
                                                                            if ((0x40000000L & condition) != 0x0L) {
                                                                                if (!(applicant instanceof BasicCharacterInfo)) {
                                                                                    continue;
                                                                                }
                                                                                final Breed breed = ((BasicCharacterInfo)applicant).getBreed();
                                                                                if (breed == null) {
                                                                                    continue;
                                                                                }
                                                                                if (!checkBreed(target, breed.getBreedId())) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x80000000L & condition) != 0x0L) {
                                                                                if (!(applicant instanceof BasicCharacterInfo)) {
                                                                                    continue;
                                                                                }
                                                                                if (!(target instanceof BasicCharacterInfo)) {
                                                                                    continue;
                                                                                }
                                                                                final Breed applicantbreed = ((BasicCharacterInfo)applicant).getBreed();
                                                                                final Breed targetbreed = ((BasicCharacterInfo)target).getBreed();
                                                                                if (applicantbreed == null) {
                                                                                    continue;
                                                                                }
                                                                                if (targetbreed == null) {
                                                                                    continue;
                                                                                }
                                                                                if (applicantbreed.getFamilyId() != targetbreed.getFamilyId()) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x8000000000L & condition) != 0x0L) {
                                                                                if (!(applicant instanceof BasicCharacterInfo)) {
                                                                                    continue;
                                                                                }
                                                                                final BasicCharacterInfo character = (BasicCharacterInfo)applicant;
                                                                                if (character.getKiller() != target) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x800000000L & condition) != 0x0L) {
                                                                                if (!(target instanceof BasicCharacterInfo)) {
                                                                                    continue;
                                                                                }
                                                                                final BasicCharacterInfo character = (BasicCharacterInfo)target;
                                                                                if (!character.isActiveProperty(FightPropertyType.IS_SADIDA_PUPPET)) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x1000000000L & condition) != 0x0L) {
                                                                                if (!(target instanceof BasicCharacterInfo)) {
                                                                                    continue;
                                                                                }
                                                                                final BasicCharacterInfo character = (BasicCharacterInfo)target;
                                                                                if (character.isActiveProperty(FightPropertyType.IS_SADIDA_PUPPET)) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x2000000000L & condition) != 0x0L) {
                                                                                if (!(target instanceof EffectUser)) {
                                                                                    continue;
                                                                                }
                                                                                final EffectUser user = (EffectUser)target;
                                                                                if (!user.isActiveProperty(FightPropertyType.IS_WALKABLE_SADIDA_TOTEM)) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x4000000000L & condition) != 0x0L && target instanceof BasicEffectArea) {
                                                                                final BasicEffectArea area2 = (BasicEffectArea)target;
                                                                                if (area2.isActiveProperty(FightPropertyType.IS_WALKABLE_SADIDA_TOTEM)) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x8000L & condition) != 0x0L) {
                                                                                final EffectContext context = ((BasicCharacterInfo)target).getEffectContext();
                                                                                if (context == null) {
                                                                                    continue;
                                                                                }
                                                                                final FightMap fightMap = context.getFightMap();
                                                                                if (fightMap == null) {
                                                                                    continue;
                                                                                }
                                                                                if (fightMap.getObstacle(target.getWorldCellX(), target.getWorldCellY()) != null) {
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            if ((0x4000000000000L & condition) == 0x0L || ((FightObstacle)target).getObstacleId() >= 0) {
                                                                                if ((0x2000000000000L & condition) == 0x0L || !(target instanceof BasicEffectArea)) {
                                                                                    if ((0x1L & condition) == 0x0L) {
                                                                                        return new ObjectPair<TargetValidity, ArrayList<EffectUser>>(TargetValidity.VALID, subTarget);
                                                                                    }
                                                                                    if (applicant instanceof BasicCharacterInfo && target instanceof BasicCharacterInfo) {
                                                                                        final BasicCharacterInfo cible = (BasicCharacterInfo)target;
                                                                                        if (cible.isActiveProperty(FightPropertyType.UNTARGETTABLE_BY_OTHER)) {
                                                                                            continue;
                                                                                        }
                                                                                    }
                                                                                    bestAnswer = TargetValidity.VALID_IF_IN_AOE;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ObjectPair<TargetValidity, ArrayList<EffectUser>>(bestAnswer, subTarget);
    }
    
    private static boolean checkBreed(final Target target, final short avatarBreedId) {
        if (!(target instanceof BasicCharacterInfo)) {
            return false;
        }
        final Breed breed = ((BasicCharacterInfo)target).getBreed();
        return breed != null && avatarBreedId == breed.getBreedId();
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightTargetValidator.class);
        NO_CONDITIONS = new long[0];
    }
}
