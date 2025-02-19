package net.backsunho.backsheco.util;

import net.backsunho.backsheco.command.*;
import net.backsunho.backsheco.event.ModPlayerEventCopyFrom;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class ModRegistries {
    public static void registerModStuffs() {
        registerCommands();
        registerEvents();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(Deposit::register);
        CommandRegistrationCallback.EVENT.register(Withdrawal::register);
        CommandRegistrationCallback.EVENT.register(Balance::register);
        CommandRegistrationCallback.EVENT.register(BalanceSet::register);
        CommandRegistrationCallback.EVENT.register(SetHome::register);
        CommandRegistrationCallback.EVENT.register(ReturnHome::register);
    }

    private static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new ModPlayerEventCopyFrom());
    }
}
