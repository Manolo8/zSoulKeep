package br.com.tinycraft.zsoulkeep.data;

import br.com.tinycraft.zsoulkeep.exceptions.NoUserDataException;
import br.com.tinycraft.zsoulkeep.user.User;
import java.util.UUID;

/**
 *
 * @author Willian
 */
public interface UserData 
{
    User loadUser(UUID uuid) throws NoUserDataException;
    
    void saveUser(User user);
    
    void saveData();
}
