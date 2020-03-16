//
//  CurrentUserData.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import Firebase
import CodableFirebase
import FirebaseStorage

class CurrentUserData {
    static let currentUser = CurrentUserData()
    var data : LendUser?
    var postings : [Item]?
    var bookings : [Booking]?
    
    

    func initializeUser(closure : @escaping () -> Void) {
        let db = Firestore.firestore()
        guard Auth.auth().currentUser != nil else {
          print("Exception occured - user does not exist.")
            return
        }
        let cUser = Auth.auth().currentUser!
        db.collection("users").whereField("id", isEqualTo: cUser.uid).addSnapshotListener() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    assert(querySnapshot!.documents.count == 1, "More than 1 user found with same ID.")
                    var userDocument : QueryDocumentSnapshot!
                    for document in querySnapshot!.documents {
                        userDocument = document
                    }
                    let model = try! FirestoreDecoder().decode(LendUser.self, from: userDocument.data())
                    self.data = model
                    closure()
            }
        }
    }
    
    
    
    
    static func updateURL(newURL : String) {
        let profileRef = Storage.storage().reference(forURL: newURL)
        profileRef.downloadURL { url, error in
          if let error = error {
            // Handle any errors
            print("Error unable to find current profile photo: \(error.localizedDescription)")
          } else {
            // Get the download URL
            CurrentUserData.currentUser.data!.setProfileURL(url: url!.absoluteString)
            CurrentUserData.pushNewUserData()
          }
        }
    }
    
    /*
     Pushes current user data to firebase.
     */
    static func pushNewUserData() {
        let db = Firestore.firestore()
        do {
            try db.collection("users").document("\(CurrentUserData.currentUser.data!.username!)").setData(from: CurrentUserData.currentUser.data!)
        } catch let error {
            print("Error writing city to Firestore: \(error)")
        }
    }
}
