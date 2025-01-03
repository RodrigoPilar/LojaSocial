package com.example.lojasocial.ui.check

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lojasocial.R

// Activity responsável por gerir a UI de Check-in e Check-out
class CheckView : AppCompatActivity() {

    private lateinit var checkInButton: Button
    private lateinit var checkOutButton: Button
    private lateinit var viewModel: CheckViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        // Inicializa os botões de Check-in e Check-out
        checkInButton = findViewById(R.id.checkInButton)
        checkOutButton = findViewById(R.id.checkOutButton)

        // Inicializa o ViewModel
        viewModel = ViewModelProvider(this).get(CheckViewModel::class.java)

        // Lógica para o botão de Check-in
        checkInButton.setOnClickListener {
            // Chama o ViewModel para realizar o Check-in
            viewModel.checkInOut("checked-in") { success ->
                if (success) {
                    // Caso o Check-in tenha sido bem sucedido, mostramos uma mensagem
                    Toast.makeText(this, "Check-in realizado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    // Caso haja falha, mostramos uma mensagem de erro
                    Toast.makeText(this, "Erro ao realizar Check-in.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Lógica para o botão de Check-out
        checkOutButton.setOnClickListener {
            // Chama o ViewModel para realizar o Check-out
            viewModel.checkInOut("checked-out") { success ->
                if (success) {
                    // Caso o Check-out tenha sido bem sucedido, mostramos uma mensagem
                    Toast.makeText(this, "Check-out realizado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    // Caso haja falha, mostramos uma mensagem de erro
                    Toast.makeText(this, "Erro ao realizar Check-out.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}