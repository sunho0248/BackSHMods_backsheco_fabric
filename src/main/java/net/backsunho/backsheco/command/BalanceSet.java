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
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BalanceSet {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("잔액설정")
                .requires(source -> source.hasPermissionLevel(2)) // OP 권한 필요
                .then(CommandManager.argument("player", EntityArgumentType.entities())
                    .then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(BalanceSet::run))));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player"); // 대상 플레이어
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;
        long tempMoney = IntegerArgumentType.getInteger(context, "amount"); // 설정할 금액
        int amount = (tempMoney > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) tempMoney;

        //잔액 설정

        dataSaver.getPersistentData().putInt("money", amount); // 잔액 설정
        String formattedAmount = String.format("%,d", amount); // 잔액

        context.getSource().sendMessage(Text.literal("[")
                .append(Text.literal(player.getName().getString()).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                .append("]님의 잔액을 ")
                .append(Text.literal(formattedAmount).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
                .append(" 원으로 설정하였습니다.")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        player.sendMessage(Text.literal("당신의 잔액이 ")
                .append(Text.literal(formattedAmount).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
                .append(" 원으로 설정되었습니다.")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        return 1;
    }
}
