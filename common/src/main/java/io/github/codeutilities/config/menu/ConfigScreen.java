package io.github.codeutilities.config.menu;

import io.github.codeutilities.config.internal.ITranslatable;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.structure.ConfigManager;
import io.github.codeutilities.config.structure.ConfigSetting;
import io.github.codeutilities.config.structure.ConfigSubGroup;
import io.github.codeutilities.config.types.*;
import io.github.codeutilities.config.types.list.ListSetting;
import io.github.codeutilities.config.types.list.StringListSetting;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import org.w3c.dom.Text;

import java.util.List;

public class ConfigScreen implements ITranslatable {
    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private static final String PREFIX = "config.codeutilities.";
    // Default values
    private static final String CATEGORY_TEXT = PREFIX + "category.";
    private static final String SUB_CATEGORY_TEXT = PREFIX + "subcategory.";
    private static final String KEY_TEXT = PREFIX + "option.";
    private static final String TOOLTIP_TEXT = ".tooltip";

    public static Screen getScreen(Screen parent) {

        // Builder
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(ITranslatable.get(PREFIX + "title"));

        // Entry builder
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        List<ConfigGroup> groups = CONFIG.getRegistered();
        // Optimized loop
        for (ConfigGroup group : groups) {
                // Category
            String groupName = group.getName();

            // Group translation
            TextComponent groupTranslation;
            if (group.getRawKey().isPresent()) {
                groupTranslation = group.getRawKey().get();
            } else {
                groupTranslation = ITranslatable.get(CATEGORY_TEXT + groupName);
            }
            ConfigCategory category = builder.getOrCreateCategory(groupTranslation);

            // These are group settings (the non sub-grouped ones)
            List<ConfigSetting<?>> groupSettings = group.getSettings();
            // Optimized loop
            for (ConfigSetting<?> groupSetting : groupSettings) {
                String settingKey = groupSetting.getCustomKey();

                // Get custom translations or standard ones
                TextComponent keyTranslation;
                if (groupSetting.getRawKey().isPresent()) {
                    keyTranslation = groupSetting.getRawKey().get();
                } else {
                    keyTranslation = ITranslatable.get(KEY_TEXT + settingKey);
                }

                TextComponent tooltipTranslation;
                if (groupSetting.getRawTooltip().isPresent()) {
                    tooltipTranslation = groupSetting.getRawTooltip().get();
                } else {
                    tooltipTranslation = ITranslatable.get(KEY_TEXT + settingKey + TOOLTIP_TEXT);
                }
                category.addEntry(
                    getEntry(entryBuilder, groupSetting, keyTranslation, tooltipTranslation));
            }

            List<ConfigSubGroup> subGroups = group.getRegistered();
            // Optimized loop
            for (ConfigSubGroup subGroup : subGroups) {

                // Sub Category
                String subGroupName = subGroup.getName();

                TextComponent groupKey;
                if (subGroup.getRawKey().isPresent()) {
                    groupKey = subGroup.getRawKey().get();
                } else {
                    groupKey = ITranslatable
                        .get(SUB_CATEGORY_TEXT + groupName + "_" + subGroupName);
                }

                TextComponent groupTooltip;
                if (subGroup.getRawTooltip().isPresent()) {
                    groupTooltip = subGroup.getRawTooltip().get();
                } else {
                    groupTooltip = ITranslatable
                        .get(SUB_CATEGORY_TEXT + groupName + "_" + subGroupName + TOOLTIP_TEXT);
                }

                SubCategoryBuilder subBuilder = entryBuilder.startSubCategory(groupKey)
                    .setExpanded(subGroup.isStartExpanded())
                    .setTooltip(groupTooltip);

                for (ConfigSetting<?> configSetting : subGroup.getRegistered()) {
                    String settingKey = configSetting.getCustomKey();

                    TextComponent keyTranslation;
                    if (configSetting.getRawKey().isPresent()) {
                        keyTranslation = configSetting.getRawKey().get();
                    } else {
                        keyTranslation = ITranslatable.get(KEY_TEXT + settingKey);
                    }

                    TextComponent tooltipTranslation;
                    if (configSetting.getRawTooltip().isPresent()) {
                        tooltipTranslation = configSetting.getRawTooltip().get();
                    } else {
                        tooltipTranslation = ITranslatable
                            .get(KEY_TEXT + settingKey + TOOLTIP_TEXT);
                    }

                    subBuilder.add(
                        getEntry(entryBuilder, configSetting, keyTranslation, tooltipTranslation));
                }

                // Finally add the sub group
                category.addEntry(subBuilder.build());
            }
        }
        return builder.build();
    }

