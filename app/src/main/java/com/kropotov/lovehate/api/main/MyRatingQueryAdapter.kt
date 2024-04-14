package com.kropotov.lovehate.api.main

import com.kropotov.lovehate.GetMyRatedEventsQuery

object MyRatingQueryAdapter {

    fun getLatestItems(page: Int) = GetMyRatedEventsQuery(page)
}