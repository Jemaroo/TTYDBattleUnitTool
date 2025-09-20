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
     * @Function Searches for matching files in a given directory
     */
    public static ArrayList<File> findMatchingFiles(File directory, ArrayList<String> targetFilenames) 
    {
        ArrayList<File> matchingFiles = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) 
        {
            for (File file : files) 
            {
                if (file.isDirectory()) 
                {
                    matchingFiles.addAll(findMatchingFiles(file, targetFilenames));
                } 
                else if (targetFilenames.contains(file.getName())) 
                {
                    matchingFiles.add(file);
                }
            }
        }

        return matchingFiles;
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
                
                //HP
                int locator = tempBattleUnitKind.HP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.HP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                //DangerHP
                locator = tempBattleUnitKind.dangerHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.dangerHP = ByteUtils.bytesToInt(givenFiledata[locator]);

                //PerilHP
                locator = tempBattleUnitKind.perilHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.perilHP = ByteUtils.bytesToInt(givenFiledata[locator]);

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

                //SwallowChance
                locator = tempBattleUnitKind.swallowChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.swallowChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                //SwallowAttributes
                locator = tempBattleUnitKind.swallowAttribute_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.swallowAttribute = ByteUtils.bytesToInt(givenFiledata[locator]);

                //UltraHammerKnockChance
                locator = tempBattleUnitKind.ultraHammerKnockChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.ultraHammerKnockChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                //ItemStealParameter
                locator = tempBattleUnitKind.itemStealParameter_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.itemStealParameter = ByteUtils.bytesToInt(givenFiledata[locator]);

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

                        //DangerHP
                        locator = tempBattleUnitKind.dangerHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.dangerHP = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //PerilHP
                        locator = tempBattleUnitKind.perilHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.perilHP = ByteUtils.bytesToInt(givenFiledata[locator]);

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

                        //SwallowChance
                        locator = tempBattleUnitKind.swallowChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.swallowChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //SwallowAttribute
                        locator = tempBattleUnitKind.swallowAttribute_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.swallowAttribute = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //UltraHammerKnockChance
                        locator = tempBattleUnitKind.ultraHammerKnockChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.ultraHammerKnockChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //ItemStealParameters
                        locator = tempBattleUnitKind.itemStealParameter_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.itemStealParameter = ByteUtils.bytesToInt(givenFiledata[locator]);

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
                BattleUnitDefenseAttr tempBattleUnitDefenseAttr = new BattleUnitDefenseAttr();
                
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
                        tempBattleUnitDefenseAttr = new BattleUnitDefenseAttr();

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

            //BattleWeapon
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(4)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(4)).get("Multiple");
            JSONArray attackNamesArray = (JSONArray)((JSONObject)structArray.get(4)).get("AttackNames");
            nameCounter = 0;
            int attackNameCounter = 0;
            for(int i = 0; i < units.size(); i++)
            {
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleCheck == -1)
                {
                    BattleWeapon tempBattleWeapon = new BattleWeapon();

                    //Attack Name
                    tempBattleWeapon.attackName = attackNamesArray.get(attackNameCounter).toString();

                    //Accuracy
                    int locator = tempBattleWeapon.accuracy_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.accuracy = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //FP Cost
                    locator = tempBattleWeapon.fp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //SP Cost
                    locator = tempBattleWeapon.sp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Superguard State
                    locator = tempBattleWeapon.superguard_state_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.superguard_state = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Stylish Multiplier
                    locator = tempBattleWeapon.sylish_multiplier_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sylish_multiplier = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Bingo Slot Increase Chance
                    locator = tempBattleWeapon.bingo_slot_inc_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.bingo_slot_inc_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Base Damage 1
                    locator = tempBattleWeapon.base_damage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 2
                    locator = tempBattleWeapon.base_damage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 3
                    locator = tempBattleWeapon.base_damage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 4
                    locator = tempBattleWeapon.base_damage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 5
                    locator = tempBattleWeapon.base_damage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 6
                    locator = tempBattleWeapon.base_damage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 7
                    locator = tempBattleWeapon.base_damage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 8
                    locator = tempBattleWeapon.base_damage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 1
                    locator = tempBattleWeapon.base_fpdamage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 2
                    locator = tempBattleWeapon.base_fpdamage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 3
                    locator = tempBattleWeapon.base_fpdamage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 4
                    locator = tempBattleWeapon.base_fpdamage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 5
                    locator = tempBattleWeapon.base_fpdamage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 6
                    locator = tempBattleWeapon.base_fpdamage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 7
                    locator = tempBattleWeapon.base_fpdamage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 8
                    locator = tempBattleWeapon.base_fpdamage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //TargetClassFlags
                    locator = tempBattleWeapon.TargetClassFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
                    tempBattleWeapon.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
                    tempBattleWeapon.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
                    tempBattleWeapon.Unused1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused1);
                    tempBattleWeapon.Unused2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused2);
                    tempBattleWeapon.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
                    tempBattleWeapon.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
                    tempBattleWeapon.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
                    tempBattleWeapon.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
                    tempBattleWeapon.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
                    tempBattleWeapon.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
                    tempBattleWeapon.Unused3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused3);
                    tempBattleWeapon.Unused4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused4);
                    tempBattleWeapon.Unused5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused5);
                    tempBattleWeapon.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
                    tempBattleWeapon.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
                    tempBattleWeapon.Unused6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused6);

                    //TargetPropertyFlags
                    locator = tempBattleWeapon.TargetPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
                    tempBattleWeapon.Unused7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused7);
                    tempBattleWeapon.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
                    tempBattleWeapon.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
                    tempBattleWeapon.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
                    tempBattleWeapon.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
                    tempBattleWeapon.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
                    tempBattleWeapon.Unused8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused8);
                    tempBattleWeapon.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
                    tempBattleWeapon.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
                    tempBattleWeapon.Unused9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused9);
                    tempBattleWeapon.Unused10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused10);
                    tempBattleWeapon.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
                    tempBattleWeapon.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);

                    //Element
                    locator = tempBattleWeapon.element_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.element = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Damage Pattern
                    locator = tempBattleWeapon.damage_pattern_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.damage_pattern = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //AC Level
                    locator = tempBattleWeapon.ac_level_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.ac_level = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //SpecialPropertyFlags
                    locator = tempBattleWeapon.SpecialPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
                    tempBattleWeapon.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
                    tempBattleWeapon.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
                    tempBattleWeapon.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
                    tempBattleWeapon.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
                    tempBattleWeapon.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
                    tempBattleWeapon.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
                    tempBattleWeapon.Unused11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused11);
                    tempBattleWeapon.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
                    tempBattleWeapon.Unused12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused12);
                    tempBattleWeapon.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
                    tempBattleWeapon.Unused13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused13);
                    tempBattleWeapon.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
                    tempBattleWeapon.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
                    tempBattleWeapon.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
                    tempBattleWeapon.Unused14 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused14);
                    tempBattleWeapon.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
                    tempBattleWeapon.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
                    tempBattleWeapon.Unused15 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused15);

                    //CounterResistanceFlags
                    locator = tempBattleWeapon.CounterResistanceFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
                    tempBattleWeapon.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
                    tempBattleWeapon.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
                    tempBattleWeapon.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
                    tempBattleWeapon.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
                    tempBattleWeapon.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
                    tempBattleWeapon.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
                    tempBattleWeapon.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
                    tempBattleWeapon.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
                    tempBattleWeapon.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
                    tempBattleWeapon.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);

                    //TargetWeightingFlags
                    locator = tempBattleWeapon.TargetWeightingFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
                    tempBattleWeapon.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
                    tempBattleWeapon.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
                    tempBattleWeapon.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
                    tempBattleWeapon.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
                    tempBattleWeapon.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
                    tempBattleWeapon.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
                    tempBattleWeapon.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
                    tempBattleWeapon.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
                    tempBattleWeapon.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
                    tempBattleWeapon.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
                    tempBattleWeapon.Unused16 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.Unused16);
                    tempBattleWeapon.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);

                    //Sleep Chance
                    locator = tempBattleWeapon.sleep_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sleep_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Sleep Time
                    locator = tempBattleWeapon.sleep_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sleep_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Stop Chance
                    locator = tempBattleWeapon.stop_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stop_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Stop Time
                    locator = tempBattleWeapon.stop_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stop_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dizzy Chance
                    locator = tempBattleWeapon.dizzy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dizzy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dizzy Time
                    locator = tempBattleWeapon.dizzy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dizzy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Poison Chance
                    locator = tempBattleWeapon.poison_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.poison_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Poison Time
                    locator = tempBattleWeapon.poison_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.poison_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Poison Strength
                    locator = tempBattleWeapon.poison_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.poison_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Confuse Chance
                    locator = tempBattleWeapon.confuse_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.confuse_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Confuse Time
                    locator = tempBattleWeapon.confuse_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.confuse_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Electric Chance
                    locator = tempBattleWeapon.electric_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.electric_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Electric Time
                    locator = tempBattleWeapon.electric_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.electric_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dodgy Chance
                    locator = tempBattleWeapon.dodgy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dodgy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dodgy Time
                    locator = tempBattleWeapon.dodgy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dodgy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Burn Chance
                    locator = tempBattleWeapon.burn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.burn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Burn Time
                    locator = tempBattleWeapon.burn_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.burn_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Freeze Chance
                    locator = tempBattleWeapon.freeze_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.freeze_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Freeze Time
                    locator = tempBattleWeapon.freeze_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.freeze_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Size Change Chance
                    locator = tempBattleWeapon.size_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.size_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Size Change Time
                    locator = tempBattleWeapon.size_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.size_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Size Change Strength
                    locator = tempBattleWeapon.size_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.size_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Atk Change Chance
                    locator = tempBattleWeapon.atk_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.atk_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Atk Change Time
                    locator = tempBattleWeapon.atk_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.atk_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Atk Change Strength
                    locator = tempBattleWeapon.atk_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.atk_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Def Change Chance
                    locator = tempBattleWeapon.def_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.def_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Def Change Time
                    locator = tempBattleWeapon.def_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.def_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Def Change Strength
                    locator = tempBattleWeapon.def_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.def_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Allergic Chance
                    locator = tempBattleWeapon.allergic_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.allergic_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Allergic Time
                    locator = tempBattleWeapon.allergic_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.allergic_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //OHKO Chance
                    locator = tempBattleWeapon.ohko_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.ohko_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Charge Strength
                    locator = tempBattleWeapon.charge_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.charge_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Fast Chance
                    locator = tempBattleWeapon.fast_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fast_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Fast Time
                    locator = tempBattleWeapon.fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Slow Chance
                    locator = tempBattleWeapon.slow_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.slow_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Slow Time
                    locator = tempBattleWeapon.slow_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.slow_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Fright Chance
                    locator = tempBattleWeapon.fright_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fright_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Gale Force Chance
                    locator = tempBattleWeapon.gale_force_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.gale_force_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Payback Time
                    locator = tempBattleWeapon.payback_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.payback_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Hold Fast Time
                    locator = tempBattleWeapon.hold_fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.hold_fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Invisible Chance
                    locator = tempBattleWeapon.invisible_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.invisible_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Invisible Time
                    locator = tempBattleWeapon.invisible_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.invisible_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //HP Regen Time
                    locator = tempBattleWeapon.hp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.hp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //HP Regen Strength
                    locator = tempBattleWeapon.hp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.hp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //FP Regen Time
                    locator = tempBattleWeapon.fp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //FP Regen Strength
                    locator = tempBattleWeapon.fp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_background_fallweight1
                    locator = tempBattleWeapon.stage_background_fallweight1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight1 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_background_fallweight2
                    locator = tempBattleWeapon.stage_background_fallweight2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight2 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_background_fallweight3
                    locator = tempBattleWeapon.stage_background_fallweight3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight3 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_background_fallweight4
                    locator = tempBattleWeapon.stage_background_fallweight4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight4 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_nozzle_turn_chance
                    locator = tempBattleWeapon.stage_nozzle_turn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_nozzle_turn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_nozzle_fire_chance
                    locator = tempBattleWeapon.stage_nozzle_fire_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_nozzle_fire_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_ceiling_fall_chance
                    locator = tempBattleWeapon.stage_ceiling_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_ceiling_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_object_fall_chance
                    locator = tempBattleWeapon.stage_object_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_object_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    attackNameCounter++;
                    units.get(i).addBattleWeaponStruct(tempBattleWeapon);
                }
                else if(Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))) != 0)
                {
                    for(int j = 0; j < Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))); j++)
                    {
                        BattleWeapon tempBattleWeapon = new BattleWeapon();

                        //Attack Name
                        tempBattleWeapon.attackName = attackNamesArray.get(attackNameCounter).toString();

                        //Accuracy
                        int locator = tempBattleWeapon.accuracy_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.accuracy = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //FP Cost
                        locator = tempBattleWeapon.fp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //SP Cost
                        locator = tempBattleWeapon.sp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Superguard State
                        locator = tempBattleWeapon.superguard_state_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.superguard_state = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stylish Multiplier
                        locator = tempBattleWeapon.sylish_multiplier_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sylish_multiplier = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Bingo Slot Increase Chance
                        locator = tempBattleWeapon.bingo_slot_inc_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.bingo_slot_inc_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Base Damage 1
                        locator = tempBattleWeapon.base_damage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 2
                        locator = tempBattleWeapon.base_damage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 3
                        locator = tempBattleWeapon.base_damage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 4
                        locator = tempBattleWeapon.base_damage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 5
                        locator = tempBattleWeapon.base_damage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 6
                        locator = tempBattleWeapon.base_damage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 7
                        locator = tempBattleWeapon.base_damage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 8
                        locator = tempBattleWeapon.base_damage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 1
                        locator = tempBattleWeapon.base_fpdamage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 2
                        locator = tempBattleWeapon.base_fpdamage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 3
                        locator = tempBattleWeapon.base_fpdamage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 4
                        locator = tempBattleWeapon.base_fpdamage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 5
                        locator = tempBattleWeapon.base_fpdamage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 6
                        locator = tempBattleWeapon.base_fpdamage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 7
                        locator = tempBattleWeapon.base_fpdamage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 8
                        locator = tempBattleWeapon.base_fpdamage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //TargetClassFlags
                        locator = tempBattleWeapon.TargetClassFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
                        tempBattleWeapon.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
                        tempBattleWeapon.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
                        tempBattleWeapon.Unused1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused1);
                        tempBattleWeapon.Unused2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused2);
                        tempBattleWeapon.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
                        tempBattleWeapon.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
                        tempBattleWeapon.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
                        tempBattleWeapon.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
                        tempBattleWeapon.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
                        tempBattleWeapon.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
                        tempBattleWeapon.Unused3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused3);
                        tempBattleWeapon.Unused4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused4);
                        tempBattleWeapon.Unused5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused5);
                        tempBattleWeapon.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
                        tempBattleWeapon.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
                        tempBattleWeapon.Unused6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused6);

                        //TargetPropertyFlags
                        locator = tempBattleWeapon.TargetPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
                        tempBattleWeapon.Unused7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused7);
                        tempBattleWeapon.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
                        tempBattleWeapon.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
                        tempBattleWeapon.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
                        tempBattleWeapon.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
                        tempBattleWeapon.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
                        tempBattleWeapon.Unused8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused8);
                        tempBattleWeapon.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
                        tempBattleWeapon.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
                        tempBattleWeapon.Unused9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused9);
                        tempBattleWeapon.Unused10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused10);
                        tempBattleWeapon.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
                        tempBattleWeapon.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);

                        //Element
                        locator = tempBattleWeapon.element_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.element = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Damage Pattern
                        locator = tempBattleWeapon.damage_pattern_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.damage_pattern = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //AC Level
                        locator = tempBattleWeapon.ac_level_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.ac_level = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //SpecialPropertyFlags
                        locator = tempBattleWeapon.SpecialPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
                        tempBattleWeapon.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
                        tempBattleWeapon.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
                        tempBattleWeapon.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
                        tempBattleWeapon.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
                        tempBattleWeapon.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
                        tempBattleWeapon.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
                        tempBattleWeapon.Unused11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused11);
                        tempBattleWeapon.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
                        tempBattleWeapon.Unused12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused12);
                        tempBattleWeapon.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
                        tempBattleWeapon.Unused13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused13);
                        tempBattleWeapon.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
                        tempBattleWeapon.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
                        tempBattleWeapon.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
                        tempBattleWeapon.Unused14 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused14);
                        tempBattleWeapon.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
                        tempBattleWeapon.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
                        tempBattleWeapon.Unused15 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused15);

                        //CounterResistanceFlags
                        locator = tempBattleWeapon.CounterResistanceFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
                        tempBattleWeapon.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
                        tempBattleWeapon.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
                        tempBattleWeapon.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
                        tempBattleWeapon.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
                        tempBattleWeapon.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
                        tempBattleWeapon.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
                        tempBattleWeapon.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
                        tempBattleWeapon.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
                        tempBattleWeapon.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
                        tempBattleWeapon.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);

                        //TargetWeightingFlags
                        locator = tempBattleWeapon.TargetWeightingFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
                        tempBattleWeapon.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
                        tempBattleWeapon.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
                        tempBattleWeapon.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
                        tempBattleWeapon.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
                        tempBattleWeapon.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
                        tempBattleWeapon.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
                        tempBattleWeapon.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
                        tempBattleWeapon.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
                        tempBattleWeapon.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
                        tempBattleWeapon.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
                        tempBattleWeapon.Unused16 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.Unused16);
                        tempBattleWeapon.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);

                        //Sleep Chance
                        locator = tempBattleWeapon.sleep_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sleep_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Sleep Time
                        locator = tempBattleWeapon.sleep_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sleep_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stop Chance
                        locator = tempBattleWeapon.stop_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stop_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stop Time
                        locator = tempBattleWeapon.stop_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stop_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dizzy Chance
                        locator = tempBattleWeapon.dizzy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dizzy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dizzy Time
                        locator = tempBattleWeapon.dizzy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dizzy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Poison Chance
                        locator = tempBattleWeapon.poison_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.poison_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Poison Time
                        locator = tempBattleWeapon.poison_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.poison_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Poison Strength
                        locator = tempBattleWeapon.poison_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.poison_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Confuse Chance
                        locator = tempBattleWeapon.confuse_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.confuse_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Confuse Time
                        locator = tempBattleWeapon.confuse_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.confuse_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric Chance
                        locator = tempBattleWeapon.electric_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.electric_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric Time
                        locator = tempBattleWeapon.electric_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.electric_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dodgy Chance
                        locator = tempBattleWeapon.dodgy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dodgy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dodgy Time
                        locator = tempBattleWeapon.dodgy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dodgy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Burn Chance
                        locator = tempBattleWeapon.burn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.burn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Burn Time
                        locator = tempBattleWeapon.burn_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.burn_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Freeze Chance
                        locator = tempBattleWeapon.freeze_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.freeze_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Freeze Time
                        locator = tempBattleWeapon.freeze_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.freeze_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Size Change Chance
                        locator = tempBattleWeapon.size_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.size_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Size Change Time
                        locator = tempBattleWeapon.size_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.size_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Size Change Strength
                        locator = tempBattleWeapon.size_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.size_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Atk Change Chance
                        locator = tempBattleWeapon.atk_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.atk_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Atk Change Time
                        locator = tempBattleWeapon.atk_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.atk_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Atk Change Strength
                        locator = tempBattleWeapon.atk_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.atk_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Def Change Chance
                        locator = tempBattleWeapon.def_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.def_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Def Change Time
                        locator = tempBattleWeapon.def_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.def_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Def Change Strength
                        locator = tempBattleWeapon.def_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.def_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Allergic Chance
                        locator = tempBattleWeapon.allergic_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.allergic_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Allergic Time
                        locator = tempBattleWeapon.allergic_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.allergic_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //OHKO Chance
                        locator = tempBattleWeapon.ohko_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.ohko_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Charge Strength
                        locator = tempBattleWeapon.charge_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.charge_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fast Chance
                        locator = tempBattleWeapon.fast_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fast_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fast Time
                        locator = tempBattleWeapon.fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Slow Chance
                        locator = tempBattleWeapon.slow_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.slow_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Slow Time
                        locator = tempBattleWeapon.slow_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.slow_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fright Chance
                        locator = tempBattleWeapon.fright_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fright_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Gale Force Chance
                        locator = tempBattleWeapon.gale_force_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.gale_force_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Payback Time
                        locator = tempBattleWeapon.payback_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.payback_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Hold Fast Time
                        locator = tempBattleWeapon.hold_fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.hold_fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Invisible Chance
                        locator = tempBattleWeapon.invisible_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.invisible_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Invisible Time
                        locator = tempBattleWeapon.invisible_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.invisible_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //HP Regen Time
                        locator = tempBattleWeapon.hp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.hp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //HP Regen Strength
                        locator = tempBattleWeapon.hp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.hp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //FP Regen Time
                        locator = tempBattleWeapon.fp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //FP Regen Strength
                        locator = tempBattleWeapon.fp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_background_fallweight1
                        locator = tempBattleWeapon.stage_background_fallweight1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight1 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_background_fallweight2
                        locator = tempBattleWeapon.stage_background_fallweight2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight2 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_background_fallweight3
                        locator = tempBattleWeapon.stage_background_fallweight3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight3 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_background_fallweight4
                        locator = tempBattleWeapon.stage_background_fallweight4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight4 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_nozzle_turn_chance
                        locator = tempBattleWeapon.stage_nozzle_turn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_nozzle_turn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_nozzle_fire_chance
                        locator = tempBattleWeapon.stage_nozzle_fire_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_nozzle_fire_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_ceiling_fall_chance
                        locator = tempBattleWeapon.stage_ceiling_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_ceiling_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_object_fall_chance
                        locator = tempBattleWeapon.stage_object_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_object_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        attackNameCounter++;
                        units.get(i).addBattleWeaponStruct(tempBattleWeapon);
                    }
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            //BattleUnitKindPart
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(5)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(5)).get("Multiple");
            nameCounter = 0;
            int offsetCounter = 0;
            for(int i = 0; i < units.size(); i++)
            {
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleCheck == -1)
                {
                    BattleUnitKindPart tempBattleUnitKindPart = new BattleUnitKindPart();

                    //PartsAttributeFlags
                    int locator = tempBattleUnitKindPart.PartsAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleUnitKindPart.MostPreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MostPreferredSelectTarget);
                    tempBattleUnitKindPart.PreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreferredSelectTarget);
                    tempBattleUnitKindPart.SelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SelectTarget);
                    tempBattleUnitKindPart.Unknown1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown1);
                    tempBattleUnitKindPart.Unknown2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown2);
                    tempBattleUnitKindPart.Unknown3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown3);
                    tempBattleUnitKindPart.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
                    tempBattleUnitKindPart.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
                    tempBattleUnitKindPart.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
                    tempBattleUnitKindPart.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
                    tempBattleUnitKindPart.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
                    tempBattleUnitKindPart.Unknown4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown4);
                    tempBattleUnitKindPart.Unknown5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown5);
                    tempBattleUnitKindPart.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
                    tempBattleUnitKindPart.Unknown6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown6);
                    tempBattleUnitKindPart.Unknown7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown7);
                    tempBattleUnitKindPart.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
                    tempBattleUnitKindPart.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
                    tempBattleUnitKindPart.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
                    tempBattleUnitKindPart.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
                    tempBattleUnitKindPart.Unknown8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown8);
                    tempBattleUnitKindPart.Unknown9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown9);
                    tempBattleUnitKindPart.Unknown10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown10);
                    tempBattleUnitKindPart.Unknown11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown11);
                    tempBattleUnitKindPart.Unknown12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown12);
                    tempBattleUnitKindPart.Unknown13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown13);
                    tempBattleUnitKindPart.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
                    tempBattleUnitKindPart.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
                    tempBattleUnitKindPart.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);

                    //PartsCounterAttributeFlags
                    locator = tempBattleUnitKindPart.PartsCounterAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleUnitKindPart.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
                    tempBattleUnitKindPart.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
                    tempBattleUnitKindPart.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
                    tempBattleUnitKindPart.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
                    tempBattleUnitKindPart.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
                    tempBattleUnitKindPart.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
                    tempBattleUnitKindPart.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
                    tempBattleUnitKindPart.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
                    tempBattleUnitKindPart.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
                    tempBattleUnitKindPart.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
                    tempBattleUnitKindPart.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
                    tempBattleUnitKindPart.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
                    tempBattleUnitKindPart.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);

                    offsetCounter++;
                    units.get(i).addBattleUnitKindPartStruct(tempBattleUnitKindPart);
                }
                else if(Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))) != 0)
                {
                    for(int j = 0; j < Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))); j++)
                    {
                        BattleUnitKindPart tempBattleUnitKindPart = new BattleUnitKindPart();

                        //PartsAttributeFlags
                        int locator = tempBattleUnitKindPart.PartsAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleUnitKindPart.MostPreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MostPreferredSelectTarget);
                        tempBattleUnitKindPart.PreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreferredSelectTarget);
                        tempBattleUnitKindPart.SelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SelectTarget);
                        tempBattleUnitKindPart.Unknown1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown1);
                        tempBattleUnitKindPart.Unknown2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown2);
                        tempBattleUnitKindPart.Unknown3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown3);
                        tempBattleUnitKindPart.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
                        tempBattleUnitKindPart.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
                        tempBattleUnitKindPart.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
                        tempBattleUnitKindPart.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
                        tempBattleUnitKindPart.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
                        tempBattleUnitKindPart.Unknown4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown4);
                        tempBattleUnitKindPart.Unknown5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown5);
                        tempBattleUnitKindPart.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
                        tempBattleUnitKindPart.Unknown6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown6);
                        tempBattleUnitKindPart.Unknown7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown7);
                        tempBattleUnitKindPart.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
                        tempBattleUnitKindPart.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
                        tempBattleUnitKindPart.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
                        tempBattleUnitKindPart.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
                        tempBattleUnitKindPart.Unknown8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown8);
                        tempBattleUnitKindPart.Unknown9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown9);
                        tempBattleUnitKindPart.Unknown10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown10);
                        tempBattleUnitKindPart.Unknown11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown11);
                        tempBattleUnitKindPart.Unknown12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown12);
                        tempBattleUnitKindPart.Unknown13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown13);
                        tempBattleUnitKindPart.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
                        tempBattleUnitKindPart.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
                        tempBattleUnitKindPart.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);

                        //PartsCounterAttributeFlags
                        locator = tempBattleUnitKindPart.PartsCounterAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleUnitKindPart.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
                        tempBattleUnitKindPart.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
                        tempBattleUnitKindPart.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
                        tempBattleUnitKindPart.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
                        tempBattleUnitKindPart.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
                        tempBattleUnitKindPart.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
                        tempBattleUnitKindPart.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
                        tempBattleUnitKindPart.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
                        tempBattleUnitKindPart.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
                        tempBattleUnitKindPart.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
                        tempBattleUnitKindPart.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
                        tempBattleUnitKindPart.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
                        tempBattleUnitKindPart.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);

                        offsetCounter++;
                        units.get(i).addBattleUnitKindPartStruct(tempBattleUnitKindPart);
                    }
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
        int BWOffsetCounter = 0;
        int BUKPOffsetCounter = 0;

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

                    //DangerHP
                    locator = units.get(i).BattleUnitKindData.get(j).dangerHP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).dangerHP);

                    //PerilHP
                    locator = units.get(i).BattleUnitKindData.get(j).perilHP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).perilHP);

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

                    //SwallowChance
                    locator = units.get(i).BattleUnitKindData.get(j).swallowChance_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowChance);

                    //SwallowAttribute
                    locator = units.get(i).BattleUnitKindData.get(j).swallowAttribute_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowAttribute);

                    //UltraHammerKnockChance
                    locator = units.get(i).BattleUnitKindData.get(j).ultraHammerKnockChance_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).ultraHammerKnockChance);

                    //ItemStealParameters
                    locator = units.get(i).BattleUnitKindData.get(j).itemStealParameter_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).itemStealParameter);

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

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(4)).get("Offsets");
                //BattleWeapon
                for(int j = 0; j < units.get(i).BattleWeaponData.size(); j++)
                {
                    //Accuracy
                    locator = units.get(i).BattleWeaponData.get(j).accuracy_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).accuracy);

                    //FP Cost
                    locator = units.get(i).BattleWeaponData.get(j).fp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_cost);

                    //SP Cost
                    locator = units.get(i).BattleWeaponData.get(j).sp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sp_cost);

                    //Superguard State
                    locator = units.get(i).BattleWeaponData.get(j).superguard_state_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).superguard_state);

                    //Stylish Multiplier
                    locator = units.get(i).BattleWeaponData.get(j).sylish_multiplier_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sylish_multiplier);

                    //Bingo Slot Increase Chance
                    locator = units.get(i).BattleWeaponData.get(j).bingo_slot_inc_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).bingo_slot_inc_chance);

                    //Base Damage 1
                    locator = units.get(i).BattleWeaponData.get(j).base_damage1_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage1);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD1[k];
                    }

                    //Base Damage 2
                    locator = units.get(i).BattleWeaponData.get(j).base_damage2_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage2);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD2[k];
                    }

                    //Base Damage 3
                    locator = units.get(i).BattleWeaponData.get(j).base_damage3_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage3);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD3[k];
                    }

                    //Base Damage 4
                    locator = units.get(i).BattleWeaponData.get(j).base_damage4_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage4);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD4[k];
                    }

                    //Base Damage 5
                    locator = units.get(i).BattleWeaponData.get(j).base_damage5_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage5);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD5[k];
                    }

                    //Base Damage 6
                    locator = units.get(i).BattleWeaponData.get(j).base_damage6_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage6);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD6[k];
                    }

                    //Base Damage 7
                    locator = units.get(i).BattleWeaponData.get(j).base_damage7_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage7);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD7[k];
                    }

                    //Base Damage 8
                    locator = units.get(i).BattleWeaponData.get(j).base_damage8_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage8);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD8[k];
                    }

                    //Base FP Damage 1
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage1_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage1);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD1[k];
                    }

                    //Base FP Damage 2
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage2_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage2);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD2[k];
                    }

                    //Base FP Damage 3
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage3_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage3);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD3[k];
                    }

                    //Base FP Damage 4
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage4_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage4);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD4[k];
                    }

                    //Base FP Damage 5
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage5_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage5);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD5[k];
                    }

                    //Base FP Damage 6
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage6_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage6);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD6[k];
                    }

                    //Base FP Damage 7
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage7_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage7);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD7[k];
                    }

                    //Base FP Damage 8
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage8_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage8);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD8[k];
                    }

                    //TargetClassFlags
                    locator = units.get(i).BattleWeaponData.get(j).TargetClassFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempTCF = ByteUtils.buildTCFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempTCF[k];
                    }

                    //TargetPropertyFlags
                    locator = units.get(i).BattleWeaponData.get(j).TargetPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempTPF = ByteUtils.buildTPFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempTPF[k];
                    }

                    //Element
                    locator = units.get(i).BattleWeaponData.get(j).element_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).element);

                    //Damage Pattern
                    locator = units.get(i).BattleWeaponData.get(j).damage_pattern_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).damage_pattern);

                    //AC Level
                    locator = units.get(i).BattleWeaponData.get(j).ac_level_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ac_level);

                    //SpecialPropertyFlags
                    locator = units.get(i).BattleWeaponData.get(j).SpecialPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempSPF = ByteUtils.buildSPFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempSPF[k];
                    }

                    //CounterResistanceFlags
                    locator = units.get(i).BattleWeaponData.get(j).CounterResistanceFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempCRF = ByteUtils.buildCRFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempCRF[k];
                    }

                    //TargetWeightingFlags
                    locator = units.get(i).BattleWeaponData.get(j).TargetWeightingFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempTWF = ByteUtils.buildTWFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempTWF[k];
                    }

                    //Sleep Chance
                    locator = units.get(i).BattleWeaponData.get(j).sleep_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_chance);

                    //Sleep Time
                    locator = units.get(i).BattleWeaponData.get(j).sleep_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_time);

                    //Stop Chance
                    locator = units.get(i).BattleWeaponData.get(j).stop_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_chance);

                    //Stop Time
                    locator = units.get(i).BattleWeaponData.get(j).stop_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_time);

                    //Dizzy Chance
                    locator = units.get(i).BattleWeaponData.get(j).dizzy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_chance);

                    //Dizzy Time
                    locator = units.get(i).BattleWeaponData.get(j).dizzy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_time);

                    //Poison Chance
                    locator = units.get(i).BattleWeaponData.get(j).poison_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_chance);

                    //Poison Time
                    locator = units.get(i).BattleWeaponData.get(j).poison_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_time);

                    //Poison Strength
                    locator = units.get(i).BattleWeaponData.get(j).poison_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_strength);

                    //Confuse Chance
                    locator = units.get(i).BattleWeaponData.get(j).confuse_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_chance);

                    //Confuse Time
                    locator = units.get(i).BattleWeaponData.get(j).confuse_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_time);

                    //Electric Chance
                    locator = units.get(i).BattleWeaponData.get(j).electric_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_chance);

                    //Electric Time
                    locator = units.get(i).BattleWeaponData.get(j).electric_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_time);

                    //Dodgy Chance
                    locator = units.get(i).BattleWeaponData.get(j).dodgy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_chance);

                    //Dodgy Time
                    locator = units.get(i).BattleWeaponData.get(j).dodgy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_time);

                    //Burn Chance
                    locator = units.get(i).BattleWeaponData.get(j).burn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_chance);

                    //Burn Time
                    locator = units.get(i).BattleWeaponData.get(j).burn_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_time);

                    //Freeze Chance
                    locator = units.get(i).BattleWeaponData.get(j).freeze_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_chance);

                    //Freeze Time
                    locator = units.get(i).BattleWeaponData.get(j).freeze_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_time);

                    //Size Change Chance
                    locator = units.get(i).BattleWeaponData.get(j).size_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_chance);

                    //Size Change Time
                    locator = units.get(i).BattleWeaponData.get(j).size_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_time);

                    //Size Change Strength
                    locator = units.get(i).BattleWeaponData.get(j).size_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_strength);

                    //Atk Change Chance
                    locator = units.get(i).BattleWeaponData.get(j).atk_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_chance);

                    //Atk Change Time
                    locator = units.get(i).BattleWeaponData.get(j).atk_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_time);

                    //Atk Change Strength
                    locator = units.get(i).BattleWeaponData.get(j).atk_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_strength);

                    //Def Change Chance
                    locator = units.get(i).BattleWeaponData.get(j).def_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_chance);

                    //Def Change Time
                    locator = units.get(i).BattleWeaponData.get(j).def_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_time);

                    //Def Change Strength
                    locator = units.get(i).BattleWeaponData.get(j).def_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_strength);

                    //Allergic Chance
                    locator = units.get(i).BattleWeaponData.get(j).allergic_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_chance);

                    //Allergic Time
                    locator = units.get(i).BattleWeaponData.get(j).allergic_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_time);

                    //OHKO Chance
                    locator = units.get(i).BattleWeaponData.get(j).ohko_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ohko_chance);

                    //Charge Strength
                    locator = units.get(i).BattleWeaponData.get(j).charge_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).charge_strength);

                    //Fast Chance
                    locator = units.get(i).BattleWeaponData.get(j).fast_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_chance);

                    //Fast Time
                    locator = units.get(i).BattleWeaponData.get(j).fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_time);

                    //Slow Chance
                    locator = units.get(i).BattleWeaponData.get(j).slow_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_chance);

                    //Slow Time
                    locator = units.get(i).BattleWeaponData.get(j).slow_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_time);

                    //Fright Chance
                    locator = units.get(i).BattleWeaponData.get(j).fright_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fright_chance);

                    //Gale Force Chance
                    locator = units.get(i).BattleWeaponData.get(j).gale_force_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).gale_force_chance);

                    //Payback Time
                    locator = units.get(i).BattleWeaponData.get(j).payback_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).payback_time);

                    //Hold Fast Time
                    locator = units.get(i).BattleWeaponData.get(j).hold_fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hold_fast_time);

                    //Invisible Chance
                    locator = units.get(i).BattleWeaponData.get(j).invisible_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_chance);

                    //Invisible Time
                    locator = units.get(i).BattleWeaponData.get(j).invisible_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_time);

                    //HP Regen Time
                    locator = units.get(i).BattleWeaponData.get(j).hp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_time);

                    //HP Regen Strength
                    locator = units.get(i).BattleWeaponData.get(j).hp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_strength);

                    //FP Regen Time
                    locator = units.get(i).BattleWeaponData.get(j).fp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_time);

                    //FP Regen Strength
                    locator = units.get(i).BattleWeaponData.get(j).fp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_strength);

                    //stage_background_fallweight1
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight1_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight1);

                    //stage_background_fallweight2
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight2_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight2);

                    //stage_background_fallweight3
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight3_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight3);

                    //stage_background_fallweight4
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight4_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight4);

                    //stage_nozzle_turn_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_nozzle_turn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_turn_chance);

                    //stage_nozzle_fire_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_nozzle_fire_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_fire_chance);

                    //stage_ceiling_fall_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_ceiling_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_ceiling_fall_chance);

                    //stage_object_fall_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_object_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_object_fall_chance);

                    BWOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(5)).get("Offsets");
                //BattleUnitKindPart
                for(int j = 0; j < units.get(i).BattleUnitKindPartData.size(); j++)
                {
                    //PartsAttributeFlags
                    locator = units.get(i).BattleUnitKindPartData.get(j).PartsAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKPOffsetCounter));
                    byte[] tempPAF = ByteUtils.buildPAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempPAF[k];
                    }

                    //PartsCounterAttributeFlags
                    locator = units.get(i).BattleUnitKindPartData.get(j).PartsCounterAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKPOffsetCounter));
                    byte[] tempPCAF = ByteUtils.buildPCAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempPCAF[k];
                    }

                    BUKPOffsetCounter++;
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

    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given Battle Unit Preset file and the parse it into an array of UnitData
     */
    public static ArrayList<UnitData> getPresetData(File presetFile)
    {
        byte[] presetFileData = ByteUtils.readData(presetFile);
        ArrayList<UnitData> units = new ArrayList<UnitData>();
        UnitData tempUnit = new UnitData();
        int locator = 0;
        int unitSize = 0;
        int size = 0;

        if(presetFileData[locator] == 0x00)
        {
            //File
            locator++;
            unitSize = presetFileData[locator];
            locator++;

            for(int j = 0; j < unitSize; j++)
            {
                tempUnit = new UnitData();

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitKind tempBUK = new BattleUnitKind();

                        //HP
                        tempBUK.HP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //DangerHP
                        tempBUK.dangerHP = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //PerilHP
                        tempBUK.perilHP = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Level
                        tempBUK.level = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //BonusXP
                        tempBUK.bonusXP = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //BonusCoin
                        tempBUK.bonusCoin = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //BonusCoinRate
                        tempBUK.bonusCoinRate = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //BaseCoin
                        tempBUK.baseCoin = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //RunRate
                        tempBUK.runRate = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //pbCap
                        tempBUK.pbCap = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //SwallowChance
                        tempBUK.swallowChance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //SwallowAttribute
                        tempBUK.swallowAttribute = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //UltraHammerKnockChance
                        tempBUK.ultraHammerKnockChance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //ItemStealParameter
                        tempBUK.itemStealParameter = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addBattleUnitKindStruct(tempBUK);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitKindPart tempBUKP = new BattleUnitKindPart();

                        int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBUKP.MostPreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MostPreferredSelectTarget);
                        tempBUKP.PreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreferredSelectTarget);
                        tempBUKP.SelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SelectTarget);
                        tempBUKP.Unknown1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown1);
                        tempBUKP.Unknown2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown2);
                        tempBUKP.Unknown3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown3);
                        tempBUKP.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
                        tempBUKP.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
                        tempBUKP.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
                        tempBUKP.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
                        tempBUKP.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
                        tempBUKP.Unknown4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown4);
                        tempBUKP.Unknown5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown5);
                        tempBUKP.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
                        tempBUKP.Unknown6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown6);
                        tempBUKP.Unknown7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown7);
                        tempBUKP.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
                        tempBUKP.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
                        tempBUKP.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
                        tempBUKP.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
                        tempBUKP.Unknown8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown8);
                        tempBUKP.Unknown9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown9);
                        tempBUKP.Unknown10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown10);
                        tempBUKP.Unknown11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown11);
                        tempBUKP.Unknown12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown12);
                        tempBUKP.Unknown13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown13);
                        tempBUKP.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
                        tempBUKP.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
                        tempBUKP.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);
                        locator += 4;

                        //PartsCounterAttributeFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBUKP.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
                        tempBUKP.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
                        tempBUKP.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
                        tempBUKP.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
                        tempBUKP.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
                        tempBUKP.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
                        tempBUKP.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
                        tempBUKP.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
                        tempBUKP.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
                        tempBUKP.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
                        tempBUKP.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
                        tempBUKP.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
                        tempBUKP.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);
                        locator += 4;

                        tempUnit.addBattleUnitKindPartStruct(tempBUKP);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitDefense tempBUD = new BattleUnitDefense();

                        //Normal
                        tempBUD.normal = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fire
                        tempBUD.fire = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Ice
                        tempBUD.ice = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Explosion
                        tempBUD.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric
                        tempBUD.electric = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addBattleUnitDefenseStruct(tempBUD);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitDefenseAttr tempBUDA = new BattleUnitDefenseAttr();

                        //Normal
                        tempBUDA.normal = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fire
                        tempBUDA.fire = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Ice
                        tempBUDA.ice = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Explosion
                        tempBUDA.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric
                        tempBUDA.electric = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addBattleUnitDefenseAttrStruct(tempBUDA);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        StatusVulnerability tempSV = new StatusVulnerability();

                        //Sleep
                        tempSV.sleep = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stop
                        tempSV.stop = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Dizzy
                        tempSV.dizzy = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Poison
                        tempSV.poison = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Confuse
                        tempSV.confuse = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Electric
                        tempSV.electric = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Burn
                        tempSV.burn = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Freeze
                        tempSV.freeze = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Huge
                        tempSV.huge = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Tiny
                        tempSV.tiny = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Attack Up
                        tempSV.attack_up = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Attack Down
                        tempSV.attack_down = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Defense Up
                        tempSV.defense_up = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Defense Down
                        tempSV.defense_down = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Allergic
                        tempSV.allergic = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Fright
                        tempSV.fright = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Gale Force
                        tempSV.gale_force = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Fast
                        tempSV.fast = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Slow
                        tempSV.slow = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;               
                        
                        //Dodgy
                        tempSV.dodgy = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Invisible
                        tempSV.invisible = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //OHKO             
                        tempSV.ohko = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addStatusVulnerabilityStruct(tempSV);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleWeapon tempBW = new BattleWeapon();

                        //Accuracy
                        tempBW.accuracy = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //FP Cost
                        tempBW.fp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //SP Cost
                        tempBW.sp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Superguard State
                        tempBW.superguard_state = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stylish Multiplier
                        tempBW.sylish_multiplier = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Bingo Slot Increase Chance
                        tempBW.bingo_slot_inc_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Base Damage 1
                        tempBW.base_damage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 2
                        tempBW.base_damage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 3
                        tempBW.base_damage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 4
                        tempBW.base_damage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 5
                        tempBW.base_damage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 6
                        tempBW.base_damage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 7
                        tempBW.base_damage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 8
                        tempBW.base_damage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 1
                        tempBW.base_fpdamage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 2
                        tempBW.base_fpdamage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 3
                        tempBW.base_fpdamage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 4
                        tempBW.base_fpdamage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 5
                        tempBW.base_fpdamage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 6
                        tempBW.base_fpdamage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 7
                        tempBW.base_fpdamage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 8
                        tempBW.base_fpdamage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //TargetClassFlags
                        int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
                        tempBW.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
                        tempBW.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
                        tempBW.Unused1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused1);
                        tempBW.Unused2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused2);
                        tempBW.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
                        tempBW.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
                        tempBW.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
                        tempBW.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
                        tempBW.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
                        tempBW.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
                        tempBW.Unused3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused3);
                        tempBW.Unused4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused4);
                        tempBW.Unused5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused5);
                        tempBW.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
                        tempBW.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
                        tempBW.Unused6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused6);
                        locator += 4;

                        //TargetPropertyFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
                        tempBW.Unused7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused7);
                        tempBW.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
                        tempBW.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
                        tempBW.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
                        tempBW.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
                        tempBW.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
                        tempBW.Unused8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused8);
                        tempBW.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
                        tempBW.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
                        tempBW.Unused9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused9);
                        tempBW.Unused10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused10);
                        tempBW.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
                        tempBW.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);
                        locator += 4;

                        //Element
                        tempBW.element = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Damage Pattern
                        tempBW.damage_pattern = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //AC Level
                        tempBW.ac_level = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //SpecialPropertyFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
                        tempBW.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
                        tempBW.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
                        tempBW.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
                        tempBW.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
                        tempBW.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
                        tempBW.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
                        tempBW.Unused11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused11);
                        tempBW.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
                        tempBW.Unused12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused12);
                        tempBW.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
                        tempBW.Unused13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused13);
                        tempBW.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
                        tempBW.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
                        tempBW.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
                        tempBW.Unused14 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused14);
                        tempBW.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
                        tempBW.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
                        tempBW.Unused15 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused15);
                        locator += 4;

                        //CounterResistanceFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
                        tempBW.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
                        tempBW.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
                        tempBW.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
                        tempBW.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
                        tempBW.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
                        tempBW.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
                        tempBW.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
                        tempBW.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
                        tempBW.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
                        tempBW.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);
                        locator += 4;

                        //TargetWeightingFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
                        tempBW.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
                        tempBW.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
                        tempBW.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
                        tempBW.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
                        tempBW.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
                        tempBW.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
                        tempBW.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
                        tempBW.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
                        tempBW.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
                        tempBW.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
                        tempBW.Unused16 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.Unused16);
                        tempBW.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);
                        locator += 4;

                        //Sleep Chance
                        tempBW.sleep_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Sleep Time
                        tempBW.sleep_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stop Chance
                        tempBW.stop_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stop Time
                        tempBW.stop_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dizzy Chance
                        tempBW.dizzy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dizzy Time
                        tempBW.dizzy_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Poison Chance
                        tempBW.poison_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Poison Time
                        tempBW.poison_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Poison Strength
                        tempBW.poison_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Confuse Chance
                        tempBW.confuse_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Confuse Time
                        tempBW.confuse_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric Chance
                        tempBW.electric_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric Time
                        tempBW.electric_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dodgy Chance
                        tempBW.dodgy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dodgy Time
                        tempBW.dodgy_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Burn Chance
                        tempBW.burn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Burn Time
                        tempBW.burn_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Freeze Chance
                        tempBW.freeze_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Freeze Time
                        tempBW.freeze_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Size Change Chance
                        tempBW.size_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Size Change Time
                        tempBW.size_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Size Change Strength
                        tempBW.size_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Atk Change Chance
                        tempBW.atk_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Atk Change Time
                        tempBW.atk_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Atk Change Strength
                        tempBW.atk_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Def Change Chance
                        tempBW.def_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Def Change Time
                        tempBW.def_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Def Change Strength
                        tempBW.def_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Allergic Chance
                        tempBW.allergic_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Allergic Time
                        tempBW.allergic_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //OHKO Chance
                        tempBW.ohko_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Charge Strength
                        tempBW.charge_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fast Chance
                        tempBW.fast_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fast Time
                        tempBW.fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Slow Chance
                        tempBW.slow_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Slow Time
                        tempBW.slow_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fright Chance
                        tempBW.fright_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Gale Force Chance
                        tempBW.gale_force_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Payback Time
                        tempBW.payback_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Hold Fast Time
                        tempBW.hold_fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Invisible Chance
                        tempBW.invisible_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Invisible Time
                        tempBW.invisible_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //HP Regen Time
                        tempBW.hp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //HP Regen Strength
                        tempBW.hp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //FP Regen Time
                        tempBW.fp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //FP Regen Strength
                        tempBW.fp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_background_fallweight1
                        tempBW.stage_background_fallweight1 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_background_fallweight2
                        tempBW.stage_background_fallweight2 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_background_fallweight3
                        tempBW.stage_background_fallweight3 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_background_fallweight4
                        tempBW.stage_background_fallweight4 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_nozzle_turn_chance
                        tempBW.stage_nozzle_turn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_nozzle_fire_chance
                        tempBW.stage_nozzle_fire_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_ceiling_fall_chance
                        tempBW.stage_ceiling_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_object_fall_chance
                        tempBW.stage_object_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addBattleWeaponStruct(tempBW);
                    }
                }
                    
                units.add(tempUnit);
            }

            return units;
        }
        else if(presetFileData[locator] == 0x01)
        {
            //BattleUnitKind
            locator++;
            BattleUnitKind tempBUK = new BattleUnitKind();

            //HP
            tempBUK.HP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //DangerHP
            tempBUK.dangerHP = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //PerilHP
            tempBUK.perilHP = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Level
            tempBUK.level = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //BonusXP
            tempBUK.bonusXP = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //BonusCoin
            tempBUK.bonusCoin = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //BonusCoinRate
            tempBUK.bonusCoinRate = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //BaseCoin
            tempBUK.baseCoin = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //RunRate
            tempBUK.runRate = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //pbCap
            tempBUK.pbCap = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //SwallowChance
            tempBUK.swallowChance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //SwallowAttribute
            tempBUK.swallowAttribute = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //UltraHammerKnockChance
            tempBUK.ultraHammerKnockChance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //ItemStealParameter
            tempBUK.itemStealParameter = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addBattleUnitKindStruct(tempBUK);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x02)
        {
            //BattleUnitKindPart
            locator++;
            BattleUnitKindPart tempBUKP = new BattleUnitKindPart();

            int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBUKP.MostPreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MostPreferredSelectTarget);
            tempBUKP.PreferredSelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreferredSelectTarget);
            tempBUKP.SelectTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SelectTarget);
            tempBUKP.Unknown1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown1);
            tempBUKP.Unknown2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown2);
            tempBUKP.Unknown3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown3);
            tempBUKP.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
            tempBUKP.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
            tempBUKP.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
            tempBUKP.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
            tempBUKP.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
            tempBUKP.Unknown4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown4);
            tempBUKP.Unknown5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown5);
            tempBUKP.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
            tempBUKP.Unknown6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown6);
            tempBUKP.Unknown7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown7);
            tempBUKP.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
            tempBUKP.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
            tempBUKP.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
            tempBUKP.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
            tempBUKP.Unknown8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown8);
            tempBUKP.Unknown9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown9);
            tempBUKP.Unknown10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown10);
            tempBUKP.Unknown11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown11);
            tempBUKP.Unknown12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown12);
            tempBUKP.Unknown13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Unknown13);
            tempBUKP.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
            tempBUKP.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
            tempBUKP.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);
            locator += 4;

            //PartsCounterAttributeFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBUKP.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
            tempBUKP.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
            tempBUKP.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
            tempBUKP.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
            tempBUKP.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
            tempBUKP.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
            tempBUKP.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
            tempBUKP.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
            tempBUKP.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
            tempBUKP.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
            tempBUKP.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
            tempBUKP.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
            tempBUKP.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);
            locator += 4;

            tempUnit.addBattleUnitKindPartStruct(tempBUKP);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x03)
        {
            //BattleUnitDefense
            locator++;
            BattleUnitDefense tempBUD = new BattleUnitDefense();

            //Normal
            tempBUD.normal = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fire
            tempBUD.fire = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Ice
            tempBUD.ice = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Explosion
            tempBUD.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric
            tempBUD.electric = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addBattleUnitDefenseStruct(tempBUD);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x04)
        {
            //BattleUnitDefenseAttr
            locator++;
            BattleUnitDefenseAttr tempBUDA = new BattleUnitDefenseAttr();

            //Normal
            tempBUDA.normal = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fire
            tempBUDA.fire = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Ice
            tempBUDA.ice = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Explosion
            tempBUDA.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric
            tempBUDA.electric = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addBattleUnitDefenseAttrStruct(tempBUDA);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x05)
        {
            //StatusVulnerability
            locator++;
            StatusVulnerability tempSV = new StatusVulnerability();

            //Sleep
            tempSV.sleep = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stop
            tempSV.stop = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Dizzy
            tempSV.dizzy = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Poison
            tempSV.poison = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Confuse
            tempSV.confuse = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Electric
            tempSV.electric = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Burn
            tempSV.burn = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Freeze
            tempSV.freeze = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Huge
            tempSV.huge = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Tiny
            tempSV.tiny = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Attack Up
            tempSV.attack_up = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Attack Down
            tempSV.attack_down = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Defense Up
            tempSV.defense_up = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Defense Down
            tempSV.defense_down = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Allergic
            tempSV.allergic = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Fright
            tempSV.fright = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Gale Force
            tempSV.gale_force = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Fast
            tempSV.fast = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Slow
            tempSV.slow = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;               
            
            //Dodgy
            tempSV.dodgy = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Invisible
            tempSV.invisible = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //OHKO             
            tempSV.ohko = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addStatusVulnerabilityStruct(tempSV);
            units.add(tempUnit);
            return units;
            
        }
        else if(presetFileData[locator] == 0x06)
        {
            //BattleWeapon
            locator++;
            BattleWeapon tempBW = new BattleWeapon();

            //Accuracy
            tempBW.accuracy = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //FP Cost
            tempBW.fp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //SP Cost
            tempBW.sp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Superguard State
            tempBW.superguard_state = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stylish Multiplier
            tempBW.sylish_multiplier = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Bingo Slot Increase Chance
            tempBW.bingo_slot_inc_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Base Damage 1
            tempBW.base_damage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 2
            tempBW.base_damage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 3
            tempBW.base_damage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 4
            tempBW.base_damage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 5
            tempBW.base_damage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 6
            tempBW.base_damage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 7
            tempBW.base_damage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 8
            tempBW.base_damage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 1
            tempBW.base_fpdamage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 2
            tempBW.base_fpdamage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 3
            tempBW.base_fpdamage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 4
            tempBW.base_fpdamage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 5
            tempBW.base_fpdamage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 6
            tempBW.base_fpdamage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 7
            tempBW.base_fpdamage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 8
            tempBW.base_fpdamage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //TargetClassFlags
            int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
            tempBW.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
            tempBW.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
            tempBW.Unused1 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused1);
            tempBW.Unused2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused2);
            tempBW.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
            tempBW.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
            tempBW.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
            tempBW.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
            tempBW.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
            tempBW.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
            tempBW.Unused3 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused3);
            tempBW.Unused4 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused4);
            tempBW.Unused5 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused5);
            tempBW.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
            tempBW.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
            tempBW.Unused6 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.Unused6);
            locator += 4;

            //TargetPropertyFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
            tempBW.Unused7 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused7);
            tempBW.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
            tempBW.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
            tempBW.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
            tempBW.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
            tempBW.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
            tempBW.Unused8 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused8);
            tempBW.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
            tempBW.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
            tempBW.Unused9 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused9);
            tempBW.Unused10 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Unused10);
            tempBW.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
            tempBW.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);
            locator += 4;

            //Element
            tempBW.element = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Damage Pattern
            tempBW.damage_pattern = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //AC Level
            tempBW.ac_level = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //SpecialPropertyFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
            tempBW.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
            tempBW.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
            tempBW.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
            tempBW.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
            tempBW.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
            tempBW.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
            tempBW.Unused11 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused11);
            tempBW.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
            tempBW.Unused12 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused12);
            tempBW.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
            tempBW.Unused13 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused13);
            tempBW.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
            tempBW.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
            tempBW.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
            tempBW.Unused14 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused14);
            tempBW.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
            tempBW.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
            tempBW.Unused15 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unused15);
            locator += 4;

            //CounterResistanceFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
            tempBW.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
            tempBW.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
            tempBW.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
            tempBW.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
            tempBW.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
            tempBW.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
            tempBW.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
            tempBW.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
            tempBW.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
            tempBW.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);
            locator += 4;

            //TargetWeightingFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
            tempBW.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
            tempBW.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
            tempBW.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
            tempBW.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
            tempBW.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
            tempBW.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
            tempBW.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
            tempBW.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
            tempBW.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
            tempBW.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
            tempBW.Unused16 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.Unused16);
            tempBW.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);
            locator += 4;

            //Sleep Chance
            tempBW.sleep_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Sleep Time
            tempBW.sleep_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stop Chance
            tempBW.stop_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stop Time
            tempBW.stop_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dizzy Chance
            tempBW.dizzy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dizzy Time
            tempBW.dizzy_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Poison Chance
            tempBW.poison_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Poison Time
            tempBW.poison_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Poison Strength
            tempBW.poison_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Confuse Chance
            tempBW.confuse_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Confuse Time
            tempBW.confuse_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric Chance
            tempBW.electric_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric Time
            tempBW.electric_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dodgy Chance
            tempBW.dodgy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dodgy Time
            tempBW.dodgy_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Burn Chance
            tempBW.burn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Burn Time
            tempBW.burn_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Freeze Chance
            tempBW.freeze_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Freeze Time
            tempBW.freeze_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Size Change Chance
            tempBW.size_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Size Change Time
            tempBW.size_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Size Change Strength
            tempBW.size_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Atk Change Chance
            tempBW.atk_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Atk Change Time
            tempBW.atk_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Atk Change Strength
            tempBW.atk_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Def Change Chance
            tempBW.def_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Def Change Time
            tempBW.def_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Def Change Strength
            tempBW.def_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Allergic Chance
            tempBW.allergic_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Allergic Time
            tempBW.allergic_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //OHKO Chance
            tempBW.ohko_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Charge Strength
            tempBW.charge_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fast Chance
            tempBW.fast_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fast Time
            tempBW.fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Slow Chance
            tempBW.slow_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Slow Time
            tempBW.slow_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fright Chance
            tempBW.fright_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Gale Force Chance
            tempBW.gale_force_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Payback Time
            tempBW.payback_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Hold Fast Time
            tempBW.hold_fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Invisible Chance
            tempBW.invisible_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Invisible Time
            tempBW.invisible_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //HP Regen Time
            tempBW.hp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //HP Regen Strength
            tempBW.hp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //FP Regen Time
            tempBW.fp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //FP Regen Strength
            tempBW.fp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_background_fallweight1
            tempBW.stage_background_fallweight1 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_background_fallweight2
            tempBW.stage_background_fallweight2 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_background_fallweight3
            tempBW.stage_background_fallweight3 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_background_fallweight4
            tempBW.stage_background_fallweight4 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_nozzle_turn_chance
            tempBW.stage_nozzle_turn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_nozzle_fire_chance
            tempBW.stage_nozzle_fire_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_ceiling_fall_chance
            tempBW.stage_ceiling_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_object_fall_chance
            tempBW.stage_object_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addBattleWeaponStruct(tempBW);
            units.add(tempUnit);
            return units;
        }

        return units;
    }

    /**
     * @Author Jemaroo
     * @Function Will export the array of units into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, ArrayList<UnitData> units)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x00;
        presetFileDataList.add(marker);

        presetFileDataList.add(ByteUtils.intTo1Byte(units.size()));
        for(int i = 0; i < units.size(); i++)
        {
            //BattleUnitKind
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.size()));
            for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
            {
                //HP
                byte[] tempHP = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).HP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempHP[k]);
                }

                //DangerHP
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).dangerHP));

                //PerilHP
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).perilHP));

                //Level
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).level));

                //BonusXP
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusXP));
                
                //BonusCoin
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoin));

                //BonusCoinRate
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoinRate));

                //BaseCoin
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).baseCoin));

                //RunRate
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).runRate));

                //pbCap
                byte[] tempPBCap = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).pbCap);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempPBCap[k]);
                }

                //SwallowChance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowChance));

                //SwallowAttribute
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowAttribute));

                //UltraHammerKnockChance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).ultraHammerKnockChance));

                //ItemStealParameter
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).itemStealParameter));
            }

            //BattleUnitKindPart
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindPartData.size()));
            for(int j = 0; j < units.get(i).BattleUnitKindPartData.size(); j++)
            {
                //PartsAttributeFlags
                byte[] tempPAF = ByteUtils.buildPAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempPAF[k]);
                }

                //PartsCounterAttributeFlags
                byte[] tempPCAF = ByteUtils.buildPCAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempPCAF[k]);
                }
            }

            //BattleUnitDefense
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.size()));
            for(int j = 0; j < units.get(i).BattleUnitDefenseData.size(); j++)
            {
                //Normal
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).normal));

                //Fire
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).fire));

                //Ice
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).ice));

                //Explosion
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).explosion));

                //Electric
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).electric));
            }

            //BattleUnitDefenseAttr
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.size()));
            for(int j = 0; j < units.get(i).BattleUnitDefenseAttrData.size(); j++)
            {
                //Normal
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).normal));

                //Fire
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).fire));

                //Ice
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).ice));

                //Explosion
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).explosion));

                //Electric
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).electric));
            }

            //StatusVulnerability
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.size()));
            for(int j = 0; j < units.get(i).StatusVulnerabilityData.size(); j++)
            {
                //Sleep
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).sleep));

                //Stop
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).stop));
                
                //Dizzy
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dizzy));
                
                //Poison
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).poison));
                
                //Confuse
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).confuse));
                
                //Electric
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).electric));
                
                //Burn
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).burn));
                
                //Freeze
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).freeze));
                
                //Huge
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).huge));
                
                //Tiny
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).tiny));
                
                //Attack Up
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_up));
                
                //Attack Down
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_down));
                
                //Defense Up
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_up));
                
                //Defense Down
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_down));
                
                //Allergic
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).allergic));
                
                //Fright
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fright));
                
                //Gale Force
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).gale_force));
                
                //Fast
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fast));
                
                //Slow
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).slow));                 
                
                //Dodgy
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dodgy));
                
                //Invisible
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).invisible));
                
                //OHKO             
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).ohko));
            }

            //BattleWeapon
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.size()));
            for(int j = 0; j < units.get(i).BattleWeaponData.size(); j++)
            {
                //Accuracy
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).accuracy));

                //FP Cost
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_cost));

                //SP Cost
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sp_cost));

                //Superguard State
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).superguard_state));

                //Stylish Multiplier
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sylish_multiplier));

                //Bingo Slot Increase Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).bingo_slot_inc_chance));

                //Base Damage 1
                byte[] tempBD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage1);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD1[k]);
                }

                //Base Damage 2
                byte[] tempBD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage2);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD2[k]);
                }

                //Base Damage 3
                byte[] tempBD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage3);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD3[k]);
                }

                //Base Damage 4
                byte[] tempBD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage4);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD4[k]);
                }

                //Base Damage 5
                byte[] tempBD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage5);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD5[k]);
                }

                //Base Damage 6
                byte[] tempBD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage6);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD6[k]);
                }

                //Base Damage 7
                byte[] tempBD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage7);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD7[k]);
                }

                //Base Damage 8
                byte[] tempBD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage8);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD8[k]);
                }

                //Base FP Damage 1
                byte[] tempBFPD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage1);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD1[k]);
                }

                //Base FP Damage 2
                byte[] tempBFPD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage2);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD2[k]);
                }

                //Base FP Damage 3
                byte[] tempBFPD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage3);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD3[k]);
                }

                //Base FP Damage 4
                byte[] tempBFPD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage4);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD4[k]);
                }

                //Base FP Damage 5
                byte[] tempBFPD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage5);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD5[k]);
                }

                //Base FP Damage 6
                byte[] tempBFPD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage6);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD6[k]);
                }

                //Base FP Damage 7
                byte[] tempBFPD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage7);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD7[k]);
                }

                //Base FP Damage 8
                byte[] tempBFPD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage8);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD8[k]);
                }

                //TargetClassFlags
                byte[] tempTCF = ByteUtils.buildTCFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempTCF[k]);
                }

                //TargetPropertyFlags
                byte[] tempTPF = ByteUtils.buildTPFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempTPF[k]);
                }

                //Element
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).element));

                //Damage Pattern
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).damage_pattern));

                //AC Level
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ac_level));

                //SpecialPropertyFlags
                byte[] tempSPF = ByteUtils.buildSPFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempSPF[k]);
                }

                //CounterResistanceFlags
                byte[] tempCRF = ByteUtils.buildCRFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempCRF[k]);
                }

                //TargetWeightingFlags
                byte[] tempTWF = ByteUtils.buildTWFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempTWF[k]);
                }

                //Sleep Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_chance));

                //Sleep Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_time));

                //Stop Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_chance));

                //Stop Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_time));

                //Dizzy Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_chance));

                //Dizzy Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_time));

                //Poison Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_chance));

                //Poison Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_time));

                //Poison Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_strength));

                //Confuse Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_chance));

                //Confuse Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_time));

                //Electric Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_chance));

                //Electric Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_time));

                //Dodgy Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_chance));

                //Dodgy Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_time));

                //Burn Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_chance));

                //Burn Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_time));

                //Freeze Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_chance));

                //Freeze Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_time));

                //Size Change Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_chance));

                //Size Change Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_time));

                //Size Change Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_strength));

                //Atk Change Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_chance));

                //Atk Change Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_time));

                //Atk Change Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_strength));

                //Def Change Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_chance));

                //Def Change Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_time));

                //Def Change Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_strength));

                //Allergic Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_chance));

                //Allergic Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_time));

                //OHKO Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ohko_chance));

                //Charge Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).charge_strength));

                //Fast Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_chance));

                //Fast Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_time));

                //Slow Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_chance));

                //Slow Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_time));

                //Fright Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fright_chance));

                //Gale Force Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).gale_force_chance));

                //Payback Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).payback_time));

                //Hold Fast Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hold_fast_time));

                //Invisible Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_chance));

                //Invisible Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_time));

                //HP Regen Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_time));

                //HP Regen Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_strength));

                //FP Regen Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_time));

                //FP Regen Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_strength));

                //stage_background_fallweight1
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight1));

                //stage_background_fallweight2
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight2));

                //stage_background_fallweight3
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight3));

                //stage_background_fallweight4
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight4));

                //stage_nozzle_turn_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_turn_chance));

                //stage_nozzle_fire_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_fire_chance));

                //stage_ceiling_fall_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_ceiling_fall_chance));

                //stage_object_fall_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_object_fall_chance));
            }
        }

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitKind into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitKind buk)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x01;
        presetFileDataList.add(marker);

        //HP
        byte[] tempHP = ByteUtils.intTo2Bytes(buk.HP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempHP[k]);
        }

        //DangerHP
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.dangerHP));

        //PerilHP
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.perilHP));

        //Level
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.level));

        //BonusXP
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.bonusXP));
        
        //BonusCoin
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.bonusCoin));

        //BonusCoinRate
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.bonusCoinRate));

        //BaseCoin
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.baseCoin));

        //RunRate
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.runRate));

        //pbCap
        byte[] tempPBCap = ByteUtils.intTo2Bytes(buk.pbCap);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempPBCap[k]);
        }

        //SwallowChance
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.swallowChance));

        //SwallowAttribute
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.swallowAttribute));

        //UltraHammerKnockChance
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.ultraHammerKnockChance));

        //ItemStealParameter
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.itemStealParameter));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitKindPart into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitKindPart bukp)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x02;
        presetFileDataList.add(marker);

        //PartsAttributeFlags
        byte[] tempPAF = ByteUtils.buildPAFBitfield(bukp);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempPAF[k]);
        }

        //PartsCounterAttributeFlags
        byte[] tempPCAF = ByteUtils.buildPCAFBitfield(bukp);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempPCAF[k]);
        }

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitDefense into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitDefense bud)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x03;
        presetFileDataList.add(marker);

        //Normal
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.normal));

        //Fire
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.fire));

        //Ice
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.ice));

        //Explosion
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.explosion));

        //Electric
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.electric));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitDefenseAttr into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitDefenseAttr buda)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x04;
        presetFileDataList.add(marker);

        //Normal
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.normal));

        //Fire
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.fire));

        //Ice
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.ice));

        //Explosion
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.explosion));

        //Electric
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.electric));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a StatusVulnerability into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, StatusVulnerability sv)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x05;
        presetFileDataList.add(marker);

        //Sleep
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.sleep));

        //Stop
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.stop));
        
        //Dizzy
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.dizzy));
        
        //Poison
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.poison));
        
        //Confuse
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.confuse));
        
        //Electric
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.electric));
        
        //Burn
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.burn));
        
        //Freeze
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.freeze));
        
        //Huge
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.huge));
        
        //Tiny
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.tiny));
        
        //Attack Up
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.attack_up));
        
        //Attack Down
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.attack_down));
        
        //Defense Up
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.defense_up));
        
        //Defense Down
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.defense_down));
        
        //Allergic
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.allergic));
        
        //Fright
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.fright));
        
        //Gale Force
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.gale_force));
        
        //Fast
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.fast));
        
        //Slow
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.slow));                 
        
        //Dodgy
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.dodgy));
        
        //Invisible
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.invisible));
        
        //OHKO             
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.ohko));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleWeapon into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleWeapon bw)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x06;
        presetFileDataList.add(marker);

        //Accuracy
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.accuracy));

        //FP Cost
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fp_cost));

        //SP Cost
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sp_cost));

        //Superguard State
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.superguard_state));

        //Stylish Multiplier
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sylish_multiplier));

        //Bingo Slot Increase Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.bingo_slot_inc_chance));

        //Base Damage 1
        byte[] tempBD1 = ByteUtils.intTo4Bytes(bw.base_damage1);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD1[k]);
        }

        //Base Damage 2
        byte[] tempBD2 = ByteUtils.intTo4Bytes(bw.base_damage2);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD2[k]);
        }

        //Base Damage 3
        byte[] tempBD3 = ByteUtils.intTo4Bytes(bw.base_damage3);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD3[k]);
        }

        //Base Damage 4
        byte[] tempBD4 = ByteUtils.intTo4Bytes(bw.base_damage4);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD4[k]);
        }

        //Base Damage 5
        byte[] tempBD5 = ByteUtils.intTo4Bytes(bw.base_damage5);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD5[k]);
        }

        //Base Damage 6
        byte[] tempBD6 = ByteUtils.intTo4Bytes(bw.base_damage6);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD6[k]);
        }

        //Base Damage 7
        byte[] tempBD7 = ByteUtils.intTo4Bytes(bw.base_damage7);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD7[k]);
        }

        //Base Damage 8
        byte[] tempBD8 = ByteUtils.intTo4Bytes(bw.base_damage8);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD8[k]);
        }

        //Base FP Damage 1
        byte[] tempBFPD1 = ByteUtils.intTo4Bytes(bw.base_fpdamage1);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD1[k]);
        }

        //Base FP Damage 2
        byte[] tempBFPD2 = ByteUtils.intTo4Bytes(bw.base_fpdamage2);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD2[k]);
        }

        //Base FP Damage 3
        byte[] tempBFPD3 = ByteUtils.intTo4Bytes(bw.base_fpdamage3);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD3[k]);
        }

        //Base FP Damage 4
        byte[] tempBFPD4 = ByteUtils.intTo4Bytes(bw.base_fpdamage4);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD4[k]);
        }

        //Base FP Damage 5
        byte[] tempBFPD5 = ByteUtils.intTo4Bytes(bw.base_fpdamage5);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD5[k]);
        }

        //Base FP Damage 6
        byte[] tempBFPD6 = ByteUtils.intTo4Bytes(bw.base_fpdamage6);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD6[k]);
        }

        //Base FP Damage 7
        byte[] tempBFPD7 = ByteUtils.intTo4Bytes(bw.base_fpdamage7);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD7[k]);
        }

        //Base FP Damage 8
        byte[] tempBFPD8 = ByteUtils.intTo4Bytes(bw.base_fpdamage8);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD8[k]);
        }

        //TargetClassFlags
        byte[] tempTCF = ByteUtils.buildTCFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempTCF[k]);
        }

        //TargetPropertyFlags
        byte[] tempTPF = ByteUtils.buildTPFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempTPF[k]);
        }

        //Element
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.element));

        //Damage Pattern
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.damage_pattern));

        //AC Level
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.ac_level));

        //SpecialPropertyFlags
        byte[] tempSPF = ByteUtils.buildSPFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempSPF[k]);
        }

        //CounterResistanceFlags
        byte[] tempCRF = ByteUtils.buildCRFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempCRF[k]);
        }

        //TargetWeightingFlags
        byte[] tempTWF = ByteUtils.buildTWFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempTWF[k]);
        }

        //Sleep Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sleep_chance));

        //Sleep Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sleep_time));

        //Stop Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stop_chance));

        //Stop Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stop_time));

        //Dizzy Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dizzy_chance));

        //Dizzy Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dizzy_time));

        //Poison Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.poison_chance));

        //Poison Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.poison_time));

        //Poison Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.poison_strength));

        //Confuse Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.confuse_chance));

        //Confuse Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.confuse_time));

        //Electric Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.electric_chance));

        //Electric Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.electric_time));

        //Dodgy Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dodgy_chance));

        //Dodgy Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dodgy_time));

        //Burn Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.burn_chance));

        //Burn Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.burn_time));

        //Freeze Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.freeze_chance));

        //Freeze Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.freeze_time));

        //Size Change Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.size_change_chance));

        //Size Change Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.size_change_time));

        //Size Change Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.size_change_strength));

        //Atk Change Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.atk_change_chance));

        //Atk Change Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.atk_change_time));

        //Atk Change Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.atk_change_strength));

        //Def Change Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.def_change_chance));

        //Def Change Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.def_change_time));

        //Def Change Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.def_change_strength));

        //Allergic Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.allergic_chance));

        //Allergic Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.allergic_time));

        //OHKO Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.ohko_chance));

        //Charge Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.charge_strength));

        //Fast Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fast_chance));

        //Fast Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fast_time));

        //Slow Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.slow_chance));

        //Slow Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.slow_time));

        //Fright Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fright_chance));

        //Gale Force Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.gale_force_chance));

        //Payback Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.payback_time));

        //Hold Fast Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.hold_fast_time));

        //Invisible Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.invisible_chance));

        //Invisible Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.invisible_time));

        //HP Regen Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.hp_regen_time));

        //HP Regen Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.hp_regen_strength));

        //FP Regen Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fp_regen_time));

        //FP Regen Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fp_regen_strength));

        //stage_background_fallweight1
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight1));

        //stage_background_fallweight2
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight2));

        //stage_background_fallweight3
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight3));

        //stage_background_fallweight4
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight4));

        //stage_nozzle_turn_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_nozzle_turn_chance));

        //stage_nozzle_fire_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_nozzle_fire_chance));

        //stage_ceiling_fall_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_ceiling_fall_chance));

        //stage_object_fall_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_object_fall_chance));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }
}