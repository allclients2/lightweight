package allclients2.lightweight.eventbus.events;

import net.minecraft.util.math.ChunkPos;

public class ChunkUnloadEvent {
    public ChunkPos chunkPos;

    public ChunkUnloadEvent(ChunkPos chunkPos) {
        this.chunkPos = chunkPos;
    }
}
