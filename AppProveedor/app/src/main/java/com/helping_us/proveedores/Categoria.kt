package com.helping_us.proveedores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_motoclicleta.*
import java.lang.ref.PhantomReference


class Categoria : AppCompatActivity() {
    private lateinit var imagen:ImageView
    private lateinit var categorias:Spinner
    private lateinit var enviar : Button
    private lateinit var validar:Button
    private lateinit var  cerrar:Button

    //***********************************************checkbox**************************
    //*--------------------------------------------mecanica
    private lateinit var tmecanica:TableLayout
    private lateinit var mecanica:CheckBox
    private lateinit var solda:CheckBox
    private lateinit var torno:CheckBox
    private lateinit var balanceo:CheckBox
    private lateinit var pintura:CheckBox
    private lateinit var llantera:CheckBox
    private lateinit var radiadores:CheckBox
    private lateinit var fricciones:CheckBox
    private lateinit var electricidad:CheckBox
    private lateinit var stiker:CheckBox
    private lateinit var polarizad:CheckBox
    //--------------------------------------------moto---------
    private lateinit var tmotos:TableLayout
    private lateinit var mecaM:CheckBox
    private lateinit var llanteM:CheckBox
    private lateinit var electriM:CheckBox
    private lateinit var otroM:CheckBox
    //---------------------------------------------soldadura/********************
    private lateinit var tsolda:TableLayout
    private lateinit var industrial:CheckBox
    private lateinit var agricola:CheckBox
    private lateinit var balconeria:CheckBox
    private lateinit var autogena:CheckBox
    //*************************************************Hogar----------------------
    private lateinit var thogar:TableLayout
    private lateinit var elctriciH:CheckBox
    private lateinit var cerra:CheckBox
    private lateinit var fontaneria:CheckBox
    private lateinit var electrodomes:CheckBox
    private lateinit var limpieza:CheckBox
    private lateinit var fumigacion:CheckBox
    private lateinit var estilista:CheckBox
    private lateinit var ninera:CheckBox
    private lateinit var zapateria:CheckBox
    private lateinit var jardineria:CheckBox
    //*************************************************developer********************
    private lateinit var tdeveloper:TableLayout
    private lateinit var escritorio:CheckBox
    private lateinit var web:CheckBox
    private lateinit var movil:CheckBox
    //-----------------------------------------*********servicios informaticos-------------
    private lateinit var tInformatica:TableLayout
    private lateinit var tecnico:CheckBox
    private lateinit var camaras:CheckBox
    private lateinit var audio:CheckBox
    private lateinit var redes:CheckBox
    //-----------------------------------------*********servicios educacion-------------
    private lateinit var tEduca:TableLayout
    private lateinit var capasita:CheckBox
    private lateinit var cursos:CheckBox
    private lateinit var maestro:CheckBox
    private lateinit var otrosEdu:CheckBox
    //------------------------------------------diseño y publicidad/////////////////
    private lateinit var tdiseno:TableLayout
    private  lateinit var diseno:CheckBox
    private lateinit var publici:CheckBox
    //------------------------------------mandadito---------------------------
    private lateinit var tmanda:TableLayout
    private  lateinit var moto:CheckBox
    private lateinit var carro:CheckBox
    //------------------------------------transorte---------------------------
    private lateinit var tTransporte:TableLayout
    private  lateinit var bus:CheckBox
    private lateinit var camion:CheckBox
    private  lateinit var taxi:CheckBox
    private lateinit var mudanza:CheckBox
    private  lateinit var grua:CheckBox
    private  lateinit var otrosTrans:CheckBox
    //------------------------------------salud---------------------------
    private lateinit var tsalud:TableLayout
    private  lateinit var medico:CheckBox
    private lateinit var enfermera:CheckBox
    private  lateinit var examen:CheckBox
    private lateinit var ambulancia:CheckBox
    private  lateinit var veterinaria:CheckBox
    //------------------------------------eventos---------------------------
    private lateinit var tEvento:TableLayout
    private  lateinit var disco:CheckBox
    private lateinit var animacion:CheckBox
    private  lateinit var decoracion:CheckBox
    private lateinit var fotografo:CheckBox
    //------------------------------------salud---------------------------
    private lateinit var tConstruccion:TableLayout
    private  lateinit var ladrillo:CheckBox
    private lateinit var bloque:CheckBox
    private  lateinit var albanil:CheckBox
    private lateinit var tablaYeso:CheckBox
    //**********************comida******************************
    private lateinit var tcomida:TableLayout
    private  lateinit var rapida:CheckBox
    private lateinit var sopa:CheckBox
    private  lateinit var marinera:CheckBox
    private lateinit var almuerzo:CheckBox
    private  lateinit var reposteria:CheckBox
    private lateinit var golosinas:CheckBox


