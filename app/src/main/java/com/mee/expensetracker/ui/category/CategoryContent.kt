package com.carded.api

import androidx.lifecycle.LiveData

sealed class CategoryContent{
    data class Item( val content: String): CategoryContent()
    data class Add( val content: String? = ""): CategoryContent()
}
