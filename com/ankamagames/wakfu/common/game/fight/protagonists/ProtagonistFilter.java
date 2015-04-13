package com.ankamagames.wakfu.common.game.fight.protagonists;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.io.*;

public abstract class ProtagonistFilter
{
    public static final ProtagonistFilter NONE;
    
    public abstract boolean matches(final BasicCharacterInfo p0, final FighterState p1);
    
    protected abstract String name();
    
    @Override
    public String toString() {
        return this.name();
    }
    
    public static ProtagonistFilter ofType(final byte type) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighter.getType() == type;
            }
            
            @Override
            protected String name() {
                return "ofType";
            }
        };
    }
    
    public static ProtagonistFilter isSummon() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighter.isSummoned();
            }
            
            @Override
            protected String name() {
                return "isSummoned";
            }
        };
    }
    
    public static ProtagonistFilter inPlay() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isInPlay();
            }
            
            @Override
            protected String name() {
                return "inPlay";
            }
        };
    }
    
    public static ProtagonistFilter localToFight() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isLocalToFight();
            }
            
            @Override
            protected String name() {
                return "localToFight";
            }
        };
    }
    
    public static ProtagonistFilter offPlay() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isOffPlay();
            }
            
            @Override
            protected String name() {
                return "offPlay";
            }
        };
    }
    
    public static ProtagonistFilter outOffPlay() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isOutOfPlay();
            }
            
            @Override
            protected String name() {
                return "outOffPlay";
            }
        };
    }
    
    public static ProtagonistFilter withId(final long id) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighter.getId() == id;
            }
            
            @Override
            protected String name() {
                return "withId";
            }
        };
    }
    
    public static ProtagonistFilter inTeam(final byte teamId) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.getTeamId() == teamId;
            }
            
            @Override
            protected String name() {
                return "inTeam";
            }
        };
    }
    
    public static ProtagonistFilter inOriginalTeam(final byte teamId) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.getOriginalTeamId() == teamId;
            }
            
            @Override
            protected String name() {
                return "inTeam";
            }
        };
    }
    
    public static ProtagonistFilter hasProperty(final PropertyType property) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighter.isActiveProperty(property);
            }
            
            @Override
            protected String name() {
                return "hasProperty";
            }
        };
    }
    
    public static ProtagonistFilter isCarried() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighter.isCarried();
            }
            
            @Override
            protected String name() {
                return "isCarried";
            }
        };
    }
    
    public static ProtagonistFilter leaderOfTeam() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isTeamLeader();
            }
            
            @Override
            protected String name() {
                return "leaderOfTeam";
            }
        };
    }
    
    public static ProtagonistFilter controller() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isOriginallyControlledBy(fighter.getId());
            }
            
            @Override
            protected String name() {
                return "controller";
            }
        };
    }
    
    public static ProtagonistFilter controlledBy(final BasicCharacterInfo controller) {
        if (controller == null) {
            return ProtagonistFilter.NONE;
        }
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isCurrentlyControlledBy(controller.getId());
            }
            
            @Override
            protected String name() {
                return "controlledBy";
            }
        };
    }
    
    public static ProtagonistFilter controllerInTeam(final byte controllerTeamId) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighter.getControllerTeamId() == controllerTeamId;
            }
            
            @Override
            protected String name() {
                return "controllerInTeam";
            }
        };
    }
    
    public static ProtagonistFilter originallyControlledBy(final BasicCharacterInfo controller) {
        if (controller == null) {
            return ProtagonistFilter.NONE;
        }
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighterState.isOriginallyControlledBy(controller.getId());
            }
            
            @Override
            protected String name() {
                return "originallyControlledBy";
            }
        };
    }
    
    public static ProtagonistFilter or(final ProtagonistFilter... specs) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                for (final ProtagonistFilter spec : specs) {
                    if (spec.matches(fighter, fighterState)) {
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            protected String name() {
                final StringBuilder name = new StringBuilder("or(");
                for (int i = 0; i < specs.length; ++i) {
                    final ProtagonistFilter spec = specs[i];
                    name.append(spec.name()).append(", ");
                }
                if (specs.length > 0) {
                    name.setLength(name.length() - 2);
                }
                return name.append(")").toString();
            }
        };
    }
    
    public static ProtagonistFilter and(final ProtagonistFilter... specs) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                for (final ProtagonistFilter spec : specs) {
                    if (!spec.matches(fighter, fighterState)) {
                        return false;
                    }
                }
                return true;
            }
            
            @Override
            protected String name() {
                final StringBuilder name = new StringBuilder("and(");
                for (int i = 0; i < specs.length; ++i) {
                    final ProtagonistFilter spec = specs[i];
                    name.append(spec.name()).append(", ");
                }
                if (specs.length > 0) {
                    name.setLength(name.length() - 2);
                }
                return name.append(")").toString();
            }
        };
    }
    
    public static ProtagonistFilter not(final ProtagonistFilter operand) {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return !operand.matches(fighter, fighterState);
            }
            
            @Override
            protected String name() {
                return "not(" + operand.name() + ")";
            }
        };
    }
    
    public static ProtagonistFilter invisible() {
        return new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return fighter.isInvisible();
            }
            
            @Override
            protected String name() {
                return "invisible";
            }
        };
    }
    
    static {
        NONE = new ProtagonistFilter() {
            @Override
            public boolean matches(final BasicCharacterInfo fighter, final FighterState fighterState) {
                return false;
            }
            
            @Override
            protected String name() {
                return "none";
            }
        };
    }
}
