//
//  HandleSignup.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit
import GooglePlaces

class HandleSignup {
    static func attemptSignup(usernameField : UITextField, emailField : UITextField, passwordField : UITextField, confirmPasswordField : UITextField, place : GMSPlace?, view : SignupViewController) {
        if (verifySignup(usernameField, emailField, passwordField, confirmPasswordField, place: place, view: view)) {
            PerformSignup.performSignup(username : usernameField.text!, email: emailField.text!, password : passwordField.text!, place: place!, view: view) {
                view.performSegue(withIdentifier: "toDashboard", sender: view)
            }
        }
        
    }
    
    static func verifySignup(_ usernameField : UITextField, _ emailField : UITextField, _ passwordField : UITextField, _ confirmPasswordField : UITextField, place : GMSPlace?, view : UIViewController) -> Bool {
        guard let username = usernameField.text else {
            Utils.displayAlert(title: "Error", message: "No username entered.", controller: view)
            return false
        }
        guard let email = emailField.text else{
            Utils.displayAlert(title: "Error", message: "No email entered.", controller: view)
            return false
        }
        guard let password = passwordField.text else{
            Utils.displayAlert(title: "Error", message: "No password entered.", controller: view)
            return false
        }
        guard let confirmedPassword = confirmPasswordField.text else {
            Utils.displayAlert(title: "Error", message: "Please confirm your password and try again.", controller: view)
            return false
        }
        guard place != nil && place!.name != nil else {
            Utils.displayAlert(title: "Error", message: "Please select a valid location and try again.", controller: view)
            return false
        }
        if(username.count == 0 || email.count == 0 || password.count == 0 || confirmedPassword.count == 0 || place!.name!.count == 0){
            Utils.displayAlert(title: "Error", message: "Please fill all fields, then try again.", controller: view)
            return false
        }
        if(password != confirmedPassword){
            Utils.displayAlert(title: "Error", message: "Password does not match confirmed password.", controller: view)
            return false
        }
        
        return true
    }
    
    private func checkIfUsernameTaken (usernameTextField textField: UITextField) {
        
    }
}
