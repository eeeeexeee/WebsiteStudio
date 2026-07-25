package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Query("SELECT * FROM portfolios ORDER BY lastModifiedTimestamp DESC")
    fun getAllPortfolios(): Flow<List<PortfolioEntity>>

    @Query("SELECT * FROM portfolios WHERE id = :id LIMIT 1")
    suspend fun getPortfolioById(id: String): PortfolioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePortfolio(portfolio: PortfolioEntity)

    @Query("DELETE FROM portfolios WHERE id = :id")
    suspend fun deletePortfolioById(id: String)
}
