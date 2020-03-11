//
//  FirebaseLendUser.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import Firebase

public struct LendUser : Codable {
    
    static let defaultPhotoURL = "https://firebasestorage.googleapis.com/v0/b/lend-7958f.appspot.com/o/pictures%2Fprofiles%2Felephant_white.png?alt=media&token=465fdaa4-448b-45ef-814f-985a572e5d86"

    let id, username, photoURL, city, email, description : String?
    let rating, latitude, longitude: Double?
    let numberOfBookings, numReviews, yearJoined: Int?

    enum CodingKeys: String, CodingKey {
        case id
        case username
        case photoURL
        case city
        case email
        case rating
        case latitude
        case longitude
        case numberOfBookings
        case numReviews
        case yearJoined
        case description
    }

}

