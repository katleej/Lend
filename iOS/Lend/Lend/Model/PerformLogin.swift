//
//  PerformLogin.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/6/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import Firebase

class PerformLogin {
    static func performLogin(email: String, password: String, loginViewController : LoginScreenViewController) {
        Auth.auth().signIn(withEmail: email, password: password) { (signedInUser, signInError) in
                   guard signInError == nil else {
                    Utils.displayAlert(title: "Error", message: "Invalid credentials", controller : loginViewController)
                       return
                   }
            guard signedInUser != nil else {
                Utils.displayAlert(title: "Error", message: "No account found", controller : loginViewController)
                       return
                   }
            UserDefaults.standard.set(true, forKey: "usersignedin")
            loginViewController.goToDashboard()
        }
        
        
    }
}
