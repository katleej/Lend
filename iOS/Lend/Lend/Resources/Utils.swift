//
//  Utils.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/6/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit
import GooglePlaces

class Utils {
    static func makeButtonRounded(button : UIButton, cornerRadius : CGFloat, borderWidth : CGFloat, borderColor : UIColor, backgroundColor : UIColor, textColor: UIColor) {
        button.backgroundColor = backgroundColor
        button.layer.cornerRadius = cornerRadius
        button.layer.borderWidth = borderWidth
        button.layer.borderColor = borderColor.cgColor
        button.setTitleColor(textColor, for: .normal)
        button.contentHorizontalAlignment = .center
    }
    
    static func displayAlert(title: String, message: String, controller : UIViewController) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let defaultAction = UIAlertAction(title: "OK", style: .default, handler: nil)
        alert.addAction(defaultAction)
        controller.present(alert, animated: true, completion: nil)
    }
    
    static func setupTextField(textField : UITextField, placeholderText : String, backgroundColor : UIColor) {
        textField.backgroundColor = backgroundColor
        textField.attributedPlaceholder = NSAttributedString(string: placeholderText,
        attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
    
    /*
     Function to extract the city name and country name from a given GMSPlace instance.
     Returns RV, a dictionary mapping "City" to the city name, and "Country" to the country name.
     */
    static func extractCountryCityFromPlace(place : GMSPlace) -> [String : String] {
        let attributes = place.addressComponents!
        var rv = [String : String]()
        for location in attributes {
            if (location.types.contains("locality")) {
                rv["City"] = location.name
            } else if (location.types.contains("country")) {
                rv["Country"] = location.name
            }
        }
        assert(rv.count == 2, "Something went wrong extracting country and city from place")
        return rv
    }
    
}