    ///--------------------------------------------barra de progreso-----------
    private lateinit var barra:ProgressBar

    //******************************************variables -------------------------
    var conex=0
    //obtine los valores del key
    var key:String?=null

    private var spiner:String=""
//    var arrayCat = arrayListOf<String>()
    var arrayCat= ArrayList<String>()

    private lateinit var dbRef: DatabaseReference
    private lateinit var dataB: FirebaseDatabase

    private var authStateLis:FirebaseAuth.AuthStateListener? = null
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motoclicleta)
        supportActionBar?.hide()

        categorias=findViewById(R.id.spCat)
        enviar=findViewById(R.id.btnEnviar)
        validar=findViewById(R.id.btnValidar)
       // imagen=findViewById(R.id.img2)

        //*****************************mecanica chekbox--------------------
        tmecanica=findViewById(R.id.tbMeca)
        mecanica=findViewById(R.id.chMeca)
        solda=findViewById(R.id.chSolda)
        torno=findViewById(R.id.chTorno)
        balanceo=findViewById(R.id.chBalanc)
        pintura=findViewById(R.id.chPint)
        llantera=findViewById(R.id.chLlantera)
        radiadores=findViewById(R.id.chRad)
        fricciones=findViewById(R.id.chFric)
        electricidad=findViewById(R.id.chElect)
        polarizad=findViewById(R.id.chPolari)
        stiker=findViewById(R.id.chStiker)
        //-------------------------------------motos-----------------------
        tmotos=findViewById(R.id.tbMotos)
        mecaM=findViewById(R.id.chMecaM)
        llanteM=findViewById(R.id.chLlanteraM)
        electriM=findViewById(R.id.chElecM)
        otroM=findViewById(R.id.chOtrosM)
        ///------------------------------------soldadura--------------
        tsolda=findViewById(R.id.tbSolda)
        industrial=findViewById(R.id.chSolIndus)
        agricola=findViewById(R.id.chSolAgri)
        autogena=findViewById(R.id.chAutoge)
        balconeria=findViewById(R.id.chBalconeria)
        ///----------------------------------------hogar----------------------------
        thogar=findViewById(R.id.tbHogar)
        elctriciH=findViewById(R.id.chElectri)
        cerra=findViewById(R.id.chCerrajeria)
        fontaneria=findViewById(R.id.chFontaneria)
        electrodomes=findViewById(R.id.chElectrod)
        limpieza=findViewById(R.id.chLimpieza)
        fumigacion=findViewById(R.id.chFumiga)
        estilista=findViewById(R.id.chEstilis)
        ninera=findViewById(R.id.chNinera)
        zapateria=findViewById(R.id.chZapa)
        jardineria=findViewById(R.id.chPolari)
        //-------------------------------developer-----------------********************
        tdeveloper=findViewById(R.id.tbDeveloper)
        escritorio=findViewById(R.id.chEscritorio)
        web=findViewById(R.id.chWeb)
        movil=findViewById(R.id.chMovil)

        //---------------------------------serv. informatica-----------------------**
        tInformatica=findViewById(R.id.tbInforma)
        tecnico=findViewById(R.id.chTecni)
        camaras=findViewById(R.id.chCamaras)
        audio=findViewById(R.id.chAudio)
        redes=findViewById(R.id.chRedes)
        //---------------------------------serv. educativa-----------------------**
        tEduca=findViewById(R.id.tbEducacion)
        capasita=findViewById(R.id.chCapasita)
        cursos=findViewById(R.id.chCursos)
        maestro=findViewById(R.id.chMaestro)
        otrosEdu=findViewById(R.id.chOtrosEdu)
        //----------------------------------------diseno y publicidad-**-*----------**-
        tdiseno=findViewById(R.id.tbDiseno)
        diseno=findViewById(R.id.chDiseno)
        publici=findViewById(R.id.chPublicidad)
        //---------------------------------------- Mandadito**-*----------**-
        tmanda=findViewById(R.id.tbMandadito)
        moto=findViewById(R.id.chMotoMan)
        carro=findViewById(R.id.chAutoM)
        //---------------------------------  Transportes-----------------------**
        tTransporte=findViewById(R.id.tbTransporte)
        taxi=findViewById(R.id.chTaxi)
        camion=findViewById(R.id.chCamiones)
        bus=findViewById(R.id.chBuses)
        grua=findViewById(R.id.chGrua)
        mudanza=findViewById(R.id.chMudanza)
        otrosTrans=findViewById(R.id.chOtrosTran)
        //---------------------------------  salud -----------------------**
        tsalud=findViewById(R.id.tbSalud)
        medico=findViewById(R.id.chMedico)
        enfermera=findViewById(R.id.chEnfermera)
        examen=findViewById(R.id.chExamenes)
        ambulancia=findViewById(R.id.chAmbulan)
        veterinaria=findViewById(R.id.chVeteri)
        //---------------------------------  eventos -----------------------**
        tEvento=findViewById(R.id.tbEvento)
        disco=findViewById(R.id.chDisco)
        animacion=findViewById(R.id.chAnimacion)
        decoracion=findViewById(R.id.chDecoracion)
        fotografo=findViewById(R.id.chFotografo)
        //---------------------------------  construccion -----------------------**
        tConstruccion=findViewById(R.id.tbConstruc)
        ladrillo=findViewById(R.id.chLadrillo)
        bloque=findViewById(R.id.chBloque)
        albanil=findViewById(R.id.chAlbanil)
        tablaYeso=findViewById(R.id.chTabla)
        //***************************************comidas/////
        tcomida=findViewById(R.id.tbcomidas)
        sopa=findViewById(R.id.chsopas)
        almuerzo=findViewById(R.id.chAlmuerzo)
        marinera=findViewById(R.id.chmarinera)
        reposteria=findViewById(R.id.chReposteria)
        golosinas=findViewById(R.id.chGolo)
        rapida=findViewById(R.id.chRad)

        barra=findViewById(R.id.pb1)

        cerrar=findViewById(R.id.btnCerrar)
        cerrar.setOnClickListener { cerrarS() }
        enviar.setOnClickListener { enviarDat() }
        validar.setOnClickListener { valida() }
        auth = FirebaseAuth.getInstance()

        var arrayCateg = arrayListOf<String>("Categorias:","Automotriz","Motocicleta","Soldadura","Hogar","Comida","Desarrollo de software",
            "Servicios informáticos","Servicios educación","Servicios medicos","Diseño y publicidad","Mandaditos","Transporte","Eventos",
            "Vidreria","Construcción","Abogado","Chofer")

        var adacterCat: ArrayAdapter<String> = ArrayAdapter(this, R.layout.sp_item_cate,arrayCateg)
        categorias.adapter=adacterCat


