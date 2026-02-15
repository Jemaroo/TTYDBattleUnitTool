import java.util.ArrayList;

public class test 
{
    public static void testUnitData(ArrayList<UnitData> units)
    {
        testUnits(units);
        checkForThis(units);
    }

    private static void testUnits(ArrayList<UnitData> units)
    {
        String retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            retString += "Name: " + units.get(i).name + "\n"
            + "     BattleUnitKind Structs: " + units.get(i).BattleUnitKindData.size() + "\n"
            + "     BattleUnitDefense Structs: " + units.get(i).BattleUnitDefenseData.size() + "\n"
            + "     BattleUnitDefenseAttr Structs: " + units.get(i).BattleUnitDefenseAttrData.size() + "\n"
            + "     StatusVulnerability Structs: " + units.get(i).StatusVulnerabilityData.size() + "\n"
            + "     BattleWeapon Structs: " + units.get(i).BattleWeaponData.size() + "\n"
            + "     BattleUnitKindPart Structs: " + units.get(i).BattleUnitKindPartData.size() + "\n"
            + "     HealthUpgrades: " + units.get(i).HealthUpgradesData.size() + "\n";
        }
        System.out.println("Units: \n" + retString);
    }

    private static void checkForThis(ArrayList<UnitData> units)
    {
        for(int i = 0; i < units.size(); i++)
        {
            for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
            {
                if(units.get(i).BattleUnitKindData.get(j).LimitSwitch)
                {
                    System.out.println(units.get(i).name);
                }
            }
        }
    }
}
