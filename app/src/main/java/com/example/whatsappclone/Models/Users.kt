package com.example.whatsappclone.Models

class Users {

    var profilePic: String = ""
    var userName: String
    var mail: String
    var password: String
    var userId: String = ""
    var lastMessage: String = ""


    constructor(
        userName: String,
        mail: String,
        password: String,
    ) {
        this.userName = userName
        this.mail = mail
        this.password = password
    }
}