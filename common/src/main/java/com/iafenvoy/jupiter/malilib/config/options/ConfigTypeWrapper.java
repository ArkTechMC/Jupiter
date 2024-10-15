package com.iafenvoy.jupiter.malilib.config.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.jupiter.malilib.MaLiLib;
import com.iafenvoy.jupiter.malilib.config.*;
import com.iafenvoy.jupiter.malilib.interfaces.IValueChangeCallback;
import com.iafenvoy.jupiter.malilib.util.Color4f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

public class ConfigTypeWrapper implements IConfigBoolean, IConfigDouble, IConfigInteger, IConfigOptionList, IConfigNotifiable<IConfigBase> {
    private final ConfigType wrappedType;
    private final IConfigBase wrappedConfig;

    public ConfigTypeWrapper(ConfigType wrappedType, IConfigBase wrappedConfig) {
        this.wrappedType = wrappedType;
        this.wrappedConfig = wrappedConfig;
    }

    @Override
    public boolean shouldUseSlider() {
        if (this.wrappedConfig instanceof IConfigInteger) {
            return ((IConfigInteger) this.wrappedConfig).shouldUseSlider();
        } else if (this.wrappedConfig instanceof IConfigDouble) {
            return ((IConfigDouble) this.wrappedConfig).shouldUseSlider();
        }

        return false;
    }

    @Override
    public void toggleUseSlider() {
        if (this.wrappedConfig instanceof IConfigInteger) {
            ((IConfigInteger) this.wrappedConfig).toggleUseSlider();
        } else if (this.wrappedConfig instanceof IConfigDouble) {
            ((IConfigDouble) this.wrappedConfig).toggleUseSlider();
        }
    }

    @Override
    public ConfigType getType() {
        return this.wrappedType;
    }

    @Override
    public String getNameKey() {
        return this.wrappedConfig.getNameKey();
    }

    @Override
    public String getComment() {
        return this.wrappedConfig.getComment();
    }

    @Override
    public String getPrettyName() {
        return this.wrappedConfig.getPrettyName();
    }

