/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitDefense data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
public class BattleUnitDefense
{
    public int normal = 0;
    public int fire = 0;
    public int ice = 0;
    public int explosion = 0;
    public int electric = 0;

    public final int normal_offset = 0;
    public final int fire_offset = 1;
    public final int ice_offset = 2;
    public final int explosion_offset = 3;
    public final int electric_offset =  4;
}