package com.example.finance

import java.time.Instant
import java.util.*

data class DateObject(var raw: Instant, var date:Date, var formattedDate: String, var index: Int)
