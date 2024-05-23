package cloud.melion.extras.magicstuff

import cloud.melion.extras.magicstuff.listener.CustomBlockListener
import cloud.melion.extras.magicstuff.utils.FileConfig
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileFilter

class Magicstuff : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: Magicstuff
    }

    init {
        INSTANCE = this
    }


    override fun onEnable() {
        convertStones()
        convertBranches()
        registerListener()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }



    private fun registerListener() {
       Bukkit.getPluginManager().registerEvents(CustomBlockListener(), this)
    }

}



fun convertIcons() {
    val iconFolder = File("F:\\GameServers\\spigot\\itemsadder\\plugins\\ItemsAdder\\data\\resource_pack\\assets\\melion\\textures\\icons")

    val icons = mutableListOf<String>()

    iconFolder.listFiles { pathname -> pathname.extension == "png" }.forEach {
        icons.add(it.nameWithoutExtension)
    }

    val iconConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\melion\\icons.yml")
    iconConfig["info.namespace"] = "melion"
    icons.forEach {
        iconConfig["items.$it.resource.material"] = "PAPER"
        iconConfig["items.$it.resource.generate"] = true
        iconConfig["items.$it.resource.textures"] = listOf("icons/$it.png")
        //iconConfig["items.$it.resource.model_path"] = "icons/$it"
    }
    iconConfig.saveConfig()

    val categoryConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\melion\\categories.yml")
    val convertedIcons = icons.map { "melion:$it" }
    categoryConfig["categories.icons.enabled"] = true
    categoryConfig["categories.icons.icon"] = convertedIcons.first()
    categoryConfig["categories.icons.name"] = "Icons"
    categoryConfig["categories.icons.permission"] = "ia.menu.melion"
    categoryConfig["categories.icons.items"] = convertedIcons
    categoryConfig.saveConfig()
}

fun convertStones() {

    val blocks = listOf(
        "cobblestone", "mossy_cobblestone", "stone_bricks", "sand", "bricks", "andesite", "granite", "diorite", "polished_andesite",
        "polished_diorite", "polished_andesite", "coarse_dirt", "blackstone"
    )

    val stoneFolder = File("F:\\GameServers\\spigot\\itemsadder\\plugins\\ItemsAdder\\data\\resource_pack\\assets\\pathwayaddons\\models\\stones")
    var models = mutableListOf<String>()

    val solid = mutableListOf(false, false, false, true, false)


    val bases = stoneFolder.listFiles { dir, name -> name.startsWith("stone") && !name.startsWith("stone_") }

    (1..bases.size).forEach {
        models.add("stone$it")
    }

    for(block in blocks) {
        for (stone in bases) {
            val number = stone.nameWithoutExtension.replace("stone", "").toInt()
            val file = File(stoneFolder, "$block$number.json")
            file.writeText("{\"credit\":\"© 2021 Melion.cloud\",\"parent\":\"pathwayaddons:stones/stone$number\",\"textures\":{\"particle\":\"block/$block\",\"texture\":\"block/$block\"}}")
            models.add("$block$number")
        }
    }


    val stoneConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\pathwayaddons\\stones.yml")
    stoneConfig["info.namespace"] = "pathwayaddons"
    models.forEach {
        stoneConfig["items.$it.resource.material"] = "STONE"
        stoneConfig["items.$it.resource.generate"] = false
        stoneConfig["items.$it.resource.model_path"] = "stones/$it"
        stoneConfig["items.$it.behaviours.furniture.small_hitbox"] = true
        stoneConfig["items.$it.behaviours.furniture.solid"] = solid[it.last().toString().toInt()-1]
    }
    stoneConfig.saveConfig()

    models = models.map { "pathwayaddons:$it" }.toMutableList()
    val categoryConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\pathwayaddons\\categories.yml")
    categoryConfig["categories.stones.enabled"] = true
    categoryConfig["categories.stones.icon"] = models.first()
    categoryConfig["categories.stones.name"] = "§x§a§a§a§6§9§dStones §8[§x§5§5§e§f§c§4Pathway Addons§8]"
    categoryConfig["categories.stones.permission"] = "ia.menu.pathwayaddons"
    categoryConfig["categories.stones.items"] = models
    categoryConfig.saveConfig()

}

