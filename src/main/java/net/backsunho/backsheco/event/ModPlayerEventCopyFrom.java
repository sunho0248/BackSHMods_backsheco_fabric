package net.backsunho.backsheco.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.backsunho.backsheco.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModPlayerEventCopyFrom implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IEntityDataSaver original = ((IEntityDataSaver) oldPlayer);
        IEntityDataSaver player = ((IEntityDataSaver) newPlayer);

        player.getPersistentData().putIntArray("homepos", original.getPersistentData().getIntArray("homepos"));
        player.getPersistentData().putInt("money", original.getPersistentData().getInt("money"));
    }
}
