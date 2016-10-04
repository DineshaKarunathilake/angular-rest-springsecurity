package net.dontdrinkandroot.example.angularrestspringsecurity.dao;


import net.dontdrinkandroot.example.angularrestspringsecurity.dao.newmeasuremententry.NewMeasurementEntryDao;
import net.dontdrinkandroot.example.angularrestspringsecurity.dao.user.UserDao;
import net.dontdrinkandroot.example.angularrestspringsecurity.entity.NewMeasurementEntry;
import net.dontdrinkandroot.example.angularrestspringsecurity.entity.Role;
import net.dontdrinkandroot.example.angularrestspringsecurity.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

/**
 * Initialize the database with some test entries.
 *
 * @author Philip W. Sorst <philip@sorst.net>
 */
public class DataBaseInitializer
{
    private NewMeasurementEntryDao newMeasurementEntryDao;

    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    protected DataBaseInitializer()
    {
        /* Default constructor for reflection instantiation */
    }

    public DataBaseInitializer(UserDao userDao, NewMeasurementEntryDao newMeasurementEntryDao, PasswordEncoder passwordEncoder)
    {
        this.userDao = userDao;
        this.newMeasurementEntryDao = newMeasurementEntryDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void initDataBase()
    {
        User userUser = new User("user", this.passwordEncoder.encode("user"));
        userUser.addRole(Role.USER);
        this.userDao.save(userUser);

        User adminUser = new User("admin", this.passwordEncoder.encode("admin"));
        adminUser.addRole(Role.USER);
        adminUser.addRole(Role.ADMIN);
        this.userDao.save(adminUser);

        long timestamp = System.currentTimeMillis() - (1000 * 60 * 60 * 24);
        for (int i = 0; i < 10; i++) {
            NewMeasurementEntry newMeasurementEntry = new NewMeasurementEntry();
            newMeasurementEntry.setContent("This is example content " + i);
            newMeasurementEntry.setDate(new Date(timestamp));
            this.newMeasurementEntryDao.save(newMeasurementEntry);
            timestamp += 1000 * 60 * 60;
        }
    }
}
