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
    
    static func postProfileImage(image : UIImage) {
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
    
    static func postItemImage(itemId: String, image : UIImage, closure : @escaping (String) -> ()) {
        let reducedImage = image.resized(toWidth: CGFloat(Utils.REDUCED_IMAGE_SIZE))
        if let data = reducedImage!.pngData() { // convert your UIImage into Data object using png representation
            FirebaseStorageManager().uploadImageData(data: data, serverFileName: itemId) { (isSuccess, url) in
                print("uploadImageData: \(isSuccess), \(url)")
                if (isSuccess) {
                    closure(url!)
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
        itemsRef.getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                    closure(itemArray)
                } else {
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
    
    /*
     Gets all bookings
     */
    static func getBookings(closure : @escaping ([Booking]) -> ()) {
        
        let db = Firestore.firestore()
        var bookingArray : [Booking] = [Booking]()
        let itemsRef = db.collection("bookings")
        itemsRef.getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                    closure(bookingArray)
                } else {
                    for document in querySnapshot!.documents {
                        let model = try! FirestoreDecoder().decode(Booking.self, from: document.data())
                        bookingArray.append(model)
                    }
                    closure(bookingArray)
                    
                }
        }
    }
    
    static func updateAllBookingsItems() {
        let db = Firestore.firestore()
        let itemsRef = db.collection("bookings")
        itemsRef.getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    for document in querySnapshot!.documents {
                        let tempItem = document.get("Item") as! [String : Any]
                        db.collection("items").whereField("Item Name", isEqualTo: tempItem["Item Name"]!).whereField("Lender Name", isEqualTo: tempItem["Lender Name"]!).getDocuments() { (querySnapshot, err) in
                            if let err = err {
                                print("Error getting documents: \(err)")
                            } else {
                                assert (querySnapshot!.documents.count == 1)
                                let document2 = querySnapshot!.documents[0]
                                let data = document2.data()
                                FirebaseQueries.updateBooking(bookingId: document.documentID, updates: ["Item" : data])
                            }
                        }
                        
                        
                    }
                    
                }
        }
    }
    
    static func updateItemIds() {
        let db = Firestore.firestore()
        let itemsRef = db.collection("items")
        itemsRef.getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    for document in querySnapshot!.documents {
                        var itemDict = document.data()
                        let newItemRef = itemsRef.document()
                        itemDict["ID"] = newItemRef.documentID
                        newItemRef.setData(itemDict)
                }
                    
            }
        }
    }
    
    static func updateBookingIds() {
        let db = Firestore.firestore()
        let bookingsRef = db.collection("bookings")
        bookingsRef.getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    for document in querySnapshot!.documents {
                        var bookingDict = document.data()
                        let newBookingRef = bookingsRef.document()
                        bookingDict["ID"] = newBookingRef.documentID
                        newBookingRef.setData(bookingDict)
                }
                    
            }
        }
    }
    
    static func deleteDuplicateItems() {
        let db = Firestore.firestore()
        let itemsRef = db.collection("items")
        itemsRef.getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    for document in querySnapshot!.documents {
                        let itemDict = document.data()
                        let itemName = itemDict["Item Name"]
                        let lenderName = itemDict["Lender Name"]
                        itemsRef.whereField("Item Name", isEqualTo: itemName!).whereField("Lender Name", isEqualTo: lenderName!).getDocuments() { (querySnapshot, err) in
                            if let err = err {
                                print("Error getting documents: \(err)")
                            } else {
                                let documents = querySnapshot!.documents
                                if (documents.count <= 1) {
                                    print("all good")
                                    return
                                } else {
                                    var i = 1
                                    while (i < documents.count) {
                                        db.collection("items").document(documents[i].documentID).delete() { err in
                                            if let err = err {
                                                print("Error removing document: \(err)")
                                            } else {
                                                print("Document successfully removed!")
                                            }
                                        }
                                        i = i + 1
                                    }
                                }
                            }
                        }
                }
                    
            }
        }
    }
    
    
    static func makeBookingForItem(item : Item, bookingDays : Int, pastBookings : [Booking]) {
        let db = Firestore.firestore()
        let doc = db.collection("bookings").document()
        let booking = Booking(id: doc.documentID, active: true, userReturned: false, bookingDays: bookingDays, lender: item.lender, borrower: CurrentUserData.currentUser.data!, item: item)
        FirebaseQueries.pushBookingData(booking: booking)
        var newBookings = pastBookings
        newBookings.append(booking)
        FirebaseQueries.pushBorrowerToBookingsArray(user: booking.borrower!, bookings: newBookings)
    }
     
    
    static func getBookings(for user: LendUser, closure : @escaping ([Booking]) -> ()) {
        let db = Firestore.firestore()
        db.collection("borrowerToBookings").document(user.id).getDocument() { (document, error) in
            var allBookings : [Booking] = [Booking]()
            if let document = document, document.exists {
                let bookings = document.get("Bookings") as! [[String : Any]]
                for b in bookings {
                    let model = try! FirestoreDecoder().decode(Booking.self, from: b)
                    allBookings.append(model)
                }
                closure(allBookings)
            } else {
                print("Document does not exist")
            }
        }
    }
    
    static func getBookings(from id: String, closure : @escaping (Booking) -> ()) {
        let db = Firestore.firestore()
        db.collection("bookings").document(id).getDocument() { (document, error) in
                if let document = document, document.exists {
                    let model = try! FirestoreDecoder().decode(Booking.self, from: document.data()!)
                    closure(model)
                } else {
                    print("Found no booking with that id.")
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
    
    static func updateItem(itemId : String, updates : [AnyHashable : Any]) {
        let db = Firestore.firestore()
        do {
            try db.collection("items").document(itemId).updateData(updates)
        } catch let error {
            print("Error writing item to Firestore: \(error)")
        }
    }
    
    static func updateBooking(bookingId : String, updates : [AnyHashable : Any]) {
        let db = Firestore.firestore()
        do {
            try db.collection("bookings").document(bookingId).updateData(updates)
        } catch let error {
            print("Error writing item to Firestore: \(error)")
        }
    }
    
    
    
    static func pushBookingData(booking : Booking) {
        let db = Firestore.firestore()
        do {
            try db.collection("bookings").document(String(booking.id!)).setData(from: booking, merge: true)
        } catch let error {
            print("Error writing item to Firestore: \(error)")
        }
    }
    
    static func pushBookingToUserIDsData(booking : Booking) {
        let db = Firestore.firestore()
        do {
            try db.collection("bookingsToUserIDs").document(String(booking.id!)).setData(from: ["Borrower ID" : booking.borrower?.id, "Lender ID" : booking.lender.id], merge: true)
        } catch let error {
            print("Error writing item to Firestore: \(error)")
        }
    }
    
    static func pushBorrowerToBookingsArray(user : LendUser?, bookings : [Booking]) {
        guard user != nil else {
            return
        }
        let db = Firestore.firestore()
        do {
            try db.collection("borrowerToBookings").document(String(user!.id!)).setData(from: ["Bookings" : bookings], merge: true)
        } catch let error {
            print("Error writing item to Firestore: \(error)")
        }
    }
    
    static func updateBookingsArrays(user : LendUser?) {
        guard user != nil else {
            return
        }
        var bookingsArray : [Booking] = [Booking]()
        FirebaseQueries.getBookings() { bookings in
            for booking in bookings {
                if (booking.borrower?.id == user!.id && booking.active) {
                    bookingsArray.append(booking)
                }
            }
            FirebaseQueries.pushBorrowerToBookingsArray(user: user, bookings: bookingsArray)
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
    
    static func getLenderFromId(lenderId : String, closure : @escaping (LendUser?) -> ()) {
        let db = Firestore.firestore()
        db.collection("users").whereField("id", isEqualTo: lenderId).getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                if (querySnapshot!.count == 1) {
                    var userDocument : QueryDocumentSnapshot!
                    for document in querySnapshot!.documents {
                        userDocument = document
                    }
                    let model = try! FirestoreDecoder().decode(LendUser.self, from: userDocument.data())
                    closure(model)
                }
            }
        }
    }
    
    
    static func getItemFromId(itemId : String, closure : @escaping (Item?) -> ()) {
        let db = Firestore.firestore()
        db.collection("items").document(itemId).getDocument() { (document, error) in
                if let document = document, document.exists {
                    let model = try! FirestoreDecoder().decode(Item.self, from: document.data()!)
                    closure(model)
                } else {
                    print("Found no item with that ID.")
                    closure(nil)
                }
        }
    }
    
    static func getAllLenders(closure : @escaping ([LendUser]) -> ()) {
        let db = Firestore.firestore()
        db.collection("users").getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error performing get all users query: \(err)")
                } else {
                    var users = [LendUser]()
                    for document in querySnapshot!.documents {
                        let found = try! FirestoreDecoder().decode(LendUser.self, from: document.data())
                        users.append(found)
                    }
                    closure(users)
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
    
    static func getFeaturedLenders(closure : @escaping ([LendUser]) -> ()) {
        let db = Firestore.firestore()
        db.collection("users").whereField("rating", isGreaterThan: 4).getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error performing queries: \(err)")
                } else {
                    var foundItems = [LendUser]()
                    for document in querySnapshot!.documents {
                        let found = try! FirestoreDecoder().decode(LendUser.self, from: document.data())
                        foundItems.append(found)
                    }
                    closure(foundItems)
                }
        }
    }
    
    private static func filterByLongitude(items : [Item]) -> [Item] {
        var finalItems = [Item]()
        let currentUser = CurrentUserData.currentUser.data!
        for item in items {
            if (item.lender.longitude! < currentUser.longitude! + 2
                && item.lender.latitude! > currentUser.longitude! - 2) {
                finalItems.append(item)
            }
        }
        return finalItems
    }
    
    static func getNewItemId() -> String {
        let db = Firestore.firestore()
        let docRef = db.collection("items").document()
        return docRef.documentID
    }
    
}
