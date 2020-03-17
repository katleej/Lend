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
        AuthInstance.instance.auth!.signIn(withEmail: email, password: password) { (signedInUser, signInError) in
                   guard signInError == nil else {
                    Utils.displayAlert(title: "Error", message: "Invalid credentials", controller : loginViewController)
                    LoadingIndicator.hide()
                       return
                   }
            guard signedInUser != nil else {
                Utils.displayAlert(title: "Error", message: "No account found", controller : loginViewController)
                LoadingIndicator.hide()
                       return
                   }
            
            
            if (!Utils.DEVELOPMENT_MODE) {
                guard Auth.auth().currentUser!.isEmailVerified else {
                 let ok = { (action : UIAlertAction!) in
                     Auth.auth().currentUser!.sendEmailVerification() { (error) in
                         guard let error = error else {
                             Utils.displayAlert(title: "Success", message: "Please check your email and verify it is correct. Then, attempt to login again.", controller: loginViewController)
                             return
                         }
                         Utils.displayAlert(title: "Error", message: "Something went wrong sending a confirmation email. Account cannot be verified at this time.", controller: loginViewController)
                         
                         
                     }
                 }
                 let cancel = { (action : UIAlertAction!) in
                     return
                 }
                 LoadingIndicator.hide()
                 loginViewController.displayConfirmationAlert(title: "Error", message: "Account not verified. Would you like to resend the confirmation email?", okHandler: ok, cancelHandler: cancel)
                 do{
                     try AuthInstance.instance.auth!.signOut()
                 }
                 catch let signOutError as NSError {
                     print ("Error signing out: %@", signOutError)
                 }
                    return
                }
            }
            
            UserDefaults.standard.set(true, forKey: "usersignedin")
            loginViewController.goToDashboard()
        }
        
        
    }
    
    
}
