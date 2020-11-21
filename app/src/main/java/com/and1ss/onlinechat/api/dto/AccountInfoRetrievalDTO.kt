package com.and1ss.onlinechat.api.dto

import com.and1ss.onlinechat.api.model.AccountInfo

data class AccountInfoRetrievalDTO(
    var id: String? = null,
    var name: String? = null,
    var surname: String? = null
) {
    fun mapToAccountInfo() =
        AccountInfo(id!!, name!!, surname!!)

    fun getInitials(): String {
        val nameLetter = name ?: ""
        val surnameLetter = surname ?: ""

        var initials = ""
        if (nameLetter.isNotBlank()) {
            initials += nameLetter[0] + " "
        }

        if (surnameLetter.isNotBlank()) {
            initials += surnameLetter[0]
        }

        return initials
    }
}