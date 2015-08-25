package br.com.tinycraft.zsoulkeep.commands.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Willian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandSoul
{

    public String permission() default "zsoulkeep.user";

    public String permissionMessage() default "§cVocê não tem permissão para isso.";

    public int args() default 1;

    public String command() default "default";
    
    public String superCommand() default "zsoulkeep";
    
    public String usage() default "";

}
