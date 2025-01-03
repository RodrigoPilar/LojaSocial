package com.example.lojasocial.ui.check

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import com.example.lojasocial.R

// Activity responsável por gerir a interface do utilizador para o Check-in e Check-out
class CheckView : AppCompatActivity() {

    // Declaração dos botões de Check-in e Check-out
    private lateinit var checkInButton: Button
    private lateinit var checkOutButton: Button

    // Declaração do ViewModel que vai gerir a lógica de Check-in/Check-out
    private lateinit var viewModel: CheckViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout para esta Activity
        setContentView(R.layout.activity_check)  // Verifica se o layout activity_check.xml existe na pasta res/layout

        // Inicializa os botões de Check-in e Check-out utilizando os IDs definidos no layout XML
        checkInButton =
            findViewById(R.id.checkInButton)  // Certifica-te que este ID existe no layout XML
        checkOutButton =
            findViewById(R.id.checkOutButton)  // Certifica-te que este ID existe no layout XML

        // Inicializa o ViewModel que vai gerir a lógica do Check-in e Check-out
        viewModel =
            ViewModelProvider(this).get(CheckViewModel::class.java)  // Inicializa o ViewModel

        // Lógica para o botão de Check-in
        checkInButton.setOnClickListener {
            // Chama a função do ViewModel para realizar o Check-in, passando o userId
            viewModel.checkIn("user1") // Substituir "user1" pelo ID do utilizador real
            viewModel.checkInOutState.observe(this) { state ->
                if (state.status == "checked-in") {
                    Toast.makeText(this, "Check-in realizado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Erro ao realizar Check-in.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Lógica para o botão de Check-out
        checkOutButton.setOnClickListener {
            // Chama a função do ViewModel para realizar o Check-out, passando o userId
            viewModel.checkOut("user1") // Substituir "user1" pelo ID do utilizador real
            viewModel.checkInOutState.observe(this) { state ->
                if (state.status == "checked-out") {
                    Toast.makeText(this, "Check-out realizado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Erro ao realizar Check-out.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}