import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

            //HealthUpgrades
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(6)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(6)).get("Multiple");
            nameCounter = 0;
            offsetCounter = 0;
            for(int i = 0; i < units.size(); i++)
            {
                if(multipleExists(multipleArray, unitNameArray.get(nameCounter).toString()) == -1)
                {
                    HealthUpgrades tempHealthUpgrades = new HealthUpgrades();

                    //startHP / Partner: Level 1 HP
                    int locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    tempHealthUpgrades.startHP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    offsetCounter++;

                    //startFP / Partner: Level 2 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    tempHealthUpgrades.startFP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    offsetCounter++;

                    //startBP / Partner: Level 3 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    tempHealthUpgrades.startBP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    offsetCounter++;

                    if(unitNameArray.get(nameCounter).toString().equals("Mario"))
                    {
                        //upgradeHP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        tempHealthUpgrades.upgradeHP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        offsetCounter += 2;

                        //upgradeFP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        tempHealthUpgrades.upgradeFP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        offsetCounter += 2;

                        //upgradeBP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        tempHealthUpgrades.upgradeBP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        offsetCounter += 2;
                    }

                    units.get(i).addHealthUpgradeStruct(tempHealthUpgrades);
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
        int HUOffsetCounter = 0;

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

                //HealthUpgrades
                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(6)).get("Offsets");
                for(int j = 0; j < units.get(i).HealthUpgradesData.size(); j++)
                {
                    //startHP / Partner: Level 1 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                    byte[] tempStartHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startHP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempStartHP[k];
                    }
                    HUOffsetCounter++;

                    //startFP / Partner: Level 2 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                    byte[] tempStartFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startFP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempStartFP[k];
                    }
                    HUOffsetCounter++;

                    //startBP / Partner: Level 3 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                    byte[] tempStartBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startBP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempStartBP[k];
                    }
                    HUOffsetCounter++;

                    if(units.get(i).name.equals("Mario"))
                    {
                        //upgradeHP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        byte[] tempUpgradeHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeHP);
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeHP[k];
                        }
                        HUOffsetCounter++;
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeHP[k];
                        }
                        HUOffsetCounter++;

                        //upgradeFP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        byte[] tempUpgradeFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeFP);
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeFP[k];
                        }
                        HUOffsetCounter++;
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeFP[k];
                        }
                        HUOffsetCounter++;

                        //upgradeBP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        byte[] tempUpgradeBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeBP);
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeBP[k];
                        }
                        HUOffsetCounter++;
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeBP[k];
                        }
                        HUOffsetCounter++;
                    }
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
                        HealthUpgrades tempHU = new HealthUpgrades();

                        //startHP
                        tempHU.startHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //startFP
                        tempHU.startFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //startHP
                        tempHU.startBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //upgradeHP
                        tempHU.upgradeHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //upgradeFP
                        tempHU.upgradeFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //upgradeBP
                        tempHU.upgradeBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        tempUnit.addHealthUpgradeStruct(tempHU);
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
        else if(presetFileData[locator] == 0x07)
        {
            //HealthUpgrades
            locator++;
            HealthUpgrades tempHU = new HealthUpgrades();

            //startHP
            tempHU.startHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //startFP
            tempHU.startFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //startHP
            tempHU.startBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //upgradeHP
            tempHU.upgradeHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //upgradeFP
            tempHU.upgradeFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //upgradeBP
            tempHU.upgradeBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            tempUnit.addHealthUpgradeStruct(tempHU);
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

            //HealthUpgrades
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).HealthUpgradesData.size()));
            for(int j = 0; j < units.get(i).HealthUpgradesData.size(); j++)
            {
                //startHP
                byte[] tempStartHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startHP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempStartHP[k]);
                }

                //startFP
                byte[] tempStartFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startFP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempStartFP[k]);
                }

                //startBP
                byte[] tempStartBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startBP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempStartBP[k]);
                }

                //upgradeHP
                byte[] tempUpgradeHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeHP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempUpgradeHP[k]);
                }

                //upgradeFP
                byte[] tempUpgradeFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeFP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempUpgradeFP[k]);
                }

                //upgradeBP
                byte[] tempUpgradeBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeBP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempUpgradeBP[k]);
                }
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

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitKind into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, HealthUpgrades hu)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x07;
        presetFileDataList.add(marker);

        //startHP
        byte[] tempStartHP = ByteUtils.intTo2Bytes(hu.startHP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempStartHP[k]);
        }

        //startFP
        byte[] tempStartFP = ByteUtils.intTo2Bytes(hu.startFP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempStartFP[k]);
        }

        //startBP
        byte[] tempStartBP = ByteUtils.intTo2Bytes(hu.startBP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempStartBP[k]);
        }

        //upgradeHP
        byte[] tempUpgradeHP = ByteUtils.intTo2Bytes(hu.upgradeHP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempUpgradeHP[k]);
        }

        //upgradeFP
        byte[] tempUpgradeFP = ByteUtils.intTo2Bytes(hu.upgradeFP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempUpgradeFP[k]);
        }

        //upgradeBP
        byte[] tempUpgradeBP = ByteUtils.intTo2Bytes(hu.upgradeBP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempUpgradeBP[k]);
        }

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }
    
    public static TattleNames TattleNameConverter(String name, File openedFile)
    {
        switch (name) 
        {
            case "Magnus Von Grapple 2.0": return new TattleNames("btl_hlp_magnum_battender_mkII", "menu_enemy_140");
            case "X-Punch": return new TattleNames("btl_hlp_rocket_punch_mkII");
            case "X-Naut": return new TattleNames("btl_hlp_gundan_zako", "menu_enemy_018");
            case "Mini-Z-Yux": return new TattleNames("btl_hlp_barriern_z_petit", "menu_enemy_074");
            case "Z-Yux": return new TattleNames("btl_hlp_barriern_z", "menu_enemy_073");
            case "Elite X-Naut": return new TattleNames("btl_hlp_gundan_zako_elite", "menu_enemy_076");
            case "X-Naut PHD": return new TattleNames("btl_hlp_gundan_zako_magician", "menu_enemy_075");
            case "X-Yux": return new TattleNames("btl_hlp_barriern_custom", "menu_enemy_179");
            case "Mini-X-Yux": return new TattleNames("btl_hlp_barriern_custom_satellite", "menu_enemy_180");
            case "Yux": return new TattleNames("btl_hlp_barriern", "menu_enemy_016");
            case "Mini-Yux": return new TattleNames("btl_hlp_barriern_petit", "menu_enemy_017");
            case "Goomba": return new TattleNames("btl_hlp_kuriboo", "menu_enemy_001");
            case "Ice Puff": return new TattleNames("btl_hlp_bllizard", "menu_enemy_066");
            case "Frost Piranha": return new TattleNames("btl_hlp_ice_pakkun", "menu_enemy_047");
            case "Lava Bubble": return new TattleNames("btl_hlp_bubble", "menu_enemy_080");
            case "Bullet Bill": return new TattleNames("btl_hlp_killer", "menu_enemy_057");
            case "Bill Blaster": return new TattleNames("btl_hlp_killer_cannon", "menu_enemy_059");
            case "Bulky Bob-omb": return new TattleNames("btl_hlp_heavy_bom", "menu_enemy_064");
            case "Ember": return new TattleNames("btl_hlp_hermos", "menu_enemy_081");
            case "Parabuzzy": return new TattleNames("btl_hlp_patamet", "menu_enemy_050");
            case "Pokey": return new TattleNames("btl_hlp_sambo", "menu_enemy_055");
            case "Poison Pokey": return new TattleNames("btl_hlp_sambo_mummy", "menu_enemy_056");
            case "Spiky Parabuzzy": return new TattleNames("btl_hlp_patatogemet", "menu_enemy_051");
            case "Ruff Puff": return new TattleNames("btl_hlp_kurokumorn", "menu_enemy_065");
            case "Hooktail": return new TattleNames("btl_hlp_gonbaba", "menu_enemy_117");
            case "Dull Bones": return new TattleNames("btl_hlp_honenoko", "menu_enemy_007");
            case "Red Bones": return new TattleNames("btl_hlp_red_honenoko", "menu_enemy_012");
            case "Koopa Troopa": return new TattleNames("btl_hlp_nokonoko", "menu_enemy_004");
            case "Paragoomba": return new TattleNames("btl_hlp_patakuri", "menu_enemy_003");
            case "Paratroopa": return new TattleNames("btl_hlp_patapata", "menu_enemy_005");
            case "Spiky Goomba": return new TattleNames("btl_hlp_togekuri", "menu_enemy_002");
            case "Gus": return new TattleNames("btl_hlp_monban", "menu_enemy_103");
            case "Lord Crump": 
                if(openedFile.getName().equals("muj.rel"))
                    {return new TattleNames("btl_hlp_kanbu3", "menu_enemy_144");} 
                else
                    {return new TattleNames("");}
            case "Hyper Goomba": return new TattleNames("btl_hlp_hyper_kuriboo", "menu_enemy_023");
            case "Hyper Paragoomba": return new TattleNames("btl_hlp_hyper_patakuri", "menu_enemy_025");
            case "Hyper Spiky Goomba": return new TattleNames("btl_hlp_hyper_togekuri", "menu_enemy_024");
            case "Hyper Cleft": return new TattleNames("btl_hlp_hyper_sinemon", "menu_enemy_037");
            case "Crazee Dayzee": return new TattleNames("btl_hlp_pansy", "menu_enemy_082");
            case "Amazy Dayzee": return new TattleNames("btl_hlp_twinkling_pansy", "menu_enemy_083");
            case "Fuzzy": return new TattleNames("btl_hlp_chorobon", "menu_enemy_006");
            case "Fuzzy Horde": return new TattleNames("btl_hlp_chorobon_gundan", "menu_enemy_011");
            case "Gold Fuzzy": return new TattleNames("btl_hlp_gold_chorobon", "menu_enemy_010");
            case "Moon Cleft": return new TattleNames("btl_hlp_sinemon", "menu_enemy_034");
            case "Bald Cleft": return new TattleNames("btl_hlp_sinnosuke", "menu_enemy_008");
            case "Bristle": return new TattleNames("btl_hlp_togedaruma", "menu_enemy_009");
            case "Cleft": return new TattleNames("btl_hlp_monochrome_sinemon", "menu_enemy_035");
            case "Atomic Boo": return new TattleNames("btl_hlp_atomic_teresa", "menu_enemy_054");
            case "Doopliss":
                if(openedFile.getName().equals("jin.rel"))
                    {return new TattleNames("btl_hlp_hatena", "menu_enemy_124");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_ranperu_las", "menu_enemy_125");} 
                else 
                    {return new TattleNames("");}
            case "Buzzy Beetle": return new TattleNames("btl_hlp_met", "menu_enemy_048");
            case "Spiky Buzzy": return new TattleNames("btl_hlp_togemet", "menu_enemy_049");
            case "Swooper": return new TattleNames("btl_hlp_basabasa", "menu_enemy_040");
            case "Boo": return new TattleNames("btl_hlp_teresa", "menu_enemy_052");
            case "Bonetail": return new TattleNames("btl_hlp_zonbaba", "menu_enemy_195");
            case "Spania": return new TattleNames("btl_hlp_hannya", "menu_enemy_021");
            case "Spinia": return new TattleNames("btl_hlp_hinnya", "menu_enemy_020");
            case "Gloomba": return new TattleNames("btl_hlp_yami_kuriboo", "menu_enemy_026");
            case "Paragloomba": return new TattleNames("btl_hlp_yami_patakuri", "menu_enemy_028");
            case "Dark Puff": return new TattleNames("btl_hlp_monochrome_kurokumorn", "menu_enemy_172");
            case "Spiky Gloomba": return new TattleNames("btl_hlp_yami_togekuri", "menu_enemy_027");
            case "Bandit": return new TattleNames("btl_hlp_borodo", "menu_enemy_077");
            case "Lakitu": return new TattleNames("btl_hlp_jyugem", "menu_enemy_084");
            case "Bob-omb": return new TattleNames("btl_hlp_bomhei", "menu_enemy_061");
            case "Spiny": return new TattleNames("btl_hlp_togezo", "menu_enemy_085");
            case "Dark Koopa": return new TattleNames("btl_hlp_yami_noko", "menu_enemy_029");
            case "Shady Koopa": return new TattleNames("btl_hlp_ura_noko", "menu_enemy_031");
            case "Flower Fuzzy": return new TattleNames("btl_hlp_flower_chorobon", "menu_enemy_043");
            case "Dark Paratroopa": return new TattleNames("btl_hlp_yami_pata", "menu_enemy_030");
            case "Dark Bristle": return new TattleNames("btl_hlp_yamitogedaruma", "menu_enemy_174");
            case "Chain-Chomp": return new TattleNames("btl_hlp_wanwan", "menu_enemy_079");
            case "Dark Koopatrol": return new TattleNames("btl_hlp_togenoko_ace", "menu_enemy_175");
            case "Dark Wizzerd": return new TattleNames("btl_hlp_super_mahorn", "menu_enemy_071", "btl_hlp_super_mahorn_bunsin");
            case "Dark Boo": return new TattleNames("btl_hlp_purple_teresa", "menu_enemy_053");
            case "Phantom Ember": return new TattleNames("btl_hlp_phantom", "menu_enemy_178");
            case "Piranha Plant": return new TattleNames("btl_hlp_pakkun_flower", "menu_enemy_014");
            case "Elite Wizzerd": return new TattleNames("btl_hlp_mahorn_custom", "menu_enemy_183", "btl_hlp_mahorn_custom_bunshin");
            case "Wizzerd": return new TattleNames("btl_hlp_mahorn", "menu_enemy_070");
            case "Dry Bones": return new TattleNames("btl_hlp_karon", "menu_enemy_044");
            case "Dark Lakitu": return new TattleNames("btl_hlp_hyper_jyugem", "menu_enemy_186");
            case "Spunia": return new TattleNames("btl_hlp_hennya", "menu_enemy_022");
            case "Bob-ulk": return new TattleNames("btl_hlp_giant_bomb", "menu_enemy_182");
            case "Poison Puff": return new TattleNames("btl_hlp_dokugassun", "menu_enemy_013");
            case "Dark Craw": return new TattleNames("btl_hlp_dark_keeper", "menu_enemy_184");
            case "Red Chomp": return new TattleNames("btl_hlp_burst_wanwan", "menu_enemy_185");
            case "Swoopula": return new TattleNames("btl_hlp_basabasa_chururu", "menu_enemy_041");
            case "Swampire": return new TattleNames("btl_hlp_basabasa_green", "menu_enemy_177");
            case "Badge Bandit": return new TattleNames("btl_hlp_badge_borodo", "menu_enemy_078");
            case "Pider": return new TattleNames("btl_hlp_piders", "menu_enemy_015");
            case "Sky Blue Spiny": return new TattleNames("btl_hlp_hyper_togezo", "menu_enemy_173");
            case "Arantula": return new TattleNames("btl_hlp_churantalar", "menu_enemy_176");
            case "Bowser":
                if(openedFile.getName().equals("tou2.rel"))
                    {return new TattleNames("btl_hlp_koopa_tou", "menu_enemy_187");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_koopa", "menu_enemy_149");} 
                else 
                    {return new TattleNames("");}
            case "Kammy Koopa": return new TattleNames("btl_hlp_kamec_obaba", "menu_enemy_150");
            case "Shadow Peach": return new TattleNames("btl_hlp_shadowqueen1", "menu_enemy_153");
            case "Shadow Queen": return new TattleNames("btl_hlp_shadowqueen2", "menu_enemy_154");
            case "Dark Bones": return new TattleNames("btl_hlp_black_karon", "menu_enemy_045");
            case "Grodus": return new TattleNames("btl_hlp_batsugalf", "menu_enemy_151");
            case "Grodus X": return new TattleNames("btl_hlp_batsu_satellite", "menu_enemy_152");
            case "Gloomtail": return new TattleNames("btl_hlp_bunbaba", "menu_enemy_196");
            case "Bombshell Bill": return new TattleNames("btl_hlp_super_killer", "menu_enemy_058");
            case "Bombshell Bill Blaster": return new TattleNames("btl_hlp_super_killer_cannon", "menu_enemy_060");
            case "Marilyn":
                if(openedFile.getName().equals("win.rel"))
                    {return new TattleNames("btl_hlp_maririn", "menu_enemy_108");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_maririn2", "menu_enemy_111");} 
                else 
                    {return new TattleNames("");}
            case "Beldam":
                if(openedFile.getName().equals("win.rel"))
                    {return new TattleNames("btl_hlp_majyorin", "menu_enemy_104");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_majyorin2", "menu_enemy_107");} 
                else 
                    {return new TattleNames("");}
            case "Magnus Von Grapple": return new TattleNames("btl_hlp_magnum_battender", "menu_enemy_139");
            case "X-Fist": return new TattleNames("btl_hlp_rocket_punch");
            case "Pale Piranha": return new TattleNames("btl_hlp_monochrome_pakkun", "menu_enemy_181");
            case "Cortez": return new TattleNames("btl_hlp_cortez", "menu_enemy_132");
            case "X-Naut Platoon Formation 1": return new TattleNames("btl_hlp_gundan_zako_group1", "menu_enemy_146");
            case "Green Fuzzy": return new TattleNames("btl_hlp_green_chorobon", "menu_enemy_042");
            case "Putrid Piranha": return new TattleNames("btl_hlp_poison_pakkun", "menu_enemy_046");
            case "Smorg": return new TattleNames("btl_hlp_moamoa", "menu_enemy_118");
            case "Blooper": return new TattleNames("btl_hlp_gesso", "menu_enemy_114");
            case "Hammer Bro": return new TattleNames("btl_hlp_hammer_bros", "menu_enemy_067");
            case "Magikoopa": return new TattleNames("btl_hlp_kamec", "menu_enemy_086", "btl_hlp_kamec_bunshin");
            case "Koopatrol": return new TattleNames("btl_hlp_togenoko", "menu_enemy_033");
            case "Macho Grubba": return new TattleNames("btl_hlp_macho_gansu", "menu_enemy_141");
            case "Rawk Hawk": return new TattleNames("btl_hlp_champion", "menu_enemy_142");
            case "Boomerang Bro": return new TattleNames("btl_hlp_boomerang_bros", "menu_enemy_068");
            case "Fire Bro": return new TattleNames("btl_hlp_fire_bros", "menu_enemy_069");
            case "Iron Cleft 1": return new TattleNames("btl_hlp_iron_sinemon", "menu_enemy_038");
            case "Iron Cleft 2": return new TattleNames("btl_hlp_iron_sinemon2", "menu_enemy_039");
            case "Shady Paratroopa": return new TattleNames("btl_hlp_ura_pata", "menu_enemy_032");
            case "Hyper Bald Cleft": return new TattleNames("btl_hlp_hyper_sinnosuke", "menu_enemy_036");
            case "Green Magikoopa": return new TattleNames("btl_hlp_kamec_green", "menu_enemy_171", "btl_hlp_kamec_green_bunshin");
            case "Red Magikoopa": return new TattleNames("btl_hlp_kamec_red", "menu_enemy_169", "btl_hlp_kamec_red_bunshin");
            case "White Magikoopa": return new TattleNames("btl_hlp_kamec_white", "menu_enemy_170", "btl_hlp_kamec_white_bunshin");
            case "Big Bandit": return new TattleNames("btl_hlp_borodo_king", "menu_enemy_190");
            case "KP Koopa": return new TattleNames("btl_hlp_nokonoko_fighter", "menu_enemy_188");
            case "KP Paratroopa": return new TattleNames("btl_hlp_patapata_fighter", "menu_enemy_189");
            case "Red Spiky Buzzy": return new TattleNames("btl_hlp_crimson_togemet", "menu_enemy_191");
            case "Vivian": return new TattleNames("btl_hlp_vivian", "menu_enemy_112");
            
            default: return new TattleNames("");
        }
    }

    public static byte[] buildNewTattleFile(File givenGlobal, File givenFile, ArrayList<UnitData> units)
    {
        File jsonFile = new File("src\\TattleFormat.json");
        File excelFile = new File("src\\Tattle Data.xlsx");

        byte[] globalData = ByteUtils.readData(givenGlobal);
        String globalDataString = new String(globalData, StandardCharsets.ISO_8859_1);
        ArrayList<String> globalDataStringList = new ArrayList<String>();
        String[] splitGlobalDataString = globalDataString.split("\0");
        for(int i = 0; i < splitGlobalDataString.length; i++)
        {
            globalDataStringList.add(splitGlobalDataString[i]);
            //System.out.println(globalDataStringList.get(i));
        }

        try
        {
            //Get Format
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            String tattleDialogue = (String)root.get("Tattle Dialogue");
            String menuDialogue = (String)root.get("Tattle Menu");
            JSONArray uniqueTattles = (JSONArray)root.get("Unique Tattle Formats");
            ArrayList<String> additionalFormatsIDs = new ArrayList<String>();
            ArrayList<String> additionalFormats = new ArrayList<String>();
            for(int i = 0; i < uniqueTattles.size(); i++)
            {
                additionalFormatsIDs.add((String)((JSONObject)uniqueTattles.get(i)).get("ID"));
                additionalFormats.add((String)((JSONObject)uniqueTattles.get(i)).get("Text"));
            }

            //Get Excel Sheet
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);

            for(int i = 0; i < units.size(); i++)
            {
                System.out.println(units.get(i).name);

                String retString_btl = "";
                String retString_menu = "";
                String retString_clone = "";
                TattleNames tempTattleNames = TattleNameConverter(units.get(i).name, givenFile);

                //Pull Start Message
                Iterator<Row> iterator = firstSheet.iterator();
                while (iterator.hasNext()) 
                {
                    Row nextRow = iterator.next();

                    if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.btl) && nextRow.getCell(1) != null)
                    {
                        retString_btl += nextRow.getCell(1).getStringCellValue(); 
                    }
                }

                if(tempTattleNames.cloneFlag)
                {
                    iterator = firstSheet.iterator();
                    while (iterator.hasNext()) 
                    {
                        Row nextRow = iterator.next();

                        if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.clone) && nextRow.getCell(1) != null)
                        {
                            retString_clone += nextRow.getCell(1).getStringCellValue(); 
                        }
                    }
                }
                
                //Pull Layout 
                String[] split_btl_layout = tattleDialogue.split(" ");
                String[] split_menu_layout = menuDialogue.split(" ");

                if(additionalFormatsIDs.contains(tempTattleNames.btl))
                {
                    int offset = additionalFormatsIDs.indexOf(tempTattleNames.btl);
                    String[] split_btl_custom_layout = additionalFormats.get(offset).split(" ");
                    for(int j = 0; j < split_btl_custom_layout.length; j++)
                    {
                        retString_btl += tattleCommandTranslation(split_btl_custom_layout[j], i, units);
                    }
                }
                else
                {
                    for(int j = 0; j < split_btl_layout.length; j++)
                    {
                        retString_btl += tattleCommandTranslation(split_btl_layout[j], i, units);
                    }
                }

                if(tempTattleNames.menuFlag)
                {
                    if(additionalFormatsIDs.contains(tempTattleNames.menu))
                    {
                        int offset = additionalFormatsIDs.indexOf(tempTattleNames.menu);
                        String[] split_menu_custom_layout = additionalFormats.get(offset).split(" ");
                        for(int j = 0; j < split_menu_custom_layout.length; j++)
                        {
                            retString_btl += tattleCommandTranslation(split_menu_custom_layout[j], i, units);
                        }
                    }
                    else
                    {
                        for(int j = 0; j < split_menu_layout.length; j++)
                        {
                            retString_menu += tattleCommandTranslation(split_menu_layout[j], i, units);
                        }
                    }
                }

                if(tempTattleNames.cloneFlag)
                {
                    if(additionalFormatsIDs.contains(tempTattleNames.clone))
                    {
                        int offset = additionalFormatsIDs.indexOf(tempTattleNames.clone);
                        String[] split_clone_custom_layout = additionalFormats.get(offset).split(" ");
                        for(int j = 0; j < split_clone_custom_layout.length; j++)
                        {
                            retString_btl += tattleCommandTranslation(split_clone_custom_layout[j], i, units);
                        }
                    }
                    else
                    {
                        for(int j = 0; j < split_btl_layout.length; j++)
                        {
                            retString_clone += tattleCommandTranslation(split_btl_layout[j], i, units);
                        }
                    }
                }

                //Pull End Message
                iterator = firstSheet.iterator();
                while (iterator.hasNext()) 
                {
                    Row nextRow = iterator.next();

                    if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.btl) && nextRow.getCell(2) != null)
                    {
                        retString_btl += nextRow.getCell(2).getStringCellValue(); 
                    }
                }

                if(tempTattleNames.cloneFlag)
                {
                    iterator = firstSheet.iterator();
                    while (iterator.hasNext()) 
                    {
                        Row nextRow = iterator.next();

                        if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.clone) && nextRow.getCell(2) != null)
                        {
                            retString_clone += nextRow.getCell(2).getStringCellValue(); 
                        }
                    }
                }

                //Replace old tattle string with new built one
                for(int j = 0; j < globalDataStringList.size(); j++)
                {
                    if(globalDataStringList.get(j).equals(tempTattleNames.btl))
                    {
                        globalDataStringList.set((j + 1), retString_btl);
                    }
                }
                
                for(int j = 0; j < globalDataStringList.size(); j++)
                {
                    if(globalDataStringList.get(j).equals(tempTattleNames.menu))
                    {
                        globalDataStringList.set((j + 1), retString_menu);
                    }
                }

                for(int j = 0; j < globalDataStringList.size(); j++)
                {
                    if(globalDataStringList.get(j).equals(tempTattleNames.clone))
                    {
                        globalDataStringList.set((j + 1), retString_clone);
                    }
                }
            }
            
            workbook.close();
            inputStream.close();

            String newRetString = "";
            for(int i = 0; i < globalDataStringList.size(); i++)
            {
                newRetString += globalDataStringList.get(i) + '\0';
            }

            return newRetString.getBytes(StandardCharsets.UTF_8);
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

        return null;
    }

    public static String tattleCommandTranslation(String phrase, int index, ArrayList<UnitData> units)
    {
        String[] splitPhrase = phrase.split("_");

        switch (splitPhrase[0]) 
        {
            case "[BUK": return BUK_TCT(phrase, index, units);
            case "[BUKP": return BUKP_TCT(phrase, index, units);
            case "[HU": return HU_TCT(phrase, index, units);
            case "[BUD": return BUD_TCT(phrase, index, units);
            case "[BUDA": return BUDA_TCT(phrase, index, units);
            case "[SV": return SV_TCT(phrase, index, units);
            case "[BW": return BW_TCT(phrase, index, units);
            case "[LOGIC": return LOGIC_TCT(phrase, index, units);           

            default: return phrase;
        }
    }

    public static String BUK_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BUK_HP_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).perilHP);} else{return "???";}
            case "[BUK_level_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).level);} else{return "???";}
            case "[BUK_bonusXP_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return SAtoName(units.get(index).BattleUnitKindData.get(0).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct1]": if(units.get(index).BattleUnitKindData.size() > 0){return String.valueOf(units.get(index).BattleUnitKindData.get(0).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).perilHP);} else{return "???";}
            case "[BUK_level_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).level);} else{return "???";}
            case "[BUK_bonusXP_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return SAtoName(units.get(index).BattleUnitKindData.get(1).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct2]": if(units.get(index).BattleUnitKindData.size() > 1){return String.valueOf(units.get(index).BattleUnitKindData.get(1).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).perilHP);} else{return "???";}
            case "[BUK_level_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).level);} else{return "???";}
            case "[BUK_bonusXP_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return SAtoName(units.get(index).BattleUnitKindData.get(2).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct3]": if(units.get(index).BattleUnitKindData.size() > 2){return String.valueOf(units.get(index).BattleUnitKindData.get(2).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).perilHP);} else{return "???";}
            case "[BUK_level_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).level);} else{return "???";}
            case "[BUK_bonusXP_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return SAtoName(units.get(index).BattleUnitKindData.get(3).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct4]": if(units.get(index).BattleUnitKindData.size() > 3){return String.valueOf(units.get(index).BattleUnitKindData.get(3).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).perilHP);} else{return "???";}
            case "[BUK_level_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).level);} else{return "???";}
            case "[BUK_bonusXP_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return SAtoName(units.get(index).BattleUnitKindData.get(4).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct5]": if(units.get(index).BattleUnitKindData.size() > 4){return String.valueOf(units.get(index).BattleUnitKindData.get(4).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).perilHP);} else{return "???";}
            case "[BUK_level_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).level);} else{return "???";}
            case "[BUK_bonusXP_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return SAtoName(units.get(index).BattleUnitKindData.get(5).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct6]": if(units.get(index).BattleUnitKindData.size() > 5){return String.valueOf(units.get(index).BattleUnitKindData.get(5).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).perilHP);} else{return "???";}
            case "[BUK_level_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).level);} else{return "???";}
            case "[BUK_bonusXP_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return SAtoName(units.get(index).BattleUnitKindData.get(6).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct7]": if(units.get(index).BattleUnitKindData.size() > 6){return String.valueOf(units.get(index).BattleUnitKindData.get(6).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).perilHP);} else{return "???";}
            case "[BUK_level_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).level);} else{return "???";}
            case "[BUK_bonusXP_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return SAtoName(units.get(index).BattleUnitKindData.get(7).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct8]": if(units.get(index).BattleUnitKindData.size() > 7){return String.valueOf(units.get(index).BattleUnitKindData.get(7).itemStealParameter);} else{return "???";}
            case "[BUK_HP_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).HP);} else{return "???";}
            case "[BUK_dangerHP_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).dangerHP);} else{return "???";}
            case "[BUK_perilHP_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).perilHP);} else{return "???";}
            case "[BUK_level_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).level);} else{return "???";}
            case "[BUK_bonusXP_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).bonusXP);} else{return "???";}
            case "[BUK_bonusCoin_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).bonusCoin);} else{return "???";}
            case "[BUK_bonusCoinRate_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).bonusCoinRate);} else{return "???";}
            case "[BUK_baseCoin_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).baseCoin);} else{return "???";}
            case "[BUK_runRate_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).runRate);} else{return "???";}
            case "[BUK_pbCap_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).pbCap);} else{return "???";}
            case "[BUK_swallowChance_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).swallowChance);} else{return "???";}
            case "[BUK_swallowAttribute_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return SAtoName(units.get(index).BattleUnitKindData.get(8).swallowAttribute);} else{return "???";}
            case "[BUK_ultraHammerKnockChance_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).ultraHammerKnockChance);} else{return "???";}
            case "[BUK_itemStealParameter_Struct9]": if(units.get(index).BattleUnitKindData.size() > 8){return String.valueOf(units.get(index).BattleUnitKindData.get(8).itemStealParameter);} else{return "???";}

            default: return phrase;
        }
    }

    public static String BUKP_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BUKP_MostPreferredSelectTarget_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct1]": if(units.get(index).BattleUnitKindPartData.size() > 0){return BooltoYN(units.get(index).BattleUnitKindPartData.get(0).IsImmuneToStatus);} else{return "???";}
            case "[BUKP_MostPreferredSelectTarget_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct2]": if(units.get(index).BattleUnitKindPartData.size() > 1){return BooltoYN(units.get(index).BattleUnitKindPartData.get(1).IsImmuneToStatus);} else{return "???";}
            case "[BUKP_MostPreferredSelectTarget_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct3]": if(units.get(index).BattleUnitKindPartData.size() > 2){return BooltoYN(units.get(index).BattleUnitKindPartData.get(2).IsImmuneToStatus);} else{return "???";}
            case "[BUKP_MostPreferredSelectTarget_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct4]": if(units.get(index).BattleUnitKindPartData.size() > 3){return BooltoYN(units.get(index).BattleUnitKindPartData.get(3).IsImmuneToStatus);} else{return "???";}
            case "[BUKP_MostPreferredSelectTarget_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct5]": if(units.get(index).BattleUnitKindPartData.size() > 4){return BooltoYN(units.get(index).BattleUnitKindPartData.get(4).IsImmuneToStatus);} else{return "???";}
            case "[BUKP_MostPreferredSelectTarget_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct6]": if(units.get(index).BattleUnitKindPartData.size() > 5){return BooltoYN(units.get(index).BattleUnitKindPartData.get(5).IsImmuneToStatus);} else{return "???";}
            case "[BUKP_MostPreferredSelectTarget_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct7]": if(units.get(index).BattleUnitKindPartData.size() > 6){return BooltoYN(units.get(index).BattleUnitKindPartData.get(6).IsImmuneToStatus);} else{return "???";}
            case "[BUKP_MostPreferredSelectTarget_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).MostPreferredSelectTarget);} else{return "???";}
            case "[BUKP_PreferredSelectTarget_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).PreferredSelectTarget);} else{return "???";}
            case "[BUKP_SelectTarget_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).SelectTarget);} else{return "???";}
            case "[BUKP_WeakToAttackFxR_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).WeakToAttackFxR);} else{return "???";}
            case "[BUKP_WeakToIcePower_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).WeakToIcePower);} else{return "???";}
            case "[BUKP_IsWinged_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).IsWinged);} else{return "???";}
            case "[BUKP_IsShelled_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).IsShelled);} else{return "???";}
            case "[BUKP_IsBombFlippable_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).IsBombFlippable);} else{return "???";}
            case "[BUKP_NeverTargetable_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).NeverTargetable);} else{return "???";}
            case "[BUKP_Untattleable_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).Untattleable);} else{return "???";}
            case "[BUKP_JumplikeCannotTarget_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).JumplikeCannotTarget);} else{return "???";}
            case "[BUKP_HammerlikeCannotTarget_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).HammerlikeCannotTarget);} else{return "???";}
            case "[BUKP_ShellTosslikeCannotTarget_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).ShellTosslikeCannotTarget);} else{return "???";}
            case "[BUKP_IsImmuneToDamageOrStatus_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).IsImmuneToDamageOrStatus);} else{return "???";}
            case "[BUKP_IsImmuneToOHKO_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).IsImmuneToOHKO);} else{return "???";}
            case "[BUKP_IsImmuneToStatus_Struct8]": if(units.get(index).BattleUnitKindPartData.size() > 7){return BooltoYN(units.get(index).BattleUnitKindPartData.get(7).IsImmuneToStatus);} else{return "???";}

            default: return phrase;
        }
    }

    public static String HU_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[HU_startHP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).startHP);} else{return "???";}
            case "[HU_startFP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).startFP);} else{return "???";}
            case "[HU_startBP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).startBP);} else{return "???";}
            case "[HU_upgradeHP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).upgradeHP);} else{return "???";}
            case "[HU_upgradeFP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).upgradeFP);} else{return "???";}
            case "[HU_upgradeBP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).upgradeBP);} else{return "???";}
            case "[HU_PartnerLevel1HP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).startHP);} else{return "???";}
            case "[HU_PartnerLevel2HP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).startFP);} else{return "???";}
            case "[HU_PartnerLevel3HP_Struct1]": if(units.get(index).HealthUpgradesData.size() > 0){return String.valueOf(units.get(index).HealthUpgradesData.get(0).startBP);} else{return "???";}

            default: return phrase;
        }
    }

    public static String BUD_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BUD_normal_Struct1]": if(units.get(index).BattleUnitDefenseData.size() > 0){return String.valueOf(units.get(index).BattleUnitDefenseData.get(0).normal);} else{return "???";}
            case "[BUD_fire_Struct1]": if(units.get(index).BattleUnitDefenseData.size() > 0){return String.valueOf(units.get(index).BattleUnitDefenseData.get(0).fire);} else{return "???";}
            case "[BUD_ice_Struct1]": if(units.get(index).BattleUnitDefenseData.size() > 0){return String.valueOf(units.get(index).BattleUnitDefenseData.get(0).ice);} else{return "???";}
            case "[BUD_explosion_Struct1]": if(units.get(index).BattleUnitDefenseData.size() > 0){return String.valueOf(units.get(index).BattleUnitDefenseData.get(0).explosion);} else{return "???";}
            case "[BUD_electric_Struct1]": if(units.get(index).BattleUnitDefenseData.size() > 0){return String.valueOf(units.get(index).BattleUnitDefenseData.get(0).electric);} else{return "???";}
            case "[BUD_normal_Struct2]": if(units.get(index).BattleUnitDefenseData.size() > 1){return String.valueOf(units.get(index).BattleUnitDefenseData.get(1).normal);} else{return "???";}
            case "[BUD_fire_Struct2]": if(units.get(index).BattleUnitDefenseData.size() > 1){return String.valueOf(units.get(index).BattleUnitDefenseData.get(1).fire);} else{return "???";}
            case "[BUD_ice_Struct2]": if(units.get(index).BattleUnitDefenseData.size() > 1){return String.valueOf(units.get(index).BattleUnitDefenseData.get(1).ice);} else{return "???";}
            case "[BUD_explosion_Struct2]": if(units.get(index).BattleUnitDefenseData.size() > 1){return String.valueOf(units.get(index).BattleUnitDefenseData.get(1).explosion);} else{return "???";}
            case "[BUD_electric_Struct2]": if(units.get(index).BattleUnitDefenseData.size() > 1){return String.valueOf(units.get(index).BattleUnitDefenseData.get(1).electric);} else{return "???";}
            case "[BUD_normal_Struct3]": if(units.get(index).BattleUnitDefenseData.size() > 2){return String.valueOf(units.get(index).BattleUnitDefenseData.get(2).normal);} else{return "???";}
            case "[BUD_fire_Struct3]": if(units.get(index).BattleUnitDefenseData.size() > 2){return String.valueOf(units.get(index).BattleUnitDefenseData.get(2).fire);} else{return "???";}
            case "[BUD_ice_Struct3]": if(units.get(index).BattleUnitDefenseData.size() > 2){return String.valueOf(units.get(index).BattleUnitDefenseData.get(2).ice);} else{return "???";}
            case "[BUD_explosion_Struct3]": if(units.get(index).BattleUnitDefenseData.size() > 2){return String.valueOf(units.get(index).BattleUnitDefenseData.get(2).explosion);} else{return "???";}
            case "[BUD_electric_Struct3]": if(units.get(index).BattleUnitDefenseData.size() > 2){return String.valueOf(units.get(index).BattleUnitDefenseData.get(2).electric);} else{return "???";}
            case "[BUD_normal_Struct4]": if(units.get(index).BattleUnitDefenseData.size() > 3){return String.valueOf(units.get(index).BattleUnitDefenseData.get(3).normal);} else{return "???";}
            case "[BUD_fire_Struct4]": if(units.get(index).BattleUnitDefenseData.size() > 3){return String.valueOf(units.get(index).BattleUnitDefenseData.get(3).fire);} else{return "???";}
            case "[BUD_ice_Struct4]": if(units.get(index).BattleUnitDefenseData.size() > 3){return String.valueOf(units.get(index).BattleUnitDefenseData.get(3).ice);} else{return "???";}
            case "[BUD_explosion_Struct4]": if(units.get(index).BattleUnitDefenseData.size() > 3){return String.valueOf(units.get(index).BattleUnitDefenseData.get(3).explosion);} else{return "???";}
            case "[BUD_electric_Struct4]": if(units.get(index).BattleUnitDefenseData.size() > 3){return String.valueOf(units.get(index).BattleUnitDefenseData.get(3).electric);} else{return "???";}
            case "[BUD_normal_Struct5]": if(units.get(index).BattleUnitDefenseData.size() > 4){return String.valueOf(units.get(index).BattleUnitDefenseData.get(4).normal);} else{return "???";}
            case "[BUD_fire_Struct5]": if(units.get(index).BattleUnitDefenseData.size() > 4){return String.valueOf(units.get(index).BattleUnitDefenseData.get(4).fire);} else{return "???";}
            case "[BUD_ice_Struct5]": if(units.get(index).BattleUnitDefenseData.size() > 4){return String.valueOf(units.get(index).BattleUnitDefenseData.get(4).ice);} else{return "???";}
            case "[BUD_explosion_Struct5]": if(units.get(index).BattleUnitDefenseData.size() > 4){return String.valueOf(units.get(index).BattleUnitDefenseData.get(4).explosion);} else{return "???";}
            case "[BUD_electric_Struct5]": if(units.get(index).BattleUnitDefenseData.size() > 4){return String.valueOf(units.get(index).BattleUnitDefenseData.get(4).electric);} else{return "???";}
            case "[BUD_normal_Struct6]": if(units.get(index).BattleUnitDefenseData.size() > 5){return String.valueOf(units.get(index).BattleUnitDefenseData.get(5).normal);} else{return "???";}
            case "[BUD_fire_Struct6]": if(units.get(index).BattleUnitDefenseData.size() > 5){return String.valueOf(units.get(index).BattleUnitDefenseData.get(5).fire);} else{return "???";}
            case "[BUD_ice_Struct6]": if(units.get(index).BattleUnitDefenseData.size() > 5){return String.valueOf(units.get(index).BattleUnitDefenseData.get(5).ice);} else{return "???";}
            case "[BUD_explosion_Struct6]": if(units.get(index).BattleUnitDefenseData.size() > 5){return String.valueOf(units.get(index).BattleUnitDefenseData.get(5).explosion);} else{return "???";}
            case "[BUD_electric_Struct6]": if(units.get(index).BattleUnitDefenseData.size() > 5){return String.valueOf(units.get(index).BattleUnitDefenseData.get(5).electric);} else{return "???";}
            case "[BUD_normal_Struct7]": if(units.get(index).BattleUnitDefenseData.size() > 6){return String.valueOf(units.get(index).BattleUnitDefenseData.get(6).normal);} else{return "???";}
            case "[BUD_fire_Struct7]": if(units.get(index).BattleUnitDefenseData.size() > 6){return String.valueOf(units.get(index).BattleUnitDefenseData.get(6).fire);} else{return "???";}
            case "[BUD_ice_Struct7]": if(units.get(index).BattleUnitDefenseData.size() > 6){return String.valueOf(units.get(index).BattleUnitDefenseData.get(6).ice);} else{return "???";}
            case "[BUD_explosion_Struct7]": if(units.get(index).BattleUnitDefenseData.size() > 6){return String.valueOf(units.get(index).BattleUnitDefenseData.get(6).explosion);} else{return "???";}
            case "[BUD_electric_Struct7]": if(units.get(index).BattleUnitDefenseData.size() > 6){return String.valueOf(units.get(index).BattleUnitDefenseData.get(6).electric);} else{return "???";}
            case "[BUD_normal_Struct8]": if(units.get(index).BattleUnitDefenseData.size() > 7){return String.valueOf(units.get(index).BattleUnitDefenseData.get(7).normal);} else{return "???";}
            case "[BUD_fire_Struct8]": if(units.get(index).BattleUnitDefenseData.size() > 7){return String.valueOf(units.get(index).BattleUnitDefenseData.get(7).fire);} else{return "???";}
            case "[BUD_ice_Struct8]": if(units.get(index).BattleUnitDefenseData.size() > 7){return String.valueOf(units.get(index).BattleUnitDefenseData.get(7).ice);} else{return "???";}
            case "[BUD_explosion_Struct8]": if(units.get(index).BattleUnitDefenseData.size() > 7){return String.valueOf(units.get(index).BattleUnitDefenseData.get(7).explosion);} else{return "???";}
            case "[BUD_electric_Struct8]": if(units.get(index).BattleUnitDefenseData.size() > 7){return String.valueOf(units.get(index).BattleUnitDefenseData.get(7).electric);} else{return "???";}
            case "[BUD_normal_Struct9]": if(units.get(index).BattleUnitDefenseData.size() > 8){return String.valueOf(units.get(index).BattleUnitDefenseData.get(8).normal);} else{return "???";}
            case "[BUD_fire_Struct9]": if(units.get(index).BattleUnitDefenseData.size() > 8){return String.valueOf(units.get(index).BattleUnitDefenseData.get(8).fire);} else{return "???";}
            case "[BUD_ice_Struct9]": if(units.get(index).BattleUnitDefenseData.size() > 8){return String.valueOf(units.get(index).BattleUnitDefenseData.get(8).ice);} else{return "???";}
            case "[BUD_explosion_Struct9]": if(units.get(index).BattleUnitDefenseData.size() > 8){return String.valueOf(units.get(index).BattleUnitDefenseData.get(8).explosion);} else{return "???";}
            case "[BUD_electric_Struct9]": if(units.get(index).BattleUnitDefenseData.size() > 8){return String.valueOf(units.get(index).BattleUnitDefenseData.get(8).electric);} else{return "???";}
            case "[BUD_normal_Struct10]": if(units.get(index).BattleUnitDefenseData.size() > 9){return String.valueOf(units.get(index).BattleUnitDefenseData.get(9).normal);} else{return "???";}
            case "[BUD_fire_Struct10]": if(units.get(index).BattleUnitDefenseData.size() > 9){return String.valueOf(units.get(index).BattleUnitDefenseData.get(9).fire);} else{return "???";}
            case "[BUD_ice_Struct10]": if(units.get(index).BattleUnitDefenseData.size() > 9){return String.valueOf(units.get(index).BattleUnitDefenseData.get(9).ice);} else{return "???";}
            case "[BUD_explosion_Struct10]": if(units.get(index).BattleUnitDefenseData.size() > 9){return String.valueOf(units.get(index).BattleUnitDefenseData.get(9).explosion);} else{return "???";}
            case "[BUD_electric_Struct10]": if(units.get(index).BattleUnitDefenseData.size() > 9){return String.valueOf(units.get(index).BattleUnitDefenseData.get(9).electric);} else{return "???";}

            default: return phrase;
        }
    }

    public static String BUDA_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BUDA_normal_Struct1]": if(units.get(index).BattleUnitDefenseAttrData.size() > 0){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(0).normal);} else{return "???";}
            case "[BUDA_fire_Struct1]": if(units.get(index).BattleUnitDefenseAttrData.size() > 0){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(0).fire);} else{return "???";}
            case "[BUDA_ice_Struct1]": if(units.get(index).BattleUnitDefenseAttrData.size() > 0){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(0).ice);} else{return "???";}
            case "[BUDA_explosion_Struct1]": if(units.get(index).BattleUnitDefenseAttrData.size() > 0){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(0).explosion);} else{return "???";}
            case "[BUDA_electric_Struct1]": if(units.get(index).BattleUnitDefenseAttrData.size() > 0){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(0).electric);} else{return "???";}
            case "[BUDA_normal_Struct2]": if(units.get(index).BattleUnitDefenseAttrData.size() > 1){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(1).normal);} else{return "???";}
            case "[BUDA_fire_Struct2]": if(units.get(index).BattleUnitDefenseAttrData.size() > 1){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(1).fire);} else{return "???";}
            case "[BUDA_ice_Struct2]": if(units.get(index).BattleUnitDefenseAttrData.size() > 1){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(1).ice);} else{return "???";}
            case "[BUDA_explosion_Struct2]": if(units.get(index).BattleUnitDefenseAttrData.size() > 1){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(1).explosion);} else{return "???";}
            case "[BUDA_electric_Struct2]": if(units.get(index).BattleUnitDefenseAttrData.size() > 1){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(1).electric);} else{return "???";}
            case "[BUDA_normal_Struct3]": if(units.get(index).BattleUnitDefenseAttrData.size() > 2){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(2).normal);} else{return "???";}
            case "[BUDA_fire_Struct3]": if(units.get(index).BattleUnitDefenseAttrData.size() > 2){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(2).fire);} else{return "???";}
            case "[BUDA_ice_Struct3]": if(units.get(index).BattleUnitDefenseAttrData.size() > 2){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(2).ice);} else{return "???";}
            case "[BUDA_explosion_Struct3]": if(units.get(index).BattleUnitDefenseAttrData.size() > 2){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(2).explosion);} else{return "???";}
            case "[BUDA_electric_Struct3]": if(units.get(index).BattleUnitDefenseAttrData.size() > 2){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(2).electric);} else{return "???";}
            case "[BUDA_normal_Struct4]": if(units.get(index).BattleUnitDefenseAttrData.size() > 3){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(3).normal);} else{return "???";}
            case "[BUDA_fire_Struct4]": if(units.get(index).BattleUnitDefenseAttrData.size() > 3){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(3).fire);} else{return "???";}
            case "[BUDA_ice_Struct4]": if(units.get(index).BattleUnitDefenseAttrData.size() > 3){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(3).ice);} else{return "???";}
            case "[BUDA_explosion_Struct4]": if(units.get(index).BattleUnitDefenseAttrData.size() > 3){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(3).explosion);} else{return "???";}
            case "[BUDA_electric_Struct4]": if(units.get(index).BattleUnitDefenseAttrData.size() > 3){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(3).electric);} else{return "???";}
            case "[BUDA_normal_Struct5]": if(units.get(index).BattleUnitDefenseAttrData.size() > 4){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(4).normal);} else{return "???";}
            case "[BUDA_fire_Struct5]": if(units.get(index).BattleUnitDefenseAttrData.size() > 4){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(4).fire);} else{return "???";}
            case "[BUDA_ice_Struct5]": if(units.get(index).BattleUnitDefenseAttrData.size() > 4){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(4).ice);} else{return "???";}
            case "[BUDA_explosion_Struct5]": if(units.get(index).BattleUnitDefenseAttrData.size() > 4){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(4).explosion);} else{return "???";}
            case "[BUDA_electric_Struct5]": if(units.get(index).BattleUnitDefenseAttrData.size() > 4){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(4).electric);} else{return "???";}
            case "[BUDA_normal_Struct6]": if(units.get(index).BattleUnitDefenseAttrData.size() > 5){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(5).normal);} else{return "???";}
            case "[BUDA_fire_Struct6]": if(units.get(index).BattleUnitDefenseAttrData.size() > 5){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(5).fire);} else{return "???";}
            case "[BUDA_ice_Struct6]": if(units.get(index).BattleUnitDefenseAttrData.size() > 5){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(5).ice);} else{return "???";}
            case "[BUDA_explosion_Struct6]": if(units.get(index).BattleUnitDefenseAttrData.size() > 5){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(5).explosion);} else{return "???";}
            case "[BUDA_electric_Struct6]": if(units.get(index).BattleUnitDefenseAttrData.size() > 5){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(5).electric);} else{return "???";}
            case "[BUDA_normal_Struct7]": if(units.get(index).BattleUnitDefenseAttrData.size() > 6){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(6).normal);} else{return "???";}
            case "[BUDA_fire_Struct7]": if(units.get(index).BattleUnitDefenseAttrData.size() > 6){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(6).fire);} else{return "???";}
            case "[BUDA_ice_Struct7]": if(units.get(index).BattleUnitDefenseAttrData.size() > 6){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(6).ice);} else{return "???";}
            case "[BUDA_explosion_Struct7]": if(units.get(index).BattleUnitDefenseAttrData.size() > 6){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(6).explosion);} else{return "???";}
            case "[BUDA_electric_Struct7]": if(units.get(index).BattleUnitDefenseAttrData.size() > 6){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(6).electric);} else{return "???";}
            case "[BUDA_normal_Struct8]": if(units.get(index).BattleUnitDefenseAttrData.size() > 7){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(7).normal);} else{return "???";}
            case "[BUDA_fire_Struct8]": if(units.get(index).BattleUnitDefenseAttrData.size() > 7){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(7).fire);} else{return "???";}
            case "[BUDA_ice_Struct8]": if(units.get(index).BattleUnitDefenseAttrData.size() > 7){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(7).ice);} else{return "???";}
            case "[BUDA_explosion_Struct8]": if(units.get(index).BattleUnitDefenseAttrData.size() > 7){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(7).explosion);} else{return "???";}
            case "[BUDA_electric_Struct8]": if(units.get(index).BattleUnitDefenseAttrData.size() > 7){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(7).electric);} else{return "???";}
            case "[BUDA_normal_Struct9]": if(units.get(index).BattleUnitDefenseAttrData.size() > 8){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(8).normal);} else{return "???";}
            case "[BUDA_fire_Struct9]": if(units.get(index).BattleUnitDefenseAttrData.size() > 8){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(8).fire);} else{return "???";}
            case "[BUDA_ice_Struct9]": if(units.get(index).BattleUnitDefenseAttrData.size() > 8){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(8).ice);} else{return "???";}
            case "[BUDA_explosion_Struct9]": if(units.get(index).BattleUnitDefenseAttrData.size() > 8){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(8).explosion);} else{return "???";}
            case "[BUDA_electric_Struct9]": if(units.get(index).BattleUnitDefenseAttrData.size() > 8){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(8).electric);} else{return "???";}
            case "[BUDA_normal_Struct10]": if(units.get(index).BattleUnitDefenseAttrData.size() > 9){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(9).normal);} else{return "???";}
            case "[BUDA_fire_Struct10]": if(units.get(index).BattleUnitDefenseAttrData.size() > 9){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(9).fire);} else{return "???";}
            case "[BUDA_ice_Struct10]": if(units.get(index).BattleUnitDefenseAttrData.size() > 9){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(9).ice);} else{return "???";}
            case "[BUDA_explosion_Struct10]": if(units.get(index).BattleUnitDefenseAttrData.size() > 9){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(9).explosion);} else{return "???";}
            case "[BUDA_electric_Struct10]": if(units.get(index).BattleUnitDefenseAttrData.size() > 9){return DAtoName(units.get(index).BattleUnitDefenseAttrData.get(9).electric);} else{return "???";}

            default: return phrase;
        }
    }

    public static String SV_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[SV_sleep_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).sleep);} else{return "???";}
            case "[SV_stop_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).stop);} else{return "???";}
            case "[SV_dizzy_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).dizzy);} else{return "???";}
            case "[SV_poison_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).poison);} else{return "???";}
            case "[SV_confuse_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).confuse);} else{return "???";}
            case "[SV_electric_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).electric);} else{return "???";}
            case "[SV_burn_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).burn);} else{return "???";}
            case "[SV_freeze_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).freeze);} else{return "???";}
            case "[SV_huge_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).huge);} else{return "???";}
            case "[SV_tiny_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).tiny);} else{return "???";}
            case "[SV_attack_up_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).defense_down);} else{return "???";}
            case "[SV_allergic_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).allergic);} else{return "???";}
            case "[SV_fright_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).fright);} else{return "???";}
            case "[SV_gale_force_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).gale_force);} else{return "???";}
            case "[SV_fast_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).fast);} else{return "???";}
            case "[SV_slow_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).slow);} else{return "???";}
            case "[SV_dodgy_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).dodgy);} else{return "???";}
            case "[SV_invisible_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).invisible);} else{return "???";}
            case "[SV_ohko_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return String.valueOf(units.get(index).StatusVulnerabilityData.get(0).ohko);} else{return "???";}
            case "[SV_sleep_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).sleep);} else{return "???";}
            case "[SV_stop_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).stop);} else{return "???";}
            case "[SV_dizzy_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).dizzy);} else{return "???";}
            case "[SV_poison_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).poison);} else{return "???";}
            case "[SV_confuse_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).confuse);} else{return "???";}
            case "[SV_electric_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).electric);} else{return "???";}
            case "[SV_burn_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).burn);} else{return "???";}
            case "[SV_freeze_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).freeze);} else{return "???";}
            case "[SV_huge_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).huge);} else{return "???";}
            case "[SV_tiny_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).tiny);} else{return "???";}
            case "[SV_attack_up_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).defense_down);} else{return "???";}
            case "[SV_allergic_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).allergic);} else{return "???";}
            case "[SV_fright_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).fright);} else{return "???";}
            case "[SV_gale_force_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).gale_force);} else{return "???";}
            case "[SV_fast_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).fast);} else{return "???";}
            case "[SV_slow_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).slow);} else{return "???";}
            case "[SV_dodgy_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).dodgy);} else{return "???";}
            case "[SV_invisible_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).invisible);} else{return "???";}
            case "[SV_ohko_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return String.valueOf(units.get(index).StatusVulnerabilityData.get(1).ohko);} else{return "???";}
            case "[SV_sleep_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).sleep);} else{return "???";}
            case "[SV_stop_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).stop);} else{return "???";}
            case "[SV_dizzy_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).dizzy);} else{return "???";}
            case "[SV_poison_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).poison);} else{return "???";}
            case "[SV_confuse_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).confuse);} else{return "???";}
            case "[SV_electric_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).electric);} else{return "???";}
            case "[SV_burn_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).burn);} else{return "???";}
            case "[SV_freeze_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).freeze);} else{return "???";}
            case "[SV_huge_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).huge);} else{return "???";}
            case "[SV_tiny_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).tiny);} else{return "???";}
            case "[SV_attack_up_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).defense_down);} else{return "???";}
            case "[SV_allergic_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).allergic);} else{return "???";}
            case "[SV_fright_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).fright);} else{return "???";}
            case "[SV_gale_force_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).gale_force);} else{return "???";}
            case "[SV_fast_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).fast);} else{return "???";}
            case "[SV_slow_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).slow);} else{return "???";}
            case "[SV_dodgy_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).dodgy);} else{return "???";}
            case "[SV_invisible_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).invisible);} else{return "???";}
            case "[SV_ohko_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return String.valueOf(units.get(index).StatusVulnerabilityData.get(2).ohko);} else{return "???";}
            case "[SV_sleep_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).sleep);} else{return "???";}
            case "[SV_stop_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).stop);} else{return "???";}
            case "[SV_dizzy_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).dizzy);} else{return "???";}
            case "[SV_poison_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).poison);} else{return "???";}
            case "[SV_confuse_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).confuse);} else{return "???";}
            case "[SV_electric_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).electric);} else{return "???";}
            case "[SV_burn_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).burn);} else{return "???";}
            case "[SV_freeze_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).freeze);} else{return "???";}
            case "[SV_huge_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).huge);} else{return "???";}
            case "[SV_tiny_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).tiny);} else{return "???";}
            case "[SV_attack_up_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).defense_down);} else{return "???";}
            case "[SV_allergic_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).allergic);} else{return "???";}
            case "[SV_fright_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).fright);} else{return "???";}
            case "[SV_gale_force_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).gale_force);} else{return "???";}
            case "[SV_fast_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).fast);} else{return "???";}
            case "[SV_slow_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).slow);} else{return "???";}
            case "[SV_dodgy_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).dodgy);} else{return "???";}
            case "[SV_invisible_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).invisible);} else{return "???";}
            case "[SV_ohko_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return String.valueOf(units.get(index).StatusVulnerabilityData.get(3).ohko);} else{return "???";}
            case "[SV_sleep_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).sleep);} else{return "???";}
            case "[SV_stop_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).stop);} else{return "???";}
            case "[SV_dizzy_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).dizzy);} else{return "???";}
            case "[SV_poison_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).poison);} else{return "???";}
            case "[SV_confuse_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).confuse);} else{return "???";}
            case "[SV_electric_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).electric);} else{return "???";}
            case "[SV_burn_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).burn);} else{return "???";}
            case "[SV_freeze_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).freeze);} else{return "???";}
            case "[SV_huge_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).huge);} else{return "???";}
            case "[SV_tiny_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).tiny);} else{return "???";}
            case "[SV_attack_up_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).defense_down);} else{return "???";}
            case "[SV_allergic_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).allergic);} else{return "???";}
            case "[SV_fright_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).fright);} else{return "???";}
            case "[SV_gale_force_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).gale_force);} else{return "???";}
            case "[SV_fast_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).fast);} else{return "???";}
            case "[SV_slow_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).slow);} else{return "???";}
            case "[SV_dodgy_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).dodgy);} else{return "???";}
            case "[SV_invisible_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).invisible);} else{return "???";}
            case "[SV_ohko_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return String.valueOf(units.get(index).StatusVulnerabilityData.get(4).ohko);} else{return "???";}
            case "[SV_sleep_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).sleep);} else{return "???";}
            case "[SV_stop_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).stop);} else{return "???";}
            case "[SV_dizzy_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).dizzy);} else{return "???";}
            case "[SV_poison_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).poison);} else{return "???";}
            case "[SV_confuse_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).confuse);} else{return "???";}
            case "[SV_electric_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).electric);} else{return "???";}
            case "[SV_burn_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).burn);} else{return "???";}
            case "[SV_freeze_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).freeze);} else{return "???";}
            case "[SV_huge_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).huge);} else{return "???";}
            case "[SV_tiny_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).tiny);} else{return "???";}
            case "[SV_attack_up_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).defense_down);} else{return "???";}
            case "[SV_allergic_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).allergic);} else{return "???";}
            case "[SV_fright_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).fright);} else{return "???";}
            case "[SV_gale_force_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).gale_force);} else{return "???";}
            case "[SV_fast_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).fast);} else{return "???";}
            case "[SV_slow_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).slow);} else{return "???";}
            case "[SV_dodgy_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).dodgy);} else{return "???";}
            case "[SV_invisible_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).invisible);} else{return "???";}
            case "[SV_ohko_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return String.valueOf(units.get(index).StatusVulnerabilityData.get(5).ohko);} else{return "???";}
            case "[SV_sleep_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).sleep);} else{return "???";}
            case "[SV_stop_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).stop);} else{return "???";}
            case "[SV_dizzy_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).dizzy);} else{return "???";}
            case "[SV_poison_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).poison);} else{return "???";}
            case "[SV_confuse_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).confuse);} else{return "???";}
            case "[SV_electric_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).electric);} else{return "???";}
            case "[SV_burn_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).burn);} else{return "???";}
            case "[SV_freeze_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).freeze);} else{return "???";}
            case "[SV_huge_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).huge);} else{return "???";}
            case "[SV_tiny_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).tiny);} else{return "???";}
            case "[SV_attack_up_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).defense_down);} else{return "???";}
            case "[SV_allergic_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).allergic);} else{return "???";}
            case "[SV_fright_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).fright);} else{return "???";}
            case "[SV_gale_force_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).gale_force);} else{return "???";}
            case "[SV_fast_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).fast);} else{return "???";}
            case "[SV_slow_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).slow);} else{return "???";}
            case "[SV_dodgy_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).dodgy);} else{return "???";}
            case "[SV_invisible_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).invisible);} else{return "???";}
            case "[SV_ohko_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return String.valueOf(units.get(index).StatusVulnerabilityData.get(6).ohko);} else{return "???";}
            case "[SV_sleep_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).sleep);} else{return "???";}
            case "[SV_stop_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).stop);} else{return "???";}
            case "[SV_dizzy_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).dizzy);} else{return "???";}
            case "[SV_poison_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).poison);} else{return "???";}
            case "[SV_confuse_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).confuse);} else{return "???";}
            case "[SV_electric_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).electric);} else{return "???";}
            case "[SV_burn_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).burn);} else{return "???";}
            case "[SV_freeze_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).freeze);} else{return "???";}
            case "[SV_huge_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).huge);} else{return "???";}
            case "[SV_tiny_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).tiny);} else{return "???";}
            case "[SV_attack_up_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).defense_down);} else{return "???";}
            case "[SV_allergic_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).allergic);} else{return "???";}
            case "[SV_fright_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).fright);} else{return "???";}
            case "[SV_gale_force_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).gale_force);} else{return "???";}
            case "[SV_fast_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).fast);} else{return "???";}
            case "[SV_slow_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).slow);} else{return "???";}
            case "[SV_dodgy_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).dodgy);} else{return "???";}
            case "[SV_invisible_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).invisible);} else{return "???";}
            case "[SV_ohko_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return String.valueOf(units.get(index).StatusVulnerabilityData.get(7).ohko);} else{return "???";}
            case "[SV_sleep_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).sleep);} else{return "???";}
            case "[SV_stop_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).stop);} else{return "???";}
            case "[SV_dizzy_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).dizzy);} else{return "???";}
            case "[SV_poison_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).poison);} else{return "???";}
            case "[SV_confuse_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).confuse);} else{return "???";}
            case "[SV_electric_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).electric);} else{return "???";}
            case "[SV_burn_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).burn);} else{return "???";}
            case "[SV_freeze_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).freeze);} else{return "???";}
            case "[SV_huge_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).huge);} else{return "???";}
            case "[SV_tiny_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).tiny);} else{return "???";}
            case "[SV_attack_up_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).defense_down);} else{return "???";}
            case "[SV_allergic_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).allergic);} else{return "???";}
            case "[SV_fright_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).fright);} else{return "???";}
            case "[SV_gale_force_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).gale_force);} else{return "???";}
            case "[SV_fast_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).fast);} else{return "???";}
            case "[SV_slow_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).slow);} else{return "???";}
            case "[SV_dodgy_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).dodgy);} else{return "???";}
            case "[SV_invisible_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).invisible);} else{return "???";}
            case "[SV_ohko_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return String.valueOf(units.get(index).StatusVulnerabilityData.get(8).ohko);} else{return "???";}
            case "[SV_sleep_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).sleep);} else{return "???";}
            case "[SV_stop_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).stop);} else{return "???";}
            case "[SV_dizzy_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).dizzy);} else{return "???";}
            case "[SV_poison_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).poison);} else{return "???";}
            case "[SV_confuse_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).confuse);} else{return "???";}
            case "[SV_electric_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).electric);} else{return "???";}
            case "[SV_burn_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).burn);} else{return "???";}
            case "[SV_freeze_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).freeze);} else{return "???";}
            case "[SV_huge_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).huge);} else{return "???";}
            case "[SV_tiny_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).tiny);} else{return "???";}
            case "[SV_attack_up_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).attack_up);} else{return "???";}
            case "[SV_attack_down_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).attack_down);} else{return "???";}
            case "[SV_defense_up_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).defense_up);} else{return "???";}
            case "[SV_defense_down_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).defense_down);} else{return "???";}
            case "[SV_allergic_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).allergic);} else{return "???";}
            case "[SV_fright_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).fright);} else{return "???";}
            case "[SV_gale_force_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).gale_force);} else{return "???";}
            case "[SV_fast_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).fast);} else{return "???";}
            case "[SV_slow_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).slow);} else{return "???";}
            case "[SV_dodgy_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).dodgy);} else{return "???";}
            case "[SV_invisible_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).invisible);} else{return "???";}
            case "[SV_ohko_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return String.valueOf(units.get(index).StatusVulnerabilityData.get(9).ohko);} else{return "???";}

            default: return phrase;
        }
    }

    public static String BW_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return units.get(index).BattleWeaponData.get(0).attackName;} else{return "???";}
            case "[BW_accuracy_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return SStoName(units.get(index).BattleWeaponData.get(0).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return EtoName(units.get(index).BattleWeaponData.get(0).element);} else{return "???";}
            case "[BW_damage_pattern_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return DPtoName(units.get(index).BattleWeaponData.get(0).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return ACLtoName(units.get(index).BattleWeaponData.get(0).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Fiery);} else{return "???";}
            case "[BW_Icy_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Icy);} else{return "???";}
            case "[BW_Poison_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Poison);} else{return "???";}
            case "[BW_Explosive_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return BooltoYN(units.get(index).BattleWeaponData.get(0).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return String.valueOf(units.get(index).BattleWeaponData.get(0).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT2(phrase, index, units);
        }
    }

    public static String BW_TCT2(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return units.get(index).BattleWeaponData.get(1).attackName;} else{return "???";}
            case "[BW_accuracy_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return SStoName(units.get(index).BattleWeaponData.get(1).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return EtoName(units.get(index).BattleWeaponData.get(1).element);} else{return "???";}
            case "[BW_damage_pattern_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return DPtoName(units.get(index).BattleWeaponData.get(1).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return ACLtoName(units.get(index).BattleWeaponData.get(1).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Fiery);} else{return "???";}
            case "[BW_Icy_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Icy);} else{return "???";}
            case "[BW_Poison_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Poison);} else{return "???";}
            case "[BW_Explosive_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return BooltoYN(units.get(index).BattleWeaponData.get(1).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return String.valueOf(units.get(index).BattleWeaponData.get(1).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT2(phrase, index, units);
        }
    }

    public static String BW_TCT3(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return units.get(index).BattleWeaponData.get(2).attackName;} else{return "???";}
            case "[BW_accuracy_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return SStoName(units.get(index).BattleWeaponData.get(2).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return EtoName(units.get(index).BattleWeaponData.get(2).element);} else{return "???";}
            case "[BW_damage_pattern_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return DPtoName(units.get(index).BattleWeaponData.get(2).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return ACLtoName(units.get(index).BattleWeaponData.get(2).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Fiery);} else{return "???";}
            case "[BW_Icy_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Icy);} else{return "???";}
            case "[BW_Poison_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Poison);} else{return "???";}
            case "[BW_Explosive_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return BooltoYN(units.get(index).BattleWeaponData.get(2).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return String.valueOf(units.get(index).BattleWeaponData.get(2).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT4(phrase, index, units);
        }
    }

    public static String BW_TCT4(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return units.get(index).BattleWeaponData.get(3).attackName;} else{return "???";}
            case "[BW_accuracy_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return SStoName(units.get(index).BattleWeaponData.get(3).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return EtoName(units.get(index).BattleWeaponData.get(3).element);} else{return "???";}
            case "[BW_damage_pattern_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return DPtoName(units.get(index).BattleWeaponData.get(3).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return ACLtoName(units.get(index).BattleWeaponData.get(3).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Fiery);} else{return "???";}
            case "[BW_Icy_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Icy);} else{return "???";}
            case "[BW_Poison_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Poison);} else{return "???";}
            case "[BW_Explosive_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return BooltoYN(units.get(index).BattleWeaponData.get(3).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return String.valueOf(units.get(index).BattleWeaponData.get(3).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT5(phrase, index, units);
        }
    }

    public static String BW_TCT5(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return units.get(index).BattleWeaponData.get(4).attackName;} else{return "???";}
            case "[BW_accuracy_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return SStoName(units.get(index).BattleWeaponData.get(4).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return EtoName(units.get(index).BattleWeaponData.get(4).element);} else{return "???";}
            case "[BW_damage_pattern_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return DPtoName(units.get(index).BattleWeaponData.get(4).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return ACLtoName(units.get(index).BattleWeaponData.get(4).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Fiery);} else{return "???";}
            case "[BW_Icy_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Icy);} else{return "???";}
            case "[BW_Poison_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Poison);} else{return "???";}
            case "[BW_Explosive_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return BooltoYN(units.get(index).BattleWeaponData.get(4).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return String.valueOf(units.get(index).BattleWeaponData.get(4).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT6(phrase, index, units);
        }
    }

    public static String BW_TCT6(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return units.get(index).BattleWeaponData.get(5).attackName;} else{return "???";}
            case "[BW_accuracy_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return SStoName(units.get(index).BattleWeaponData.get(5).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return EtoName(units.get(index).BattleWeaponData.get(5).element);} else{return "???";}
            case "[BW_damage_pattern_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return DPtoName(units.get(index).BattleWeaponData.get(5).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return ACLtoName(units.get(index).BattleWeaponData.get(5).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Fiery);} else{return "???";}
            case "[BW_Icy_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Icy);} else{return "???";}
            case "[BW_Poison_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Poison);} else{return "???";}
            case "[BW_Explosive_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return BooltoYN(units.get(index).BattleWeaponData.get(5).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return String.valueOf(units.get(index).BattleWeaponData.get(5).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT7(phrase, index, units);
        }
    }

    public static String BW_TCT7(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return units.get(index).BattleWeaponData.get(6).attackName;} else{return "???";}
            case "[BW_accuracy_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return SStoName(units.get(index).BattleWeaponData.get(6).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return EtoName(units.get(index).BattleWeaponData.get(6).element);} else{return "???";}
            case "[BW_damage_pattern_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return DPtoName(units.get(index).BattleWeaponData.get(6).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return ACLtoName(units.get(index).BattleWeaponData.get(6).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Fiery);} else{return "???";}
            case "[BW_Icy_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Icy);} else{return "???";}
            case "[BW_Poison_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Poison);} else{return "???";}
            case "[BW_Explosive_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return BooltoYN(units.get(index).BattleWeaponData.get(6).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return String.valueOf(units.get(index).BattleWeaponData.get(6).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT8(phrase, index, units);
        }
    }

    public static String BW_TCT8(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return units.get(index).BattleWeaponData.get(7).attackName;} else{return "???";}
            case "[BW_accuracy_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return SStoName(units.get(index).BattleWeaponData.get(7).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return EtoName(units.get(index).BattleWeaponData.get(7).element);} else{return "???";}
            case "[BW_damage_pattern_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return DPtoName(units.get(index).BattleWeaponData.get(7).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return ACLtoName(units.get(index).BattleWeaponData.get(7).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Fiery);} else{return "???";}
            case "[BW_Icy_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Icy);} else{return "???";}
            case "[BW_Poison_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Poison);} else{return "???";}
            case "[BW_Explosive_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return BooltoYN(units.get(index).BattleWeaponData.get(7).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return String.valueOf(units.get(index).BattleWeaponData.get(7).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT9(phrase, index, units);
        }
    }

    public static String BW_TCT9(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return units.get(index).BattleWeaponData.get(8).attackName;} else{return "???";}
            case "[BW_accuracy_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return SStoName(units.get(index).BattleWeaponData.get(8).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return EtoName(units.get(index).BattleWeaponData.get(8).element);} else{return "???";}
            case "[BW_damage_pattern_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return DPtoName(units.get(index).BattleWeaponData.get(8).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return ACLtoName(units.get(index).BattleWeaponData.get(8).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Fiery);} else{return "???";}
            case "[BW_Icy_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Icy);} else{return "???";}
            case "[BW_Poison_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Poison);} else{return "???";}
            case "[BW_Explosive_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return BooltoYN(units.get(index).BattleWeaponData.get(8).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return String.valueOf(units.get(index).BattleWeaponData.get(8).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT10(phrase, index, units);
        }
    }

    public static String BW_TCT10(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return units.get(index).BattleWeaponData.get(9).attackName;} else{return "???";}
            case "[BW_accuracy_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return SStoName(units.get(index).BattleWeaponData.get(9).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return EtoName(units.get(index).BattleWeaponData.get(9).element);} else{return "???";}
            case "[BW_damage_pattern_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return DPtoName(units.get(index).BattleWeaponData.get(9).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return ACLtoName(units.get(index).BattleWeaponData.get(9).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Fiery);} else{return "???";}
            case "[BW_Icy_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Icy);} else{return "???";}
            case "[BW_Poison_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Poison);} else{return "???";}
            case "[BW_Explosive_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return BooltoYN(units.get(index).BattleWeaponData.get(9).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return String.valueOf(units.get(index).BattleWeaponData.get(9).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT11(phrase, index, units);
        }
    }

    public static String BW_TCT11(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return units.get(index).BattleWeaponData.get(10).attackName;} else{return "???";}
            case "[BW_accuracy_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return SStoName(units.get(index).BattleWeaponData.get(10).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return EtoName(units.get(index).BattleWeaponData.get(10).element);} else{return "???";}
            case "[BW_damage_pattern_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return DPtoName(units.get(index).BattleWeaponData.get(10).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return ACLtoName(units.get(index).BattleWeaponData.get(10).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Fiery);} else{return "???";}
            case "[BW_Icy_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Icy);} else{return "???";}
            case "[BW_Poison_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Poison);} else{return "???";}
            case "[BW_Explosive_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return BooltoYN(units.get(index).BattleWeaponData.get(10).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return String.valueOf(units.get(index).BattleWeaponData.get(10).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT12(phrase, index, units);
        }
    }

    public static String BW_TCT12(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return units.get(index).BattleWeaponData.get(11).attackName;} else{return "???";}
            case "[BW_accuracy_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return SStoName(units.get(index).BattleWeaponData.get(11).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return EtoName(units.get(index).BattleWeaponData.get(11).element);} else{return "???";}
            case "[BW_damage_pattern_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return DPtoName(units.get(index).BattleWeaponData.get(11).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return ACLtoName(units.get(index).BattleWeaponData.get(11).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Fiery);} else{return "???";}
            case "[BW_Icy_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Icy);} else{return "???";}
            case "[BW_Poison_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Poison);} else{return "???";}
            case "[BW_Explosive_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return BooltoYN(units.get(index).BattleWeaponData.get(11).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return String.valueOf(units.get(index).BattleWeaponData.get(11).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT13(phrase, index, units);
        }
    }

    public static String BW_TCT13(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return units.get(index).BattleWeaponData.get(12).attackName;} else{return "???";}
            case "[BW_accuracy_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return SStoName(units.get(index).BattleWeaponData.get(12).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return EtoName(units.get(index).BattleWeaponData.get(12).element);} else{return "???";}
            case "[BW_damage_pattern_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return DPtoName(units.get(index).BattleWeaponData.get(12).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return ACLtoName(units.get(index).BattleWeaponData.get(12).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Fiery);} else{return "???";}
            case "[BW_Icy_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Icy);} else{return "???";}
            case "[BW_Poison_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Poison);} else{return "???";}
            case "[BW_Explosive_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return BooltoYN(units.get(index).BattleWeaponData.get(12).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return String.valueOf(units.get(index).BattleWeaponData.get(12).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT14(phrase, index, units);
        }
    }

    public static String BW_TCT14(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return units.get(index).BattleWeaponData.get(13).attackName;} else{return "???";}
            case "[BW_accuracy_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return SStoName(units.get(index).BattleWeaponData.get(13).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return EtoName(units.get(index).BattleWeaponData.get(13).element);} else{return "???";}
            case "[BW_damage_pattern_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return DPtoName(units.get(index).BattleWeaponData.get(13).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return ACLtoName(units.get(index).BattleWeaponData.get(13).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Fiery);} else{return "???";}
            case "[BW_Icy_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Icy);} else{return "???";}
            case "[BW_Poison_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Poison);} else{return "???";}
            case "[BW_Explosive_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return BooltoYN(units.get(index).BattleWeaponData.get(13).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return String.valueOf(units.get(index).BattleWeaponData.get(13).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT15(phrase, index, units);
        }
    }

    public static String BW_TCT15(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return units.get(index).BattleWeaponData.get(14).attackName;} else{return "???";}
            case "[BW_accuracy_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return SStoName(units.get(index).BattleWeaponData.get(14).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return EtoName(units.get(index).BattleWeaponData.get(14).element);} else{return "???";}
            case "[BW_damage_pattern_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return DPtoName(units.get(index).BattleWeaponData.get(14).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return ACLtoName(units.get(index).BattleWeaponData.get(14).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Fiery);} else{return "???";}
            case "[BW_Icy_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Icy);} else{return "???";}
            case "[BW_Poison_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Poison);} else{return "???";}
            case "[BW_Explosive_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return BooltoYN(units.get(index).BattleWeaponData.get(14).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return String.valueOf(units.get(index).BattleWeaponData.get(14).stage_object_fall_chance);} else{return "???";}

            default: return BW_TCT16(phrase, index, units);
        }
    }

    public static String BW_TCT16(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[BW_attackName_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return units.get(index).BattleWeaponData.get(15).attackName;} else{return "???";}
            case "[BW_accuracy_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).accuracy);} else{return "???";}
            case "[BW_fp_cost_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).fp_cost);} else{return "???";}
            case "[BW_sp_cost_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).sp_cost);} else{return "???";}
            case "[BW_superguard_state_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return SStoName(units.get(index).BattleWeaponData.get(15).superguard_state);} else{return "???";}
            case "[BW_sylish_multiplier_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).sylish_multiplier);} else{return "???";}
            case "[BW_bingo_slot_inc_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).bingo_slot_inc_chance);} else{return "???";}
            case "[BW_base_damage1_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage1);} else{return "???";}
            case "[BW_base_damage2_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage2);} else{return "???";}
            case "[BW_base_damage3_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage3);} else{return "???";}
            case "[BW_base_damage4_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage4);} else{return "???";}
            case "[BW_base_damage5_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage5);} else{return "???";}
            case "[BW_base_damage6_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage6);} else{return "???";}
            case "[BW_base_damage7_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage7);} else{return "???";}
            case "[BW_base_damage8_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_damage8);} else{return "???";}
            case "[BW_base_fpdamage1_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage1);} else{return "???";}
            case "[BW_base_fpdamage2_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage2);} else{return "???";}
            case "[BW_base_fpdamage3_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage3);} else{return "???";}
            case "[BW_base_fpdamage4_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage4);} else{return "???";}
            case "[BW_base_fpdamage5_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage5);} else{return "???";}
            case "[BW_base_fpdamage6_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage6);} else{return "???";}
            case "[BW_base_fpdamage7_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage7);} else{return "???";}
            case "[BW_base_fpdamage8_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).base_fpdamage8);} else{return "???";}
            case "[BW_CannotTargetMarioOrShellShield_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetMarioOrShellShield);} else{return "???";}
            case "[BW_CannotTargetPartner_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetPartner);} else{return "???";}
            case "[BW_CannotTargetEnemy_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetEnemy);} else{return "???";}
            case "[BW_CannotTargetOppositeAlliance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetOppositeAlliance);} else{return "???";}
            case "[BW_CannotTargetOwnAlliance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetOwnAlliance);} else{return "???";}
            case "[BW_CannotTargetSelf_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetSelf);} else{return "???";}
            case "[BW_CannotTargetSameSpecies_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetSameSpecies);} else{return "???";}
            case "[BW_OnlyTargetSelf_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).OnlyTargetSelf);} else{return "???";}
            case "[BW_OnlyTargetMario_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).OnlyTargetMario);} else{return "???";}
            case "[BW_SingleTarget_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).SingleTarget);} else{return "???";}
            case "[BW_MultipleTarget_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).MultipleTarget);} else{return "???";}
            case "[BW_Tattlelike_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Tattlelike);} else{return "???";}
            case "[BW_CannotTargetCeiling_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetCeiling);} else{return "???";}
            case "[BW_CannotTargetFloating_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetFloating);} else{return "???";}
            case "[BW_CannotTargetGrounded_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotTargetGrounded);} else{return "???";}
            case "[BW_Jumplike_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Jumplike);} else{return "???";}
            case "[BW_Hammerlike_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Hammerlike);} else{return "???";}
            case "[BW_ShellTosslike_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).ShellTosslike);} else{return "???";}
            case "[BW_RecoilDamage_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).RecoilDamage);} else{return "???";}
            case "[BW_CanOnlyTargetFrontmost_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CanOnlyTargetFrontmost);} else{return "???";}
            case "[BW_TargetSameAllianceDirection_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).TargetSameAllianceDirection);} else{return "???";}
            case "[BW_TargetOppositeAllianceDirection_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).TargetOppositeAllianceDirection);} else{return "???";}
            case "[BW_element_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return EtoName(units.get(index).BattleWeaponData.get(15).element);} else{return "???";}
            case "[BW_damage_pattern_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return DPtoName(units.get(index).BattleWeaponData.get(15).damage_pattern);} else{return "???";}
            case "[BW_ac_level_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return ACLtoName(units.get(index).BattleWeaponData.get(15).ac_level);} else{return "???";}
            case "[BW_BadgeCanAffectPower_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).BadgeCanAffectPower);} else{return "???";}
            case "[BW_StatusCanAffectPower_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).StatusCanAffectPower);} else{return "???";}
            case "[BW_IsChargeable_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).IsChargeable);} else{return "???";}
            case "[BW_CannotMiss_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CannotMiss);} else{return "???";}
            case "[BW_DiminishingReturnsByHit_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).DiminishingReturnsByHit);} else{return "???";}
            case "[BW_DiminishingReturnsByTarget_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).DiminishingReturnsByTarget);} else{return "???";}
            case "[BW_PiercesDefense_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PiercesDefense);} else{return "???";}
            case "[BW_IgnoreTargetStatusVulnerability_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).IgnoreTargetStatusVulnerability);} else{return "???";}
            case "[BW_IgnitesIfBurned_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).IgnitesIfBurned);} else{return "???";}
            case "[BW_FlipsShellEnemies_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).FlipsShellEnemies);} else{return "???";}
            case "[BW_FlipsBombFlippableEnemies_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).FlipsBombFlippableEnemies);} else{return "???";}
            case "[BW_GroundsWingedEnemies_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).GroundsWingedEnemies);} else{return "???";}
            case "[BW_CanBeUsedAsConfusedAction_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).CanBeUsedAsConfusedAction);} else{return "???";}
            case "[BW_Unguardable_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Unguardable);} else{return "???";}
            case "[BW_Electric_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Electric);} else{return "???";}
            case "[BW_TopSpiky_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).TopSpiky);} else{return "???";}
            case "[BW_PreemptiveFrontSpiky_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreemptiveFrontSpiky);} else{return "???";}
            case "[BW_FrontSpiky_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).FrontSpiky);} else{return "???";}
            case "[BW_Fiery_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Fiery);} else{return "???";}
            case "[BW_Icy_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Icy);} else{return "???";}
            case "[BW_Poison_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Poison);} else{return "???";}
            case "[BW_Explosive_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Explosive);} else{return "???";}
            case "[BW_VolatileExplosive_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).VolatileExplosive);} else{return "???";}
            case "[BW_Payback_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).Payback);} else{return "???";}
            case "[BW_HoldFast_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).HoldFast);} else{return "???";}
            case "[BW_PreferMario_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferMario);} else{return "???";}
            case "[BW_PreferPartner_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferPartner);} else{return "???";}
            case "[BW_PreferFront_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferFront);} else{return "???";}
            case "[BW_PreferBack_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferBack);} else{return "???";}
            case "[BW_PreferSameAlliance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferSameAlliance);} else{return "???";}
            case "[BW_PreferOppositeAlliance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferOppositeAlliance);} else{return "???";}
            case "[BW_PreferLessHealthy_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferLessHealthy);} else{return "???";}
            case "[BW_GreatlyPreferLessHealthy_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).GreatlyPreferLessHealthy);} else{return "???";}
            case "[BW_PreferLowerHP_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferLowerHP);} else{return "???";}
            case "[BW_PreferHigherHP_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferHigherHP);} else{return "???";}
            case "[BW_PreferInPeril_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).PreferInPeril);} else{return "???";}
            case "[BW_ChooseWeightedRandomly_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return BooltoYN(units.get(index).BattleWeaponData.get(15).ChooseWeightedRandomly);} else{return "???";}
            case "[BW_sleep_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).sleep_chance);} else{return "???";}
            case "[BW_sleep_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).sleep_time);} else{return "???";}
            case "[BW_stop_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stop_chance);} else{return "???";}
            case "[BW_stop_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stop_time);} else{return "???";}
            case "[BW_dizzy_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).dizzy_chance);} else{return "???";}
            case "[BW_dizzy_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).dizzy_time);} else{return "???";}
            case "[BW_poison_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).poison_chance);} else{return "???";}
            case "[BW_poison_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).poison_time);} else{return "???";}
            case "[BW_poison_strength_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).poison_strength);} else{return "???";}
            case "[BW_confuse_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).confuse_chance);} else{return "???";}
            case "[BW_confuse_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).confuse_time);} else{return "???";}
            case "[BW_electric_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).electric_chance);} else{return "???";}
            case "[BW_electric_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).electric_time);} else{return "???";}
            case "[BW_dodgy_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).dodgy_chance);} else{return "???";}
            case "[BW_dodgy_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).dodgy_time);} else{return "???";}
            case "[BW_burn_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).burn_chance);} else{return "???";}
            case "[BW_burn_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).burn_time);} else{return "???";}
            case "[BW_freeze_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).freeze_chance);} else{return "???";}
            case "[BW_freeze_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).freeze_time);} else{return "???";}
            case "[BW_size_change_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).size_change_chance);} else{return "???";}
            case "[BW_size_change_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).size_change_time);} else{return "???";}
            case "[BW_size_change_strength_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).size_change_strength);} else{return "???";}
            case "[BW_atk_change_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).atk_change_chance);} else{return "???";}
            case "[BW_atk_change_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).atk_change_time);} else{return "???";}
            case "[BW_atk_change_strength_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).atk_change_strength);} else{return "???";}
            case "[BW_def_change_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).def_change_chance);} else{return "???";}
            case "[BW_def_change_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).def_change_time);} else{return "???";}
            case "[BW_def_change_strength_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).def_change_strength);} else{return "???";}
            case "[BW_allergic_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).allergic_chance);} else{return "???";}
            case "[BW_allergic_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).allergic_time);} else{return "???";}
            case "[BW_ohko_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).ohko_chance);} else{return "???";}
            case "[BW_charge_strength_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).charge_strength);} else{return "???";}
            case "[BW_fast_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).fast_chance);} else{return "???";}
            case "[BW_fast_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).fast_time);} else{return "???";}
            case "[BW_slow_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).slow_chance);} else{return "???";}
            case "[BW_slow_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).slow_time);} else{return "???";}
            case "[BW_fright_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).fright_chance);} else{return "???";}
            case "[BW_gale_force_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).gale_force_chance);} else{return "???";}
            case "[BW_payback_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).payback_time);} else{return "???";}
            case "[BW_hold_fast_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).hold_fast_time);} else{return "???";}
            case "[BW_invisible_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).invisible_chance);} else{return "???";}
            case "[BW_invisible_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).invisible_time);} else{return "???";}
            case "[BW_hp_regen_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).hp_regen_time);} else{return "???";}
            case "[BW_hp_regen_strength_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).hp_regen_strength);} else{return "???";}
            case "[BW_fp_regen_time_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).fp_regen_time);} else{return "???";}
            case "[BW_fp_regen_strength_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).fp_regen_strength);} else{return "???";}
            case "[BW_stage_background_fallweight1_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_background_fallweight1);} else{return "???";}
            case "[BW_stage_background_fallweight2_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_background_fallweight2);} else{return "???";}
            case "[BW_stage_background_fallweight3_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_background_fallweight3);} else{return "???";}
            case "[BW_stage_background_fallweight4_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_background_fallweight4);} else{return "???";}
            case "[BW_stage_nozzle_turn_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_nozzle_turn_chance);} else{return "???";}
            case "[BW_stage_nozzle_fire_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_nozzle_fire_chance);} else{return "???";}
            case "[BW_stage_ceiling_fall_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_ceiling_fall_chance);} else{return "???";}
            case "[BW_stage_object_fall_chance_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return String.valueOf(units.get(index).BattleWeaponData.get(15).stage_object_fall_chance);} else{return "???";}

            default: return phrase;
        }
    }

    public static String LOGIC_TCT(String phrase, int index, ArrayList<UnitData> units)
    {
        switch(phrase)
        {
            case "[LOGIC_ElementalWeakness_Struct1]": if(units.get(index).BattleUnitDefenseAttrData.size() > 0){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(0));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct1]": if(units.get(index).BattleUnitDefenseAttrData.size() > 0){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(0));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct2]": if(units.get(index).BattleUnitDefenseAttrData.size() > 1){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(1));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct2]": if(units.get(index).BattleUnitDefenseAttrData.size() > 1){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(1));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct3]": if(units.get(index).BattleUnitDefenseAttrData.size() > 2){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(2));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct3]": if(units.get(index).BattleUnitDefenseAttrData.size() > 2){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(2));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct4]": if(units.get(index).BattleUnitDefenseAttrData.size() > 3){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(3));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct4]": if(units.get(index).BattleUnitDefenseAttrData.size() > 3){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(3));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct5]": if(units.get(index).BattleUnitDefenseAttrData.size() > 4){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(4));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct5]": if(units.get(index).BattleUnitDefenseAttrData.size() > 4){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(4));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct6]": if(units.get(index).BattleUnitDefenseAttrData.size() > 5){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(5));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct6]": if(units.get(index).BattleUnitDefenseAttrData.size() > 5){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(5));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct7]": if(units.get(index).BattleUnitDefenseAttrData.size() > 6){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(6));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct7]": if(units.get(index).BattleUnitDefenseAttrData.size() > 6){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(6));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct8]": if(units.get(index).BattleUnitDefenseAttrData.size() > 7){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(7));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct8]": if(units.get(index).BattleUnitDefenseAttrData.size() > 7){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(7));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct9]": if(units.get(index).BattleUnitDefenseAttrData.size() > 8){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(8));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct9]": if(units.get(index).BattleUnitDefenseAttrData.size() > 8){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(8));} else{return "???";}
            case "[LOGIC_ElementalWeakness_Struct10]": if(units.get(index).BattleUnitDefenseAttrData.size() > 9){return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get(9));} else{return "???";}
            case "[LOGIC_ElementalImmunity_Struct10]": if(units.get(index).BattleUnitDefenseAttrData.size() > 9){return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get(9));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(0));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct1]": if(units.get(index).StatusVulnerabilityData.size() > 0){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(0));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(1));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct2]": if(units.get(index).StatusVulnerabilityData.size() > 1){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(1));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(2));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct3]": if(units.get(index).StatusVulnerabilityData.size() > 2){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(2));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(3));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct4]": if(units.get(index).StatusVulnerabilityData.size() > 3){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(3));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(4));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct5]": if(units.get(index).StatusVulnerabilityData.size() > 4){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(4));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(5));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct6]": if(units.get(index).StatusVulnerabilityData.size() > 5){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(5));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(6));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct7]": if(units.get(index).StatusVulnerabilityData.size() > 6){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(6));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(7));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct8]": if(units.get(index).StatusVulnerabilityData.size() > 7){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(7));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(8));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct9]": if(units.get(index).StatusVulnerabilityData.size() > 8){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(8));} else{return "???";}
            case "[LOGIC_StatusStrength_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get(9));} else{return "???";}
            case "[LOGIC_StatusWeakness_Struct10]": if(units.get(index).StatusVulnerabilityData.size() > 9){return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get(9));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return SStoYN(units.get(index).BattleWeaponData.get(0).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct1]": if(units.get(index).BattleWeaponData.size() > 0){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(0));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return SStoYN(units.get(index).BattleWeaponData.get(1).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct2]": if(units.get(index).BattleWeaponData.size() > 1){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(1));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return SStoYN(units.get(index).BattleWeaponData.get(2).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct3]": if(units.get(index).BattleWeaponData.size() > 2){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(2));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return SStoYN(units.get(index).BattleWeaponData.get(3).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct4]": if(units.get(index).BattleWeaponData.size() > 3){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(3));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return SStoYN(units.get(index).BattleWeaponData.get(4).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct5]": if(units.get(index).BattleWeaponData.size() > 4){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(4));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return SStoYN(units.get(index).BattleWeaponData.get(5).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct6]": if(units.get(index).BattleWeaponData.size() > 5){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(5));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return SStoYN(units.get(index).BattleWeaponData.get(6).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct7]": if(units.get(index).BattleWeaponData.size() > 6){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(6));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return SStoYN(units.get(index).BattleWeaponData.get(7).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct8]": if(units.get(index).BattleWeaponData.size() > 7){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(7));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return SStoYN(units.get(index).BattleWeaponData.get(8).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct9]": if(units.get(index).BattleWeaponData.size() > 8){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(8));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return SStoYN(units.get(index).BattleWeaponData.get(9).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct10]": if(units.get(index).BattleWeaponData.size() > 9){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(9));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return SStoYN(units.get(index).BattleWeaponData.get(10).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct11]": if(units.get(index).BattleWeaponData.size() > 10){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(10));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return SStoYN(units.get(index).BattleWeaponData.get(11).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct12]": if(units.get(index).BattleWeaponData.size() > 11){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(11));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return SStoYN(units.get(index).BattleWeaponData.get(12).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct13]": if(units.get(index).BattleWeaponData.size() > 12){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(12));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return SStoYN(units.get(index).BattleWeaponData.get(13).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct14]": if(units.get(index).BattleWeaponData.size() > 13){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(13));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return SStoYN(units.get(index).BattleWeaponData.get(14).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct15]": if(units.get(index).BattleWeaponData.size() > 14){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(14));} else{return "???";}
            case "[LOGIC_SuperguardYN_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return SStoYN(units.get(index).BattleWeaponData.get(15).superguard_state);} else{return "???";}
            case "[LOGIC_StatusAffliction_Struct16]": if(units.get(index).BattleWeaponData.size() > 15){return StatusAfflictionLogic(units.get(index).BattleWeaponData.get(15));} else{return "???";}

            default: return phrase;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Swallow Attribute into it's name
     */
    public static String SAtoName(int SA)
    {
        switch(SA)
        {
            case 1: return "Swallow Bomb";
            case 2: return "Spit into Other Enemy";
            case 4: return "Spit Fireball";
            case 8: return "Swallow Part of Enemy";
            default: return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a boolean into a yes/no
     */
    public static String BooltoYN(boolean bool)
    {
        if(bool)
        {
            return "Yes";
        }
        else
        {
            return "No";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Defense Attribute into it's name
     */
    public static String DAtoName(int DA)
    {
        switch(DA)
        {
            case 1: return "Weak To";
            case 2: return "Immune To";
            case 3: return "Absorbs";
            case 4: return "Immune To (Except Gulp)";
            default: return "Normal";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Superguard State into it's name
     */
    public static String SStoName(int SS)
    {
        switch(SS)
        {
            case 1: return "Superguardable with Recoil";
            case 2: return "Superguardable without Recoil";
            default: return "Unsuperguardable";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Superguard State into a yes/no
     */
    public static String SStoYN(int SS)
    {
        switch(SS)
        {
            case 1:
            case 2: return "Yes";
            default: return "No";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Element into it's name
     */
    public static String EtoName(int E)
    {
        switch(E)
        {
            case 1: return "Fire";
            case 2: return "Ice";
            case 3: return "Explosive";
            case 4: return "Electric";
            default: return "Normal";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Damage Pattern into it's name
     */
    public static String DPtoName(int DP)
    {
        switch(DP)
        {
            case 4: return "???";
            case 5: return "Gulp Knocked onto Ground";
            case 6: return "Gulp Knocked into next Target";
            case 8: return "???";
            case 10: return "Squashed Type 1";
            case 11: return "Squashed Type 2";
            case 12: return "Squashed Type 3";
            case 13: return "Inked";
            case 14: return "Super Hammer Knocked Back";
            case 16: return "Ultra Hammer Knocked Back";
            case 19: return "???";
            case 20: return "Confusion Effect";
            case 21: return "Spin Once 1";
            case 22: return "Spin Once 2";
            case 23: return "Quickly Spin 1";
            case 24: return "Quickly Spin 2";
            case 25: return "Blown Away";
            case 27: return "???";
            default: return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a AC Level into it's name
     */
    public static String ACLtoName(int ACL)
    {
        switch(ACL)
        {
            case 1: return "Very Easy";
            case 2: return "Easy";
            case 3: return "Normal";
            case 4: return "Difficult";
            case 5: return "Very Difficult";
            case 6: return "Extremely Difficult";
            default: return "Extremely Easy";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Determines the first Elemental Weakness
     */
    public static String ElementalWeaknessLogic(BattleUnitDefenseAttr unit)
    {
        if(unit.normal == 1)
        {
            return "Normal";
        }
        else if(unit.fire == 1)
        {
            return "Fire";
        }
        else if(unit.ice == 1)
        {
            return "Ice";
        }
        else if(unit.explosion == 1)
        {
            return "Explosion";
        }
        else if(unit.electric == 1)
        {
            return "Electric";
        }
        else
        {
            return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Determines the first Elemental Immunity
     */
    public static String ElementalImmunityLogic(BattleUnitDefenseAttr unit)
    {
        if(unit.normal == 2)
        {
            return "Normal";
        }
        else if(unit.fire == 2)
        {
            return "Fire";
        }
        else if(unit.ice == 2)
        {
            return "Ice";
        }
        else if(unit.explosion == 2)
        {
            return "Explosion";
        }
        else if(unit.electric == 2)
        {
            return "Electric";
        }
        else
        {
            return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Determines the lowest Status Vulnerability
     */
    public static String StatusStrengthLogic(StatusVulnerability unit)
    {
        String lowestName = "Sleep";
        int minimumValue = unit.sleep;

        if (unit.stop < minimumValue) {minimumValue = unit.stop; lowestName = "Stop";}
        if (unit.dizzy < minimumValue) {minimumValue = unit.dizzy; lowestName = "Dizzy";}
        if (unit.poison < minimumValue) {minimumValue = unit.poison; lowestName = "Poison";}
        if (unit.confuse < minimumValue) {minimumValue = unit.confuse; lowestName = "Confuse";}
        //if (unit.electric < minimumValue) {minimumValue = unit.electric; lowestName = "Electric";}
        if (unit.burn < minimumValue) {minimumValue = unit.burn; lowestName = "Burn";}
        if (unit.freeze < minimumValue) {minimumValue = unit.freeze; lowestName = "Freeze";}
        //if (unit.huge < minimumValue) {minimumValue = unit.huge; lowestName = "Huge";}
        if (unit.tiny < minimumValue) {minimumValue = unit.tiny; lowestName = "Tiny";}
        //if (unit.attack_up < minimumValue) {minimumValue = unit.attack_up; lowestName = "Attack Up";}
        if (unit.attack_down < minimumValue) {minimumValue = unit.attack_down; lowestName = "Attack Down";}
        //if (unit.defense_up < minimumValue) {minimumValue = unit.defense_up; lowestName = "Defense Up";}
        if (unit.defense_down < minimumValue) {minimumValue = unit.defense_down; lowestName = "Defense Down";}
        if (unit.allergic < minimumValue) {minimumValue = unit.allergic; lowestName = "Allergic";}
        if (unit.fright < minimumValue) {minimumValue = unit.fright; lowestName = "Fright";}
        if (unit.gale_force < minimumValue) {minimumValue = unit.gale_force; lowestName = "Gale Force";}
        //if (unit.fast < minimumValue) {minimumValue = unit.fast; lowestName = "Fast";}
        if (unit.slow < minimumValue) {minimumValue = unit.slow; lowestName = "Slow";}
        //if (unit.dodgy < minimumValue) {minimumValue = unit.dodgy; lowestName = "Dodgy";}
        //if (unit.invisible < minimumValue) {minimumValue = unit.invisible; lowestName = "Invisible";}
        if (unit.ohko < minimumValue) {minimumValue = unit.ohko; lowestName = "OHKO";}

        if(unit.sleep >= 100 && lowestName.equals("Sleep")) {lowestName = "None";}

        return lowestName;
    }

    /**
     * @Author Jemaroo
     * @Function Determines the highest Status Vulnerability
     */
    public static String StatusVulnerabilityLogic(StatusVulnerability unit)
    {
        String highestName = "Sleep";
        int maximumValue = unit.sleep;

        if (unit.stop > maximumValue) {maximumValue = unit.stop; highestName = "Stop";}
        if (unit.dizzy > maximumValue) {maximumValue = unit.dizzy; highestName = "Dizzy";}
        if (unit.poison > maximumValue) {maximumValue = unit.poison; highestName = "Poison";}
        if (unit.confuse > maximumValue) {maximumValue = unit.confuse; highestName = "Confuse";}
        //if (unit.electric > maximumValue) {maximumValue = unit.electric; highestName = "Electric";}
        if (unit.burn > maximumValue) {maximumValue = unit.burn; highestName = "Burn";}
        if (unit.freeze > maximumValue) {maximumValue = unit.freeze; highestName = "Freeze";}
        //if (unit.huge > maximumValue) {maximumValue = unit.huge; highestName = "Huge";}
        if (unit.tiny > maximumValue) {maximumValue = unit.tiny; highestName = "Tiny";}
        //if (unit.attack_up > maximumValue) {maximumValue = unit.attack_up; highestName = "Attack Up";}
        if (unit.attack_down > maximumValue) {maximumValue = unit.attack_down; highestName = "Attack Down";}
        //if (unit.defense_up > maximumValue) {maximumValue = unit.defense_up; highestName = "Defense Up";}
        if (unit.defense_down > maximumValue) {maximumValue = unit.defense_down; highestName = "Defense Down";}
        if (unit.allergic > maximumValue) {maximumValue = unit.allergic; highestName = "Allergic";}
        if (unit.fright > maximumValue) {maximumValue = unit.fright; highestName = "Fright";}
        if (unit.gale_force > maximumValue) {maximumValue = unit.gale_force; highestName = "Gale Force";}
        //if (unit.fast > maximumValue) {maximumValue = unit.fast; highestName = "Fast";}
        if (unit.slow > maximumValue) {maximumValue = unit.slow; highestName = "Slow";}
        //if (unit.dodgy > maximumValue) {maximumValue = unit.dodgy; highestName = "Dodgy";}
        //if (unit.invisible > maximumValue) {maximumValue = unit.invisible; highestName = "Invisible";}
        if (unit.ohko > maximumValue) {maximumValue = unit.ohko; highestName = "OHKO";}

        if(unit.sleep == 0 && highestName.equals("Sleep")) {highestName = "None";}

        return highestName;
    }

    /**
     * @Author Jemaroo
     * @Function Determines the highest Status Affliction
     */
    public static String StatusAfflictionLogic(BattleWeapon unit)
    {
        String highestName = "Sleep";
        int maximumValue = unit.sleep_chance;

        if (unit.stop_chance > maximumValue) {maximumValue = unit.stop_chance; highestName = "Stop";}
        if (unit.dizzy_chance > maximumValue) {maximumValue = unit.dizzy_chance; highestName = "Dizzy";}
        if (unit.poison_chance > maximumValue) {maximumValue = unit.poison_chance; highestName = "Poison";}
        if (unit.confuse_chance > maximumValue) {maximumValue = unit.confuse_chance; highestName = "Confuse";}
        if (unit.electric_chance > maximumValue) {maximumValue = unit.electric_chance; highestName = "Electric";}
        if (unit.dodgy_chance > maximumValue) {maximumValue = unit.dodgy_chance; highestName = "Dodgy";}
        if (unit.burn_chance > maximumValue) {maximumValue = unit.burn_chance; highestName = "Burn";}
        if (unit.freeze_chance > maximumValue) {maximumValue = unit.freeze_chance; highestName = "Freeze";}
        if (unit.size_change_chance > maximumValue && unit.size_change_strength < 128) {maximumValue = unit.size_change_chance; highestName = "Huge";}
        if (unit.size_change_chance > maximumValue && unit.size_change_strength > 127) {maximumValue = unit.size_change_chance; highestName = "Tiny";}
        if (unit.atk_change_chance > maximumValue && unit.atk_change_strength < 128) {maximumValue = unit.atk_change_chance; highestName = "Attack Up";}
        if (unit.atk_change_chance > maximumValue && unit.atk_change_strength > 127) {maximumValue = unit.atk_change_chance; highestName = "Attack Down";}
        if (unit.def_change_chance > maximumValue && unit.def_change_strength < 128) {maximumValue = unit.def_change_chance; highestName = "Defense Up";}
        if (unit.def_change_chance > maximumValue && unit.def_change_strength > 127) {maximumValue = unit.def_change_chance; highestName = "Defense Down";}
        if (unit.allergic_chance > maximumValue) {maximumValue = unit.allergic_chance; highestName = "Allergic";}
        if (unit.ohko_chance > maximumValue) {maximumValue = unit.ohko_chance; highestName = "OHKO";}
        if (unit.fast_chance > maximumValue) {maximumValue = unit.fast_chance; highestName = "Fast";}
        if (unit.slow_chance > maximumValue) {maximumValue = unit.slow_chance; highestName = "Slow";}
        if (unit.fright_chance > maximumValue) {maximumValue = unit.fright_chance; highestName = "Fright";}
        if (unit.gale_force_chance > maximumValue) {maximumValue = unit.gale_force_chance; highestName = "Gale Force";}
        if (unit.invisible_chance > maximumValue) {maximumValue = unit.invisible_chance; highestName = "Invisible";}

        if(unit.sleep_chance == 0 && highestName.equals("Sleep")) {highestName = "None";}

        return highestName;
    }
}