    private static AbstractConfigListEntry<?> getEntry(ConfigEntryBuilder builder, ConfigSetting<?> configSetting, TextComponent title, TextComponent tooltip) {

        if (configSetting.isList()) {
            ListSetting<?> setting = configSetting.cast();

            if (setting.isString()) {
                StringListSetting list = setting.cast();

                return builder.startStringDropdownMenu(title, list.getSelected())
                        .setSelections(list.getValue())
                        .setDefaultValue(list.getSelected())
                        .setTooltip(tooltip)
                        .setSaveConsumer(list::setSelected)
                        .build();
            }
        }
        if (configSetting.isBoolean()) {
            BooleanSetting setting = configSetting.cast();
            return builder.startBooleanToggle(title, setting.getValue())
                    .setDefaultValue(setting.getDefaultValue())
                    .setTooltip(tooltip)
                    .setSaveConsumer(setting::setValue)
                    .build();
        }
        if (configSetting.isString()) {
            StringSetting setting = configSetting.cast();
            return builder.startStrField(title, setting.getValue())
                    .setDefaultValue(setting.getDefaultValue())
                    .setTooltip(tooltip)
                    .setSaveConsumer(setting::setValue)
                    .build();
        }
        if (configSetting.isInteger()) {
            IntegerSetting setting = configSetting.cast();
            return builder.startIntField(title, setting.getValue())
                    .setDefaultValue(setting.getDefaultValue())
                    .setTooltip(tooltip)
                    .setSaveConsumer(setting::setValue)
                    .build();
        }
        if (configSetting.isLong()) {
            LongSetting setting = configSetting.cast();
            return builder.startLongField(title, setting.getValue())
                    .setDefaultValue(setting.getDefaultValue())
                    .setTooltip(tooltip)
                    .setSaveConsumer(setting::setValue)
                    .build();
        }
        if (configSetting.isDouble()) {
            DoubleSetting setting = configSetting.cast();
            return builder.startDoubleField(title, setting.getValue())
                    .setDefaultValue(setting.getDefaultValue())
                    .setTooltip(tooltip)
                    .setSaveConsumer(setting::setValue)
                    .build();
        }
        if (configSetting.isFloat()) {
            FloatSetting setting = configSetting.cast();
            return builder.startFloatField(title, setting.getValue())
                    .setDefaultValue(setting.getDefaultValue())
                    .setTooltip(tooltip)
                    .setSaveConsumer(setting::setValue)
                    .build();
        }
        if (configSetting.isText()) {
            return builder.startTextDescription(title)
                    .build();
        }
        if (configSetting.isEnum()) {
            EnumSetting<?> setting = configSetting.cast();

            return setupEnumSelector(builder,title,setting)
                    .setTooltip(tooltip)
                    .build();
        }

        return null;
    }

    private static <E extends Enum<E> & IConfigEnum> EnumSelectorBuilder<E> setupEnumSelector(ConfigEntryBuilder builder, TextComponent title, EnumSetting<E> enumList) {
        return builder
                .startEnumSelector(title, enumList.getEnumClass(), enumList.getValue())
                .setEnumNameProvider(ConfigScreen::getEnumName)
                .setDefaultValue(enumList.getValue())
                .setSaveConsumer(enumList::setValue);
    }

    private static TextComponent getEnumName(Enum<?> anEnum) {
        if (!(anEnum instanceof IConfigEnum)) {
            throw new IllegalStateException("Enum must implement IConfigEnum");
        }

        String key = "." + anEnum.toString().toLowerCase();
        return ITranslatable.get(KEY_TEXT + ((IConfigEnum) anEnum).getKey() + key);
    }
}
