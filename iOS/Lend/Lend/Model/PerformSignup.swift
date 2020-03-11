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
        Auth.auth().createUser(withEmail: email, password: password) { authResult, error in
            guard error == nil else {
                Utils.displayAlert(title: "Error", message: error!.localizedDescription, controller : view)
                return
            }
            guard authResult != nil else {
                Utils.displayAlert(title: "Error", message: "No user", controller : view)
                return
            }
            
            //SETUP LENDUSER INSTANCE
            let date = Date()
            let calendar = Calendar.current
            let placeInfo = Utils.extractCountryCityFromPlace(place: place)
            let user = LendUser(id: Auth.auth().currentUser!.uid, username: username, photoURL: LendUser.defaultPhotoURL, city: placeInfo["City"]! + ", " + placeInfo["Country"]!, email: email, description : "No Description ", rating: 0, latitude: place.coordinate.latitude, longitude: place.coordinate.longitude, numberOfBookings: 0, numReviews: 0, yearJoined: calendar.component(.year, from: date))

            let db = Firestore.firestore()
            do {
                try db.collection("users").document(user.username!).setData(from : user)
            } catch let error {
                print("Error writing city to Firestore: \(error)")
            }
            view.transitionToDashboard()
        }
    }
}
