package com.gradysbooch.restaurant.util

import com.gradysbooch.restaurant.model.Table
import java.util.*
import kotlin.collections.ArrayList

class TableUtil {
    companion object {
        fun getTables(n: Int): List<Table> {
            val tables = ArrayList<Table>()
            for (i in 1..n) {
                tables.add(Table(i.toString(), "T" + i.toString(), 0, false))
            }
            return tables
        }
    }
}