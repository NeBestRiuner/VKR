package org.example.project.model

import kotlinx.serialization.Serializable


@Serializable
data class ProfileInfo(var name:String?, var surname:String?, var patronymic:String?,
                       var phoneNumber:String?)
