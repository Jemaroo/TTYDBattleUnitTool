import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @Author Jemaroo
 * @Function Main Functions for reading and parsing input data
 */
public class Main 
{
    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given file, send it to getTableData to get the right data, and return an array of UnitData
     */
    public static ArrayList<UnitData> determineFile(File givenFile)
    {
        switch(givenFile.getName())
        {
            case "main.dol":{return getTableData(givenFile);}
            case "Start.dol":{return getTableData(givenFile);}
            case "aji.rel":{return getTableData(givenFile);}
            case "bom.rel":{return getTableData(givenFile);}
            case "dou.rel":{return getTableData(givenFile);}
            case "eki.rel":{return getTableData(givenFile);}
            case "gon.rel":{return getTableData(givenFile);}
            case "gor.rel":{return getTableData(givenFile);}
            case "gra.rel":{return getTableData(givenFile);}
            case "hei.rel":{return getTableData(givenFile);}
            case "jin.rel":{return getTableData(givenFile);}
            case "jon.rel":{return getTableData(givenFile);}
            case "las.rel":{return getTableData(givenFile);}
            case "moo.rel":{return getTableData(givenFile);}
            case "mri.rel":{return getTableData(givenFile);}
            case "muj.rel":{return getTableData(givenFile);}
            case "nok.rel":{return getTableData(givenFile);}
            case "pik.rel":{return getTableData(givenFile);}
            case "rsh.rel":{return getTableData(givenFile);}
            case "tik.rel":{return getTableData(givenFile);}
            case "tou2.rel":{return getTableData(givenFile);}
            case "win.rel":{return getTableData(givenFile);}
            case "mod.rel":{return getTableData(givenFile);}
            case "hm_enemies.rel":{return getTableData(givenFile);}
            default:
            {
                System.out.println("File not valid");
                return null;
            }
        }
    }

    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given file and the json file and parse them into an array of UnitData
     */
    public static ArrayList<UnitData> getTableData(File givenFile)
    {
        File jsonFile = new File("src\\UnitData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);

        ArrayList<UnitData> units = new ArrayList<>();

        //Check for correct dol/rel in json
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            JSONArray fileArray = (JSONArray)root.get("File");
            JSONObject fileObj = null;
            JSONArray structArray = null;

            for(int i = 0; i < fileArray.size(); i ++)
            {
                fileObj = (JSONObject)fileArray.get(i);
                String name = (String)fileObj.get("Name");
                if(givenFile.getName().equalsIgnoreCase(name))
                {  
                    structArray = (JSONArray)fileObj.get("Struct");
                    System.out.println(name + " File Structs Found in: " + jsonFile.getName());
                    break;
                }
            }

            int unitCount = 0;
            JSONArray unitNameArray = null;
            int nameCounter = 0;
            if(structArray != null)
            {
                //Grabbing the amount of units in the dol/rel
                unitCount = ((Long)(((JSONObject)structArray.get(0)).get("Count"))).intValue();
                System.out.println("Unit Count Found: " + unitCount + "\n");

                //Grabbing the Unit name List
                unitNameArray = (JSONArray)fileObj.get("Units");
            }

            JSONArray unitOffsetArray = (JSONArray)((JSONObject)structArray.get(0)).get("Offsets");
            JSONArray multipleArray = (JSONArray)((JSONObject)structArray.get(0)).get("Multiple");
            
            //BattleUnitKind
            for(int i = 0; i < unitCount; i++)
            {
                UnitData unit = new UnitData();

                //Extract unit name and add it to unit
                unit.name = unitNameArray.get(nameCounter).toString();

                BattleUnitKind tempBattleUnitKind = new BattleUnitKind();

                //System.out.println("i: " + i + " | " + "Offset: " + (unitOffsetArray.get(i)));
                
                //HP
                int locator = tempBattleUnitKind.HP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.HP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                //Level
                locator = tempBattleUnitKind.level_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.level = ByteUtils.bytesToInt(givenFiledata[locator]);

                //BonusXP
                locator = tempBattleUnitKind.bonusXP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.bonusXP = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //BonusCoin
                locator = tempBattleUnitKind.bonusCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.bonusCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                //BonusCoinRate
                locator = tempBattleUnitKind.bonusCoinRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.bonusCoinRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                //BaseCoin
                locator = tempBattleUnitKind.baseCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.baseCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                //RunRate
                locator = tempBattleUnitKind.runRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.runRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                //pbCap
                locator = tempBattleUnitKind.pbCap_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.pbCap = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                //Add to arrayList
                unit.addBattleUnitKindStruct(tempBattleUnitKind);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempBattleUnitKind = new BattleUnitKind();

                        //HP
                        locator = tempBattleUnitKind.HP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.HP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                        //Level
                        locator = tempBattleUnitKind.level_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.level = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //BonusXP
                        locator = tempBattleUnitKind.bonusXP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.bonusXP = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //BonusCoin
                        locator = tempBattleUnitKind.bonusCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.bonusCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //BonusCoinRate
                        locator = tempBattleUnitKind.bonusCoinRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.bonusCoinRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //BaseCoin
                        locator = tempBattleUnitKind.baseCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.baseCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //RunRate
                        locator = tempBattleUnitKind.runRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.runRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //pbCap
                        locator = tempBattleUnitKind.pbCap_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.pbCap = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                        //Add to arrayList
                        unit.addBattleUnitKindStruct(tempBattleUnitKind);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                //Adding temp unit to units list
                units.add(unit);
                nameCounter++;
            }

            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(1)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(1)).get("Multiple");
            unitCount = ((Long)(((JSONObject)structArray.get(1)).get("Count"))).intValue();
            nameCounter = 0;
            //BattleUnitDefense
            for(int i = 0; i < unitCount; i++)
            {
                BattleUnitDefense tempBattleUnitDefense = new BattleUnitDefense();
                
                //Normal
                int locator = tempBattleUnitDefense.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Fire
                locator = tempBattleUnitDefense.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Ice
                locator = tempBattleUnitDefense.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Explosion
                locator = tempBattleUnitDefense.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Electric
                locator = tempBattleUnitDefense.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Add to arrayList
                units.get(nameCounter).addBattleUnitDefenseStruct(tempBattleUnitDefense);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempBattleUnitDefense = new BattleUnitDefense();

                        //Normal
                        locator = tempBattleUnitDefense.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fire
                        locator = tempBattleUnitDefense.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Ice
                        locator = tempBattleUnitDefense.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Explosion
                        locator = tempBattleUnitDefense.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric
                        locator = tempBattleUnitDefense.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Add to arrayList
                        units.get(nameCounter).addBattleUnitDefenseStruct(tempBattleUnitDefense);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }
            
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(2)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(2)).get("Multiple");
            unitCount = ((Long)(((JSONObject)structArray.get(2)).get("Count"))).intValue();
            nameCounter = 0;
            //BattleUnitDefenseAttr
            for(int i = 0; i < unitCount; i++)
            {
                BattleUnitDefense tempBattleUnitDefenseAttr = new BattleUnitDefense();
                
                //Normal
                int locator = tempBattleUnitDefenseAttr.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Fire
                locator = tempBattleUnitDefenseAttr.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Ice
                locator = tempBattleUnitDefenseAttr.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Explosion
                locator = tempBattleUnitDefenseAttr.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Electric
                locator = tempBattleUnitDefenseAttr.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Add to arrayList
                units.get(nameCounter).addBattleUnitDefenseAttrStruct(tempBattleUnitDefenseAttr);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempBattleUnitDefenseAttr = new BattleUnitDefense();

                        //Normal
                        locator = tempBattleUnitDefenseAttr.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fire
                        locator = tempBattleUnitDefenseAttr.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Ice
                        locator = tempBattleUnitDefenseAttr.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Explosion
                        locator = tempBattleUnitDefenseAttr.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric
                        locator = tempBattleUnitDefenseAttr.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Add to arrayList
                        units.get(nameCounter).addBattleUnitDefenseAttrStruct(tempBattleUnitDefenseAttr);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(3)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(3)).get("Multiple");
            unitCount = ((Long)(((JSONObject)structArray.get(3)).get("Count"))).intValue();
            nameCounter = 0;
            //StatusVulnerability
            for(int i = 0; i < unitCount; i++)
            {
                StatusVulnerability tempStatusVulnerability = new StatusVulnerability();
                
                //Sleep
                int locator = tempStatusVulnerability.sleep_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.sleep = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Stop
                locator = tempStatusVulnerability.stop_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.stop = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Dizzy
                locator = tempStatusVulnerability.dizzy_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.dizzy = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Poison
                locator = tempStatusVulnerability.poison_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.poison = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Confuse
                locator = tempStatusVulnerability.confuse_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.confuse = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Electric
                locator = tempStatusVulnerability.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.electric = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Burn
                locator = tempStatusVulnerability.burn_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.burn = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Freeze
                locator = tempStatusVulnerability.freeze_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.freeze = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Huge
                locator = tempStatusVulnerability.huge_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.huge = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Tiny
                locator = tempStatusVulnerability.tiny_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.tiny = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Attack Up
                locator = tempStatusVulnerability.attack_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.attack_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Attack Down
                locator = tempStatusVulnerability.attack_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.attack_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Defense Up
                locator = tempStatusVulnerability.defense_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.defense_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Defense Down
                locator = tempStatusVulnerability.defense_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.defense_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Allergic
                locator = tempStatusVulnerability.allergic_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.allergic = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Fright
                locator = tempStatusVulnerability.fright_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.fright = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Gale Force
                locator = tempStatusVulnerability.gale_force_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.gale_force = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Fast
                locator = tempStatusVulnerability.fast_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.fast = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Slow
                locator = tempStatusVulnerability.slow_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.slow = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Dodgy
                locator = tempStatusVulnerability.dodgy_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.dodgy = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Invisible
                locator = tempStatusVulnerability.invisible_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.invisible = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //OHKO             
                locator = tempStatusVulnerability.ohko_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.ohko = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Add to arrayList
                units.get(nameCounter).addStatusVulnerabilityStruct(tempStatusVulnerability);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempStatusVulnerability = new StatusVulnerability();

                        //Sleep
                        locator = tempStatusVulnerability.sleep_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.sleep = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stop
                        locator = tempStatusVulnerability.stop_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.stop = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Dizzy
                        locator = tempStatusVulnerability.dizzy_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.dizzy = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Poison
                        locator = tempStatusVulnerability.poison_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.poison = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Confuse
                        locator = tempStatusVulnerability.confuse_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.confuse = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Electric
                        locator = tempStatusVulnerability.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.electric = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Burn
                        locator = tempStatusVulnerability.burn_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.burn = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Freeze
                        locator = tempStatusVulnerability.freeze_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.freeze = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Huge
                        locator = tempStatusVulnerability.huge_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.huge = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Tiny
                        locator = tempStatusVulnerability.tiny_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.tiny = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Attack Up
                        locator = tempStatusVulnerability.attack_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.attack_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Attack Down
                        locator = tempStatusVulnerability.attack_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.attack_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Defense Up
                        locator = tempStatusVulnerability.defense_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.defense_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Defense Down
                        locator = tempStatusVulnerability.defense_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.defense_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Allergic
                        locator = tempStatusVulnerability.allergic_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.allergic = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Fright
                        locator = tempStatusVulnerability.fright_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.fright = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Gale Force
                        locator = tempStatusVulnerability.gale_force_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.gale_force = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Fast
                        locator = tempStatusVulnerability.fast_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.fast = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Slow
                        locator = tempStatusVulnerability.slow_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.slow = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Dodgy
                        locator = tempStatusVulnerability.dodgy_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.dodgy = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Invisible
                        locator = tempStatusVulnerability.invisible_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.invisible = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //OHKO             
                        locator = tempStatusVulnerability.ohko_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.ohko = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Add to arrayList
                        units.get(nameCounter).addStatusVulnerabilityStruct(tempStatusVulnerability);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            return units;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("There was an Error Finding the JSON File");
        }
        catch (IOException e)
        {
            System.out.println("There was an Error Reading the JSON File");
        }
        catch (ParseException e)
        {
            System.out.println("There was an Error Parsing the JSON File");
        }

