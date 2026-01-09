import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ByteUtils
{
    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given file and return the byte data
     */
    public static byte[] readData(File file)
    {
        try 
        {
            FileInputStream f1 = new FileInputStream(file);

            byte[] data = new byte[(int)file.length()];

            f1.read(data);

            f1.close();

            return data;
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("There was an Error Finding the Input File");
        }
        catch (IOException e)
        {
            System.out.println("There was an Error Reading the Input File");
        }
    
        return new byte[0];
    }

    /**
     * @Author ChatGPT
     * @Function Will convert 1 byte to a single int
     */
    public static int bytesToInt(byte b1) 
    {
        return b1 & 0xFF;
    }

    /**
     * @Author ChatGPT
     * @Function Will convert 2 bytes to a single int
     */
    public static int bytesToInt(byte b1, byte b2) 
    {
        return ((b1 & 0xFF) << 8) | (b2 & 0xFF);
    }

    /**
     * @Author ChatGPT
     * @Function Will convert 4 bytes to a single int
     */
    public static int bytesToInt(byte b1, byte b2, byte b3, byte b4) 
    {
        return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }

    /**
     * @Author ChatGPT
     * @Function Will convert a single int into 1 byte
     */
    public static byte intTo1Byte(int value) 
    {
        byte result = (byte) (value & 0xFF);
        return result;
    }

    /**
     * @Author ChatGPT
     * @Function Will convert a single int into 2 bytes
     */
    public static byte[] intTo2Bytes(int value) 
    {
        byte[] result = new byte[2];
        result[0] = (byte) ((value >> 8) & 0xFF);
        result[1] = (byte) (value & 0xFF);
        return result;
    }

    /**
     * @Author ChatGPT
     * @Function Will convert a single int into 4 bytes
     */
    public static byte[] intTo4Bytes(int value) 
    {
        byte[] result = new byte[4];
        result[0] = (byte) ((value >> 24) & 0xFF);
        result[1] = (byte) ((value >> 16) & 0xFF);
        result[2] = (byte) ((value >> 8) & 0xFF);
        result[3] = (byte) (value & 0xFF);
        return result;
    }

    /**
     * @Author ChatGPT
     * @Function Will check if a flag is enabled in TargetClassFlags
     */
    public static boolean bitFieldFlagCheck(int bitfield, BattleWeapon.TargetClassFlags flag) 
    {
        return (bitfield & flag.mask) != 0;
    }

    /**
     * @Author ChatGPT
     * @Function Will check if a flag is enabled in TargetPropertyFlags
     */
    public static boolean bitFieldFlagCheck(int bitfield, BattleWeapon.TargetPropertyFlags flag) 
    {
        return (bitfield & flag.mask) != 0;
    }

    /**
     * @Author ChatGPT
     * @Function Will check if a flag is enabled in SpecialPropertyFlags
     */
    public static boolean bitFieldFlagCheck(int bitfield, BattleWeapon.SpecialPropertyFlags flag) 
    {
        return (bitfield & flag.mask) != 0;
    }

    /**
     * @Author ChatGPT
     * @Function Will check if a flag is enabled in CounterResistanceFlags
     */
    public static boolean bitFieldFlagCheck(int bitfield, BattleWeapon.CounterResistanceFlags flag) 
    {
        return (bitfield & flag.mask) != 0;
    }

    /**
     * @Author ChatGPT
     * @Function Will check if a flag is enabled in TargetWeightingFlags
     */
    public static boolean bitFieldFlagCheck(int bitfield, BattleWeapon.TargetWeightingFlags flag) 
    {
        return (bitfield & flag.mask) != 0;
    }

    /**
     * @Author ChatGPT
     * @Function Will check if a flag is enabled in PartsAttributeFlags
     */
    public static boolean bitFieldFlagCheck(int bitfield, BattleUnitKindPart.PartsAttributeFlags flag) 
    {
        return (bitfield & flag.mask) != 0;
    }

    /**
     * @Author ChatGPT
     * @Function Will check if a flag is enabled in PartsCounterAttributeFlags
     */
    public static boolean bitFieldFlagCheck(int bitfield, BattleUnitKindPart.PartsCounterAttributeFlags flag) 
    {
        return (bitfield & flag.mask) != 0;
    }

    /**
     * @Author ChatGPT
     * @Function Will take the current TargetClassFlags and convert them into a bitfield
     */
    public static byte[] buildTCFBitfield(BattleWeapon unitWeapon) 
    {
        int bitfield = 0;

        if(unitWeapon.CannotTargetMarioOrShellShield) bitfield |= 0x1;
        if(unitWeapon.CannotTargetPartner) bitfield |= 0x2;
        if(unitWeapon.CannotTargetEnemy) bitfield |= 0x10;
        if(unitWeapon.Unused1) bitfield |= 0x20;
        if(unitWeapon.Unused2) bitfield |= 0x40;
        if(unitWeapon.CannotTargetOppositeAlliance) bitfield |= 0x100;
        if(unitWeapon.CannotTargetOwnAlliance) bitfield |= 0x200;
        if(unitWeapon.CannotTargetSelf) bitfield |= 0x1000;
        if(unitWeapon.CannotTargetSameSpecies) bitfield |= 0x2000;
        if(unitWeapon.OnlyTargetSelf) bitfield |= 0x4000;
        if(unitWeapon.OnlyTargetMario) bitfield |= 0x10000;
        if(unitWeapon.Unused3) bitfield |= 0x20000;
        if(unitWeapon.Unused4) bitfield |= 0x100000;
        if(unitWeapon.Unused5) bitfield |= 0x200000;
        if(unitWeapon.SingleTarget) bitfield |= 0x1000000;
        if(unitWeapon.MultipleTarget) bitfield |= 0x2000000;
        if(unitWeapon.Unused6) bitfield |= 0x80000000;

        return new byte[] 
        {
            (byte) ((bitfield >> 24) & 0xFF),
            (byte) ((bitfield >> 16) & 0xFF),
            (byte) ((bitfield >> 8) & 0xFF),
            (byte) (bitfield & 0xFF)
        };
    }

    /**
     * @Author ChatGPT
     * @Function Will take the current TargetPropertyFlags and convert them into a bitfield
     */
    public static byte[] buildTPFBitfield(BattleWeapon unitWeapon) 
    {
        int bitfield = 0;

        if(unitWeapon.Tattlelike) bitfield |= 0x1;
        if(unitWeapon.Unused7) bitfield |= 0x2;
        if(unitWeapon.CannotTargetCeiling) bitfield |= 0x4;
        if(unitWeapon.CannotTargetFloating) bitfield |= 0x8;
        if(unitWeapon.CannotTargetGrounded) bitfield |= 0x10;
        if(unitWeapon.Jumplike) bitfield |= 0x1000;
        if(unitWeapon.Hammerlike) bitfield |= 0x2000;
        if(unitWeapon.ShellTosslike) bitfield |= 0x4000;
        if(unitWeapon.Unused8) bitfield |= 0x8000;
        if(unitWeapon.RecoilDamage) bitfield |= 0x100000;
        if(unitWeapon.CanOnlyTargetFrontmost) bitfield |= 0x1000000;
        if(unitWeapon.Unused9) bitfield |= 0x2000000;
        if(unitWeapon.Unused10) bitfield |= 0x4000000;
        if(unitWeapon.TargetSameAllianceDirection) bitfield |= 0x10000000;
        if(unitWeapon.TargetOppositeAllianceDirection) bitfield |= 0x20000000;

        return new byte[] 
        {
            (byte) ((bitfield >> 24) & 0xFF),
            (byte) ((bitfield >> 16) & 0xFF),
            (byte) ((bitfield >> 8) & 0xFF),
            (byte) (bitfield & 0xFF)
        };
    }

    /**
     * @Author ChatGPT
     * @Function Will take the current SpecialPropertyFlags and convert them into a bitfield
     */
    public static byte[] buildSPFBitfield(BattleWeapon unitWeapon) 
    {
        int bitfield = 0;

        if(unitWeapon.BadgeCanAffectPower) bitfield |= 0x1;
        if(unitWeapon.StatusCanAffectPower) bitfield |= 0x2;
        if(unitWeapon.IsChargeable) bitfield |= 0x4;
        if(unitWeapon.CannotMiss) bitfield |= 0x8;
        if(unitWeapon.DiminishingReturnsByHit) bitfield |= 0x10;
        if(unitWeapon.DiminishingReturnsByTarget) bitfield |= 0x20;
        if(unitWeapon.PiercesDefense) bitfield |= 0x40;
        if(unitWeapon.Unused11) bitfield |= 0x80;
        if(unitWeapon.IgnoreTargetStatusVulnerability) bitfield |= 0x100;
        if(unitWeapon.Unused12) bitfield |= 0x200;
        if(unitWeapon.IgnitesIfBurned) bitfield |= 0x400;
        if(unitWeapon.Unused13) bitfield |= 0x800;
        if(unitWeapon.FlipsShellEnemies) bitfield |= 0x1000;
        if(unitWeapon.FlipsBombFlippableEnemies) bitfield |= 0x2000;
        if(unitWeapon.GroundsWingedEnemies) bitfield |= 0x4000;
        if(unitWeapon.Unused14) bitfield |= 0x8000;
        if(unitWeapon.CanBeUsedAsConfusedAction) bitfield |= 0x10000;
        if(unitWeapon.Unguardable) bitfield |= 0x20000;
        if(unitWeapon.Unused15) bitfield |= 0x40000;

        return new byte[] 
        {
            (byte) ((bitfield >> 24) & 0xFF),
            (byte) ((bitfield >> 16) & 0xFF),
            (byte) ((bitfield >> 8) & 0xFF),
            (byte) (bitfield & 0xFF)
        };
    }

    /**
     * @Author ChatGPT
     * @Function Will take the current CounterResistanceFlags and convert them into a bitfield
     */
    public static byte[] buildCRFBitfield(BattleWeapon unitWeapon) 
    {
        int bitfield = 0;

        if(unitWeapon.Electric) bitfield |= 0x1;
        if(unitWeapon.TopSpiky) bitfield |= 0x2;
        if(unitWeapon.PreemptiveFrontSpiky) bitfield |= 0x4;
        if(unitWeapon.FrontSpiky) bitfield |= 0x8;
        if(unitWeapon.Fiery) bitfield |= 0x10;
        if(unitWeapon.Icy) bitfield |= 0x20;
        if(unitWeapon.Poison) bitfield |= 0x40;
        if(unitWeapon.Explosive) bitfield |= 0x80;
        if(unitWeapon.VolatileExplosive) bitfield |= 0x100;
        if(unitWeapon.Payback) bitfield |= 0x200;
        if(unitWeapon.HoldFast) bitfield |= 0x400;

        return new byte[] 
        {
            (byte) ((bitfield >> 24) & 0xFF),
            (byte) ((bitfield >> 16) & 0xFF),
            (byte) ((bitfield >> 8) & 0xFF),
            (byte) (bitfield & 0xFF)
        };
    }

    /**
     * @Author ChatGPT
     * @Function Will take the current TargetWeightingFlags and convert them into a bitfield
     */
    public static byte[] buildTWFBitfield(BattleWeapon unitWeapon) 
    {
        int bitfield = 0;

        if(unitWeapon.PreferMario) bitfield |= 0x1;
        if(unitWeapon.PreferPartner) bitfield |= 0x2;
        if(unitWeapon.PreferFront) bitfield |= 0x4;
        if(unitWeapon.PreferBack) bitfield |= 0x8;
        if(unitWeapon.PreferSameAlliance) bitfield |= 0x10;
        if(unitWeapon.PreferOppositeAlliance) bitfield |= 0x20;
        if(unitWeapon.PreferLessHealthy) bitfield |= 0x100;
        if(unitWeapon.GreatlyPreferLessHealthy) bitfield |= 0x200;
        if(unitWeapon.PreferLowerHP) bitfield |= 0x400;
        if(unitWeapon.PreferHigherHP) bitfield |= 0x800;
        if(unitWeapon.PreferInPeril) bitfield |= 0x1000;
        if(unitWeapon.Unused16) bitfield |= 0x2000;
        if(unitWeapon.ChooseWeightedRandomly) bitfield |= 0x80000000;

        return new byte[] 
        {
            (byte) ((bitfield >> 24) & 0xFF),
            (byte) ((bitfield >> 16) & 0xFF),
            (byte) ((bitfield >> 8) & 0xFF),
            (byte) (bitfield & 0xFF)
        };
    }

    /**
     * @Author ChatGPT
     * @Function Will take the current PartsAttributeFlags and convert them into a bitfield
     */
    public static byte[] buildPAFBitfield(BattleUnitKindPart unitKindPart) 
    {
        int bitfield = 0;

        if(unitKindPart.MostPreferredSelectTarget) bitfield |= 0x1;
        if(unitKindPart.PreferredSelectTarget) bitfield |= 0x2;
        if(unitKindPart.SelectTarget) bitfield |= 0x4;
        if(unitKindPart.Unknown1) bitfield |= 0x8;
        if(unitKindPart.Unknown2) bitfield |= 0x10;
        if(unitKindPart.Unknown3) bitfield |= 0x40;
        if(unitKindPart.WeakToAttackFxR) bitfield |= 0x80;
        if(unitKindPart.WeakToIcePower) bitfield |= 0x100;
        if(unitKindPart.IsWinged) bitfield |= 0x800;
        if(unitKindPart.IsShelled) bitfield |= 0x1000;
        if(unitKindPart.IsBombFlippable) bitfield |= 0x2000;
        if(unitKindPart.Unknown4) bitfield |= 0x4000;
        if(unitKindPart.Unknown5) bitfield |= 0x8000;
        if(unitKindPart.NeverTargetable) bitfield |= 0x10000;
        if(unitKindPart.Unknown6) bitfield |= 0x20000;
        if(unitKindPart.Unknown7) bitfield |= 0x40000;
        if(unitKindPart.Untattleable) bitfield |= 0x80000;
        if(unitKindPart.JumplikeCannotTarget) bitfield |= 0x100000;
        if(unitKindPart.HammerlikeCannotTarget) bitfield |= 0x200000;
        if(unitKindPart.ShellTosslikeCannotTarget) bitfield |= 0x400000;
        if(unitKindPart.Unknown8) bitfield |= 0x800000;
        if(unitKindPart.Unknown9) bitfield |= 0x1000000;
        if(unitKindPart.Unknown10) bitfield |= 0x2000000;
        if(unitKindPart.Unknown11) bitfield |= 0x4000000;
        if(unitKindPart.Unknown12) bitfield |= 0x8000000;
        if(unitKindPart.Unknown13) bitfield |= 0x10000000;
        if(unitKindPart.IsImmuneToDamageOrStatus) bitfield |= 0x20000000;
        if(unitKindPart.IsImmuneToOHKO) bitfield |= 0x40000000;
        if(unitKindPart.IsImmuneToStatus) bitfield |= 0x80000000;

        return new byte[] 
        {
            (byte) ((bitfield >> 24) & 0xFF),
            (byte) ((bitfield >> 16) & 0xFF),
            (byte) ((bitfield >> 8) & 0xFF),
            (byte) (bitfield & 0xFF)
        };
    }

    /**
     * @Author ChatGPT
     * @Function Will take the current PartsCounterAttributeFlags and convert them into a bitfield
     */
    public static byte[] buildPCAFBitfield(BattleUnitKindPart unitKindPart) 
    {
        int bitfield = 0;

        if(unitKindPart.TopSpiky) bitfield |= 0x1;
        if(unitKindPart.PreemptiveFrontSpiky) bitfield |= 0x2;
        if(unitKindPart.FrontSpiky) bitfield |= 0x4;
        if(unitKindPart.Fiery) bitfield |= 0x10;
        if(unitKindPart.FieryStatus) bitfield |= 0x20;
        if(unitKindPart.Icy) bitfield |= 0x40;
        if(unitKindPart.IcyStatus) bitfield |= 0x80;
        if(unitKindPart.Poison) bitfield |= 0x100;
        if(unitKindPart.PoisonStatus) bitfield |= 0x200;
        if(unitKindPart.Electric) bitfield |= 0x400;
        if(unitKindPart.ElectricStatus) bitfield |= 0x800;
        if(unitKindPart.Explosive) bitfield |= 0x1000;
        if(unitKindPart.VolatileExplosive) bitfield |= 0x2000;

        return new byte[] 
        {
            (byte) ((bitfield >> 24) & 0xFF),
            (byte) ((bitfield >> 16) & 0xFF),
            (byte) ((bitfield >> 8) & 0xFF),
            (byte) (bitfield & 0xFF)
        };
    }
}