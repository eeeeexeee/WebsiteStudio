package com.example.data.local

import com.example.data.model.PortfolioState
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PortfolioRepository(private val portfolioDao: PortfolioDao) {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(PortfolioState::class.java)

    val allPortfolios: Flow<List<PortfolioState>> = portfolioDao.getAllPortfolios().map { list ->
        list.mapNotNull { entity ->
            try {
                adapter.fromJson(entity.portfolioStateJson)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun savePortfolio(state: PortfolioState) {
        val json = adapter.toJson(state)
        val entity = PortfolioEntity(
            id = state.id,
            name = state.portfolioName,
            lastModifiedTimestamp = System.currentTimeMillis(),
            portfolioStateJson = json
        )
        portfolioDao.insertOrUpdatePortfolio(entity)
    }

    suspend fun loadPortfolio(id: String): PortfolioState? {
        val entity = portfolioDao.getPortfolioById(id) ?: return null
        return try {
            adapter.fromJson(entity.portfolioStateJson)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deletePortfolio(id: String) {
        portfolioDao.deletePortfolioById(id)
    }
}
