package com.everis.listadecontatos.feature.contato

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.everis.listadecontatos.R
import com.everis.listadecontatos.application.ContatoApplication
import com.everis.listadecontatos.bases.BaseActivity
import com.everis.listadecontatos.feature.listacontatos.model.ContatosModel
import kotlinx.android.synthetic.main.activity_contato.*

class ContatoActivity : BaseActivity() {

    private var idContato: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)
        setupToolBar(toolBar, "Contato",true)
        setupContato()
        initButtons()
    }

    private fun initButtons(){
        btnSalvarConato.setOnClickListener { salvarContato() }
        btnExcluirContato.setOnClickListener{ excluirContato() }
    }

    private fun setupContato(){
        idContato = intent.getIntExtra("index",-1)
        if (idContato == -1){
            btnExcluirContato.visibility = View.GONE
            return
        }
        progress.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(1500)
            var lista = ContatoApplication.instance.helperDB?.buscarContatos("$idContato",true) ?: return@Runnable
            var contato = lista.getOrNull(0) ?: return@Runnable
            runOnUiThread {
                etNome.setText(contato.nome)
                etTelefone.setText(contato.telefone)
                progress.visibility = View.GONE
            }
        }).start()
    }

    private fun salvarContato(){
        if(etNome.text.toString().isNotBlank() && etTelefone.text.toString().isNotBlank()) {
            val nome = etNome.text.toString().trim()
            val telefone = etTelefone.text.toString().trim()
            val contato = ContatosModel(
                    idContato,
                    nome,
                    telefone
            )
            progress.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(500)
                if (idContato == -1) {
                    ContatoApplication.instance.helperDB?.salvarContato(contato)
                } else {
                    ContatoApplication.instance.helperDB?.updateContato(contato)
                }
                runOnUiThread {
                    progress.visibility = View.GONE
                    finish()
                }
            }).start()

        }else{
            Toast.makeText(this,"Os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun excluirContato() {
        if(idContato > -1){
            progress.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(500)
                ContatoApplication.instance.helperDB?.deletarCoontato(idContato)
                runOnUiThread {
                    progress.visibility = View.GONE
                    finish()
                }
            }).start()
        }
    }
}
