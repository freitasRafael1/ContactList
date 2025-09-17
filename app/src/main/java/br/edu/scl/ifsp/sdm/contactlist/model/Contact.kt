package br.edu.scl.ifsp.sdm.contactlist.model

import android.os.Parcelable
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.INVALID_CONTACT_ID
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact (
    var id: Int? = INVALID_CONTACT_ID, // -1 para trabalhar futuramente com biblioteca de reflexao
    var name: String = "",
    var address: String = "",
    var phone: String = "",
    var email: String = ""

): Parcelable {

}