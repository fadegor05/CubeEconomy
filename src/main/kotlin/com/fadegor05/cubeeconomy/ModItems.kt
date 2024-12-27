package com.fadegor05.cubeeconomy

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {

    val ZINC_COIN = registerItem("zinc_coin", Item(FabricItemSettings()))
    val COPPER_COIN = registerItem("copper_coin", Item(FabricItemSettings()))

    fun addItemsToIngredientTabItemGroup(entries: FabricItemGroupEntries) {
        entries.add(ZINC_COIN)
        entries.add(COPPER_COIN)
    }

    fun registerItem(name: String, item: Item): Item {
        return Registry.register(Registries.ITEM, Identifier("cubeeconomy", name), item)
    }

    fun registerItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup)
    }
}