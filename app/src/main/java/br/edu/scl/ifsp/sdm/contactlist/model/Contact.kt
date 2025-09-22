package br.edu.scl.ifsp.sdm.contactlist.model

import android.os.Parcelable
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.INVALID_CONTACT_ID
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Int? = INVALID_CONTACT_ID,
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = ""
) : Parcelable

