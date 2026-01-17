package fr.swordtales.clickevent;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerClickEvent {

    private final PlayerRef playerRef;
    private final Player player;
    private final ClickType clickType;
    private final TargetType targetType;
    private final Ref<EntityStore> playerEntityRef;
    private final Ref<EntityStore> targetEntityRef;
    private final UUID targetUUID;
    private final int targetNetworkId;
    private final BlockPosition targetBlock;
    private final Vector3d clickPosition;
    private final InteractionContext context;
    private final ItemStack heldItem;
    private final String itemId;
    private boolean cancelled = false;

    public PlayerClickEvent(
            PlayerRef playerRef,
            Player player,
            ClickType clickType,
            TargetType targetType,
            Ref<EntityStore> playerEntityRef,
            @Nullable Ref<EntityStore> targetEntityRef,
            @Nullable UUID targetUUID,
            int targetNetworkId,
            @Nullable BlockPosition targetBlock,
            @Nullable Vector3d clickPosition,
            InteractionContext context,
            @Nullable ItemStack heldItem,
            @Nullable String itemId
    ) {
        this.playerRef = playerRef;
        this.player = player;
        this.clickType = clickType;
        this.targetType = targetType;
        this.playerEntityRef = playerEntityRef;
        this.targetEntityRef = targetEntityRef;
        this.targetUUID = targetUUID;
        this.targetNetworkId = targetNetworkId;
        this.targetBlock = targetBlock;
        this.clickPosition = clickPosition;
        this.context = context;
        this.heldItem = heldItem;
        this.itemId = itemId;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public Player getPlayer() {
        return player;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public Ref<EntityStore> getPlayerEntityRef() {
        return playerEntityRef;
    }

    @Nullable
    public Ref<EntityStore> getTargetEntityRef() {
        return targetEntityRef;
    }

    public boolean hasTarget() {
        return targetType != TargetType.NONE;
    }

    public boolean hasEntityTarget() {
        return targetType == TargetType.ENTITY && targetEntityRef != null && targetEntityRef.isValid();
    }

    public boolean hasBlockTarget() {
        return targetType == TargetType.BLOCK && targetBlock != null;
    }

    @Nullable
    public UUID getTargetUUID() {
        return targetUUID;
    }

    public int getTargetNetworkId() {
        return targetNetworkId;
    }

    @Nullable
    public BlockPosition getTargetBlock() {
        return targetBlock;
    }

    @Nullable
    public Vector3d getClickPosition() {
        return clickPosition;
    }

    public InteractionContext getContext() {
        return context;
    }

    @Nullable
    public ItemStack getHeldItem() {
        return heldItem;
    }

    @Nullable
    public String getItemId() {
        return itemId;
    }

    public boolean isFromItem(String itemId) {
        return this.itemId != null && this.itemId.equals(itemId);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isRightClick() {
        return clickType == ClickType.SECONDARY;
    }

    public boolean isLeftClick() {
        return clickType == ClickType.PRIMARY;
    }
}
