package com.example.weapp_dv10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.Reference

class Registro : AppCompatActivity() {

    private lateinit var txtName:EditText
    private lateinit var txtPassword:EditText
    private lateinit var txtDir:EditText
    private lateinit var txtFechaNac:EditText
    private lateinit var txtPhone:EditText
    private lateinit var txtEmail:EditText

    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        txtName=findViewById(R.id.txtName)
        txtPassword=findViewById(R.id.txtPassword)
        txtDir=findViewById(R.id.txtDir)
        txtFechaNac=findViewById(R.id.txtFechaNac)
        txtPhone=findViewById(R.id.txtPhone)
        txtEmail=findViewById(R.id.txtEmail)

        progressBar= findViewById(R.id.progressBar)

        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()

        dbReference=database.reference.child("Usuario")
    }

    fun register(view:View){
        nuevaCuenta()
    }

    private fun nuevaCuenta() {
        val name: String = txtName.text.toString()
        val pass: String = txtPassword.text.toString()
        val dir: String = txtDir.text.toString()
        val fecha_nac: String = txtFechaNac.text.toString()
        val tel: String = txtPhone.text.toString()
        val email: String = txtEmail.text.toString()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(dir) && !TextUtils.isEmpty(fecha_nac) && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(email)){
            progressBar.visibility=View.VISIBLE

            auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this){
                    task ->

                    if (task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        verificacionEmail(user)

                        val userBD=dbReference.child(user?.uid.toString())

                        userBD.child("nombre").setValue(name)
                        userBD.child("pass").setValue(pass)
                        userBD.child("email").setValue(email)
                        userBD.child("direccion").setValue(dir)
                        userBD.child("fechaNac").setValue(fecha_nac)
                        userBD.child("tel").setValue(tel)

                        accion()
                    }
                }

        }
    }

    private fun accion(){
        startActivity(Intent(this,LoginActivity::class.java  ))
    }

    private fun verificacionEmail(user:FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->

                if (task.isComplete){
                    Toast.makeText(this,"Email enviado",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Error al enviar el email",Toast.LENGTH_LONG).show()
                }
            }
    }


}
