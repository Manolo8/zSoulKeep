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
    private int limit;

    public User(UUID uuid, long lastGive, int souls, int limit)
    {
        this.uuid = uuid;
        this.lastGive = lastGive;
        this.souls = souls;
        this.limit = limit;
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

    public int getLimit()
    {
        return limit;
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
    
    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public boolean giveSoul()
    {
        if (souls >= limit)
        {
            return false;
        } else
        {
            souls++;
            return true;
        }
    }

    public int giveSouls(int quantity)
    {
        int oldQuantity = souls;
        souls += quantity;

        if (souls > limit)
        {
            souls = limit;
        }
        
        return souls - oldQuantity;
    }
}
