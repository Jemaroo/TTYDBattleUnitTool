import java.util.ArrayList;

public class test 
{
    public static void testUnitData(ArrayList<UnitData> units)
    {
        //TESTING BATTLEUNITKIND COUNT
        String retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            if(units.get(i).BattleUnitKindData.size() > 1)
            {
                retString += "Name: " + units.get(i).name + "\n";

                for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
                {
                    retString += "     HP Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).HP + " | Level Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).level + " | Bonus XP Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).bonusXP + " | Bonus Coin Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).bonusCoin + " | Bonus Coin Rate Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).bonusCoinRate + " | Base Coin Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).baseCoin + " | Run Rate Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).runRate + " | PB Cap Stat " + (j + 1) + ": "
                    + units.get(i).BattleUnitKindData.get(0).pbCap + "\n";
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

            //retString += units.get(i).name + ": " + units.get(i).BattleUnitKindData.size();
        }
        System.out.println("BattleUnitKind: \n" + retString);

        //TESTING BATTLEUNITDEFENSE COUNT
        retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            if(units.get(i).BattleUnitDefenseData.size() > 1)
            {
                retString += "Name: " + units.get(i).name + "\n";

                for(int j = 0; j < units.get(i).BattleUnitDefenseData.size(); j++)
                {
                    retString += "     Normal Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(0).normal + " | Fire Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(0).fire + " | Ice Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(0).ice + " | Explosion Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(0).explosion + " | Electric Def " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseData.get(0).electric + "\n";
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

            //retString += units.get(i).name + ": " + units.get(i).BattleUnitKindData.size();
        }
        System.out.println("BattleUnitDefense: \n" + retString);

        //TESTING BATTLEUNITDEFENSEATTR COUNT
        retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            if(units.get(i).BattleUnitDefenseAttrData.size() > 1)
            {
                retString += "Name: " + units.get(i).name + "\n";

                for(int j = 0; j < units.get(i).BattleUnitDefenseAttrData.size(); j++)
                {
                    retString += "     Normal Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(0).normal + " | Fire Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(0).fire + " | Ice Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(0).ice + " | Explosion Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(0).explosion + " | Electric Def Attr " + (j + 1) + ": "
                    + units.get(i).BattleUnitDefenseAttrData.get(0).electric + "\n";
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

            //retString += units.get(i).name + ": " + units.get(i).BattleUnitKindData.size();
        }
        System.out.println("BattleUnitDefenseAttr: \n" + retString);
    }
}