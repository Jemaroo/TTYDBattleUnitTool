/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitKind data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
public class BattleUnitKind
{
    public int HP = 0;
    public int dangerHP = 0;
    public int perilHP = 0;
    public int level = 0;
    public int bonusXP = 0;
    public int bonusCoin = 0;
    public int bonusCoinRate = 0;
    public int baseCoin = 0;
    public int runRate = 0;
    public int pbCap = 0;
    public int swallowChance = 0;
    public int swallowAttribute = 0;
    public int ultraHammerKnockChance = 0;
    public int itemStealParameter = 0;

    //UnitAttributeFlags
    public boolean MapObj = false;
    public boolean OutOfReach = false;
    public boolean Unquakeable = false;
    public boolean IsInvisible = false;
    public boolean IsVeiled = false;
    public boolean ShellShielded = false;
    public boolean NeverTargetable = false;
    public boolean LimitSwitch = false;
    public boolean DisableZeroGravityFloat = false;
    public boolean DisableZeroGravityImmobility = false;
    public boolean immuneToUltraHammerKnock = false;
    public boolean IsUndead = false;
    public boolean IsCorpse = false;
    public boolean IsLeader = false;
    public boolean CannotTakeActions = false;
    public boolean NotSpunByLoveSlap = false;
    public boolean DisableDamageStars = false;
    public boolean DisableAllPartVisibility = false;
    public boolean DisableHPGauge = false;
    public boolean LookCamera = false;
    public boolean NonCombatant = false;
    public boolean NoShadow = false;
    public boolean DisableDamage = false;

    public final int HP_offset = 8;
    public final int dangerHP_offset = 12;
    public final int perilHP_offset = 13;
    public final int level_offset = 14;
    public final int bonusXP_offset = 15;
    public final int bonusCoin_offset = 16;
    public final int bonusCoinRate_offset = 17;
    public final int baseCoin_offset = 18;
    public final int runRate_offset = 19;
    public final int pbCap_offset = 20;
    public final int swallowChance_offset = 138;
    public final int swallowAttribute_offset = 139;
    public final int ultraHammerKnockChance_offset = 140;
    public final int itemStealParameter_offset = 141;
    public final int UnitAttributeFlags_offset = 172;

    public enum UnitAttributeFlags 
    {
        MapObj(0x1),
        OutOfReach(0x2),
        Unquakeable(0x4),
        IsInvisible(0x8),
        IsVeiled(0x10),
        ShellShielded(0x20),
        NeverTargetable(0x40),
        LimitSwitch(0x100),
        DisableZeroGravityFloat(0x1000),
        DisableZeroGravityImmobility(0x2000),
        immuneToUltraHammerKnock(0x4000),
        IsUndead(0x10000),
        IsCorpse(0x20000),
        IsLeader(0x40000),
        CannotTakeActions(0x80000),
        NotSpunByLoveSlap(0x200000),
        DisableDamageStars(0x400000),
        DisableAllPartVisibility(0x1000000),
        DisableHPGauge(0x2000000),
        LookCamera(0x4000000),
        NonCombatant(0x10000000),
        NoShadow(0x20000000),
        DisableDamage(0x40000000);

        public final int mask;
        
        UnitAttributeFlags(int mask) 
        {
            this.mask = mask;
        }
    }
}
