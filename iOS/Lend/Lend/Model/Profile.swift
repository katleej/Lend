//
//  Profile.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/11/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

struct Profile {
    
    var username : String
    var description : String
    var profilePictureURL : String
    var rating : Double
    var location : Location
    
    struct Location {
        var latitude : CGFloat
        var longitude : CGFloat
        var city : String
        var country : String
        var placeDescription : String {
            return "\(city), \(country)"
        }
    }
    
    func makeProfileFromUser(user : LendUser) -> Profile {
        let locationSplit = LendUser.splitCityToCityAndCountry(cityCountryString: user.city!)
        let loc = Location(latitude: CGFloat(user.latitude!), longitude: CGFloat(user.longitude!), city: locationSplit.0, country: locationSplit.1)
        return Profile(username: user.username!, description: user.description!, profilePictureURL: user.photoURL!, rating: user.rating!, location: loc)
    }
}
