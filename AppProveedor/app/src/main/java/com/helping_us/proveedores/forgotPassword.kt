package com.helping_us.proveedores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

private lateinit var forEmail: EditText
private lateinit var enviar: Button
private lateinit var barra: ProgressBar

private var conex:Int=0
private var MY_PERMISSIONS_REQUEST: Int = 0
private lateinit var fusedLocationClient: FusedLocationProviderClient

private lateinit var auth:FirebaseAuth
//class forgotPassword : AppCompatActivity()
class forgotPassword : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        actionBar?.hide()
        //no aparesca la barra superior
        supportActionBar?.hide()
        //para que no gire la pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)


        forEmail=findViewById(R.id.txtcorreo)
        enviar=findViewById(R.id.btnActualizar)
        barra=findViewById(R.id.barras)

        enviar.setOnClickListener { enviar() }

        auth= FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        visibles()
        conexInternet()

    }
//    fun onAttachFragment(fragment: Fragment) {
//        if (fragment is HeadlinesFragment) {
//            fragment.setOnHeadlineSelectedListener(this)
//        }
//    }
    fun visibles(){
        forEmail.visibility= View.VISIBLE
        enviar.visibility= View.VISIBLE
        barra.visibility= View.GONE
    }
    fun invisibles(){
        forEmail.visibility= View.GONE
        enviar.visibility= View.GONE
        barra.visibility= View.VISIBLE
    }
    private fun enviar(){
        if(conex==1){
            var email=txtcorreo.text.toString().trim()
            var e : Exception=java.lang.Exception()

            if (!TextUtils.isEmpty(email)){
                //verificaremos
                invisibles()
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this) {
                            task ->
                        if (task.isSuccessful){
                            // si no hay ningun problema manda el correo para verificar
                            visibles()
                            Toast.makeText(this,"Revise su cuenta de correo", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, login::class.java))
                            finish()
                        }else{

                            visibles()
                            Toast.makeText(this,"Error al enviar el correo, verifique la cuenta", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
                            txtcorreo.setText("")
                            txtcorreo.requestFocus()

                        }
                    }
            }else{
                visibles()
                Toast.makeText(this,"El correo no debe estar vacio", Toast.LENGTH_SHORT).show()
                txtcorreo.setText("")
                txtcorreo.requestFocus()

            }
        }


    }

    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    fun conexInternet() {
        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networInfo = cm.activeNetworkInfo

        if (networInfo != null && networInfo.isConnected == true) {
            //estas conectado a internet
            conex = 1
            return
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Helping Us")
            builder.setMessage("Lo sentimos, conexción a interet a fallado!")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                finish()
            }

            builder.show()
            //oast.makeText(baseContext,"No se ha podido enviar tu solicitud, verifica tu conexión de internet", Toast.LENGTH_SHORT).show()
        }
    }
}
