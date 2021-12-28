package com.example.whatsappclone.Models

class Users {

    var profilePic: String = ""
    var userName: String = ""
    var mail: String = ""
    var password: String = ""
    var userId: String = ""
    var lastMessage: String = ""

    constructor() {

    }

    constructor(
        userName: String,
        mail: String,
        password: String,
        uid: String
    ) {
        this.userName = userName
        this.mail = mail
        this.password = password
        this.userId = uid
    }
}