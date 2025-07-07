/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitKind data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
public class BattleUnitKind
{
    public int HP = 0;
    public int level = 0;
    public int bonusXP = 0;
    public int bonusCoin = 0;
    public int bonusCoinRate = 0;
    public int baseCoin = 0;
    public int runRate = 0;
    public int pbCap = 0;

    public final int HP_offset = 8;
    public final int level_offset = 14;
    public final int bonusXP_offset = 15;
    public final int bonusCoin_offset = 16;
    public final int bonusCoinRate_offset = 17;
    public final int baseCoin_offset = 18;
    public final int runRate_offset = 19;
    public final int pbCap_offset = 20;
}