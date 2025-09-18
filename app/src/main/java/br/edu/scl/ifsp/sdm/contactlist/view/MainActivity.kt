package br.edu.scl.ifsp.sdm.contactlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.view.Menu
import android.view.MenuItem
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
import android.content.Intent
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactAdapter
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRvAdapter
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.view.ContactActivity



class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //data source
    private val contactList: MutableList<Contact> = mutableListOf()

    //Adapter
    private val contactAdapter:ContactRvAdapter by lazy {
        ContactRvAdapter( contactList, this)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK){
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.also {newOrEditedContact ->
                    if(contactList.any{it.id == newOrEditedContact.id}){ //se o contato ja esta na lista
                      val position = contactList.indexOfFirst { it.id == newOrEditedContact.id }
                        contactList[position] = newOrEditedContact
                        contactAdapter.notifyItemChanged(position)

                    //editar
                } else {
                    contactList.add(newOrEditedContact)
                    contactAdapter.notifyItemInserted(contactList.lastIndex)


                }
                }
            }
        }

        fillContacts()

        amb.contactRv.adapter = contactAdapter
        amb.contactRv.layoutManager = LinearLayoutManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java ))
                true
            }
            else -> {false}
        }
    }


    override fun onRemoveContactMenuItemClick(position: Int) {
        contactList.removeAt(position)
        contactAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "Contact removido", Toast.LENGTH_SHORT).show()

    }

    override fun onEditContactMenuItemClick(position: Int){
        carl.launch(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])

    })
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onContactClick(position: Int) {
        Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
            putExtra(EXTRA_VIEW_CONTACT, true)
        }.also {
            startActivity(it)
        }
    }



    private fun fillContacts(){ //aqui vou ter 50 contatos
        for (i in 1..10) {
            contactList.add(
                Contact(
                    i,
                    "Nome $i",
                    "Endereço $i",
                    "Telefone $i",
                    "Email $i"
                )
            )
        }
    }
}