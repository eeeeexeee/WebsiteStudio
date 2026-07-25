package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolios")
data class PortfolioEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val lastModifiedTimestamp: Long,
    val portfolioStateJson: String
)
