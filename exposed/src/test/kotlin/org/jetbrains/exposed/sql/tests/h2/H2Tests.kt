package org.jetbrains.exposed.sql.tests.h2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.tests.DatabaseTestsBase
import org.jetbrains.exposed.sql.tests.TestDB
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransaction
import org.junit.Test
import kotlin.test.assertFailsWith

class H2Tests : DatabaseTestsBase() {

    @Test
    fun replaceInH2WithMySQLMode() {
        withDb(TestDB.H2_MYSQL) {

            SchemaUtils.create(Testing)
            Testing.replace {
                it[Testing.id] = 1
            }

            assertEquals(1, Testing.select { Testing.id.eq(1) }.single()[Testing.id])
        }
    }

    @Test
    fun replaceInH2WithoutMySQLMode() {
        withDb(TestDB.H2) {

            SchemaUtils.create(Testing)
            assertFailsWith(UnsupportedOperationException::class) {
                Testing.replace {
                    it[Testing.id] = 1
                }
            }
        }
    }

    object Testing : Table("H2_TESTING") {
        val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    }
}