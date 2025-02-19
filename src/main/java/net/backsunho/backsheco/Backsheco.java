package net.backsunho.backsheco;

import net.backsunho.backsheco.item.ModItemGroups;
import net.backsunho.backsheco.item.ModItems;
import net.backsunho.backsheco.util.ModRegistries;
import net.backsunho.backsheco.util.StartMoney;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Backsheco implements ModInitializer {
	public static final String MOD_ID = "backsheco";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int DEFAULT_BALANCE = 1000; // 기본 지급 금액
	public static final String MONEY_OBJECTIVE = "money"; // 돈 데이터 키
	public static final float COMMISION = 0.05F;




	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();

		ModRegistries.registerModStuffs();
		// 플레이어가 처음 접속할 때 초기 자금을 설정하는 이벤트 등록
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			StartMoney.InitialMoney(player);
		});

	}

}