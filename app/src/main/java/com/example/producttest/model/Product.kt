package com.example.producttest.model

import java.io.Serializable

class Product : Serializable {
    var id: String? = null
    var name: String? = null
    var price = 0
    var count = 0
    var des: String? = null
    lateinit var image: ByteArray

    constructor() {}
    constructor(name: String?, price: Int, des: String?) {
        this.name = name
        this.price = price
        this.des = des
    }

    constructor(id: String?, name: String?, price: Int, des: String?) {
        this.id = id
        this.name = name
        this.price = price
        this.des = des
    }

    constructor(name: String?, price: Int, count: Int, des: String?) {
        this.name = name
        this.price = price
        this.count = count
        this.des = des
    }

    constructor(id: String?, name: String?, price: Int, count: Int, des: String?) {
        this.id = id
        this.name = name
        this.price = price
        this.count = count
        this.des = des
    }
}