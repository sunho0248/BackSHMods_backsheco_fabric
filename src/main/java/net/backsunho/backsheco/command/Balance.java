package net.backsunho.backsheco.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.backsunho.backsheco.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Balance {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("잔액").executes(Balance::run));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;
        int currentMoney = dataSaver.getPersistentData().getInt("money"); // 현재 보유 금액
        String formattedcurrentMoney = String.format("%,d", currentMoney);

        player.sendMessage(Text.literal("현재 잔액: ")
            .append(Text.literal(formattedcurrentMoney).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
            .append("원")
            .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        return 1;


    }
}
