package plugin.util;

import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import plugin.*;

public class PluginMenuItemFactory {

    /**
     * The menu managed by this instance.
     */
    private final JMenu menu;

    /**
     * The class loader loads the plugins.
     */
    private final PluginLoader loader;

    /**
     * The actionListener that will listen to the ENTERED menu plugins.
     */
    private final ActionListener listener;

    private static final Logger logger = Logger.getLogger("fr.unice.plugin.PluginMenu");

    /**
     * Creates an instance regarding a menu. This menu will Choices that will
     * select a plugin or another.
     *
     * @param menu the menu managed by this instance.
     * @param loader the class loader plugins.
     * @param listener
     */
    public PluginMenuItemFactory(JMenu menu, PluginLoader loader, ActionListener listener) {
        logger.setLevel(Level.OFF);

        this.menu = menu;
        this.loader = loader;
        this.listener = listener;
    }

    /**
     * Construit le menu des plugins.
     *
     * @param type type des plugins utiliss pour construire le menu. Si null,
     * tous les types de plugin seront utilises pour construire le menu.
     */
    public void buildMenu(Class type) {
        if (loader == null) {
            return;
        }
        logger.info("Construction du menu des PLUGINS");

        // Enleve les entrees precedentes s'il y en avait
        menu.removeAll();

        // Recupere les instances deja chargees
        Plugin[] instancesPlugins;
        instancesPlugins = loader.getPluginInstances();
        logger.log(Level.INFO, "Nombre de plugins trouves :{0}", instancesPlugins.length);
        for (Plugin plugin : instancesPlugins) {
            PluginMenuItem item = new PluginMenuItem(plugin.getName(), plugin);
            item.addActionListener(listener);
            menu.add(item);
        }
    }

}
