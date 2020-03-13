//
//  AuthInstance.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/11/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Firebase
import FirebaseStorage

class AuthInstance {
    static let instance = AuthInstance()
    var auth : Auth?
    var storage : Storage?
    
    static func initializeAuth() {
        AuthInstance.instance.auth = Auth.auth()
        AuthInstance.instance.storage = Storage.storage()
    }
}
