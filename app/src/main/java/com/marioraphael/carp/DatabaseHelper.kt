package com.marioraphael.carp

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_DATE TEXT," +
                "$COL_NUM_RIDES INTEGER," +
                "$COL_MILES_DRIVEN REAL)"
        db?.execSQL(createTable)
    }

    const val DATABASE_NAME = "statistics_db"
    const val DATABASE_VERSION = 1
    const val TABLE_NAME = "statistics"
    const val COL_ID = "id"
    const val COL_DATE = "date"
    const val COL_NUM_RIDES = "num_rides"
    const val COL_MILES_DRIVEN = "miles_driven"

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
// Drop the old table and create a new one
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertStat(date: String, numRides: Int, milesDriven: Double) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_DATE, date)
        contentValues.put(COL_NUM_RIDES, numRides)
        contentValues.put(COL_MILES_DRIVEN, milesDriven)
        db.insert(TABLE_NAME, null, contentValues)
    }
