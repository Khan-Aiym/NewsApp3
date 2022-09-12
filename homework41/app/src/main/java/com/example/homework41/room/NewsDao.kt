package com.example.homework41.room

import androidx.room.*
import com.example.homework41.Model.Model

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: Model?)

    @get:Query("SELECT * FROM  model ")
    val all: MutableList<Model?>

    @Query("SELECT * FROM model ORDER BY `create` DESC")
    fun sortAll(): MutableList<Model?>

    @Delete
    fun deleteTask(model: Model?)

    @Query("SELECT * FROM model WHERE title LIKE '%' || :search || '%'")
    fun getSearch(search: String?): MutableList<Model?>

    @Query("SELECT * FROM model ORDER BY title ASC")
    fun sort(): MutableList<Model?>
}