    @Override
    public String getConfigGuiDisplayName() {
        return this.wrappedConfig.getConfigGuiDisplayName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onValueChanged() {
        if (this.wrappedConfig instanceof IConfigNotifiable) {
            ((IConfigNotifiable<IConfigBase>) this.wrappedConfig).onValueChanged();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public IConfigBase setValueChangeCallback(IValueChangeCallback<IConfigBase> callback) {
        if (this.wrappedConfig instanceof IConfigNotifiable)
            ((IConfigNotifiable<IConfigBase>) this.wrappedConfig).setValueChangeCallback(callback);
        return this;
    }

    @Override
    public String getStringValue() {
        return switch (this.wrappedType) {
            case BOOLEAN -> String.valueOf(((IConfigBoolean) this.wrappedConfig).getBooleanValue());
            case DOUBLE -> String.valueOf(((IConfigDouble) this.wrappedConfig).getDoubleValue());
            case INTEGER -> String.valueOf(((IConfigInteger) this.wrappedConfig).getIntegerValue());
            case COLOR -> String.format("#%08X", ((IConfigInteger) this.wrappedConfig).getIntegerValue());
            case OPTION_LIST -> ((IConfigOptionList) this.wrappedConfig).getOptionListValue().getStringValue();
            default -> ((IStringRepresentable) this.wrappedConfig).getStringValue();
        };
    }

    @Override
    public String getDefaultStringValue() {
        return switch (this.wrappedType) {
            case BOOLEAN -> String.valueOf(((IConfigBoolean) this.wrappedConfig).getDefaultBooleanValue());
            case DOUBLE -> String.valueOf(((IConfigDouble) this.wrappedConfig).getDefaultDoubleValue());
            case INTEGER -> String.valueOf(((IConfigInteger) this.wrappedConfig).getDefaultIntegerValue());
            case COLOR -> String.format("#%08X", ((IConfigInteger) this.wrappedConfig).getDefaultIntegerValue());
            case OPTION_LIST -> ((IConfigOptionList) this.wrappedConfig).getDefaultOptionListValue().getStringValue();
            default -> ((IStringRepresentable) this.wrappedConfig).getDefaultStringValue();
        };
    }

    @Override
    public void setValueFromString(String value) {
        try {
            switch (this.wrappedType) {
                case BOOLEAN:
                    ((IConfigBoolean) this.wrappedConfig).setBooleanValue(Boolean.parseBoolean(value));
                    break;
                case DOUBLE:
                    ((IConfigDouble) this.wrappedConfig).setDoubleValue(Double.parseDouble(value));
                    break;
                case INTEGER:
                    ((IConfigInteger) this.wrappedConfig).setIntegerValue(Integer.parseInt(value));
                    break;
                case STRING:
                    ((IStringRepresentable) this.wrappedConfig).setValueFromString(value);
                    break;
                case COLOR:
                    ((IConfigInteger) this.wrappedConfig).setValueFromString(value);
                    break;
                case OPTION_LIST:
                    IConfigOptionList option = (IConfigOptionList) this.wrappedConfig;
                    option.setOptionListValue(option.getOptionListValue().fromString(value));
                    break;
                default:
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set the config value for '{}' from string '{}'", this.getNameKey(), value, e);
        }
    }

    @Override
    public boolean isModified() {
        return switch (this.wrappedType) {
            case BOOLEAN -> {
                IConfigBoolean config = (IConfigBoolean) this.wrappedConfig;
                yield config.getBooleanValue() != config.getDefaultBooleanValue();
            }
            case DOUBLE -> {
                IConfigDouble config = (IConfigDouble) this.wrappedConfig;
                yield config.getDoubleValue() != config.getDefaultDoubleValue();
            }
            case INTEGER, COLOR -> {
                IConfigInteger config = (IConfigInteger) this.wrappedConfig;
                yield config.getIntegerValue() != config.getDefaultIntegerValue();
            }
            case OPTION_LIST -> {
                IConfigOptionList config = (IConfigOptionList) this.wrappedConfig;
                yield config.getOptionListValue() != config.getDefaultOptionListValue();
            }
            case STRING -> {
                IStringRepresentable config = (IStringRepresentable) this.wrappedConfig;
                yield !config.getStringValue().equals(config.getDefaultStringValue());
            }
            default -> false;
        };
    }

    @Override
    public boolean isModified(String newValue) {
        return switch (this.wrappedType) {
            case BOOLEAN -> !String.valueOf(((IConfigBoolean) this.wrappedConfig).getBooleanValue()).equals(newValue);
            case DOUBLE -> !String.valueOf(((IConfigDouble) this.wrappedConfig).getDoubleValue()).equals(newValue);
            case INTEGER -> !String.valueOf(((IConfigInteger) this.wrappedConfig).getIntegerValue()).equals(newValue);
            case COLOR -> !((ConfigColor) this.wrappedConfig).getStringValue().equals(newValue);
            case OPTION_LIST ->
                    !((IConfigOptionList) this.wrappedConfig).getOptionListValue().getStringValue().equals(newValue);
            default -> !((IStringRepresentable) this.wrappedConfig).getStringValue().equals(newValue);
        };
    }

    @Override
    public void resetToDefault() {
        try {
            switch (this.wrappedType) {
                case BOOLEAN: {
                    IConfigBoolean config = (IConfigBoolean) this.wrappedConfig;
                    config.setBooleanValue(config.getDefaultBooleanValue());
                    break;
                }
                case DOUBLE: {
                    IConfigDouble config = (IConfigDouble) this.wrappedConfig;
                    config.setDoubleValue(config.getDefaultDoubleValue());
                    break;
                }
                case INTEGER:
                case COLOR: {
                    IConfigInteger config = (IConfigInteger) this.wrappedConfig;
                    config.setIntegerValue(config.getDefaultIntegerValue());
                    break;
                }
                case OPTION_LIST: {
                    IConfigOptionList config = (IConfigOptionList) this.wrappedConfig;
                    config.setOptionListValue(config.getDefaultOptionListValue());
                    break;
                }
                case STRING:
                default: {
                    IStringRepresentable config = (IStringRepresentable) this.wrappedConfig;
                    config.setValueFromString(config.getDefaultStringValue());
                    break;
                }
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to reset config value for {}", this.getNameKey(), e);
        }
    }

    @Override
    public boolean getBooleanValue() {
        return this.wrappedType == ConfigType.BOOLEAN && ((IConfigBoolean) this.wrappedConfig).getBooleanValue();
    }

    @Override
    public void setBooleanValue(boolean value) {
        if (this.wrappedType == ConfigType.BOOLEAN) {
            ((IConfigBoolean) this.wrappedConfig).setBooleanValue(value);
        }
    }

    @Override
    public boolean getDefaultBooleanValue() {
        return this.wrappedType == ConfigType.BOOLEAN && ((IConfigBoolean) this.wrappedConfig).getDefaultBooleanValue();
    }

    @Override
    public int getIntegerValue() {
        return this.wrappedType == ConfigType.INTEGER ? ((IConfigInteger) this.wrappedConfig).getIntegerValue() : 0;
    }

    @Override
    public void setIntegerValue(int value) {
        if (this.wrappedType == ConfigType.INTEGER) {
            ((IConfigInteger) this.wrappedConfig).setIntegerValue(value);
        }
    }

    @Override
    public int getDefaultIntegerValue() {
        return this.wrappedType == ConfigType.INTEGER ? ((IConfigInteger) this.wrappedConfig).getDefaultIntegerValue() : 0;
    }

    @Override
    public int getMinIntegerValue() {
        return this.wrappedType == ConfigType.INTEGER ? ((IConfigInteger) this.wrappedConfig).getMinIntegerValue() : 0;
    }

    @Override
    public int getMaxIntegerValue() {
        return this.wrappedType == ConfigType.INTEGER ? ((IConfigInteger) this.wrappedConfig).getMaxIntegerValue() : 0;
    }

    @Override
    public double getDoubleValue() {
        return this.wrappedType == ConfigType.DOUBLE ? ((IConfigDouble) this.wrappedConfig).getDoubleValue() : 0;
    }

    @Override
    public float getFloatValue() {
        return (float) this.getDoubleValue();
    }

    @Override
    public void setDoubleValue(double value) {
        if (this.wrappedType == ConfigType.DOUBLE) {
            ((IConfigDouble) this.wrappedConfig).setDoubleValue(value);
        }
    }

    @Override
    public double getDefaultDoubleValue() {
        return this.wrappedType == ConfigType.DOUBLE ? ((IConfigDouble) this.wrappedConfig).getDefaultDoubleValue() : 0;
    }

    @Override
    public double getMinDoubleValue() {
        return this.wrappedType == ConfigType.DOUBLE ? ((IConfigDouble) this.wrappedConfig).getMinDoubleValue() : 0;
    }

    @Override
    public double getMaxDoubleValue() {
        return this.wrappedType == ConfigType.DOUBLE ? ((IConfigDouble) this.wrappedConfig).getMaxDoubleValue() : 0;
    }

    @Override
    public IConfigOptionListEntry getOptionListValue() {
        return this.wrappedType == ConfigType.OPTION_LIST ? ((IConfigOptionList) this.wrappedConfig).getOptionListValue() : null;
    }

    @Override
    public void setOptionListValue(IConfigOptionListEntry value) {
        if (this.wrappedType == ConfigType.OPTION_LIST) {
            ((IConfigOptionList) this.wrappedConfig).setOptionListValue(value);
        }
    }

    @Override
    public IConfigOptionListEntry getDefaultOptionListValue() {
        return this.wrappedType == ConfigType.OPTION_LIST ? ((IConfigOptionList) this.wrappedConfig).getDefaultOptionListValue() : null;
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            switch (this.wrappedType) {
                case BOOLEAN:
                    ((IConfigBoolean) this.wrappedConfig).setBooleanValue(element.getAsBoolean());
                    break;
                case DOUBLE:
                    ((IConfigDouble) this.wrappedConfig).setDoubleValue(element.getAsDouble());
                    break;
                case INTEGER:
                    ((IConfigInteger) this.wrappedConfig).setIntegerValue(element.getAsInt());
                    break;
                case STRING:
                    ((IConfigValue) this.wrappedConfig).setValueFromString(element.getAsString());
                    break;
                case COLOR:
                    ((IConfigInteger) this.wrappedConfig).setValueFromString(element.getAsString());
                    break;
                case OPTION_LIST:
                    IConfigOptionList option = (IConfigOptionList) this.wrappedConfig;
                    option.setOptionListValue(option.getOptionListValue().fromString(element.getAsString()));
                    break;
                default:
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to read config value for {} from the JSON config", this.getNameKey(), e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        return switch (this.wrappedType) {
            case BOOLEAN -> new JsonPrimitive(((IConfigBoolean) this.wrappedConfig).getBooleanValue());
            case DOUBLE -> new JsonPrimitive(((IConfigDouble) this.wrappedConfig).getDoubleValue());
            case INTEGER -> new JsonPrimitive(((IConfigInteger) this.wrappedConfig).getIntegerValue());
            case STRING -> new JsonPrimitive(((IConfigValue) this.wrappedConfig).getStringValue());
            case COLOR -> new JsonPrimitive(((IConfigInteger) this.wrappedConfig).getStringValue());
            case OPTION_LIST ->
                    new JsonPrimitive(((IConfigOptionList) this.wrappedConfig).getOptionListValue().getStringValue());
            default -> new JsonPrimitive(this.getStringValue());
        };
    }

    @Override
    public IConfigBase copy() {
        return new ConfigTypeWrapper(this.wrappedType, this.wrappedConfig);
    }

    @Override
    public Codec<?> getCodec() {
        return switch (this.wrappedType) {
            case BOOLEAN -> Codec.BOOL.orElse(((IConfigBoolean) this.wrappedConfig).getDefaultBooleanValue());
            case DOUBLE -> Codec.DOUBLE.orElse(((IConfigDouble) this.wrappedConfig).getDefaultDoubleValue());
            case INTEGER -> Codec.INT.orElse(((IConfigInteger) this.wrappedConfig).getDefaultIntegerValue());
            case STRING -> Codec.STRING.orElse(((IConfigValue) this.wrappedConfig).getDefaultStringValue());
            case COLOR -> Color4f.CODEC.orElse(Color4f.fromColor(((IConfigInteger) this.wrappedConfig).getDefaultIntegerValue()));
            case OPTION_LIST -> new PrimitiveCodec<IConfigOptionListEntry>() {
                @Override
                public <T> DataResult<IConfigOptionListEntry> read(DynamicOps<T> ops, T input) {
                    return ops.getStringValue(input).result().map(x -> DataResult.success(((IConfigOptionList) wrappedConfig).getOptionListValue().fromString(x))).orElse(DataResult.error(() -> "Cannot parse to IConfigOptionListEntry"));
                }

                @Override
                public <T> T write(DynamicOps<T> ops, IConfigOptionListEntry value) {
                    return ops.createString(value.getStringValue());
                }
            }.orElse(((IConfigOptionList) this.wrappedConfig).getDefaultOptionListValue());
            default -> Codec.EMPTY.codec();
        };
    }
}
