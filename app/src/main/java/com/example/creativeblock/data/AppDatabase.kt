package com.example.creativeblock.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

// Clase que representa la base de datos de la aplicacion
// Room se encarga de crear y manejar la base de datos automaticamente
@Database(
    entities = [Idea::class],  // Lista de todas las tablas en la base de datos
    version = 1,               // Version de la base de datos (incrementar si cambia la estructura)
    exportSchema = false       // No necesitamos exportar el esquema para esta app
)
abstract class AppDatabase : RoomDatabase() {

    // Obtiene el IdeaDao para hacer operaciones con la tabla de ideas
    abstract fun ideaDao(): IdeaDao

    // Companion object para crear una unica instancia de la base de datos (Singleton)
    companion object {
        // Volatile asegura que la instancia sea visible para todos los hilos
        @Volatile
        private var Instance: AppDatabase? = null

        // Funcion para obtener la instancia de la base de datos
        fun getDatabase(context: Context): AppDatabase {
            // Si ya existe una instancia, la devuelve
            // Si no existe, crea una nueva
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "creativeblock_database"  // Nombre del archivo de base de datos
                ).build().also { Instance = it }
            }
        }
    }
}