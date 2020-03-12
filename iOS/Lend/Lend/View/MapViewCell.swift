//
//  MapViewCell.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit
import GoogleMaps

class MapViewCell : UITableViewCell, GMSMapViewDelegate, CLLocationManagerDelegate {
    @IBOutlet weak var googleMapsView: GMSMapView!
    
    @IBOutlet weak var viewMoreButton: UIButton!
    
    static let CELL_SIZE : CGFloat = 300.0
    
    var camera = GMSCameraPosition()
    func showCurrentLocationOnMap() {
        let camera = GMSCameraPosition.camera(withLatitude: (CurrentUserData.currentUser.data!.latitude)!, longitude: (CurrentUserData.currentUser.data!.longitude)!, zoom: 15)
        self.googleMapsView.camera = camera
        self.googleMapsView.accessibilityElementsHidden = false
        //googleMaps.layer.borderColor = UIColor.black.cgColor
        //googleMaps.layer.borderWidth = 1
        self.googleMapsView.layer.cornerRadius = 8
    }
}
