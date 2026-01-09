import java.util.ArrayList;

/**
 * @Author Jemaroo
 * @Function Storage and functions for handling TTYD Unit Data
 * @Notes Storage types built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
public class UnitData 
{
    public String name = "";
    public ArrayList<BattleUnitKind> BattleUnitKindData = new ArrayList<>();
    public ArrayList<BattleUnitDefense> BattleUnitDefenseData = new ArrayList<>();
    public ArrayList<BattleUnitDefenseAttr> BattleUnitDefenseAttrData = new ArrayList<>();
    public ArrayList<StatusVulnerability> StatusVulnerabilityData = new ArrayList<>();
    public ArrayList<BattleWeapon> BattleWeaponData = new ArrayList<>();
    public ArrayList<BattleUnitKindPart> BattleUnitKindPartData = new ArrayList<>();
    public ArrayList<HealthUpgrades> HealthUpgradesData = new ArrayList<>();

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitKind struct to the stored data list
     */
    public void addBattleUnitKindStruct(BattleUnitKind addedStruct)
    {
        BattleUnitKindData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitDefense struct to the stored data list
     */
    public void addBattleUnitDefenseStruct(BattleUnitDefense addedStruct)
    {
        BattleUnitDefenseData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitDefenseAttr struct to the stored data list
     */
    public void addBattleUnitDefenseAttrStruct(BattleUnitDefenseAttr addedStruct)
    {
        BattleUnitDefenseAttrData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a StatusVulnerability struct to the stored data list
     */
    public void addStatusVulnerabilityStruct(StatusVulnerability addedStruct)
    {
        StatusVulnerabilityData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleWeapon struct to the stored data list
     */
    public void addBattleWeaponStruct(BattleWeapon addedStruct)
    {
        BattleWeaponData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitKindPart struct to the stored data list
     */
    public void addBattleUnitKindPartStruct(BattleUnitKindPart addedStruct)
    {
        BattleUnitKindPartData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a HealthUpgrades struct to the stored data list
     */
    public void addHealthUpgradeStruct(HealthUpgrades addedStruct)
    {
        HealthUpgradesData.add(addedStruct);
    }
}