fun convertBranches() {

    val blocks = listOf(
       "spruce_log", "birch_log", "acacia_log", "dark_oak_log", "jungle_log"
    )

    val stoneFolder = File("F:\\GameServers\\spigot\\itemsadder\\plugins\\ItemsAdder\\data\\resource_pack\\assets\\pathwayaddons\\models\\branches")
    var models = mutableListOf<String>()

    val solid = mutableListOf(false, false, false, false)

    val bases = stoneFolder.listFiles { dir, name -> name.startsWith("oak") && !name.startsWith("oak_") }

    (1..bases.size).forEach {
        models.add("oak$it")
    }


    for(block in blocks) {
        for (stone in bases) {
            val number = stone.nameWithoutExtension.replace("oak", "").toInt()
            val file = File(stoneFolder, "$block$number.json")
            file.writeText("{\"credit\":\"© 2021 Melion.cloud\",\"parent\":\"pathwayaddons:branches/oak$number\",\"textures\":{\"particle\":\"block/$block\",\"texture\":\"block/$block\"}}")
            models.add("$block$number")
        }
    }

    fun getSolid(id: Int): Boolean {
        return if(solid.size <= id) false
        else solid[id]
    }

    val stoneConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\pathwayaddons\\branches.yml")
    stoneConfig["info.namespace"] = "pathwayaddons"
    models.forEach {
        stoneConfig["items.$it.resource.material"] = "STONE"
        stoneConfig["items.$it.resource.generate"] = false
        stoneConfig["items.$it.resource.model_path"] = "branches/$it"
        stoneConfig["items.$it.behaviours.furniture.small_hitbox"] = true
        stoneConfig["items.$it.behaviours.furniture.solid"] = getSolid(it.last().toString().toInt()-1)
    }
    stoneConfig.saveConfig()

    models = models.map { "pathwayaddons:$it" }.toMutableList()
    val categoryConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\pathwayaddons\\categories.yml")
    categoryConfig["categories.branches.enabled"] = true
    categoryConfig["categories.branches.icon"] = models.first()
    categoryConfig["categories.branches.name"] = "§x§a§a§a§6§9§dBranches §8[§x§5§5§e§f§c§4Pathway Addons§8]"
    categoryConfig["categories.branches.permission"] = "ia.menu.pathwayaddons"
    categoryConfig["categories.branches.items"] = models
    categoryConfig.saveConfig()

}

fun convertBlocks() {
    val blocks = listOf(
        "stone", "stone_bricks", "bricks", "andesite", "granite", "barrel_bottom",
        "polished_andesite",
        "birch_planks", "dark_oak_planks",
        "oak_planks", "spruce_planks"
    )

    val modelFolder = File("F:\\GameServers\\spigot\\itemsadder\\plugins\\ItemsAdder\\data\\resource_pack\\assets\\melion\\models\\deco\\halfblocks")

    println("Blocks: ${blocks.size}")

    val blockConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\melion\\blocks.yml")
    blockConfig["info.namespace"] = "melion"

    var counter = 0

    fun getModelType(): String {
        counter++
        return when(counter) {
            in 1..700 -> "REAL_NOTE"
            in 701..850 -> "REAL"
            else -> "TILE"
        }
    }

    val categoryConfig = FileConfig("plugins\\ItemsAdder\\data\\items_packs\\melion\\categories.yml")
    val convertedBlocks = mutableListOf<String>()

    val rotations = listOf("n", "w", "s", "e", "t", "d")

    blocks.forEach { first ->
        blocks.forEach second@ { second ->
            if(first == second) return@second

            val reversedFile =  File(modelFolder, "${second}_${first}.json")
            if (reversedFile.exists()) return@second

            val name = "${first}_${second}"
            val type = getModelType()

            if(type == "TILE") return@forEach

            for (rotation in rotations) {
                val file = File(modelFolder, "${name}_$rotation.json")
                file.createNewFile()
                file.writeText("{\"credit\":\"© 2021 Melion.cloud\",\"parent\":\"melion:deco/halfblocks/halfblock_$rotation\",\"textures\":{\"particle\":\"block/$first\",\"second\":\"block/$second\",\"first\":\"block/$first\"}}")
                blockConfig["items.${name}_$rotation.resource.material"] = "PAPER"
                blockConfig["items.${name}_$rotation.resource.generate"] = false
                blockConfig["items.${name}_$rotation.resource.model_path"] = "deco/halfblocks/${name}_$rotation"

                blockConfig["items.${name}_$rotation.specific_properties.block.hardness"] = 4.5
                blockConfig["items.${name}_$rotation.specific_properties.block.placed_model.type"] = getModelType()

                convertedBlocks.add("melion:${name}_$rotation")
            }
            println("Combined $first and $second")
        }
    }

    categoryConfig["categories.blocks.enabled"] = true
    categoryConfig["categories.blocks.icon"] = convertedBlocks.first()
    categoryConfig["categories.blocks.name"] = "Blocks"
    categoryConfig["categories.blocks.permission"] = "ia.menu.melion"
    categoryConfig["categories.blocks.items"] = convertedBlocks
    blockConfig.saveConfig()
    categoryConfig.saveConfig()

}