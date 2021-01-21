package com.example.producttest.model

class Cart {
    var total = 0
    var time: String? = null

    constructor(total: Int, time: String?) {
        this.total = total
        this.time = time
    }

    constructor() {}
}