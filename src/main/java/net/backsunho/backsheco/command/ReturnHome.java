package net.backsunho.backsheco.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.backsunho.backsheco.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class ReturnHome {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("home")
                .then(CommandManager.literal("return").executes(ReturnHome::run)));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();

        // not 0 means it contains SOMETHING
        int[] homepos = player.getPersistentData().getIntArray("homepos");

        if(homepos.length != 0) {
            ServerCommandSource source = context.getSource();
            ServerWorld overworld = source.getServer().getWorld(World.OVERWORLD); // 오버월드 가져오기
            ServerWorld netherworld = source.getServer().getWorld(World.NETHER); // 네더 가져오기
            ServerWorld endworld = source.getServer().getWorld(World.END); // 엔드 가져오기

            int[] playerPos = player.getPersistentData().getIntArray("homepos");
            //context.getSource().getPlayer().requestTeleport(playerPos[0], playerPos[1], playerPos[2]);
            source.getPlayer().teleport(overworld, homepos[0], homepos[1], homepos[2], source.getPlayer().getYaw(), source.getPlayer().getPitch());
            context.getSource().sendMessage(Text.literal("Player returned Home!"));
            return 1;
        }
        else {
            context.getSource().sendMessage(Text.literal("No Home Position has been Set!"));
            return -1;
        }
    }
}