package net.scp_genesis.common.scps.readers;

/** Persistent configuration snapshot plus an absolute opening deadline. */

public record ReaderDirectoryEntry(ReaderConfiguration configuration, long openUntil) {}
