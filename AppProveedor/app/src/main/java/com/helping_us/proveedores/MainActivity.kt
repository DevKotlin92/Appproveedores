package com.helping_us.proveedores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.modelo
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Spinner as Spinner

class MainActivity : AppCompatActivity() {

    private lateinit var CyS:TextView
    private lateinit var usu:TextView

    private lateinit var datosP:LinearLayoutCompat
    private lateinit var datosE:LinearLayoutCompat
    private lateinit var datosV:LinearLayoutCompat

    private lateinit var nomreP:EditText
    private lateinit var cell:EditText
    private lateinit var id:EditText
    private lateinit var direcPersona:EditText

    private lateinit var placa:EditText
    private lateinit var colore: EditText
    private lateinit var marca:EditText

    private lateinit var rtn:EditText
    private lateinit var direccion:EditText
    private lateinit var telefono:EditText
    private lateinit var nombreEmp : EditText
    private lateinit var jornada: Spinner
    private lateinit var horario: Spinner
    private lateinit var pb : ProgressBar


    private lateinit var envia:Button

    private lateinit var auth:FirebaseAuth
    private lateinit var dataB:FirebaseDatabase
    private lateinit var dbRefe:DatabaseReference
    private lateinit var url:String


    //----------------------------------------------variables-----------------
    val servicios1:modelo = modelo()
    var arrayCategorias = ArrayList<String>()
    var recorido: List<String> = listOf()
    var rtn2: Int=0

    var usuario:String?=null
    var fecHora:Date?=null
    var delCategoria:String?=null
    var latitude:String?=null
    var longitud:String?=null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var MY_PERMISSIONS_REQUEST: Int = 0
    var tipo=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        CyS=findViewById(R.id.txtCyS)
        usu=findViewById(R.id.tvUsuario)
        jornada=findViewById(R.id.spJor)
        horario=findViewById(R.id.spHora)
        envia=findViewById(R.id.btnGuardar)
        /////////////////////**********************************linialLayoutompat
        datosE=findViewById(R.id.llcDatosEmpresa)
        datosP=findViewById(R.id.llcDatosPersonales)
        datosV=findViewById(R.id.llcVehiculo)

        pb=findViewById(R.id.pbregistro)

        //--------------------------------------------editext---------------
        nomreP=findViewById(R.id.etNombreEnc)
        cell=findViewById(R.id.etCell)
        id=findViewById(R.id.etNumId)

        marca=findViewById(R.id.etMarca)
        colore=findViewById(R.id.etColor)
        placa= findViewById(R.id.etPlaca)
        direcPersona=findViewById(R.id.etDireccionPer)

        rtn= findViewById(R.id.etRTN)
        telefono= findViewById(R.id.etTelefono)
        nombreEmp=findViewById(R.id.etnombreE)
        direccion=findViewById(R.id.etdireccion)

        url="Proveedores/Categoria/"
        auth=FirebaseAuth.getInstance()
        dataB= FirebaseDatabase.getInstance()
        dbRefe=dataB.reference.child("$url")


        envia.setOnClickListener { enviar() }

        //****************************************************---------------adacter de la seleccion del spiner
        var arrayJor = arrayListOf<String>("Jornadas", "Matutina", "Diurna", "Nocturna", "Todo el día")
        //var adacterJ:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.jornada, android.R.layout.simple_spinner_dropdown_item)
        var adacterJ:ArrayAdapter<String> = ArrayAdapter(this, R.layout.sp_item_jornada, arrayJor)
        jornada.adapter= adacterJ

        var arrayHor = arrayListOf<String>("Horarios", "8:am-5:pm", "8:am-7:pm", "12:pm-5:pm", "12:pm-10:pm", "24 hrs")
//        var adacterH:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.horarios, android.R.layout.simple_spinner_dropdown_item)
        var adacterH:ArrayAdapter<String> = ArrayAdapter(this, R.layout.sp_item_hora, arrayHor)
        horario.adapter= adacterH



