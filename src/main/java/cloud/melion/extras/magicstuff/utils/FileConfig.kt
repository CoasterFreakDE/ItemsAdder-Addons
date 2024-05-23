package cloud.melion.extras.magicstuff.utils

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class FileConfig(fileName: String) : YamlConfiguration() {
    private val path: String = fileName
    fun saveConfig() {
        try {
            save(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        val file = File(path)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            load(path)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }
}