        //Failsafe
        return null;
    }

    /**
     * @Author Jemaroo
     * @Function Will go through an array of multiples and compare the names for a match. Will return index if found, and -1 if not found
     */
    public static int multipleExists(JSONArray multipleArray, String name)
    {
        for(int i = 0; i < multipleArray.size(); i++)
        {
            if(((JSONObject)(multipleArray.get(i))).get("Name").equals(name))
            {
                //Found
                return i;
            }
        }

        //Not found
        return -1;
    }

    /**
     * @Author Jemaroo
     * @Function Will export the array of units back into the same format as the given file
     */
    public static byte[] buildNewFile(File givenFile, ArrayList<UnitData> units)
    {
        File jsonFile = new File("src\\UnitData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);
        int locator = 0;
        int BUKOffsetCounter = 0;
        int BUDOffsetCounter = 0;
        int BUDAOffsetCounter = 0;
        int SVOffsetCounter = 0;
        //int BWOffsetCounter = 0;

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            JSONArray fileArray = (JSONArray)root.get("File");
            JSONObject fileObj = null;
            JSONArray structArray = null;

            for(int i = 0; i < fileArray.size(); i ++)
            {
                fileObj = (JSONObject)fileArray.get(i);
                String name = (String)fileObj.get("Name");
                if(givenFile.getName().equalsIgnoreCase(name))
                {  
                    structArray = (JSONArray)fileObj.get("Struct");
                    break;
                }
            }

            JSONArray unitOffsetArray = null;

            for(int i = 0; i < units.size(); i++)
            {
                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(0)).get("Offsets");
                //BattleUnitKind
                for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
                {
                    //HP
                    locator = units.get(i).BattleUnitKindData.get(j).HP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    byte[] tempHP = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).HP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempHP[k];
                    }

                    //Level
                    locator = units.get(i).BattleUnitKindData.get(j).level_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).level);

                    //BonusXP
                    locator = units.get(i).BattleUnitKindData.get(j).bonusXP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusXP);
                    
                    //BonusCoin
                    locator = units.get(i).BattleUnitKindData.get(j).bonusCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoin);

                    //BonusCoinRate
                    locator = units.get(i).BattleUnitKindData.get(j).bonusCoinRate_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoinRate);

                    //BaseCoin
                    locator = units.get(i).BattleUnitKindData.get(j).baseCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).baseCoin);

                    //RunRate
                    locator = units.get(i).BattleUnitKindData.get(j).runRate_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).runRate);

                    //pbCap
                    locator = units.get(i).BattleUnitKindData.get(j).pbCap_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    byte[] tempPBCap = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).pbCap);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempPBCap[k];
                    }

                    BUKOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(1)).get("Offsets");
                //BattleUnitDefense
                for(int j = 0; j < units.get(i).BattleUnitDefenseData.size(); j++)
                {
                    //Normal
                    locator = units.get(i).BattleUnitDefenseData.get(j).normal_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).normal);

                    //Fire
                    locator = units.get(i).BattleUnitDefenseData.get(j).fire_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).fire);

                    //Ice
                    locator = units.get(i).BattleUnitDefenseData.get(j).ice_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).ice);

                    //Explosion
                    locator = units.get(i).BattleUnitDefenseData.get(j).explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).explosion);

                    //Electric
                    locator = units.get(i).BattleUnitDefenseData.get(j).electric_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).electric);

                    BUDOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(2)).get("Offsets");
                //BattleUnitDefenseAttr
                for(int j = 0; j < units.get(i).BattleUnitDefenseAttrData.size(); j++)
                {
                    //Normal
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).normal_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).normal);

                    //Fire
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).fire_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).fire);

                    //Ice
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).ice_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).ice);

                    //Explosion
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).explosion);

                    //Electric
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).electric_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).electric);

                    BUDAOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(3)).get("Offsets");
                //StatusVulnerability
                for(int j = 0; j < units.get(i).StatusVulnerabilityData.size(); j++)
                {
                    //Sleep
                    locator = units.get(i).StatusVulnerabilityData.get(j).sleep_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).sleep);

                    //Stop
                    locator = units.get(i).StatusVulnerabilityData.get(j).stop_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).stop);
                    
                    //Dizzy
                    locator = units.get(i).StatusVulnerabilityData.get(j).dizzy_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dizzy);
                    
                    //Poison
                    locator = units.get(i).StatusVulnerabilityData.get(j).poison_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).poison);
                    
                    //Confuse
                    locator = units.get(i).StatusVulnerabilityData.get(j).confuse_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).confuse);
                    
                    //Electric
                    locator = units.get(i).StatusVulnerabilityData.get(j).electric_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).electric);
                    
                    //Burn
                    locator = units.get(i).StatusVulnerabilityData.get(j).burn_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).burn);
                    
                    //Freeze
                    locator = units.get(i).StatusVulnerabilityData.get(j).freeze_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).freeze);
                    
                    //Huge
                    locator = units.get(i).StatusVulnerabilityData.get(j).huge_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).huge);
                    
                    //Tiny
                    locator = units.get(i).StatusVulnerabilityData.get(j).tiny_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).tiny);
                    
                    //Attack Up
                    locator = units.get(i).StatusVulnerabilityData.get(j).attack_up_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_up);
                    
                    //Attack Down
                    locator = units.get(i).StatusVulnerabilityData.get(j).attack_down_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_down);
                    
                    //Defense Up
                    locator = units.get(i).StatusVulnerabilityData.get(j).defense_up_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_up);
                    
                    //Defense Down
                    locator = units.get(i).StatusVulnerabilityData.get(j).defense_down_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_down);
                    
                    //Allergic
                    locator = units.get(i).StatusVulnerabilityData.get(j).allergic_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).allergic);
                    
                    //Fright
                    locator = units.get(i).StatusVulnerabilityData.get(j).fright_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fright);
                    
                    //Gale Force
                    locator = units.get(i).StatusVulnerabilityData.get(j).gale_force_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).gale_force);
                    
                    //Fast
                    locator = units.get(i).StatusVulnerabilityData.get(j).fast_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fast);
                    
                    //Slow
                    locator = units.get(i).StatusVulnerabilityData.get(j).slow_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).slow);                    
                    
                    //Dodgy
                    locator = units.get(i).StatusVulnerabilityData.get(j).dodgy_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dodgy);
                    
                    //Invisible
                    locator = units.get(i).StatusVulnerabilityData.get(j).invisible_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).invisible);
                    
                    //OHKO             
                    locator = units.get(i).StatusVulnerabilityData.get(j).ohko_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).ohko);

                    SVOffsetCounter++;
                }
            }

            return givenFiledata;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("There was an Error Finding the JSON File");
        }
        catch (IOException e)
        {
            System.out.println("There was an Error Reading the JSON File");
        }
        catch (ParseException e)
        {
            System.out.println("There was an Error Parsing the JSON File");
        }

        //Failsafe
        return null;
    }
}