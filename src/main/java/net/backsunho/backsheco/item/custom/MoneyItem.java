package net.backsunho.backsheco.item.custom;

import net.backsunho.backsheco.util.IEntityDataSaver;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class MoneyItem extends Item {

    public MoneyItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
            ComponentMap customData = itemStack.getComponents();
            if (itemStack.contains(DataComponentTypes.CUSTOM_DATA)){

                // CUSTOM_DATA에서 NBT 데이터 가져오기
                NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
                NbtCompound nbt = nbtComponent.copyNbt();


                if (nbt.contains("backsheco")) {
                    int amount = nbt.getInt("backsheco");

                    // 플레이어 데이터에 금액 추가
                    IEntityDataSaver dataSaver = (IEntityDataSaver) serverPlayer;
                    int currentMoney = dataSaver.getPersistentData().getInt("money");
                    long tempMoney = (long) currentMoney + amount;
                    int finalMoney = (tempMoney > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) tempMoney;

                    dataSaver.getPersistentData().putInt("money", finalMoney); // 돈 추가

                    // 메시지 출력
                    String formattedAmount = String.format("%,d", amount);
                    String formattedTotalAmount = String.format("%,d", finalMoney);

                    serverPlayer.sendMessage(Text.literal("💰 ")
                            .append(Text.literal(formattedAmount).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                            .append("원 입금완료! 현재 잔액: ")
                            .append(Text.literal(formattedTotalAmount).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
                            .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
                }

            }
        }
        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
