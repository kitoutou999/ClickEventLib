package fr.swordtales.clickevent;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClickEventRegistry {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static ClickEventRegistry INSTANCE;

    private JavaPlugin plugin;
    private final List<Consumer<PlayerClickEvent>> listeners = new ArrayList<>();

    private ClickEventRegistry() {}

    public static ClickEventRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickEventRegistry();
        }
        return INSTANCE;
    }

    /**
     * Initialise le système de clic avec le nom de handler par défaut "ClickEventLib_Handler".
     * @param plugin Le plugin JavaPlugin
     * @return L'instance du registry pour chaîner les appels
     */
    public static ClickEventRegistry init(JavaPlugin plugin) {
        return init(plugin, "ClickEventLib_Handler");
    }

    /**
     * Initialise le système de clic avec un nom de handler personnalisé.
     * @param plugin Le plugin JavaPlugin
     * @param handlerName Le nom du handler (ex: "MonPlugin_ClickHandler")
     * @return L'instance du registry pour chaîner les appels
     */
    public static ClickEventRegistry init(JavaPlugin plugin, String handlerName) {
        ClickEventRegistry instance = getInstance();
        instance.plugin = plugin;

        // Enregistrer le handler avec le nom spécifié
        plugin.getCodecRegistry(Interaction.CODEC).register(
                handlerName,
                ClickItemHandler.class,
                ClickItemHandler.CODEC
        );

        LOGGER.atInfo().log("ClickEventRegistry initialized with handler: " + handlerName);
        return instance;
    }

    /**
     * Enregistre un listener pour recevoir les événements de clic.
     * @param listener Le listener
     * @return L'instance pour chaîner
     */
    public ClickEventRegistry register(Consumer<PlayerClickEvent> listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Supprime un listener.
     */
    public void unregister(Consumer<PlayerClickEvent> listener) {
        listeners.remove(listener);
    }

    /**
     * Dispatch un événement à tous les listeners.
     */
    public void dispatch(PlayerClickEvent event) {
        for (Consumer<PlayerClickEvent> listener : listeners) {
            try {
                listener.accept(event);
                if (event.isCancelled()) break;
            } catch (Exception e) {
                LOGGER.atWarning().withCause(e).log("Error dispatching PlayerClickEvent");
            }
        }
    }

    /**
     * Supprime tous les listeners.
     */
    public void clear() {
        listeners.clear();
    }
}
