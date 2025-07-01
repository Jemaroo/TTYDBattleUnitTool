import java.util.ArrayList;

public class test 
{
    public static void testUnitData(ArrayList<UnitData> units)
    {
        testUnits(units);
        //testBattleUnitKind(units);
        //testBattleUnitDefense(units);
        //testBattleUnitDefenseAttr(units);
        //testStatusVulnerability(units);
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
            + "     StatusVulnerability Structs: " + units.get(i).StatusVulnerabilityData.size() + "\n";
        }
        System.out.println("Units: \n" + retString);
    }

    private static void testBattleUnitKind(ArrayList<UnitData> units)
    {
        String retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            if(units.get(i).BattleUnitKindData.size() > 1)
            {
                retString += "Name: " + units.get(i).name + "\n";

                for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
                {
                    retString += "     HP Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).HP + " | Level Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).level + " | Bonus XP Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).bonusXP + " | Bonus Coin Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).bonusCoin + " | Bonus Coin Rate Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).bonusCoinRate + " | Base Coin Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).baseCoin + " | Run Rate Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).runRate + " | PB Cap Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(j).pbCap + "\n";
                }
            }
            else
            {
                retString += "Name: " + units.get(i).name + " | HP: " 
                + units.get(i).BattleUnitKindData.get(0).HP + " | Level: "
                + units.get(i).BattleUnitKindData.get(0).level + " | Bonus XP: "
                + units.get(i).BattleUnitKindData.get(0).bonusXP + " | Bonus Coin: "
                + units.get(i).BattleUnitKindData.get(0).bonusCoin + " | Bonus Coin Rate: "
                + units.get(i).BattleUnitKindData.get(0).bonusCoinRate + " | Base Coin: "
                + units.get(i).BattleUnitKindData.get(0).baseCoin + " | Run Rate: "
                + units.get(i).BattleUnitKindData.get(0).runRate + " | PB Cap: "
                + units.get(i).BattleUnitKindData.get(0).pbCap + "\n";
            }
        }
        System.out.println("BattleUnitKind: \n" + retString);
    }

    private static void testBattleUnitDefense(ArrayList<UnitData> units)
    {
        String retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            if(units.get(i).BattleUnitDefenseData.size() > 1)
            {
                retString += "Name: " + units.get(i).name + "\n";

                for(int j = 0; j < units.get(i).BattleUnitDefenseData.size(); j++)
                {
                    retString += "     Normal Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(j).normal + " | Fire Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(j).fire + " | Ice Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(j).ice + " | Explosion Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(j).explosion + " | Electric Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(j).electric + "\n";
                }
            }
            else
            {
                retString += "Name: " + units.get(i).name + " | Normal Def: "
                + units.get(i).BattleUnitDefenseData.get(0).normal + " | Fire Def: "
                + units.get(i).BattleUnitDefenseData.get(0).fire + " | Ice Def: "
                + units.get(i).BattleUnitDefenseData.get(0).ice + " | Explosion Def: "
                + units.get(i).BattleUnitDefenseData.get(0).explosion + " | Electric Def: "
                + units.get(i).BattleUnitDefenseData.get(0).electric + "\n";
            }
        }
        System.out.println("BattleUnitDefense: \n" + retString);
    }

    private static void testBattleUnitDefenseAttr(ArrayList<UnitData> units)
    {
        String retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            if(units.get(i).BattleUnitDefenseAttrData.size() > 1)
            {
                retString += "Name: " + units.get(i).name + "\n";

                for(int j = 0; j < units.get(i).BattleUnitDefenseAttrData.size(); j++)
                {
                    retString += "     Normal Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(j).normal + " | Fire Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(j).fire + " | Ice Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(j).ice + " | Explosion Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(j).explosion + " | Electric Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(j).electric + "\n";
                }
            }
            else
            {
                retString += "Name: " + units.get(i).name + " | Normal Def Attr: "
                + units.get(i).BattleUnitDefenseAttrData.get(0).normal + " | Fire Def Attr: "
                + units.get(i).BattleUnitDefenseAttrData.get(0).fire + " | Ice Def Attr: "
                + units.get(i).BattleUnitDefenseAttrData.get(0).ice + " | Explosion Def Attr: "
                + units.get(i).BattleUnitDefenseAttrData.get(0).explosion + " | Electric Def Attr: "
                + units.get(i).BattleUnitDefenseAttrData.get(0).electric + "\n";
            }
        }
        System.out.println("BattleUnitDefenseAttr: \n" + retString);
    }

    private static void testStatusVulnerability(ArrayList<UnitData> units)
    {
        String retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            if(units.get(i).StatusVulnerabilityData.size() > 1)
            {
                retString += "Name: " + units.get(i).name + "\n";

                for(int j = 0; j < units.get(i).StatusVulnerabilityData.size(); j++)
                {
                    retString += "     Sleep " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).sleep + " | Stop " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).stop + " | Dizzy " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).dizzy + " | Poison " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).poison + " | Confuse " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).confuse + " | Electric " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).electric + " | Burn " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).burn + " | Freeze " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).freeze + " | Huge " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).huge + " | Tiny " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).tiny + " | Attack Up " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).attack_up + " | Attack Down " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).attack_down + " | Defense Up " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).defense_up + " | Defense Down " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).defense_down + " | Allergic " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).allergic + " | Fright " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).fright + " | Gale Force " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).gale_force + " | Fast " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).fast + " | Slow " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).slow + " | Dodgy " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).dodgy + " | Invisible " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).invisible + " | OHKO " + (j + 1) + ": "
                    + units.get(i).StatusVulnerabilityData.get(j).ohko + "\n";
                }
            }
            else
            {
                retString += "Name: " + units.get(i).name + " | Sleep: "
                + units.get(i).StatusVulnerabilityData.get(0).sleep + " | Stop: "
                + units.get(i).StatusVulnerabilityData.get(0).stop + " | Dizzy: "
                + units.get(i).StatusVulnerabilityData.get(0).dizzy + " | Poison: "
                + units.get(i).StatusVulnerabilityData.get(0).poison + " | Confuse: "
                + units.get(i).StatusVulnerabilityData.get(0).confuse + " | Electric: "
                + units.get(i).StatusVulnerabilityData.get(0).electric + " | Burn: "
                + units.get(i).StatusVulnerabilityData.get(0).burn + " | Freeze: "
                + units.get(i).StatusVulnerabilityData.get(0).freeze + " | Huge: "
                + units.get(i).StatusVulnerabilityData.get(0).huge + " | Tiny: "
                + units.get(i).StatusVulnerabilityData.get(0).tiny + " | Attack Up: "
                + units.get(i).StatusVulnerabilityData.get(0).attack_up + " | Attack Down: "
                + units.get(i).StatusVulnerabilityData.get(0).attack_down + " | Defense Up: "
                + units.get(i).StatusVulnerabilityData.get(0).defense_up + " | Defense Down: "
                + units.get(i).StatusVulnerabilityData.get(0).defense_down + " | Allergic: "
                + units.get(i).StatusVulnerabilityData.get(0).allergic + " | Fright: "
                + units.get(i).StatusVulnerabilityData.get(0).fright + " | Gale Force: "
                + units.get(i).StatusVulnerabilityData.get(0).gale_force + " | Fast: "
                + units.get(i).StatusVulnerabilityData.get(0).fast + " | Slow: "
                + units.get(i).StatusVulnerabilityData.get(0).slow + " | Dodgy: "
                + units.get(i).StatusVulnerabilityData.get(0).dodgy + " | Invisible: "
                + units.get(i).StatusVulnerabilityData.get(0).invisible + " | OHKO: "
                + units.get(i).StatusVulnerabilityData.get(0).ohko + "\n";
            }
        }
        System.out.println("StatusVulnerability: \n" + retString);
    }
}