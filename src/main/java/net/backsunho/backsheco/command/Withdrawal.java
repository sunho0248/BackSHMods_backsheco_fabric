package net.backsunho.backsheco.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.backsunho.backsheco.item.ModItems;
import net.backsunho.backsheco.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.util.Rarity;

public class Withdrawal {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("출금")
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(Withdrawal::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;
        int amount = IntegerArgumentType.getInteger(context, "amount"); // 출금할 금액
        int currentMoney = dataSaver.getPersistentData().getInt("money"); // 현재 보유 금액

        if (currentMoney < amount) { // 출금하려는 금액이 현재 잔액보다 큰 경우

            String formattedcurrentMoney = String.format("%,d", currentMoney);

            player.sendMessage(Text.literal("잔액이 부족합니다. 현재 잔액: ")
                    .append(Text.literal(formattedcurrentMoney).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
                    .append("원")
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            return 0;
        }

        //출금 처리

        dataSaver.getPersistentData().putInt("money", currentMoney - amount); // 돈 출금
        String formattedAmount = String.format("%,d", amount); // 출금액
        String formattedcCurrentMoney = String.format("%,d", currentMoney - amount); // 출금 후 잔액

        // 종이 아이템 생성
        ItemStack itemStack = new ItemStack(ModItems.MONEY, 1);

        NbtCompound nbt = new NbtCompound();
        nbt.putInt("backsheco", amount);

        ///give @s minecraft:paper[minecraft:custom_name='{"text":"$1000"}', minecraft:custom_data={backsheco:1000},rarity="epic", enchantment_glint_override=true]

        ComponentMap.Builder components = ComponentMap.builder();
        components.add(DataComponentTypes.CUSTOM_NAME, Text.literal(formattedAmount + " 원"));
        components.add(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        components.add(DataComponentTypes.RARITY, Rarity.EPIC);
        components.add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        // 아이템에 적용
        itemStack.applyComponentsFrom(components.build());

        ItemStack itemStackToGive = itemStack.copy();

        // 아이템을 인벤토리에 넣거나, 바닥에 떨어뜨리기
        boolean inserted = player.getInventory().insertStack(itemStackToGive);
        if (inserted && itemStackToGive.isEmpty()) {
            ItemEntity itemEntity = player.dropItem(itemStack, false);
            if (itemEntity != null) {
                itemEntity.setDespawnImmediately();
            }

            player.getWorld().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ENTITY_ITEM_PICKUP,
                    SoundCategory.PLAYERS,
                    0.2F,
                    ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
            );
            player.currentScreenHandler.sendContentUpdates();
        } else {
            ItemEntity itemEntity = player.dropItem(itemStackToGive, false);
            if (itemEntity != null) {
                itemEntity.resetPickupDelay();
                itemEntity.setOwner(player.getUuid());
            }
        }





        player.sendMessage(Text.literal(formattedAmount).setStyle(Style.EMPTY.withColor(Formatting.WHITE))
                .append(Text.literal(" 원을 출금하였습니다. 현재 잔액: ")
                .append(Text.literal(formattedcCurrentMoney).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
                .append("원")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY))));

        return 1;
    }
}
