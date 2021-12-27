package com.example.whatsappclone.Models

class MessageModel {
    var uid: String = ""
    var message: String = ""
    var timeStamp: Long = 0L

    constructor() {

    }

    constructor(uid: String, message: String) {
        this.uid = uid
        this.message = message
    }


}