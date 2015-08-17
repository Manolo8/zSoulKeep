package br.com.tinycraft.zsoulkeep.listener;

import br.com.tinycraft.zsoulkeep.user.User;
import br.com.tinycraft.zsoulkeep.user.UserManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Willian
 */
public class PlayerListener implements Listener
{
    
    private final UserManager userManager;
    private final boolean keepXP;
    private final boolean keepInventory;
    private final int soulGiveDelay;
    private final boolean giveOffLine;
    private final HashMap<Player, List<ItemStack[]>> playersItems;
    
    public PlayerListener(UserManager userManager)
    {
        this.userManager = userManager;
        this.keepInventory = true;
        this.keepXP = true;
        this.soulGiveDelay = 1000 * 60 * 60 * 2;
        this.giveOffLine = true;
        playersItems = new HashMap();
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e)
    {
        User user = userManager.loadUser(e.getPlayer());
        int souls = 0;
        if (giveOffLine)
        {
            long currentTime = System.currentTimeMillis();
            long lastTime = user.getLastGive();
            long tempResult = currentTime - lastTime;
            while (tempResult >= soulGiveDelay)
            {
                tempResult -= soulGiveDelay;
                souls++;
            }
            user.giveSouls(souls);
        }
        
        if (souls == 0)
        {
            e.getPlayer().sendMessage("§aVocê tem " + user.getSouls() + " almas.");
        } else
        {
            e.getPlayer().sendMessage("§aVocê recebeu " + souls + " almas em quanto offline. Agora tem " + user.getSouls() + ".");
        }
    }
    
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e)
    {
        userManager.saveUser(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }
        
        userManager.saveUser(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e)
    {
        if (this.playersItems.containsKey(e.getPlayer()))
        {
            Player player = e.getPlayer();
            
            int row = 0;
            
            for (ItemStack[] items : playersItems.get(e.getPlayer()))
            {
                if (row == 0)
                {
                    player.getInventory().setArmorContents(items);
                } else
                {
                    player.getInventory().setContents(items);
                }
                row++;
            }
        }
        
        this.playersItems.remove(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeathEvent(PlayerDeathEvent e)
    {
        Player player = e.getEntity();
        User user = userManager.getUser(player.getUniqueId());
        if (user.lostSoul() == 0)
        {
            player.sendMessage("§cVocê morreu sem possuir almas, e acabou perdendo todos os seus itens!");
            return;
        }
        
        boolean inventory = player.hasPermission("zsoulkeep.keep.inventory");
        boolean exp = player.hasPermission("zsoulkeep.keep.exp");
        int almas = user.getSouls();
        
        if (almas == 0)
        {
            player.sendMessage("§cNa proxima morte todos os seus itens serão dropados! Tome cuidado. ");
        } else
        {
            player.sendMessage("§aLhe restam " + almas + " almas, quando você as perder, perderá seus itens.");
        }
        
        if (keepInventory && inventory)
        {
            List<ItemStack[]> items = new ArrayList();
            items.add(e.getEntity().getInventory().getArmorContents());
            items.add(e.getEntity().getInventory().getContents());
            
            this.playersItems.put(player, items);
            
            e.getDrops().clear();
        }
        
        if (keepXP && exp)
        {
            e.setKeepLevel(true);
            e.setDroppedExp(0);
        }
    }
}
