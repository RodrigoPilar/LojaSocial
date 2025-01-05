package com.example.lojasocial.ui.check

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lojasocial.R

class CheckView : AppCompatActivity() {

    private lateinit var checkInButton: Button
    private lateinit var registerButton: Button
    private lateinit var fullNameField: EditText
    private lateinit var phoneNumberField: EditText
    private lateinit var familyStatusField: EditText
    private lateinit var firstVisitSwitch: Switch
    private lateinit var viewModel: CheckViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        // Inicialização dos elementos do layout
        checkInButton = findViewById(R.id.checkInButton)
        registerButton = findViewById(R.id.registerButton)
        fullNameField = findViewById(R.id.fullName)
        phoneNumberField = findViewById(R.id.phoneNumber)
        familyStatusField = findViewById(R.id.familyStatus)
        firstVisitSwitch = findViewById(R.id.firstVisitSwitch)

        // Inicializa o ViewModel
        viewModel = ViewModelProvider(this).get(CheckViewModel::class.java)

        // Lógica do botão de Check-in
        checkInButton.setOnClickListener {
            val fullName = fullNameField.text.toString()
            val phoneNumber = phoneNumberField.text.toString()
            val familyStatus = familyStatusField.text.toString()
            val isFirstVisit = firstVisitSwitch.isChecked

            if (fullName.isNotEmpty() && phoneNumber.isNotEmpty() && familyStatus.isNotEmpty()) {
                // Realiza o check-in chamando o método do ViewModel
                viewModel.performCheckIn(
                    fullName,
                    phoneNumber,
                    familyStatus,
                    isFirstVisit
                )
                // Exibe uma mensagem de sucesso
                Toast.makeText(this, "Check-in realizado com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                // Exibe uma mensagem de erro se algum campo estiver vazio
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }

        // Lógica do botão de Registar (se necessário)
        registerButton.setOnClickListener {
            // Pode-se adicionar uma lógica para registrar o usuário no banco de dados
            Toast.makeText(this, "Usuário registrado!", Toast.LENGTH_SHORT).show()
        }
    }
}
