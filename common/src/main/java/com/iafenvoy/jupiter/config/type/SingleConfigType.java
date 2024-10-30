package com.iafenvoy.jupiter.config.type;

import java.util.List;

public class SingleConfigType<T> extends ConfigType<T> {
    public SingleConfigType() {
        super(EntryType.SINGLE);
    }
}
