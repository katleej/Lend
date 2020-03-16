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
    
    static let trophyImage = "🏆".image()
    
    /*
    Variable to run the debug mode.
    */
    static let DEBUG = false
    
    /*
     Constant that is used to determine the downscaling of user uploaded images.
     */
    static let REDUCED_IMAGE_SIZE = 300.0
    
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
    
    /*
     DEBUG FUNCTION
     */
    static func addNewAttributeToAllItems(field : String, item : Item) {
        switch (field) {
        case "city":
            FirebaseQueries.getPropertyFromName(lenderName: item.lender.username!, property: field) { property in
                var newItem = item
                newItem.location = property
                FirebaseQueries.pushItemData(item: newItem)
            }
        
        case "location":
            print("UPDATING LOCATION FOR \(item)")
            FirebaseQueries.getLatLngFromName(lenderName: item.lender.username!) { (latitude, longitude) in
                var newItem = item
                newItem.latitude = latitude
                newItem.longitude = longitude
                FirebaseQueries.pushItemData(item: newItem)
            }
            /*
        case "photoURL":
            FirebaseQueries.getPropertyFromName(lenderName: item.lender.username!, property: field) { property in
                var newItem = item
                newItem.lenderPhotoURL = property
                FirebaseQueries.pushItemData(item: newItem)
            }
        case "all":
            FirebaseQueries.getLenderFromName(lenderName: item.lender.username!) { user in
                var newItem = item
                newItem.lenderPhotoURL = user?.photoURL
                newItem.lng = user?.longitude
                newItem.lat = user?.latitude
                newItem.location = user?.city!
                FirebaseQueries.pushItemData(item: newItem)
            }
        */
        case "lender":
            FirebaseQueries.getLenderFromId(lenderId: item.lenderId!) { user in
                var newItem = item
                newItem.lender = user!
                FirebaseQueries.pushItemData(item: newItem)
            }
        /*
        case "booked":
            var trueBooked = false
            if (item.booked == "true") {
                trueBooked = true
            }
            FirebaseQueries.updateItem(item: item, updates: ["Booked" : trueBooked])
        */
        default:
            break
        }
        
    }
    
    static func addNewAttributeToAllBookings(field : String, booking : Booking) {
        switch (field) {
        case "lender":
            FirebaseQueries.getBookings(from : String(booking.id!)) { booking in
                var newBooking = booking
                FirebaseQueries.getLenderFromId(lenderId: booking.lender.id) { user in
                    newBooking.lender = user
                    FirebaseQueries.pushBookingData(booking: newBooking)
                }
            }
        case "all" :
            FirebaseQueries.getBookings(from : String(booking.id!)) { booking in
                var newBooking = booking
                FirebaseQueries.getLenderFromId(lenderId: booking.lender.id) { lender in
                    FirebaseQueries.getLenderFromId(lenderId: booking.borrower!.id!) { borrower in
                        FirebaseQueries.getItemFromId(itemId: booking.item.id) { item in
                            newBooking.lender = lender
                            newBooking.borrower = borrower
                            newBooking.item = item
                            FirebaseQueries.pushBookingData(booking: newBooking)
                        }
                    }
                }
            }
        case "item" :
            FirebaseQueries.getBookings(from : String(booking.id!)) { booking in
                var newBooking = booking
                FirebaseQueries.getItemFromId(itemId: booking.item.id) { item in
                    newBooking.item = item
                    FirebaseQueries.pushBookingData(booking: newBooking)
                }
            }
        default:
            break
        }
        
    }
    
    static func convertItemBookedStringToBool(item : Item) {
        
    }
    
    
    static func filterFeaturedItems(items : [Item]) -> [Item] {
        let originalArray = items
        let filteredArray = originalArray.filter({ item in
            return item.booked! == false
            }
        )
        return filteredArray
    }
    
    static func segueToProfile(sender : UIViewController) {
        //If user selected themselves, sends to their tab for profile.
        if (CurrentUserData.currentUser.data!.id! == currentActiveProfile.id!) {
            sender.tabBarController!.selectedIndex = ProfileViewController.TAB_VIEW_INDEX
        } else {
            sender.performSegue(withIdentifier: "toProfile", sender: self)
        }
    }
    
}
/*
 Used to truncate double to PLACES decimal places.
 */
extension Double
{
    func truncate(places : Int)-> Double
    {
        return Double(floor(pow(10.0, Double(places)) * self)/pow(10.0, Double(places)))
    }
}

/*
 Used to display confirmation alerts to the user.
 */
extension UIViewController {
    func displayConfirmationAlert(title : String, message : String, okHandler : @escaping (UIAlertAction) -> (), cancelHandler : @escaping (UIAlertAction) -> ()) {
        let confirmAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)

        confirmAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: okHandler))

        confirmAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: cancelHandler))

        present(confirmAlert, animated: true, completion: nil)
    }
    
    func displayAlertAndPop(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let defaultAction = UIAlertAction(title: "OK", style: .default, handler: { (alert: UIAlertAction!) in
            self.navigationController?.popViewController(animated: true)
        })
        alert.addAction(defaultAction)
        self.present(alert, animated: true, completion: nil)
    }
}

