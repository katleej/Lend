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
            let user = LendUser();
            user.setUsername(username: username);
            user.setLatitude( latitude: place.coordinate.latitude);
            user.setLongitude(longitude: place.coordinate.longitude);
            let date = Date()
            let calendar = Calendar.current
            user.setYearJoined(yearJoined: calendar.component(.year, from: date))
            user.setDescription(description: "No description ")
            user.setId(id: Auth.auth().currentUser!.uid)
            user.setRating(rating: 0)
            user.setNumReviews(numReviews: 0)
            user.setEmail(email: email)
            let placeInfo = Utils.extractCountryCityFromPlace(place: place)
            user.setCity(city: placeInfo["City"]! + ", " + placeInfo["Country"]!)

            let fbUser = Utils.lendUserToFirebase(user: user)
            let db = Firestore.firestore()
            do {
                try db.collection("users").document(user.getUsername()).setData(from : fbUser)
            } catch let error {
                print("Error writing city to Firestore: \(error)")
            }
            view.transitionToDashboard()
        }
    }
}
