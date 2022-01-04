package com.example.imovie.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imovie.data.entity.favorite.FavoriteEntity


@Dao
interface FavoriteDao {

    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorite(): DataSource.Factory<Int, FavoriteEntity>

    @Query("SELECT * FROM FavoriteEntity WHERE id = :id")
    fun getFavoriteById(id: Int?): LiveData<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: FavoriteEntity?)

    @Query("DELETE FROM FavoriteEntity WHERE id = :id")
    fun deleteFavorite(id: Int?): Int

}