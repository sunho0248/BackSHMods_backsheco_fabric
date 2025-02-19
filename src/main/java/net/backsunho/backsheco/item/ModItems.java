package net.backsunho.backsheco.item;

import net.backsunho.backsheco.Backsheco;
import net.backsunho.backsheco.item.custom.MoneyItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

//    public static final Item PINK_GARNET = registerItems("pink_garnet", new Item(new Item.Settings()));
//    public static final Item RAW_PINK_GARNET = registerItems("raw_pink_garnet", new Item(new Item.Settings()));
    public static final Item MONEY = registerItems("money", new MoneyItem(new Item.Settings()));


    private static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Backsheco.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Backsheco.LOGGER.info("Registering Mod Items for " + Backsheco.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
//            entries.add(PINK_GARNET);
//            entries.add(RAW_PINK_GARNET);
        });

    }
}
