package org.example.project.API.Data

import org.example.project.Model.GetProfileInfo

data class GetProfileInfoResponse(val status: String, val description: String,
                                  val getProfileInfo: GetProfileInfo?)
