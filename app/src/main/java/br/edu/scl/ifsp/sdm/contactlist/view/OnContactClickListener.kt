package br.edu.scl.ifsp.sdm.contactlist.view

interface OnContactClickListener {
    fun onContactClick(position: Int)
    fun onRemoveContactMenuItemClick(position: Int)
    fun onEditContactMenuItemClick(position: Int)

}