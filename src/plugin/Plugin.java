package plugin;

/**
 * Generic interface that all plugins must implementer. Classes that implement
 * this interface will also provide A constructor parameter (an implicit or non
 * fawn).
 *
 * @ Author Richard Grin (modified version of Michel Buffa) version 2.0 12/07/02
 */
public interface Plugin {

    /**
     * @return the name of the plugin
     */
    public String getName();

    /**
     * @return the type of plugin
     */
    public Class getType();
}
