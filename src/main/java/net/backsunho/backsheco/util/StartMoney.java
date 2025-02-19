package net.backsunho.backsheco.util;

import net.backsunho.backsheco.Backsheco;
import net.minecraft.server.network.ServerPlayerEntity;

public class StartMoney {
    public static void InitialMoney(ServerPlayerEntity player) {
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;

        // "money" 키가 존재하는지 확인하고, 없으면 1000원 지급
        if (!dataSaver.getPersistentData().contains(Backsheco.MONEY_OBJECTIVE)) {
            dataSaver.getPersistentData().putInt(Backsheco.MONEY_OBJECTIVE, Backsheco.DEFAULT_BALANCE);
        }
    }
}
