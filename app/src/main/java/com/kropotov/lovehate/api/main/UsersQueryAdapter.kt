package com.kropotov.lovehate.api.main

import com.kropotov.lovehate.GetUserQuery
import com.kropotov.lovehate.GetUsersQuery
import com.kropotov.lovehate.SendFeedbackMutation
import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.type.UsersListType as GeneratedUsersListType

object UsersQueryAdapter {

    fun getUsers(onlyFirst: Boolean, sortType: UsersListType, page: Int) =
        GetUsersQuery(onlyFirst, sortType.mapToGenerated(), page)

    fun getCurrentUser() = GetUserQuery()

    fun sendFeedback(text: String) = SendFeedbackMutation(text)

    private fun UsersListType.mapToGenerated() = GeneratedUsersListType.valueOf(name)
}
