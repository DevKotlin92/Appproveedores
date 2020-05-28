package com

import java.lang.reflect.Array.set
import java.nio.file.Paths.get

open class modelo {
    var cate:String? = String.toString()
    var array:ArrayList<String>?=ArrayList()


    constructor(cate:String?=null, array: ArrayList<String>? = null){
        this.cate = cate
        this.array= array
    }

}