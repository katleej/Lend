//
//  PerformSignup.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import Firebase
import FirebaseFirestoreSwift
import GooglePlaces

class PerformSignup {
    static func performSignup(username : String, email : String, password: String, place : GMSPlace, view : SignupViewController, completion: @escaping () -> ()){
        AuthInstance.instance.auth!.createUser(withEmail: email, password: password) { authResult, error in
            guard error == nil else {
                LoadingIndicator.hide()
                Utils.displayAlert(title: "Error", message: error!.localizedDescription, controller : view)
                return
            }
            guard authResult != nil else {
                LoadingIndicator.hide()
                Utils.displayAlert(title: "Error", message: "No user", controller : view)
                return
            }
            
            //SETUP LENDUSER INSTANCE
            let date = Date()
            let calendar = Calendar.current
            guard let placeInfo = Utils.extractStateCityFromPlace(place: place, vc: view) else {
                return
            }
            let user = LendUser(id: AuthInstance.instance.auth!.currentUser!.uid, username: username, photoURL: LendUser.defaultPhotoURL, city: placeInfo["City"]! + ", " + placeInfo["State"]!, email: email, description : "No Description ", rating: 0, latitude: place.coordinate.latitude, longitude: place.coordinate.longitude, numberOfBookings: 0, numReviews: 0, yearJoined: calendar.component(.year, from: date))

            let db = Firestore.firestore()
            do {
                try db.collection("users").document(user.username!).setData(from : user)
                db.collection("borrowerToBookings").document(user.id).setData(["Bookings" : [Booking]()])
            } catch let error {
                print("Error writing data to Firestore: \(error)")
            }
            Auth.auth().currentUser!.sendEmailVerification() { (error) in
                guard let error = error else {
                    LoadingIndicator.hide()
                    Utils.displayAlert(title: "Success", message: "Please check your email and verify it is correct. Then, you may login to your account.", controller: view) {
                        view.performSegue(withIdentifier: "unwindToLogin", sender: view)
                    }
                    return
                }
                Utils.displayAlert(title: "Error", message: "Something went wrong sending a confirmation email. Account cannot be verified at this time.", controller: view)
                LoadingIndicator.hide()
                
            }
            
        }
    }
}
