//
//  SignupViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import GooglePlaces

class SignupViewController: UIViewController, UITextFieldDelegate {
    
    
    @IBAction func backButtonClicked(_ sender: Any) {
    }
    
    
    @IBOutlet weak var usernameTextField: UITextField!
        
    @IBOutlet weak var emailTextField: UITextField!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    
    @IBOutlet weak var locationTextField: UnderLineTextField!
    
    @IBAction func beginEditLocationTextField(_ sender: Any) {
        locationTextField.resignFirstResponder()
        let autocompleteController = GMSAutocompleteViewController()
        autocompleteController.delegate = self

        // Specify the place data types to return.
        let fields: GMSPlaceField = GMSPlaceField(rawValue: UInt(GMSPlaceField.name.rawValue) |
            UInt(GMSPlaceField.placeID.rawValue) | UInt(GMSPlaceField.coordinate.rawValue) | GMSPlaceField.addressComponents.rawValue)!
        autocompleteController.placeFields = fields

        /*
        // Specify a filter.
        let filter = GMSAutocompleteFilter()
        filter.type = .address
        autocompleteController.autocompleteFilter = filter
        */

        // Display the autocomplete view controller.
        present(autocompleteController, animated: true, completion: nil)
    }
    
    @IBOutlet weak var createButton: UIButton!
    
    
    
    @IBAction func createButtonClick(_ sender: Any) {
        HandleSignup.attemptSignup(usernameField: usernameTextField, emailField: emailTextField, passwordField: passwordTextField, confirmPasswordField: confirmPasswordTextField, place: selectedPlace, view: self)
    }
    
    
    var selectedPlace : GMSPlace?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //Set delegates
        usernameTextField.delegate = self
        emailTextField.delegate = self
        passwordTextField.delegate = self
        confirmPasswordTextField.delegate = self
        
        //Configure text fields
        setupTextFields()
        
        //Make create button round
        Utils.makeButtonRounded(button: createButton, cornerRadius: 10, borderWidth: 1, borderColor: UIColor.white, backgroundColor: UIColor.white, textColor: Colors.BACKGROUND_COLOR)
        
        

        // Do any additional setup after loading the view.
    }
    
    func setupTextFields() {
        Utils.setupTextField(textField: usernameTextField, placeholderText: "Username", backgroundColor: Colors.BACKGROUND_COLOR)
        Utils.setupTextField(textField: emailTextField, placeholderText: "Email", backgroundColor: Colors.BACKGROUND_COLOR)
        Utils.setupTextField(textField: passwordTextField, placeholderText: "Password", backgroundColor: Colors.BACKGROUND_COLOR)
        Utils.setupTextField(textField: confirmPasswordTextField, placeholderText: "Confirm Password", backgroundColor: Colors.BACKGROUND_COLOR)
        Utils.setupTextField(textField: locationTextField, placeholderText: "Enter Location", backgroundColor: Colors.BACKGROUND_COLOR)
    }
    
    
    func transitionToDashboard() {
        self.performSegue(withIdentifier: "toDashboard", sender: self)
    }
    
    /*
     Handles the pressing of return on the keyboard. See LoginViewController for better explanation of how this function works.
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

extension SignupViewController: GMSAutocompleteViewControllerDelegate {

  // Handle the user's selection.
  func viewController(_ viewController: GMSAutocompleteViewController, didAutocompleteWith place: GMSPlace) {
    selectedPlace = place
    locationTextField.text = place.name
    dismiss(animated: true, completion: nil)
  }

  func viewController(_ viewController: GMSAutocompleteViewController, didFailAutocompleteWithError error: Error) {
    // TODO: handle the error.
    print("Error: ", error.localizedDescription)
  }

  // User canceled the operation.
  func wasCancelled(_ viewController: GMSAutocompleteViewController) {
    dismiss(animated: true, completion: nil)
  }

  // Turn the network activity indicator on and off again.
  func didRequestAutocompletePredictions(_ viewController: GMSAutocompleteViewController) {
    UIApplication.shared.isNetworkActivityIndicatorVisible = true
  }

  func didUpdateAutocompletePredictions(_ viewController: GMSAutocompleteViewController) {
    UIApplication.shared.isNetworkActivityIndicatorVisible = false
  }

}

