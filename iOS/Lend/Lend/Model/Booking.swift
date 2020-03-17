//
//  Booking.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/14/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation

public struct Booking : Codable {
    let id : String!
    var active, userReturned : Bool!
    var bookingDays : Int?
    var lender : LendUser!
    var borrower : LendUser?
    var item : Item!
    
    var totalPrice : Double {
        get {
            return Double(Int(item.price!)!) * Double(bookingDays!)
        }
    }
    
    enum CodingKeys : String, CodingKey {
        case id = "ID"
        case active = "Active"
        case userReturned = "User Returned"
        case bookingDays = "Booking Days"
        case lender = "Lender"
        case borrower = "Borrower"
        case item = "Item"
    }
    
}
