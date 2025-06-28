import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ByteUtils
{
    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given file and return the byte data
     */
    public static byte[] readData(File file)
    {
        try 
        {
            FileInputStream f1 = new FileInputStream(file);

            byte[] data = new byte[(int)file.length()];

            f1.read(data);

            f1.close();

            return data;
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("There was an Error Finding the Input File");
        }
        catch (IOException e)
        {
            System.out.println("There was an Error Reading the Input File");
        }
    
        return new byte[0];
    }

    /**
     * @Author ChatGPT
     * @Function Will convert 1 byte to a single int
     */
    public static int bytesToInt(byte b1) 
    {
        return b1 & 0xFF;
    }

    /**
     * @Author ChatGPT
     * @Function Will convert 2 bytes to a single int
     */
    public static int bytesToInt(byte b1, byte b2) 
    {
        return ((b1 & 0xFF) << 8) | (b2 & 0xFF);
    }

    /**
     * @Author ChatGPT
     * @Function Will convert 4 bytes to a single int
     */
    public static int bytesToInt(byte b1, byte b2, byte b3, byte b4) 
    {
        return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }

    /**
     * @Author ChatGPT
     * @Function Will convert a single int into 1 byte
     */
    public static byte intTo1Byte(int value) 
    {
        byte result = (byte) (value & 0xFF);
        return result;
    }

    /**
     * @Author ChatGPT
     * @Function Will convert a single int into 2 bytes
     */
    public static byte[] intTo2Bytes(int value) 
    {
        byte[] result = new byte[2];
        result[0] = (byte) ((value >> 8) & 0xFF);
        result[1] = (byte) (value & 0xFF);
        return result;
    }

    /**
     * @Author ChatGPT
     * @Function Will convert a single int into 4 bytes
     */
    public static byte[] intTo4Bytes(int value) 
    {
        byte[] result = new byte[4];
        result[0] = (byte) ((value >> 24) & 0xFF);
        result[1] = (byte) ((value >> 16) & 0xFF);
        result[2] = (byte) ((value >> 8) & 0xFF);
        result[3] = (byte) (value & 0xFF);
        return result;
    }
}