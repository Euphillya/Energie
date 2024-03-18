package fr.euphyllia.energie.model;

import org.bukkit.World;

public class MultipleRecords {
    public static final class WorldChunk {
        private final World world;
        private final int chunkX;
        private final int chunkZ;

        public WorldChunk(World world, int chunkX, int chunkZ) {
            this.world = world;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        public World world() {
            return world;
        }

        public int chunkX() {
            return chunkX;
        }

        public int chunkZ() {
            return chunkZ;
        }
    }
}
