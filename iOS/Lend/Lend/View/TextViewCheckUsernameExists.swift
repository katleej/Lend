//
//  TextViewCheckUsernameExists.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/12/20.
//  Copyright © 2020 mdb. All rights reserved.
//
import UIKit
import Firebase
extension UITextField {
    func checkUsername(field: String, completion: @escaping (Bool) -> Void) {
        let collectionRef = Firestore.firestore().collection("users")
        collectionRef.whereField("username", isEqualTo: field).getDocuments { (snapshot, err) in
            if let err = err {
                print("Error getting document: \(err)")
                completion(true)
            } else if (snapshot?.isEmpty)! {
                completion(false)
            } else {
                for document in (snapshot?.documents)! {
                    if document.data()["username"] != nil {
                        completion(true)
                    }
                }
            }
        }
    }
}
