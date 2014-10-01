package plugin;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.*;
import java.net.MalformedURLException;

/**
 * Over classes of plugins whose class files are placed In the URL data, and
 * creates an instance of each plugin load. This delegate class has ClassLoader
 * loading classes before Create instances.
 * <P>
 * You can browse the URL to load "hot" new plugins Who have been newly
 * installed (loadPlugins method). In this case, the old plugins are not
 * refills. It is even recuprer new versions of plugins with The methods
 * reloadPlugins.
 * <P>
 * Normally this class is used by a PluginManager but not Directly by clients
 * who want to load plugins.
 *
 * @ Author Richard Grin (amend Philippe Renevier)
 * @version 1.0
 */
public class PluginLoader {

    /**
     * The class loader that will load plugins.
     */
    private ClassLoader loader;
    /**
     * The directory where the plugins will be research.
     */
    private String pluginDirectory;
    /**
     * List of instances of plugins that have been loads loadPlugins.
     */
    private final List<Plugin> loadedPluginInstances = new ArrayList<Plugin>();

    private static final Logger logger
            = Logger.getLogger("fr.unice.plugin.PluginLoader");

    /**
     * Created a body that will look for plugins in the directory which We pass
     * the Parameter name.
     *
     * @param directory
     * @throws java.net.MalformedURLException
     */
    public PluginLoader(String directory) throws MalformedURLException {
        // On verifie que l'URL correspond bien  un repertoire.
        File dirFile = new File(directory);
        if (dirFile == null || !dirFile.isDirectory()) {
            logger.log(Level.WARNING, "{0} is not a directory", directory);
            throw new IllegalArgumentException(directory + " is not a directory");
        }

        // Si c'est un repertoire mais que l'URL ne se termine pas par un "/",
        // on ajoute un "/"  la fin (car URL ClassLoader oblige a donner
        // un URL qui se termine par un "/" pour les repertoires).
        if (!directory.endsWith("/")) {
            directory += "/";
        }
        this.pluginDirectory = directory;
        // Cree le chargeur de classes.
        createNewClassLoader();
    }

    /**
     * Load plugins instances of a certain type places in the Directory
     * indicates to the creation PluginLoader. these bodies Are loaded in
     * addition to those that have already been loaded. If you do not want that
     * instances of plugins that will be loaded In this method, and with new
     * versions if any, must Use {link #reloadPlugins (Class)} You can recuprer
     * these plugins by the method {}link #getPluginInstances. If a plugin has
     * already been loaded, it is not Recharge, even if a newer version is
     * found.
     *
     * @param type kind plugins research. If null, load plugins Of all types.
     */
    public void loadPlugins(Class type) {
        // En prevision d'un chargement ailleurs que d'un repertoire, on fait
        // cette indirection. On pourrait ainsi charger d'un jar.
        loadFromDirectory(type);
    }

    /**
     * Load instances of all plugins. You can recuprer these plugins by the
     * method {}link #getPluginInstances. If a plugin has already been loaded,
     * it is not Recharge, even if a new version is encountered.
     */
    public void loadPlugins() {
        loadPlugins(null);
    }

    /**
     * Reloads all plugins. Load the new versions of plugins if the encounter.
     */
    public void reloadPlugins() {
        reloadPlugins(null);
    }

    /**
     * Reloads all plugins of a given type. Load the new versions of plugins if
     * the encounter.
     *
     * @param type
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public void reloadPlugins(Class type) {
        // Cree un nouveau chargeur pour charger les nouvelles versions.
        try {
            createNewClassLoader();
        } catch (MalformedURLException ex) {
            // Ne devrait jamais arriver car si l'URL etait mal forme,
            // on n'aurait pu creer "this".
            ex.printStackTrace();
        }
        // Et efface tous les plugins du type deja chargs.
        erasePluginInstances(type);
        // Recharge les plugins du type
        loadPlugins(type);
    }

    /**
     * Returns the instances of plugins That Were recupres this time and Before
     * the time (if you do not clear the plugins frais Before When Last search
     * plugins {link #loadPlugins (boolean)}.
     *
     * @return the recuprees bodies. The table is full.
     */
    public Plugin[] getPluginInstances() {
        return getPluginInstances(null);
    }

    public Plugin[] getPluginInstances(Class type) {
        List<Plugin> loadedPluginInstancesOfThatType = new ArrayList<Plugin>();
        for (Plugin plugin : loadedPluginInstances) {
            if (type == null || plugin.getType().equals(type)) {
                loadedPluginInstancesOfThatType.add(plugin);
            }
        }
        return loadedPluginInstancesOfThatType.toArray(new Plugin[0]);
    }

