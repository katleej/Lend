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

class CurrentUserData {
    static let currentUser = CurrentUserData()
    var data : LendUser?
    
    
    /*
     Initializes user data into singleton instance currentUser.
     Also transitions from the signup/login VC to the DashboardVC.
     */
    func initializeUser(vc : LoginScreenViewController) {
        let db = Firestore.firestore()
        guard Auth.auth().currentUser != nil else {
          print("Exception occured - user does not exist.")
            return
        }
        let cUser = Auth.auth().currentUser!
        print(cUser.uid)
        db.collection("users").whereField("id", isEqualTo: cUser.uid).getDocuments() { (querySnapshot, err) in
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
                    vc.performSegue(withIdentifier: "toDashboard", sender: vc)
                }
        }
    }
}
