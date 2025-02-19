package net.backsunho.backsheco.item;

import net.backsunho.backsheco.Backsheco;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup PINK_GARNET_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Backsheco.MOD_ID, "pink_garnet_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.MONEY))
                    .displayName(Text.translatable("itemgroup.backsheco.pink_garnet_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.MONEY);

//                        entries.add(ModItems.PINK_GARNET);
//                        entries.add(ModItems.RAW_PINK_GARNET);
                    })
                    .build());


    public static void registerItemGroups(){
        Backsheco.LOGGER.info("Registering Item Groups for " + Backsheco.MOD_ID);
    }
}
