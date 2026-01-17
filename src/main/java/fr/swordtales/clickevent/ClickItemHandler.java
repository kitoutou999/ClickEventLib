package fr.swordtales.clickevent;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.InteractionSyncData;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Handler pour les items qui déclenchent des PlayerClickEvent.
 */
public class ClickItemHandler extends SimpleInteraction {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final String itemId;

    public static final BuilderCodec<ClickItemHandler> CODEC =
            BuilderCodec.builder(ClickItemHandler.class, ClickItemHandler::new).build();

    public ClickItemHandler() {
        this.itemId = null;
    }

    public ClickItemHandler(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public void handle(@Nonnull Ref<EntityStore> ref, boolean firstRun,
                       float time, @Nonnull InteractionType type,
                       @Nonnull InteractionContext interactionContext) {
        super.handle(ref, firstRun, time, type, interactionContext);

        var store = ref.getStore();
        var player = store.getComponent(ref, Player.getComponentType());
        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());

        if (player == null || playerRef == null) return;

        ClickType clickType = convertToClickType(type);
        if (clickType == null) return;

        // Get target entity info
        Ref<EntityStore> targetRef = interactionContext.getTargetEntity();
        UUID targetUUID = null;
        int targetNetworkId = -1;

        InteractionSyncData chainData = interactionContext.getClientState();
        if (chainData != null) {
            targetNetworkId = chainData.entityId;
        }

        if (targetRef == null || !targetRef.isValid()) {
            if (chainData != null && chainData.entityId >= 0) {
                CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
                if (commandBuffer != null) {
                    targetRef = ((EntityStore) commandBuffer.getStore().getExternalData())
                            .getRefFromNetworkId(chainData.entityId);
                }
            }
        }

        if (targetRef != null && targetRef.isValid()) {
            CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
            if (commandBuffer != null) {
                UUIDComponent uuidComponent = commandBuffer.getComponent(targetRef, UUIDComponent.getComponentType());
                if (uuidComponent != null) {
                    targetUUID = uuidComponent.getUuid();
                }
            }
        }

        // Get target block
        BlockPosition targetBlock = interactionContext.getTargetBlock();

        // Determine target type
        TargetType targetType;
        if (targetRef != null && targetRef.isValid()) {
            targetType = TargetType.ENTITY;
        } else if (targetBlock != null) {
            targetType = TargetType.BLOCK;
        } else {
            targetType = TargetType.NONE;
        }

        // Get held item
        ItemStack heldItem = interactionContext.getHeldItem();

        // Click position
        Vector3d clickPosition = null;

        // Create and dispatch event
        PlayerClickEvent event = new PlayerClickEvent(
                playerRef,
                player,
                clickType,
                targetType,
                ref,
                targetRef,
                targetUUID,
                targetNetworkId,
                targetBlock,
                clickPosition,
                interactionContext,
                heldItem,
                this.itemId
        );

        ClickEventRegistry.getInstance().dispatch(event);
    }

    private ClickType convertToClickType(InteractionType type) {
        return switch (type) {
            case Primary -> ClickType.PRIMARY;
            case Secondary -> ClickType.SECONDARY;
            case Ability1 -> ClickType.ABILITY1;
            case Ability2 -> ClickType.ABILITY2;
            case Ability3 -> ClickType.ABILITY3;
            case Pick -> ClickType.PICK;
            default -> null;
        };
    }

    public String getItemId() {
        return itemId;
    }
}
