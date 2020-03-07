//
//  LoginScreenViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/6/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class LoginScreenViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var emailTextField: UITextField!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    @IBOutlet weak var submitButton: UIButton!
    
    
    
    
    @IBAction func submitButtonClicked(_ sender: Any) {
        HandleLogin.attemptLogin(emailField: emailTextField, passwordField: passwordTextField, loginInstance : self)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupTextField(textField: emailTextField, placeholderText: "Type in email", backgroundColor: Colors.BACKGROUND_COLOR)
        setupTextField(textField: passwordTextField, placeholderText: "Type in password", backgroundColor: Colors.BACKGROUND_COLOR)
        Utils.makeButtonRounded(button: submitButton, cornerRadius: 10, borderWidth: 1, borderColor: UIColor.white, backgroundColor: UIColor.white, textColor: Colors.BACKGROUND_COLOR)
        self.hideKeyboardWhenTappedAround()
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        emailTextField.delegate = self
        passwordTextField.delegate = self
        
        // Do any additional setup after loading the view.
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        // Sets email and password field to blank upon returning to screen
        // (potentially after signing out)
           emailTextField.text? = ""
           passwordTextField.text? = ""
       }
       
       override func viewDidAppear(_ animated: Bool) {
        
           //checkIfSignedIn()
       }
    
    /*
            Function that takes in a text field, a placeholder string, and a background color, and properly sets the textfield to have a white line underneath it.
     */
    
    
    func goToDashboard() {
        performSegue(withIdentifier: "toDashboard", sender: self)
    }
    
    func setupTextField(textField : UITextField, placeholderText : String, backgroundColor : UIColor) {
        textField.backgroundColor = backgroundColor
        textField.attributedPlaceholder = NSAttributedString(string: placeholderText,
        attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
    
    /*
     Makes a simple check to UserDefaults to see if the user has already
     been logged in earlier. If true, then automatically transition to
     dashboard without requiring further authentication.
     */
    func checkIfSignedIn() {
        let hasSignedIn = UserDefaults.standard.bool(forKey: "usersignedin")
        if(hasSignedIn==true){
            self.performSegue(withIdentifier: "toDashboard", sender: self)
        }
    }
    
    
    @objc func keyboardWillShow(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            if self.view.frame.origin.y == 0 {
                self.view.frame.origin.y -= keyboardSize.height - 150
            }
        }
    }

    @objc func keyboardWillHide(notification: NSNotification) {
        if self.view.frame.origin.y != 0 {
            self.view.frame.origin.y = 0
        }
    }
    
    /*
     Handles the pressing of return on the keyboard. If return is pressed
     while entering text into the email text field, then control is passed over
     to the password text field. If submit is pressed in the password text field,
     then the keyboard is lowered.
     
     This function is handled via tags set on the text fields that were determined
     in the storyboard builder. The email text field has a tag of 0, while
     the password text field has a tag of 1.
     */
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
       // Try to find next responder
       if let nextField = textField.superview?.viewWithTag(textField.tag + 1) as? UITextField {
          nextField.becomeFirstResponder()
       } else {
          // Not found, so remove keyboard.
          textField.resignFirstResponder()
       }
       // Do not add a line break
       return false
    }
 
    
    

    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

/*
 Extension to handle keyboard raising/lowering when entering email and password
 */
extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }

    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}