    /**
     * Clears the class loader.
     */
    private void eraseClassLoader() {
        loader = null;
    }

    /**
     * Creates a new charger. Then will upload new Versions of plugins.
     */
    private void createNewClassLoader() throws MalformedURLException {
        logger.info("******Creation of a new class loader");
        loader = URLClassLoader.newInstance(new URL[]{getURL(pluginDirectory)});
    }

    /**
     * Clears all the plugins of some type already loads.
     *
     * @param type kind plugins erase. Clears all plugins if null.
     */
    private void erasePluginInstances(Class type) {
        if (type == null) {
            loadedPluginInstances.clear();
        } else {
            for (Plugin plugin : loadedPluginInstances) {
                if (plugin.getType().equals(type)) {
                    loadedPluginInstances.remove(plugin);
                }
            }
        }
    }

    /**
     * Load plugins of a certain type in a directory available Not in a jar.
     *
     * @param kind kind of plugins. Load all plugins if <code> null </ code>.
     */
    private void loadFromDirectory(Class type) {
        // To find the full name plugins trouvs: this is the part
        // The path that is in addition to the basic directory data to the loader.
        // For example, if the base path is dir1 / dir2, the plugin
        // Name machin.truc.P1 be in dir1 / dir2 / foo / stuff / P1.class
        logger.info("=+=+=+=+=+ enter loadPluginsFromDirectory=+=+=+=+");
        loadFromSubdirectory(new File(pluginDirectory), type, pluginDirectory);
        logger.info("=+=+=+=+=+ output loadPluginsFromDirectory=+=+=+=+");
    }

    /**
     * Load plugins places directly under a subdirectory A directory basis. The
     * two directories are not in a jar. Recursive call of this method by itself
     * it is time The directories to explore
     *
     * @param dir subdirectory. The package name of the plugin will Correspond
     * to the relative position of the sub-directory relative The base
     * directory.
     * @param kind kind of plugins to load.
     * @param baseName name of the directory where the plugins are Load all
     * plugins if <code> null </ code>.
     */
    private void loadFromSubdirectory(File dir, Class type, String baseName) {
        logger.log(Level.INFO, "Loading in the subdirectory {0} with base name {1}", new Object[]{dir, baseName});
        int baseNameLength = baseName.length();
        // On parcourt toute l'arborescence  la recherche de classes
        // qui pourraient etre des plugins.
        // Quand on l'a trouve, on en deduit son paquetage avec sa position
        // relativement a baseName.
        File[] files = dir.listFiles();
        logger.log(Level.INFO, "the list : {0}", files);
        for (File file : files) {
            if (file.isDirectory()) {
                loadFromSubdirectory(file, type, baseName);
                continue;
            }
            // Ca n'est pas un repertoire, on test si c'est un plugin
            logger.log(Level.INFO, "Review File  {0};{1}", new Object[]{file.getPath(), file.getName()});
            String path = file.getPath();
            String qualifiedClassName = getQualifiedName(baseNameLength, path);
            // On obtient une instance de cette classe
            if (qualifiedClassName != null) {
                Plugin plugin = getInstance(qualifiedClassName, type);
                if (plugin != null) {
                    logger.log(Level.INFO, "Class {0} is a plugin !", qualifiedClassName);
                    // S'il n'y a pas deja un plugin de la meme classe, on ajoute
                    // l'instance de plugin que l'on vient de creer.
                    boolean alreadyLoaded = false;
                    for (Plugin loadedPlugin : loadedPluginInstances) {
                        if (loadedPlugin.getClass().equals(plugin.getClass())) {
                            alreadyLoaded = true;
                            break;
                        }
                    }
                    if (!alreadyLoaded) {
                        loadedPluginInstances.add(plugin);
                    }
                    logger.log(Level.INFO, "The plugin : {0}", loadedPluginInstances);
                }
            }
        }
    }

