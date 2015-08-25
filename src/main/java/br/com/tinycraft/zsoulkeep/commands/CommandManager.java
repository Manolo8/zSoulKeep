package br.com.tinycraft.zsoulkeep.commands;

import br.com.tinycraft.zsoulkeep.commands.annotation.CommandSoul;
import br.com.tinycraft.zsoulkeep.language.Language;
import br.com.tinycraft.zsoulkeep.user.UserManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Willian
 */
public class CommandManager implements CommandExecutor
{

    private final Language language;
    private final CommandZSoul commandX1;
    private final List<Method> methods;

    public CommandManager(UserManager userManager, Language language)
    {
        this.language = language;
        this.methods = new ArrayList();
        this.commandX1 = new CommandZSoul(userManager, language);

        for (Method method : commandX1.getClass().getDeclaredMethods())
        {
            if (method.isAnnotationPresent(CommandSoul.class))
            {
                methods.add(method);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args)
    {
        if (!(cs instanceof Player))
        {
            cs.sendMessage("Players only");
            return true;
        }

        Player player = (Player) cs;

        String command = cmnd.getName().toLowerCase();

        for (Method method : methods)
        {
            CommandSoul annotation = method.getAnnotation(CommandSoul.class);

            if (!annotation.superCommand().equals(command))
            {
                continue;
            }

            if (args.length == 0)
            {
                if (!annotation.command().equalsIgnoreCase("default"))
                {
                    continue;
                }
            } else
            {
                if (!annotation.command().equalsIgnoreCase(args[0])
                        && !annotation.command().equalsIgnoreCase("any"))
                {
                    continue;
                }
            }
            if (!player.hasPermission(annotation.permission()))
            {
                player.sendMessage(annotation.permissionMessage());
                return true;
            }
            if (args.length != annotation.args())
            {
                player.sendMessage("§cUsage: " + annotation.usage());
                return true;
            }
            try
            {
                method.invoke(commandX1, player, args);
                return true;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
}
