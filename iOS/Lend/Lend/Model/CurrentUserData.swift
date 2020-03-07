//
//  CurrentUserData.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import Firebase

class CurrentUserData {
    var currentUser : LendUser!
    
    static func initializeUser() {
        let db = Firestore.firestore()
        guard Auth.auth().currentUser != nil else {
          print("Exception occured - user does not exist.")
            return
        }
        print("HELLO")
        let currentUser = Auth.auth().currentUser!
        print(currentUser.uid)
        db.collection("users").whereField("id", isEqualTo: currentUser.uid)
            .getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    assert(querySnapshot!.documents.count == 1, "More than 1 user found with same ID.")
                    var userDocument : QueryDocumentSnapshot!
                    for document in querySnapshot!.documents {
                        userDocument = document
                    }
                    print("\(userDocument.documentID) => \(userDocument.data())")
                }
        }

    }
}
