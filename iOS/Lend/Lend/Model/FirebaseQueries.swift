//
//  FirebaseQueries.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/10/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import FirebaseStorage
import FirebaseUI
import Firebase
import FirebaseFirestoreSwift
import CodableFirebase
import UIKit

class FirebaseQueries {
    
    static func postImage(image : UIImage) {
        let user = CurrentUserData.currentUser.data!
        if let data = image.pngData() { // convert your UIImage into Data object using png representation
            FirebaseStorageManager().uploadImageData(data: data, serverFileName: user.id!) { (isSuccess, url) in
                print("uploadImageData: \(isSuccess), \(url)")
                if (isSuccess) {
                    CurrentUserData.updateURL(newURL: url!)
                }
            }
        }
    }
    
    /*
     Debug Method that gets ALL items from DB
     */
    static func getItemsDebug(closure : @escaping ([Item]) -> ()) {
        let db = Firestore.firestore()
        var itemArray : [Item] = [Item]()
        let itemsRef = db.collection("items")
        var numtoFind : Int!
        itemsRef.getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                    closure(itemArray)
                } else {
                    numtoFind = querySnapshot!.documents.count
                    for document in querySnapshot!.documents {
                        let model = try! FirestoreDecoder().decode(Item.self, from: document.data())
                        itemArray.append(model)
                    }
                    closure(itemArray)
                    
                }
        }
    }
    
    static func getPropertyFromName(lenderName : String, property : String, closure: @escaping (String) -> ()) {
        let db = Firestore.firestore()
        db.collection("users").document(lenderName).getDocument() { (document, error) in
            if let document = document, document.exists {
                let data = document.data()![property] as! String
                closure(data)
            } else {
                print("Found no user with that username.")
                closure("nil")
            }
        }
    }
    
    static func getLatLngFromName(lenderName : String, closure: @escaping (Double, Double) -> ()) {
    let db = Firestore.firestore()
    db.collection("users").document(lenderName).getDocument() { (document, error) in
            if let document = document, document.exists {
                closure(document.data()!["latitude"] as! Double, document.data()!["longitude"] as! Double)
            } else {
                print("Found no user with that username.")
                closure(-1.0, -1.0)
            }
        }
    }
    
    static func pushItemData(item : Item) {
        let db = Firestore.firestore()
        do {
            try db.collection("items").document(String(item.id!)).setData(from: item)
        } catch let error {
            print("Error writing item to Firestore: \(error)")
        }
    }
    
    static func getLenderFromName(lenderName : String, closure : @escaping (LendUser?) -> ()) {
        let db = Firestore.firestore()
        db.collection("users").document(lenderName).getDocument() { (document, error) in
                if let document = document, document.exists {
                    let model = try! FirestoreDecoder().decode(LendUser.self, from: document.data()!)
                    closure(model)
                } else {
                    print("Found no user with that username.")
                    closure(nil)
                }
        }
    }
    
    static func getItemsNearCurrentUser(closure : @escaping ([Item]) -> ()) {
        let db = Firestore.firestore()
        db.collection("items").whereField("Latitude", isGreaterThan: CurrentUserData.currentUser.data!.latitude! - 2).whereField("Latitude", isLessThan: CurrentUserData.currentUser.data!.latitude!).getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error performing queries: \(err)")
                } else {
                    var foundItems = [Item]()
                    for document in querySnapshot!.documents {
                        let found = try! FirestoreDecoder().decode(Item.self, from: document.data())
                        foundItems.append(found)
                    }
                    closure(filterByLongitude(items : foundItems))
                }
        }
    }
    
    private static func filterByLongitude(items : [Item]) -> [Item] {
        var finalItems = [Item]()
        let currentUser = CurrentUserData.currentUser.data!
        for item in items {
            if (item.lng! < currentUser.longitude! + 2
                && item.lat! > currentUser.longitude! - 2) {
                finalItems.append(item)
            }
        }
        return finalItems
    }
    
}
