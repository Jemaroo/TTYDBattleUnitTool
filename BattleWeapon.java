/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleWeapon data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
public class BattleWeapon
{
    public String attackName = "";

    public int accuracy = 0;
    public int fp_cost = 0;
    public int sp_cost = 0;
    public int superguard_state = 0;
    public int sylish_multiplier = 0;
    public int bingo_slot_inc_chance = 0;
    public int base_damage1 = 0;
    public int base_damage2 = 0;
    public int base_damage3 = 0;
    public int base_damage4 = 0;
    public int base_damage5 = 0;
    public int base_damage6 = 0;
    public int base_damage7 = 0;
    public int base_damage8 = 0;
    public int base_fpdamage1 = 0;
    public int base_fpdamage2 = 0;
    public int base_fpdamage3 = 0;
    public int base_fpdamage4 = 0;
    public int base_fpdamage5 = 0;
    public int base_fpdamage6 = 0;
    public int base_fpdamage7 = 0;
    public int base_fpdamage8 = 0;

    //TargetClassFlags
    public boolean CannotTargetMarioOrShellShield = false;
    public boolean CannotTargetPartner = false;
    public boolean CannotTargetEnemy = false;
    public boolean Unused1 = false;
    public boolean Unused2 = false;
    public boolean CannotTargetOppositeAlliance = false;
    public boolean CannotTargetOwnAlliance = false;
    public boolean CannotTargetSelf = false;
    public boolean CannotTargetSameSpecies = false;
    public boolean OnlyTargetSelf = false;
    public boolean OnlyTargetMario = false;
    public boolean Unused3 = false;
    public boolean Unused4 = false;
    public boolean Unused5 = false;
    public boolean SingleTarget = false;
    public boolean MultipleTarget = false;
    public boolean Unused6 = false;

    //TargetPropertyFlags
    public boolean Tattlelike = false;
    public boolean Unused7 = false;
    public boolean CannotTargetCeiling = false;
    public boolean CannotTargetFloating = false;
    public boolean CannotTargetGrounded = false;
    public boolean Jumplike = false;
    public boolean Hammerlike = false;
    public boolean ShellTosslike = false;
    public boolean Unused8 = false;
    public boolean RecoilDamage = false;
    public boolean CanOnlyTargetFrontmost = false;
    public boolean Unused9 = false;
    public boolean Unused10 = false;
    public boolean TargetSameAllianceDirection = false;
    public boolean TargetOppositeAllianceDirection = false;

    public int element = 0;
    public int damage_pattern = 0;

    //SpecialPropertyFlags
    public boolean BadgeCanAffectPower = false;
    public boolean StatusCanAffectPower = false;
    public boolean IsChargeable = false;
    public boolean CannotMiss = false;
    public boolean DiminishingReturnsByHit = false;
    public boolean DiminishingReturnsByTarget = false;
    public boolean PiercesDefense = false;
    public boolean Unused11 = false;
    public boolean IgnoreTargetStatusVulnerability = false;
    public boolean Unused12 = false;
    public boolean IgnitesIfBurned = false;
    public boolean Unused13 = false;
    public boolean FlipsShellEnemies = false;
    public boolean FlipsBombFlippableEnemies = false;
    public boolean GroundsWingedEnemies = false;
    public boolean Unused14 = false;
    public boolean CanBeUsedAsConfusedAction = false;
    public boolean Unguardable = false;
    public boolean Unused15 = false;

    //CounterResistanceFlags
    public boolean Electric = false;
    public boolean TopSpiky = false;
    public boolean PreemptiveFrontSpiky = false;
    public boolean FrontSpiky = false;
    public boolean Fiery = false;
    public boolean Icy = false;
    public boolean Poison = false;
    public boolean Explosive = false;
    public boolean VolatileExplosive = false;
    public boolean Payback = false;
    public boolean HoldFast = false;

    //TargetWeightingFlags
    public boolean PreferMario = false;
    public boolean PreferPartner = false;
    public boolean PreferFront = false;
    public boolean PreferBack = false;
    public boolean PreferSameAlliance = false;
    public boolean PreferOppositeAlliance = false;
    public boolean PreferLessHealthy = false;
    public boolean GreatlyPreferLessHealthy = false;
    public boolean PreferLowerHP = false;
    public boolean PreferHigherHP = false;
    public boolean PreferInPeril = false;
    public boolean Unused16 = false;
    public boolean ChooseWeightedRandomly = false;

