package com.helping_us.proveedores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.modelo
import java.util.*

class login : AppCompatActivity() {

    private lateinit var etUser: EditText
    private lateinit var etPas: TextInputEditText
    private lateinit var etOlvi: TextView
    private lateinit var etRegistrar: TextView
    private lateinit var btnInicio: Button
    private var conex = 0
    private lateinit var progresBar: ProgressBar
    private lateinit var imagen: ImageView
    private lateinit var pass: TextInputLayout

    private lateinit var nombreGoo: String
    private lateinit var idGoo: String
    private var correoGo: String?=""
    private  var telefonoGoo:String?=""
    var usuarios:String? = ""
    var msjUsuario:String? = ""
    private lateinit var prove:String
    private lateinit var google: SignInButton
    //******************************importar clase
    private lateinit var modelos:modelo
    //****************************************************************************
    ///google
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    var RC_SIGN_IN: Int = 0

    lateinit var cuenta: String

    private lateinit var auth: FirebaseAuth
    private var MY_PERMISSIONS_REQUEST: Int = 0
    private lateinit var dataBase: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbQuery: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionBar?.hide()
        //no aparesca la barra superior
        supportActionBar?.hide()
        //para que no gire la pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)


        etUser= this.findViewById(R.id.etUser)
        etPas=findViewById(R.id.etPass)
        etOlvi=findViewById(R.id.etOlvidar)
        etRegistrar=findViewById(R.id.etRege)
        btnInicio=findViewById(R.id.btnIngresar)
        progresBar=findViewById(R.id.pbLogin)
        imagen=findViewById(R.id.tvProveedor)
        pass=findViewById(R.id.tilPass)

        google=findViewById(R.id.btnGoogleSing)

        // google.setColorScheme(SignInButton.COLOR_LIGHT)
        //google.setSize(SignInButton.SIZE_STANDARD)

        dataBase=FirebaseDatabase.getInstance()
        dbReference=dataBase.reference.child("Usuarios/Cliente Final")
        dbQuery=dataBase.reference.child("Usuarios/Cliente Final/Google")

        auth = FirebaseAuth.getInstance()
        ///////////////////////////////////////instancias de la clase Usuario****

