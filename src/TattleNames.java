/**
 * @Author Jemaroo
 * @Function Storage object for handling the names of Tattle Entries
 */
public class TattleNames
{
    public String btl = "";
    public String menu = "";
    public boolean menuFlag = false;
    public String clone = "";
    public boolean cloneFlag = false;

    public TattleNames(String Nbtl)
    {
        this.btl = Nbtl;
    }

    public TattleNames(String Nbtl, String Nmenu)
    {
        this.btl = Nbtl;
        this.menu = Nmenu;
        this.menuFlag = true;
    }

    public TattleNames(String Nbtl, String Nmenu, String Nclone)
    {
        this.btl = Nbtl;
        this.menu = Nmenu;
        this.clone = Nclone;
        this.cloneFlag = true;
    }
}