    public int sleep_chance = 0;
    public int sleep_time = 0;
    public int stop_chance = 0;
    public int stop_time = 0;
    public int dizzy_chance = 0;
    public int dizzy_time = 0;
    public int poison_chance = 0;
    public int poison_time = 0;
    public int poison_strength = 0;
    public int confuse_chance = 0;
    public int confuse_time = 0;
    public int electric_chance = 0;
    public int electric_time = 0;
    public int dodgy_chance = 0;
    public int dodgy_time = 0;
    public int burn_chance = 0;
    public int burn_time = 0;
    public int freeze_chance = 0;
    public int freeze_time = 0;
    public int size_change_chance = 0;
    public int size_change_time = 0;
    public int size_change_strength = 0;
    public int atk_change_chance = 0;
    public int atk_change_time = 0;
    public int atk_change_strength = 0;
    public int def_change_chance = 0;
    public int def_change_time = 0;
    public int def_change_strength = 0;
    public int allergic_chance = 0;
    public int allergic_time = 0;
    public int ohko_chance = 0;
    public int charge_strength = 0;
    public int fast_chance = 0;
    public int fast_time = 0;
    public int slow_chance = 0;
    public int slow_time = 0;
    public int fright_chance = 0;
    public int gale_force_chance = 0;
    public int payback_time = 0;
    public int hold_fast_time = 0;
    public int invisible_chance = 0;
    public int invisible_time = 0;
    public int hp_regen_time = 0;
    public int hp_regen_strength = 0;
    public int fp_regen_time = 0;
    public int fp_regen_strength = 0;
    public int stage_background_fallweight1 = 0;
    public int stage_background_fallweight2 = 0;
    public int stage_background_fallweight3 = 0;
    public int stage_background_fallweight4 = 0;
    public int stage_nozzle_turn_chance = 0;
    public int stage_nozzle_fire_chance = 0;
    public int stage_ceiling_fall_chance = 0;
    public int stage_object_fall_chance = 0;

    public final int accuracy_offset = 16;
    public final int fp_cost_offset = 17;
    public final int sp_cost_offset = 18;
    public final int superguard_state_offset = 19;
    public final int sylish_multiplier_offset = 24;
    public final int bingo_slot_inc_chance_offset = 26;
    public final int base_damage1_offset = 32;
    public final int base_damage2_offset = 36;
    public final int base_damage3_offset = 40;
    public final int base_damage4_offset = 44;
    public final int base_damage5_offset = 48;
    public final int base_damage6_offset = 52;
    public final int base_damage7_offset = 56;
    public final int base_damage8_offset = 60;
    public final int base_fpdamage1_offset = 68;
    public final int base_fpdamage2_offset = 72;
    public final int base_fpdamage3_offset = 76;
    public final int base_fpdamage4_offset = 80;
    public final int base_fpdamage5_offset = 84;
    public final int base_fpdamage6_offset = 88;
    public final int base_fpdamage7_offset = 92;
    public final int base_fpdamage8_offset = 96;
    public final int TargetClassFlags_offset = 100;
    public final int TargetPropertyFlags_offset = 104;
    public final int element_offset = 108;
    public final int damage_pattern_offset = 109;
    public final int SpecialPropertyFlags_offset = 116;
    public final int CounterResistanceFlags_offset = 120;
    public final int TargetWeightingFlags_offset = 124;
    public final int sleep_chance_offset = 128;
    public final int sleep_time_offset = 129;
    public final int stop_chance_offset = 130;
    public final int stop_time_offset = 131;
    public final int dizzy_chance_offset = 132;
    public final int dizzy_time_offset = 133;
    public final int poison_chance_offset = 134;
    public final int poison_time_offset = 135;
    public final int poison_strength_offset = 136;
    public final int confuse_chance_offset = 137;
    public final int confuse_time_offset = 138;
    public final int electric_chance_offset = 139;
    public final int electric_time_offset = 140;
    public final int dodgy_chance_offset = 141;
    public final int dodgy_time_offset = 142;
    public final int burn_chance_offset = 143;
    public final int burn_time_offset = 144;
    public final int freeze_chance_offset = 145;
    public final int freeze_time_offset = 146;
    public final int size_change_chance_offset = 147;
    public final int size_change_time_offset = 148;
    public final int size_change_strength_offset = 149;
    public final int atk_change_chance_offset = 150;
    public final int atk_change_time_offset = 151;
    public final int atk_change_strength_offset = 152;
    public final int def_change_chance_offset = 153;
    public final int def_change_time_offset = 154;
    public final int def_change_strength_offset = 155;
    public final int allergic_chance_offset = 156;
    public final int allergic_time_offset = 157;
    public final int ohko_chance_offset = 158;
    public final int charge_strength_offset = 159;
    public final int fast_chance_offset = 160;
    public final int fast_time_offset = 161;
    public final int slow_chance_offset = 162;
    public final int slow_time_offset = 163;
    public final int fright_chance_offset = 164;
    public final int gale_force_chance_offset = 165;
    public final int payback_time_offset = 166;
    public final int hold_fast_time_offset = 167;
    public final int invisible_chance_offset = 168;
    public final int invisible_time_offset = 169;
    public final int hp_regen_time_offset = 170;
    public final int hp_regen_strength_offset = 171;
    public final int fp_regen_time_offset = 172;
    public final int fp_regen_strength_offset = 173;
    public final int stage_background_fallweight1_offset = 180;
    public final int stage_background_fallweight2_offset = 181;
    public final int stage_background_fallweight3_offset = 182;
    public final int stage_background_fallweight4_offset = 184;
    public final int stage_nozzle_turn_chance_offset = 185;
    public final int stage_nozzle_fire_chance_offset = 186;
    public final int stage_ceiling_fall_chance_offset = 187;
    public final int stage_object_fall_chance_offset = 188;