        //goooooogle----------------------------------------------------------------------------
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_web_client_id))
            .requestIdToken(getString(R.string.default_web_client_id))
            //.requestId()
            .requestEmail()
            .build()


        googleSignInClient= GoogleSignIn.getClient(this,gso)
        google.setOnClickListener{ logGoo() }

        btnInicio.setOnClickListener { loginUser() }
      // etRegistrar.setOnClickListener { registrar() }
        etOlvi.setOnClickListener { olvidarPass() }
    }

    override fun onStart() {
        super.onStart()
        //validamos si el susuaio tiene iniciada ulguna sessión


    }

    override fun onResume() {
        super.onResume()
        // visibles()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            Toast.makeText(this,"Locentimos su versión de android no es compatible", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun logGoo(){
        conexInternet()

        if(conex==1){
            //damos de alta o todos los usuarios de la app
            signOut(auth,googleSignInClient)
            invisibles()
            //damos de alta usuarios y clientes de autenticacion de google antes de volver a llamarlos
            val signInIntent=  googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        conex=0
    }
    private fun invisibles(){
        progresBar.visibility= View.VISIBLE
        etUser.visibility= View.GONE
        etPas.visibility= View.GONE
        imagen.visibility= View.GONE
        etOlvi.visibility= View.GONE
        etRegistrar.visibility= View.GONE
        btnInicio.visibility= View.GONE
        pass.visibility= View.GONE
        google.visibility= View.GONE

    }
    private fun visibles(){
        progresBar.visibility= View.GONE
        etUser.visibility= View.VISIBLE
        etPas.visibility= View.VISIBLE
        imagen.visibility= View.VISIBLE
        etOlvi.visibility= View.VISIBLE
        etRegistrar.visibility= View.VISIBLE
        btnInicio.visibility= View.VISIBLE
        pass.visibility= View.VISIBLE
        google.visibility= View.GONE
    }
    //auth de google
    public override fun  onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Resultado devuelto al iniciar la intención desde GoogleSignInApi.get Iniciar sesión Intención

        if (requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data!!)
            //handleResult(task)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "El inicio de sesión de Google falló", Toast.LENGTH_SHORT).show()
                signOut(auth,googleSignInClient)
                visibles()
                Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
                // ...
            }
        }
    }

    fun signOut(auth: FirebaseAuth, client: GoogleSignInClient) {
        auth.signOut()
        client.signOut()
    }

    private fun handleResult(completeTask: Task<GoogleSignInAccount>){
        try {
            //condiciones if
            //  if (completeTask.isSuccessful) {
            val account: GoogleSignInAccount? = completeTask.getResult(ApiException::class.java)

            firebaseAuthWithGoogle(account!!)
            //  }else{
            // }

        }catch (e: ApiException){
            Toast.makeText(this, "El inicio de sesión de Google falló", Toast.LENGTH_SHORT).show()
            visibles()
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
        }catch (e: FirebaseAuthUserCollisionException){

        }


    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        //traemos las credenciales de los usuarios de google
        try {

            val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)

            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    //guarda el ususario si este no existe  en ususarios finales

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth?.currentUser
                        //verifacamos si este usuario ya esta logeado
                        auth?.currentUser
                      //  cusuarios.usuarios(auth)
                        updateUi(user)

                    } else {

                        visibles()
                        //updateUI(null)
                    }

                }

        }catch (e: FirebaseAuthException){
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
        }

    }

    private  fun updateUi(account: FirebaseUser?){
        //val dispTxt=findViewById<View>(R.id.cta) as TextView

        val nombre = account?.displayName
        val email= account?.email
        val telefono= account?.phoneNumber
        val id = account?.uid
        val proveedor :String?=account?.providerId


        correoGo=email.toString().trim()
        idGoo=id.toString().trim()
        nombreGoo=nombre.toString().trim()
        prove=proveedor.toString()

//        var intent: Intent = Intent(this,Principal::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
        //Toast.makeText(baseContext, "error 12", Toast.LENGTH_SHORT).show()
        bienvenido()
        // userExiste()
       // guardar()

    }

    //mantinene a salvo de la ofuscación al compilar con R8
    @Keep
    private fun guardar(){
        //fecha y hora del sistema
        var currentDate: Date = Date(Calendar.getInstance().timeInMillis)
        // muy importante el orden  con la clase de datos
        val key = dbReference.push().key

        val regisUsu = usuariosFB(
            correoUsu = correoGo!!.trim(),
            nombreUsu = nombreGoo,
            telefonoUsu = telefonoGoo,
            idUsu = nombreGoo + currentDate,
            proveedores = prove,
            idGoogle = idGoo,
            fecha = currentDate.toString()
        )
        //guarda los usuarios

        dbReference = dataBase.reference.child("Usuarios/ClienteApp/Google")
        //se guardara con la referencias  de google para mandar a llamar  con mayor facilidad

        dbReference.child(idGoo.trim()).setValue(regisUsu).addOnCompleteListener{
            // Toast.makeText(baseContext, "registro exitoso", Toast.LENGTH_SHORT).show()

        }

        var intent: Intent = Intent(this,login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        //Toast.makeText(baseContext, "error 12", Toast.LENGTH_SHORT).show()
        finish()
    }

    //mantinene a salvo de la ofuscación al compilar con R8
    @Keep
    private data class usuariosFB(
        var correoUsu:String?="",
        var nombreUsu:String?="",
        var idUsu:String?="",
        var proveedores:String?="",
        var telefonoUsu:String?="",
        val idGoogle:String?="",
        var fecha:String?=""
    )

    private fun permisos() {
        //ver si el permiso esta dado
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //pide el permiso
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST)

        } else {
            return
        }

    }
    ///llamada a la funcion permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //se dio el permiso...
                Toast.makeText(this, "Permiso consedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Es nesesario, tu Ubicación, para poder ayudarte", Toast.LENGTH_SHORT).show()
                return permisos() // permission denied

            }
        }
    }
    private fun olvidarPass() {
        var intent= Intent(this,forgotPassword::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP/ Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun loginUser() {
        conexInternet()
        if (conex==1){

            valida()
        }
        conex=0
    }

    private fun clean(){
        etPas.setText("")
        etUser.setText("")
        visibles()
        etUser.requestFocus()
    }


    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    fun conexInternet() {
        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networInfo = cm.activeNetworkInfo

        if (networInfo != null && networInfo.isConnected == true) {

            conex = 1
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Helping Us")
            builder.setMessage("Lo sentimos, conexción a interet a fallado. Verifique la red e intente otra vez")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                var intent= Intent(this, sinAcceso::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP/ Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent.putExtra("cuenta",etUser.text.toString()))
            }

            builder.show()
            //oast.makeText(baseContext,"No se ha podido enviar tu solicitud, verifica tu conexión de internet", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validarTokenEmail(){
        val usua:String = etUser.text.toString()

        //METODO PARA VERIFICAR SI el usuario ya verifico su cuenta de correo
        if (auth.currentUser!!.isEmailVerified){


        }
        else {
            Toast.makeText(this,"No se ha verificado el correo electronico, verifiquelo para poder iniciar sesión", Toast.LENGTH_LONG).show()
            clean()
            visibles()
            // auth.currentUser==null
        }

    }

    private fun valida(){
        val user:String = etUser.text.toString()
        val pass:String = etPas.text.toString()

        if(!TextUtils.isEmpty(user)){
            if (!TextUtils.isEmpty(pass)){
                if(pass.length>5){
                    invisibles()
                    //window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) //dasactiva el touch de la pantalla

                    ///auth.sendSignInLinkToEmail(user, actionCodeSettings)
                    auth.signInWithEmailAndPassword(user,pass)
                        .addOnCompleteListener(this){
                                Task ->

                            if(Task.isSuccessful){
                                //verificamos el token de  correo electronico
                                var userEmail=auth?.currentUser
                                userEmail?.displayName.toString()
                                userEmail?.email
                                userEmail?.isEmailVerified

                                if ( userEmail?.isEmailVerified==true){

                                    bienvenido()
                                }
                                else{
                                    Toast.makeText(this,"No se ha verificado su cuenta de correo para inicio de sesión", Toast.LENGTH_LONG).show()
                                    clean()
                                    visibles()
                                }

                            }
                            if(!Task.isSuccessful){
                                Toast.makeText(this,"Error usuario o contraseña invalidos", Toast.LENGTH_SHORT).show()
                                visibles()
                                clean()
                            }
                        }

                }else{
                    etPas.setText("")
                    etPas.error="Ingrese una contraseña valida "
                    etPas.requestFocus()
                    visibles()
                }

            }else{
                // etPas.error="Ingrese contraseña"
                etPas.requestFocus()
                Toast.makeText(this, "Debe ingresar tu contraseña", Toast.LENGTH_LONG).show()
                visibles()
            }
        }else{

            //etUser.error="Ingrese usuario"
            etUser.requestFocus()
            Toast.makeText(this, "Debe ingresar tu usuario", Toast.LENGTH_LONG).show()
            visibles()
        }
    }

    private fun bienvenido () {
        if (auth != null){
            //muestra el mensaje en pantalla del nombre del usuario o correo
            var nombre =auth.currentUser?.displayName

            if (nombre == null || nombre == ""){
                var correo = auth.currentUser?.email
                msjUsuario= correo.toString()

            }else{
                msjUsuario=nombre.toString()
            }
        }

        //mensaje para el usuario
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Helping Us")

        //builder.setIcon(Drawable.createFromPath("@drawable/helpinlogos"))
        builder.setMessage("\n Bienvenido ${msjUsuario}\n a nuestra app de multiples servicios, ahora podras registrar proveedores")
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            //acceder
            var intent= Intent(this, Categoria::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP/ Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
            // startActivity(intent.putExtra("cuenta",etUser.text.toString()))
            startActivity(intent)

            // var usu=Usuarios(usuario = etUser.text.toString(), id = etPas.text.toString())

            clean()
            finish()

        }
        builder.show()
        return
    }

}