    /**
     * In the case where a path is a class file, Calculates the full name of a
     * class from the name of a directory Basic and path of the class, the two
     * paths being the same anchors Root directory. The base directory ends with
     * "/" (see URLClassLoader). For example, a / b / c / (that is - say 6
     * baseNameLength) And a / b / c / d / e / F.class give d.e.F
     *
     * @param baseNameLength number of characters of the name of the base
     * directory.
     * @param path classPath class.
     * @return the full name of the class, or null if the name does not match
     * Not have an external class.
     */
    private String getQualifiedName(int baseNameLength, String classPath) {
        logger.log(Level.INFO, "Calcul du nom qualifi de {0} en enlevant {1} caractres au dbut", new Object[]{classPath, baseNameLength});
        // Un plugin ne peut etre une classe interne
        if ((!classPath.endsWith(".class")) || (classPath.indexOf('$') != -1)) {
            return null;
        }
        // C'est bien une classe externe
        classPath = classPath.substring(baseNameLength)
                .replace(File.separatorChar, '.');
        // On enelve le .class final pour avoir le nom de la classe
        logger.log(Level.INFO, "Nom complet de la classe : {0}", classPath);
        return classPath.substring(0, classPath.lastIndexOf('.'));
    }

    /**
     * Transforms the directory name in URL if the client does not give A good
     * format for the URL (so you can create a URLClassLoader).
     *
     * @param dir directory name.
     * @ Return the URL with the right format.
     * @ Throws MalformedURLException thrown if you can guess what URL it Is.
     */
    private static URL getURL(String dir) throws MalformedURLException {
        logger.log(Level.INFO, "URL non transforme : {0}", dir);

        /*
         * We begin by transforming the absolute URL names in Windows;
         * Eg transform C: \ dir \ foo in file: / C: / dir / foo
         */
        if (dir.indexOf("\\") != -1) {
            // Can suspecting a Windows name!
            // 4 \ for \\ for regulire expression!
            dir = dir.replaceAll("\\\\", "/");
        } //  Windows Names

        /* This is a directory; several cases:
         * 1 If there is the protocol "file", it does nothing; for instance,
         * The user indicates that the plugins are in a directory
         * With absolute name and in this case, he must put the protocol
         * "File:" at the beginning;
         * 2 If there is no protocol "file", it is added.
         */
        if (!dir.startsWith("file:")) {
            /* We consider that this is the name of a resource; if the name is
             * Absolute is a name relative to the classpath.
             */
            dir = "file:" + dir;
        }

        logger.log(Level.INFO, "URL transforme : {0}", dir);
        return new URL(dir);
    }

    /**
     * Returns an instance of a type of plugin gives.
     *
     * @param className name of the plugin class
     * @param type Type plugin
     * @param cl class loader that will do the loading of the class Plugin
     * @ Return an instance of plugin. Returns null if problem. For example, if
     * the plugin is not the right kind.
     */
    private Plugin getInstance(String nomClasse, Class type) {
        logger.info("Entre dans getInstance");
        try {
            // C'est ici que se passe le chargement de la classe par le
            // chargeur de classes.
            logger.log(Level.INFO, "Demande de chargement de la classe {0} par {1}", new Object[]{nomClasse, this});

            Class newClass = null;
            Plugin result = null;
            // ***** DU CODE A ECRIRE ICI !!!!!
            newClass = loader.loadClass(nomClasse);

            try {
                // On cree une instance de la classe
                logger.log(Level.INFO, "Creation d''une instance de {0}", newClass);

                // ***** DU CODE A ECRIRE ICI !!!!!
                if (!(newClass.isInterface() || Modifier.isAbstract(newClass.getModifiers()))) {
                    result = (Plugin) newClass.newInstance();
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
                // Le fichier  n'est pas un plugin 'Plugin'
                logger.log(Level.WARNING, "La classe {0} n''est pas un Plugin", nomClasse);
                return null;
            } catch (InstantiationException e) {
                e.printStackTrace();
                logger.log(Level.WARNING, "La classe {0} ne peut pas etre instanci", nomClasse);
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.log(Level.WARNING, "La classe {0} est interdite d''accs", nomClasse);
                return null;
            } catch (NoClassDefFoundError e) {
                // Survient si la classe n'a pas le bon nom
                e.printStackTrace();
                logger.log(Level.WARNING, "La classe {0} ne peut tre trouve", nomClasse);
                return null;
            } // Fin des catchs pour le try pour creation instance

            // Teste si plug est du bon type
            // ***** DU CODE A ECRIRE ICI !!!!!
            if (type != null && !type.isInstance(result)) {
                return null;
            }
            return result;
        } // Les catchs pour le 1er try pour chargement de la classe
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Le plugin {0} est introuvable", nomClasse);
            return null;
        } catch (NoClassDefFoundError e) {
            // Survient si la classe n'a pas le bon nom
            e.printStackTrace();
            logger.log(Level.WARNING, "La classe {0} ne peut tre trouve", nomClasse);
            return null;
        }
    }
}