    public enum TargetClassFlags 
    {
        CannotTargetMarioOrShellShield(0x1),
        CannotTargetPartner(0x2),
        CannotTargetEnemy(0x10),
        Unused1(0x20),
        Unused2(0x40),
        CannotTargetOppositeAlliance(0x100),
        CannotTargetOwnAlliance(0x200),
        CannotTargetSelf(0x1000),
        CannotTargetSameSpecies(0x2000),
        OnlyTargetSelf(0x4000),
        OnlyTargetMario(0x10000),
        Unused3(0x20000),
        Unused4(0x100000),
        Unused5(0x200000),
        SingleTarget(0x1000000),
        MultipleTarget(0x2000000),
        Unused6(0x80000000);

        public final int mask;
        
        TargetClassFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum TargetPropertyFlags 
    {
        Tattlelike(0x1),
        Unused7(0x2),
        CannotTargetCeiling(0x4),
        CannotTargetFloating(0x8),
        CannotTargetGrounded(0x10),
        Jumplike(0x1000),
        Hammerlike(0x2000),
        ShellTosslike(0x4000),
        Unused8(0x8000),
        RecoilDamage(0x100000),
        CanOnlyTargetFrontmost(0x1000000),
        Unused9(0x2000000),
        Unused10(0x4000000),
        TargetSameAllianceDirection(0x10000000),
        TargetOppositeAllianceDirection(0x20000000);

        public final int mask;
        
        TargetPropertyFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum SpecialPropertyFlags 
    {
        BadgeCanAffectPower(0x1),
        StatusCanAffectPower(0x2),
        IsChargeable(0x4),
        CannotMiss(0x8),
        DiminishingReturnsByHit(0x10),
        DiminishingReturnsByTarget(0x20),
        PiercesDefense(0x40),
        Unused11(0x80),
        IgnoreTargetStatusVulnerability(0x100),
        Unused12(0x200),
        IgnitesIfBurned(0x400),
        Unused13(0x800),
        FlipsShellEnemies(0x1000),
        FlipsBombFlippableEnemies(0x2000),
        GroundsWingedEnemies(0x4000),
        Unused14(0x8000),
        CanBeUsedAsConfusedAction(0x10000),
        Unguardable(0x20000),
        Unused15(0x40000);

        public final int mask;
        
        SpecialPropertyFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum CounterResistanceFlags 
    {
        Electric(0x1),
        TopSpiky(0x2),
        PreemptiveFrontSpiky(0x4),
        FrontSpiky(0x8),
        Fiery(0x10),
        Icy(0x20),
        Poison(0x40),
        Explosive(0x80),
        VolatileExplosive(0x100),
        Payback(0x200),
        HoldFast(0x400);

        public final int mask;
        
        CounterResistanceFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum TargetWeightingFlags 
    {
        PreferMario(0x1),
        PreferPartner(0x2),
        PreferFront(0x4),
        PreferBack(0x8),
        PreferSameAlliance(0x10),
        PreferOppositeAlliance(0x20),
        PreferLessHealthy(0x100),
        GreatlyPreferLessHealthy(0x200),
        PreferLowerHP(0x400),
        PreferHigherHP(0x800),
        PreferInPeril(0x1000),
        Unused16(0x2000),
        ChooseWeightedRandomly(0x80000000);

        public final int mask;
        
        TargetWeightingFlags(int mask) 
        {
            this.mask = mask;
        }
    }
}