package fi.dy.masa.malilib.config;

import com.google.common.collect.ImmutableList;

import java.util.List;

public interface IConfigStringList extends IConfigBase {
    List<String> getStrings();

    void setStrings(List<String> strings);

    ImmutableList<String> getDefaultStrings();
}