//        authStateLis= FirebaseAuth.AuthStateListener { firebaseAuth ->
//            Thread.sleep(1000)
//            val oUser:FirebaseUser? =auth.currentUser
//            if (oUser != null) {
//
//                Toast.makeText(baseContext, " user  ${oUser}", Toast.LENGTH_LONG).show()
//                return@AuthStateListener
//
//            } else {
//                Toast.makeText(this,  "usuario:  $oUser", Toast.LENGTH_LONG).show()
//
//                var intent:Intent= Intent(this,login::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//                auth.currentUser
//                auth.signOut()
//                finish()
//
//            }
//        }
        val keyInten:Intent=intent
        key= keyInten.getStringExtra("key")

    }

    override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener { authStateLis.toString() }
        var usu = auth.currentUser

        if (usu!=null){
           // Toast.makeText(this,  "usuario:  $usu", Toast.LENGTH_LONG).show()

        }else{
            var intent:Intent= Intent(this,login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            auth.currentUser
            auth.signOut()
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        enviar.visibility=View.GONE
      //  Toast.makeText(this,"${auth.currentUser?.displayName}", Toast.LENGTH_SHORT).show()
    }

    private fun enviarDat() {

        conexInternet()
        if(conex==1) {

            val valor = categorias.selectedItem.toString()

            verificarCheck()
          //  Toast.makeText(this,"${arrayCat.size}, ${arrayCat.joinToString()}", Toast.LENGTH_SHORT).show()

            arrayCat.add(valor)

            var int:Intent= Intent(this, MainActivity::class.java)
            int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(int.putExtra("categoria",arrayCat))
            finish()
            arrayCat.clear()
        }else{
            Toast.makeText(this,"No hay acceso a Internet, verifica tus datos o wifi",Toast.LENGTH_SHORT).show()
        }
        conex=0
    }
    fun cerrarS(){

        var int:Intent= Intent(this, Principal ::class.java)
        int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(int)
//        auth.currentUser
//        auth.signOut()
        finish()


    }

    fun noCheck(){
        val dato = categorias.selectedItem.toString()

        arrayCat.add(dato)

        var int:Intent= Intent(this, MainActivity::class.java)
        int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP / Intent.FLAG_ACTIVITY_CLEAR_TASK/ Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(int.putExtra("categoria",arrayCat))
        finish()
        arrayCat.clear()
    }

    private fun valida(){
        spiner = categorias.selectedItem.toString().trim()


        if (spiner=="Categorias:"){
            Toast.makeText(this,"Debes elegir una categoria para continuar",Toast.LENGTH_SHORT).show()
            enviar.visibility= View.GONE
        }else{
            cerrar.visibility=View.GONE
            invisibles()
            //----------------------------------------visibles los tablelayaut*******************
            if (spiner=="Automotriz"){
                enviar.visibility= View.VISIBLE
                tmecanica.visibility=View.VISIBLE
            }
            if(spiner=="Motocicleta"){
                enviar.visibility= View.VISIBLE
                tmotos.visibility=View.VISIBLE
            }
            if(spiner=="Soldadura"){
                enviar.visibility= View.VISIBLE
                tsolda.visibility=View.VISIBLE
            }
            if(spiner=="Hogar"){
                enviar.visibility= View.VISIBLE
                thogar.visibility=View.VISIBLE
            }
            if(spiner=="Comida"){
                enviar.visibility= View.VISIBLE
                tcomida.visibility=View.VISIBLE
            }
            if(spiner=="Desarrollo de software"){
                enviar.visibility= View.VISIBLE
                tdeveloper.visibility=View.VISIBLE
            }
            if(spiner=="Servicios informáticos"){
                enviar.visibility= View.VISIBLE
                tInformatica.visibility=View.VISIBLE
            }
            if(spiner=="Servicios educación"){
                enviar.visibility= View.VISIBLE
                tEduca.visibility=View.VISIBLE
            }
            if(spiner=="Servicios medicos"){
                enviar.visibility= View.VISIBLE
                tsalud.visibility=View.VISIBLE
            }
            if(spiner== "Diseño y publicidad"){
                enviar.visibility= View.VISIBLE
                tdiseno.visibility=View.VISIBLE
            }
            if(spiner== "Mandaditos"){
                enviar.visibility= View.VISIBLE
                tmanda.visibility=View.VISIBLE
            }
            if(spiner== "Transporte"){
                enviar.visibility= View.VISIBLE
                tTransporte.visibility=View.VISIBLE
            }
            if(spiner== "Eventos"){
                enviar.visibility= View.VISIBLE
                tEvento.visibility=View.VISIBLE
            }
            if(spiner == "Construcción"){
                enviar.visibility= View.VISIBLE
                tConstruccion.visibility=View.VISIBLE
            }
            if(spiner==  "Vidreria"){
                conexInternet()
                if(conex==1) {

                    invisibles()
                    Thread.sleep(2000)
                    pb1.visibility=View.VISIBLE
                    vidreria()
                }else{
                    Toast.makeText(this,"No hay acceso a Internet, verifica tus datos o wifi",Toast.LENGTH_SHORT).show()
                }
            }
            if(spiner== "Abogado"){
                conexInternet()
                if(conex==1) {

                    invisibles()
                    Thread.sleep(2000)
                    pb1.visibility=View.VISIBLE
                    abogado()
                }else{
                    Toast.makeText(this,"No hay acceso a Internet, verifica tus datos o wifi",Toast.LENGTH_SHORT).show()
                }
            }
            if(spiner == "Chofer"){
                conexInternet()

                if(conex==1) {
                    invisibles()
                    Thread.sleep(2000)
                    pb1.visibility=View.VISIBLE
                    chofer()
                }else{
                    Toast.makeText(this,"No hay acceso a Internet, verifica tus datos o wifi",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verificarCheck() {
        spiner = categorias.selectedItem.toString().trim()

        if (spiner!="Categorias:"){

            if (spiner=="Automotriz"){
                //tmecanica.visibility=View.VISIBLE
                mecanica()
            }
            if (spiner=="Motocicleta"){
              //  tmecanica.visibility=View.VISIBLE
               motos()
            }
            if(spiner=="Soldadura"){
                //tsolda.visibility=View.VISIBLE
                soldadura()
            }
            if(spiner=="Hogar"){
               // thogar.visibility=View.VISIBLE
                hogar()
            }
            if(spiner=="Comida"){
                // thogar.visibility=View.VISIBLE
                comida()
            }
            if(spiner=="Desarrollo de software"){
                developer()
            }
            if(spiner=="Servicios informáticos"){
                informatica()
            }
            if(spiner=="Servicios educación"){
                educacion()
            }
            if(spiner=="Servicios medicos"){
                salud()
            }
            if(spiner== "Diseño y publicidad"){
               disenoYpubli()
            }
            if(spiner== "Mandaditos"){
               mandado()
            }
            if(spiner== "Transporte"){
                transporte()
            }
            if(spiner== "Eventos"){
                evento()
            }
            if(spiner == "Construcción"){
                Construccion()
            }
        }
    }

    private fun visibles(){
        tmecanica.visibility=View.VISIBLE
        tmotos.visibility=View.VISIBLE
        tsolda.visibility=View.VISIBLE
        thogar.visibility=View.VISIBLE
        tdeveloper.visibility=View.VISIBLE
        tInformatica.visibility=View.VISIBLE
        tEduca.visibility=View.VISIBLE
        tsalud.visibility=View.VISIBLE
        tdiseno.visibility=View.VISIBLE
        tmanda.visibility=View.VISIBLE
        tTransporte.visibility=View.VISIBLE
        tEvento.visibility=View.VISIBLE
        tConstruccion.visibility=View.VISIBLE
        tcomida.visibility=View.VISIBLE

    }
    private fun invisibles(){
        tmecanica.visibility=View.GONE
        tmotos.visibility=View.GONE
        tsolda.visibility=View.GONE
        thogar.visibility=View.GONE
        tdeveloper.visibility=View.GONE
        tInformatica.visibility=View.GONE
        tEduca.visibility=View.GONE
        tsalud.visibility=View.GONE
        tdiseno.visibility=View.GONE
        tmanda.visibility=View.GONE
        tTransporte.visibility=View.GONE
        tEvento.visibility=View.GONE
        tConstruccion.visibility=View.GONE
        tcomida.visibility=View.GONE
//        enviar.visibility= View.VISIBLE
    }
//--------
//--------------------------------------------  funciones de las categorias para saber las subcatgorias ha elegido¿''''''''''''''

    private fun chofer() {
        arrayCat.add("Chofer Asignado")
        noCheck()
    }
    private fun vidreria() {
        arrayCat.add("Vidreria")
        noCheck()
    }
    private fun abogado() {
        arrayCat.add("Abogado")
        noCheck()
    }
    private fun Construccion(){
        if (ladrillo.isChecked){
            arrayCat.add("Ladrillo")
        }
        if (bloque.isChecked){
            arrayCat.add("Bloque")
        }
        if (albanil.isChecked){
            arrayCat.add("Albañil")
        }
        if (tablaYeso.isChecked){
            arrayCat.add("Tabla Yeso")
        }
    }

    private fun evento(){
        if (disco.isChecked){
            arrayCat.add("Disco movil")
        }
        if (animacion.isChecked){
            arrayCat.add("Animación")
        }
        if (decoracion.isChecked){
            arrayCat.add("Decoración")
        }
        if (fotografo.isChecked){
            arrayCat.add("Fotografia")
        }
    }

    private fun salud(){
        if (medico.isChecked){
            arrayCat.add("Medico General")
        }
        if (enfermera.isChecked){
            arrayCat.add("Enfermera")
        }
        if (examen.isChecked){
            arrayCat.add("Examenes Laboratorio")
        }
        if (ambulancia.isChecked){
            arrayCat.add("Ambulancia")
        }
        if (veterinaria.isChecked){
            arrayCat.add("Veterinaria")
        }
    }
  private fun transporte(){
      if (taxi.isChecked){
          arrayCat.add("Taxis")
      }
      if (camion.isChecked){
          arrayCat.add("Camiones")
      }
      if (bus.isChecked){
          arrayCat.add("Buses")
      }
      if (grua.isChecked){
          arrayCat.add("Gruas")
      }
      if (mudanza.isChecked){
          arrayCat.add("Mudanzas")
      }
      if (otrosTrans.isChecked){
          arrayCat.add("Otros Transporte")
      }
  }
    private fun mandado() {
        if (moto.isChecked){
            arrayCat.add("Motocicleta")
        }
        if (carro.isChecked){
            arrayCat.add("Automovil")
        }
    }

    private fun disenoYpubli() {
        if (diseno.isChecked){
            arrayCat.add("Diseño grafico")
        }
        if (publici.isChecked){
            arrayCat.add("Publicidad")
        }
    }
    private  fun  educacion(){
        if (capasita.isChecked){
            arrayCat.add("Capasitación")
        }
        if (cursos.isChecked){
            arrayCat.add("Cursos")
        }
        if (maestro.isChecked){
            arrayCat.add("Maestro en casa")
        }
        if (otrosEdu.isChecked){
            arrayCat.add("Otros de educación")
        }

    }
    private fun informatica(){
        if (tecnico.isChecked){
            arrayCat.add("Servicio Técnico")
        }
        if (camaras.isChecked){
            arrayCat.add("Camaras")
        }
        if (audio.isChecked){
            arrayCat.add("Audio y Video")
        }
        if (redes.isChecked){
            arrayCat.add("Redes")
        }
    }
    private fun developer(){
        if (escritorio.isChecked){
            arrayCat.add("App_Escritorio")
        }
        if (web.isChecked){
            arrayCat.add("App_Web")
        }
        if (movil.isChecked){
            arrayCat.add("App_Movil")
        }

    }

    private fun hogar(){
        if (elctriciH.isChecked){
            arrayCat.add("Electricidad")
        }
        if (cerra.isChecked){
            arrayCat.add("Cerrajeria")
        }
        if (fontaneria.isChecked){
            arrayCat.add("Fontaneria")
        }
        if (electrodomes.isChecked){
            arrayCat.add("Reparación")
        }
        if (limpieza.isChecked){
            arrayCat.add("Limpieza")
        }
        if (fumigacion.isChecked){
            arrayCat.add("Fumigación")
        }
        if (estilista.isChecked){
            arrayCat.add("Estilista")
        }
        if (ninera.isChecked){
            arrayCat.add("Niñera")
        }
        if (zapateria.isChecked){
            arrayCat.add("Zapateria")
        }
        if (jardineria.isChecked){
            arrayCat.add("Jardineria")
        }

    return
    }
    private fun comida(){
        if (rapida.isChecked){
            arrayCat.add("Comida rápida")
        }
        if (sopa.isChecked){
            arrayCat.add("Sopas")
        }
        if (golosinas.isChecked){
            arrayCat.add("Golosinas")
        }
        if (reposteria.isChecked){
            arrayCat.add("Reposteria")
        }
        if (marinera.isChecked){
            arrayCat.add("Comida marinera")
        }
        if (almuerzo.isChecked){
            arrayCat.add("Almuerzo")
        }
        return
    }

    private fun soldadura(){
        if (industrial.isChecked){
            arrayCat.add("Industrial")
        }
        if (agricola.isChecked){
            arrayCat.add("Agrícola")
        }
        if (autogena.isChecked){
            arrayCat.add("Autogena")
        }
        if (balconeria.isChecked){
            arrayCat.add("Balconeria")
        }
    }

    private fun motos(){
        if (mecaM.isChecked){
            arrayCat.add("Mecanica")
        }
        if (llanteM.isChecked){
            arrayCat.add("Llantera")
        }
        if (electriM.isChecked){
            arrayCat.add("Electricidad")
        }
        if (otroM.isChecked){
            arrayCat.add("Otros Motocicleta")
        }
    }
    private fun mecanica(){
        if (mecanica.isChecked){
            arrayCat.add("Mecanica")
        }
        if (solda.isChecked){
            arrayCat.add("Soldadura")
        }
        if (torno.isChecked){
            arrayCat.add("Torno")
        }
        if (balanceo.isChecked){
            arrayCat.add("Balanceo")
        }
        if (pintura.isChecked){
            arrayCat.add("Pintura")
        }
        if (llantera.isChecked){
            arrayCat.add("Llantera")
        }
        if (radiadores.isChecked){
            arrayCat.add("Radiadores")
        }
        if (fricciones.isChecked){
            arrayCat.add("Fricciones")
        }
        if (electricidad.isChecked){
            arrayCat.add("Electricidad")
        }
        if (stiker.isChecked){
            arrayCat.add("Stiker")
        }
        if (polarizad.isChecked){
            arrayCat.add("Polarizado")
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
            builder.setMessage("Lo sentimos, conexión a internet a fallado!")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                finish()
            }

            builder.show()

        }
    }
}


