package br.com.tinycraft.zsoulkeep.commands;

import br.com.tinycraft.zsoulkeep.commands.annotation.CommandSoul;
import br.com.tinycraft.zsoulkeep.language.Language;
import br.com.tinycraft.zsoulkeep.user.User;
import br.com.tinycraft.zsoulkeep.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Willian
 */
public class CommandZSoul
{

    private final UserManager userManager;
    private final Language language;

    public CommandZSoul(UserManager userManager, Language language)
    {
        this.userManager = userManager;
        this.language = language;
    }

    @CommandSoul(command = "setsouls",
            superCommand = "soulsadm",
            args = 3,
            permission = "zsoulkeep.admin.setsouls",
            usage = "§a/<command> setsouls [nick] quantity")
    public void setSouls(Player author, String[] args)
    {
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null)
        {
            author.sendMessage(language.getMessage("command.target.notfound", args[1]));
            return;
        }

        int quantity = 0;

        try
        {
            quantity = Integer.parseInt(args[2]);
        } catch (Exception e)
        {
            author.sendMessage(language.getMessage("command.fail", "O valor não é inteiro."));
            return;
        }

        User user = userManager.getUser(player.getUniqueId());
        user.setSouls(quantity);
        author.sendMessage(language.getMessage("command.setsouls.sucess", args[1], quantity));
    }

    @CommandSoul(command = "addsouls",
            superCommand = "soulsadm",
            args = 3,
            permission = "zsoulkeep.admin.addsouls",
            usage = "§a/<command> addsouls [nick] quantity")
    public void addSouls(Player author, String[] args)
    {
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null)
        {
            author.sendMessage(language.getMessage("command.target.notfound", args[1]));
            return;
        }

        int quantity = 0;

        try
        {
            quantity = Integer.parseInt(args[2]);
        } catch (Exception e)
        {
            author.sendMessage(language.getMessage("command.fail", "O valor não é inteiro."));
            return;
        }

        User user = userManager.getUser(player.getUniqueId());
        user.setSouls(quantity + user.getSouls());
        author.sendMessage(language.getMessage("command.addsouls.sucess", quantity, args[1]));
    }

    @CommandSoul(command = "setlimit",
            superCommand = "soulsadm",
            args = 3,
            permission = "zsoulkeep.admin.setlimit",
            usage = "§a/<command> setlimit [nick] quantity")
    public void setLimit(Player author, String[] args)
    {
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null)
        {
            author.sendMessage(language.getMessage("command.target.notfound", args[1]));
            return;
        }

        int quantity = 0;

        try
        {
            quantity = Integer.parseInt(args[2]);
        } catch (Exception e)
        {
            author.sendMessage(language.getMessage("command.fail", "O valor não é inteiro."));
            return;
        }

        User user = userManager.getUser(player.getUniqueId());
        user.setLimit(quantity);
        author.sendMessage(language.getMessage("command.setlimit.sucess", args[1], quantity));
    }

    @CommandSoul(superCommand = "souls",
            command = "default",
            args = 0,
            usage = "§a/<command>",
            permission = "zsoulkeep.user.souls.you")
    public void souls(Player author, String[] args)
    {
        User user = userManager.getUser(author.getUniqueId());
        author.sendMessage(language.getMessage("command.souls", user.getSouls()));
    }

    @CommandSoul(superCommand = "souls",
            command = "any",
            args = 1,
            permission = "zsoulkeep.user.souls.other",
            usage = "§a/<command> [player]")
    public void soulsOther(Player author, String[] args)
    {
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            author.sendMessage(language.getMessage("command.target.notfound", args[1]));
            return;
        }

        User user = userManager.getUser(player.getUniqueId());
        author.sendMessage(language.getMessage("command.souls.other", user.getSouls()));
    }
}
