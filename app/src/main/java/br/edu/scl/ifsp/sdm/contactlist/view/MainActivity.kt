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


class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //data source
    private val contactList: MutableList<Contact> = mutableListOf()

    //Adapter
    private val contactAdapter:ContactRvAdapter by lazy {
        ContactRvAdapter( contactList)
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
                    //editar
                } else {
                    contactList.add(newOrEditedContact)

                }
                contactAdapter.notifyDataSetChanged() //e vai notificvar o adaptter dessa modificacao
            }
            }
        }

        fillContacts()

        amb.contactRv.adapter = contactAdapter
        amb.contactRv.layoutManager = LinearLayoutManager(this)
         //aqui é o click LONGO

        /*amb.contactRv.setOnItemClickListener { _, _, position, _ -> //aqui o click curto
            startActivity(Intent(this, ContactActivity::class.java).apply {
                putExtra(EXTRA_CONTACT, contactList[position])
                putExtra(EXTRA_VIEW_CONTACT, true)
            }.also {
                startActivity(it)
            })
        }*/
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

    //funcao para ciar o menu de contexto EDIT e REMOVE
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    //funcao para tratar o clique no menu de contexto
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position

       return when(item.itemId){
           R.id.removeContactMi -> {
               //adcninando a funcionalidade do remove
               contactList.removeAt(position)
               contactAdapter.notifyDataSetChanged()
               Toast.makeText(this, "Contact removido", Toast.LENGTH_SHORT).show()
               true
           }
           R.id.editContactMi -> {
               //adcninando a funcionalidade do edit
               carl.launch(Intent(this, ContactActivity::class.java).apply {
                   putExtra(EXTRA_CONTACT, contactList[position])
                   putExtra(EXTRA_VIEW_CONTACT, false)

               })
               true
           }
           else -> {false}
       }
    }

    override fun onDestroy() {
        super.onDestroy()

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