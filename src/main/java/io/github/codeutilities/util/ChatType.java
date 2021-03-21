package io.github.codeutilities.util;

public enum ChatType {
    SUCCESS("§b»", '7'),
    FAIL("§b»", 'c'),
    INFO_YELLOW("§b»", 'e'),
    INFO_BLUE("§b»", 'b');

    private final String prefix;
    private final char trailing;

    ChatType(String prefix, char trailing) {
        this.prefix = prefix;
        this.trailing = trailing;
    }

    public String getString() {
        return this.prefix;
    }

    public char getTrailing() {
        return trailing;
    }
}