        //adacter de la seleccion del spiner

    }

    override fun onStart() {
        super.onStart()

        permisos()
        location()

        datosE.visibility=View.GONE
        datosV.visibility=View.GONE

        if (auth.currentUser != null){
            //muestra el mensaje en pantalla del nombre del usuario o correo
            var nombre =auth.currentUser?.displayName

            if (nombre == null || nombre == ""){
                var correo = auth.currentUser?.email
                usuario= correo.toString()

            }else{
                usuario=nombre.toString()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        alerta()
        //se manda resibe un arraylists el mismo tipo de variables que se envio
        val cateIntent: Intent = intent
         arrayCategorias= cateIntent.getStringArrayListExtra("categoria")
        //---------------------------------------mostraremos el ultimo valor agregado---------------
        var tamano= arrayCategorias.size
        var catNombre =arrayCategorias[tamano-1]

        delCategoria= catNombre.trim()

        val listaNom : List<String> = listOf(catNombre)

//        arrayCategorias.last() --  arrayCategorias.first()----devuelve el ultimo y primer valor

        CyS.text =Html.fromHtml("<font><size>$catNombre</size></font>")

        ///-------------se compara con el valor que tiene la lista en la que se guardo el nombre de la categoria si esta es vacia
//---------no es una categoria guardad en la lista cateEmpresa

        recorido = listaNom.filter{ it=="Automotriz" || it == "Motocicleta" || it=="Soldadura"|| it=="Hogar"||it=="Comida"
                || it=="Desarrollo de software" || it== "Servicios informáticos"|| it=="Servicios medicos" || it=="Diseño y publicidad" || it=="Eventos"
                || it=="Vidreria"|| it=="Construcción"|| it=="Abogado"}

        if (!recorido.none()){

            if (tipo==1){
                //no cuenta la empresa con rtn

                datosE.visibility=View.GONE
            }else{
                datosE.visibility=View.VISIBLE
            }
           // Toast.makeText(this, "Te ayudaremos a crecer tu negocio.", Toast.LENGTH_SHORT).show()
        }else{
            if (catNombre =="Mandaditos"||catNombre=="Transporte"){
                datosV.visibility=View.VISIBLE
            }else{
                datosE.visibility=View.GONE
                datosV.visibility=View.GONE
            }
        }

        url="Proveedores/Categoria/${catNombre}"
       // Toast.makeText(this,"exito ${arrayCategorias.last()}",Toast.LENGTH_SHORT).show()

    }
    fun alerta(){

            //mensaje para el usuario
            val builder = AlertDialog.Builder(this)
            builder.setTitle(Html.fromHtml("<font color='#ff9200'><h>Helping-Us</h></font>"))

            builder.setMessage("\nNesitamos saber si su negocio cuenta con RTN")
            builder.setCancelable(false)

            builder.setPositiveButton("No cuenta") { dialog, which ->
               tipo = 1
                datosE.visibility=View.GONE
            }
            builder.setNegativeButton("Si Cuenta"){ dialog, which ->
                tipo=0
                datosE.visibility=View.VISIBLE
            }
            builder.show()

    }
    private fun enviar(){
        // recorido es una lista de servicios que me mostrara los servicios que pueden ser empresa
        if (!recorido.none()){
            if (tipo==1){
                //no cuenta la empresa con rtn

               editextPer()
            }else{
               editextEmp()
            }

        } else{
            //-------------------------------------mostraremos el ultimo valor agregado---------------
            var tamano= arrayCategorias.size
            var catNombre =arrayCategorias[tamano-1]

            if (catNombre =="Mandaditos"|| catNombre=="Transporte" ){
                editextPer()
            }else{
                datosE.visibility=View.GONE
                editextPer()

            }
        }

    }
    private fun editextEmp(){
        val nomEmpresa= nombreEmp.text.toString().trim()
        val rTn = rtn.text.toString().trim()
        val direccion2 = direccion.text.toString().trim()
        val telephone = telefono.text.toString().trim()

        val jornadas = jornada.selectedItem.toString()
        val horarios= horario.selectedItem.toString()

        if (nomEmpresa.length >= 8){
            if (rTn.length==14){
                if (direccion2.length >= 20){
                    if (telephone.length==8){

                        val n2 = "2"
                        val n3= "3"
                        val n8= "8"
                        val n9= "9 "

                        if (telephone[0]==n2[0] || telephone[0]==n3[0] || telephone[0]==n8[0] || telephone[0]==n9[0] ){

                            if (jornadas!="Jornadas"){
                                if (horarios!="Horarios"){
                                    if (!recorido.none()){
                                        editextPer()
                                    }
                                }else{
                                    Toast.makeText(this,"Debes seleccionar un Horario", Toast.LENGTH_SHORT).show()
                                }

                            }else  {
                                Toast.makeText(this,"Debes seleccionar una Jornada", Toast.LENGTH_SHORT).show()
                            }
                            //****************si esta en la lista de datos personales-----------------------

                        }else{
                            telefono.error="Telefono invalido: $telephone"
                            telefono.setText("")
                            telefono.requestFocus()
                        }

                    }else{
                        telefono.error="Telefono invalido: $telephone"
                        telefono.setText("")
                        telefono.requestFocus()
                    }
                }else{
                    direccion.error="Es necesaria una dirección más exacta"
                    direccion.setText("")
                    direccion.requestFocus()
                }

            }else{
                rtn.error="Número de RTN invalido $rTn"
                rtn.setText("")
                rtn.requestFocus()
            }
        }else{
           nombreEmp.error="el nombre tiene que ser mayor a 8 digitos"
            nombreEmp.setText("")
            nombreEmp.requestFocus()
        }
    }


    private fun editextPer(){
       val nombre=nomreP.text.toString().trim()
        val id2 = id.text.toString().trim()
        val cell2 = cell.text.toString().trim()
       // if (!TextUtils.isEmpty(nombre)){

        if (nombre.length >= 8){
            if (id2.length==13){
                if (cell2.length==8){
                    val n3= "3"
                    val n8= "8"
                    val n9= "9 "
                    cell2[0]
                    if (cell2[0]==n3[0] || cell2[0]==n8[0] || cell2[0]==n9[0] ){
                        //****************si esta en la lista de datos personales-----------------------

                        var tama= arrayCategorias.size
                        var nomCat =arrayCategorias[tama-1]

                        if (!recorido.none()){

                            invisible()
                            guardarEmpre()
                        }else{
                            if (nomCat =="Mandaditos"|| nomCat=="Transporte" ){

                                editextVehi()
                            }else{

                                invisible()
                                guardarPer()
                            }
                        }
                    }else{
                        cell.error="Número de celular invalido: $cell2"
                        cell.setText("")
                        cell.requestFocus()
                    }

                }else{
                    cell.error="Número de celular invalido: $cell2"
                    cell.setText("")
                    cell.requestFocus()
                }

            }else{
                id.error="Número de identidad invalido $id2"
                id.setText("")
                id.requestFocus()
            }
        }else{
            nomreP.error="el nombre tiene que ser mayor a 8 digitos"
            nomreP.setText("")
            nomreP.requestFocus()
        }

    }

    fun editextVehi(){

        val numPlaca=placa.text.toString().trim()
        val colos = colore.text.toString().trim()
        val marca2= marca.text.toString().trim()

        if (numPlaca.length == 7){
            if (colos.length>=4){
                if (marca2.length>=3){
                    invisible()
                    guardaVehi()
                }else{
                    marca.error="Esta marca es invalida: $marca2"
                    marca.setText("")
                    marca.requestFocus()
                }

            }else   {
                colore.error="Color $colos es invalido"
                colore.setText("")
                colore.requestFocus()
            }

        }else{
            placa.error="Número de placa invalido $numPlaca"
            placa.setText("")
            placa.requestFocus()
        }


    }


    fun cerrar(){
        var int: Intent = Intent(this, Categoria::class.java)
        int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(int)
        arrayCategorias.clear()
        finish()
    }

    private fun alertasGuarda () {
        //mensaje para el usuario
        val builder = AlertDialog.Builder(this)
        builder.setTitle(Html.fromHtml("<font color='#ff9200'><h>Helping-Us</h></font>"))

        builder.setMessage("\n Registro guardado con exito ${usuario.toString()}.")
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            cerrar()
        }
        builder.show()

    }
    private fun guardaVehi(){
        fecHora= Date(Calendar.getInstance().timeInMillis)

        val key = dbRefe.push().key
        arrayCategorias.remove("${arrayCategorias.last()}")

        //Toast.makeText(this,"exito ${arrayCategorias.joinToString()}",Toast.LENGTH_SHORT).show()

        val inser = datosVehi (
            fechaHora = fecHora.toString(),
            usuario = usuario.toString(),
            responsable = nomreP.text.toString().trim(),
            identidad = id.text.toString().trim(),
            celular = cell.text.toString().trim(),
            direccion_Encargado = direcPersona.text.toString().trim(),
            servicios = arrayCategorias.joinToString(),
            placa = placa.text.toString(),
            color = colore.text.toString(),
            marca = marca.text.toString(),
            evaluacion = 0,
            promedio = 0,
            nombre_Empresa = nomreP.text.toString().trim() + "-NEP"

        )

        dbRefe=dataB.reference.child("$url")
        dbRefe.child(key.toString()).setValue(inser).addOnCompleteListener {

            alertasGuarda()
            clean()
            arrayCategorias.clear()
        }

    }
    private fun guardarPer() {

        fecHora= Date(Calendar.getInstance().timeInMillis)

        var tama= arrayCategorias.size - 1
        val servicio = arrayCategorias.remove("${arrayCategorias.last()}")
        val key = dbRefe.push().key

        //Toast.makeText(this,"exito ${arrayCategorias.joinToString()}",Toast.LENGTH_SHORT).show()

        val inser = datosPersona (
            fechaHora = fecHora.toString(),
            usuario = usuario.toString(),
            responsable = nomreP.text.toString().trim(),
            identidad = id.text.toString().trim(),
            celular = cell.text.toString().trim(),
            direccion_Encargado = direcPersona.text.toString().trim(),
            servicios = arrayCategorias.joinToString(),
            evaluacion = 0,
            promedio = 0,
            nombre_Empresa = nomreP.toString().trim() + "-NEP"


            )

            dbRefe=dataB.reference.child("$url")
            dbRefe.child(key.toString()).setValue(inser).addOnCompleteListener {

//               **---------------verificar bien los datos para guardar al llamar ala funcion
                alertasGuarda()
                clean()
                arrayCategorias.clear()
            }
            return


    }
    private fun guardarEmpre(){
        fecHora= Date(Calendar.getInstance().timeInMillis)


        val key = dbRefe.push().key
        arrayCategorias.remove("${arrayCategorias.last()}")

        //Toast.makeText(this,"exito ${arrayCategorias.joinToString()}",Toast.LENGTH_SHORT).show()

            var inser = datosEmpresa (
                nombre_Empresa = nombreEmp.text.toString(),
                rtn = rtn.text.toString(),
                direccion_Empresa = direccion.text.toString().trim(),
                telefono = telefono.text.toString().trim(),
                jornada = jornada.selectedItem.toString().trim(),
                horario = horario.selectedItem.toString().trim(),
                latitude =latitude.toString().trim(),
                longitude = longitud.toString().trim(),
                fechaHora = fecHora.toString(),
                usuario = usuario.toString(),
                responsable = nomreP.text.toString().trim(),
                identidad = id.text.toString().trim(),
                celular = cell.text.toString().trim(),
                direccion_Encargado = direcPersona.text.toString().trim(),
                servicios = arrayCategorias.joinToString(),
                evaluacion = 0,
                promedio = 0
            )

        dbRefe=dataB.reference.child("$url")
        dbRefe.child(key.toString()).setValue(inser).addOnCompleteListener {
            alertasGuarda()
            clean()
            arrayCategorias.clear()
        }
            return

    }

    fun visible(){
        datosE.visibility=View.VISIBLE
        datosV.visibility=View.VISIBLE
        datosP.visibility=View.VISIBLE
    }

    fun invisible(){
        datosE.visibility=View.GONE
        datosV.visibility=View.GONE
        datosP.visibility=View.GONE
        envia.visibility=View.GONE

        pb.visibility=View.VISIBLE
    }

    data class datosPersona(
        var fechaHora:String?,
        var usuario:String?,
        var responsable:String?,
        var identidad:String?,
        var celular:String?,
        var direccion_Encargado:String?,
        var servicios: String?,
        var evaluacion:Int?,
        var promedio:Int?,
        var nombre_Empresa: String?
    )
    data class datosEmpresa(
        var rtn:String?,
        var nombre_Empresa:String?,
        var direccion_Empresa:String?,
        var telefono:String?,
        var jornada:String?,
        var horario: String?,
        val latitude: String? = "",
        val longitude: String? = "",
        var fechaHora:String?,
        var usuario:String?,
        var responsable:String?,
        var identidad:String?,
        var celular:String?,
        var direccion_Encargado:String?,
        var servicios: String?,
        var evaluacion:Int?,
        var promedio:Int?
    )
    data class datosVehi(
        var fechaHora:String?,
        var usuario:String?,
        var responsable:String?,
        var identidad:String?,
        var celular:String?,
        var direccion_Encargado:String?,
        var servicios: String?,
        var placa:String?,
        var color:String?,
        var marca:String?,
        var evaluacion:Int?,
        var promedio:Int?,
        var nombre_Empresa: String?
    )
    fun clean(){
        nomreP.setText("")
        cell.setText("")
        id.setText("")
        direcPersona.setText("")
        nombreEmp.setText("")
        direccion.setText("")
        telefono.setText("")
        rtn.setText("")
    }

    //pedir permisos
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @SuppressLint("MissingPermission")
    private fun location() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation

            .addOnSuccessListener { location: Location? ->

                if (location != null) {
                    //llamada asincronas o coroutines
                    // Log.e("Latitud: ", location.latitude.toString() + " Longitud: " + location.longitude)
                    var long2 = location.longitude.toString()
                    var lati2 = location.latitude.toString()

                    runBlocking {
                        //SE ESPERA 3 SEGUNDOS PARA PODER CARGAR LA LOCATION DEL TELEFONO
                        delay(3000)
                        latitude = lati2
                        longitud = long2
                        // Toast.makeText(baseContext, "longitud:, ${longitud}, latitud: $latitud ok", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    alerUbica()
                }
            }
    }

    private fun alerUbica(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(Html.fromHtml("<font color='#ff9200'><h>Helping-Us Proveedores</h></font>"))
        builder.setMessage(Html.fromHtml("Lo sentimos! No podemos obtener tú ubicación, por favor verifica <b>¿sí esta activo?</b>"))
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            activargGPS()
            //location()
        }
        builder.show()
        return
    }

    private fun permisos() {
        //ver si el permiso esta dado por lo cual es distinto de denegado
        //ver si la version de api es mayor a 23 o android 6
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),MY_PERMISSIONS_REQUEST)
                // da una explicación de por qué lo necesita el permiso de ubicacion
