package br.com.tinycraft.zsoulkeep.user;

import java.util.UUID;

/**
 *
 * @author Willian
 */
public class User
{

    private final UUID uuid;
    private long lastGive;
    private int souls;
    private int limite;

    public User(UUID uuid, long lastGive, int souls, int limite)
    {
        this.uuid = uuid;
        this.lastGive = lastGive;
        this.souls = souls;
        this.limite = limite;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public long getLastGive()
    {
        return lastGive;
    }

    public int getSouls()
    {
        return souls;
    }

    public int lostSoul()
    {
        int r = souls;
        if (souls != 0)
        {
            souls--;
        }
        return r;
    }

    public void setLastGive(long lastGive)
    {
        this.lastGive = lastGive;
    }

    public void setSouls(int souls)
    {
        this.souls = souls;
    }

    public boolean giveSoul()
    {
        if (souls >= limite)
        {
            return false;
        } else
        {
            souls++;
            return true;
        }
    }

    public void giveSouls(int quantity)
    {
        souls += quantity;

        if (souls > limite)
        {
            souls = limite;
        }
    }
}
