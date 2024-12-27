package com.fadegor05.cubeeconomy

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class CubeEconomy : ModInitializer {

    override fun onInitialize() {
        ModItems.registerItems()
        registerMintingCommand(
            "mint_copper", "create:copper_sheet", ModItems.COPPER_COIN
        )
        registerMintingCommand(
            "mint_zinc", "create:zinc_ingot", ModItems.ZINC_COIN
        )

    }

    private fun registerMintingCommand(
        commandName: String,
        inputItemId: String,
        outputItem: Item
    ) {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .requires { source -> source.hasPermissionLevel(2) } // Только админы
                    .executes { context ->
                        val player = context.source.player
                        val inputItem = Registries.ITEM.get(Identifier(inputItemId))

                        if (player != null && mintItem(player, inputItem, outputItem)) {
                            playAnvilSound(player)
                            return@executes 1
                        }

                        context.source.player?.sendMessage(Text.of("Нужно больше $inputItemId, чтобы чеканить!"))
                        return@executes 0
                    }
            )
        }
    }

    private fun mintItem(player: ServerPlayerEntity, inputItem: Item, outputItem: Item): Boolean {
        val inventory = player.inventory
        val requiredAmount = 1

        if (inventory.count(inputItem) >= requiredAmount) {
            inventory.removeStack(inventory.getSlotWithStack(ItemStack(inputItem)), requiredAmount)

            inventory.offerOrDrop(ItemStack(outputItem))
            return true
        }
        return false
    }

    private fun playAnvilSound(player: ServerPlayerEntity) {
        val world = player.world
        val pos = player.blockPos
        val soundRadius = 16.0 // Радиус слышимости звука
        val randomPitch = 1.2f + Math.random().toFloat() * (2.0f - 0.7f)

        world.playSound(
            null,
            pos,
            SoundEvents.BLOCK_ANVIL_USE,
            SoundCategory.PLAYERS,
            1.0f,
            randomPitch
        )
    }

}