//
            }else{
                return
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // se concedió el permiso,
                Toast.makeText(this, "consedido", Toast.LENGTH_SHORT).show()
            } else {
                // permiso denegado, boo! Deshabilitar el
                Toast.makeText(this, "Permiso denegado, su ubicación es necesaria para mejorar el servicio.", Toast.LENGTH_SHORT).show()
//                 PermiDenegado()
            }
        }
        else {
            // Ignore all other requests.
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Runtime.getRuntime().gc()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            salir()

        }
        return super.onKeyDown(keyCode, event)
    }

    private fun activargGPS(){
        //mandamos a encender el GPS
        var locationManager=getSystemService(Context.LOCATION_SERVICE)
        var provider : String? = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        if (provider!!.contains("gps") || provider!!.contains("network")){
            // Toast.makeText(baseContext, "true", Toast.LENGTH_LONG).show()
            return
        }else{
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    fun salir(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(Html.fromHtml("<font color='#ff9200'><h>Helping-Us</h></font>"))
        builder.setMessage(Html.fromHtml("¿Deseas salir?, se <b>eliminaran</b> todos los datos que has llenado ${usuario.toString()}"))
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            invisible()
            var int:Intent= Intent(this, Categoria::class.java)
            int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(int)
            finish()
        }
        builder.setNegativeButton("No"){ dialog, which ->
            dialog.cancel()
        }
        builder.show()
        return
    }

}
