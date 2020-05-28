 package com.helping_us.proveedores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_email_and_pass.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

 class EmailAndPass : AppCompatActivity() {

     private lateinit var txtnpmbre: EditText
     private lateinit var txtapellido: EditText
     private lateinit var txtclave: TextInputEditText
     private lateinit var txtcorreo: EditText
     private lateinit var txtdirec: EditText
     private lateinit var telefono: EditText
     private lateinit var txtClave2: EditText
     private lateinit var pass: TextInputLayout
     private lateinit var pass2: TextInputLayout
     private lateinit var con1: TextInputLayout
     private lateinit var con2: TextInputLayout
     private lateinit var con3: TextInputLayout
     private lateinit var con4: TextInputLayout
     private lateinit var con5: TextInputLayout

     lateinit var button: Button
     private lateinit var progressBar: ProgressBar
     private lateinit var tvLogin: TextView


     private lateinit var  dbRefe: DatabaseReference
     private lateinit var database: FirebaseDatabase
     private lateinit var  auth: FirebaseAuth
     private lateinit var googleSignInClient: GoogleSignInClient

     private  lateinit var excepti: FirebaseAuthUserCollisionException
     private lateinit var formatInval: FirebaseAuthInvalidCredentialsException


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_and_pass)

         txtnpmbre=findViewById(R.id.txtnombre)
         txtapellido=findViewById(R.id.txtapellido)
         txtclave=findViewById(R.id.txtclave)
         txtcorreo=findViewById(R.id.txtcorreo)
         txtdirec=findViewById(R.id.txtdireccion)
         button=findViewById(R.id.btnActualizar)
         progressBar=findViewById(R.id.progressBar)
         tvLogin=findViewById(R.id.tvIog)
         telefono=findViewById(R.id.txttelefono)
         txtClave2=findViewById(R.id.txtId)
         pass=findViewById(R.id.tilPass)
         pass2=findViewById(R.id.tilPass2)
         con1=findViewById(R.id.tilContenedor)
         con2=findViewById(R.id.tilCon2)
         con3=findViewById(R.id.tilCon3)
         con4=findViewById(R.id.tilCon4)
         con5=findViewById(R.id.tilCon5)

         // btn.setOnClickListener { Goo() }

         database= FirebaseDatabase.getInstance()
         auth= FirebaseAuth.getInstance()
         dbRefe=database.reference.child("Usuarios/Proveedores")

         button.setOnClickListener { registrar() }
         tvLogin.setOnClickListener { irLog() }
    }

     public override fun onStart() {
         super.onStart()
         // Check if user is signed in (non-null) and update UI accordingly.
         auth.currentUser
         //googleSignInClient.signInIntent

     }

     private fun irLog() {
         startActivity(Intent(this, login::class.java))
         finish()
     }

     private fun registrar() {
         // createNewAcount()
         nuevaCta()
         //Toast.makeText(this,"exito", Toast.LENGTH_LONG).show()
     }

     @SuppressLint("MissingPermission")
     private  fun nuevaCta(){
         val nombre:String=txtnpmbre.text.toString().trim()
         val apellido:String=txtapellido.text.toString().trim()
         val clave:String=txtclave.text.toString().trim()
         val correo:String=txtcorreo.text.toString().trim()
         val celular:String=telefono.text.toString().trim()
         val direccion:String = txtdirec.text.toString().trim()
         val clave2:String=txtClave2.text.toString().trim()
         val user1: FirebaseUser? = auth.currentUser


         val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         val networInfo= cm.activeNetworkInfo

         if(networInfo != null && networInfo.isConnected){
             //----------------------------------correo--------------------------------------------------------
             if (!TextUtils.isEmpty(correo.trim())){
                 //----------------------------------password-------------------------------------------
                 if(!TextUtils.isEmpty(clave.trim()) && clave.length>=6){
                     if(!TextUtils.isEmpty(clave2.trim()) && clave2.length>=6){
                         if (clave.trim()==clave2.trim()){
                             //*---------------------------------------------------name user------------------
                             if (!TextUtils.isEmpty(nombre) && nombre.length>=3){
                                 ///---------------------------------------------------apellido user----------------------------
                                 if (!TextUtils.isEmpty(apellido) && apellido.length>=4){

                                     //--------------------------------------------celular----------------------------------------------------
                                     if(!TextUtils.isEmpty(celular)){
                                         //-----------------------------------------el primer digito si es 9,8,3--------------------
                                         var num=txttelefono.text.toString().trim()

                                         var n3="3".single()
                                         var n8 = "8".single()
                                         var n9 = "9".single()

                                         if (num[0]== n3 ||num[0]== n8 || num[0]== n9 ){
                                             //Toast.makeText(this,"Este Numero :" +num[0], Toast.LENGTH_LONG).show()
                                             //-----------------------------------------------direccion--------------------------
                                             if (!TextUtils.isEmpty(direccion) && direccion.length>=10){
                                                 registrarAuth()
                                             }else{
                                                 txtdirec.setText("")
                                                 txtdirec.requestFocus()
                                                 txtdirec.error="Ingrese su Direción más de 10 digítos"
                                                 //  return
                                             }

                                         }else{
                                             Toast.makeText(this,"Este número : " +num.trim()+" No esta permitido, por favor ingrese uno valido", Toast.LENGTH_LONG).show()
                                             visibles()

                                         }
                                     }else{
                                         telefono.setText("")
                                         telefono.requestFocus()
                                         telefono.error="Ingrese su Celular"
                                         // return
                                     }
                                 }else{
                                     txtapellido.setText(" ")
                                     txtapellido.requestFocus()
                                     txtapellido.error="Ingrese su Apellido correcto"
                                     //  return
                                 }

                             }else{
                                 txtnpmbre.setText("")
                                 txtnpmbre.requestFocus()
                                 txtnpmbre.error="Ingrese su Nombre correcto"
                                 ////return
                             }
                         }else {
                             txtClave2.setText("")
                             txtclave.setText("")
                             txtclave.requestFocus()
                             //txtclave.error="Sus contraseñas no coinciden, verifiquelas!"
                             Toast.makeText(this,"Sus contraseñas no coinciden, verifiquelas!", Toast.LENGTH_SHORT).show()
                             // return
                         }
                     }else{
                         txtClave2.setText("")
                         txtClave2.requestFocus()
                         txtClave2.error="Ingrese su contraseña mayor a 5 digitos"
                         //  return
                     }
                 }else{
                     txtclave.setText("")
                     txtclave.requestFocus()
                     txtclave.error="Ingrese su contraseña mayor a 5 digitos"
                     // return
                 }
             }else{
                 txtcorreo.setText("")
                 txtcorreo.requestFocus()
                 txtcorreo.error="Ingrese su Correo"
                 //return
             }

         }else {
             Toast.makeText(baseContext, "ERROR: No tienes acceso a Internet para registrarse", Toast.LENGTH_SHORT).show()
             clean()
             return
         }

     }
     override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
         if ((keyCode == KeyEvent.KEYCODE_BACK)) {

             var inten= Intent(this, MainActivity::class.java)
             inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP/Intent.FLAG_ACTIVITY_CLEAR_TASK/Intent.FLAG_ACTIVITY_NEW_TASK)
             startActivity(inten)
             finish()
         }
         return super.onKeyDown(keyCode, event)
     }

     fun visibles(){
         txtnpmbre.visibility= View.VISIBLE
         txtapellido.visibility= View.VISIBLE
         txtclave.visibility= View.VISIBLE
         txtcorreo.visibility= View.VISIBLE
         txtdirec.visibility= View.VISIBLE
         button.visibility= View.VISIBLE
         progressBar.visibility= View.GONE
         tvLogin.visibility= View.VISIBLE
         telefono.visibility= View.VISIBLE
         txtClave2.visibility= View.VISIBLE
         pass.visibility= View.VISIBLE
         pass2.visibility= View.VISIBLE
         con1.visibility= View.VISIBLE
         con2.visibility= View.VISIBLE
         con3.visibility= View.VISIBLE
         con4.visibility= View.VISIBLE
         con5.visibility= View.VISIBLE

     }
     fun invisibles(){
         txtnpmbre.visibility= View.GONE
         txtapellido.visibility= View.GONE
         txtclave.visibility= View.GONE
         txtcorreo.visibility= View.GONE
         txtdirec.visibility= View.GONE
         button.visibility= View.GONE
         progressBar.visibility= View.VISIBLE
         tvLogin.visibility= View.GONE
         telefono.visibility= View.GONE
         txtClave2.visibility= View.GONE
         pass.visibility= View.GONE
         pass2.visibility= View.GONE
         con1.visibility= View.GONE
         con2.visibility= View.GONE
         con3.visibility= View.GONE
         con4.visibility= View.GONE
         con5.visibility= View.GONE
     }

     private fun registrarAuth(){
         val clave:String=txtclave.text.toString().trim()
         val correo:String=txtcorreo.text.toString().trim()
         invisibles()

         auth.createUserWithEmailAndPassword(correo,clave)
             .addOnCompleteListener(this) {
                     task ->
                 if (!task.isSuccessful) {
                     //Toast.makeText(this, task.exception!! .message, Toast.LENGTH_SHORT).show()
                     try {

                         Toast.makeText(this, "Cuenta de correo ya esta registrada o tiene algun error, intenta con otra cuenta", Toast.LENGTH_LONG).show()
                         txtcorreo.setText("")
                         txtcorreo.isFocusable=true
                         txtcorreo.requestFocus()
                         txtcorreo.error="Ingrese su Correo"
                         visibles()

                     }
                     catch(e: FirebaseAuthInvalidCredentialsException)
                     {
                         Toast.makeText(this, "Formato del correo invalido", Toast.LENGTH_SHORT).show()
                         txtcorreo.setText("")
                         txtcorreo.requestFocus()
                         visibles()
                     }
                     catch(e: FirebaseAuthUserCollisionException) {
                         Toast.makeText(this, "Cuenta de correo ya existe, intenta con otra cuenta", Toast.LENGTH_SHORT).show()
                         txtcorreo.setText("")
                         txtcorreo.requestFocus()
                         visibles()

                     }

                 }
                 else {

                     try {
                         val users:FirebaseUser?=auth.currentUser
                         verifyEmail(users)
                     }

                     catch(e: FirebaseAuthUserCollisionException) {
                         Toast.makeText(this, "Cuenta de correo ya existe, intenta con otra cuenta", Toast.LENGTH_SHORT).show()
                         txtcorreo.setText("")
                         txtcorreo.requestFocus()
                         visibles()

                     }
                 }
             }
     }

     private fun verifyEmail(users: FirebaseUser?) {
         users?.sendEmailVerification()
             ?.addOnCompleteListener(this){
                     task ->
                 if(task.isSuccessful){
                     // Log.d(TAG, "Email enviado")
                     //Toast.makeText(baseContext,"Email Enviado a: "+txtcorreo.text.toString(), Toast.LENGTH_LONG).show()
                     runBlocking {
                         delay(2000)
                         registrarUser()
                     }
                 }else{
                     Toast.makeText(baseContext,"Error al Enviar el email", Toast.LENGTH_LONG).show()
                     visibles()
                     clean()
                 }
             }
     }
     //mantinene a salvo de la ofuscación al compilar con R8
     @Keep
     private fun registrarUser(){
         //se guardaran los dfatos de los usuarios que se login con coreo

         //fecha y hora del sistema
         var currentDate: Date = Date(Calendar.getInstance().timeInMillis)

         val name:String=txtnpmbre.text.toString().trim()
         val apelli:String=txtapellido.text.toString().trim()
         val passw:String=txtclave.text.toString().trim()
         val email:String=txtcorreo.text.toString().trim()
         val phone:String=telefono.text.toString().trim()
         //val id=txtClave2.text.toString().trim()
         val direc=txtdirec.text.toString().trim()
         val key = dbRefe.push().key

         //*guarda co la clase de datos
         val regisUsu=usuarios(
             correoUsu = email,
             passUsu = passw,
             nombreUsu = name,
             apellidoUsu = apelli,
             telefonoUsu = phone,
             direcUsu=direc,
             fecha = currentDate.toString(),
             //este valor me demuestra que es un proveedor
             permiso=1
         )
         dbRefe = database.reference.child("Usuarios/ProveedoresApp/Correo")
         dbRefe.child(key.toString()).setValue(regisUsu).addOnCompleteListener {
             mensaje()
             updateUsu()
         }
     }

     private fun updateUsu(){
         val user = FirebaseAuth.getInstance().currentUser

         if (user?.displayName==null ){
             val profileUpdates = UserProfileChangeRequest.Builder()
                 .setDisplayName(txtnpmbre.text.toString())
                 .build()

             user?.updateProfile(profileUpdates)
                 ?.addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         // Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                     }
                 }
             return
         }
     }
     fun userOf(auth2: FirebaseAuth, client: GoogleSignInClient) {
         auth2.signOut()
         client.signOut()
     }

     //mantinene a salvo de la ofuscación al compilar con R8
     @Keep
     data class usuarios(
         var correoUsu:String?="",
         var nombreUsu:String?="",
         var apellidoUsu:String?="",
         var direcUsu:String?="",
         var passUsu:String?="",
         var telefonoUsu:String?="",
         var fecha:String?="",
         var permiso:Int = 1
     )

     private fun mensaje () {

         val builder = AlertDialog.Builder(this)

         builder.setTitle(Html.fromHtml("<font color='#f79d00'><b><big>Helping-Us</big></b></font>"))
         builder.setCancelable(false)
         builder.setMessage(Html.fromHtml("\n Te enviamos un enlace  a su correo para que \n puedas ingresar, revisa tu correo electronico: \n <font color='#fc3d03'><b>${txtcorreo.text.toString()}</b></font>"))
         builder.setPositiveButton(android.R.string.yes) { dialog, which ->
             clean()
             visibles()
             action()
         }
         builder.show()

     }

     private  fun clean(){
         txtnpmbre.setText("")
         txtapellido.setText("")
         txtcorreo.setText("")
         txtclave.setText("")
         txtdirec.setText("")
         telefono.setText("")
         txtClave2.setText("")
         //damos de alta a los usuarios de correo y google
         auth.signOut()
         //userOf(auth,googleSignInClient)
         // txtcorreo.requestFocus()

     }
     //metodo para ir al loging
     private fun action (){
         //ir a otra actividad
         var intent:Intent= Intent(this,login::class.java)
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
         startActivity(intent)
         //da de alta el usuario que se acaba de registrar
         finish()

     }
}
