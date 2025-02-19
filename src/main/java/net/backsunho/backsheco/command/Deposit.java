package net.backsunho.backsheco.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.backsunho.backsheco.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class Deposit {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("입금")
                .requires(source -> source.hasPermissionLevel(2)) // OP 권한 필요
                .then(CommandManager.argument("player", EntityArgumentType.entities())
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(Deposit::run))));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player"); // 대상 플레이어
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;

        int amount = IntegerArgumentType.getInteger(context, "amount"); // 입금할 금액
        int currentMoney = dataSaver.getPersistentData().getInt("money"); // 현재 보유 금액
        long tempMoney = (long) currentMoney + amount;
        int finalMoney = (tempMoney > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) tempMoney;

        dataSaver.getPersistentData().putInt("money", finalMoney); // 돈 출금


        String formattedAmount = String.format("%,d", amount);
        String formattedTotalAmount = String.format("%,d", finalMoney);

        context.getSource().sendMessage(Text.literal("[")
                .append(Text.literal(player.getName().getString()).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                .append("]님에게 ")
                .append(Text.literal(formattedAmount).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                .append("원을 입금했습니다. 현재 잔액: ")
                .append(Text.literal(formattedTotalAmount).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
                .append("원")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        player.sendMessage(Text.literal("당신의 계좌에 ")
                .append(Text.literal(formattedAmount).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                .append("원이 입금되었습니다. 현재 잔액: ")
                .append(Text.literal(formattedTotalAmount).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
                .append("원")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        return 1;
    }

}
