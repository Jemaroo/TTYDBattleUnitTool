/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitKindPart data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
public class BattleUnitKindPart
{
    //PartsAttributeFlags
    public boolean MostPreferredSelectTarget = false;
    public boolean PreferredSelectTarget = false;
    public boolean SelectTarget = false;
    public boolean Unknown1 = false;
    public boolean Unknown2 = false;
    public boolean Unknown3 = false;
    public boolean WeakToAttackFxR = false;
    public boolean WeakToIcePower = false;
    public boolean IsWinged = false;
    public boolean IsShelled = false;
    public boolean IsBombFlippable = false;
    public boolean Unknown4 = false;
    public boolean Unknown5 = false;
    public boolean NeverTargetable = false;
    public boolean Unknown6 = false;
    public boolean Unknown7 = false;
    public boolean Untattleable = false;
    public boolean JumplikeCannotTarget = false;
    public boolean HammerlikeCannotTarget = false;
    public boolean ShellTosslikeCannotTarget = false;
    public boolean Unknown8 = false;
    public boolean Unknown9 = false;
    public boolean Unknown10 = false;
    public boolean Unknown11 = false;
    public boolean Unknown12 = false;
    public boolean Unknown13 = false;
    public boolean IsImmuneToDamageOrStatus = false;
    public boolean IsImmuneToOHKO = false;
    public boolean IsImmuneToStatus = false;

    //PartsCounterAttributeFlags
    public boolean TopSpiky = false;
    public boolean PreemptiveFrontSpiky = false;
    public boolean FrontSpiky = false;
    public boolean Fiery = false;
    public boolean FieryStatus = false;
    public boolean Icy = false;
    public boolean IcyStatus = false;
    public boolean Poison = false;
    public boolean PoisonStatus = false;
    public boolean Electric = false;
    public boolean ElectricStatus = false;
    public boolean Explosive = false;
    public boolean VolatileExplosive = false;

    public final int PartsAttributeFlags_offset = 64;
    public final int PartsCounterAttributeFlags_offset = 68;

    public enum PartsAttributeFlags 
    {
        MostPreferredSelectTarget(0x1),
        PreferredSelectTarget(0x2),
        SelectTarget(0x4),
        Unknown1(0x8),
        Unknown2(0x10),
        Unknown3(0x40),
        WeakToAttackFxR(0x80),
        WeakToIcePower(0x100),
        IsWinged(0x800),
        IsShelled(0x1000),
        IsBombFlippable(0x2000),
        Unknown4(0x4000),
        Unknown5(0x8000),
        NeverTargetable(0x10000),
        Unknown6(0x20000),
        Unknown7(0x40000),
        Untattleable(0x80000),
        JumplikeCannotTarget(0x100000),
        HammerlikeCannotTarget(0x200000),
        ShellTosslikeCannotTarget(0x400000),
        Unknown8(0x800000),
        Unknown9(0x1000000),
        Unknown10(0x2000000),
        Unknown11(0x4000000),
        Unknown12(0x8000000),
        Unknown13(0x10000000),
        IsImmuneToDamageOrStatus(0x20000000),
        IsImmuneToOHKO(0x40000000),
        IsImmuneToStatus(0x80000000);

        public final int mask;
        
        PartsAttributeFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum PartsCounterAttributeFlags 
    {
        TopSpiky(0x1),
        PreemptiveFrontSpiky(0x2),
        FrontSpiky(0x4),
        Fiery(0x10),
        FieryStatus(0x20),
        Icy(0x40),
        IcyStatus(0x80),
        Poison(0x100),
        PoisonStatus(0x200),
        Electric(0x400),
        ElectricStatus(0x800),
        Explosive(0x1000),
        VolatileExplosive(0x2000);

        public final int mask;
        
        PartsCounterAttributeFlags(int mask) 
        {
            this.mask = mask;
        }
    }
}