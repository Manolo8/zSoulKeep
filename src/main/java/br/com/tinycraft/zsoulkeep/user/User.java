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
    private final long giveDelay;

    public User(UUID uuid, long lastGive, int souls, int limit, long giveDelay)
    {
        this.uuid = uuid;
        this.lastGive = lastGive;
        this.souls = souls;
        this.limit = limit;
        this.giveDelay = giveDelay * 1000;
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

    public int updateSouls(long actualTime)
    {
        long timeDef = actualTime - lastGive;

        int soulsToGive = (int) (timeDef / giveDelay);

        lastGive = lastGive + soulsToGive * giveDelay;

        return giveSouls(soulsToGive);
    }

    public int giveSouls(int quantity)
    {
        if (quantity < 1)
        {
            return 0;
        }

        int oldQuantity = souls;

        souls += quantity;

        if (souls > limit)
        {
            souls = limit;
        }
        return souls - oldQuantity;
    }
}
