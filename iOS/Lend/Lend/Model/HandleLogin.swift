//
//  HandleLogin.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/6/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit

class HandleLogin {
    static func attemptLogin(emailField: UITextField, passwordField: UITextField, loginInstance : LoginScreenViewController) {
        if (verifySignIn(emailField, passwordField)) {
            PerformLogin.performLogin(email: emailField.text!, password: passwordField.text!, loginViewController: loginInstance)
        } else{
            Utils.displayAlert(title: "Error", message: "Please fill in all text fields, and try again.", controller : loginInstance)
            LoadingIndicator.hide()
        }
    }
    
    
    
    /*
     Function to ensure the following conditions:
        1. Email Field contains text
        2. Password Field contains text
        3. The text contained within both fields is not empty
     */
    static func verifySignIn(_ emailTextField: UITextField, _ passwordTextField: UITextField) -> Bool{
        guard let email = emailTextField.text else{
            return false
        }
        guard let password = passwordTextField.text else{
            return false
        }
        if(email.count == 0 || password.count == 0){
            return false
        }
        return true
    }
}
