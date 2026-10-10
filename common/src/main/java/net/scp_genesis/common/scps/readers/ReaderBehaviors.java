package net.scp_genesis.common.scps.readers;
/** Shared immutable strategies, without registering additional reader blocks or items. */
public final class ReaderBehaviors {
    private static final ReaderBehavior[] TYPES = {new IdReader(),new CodeReader(),new ButtonReader(),new KeycardReader(),new LockedReader()};
    private ReaderBehaviors() {}
    public static ReaderBehavior get(ReaderType type) { return TYPES[type.ordinal()]; }
}
