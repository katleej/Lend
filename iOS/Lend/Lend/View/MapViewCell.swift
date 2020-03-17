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
    
    static let CELL_SIZE : CGFloat = Utils.MAIN_CELL_HEIGHT + 30.0
    /*
     Contains DashboardViewController
     */
    var containerView : DashboardViewController?
    
    var viewMoreClickedAction : ((MapViewCell) -> Void)?
    
    
    @IBAction func viewMoreButtonClicked(_ sender: Any) {
        viewMoreClickedAction?(self)
    }
    
    
    
    var camera = GMSCameraPosition()
    func showCurrentLocationOnMap() {
        self.googleMapsView.delegate = self
        let camera = GMSCameraPosition.camera(withLatitude: (CurrentUserData.currentUser.data!.latitude)!, longitude: (CurrentUserData.currentUser.data!.longitude)!, zoom: 15)
        self.googleMapsView.camera = camera
        self.googleMapsView.accessibilityElementsHidden = false
        //googleMaps.layer.borderColor = UIColor.black.cgColor
        //googleMaps.layer.borderWidth = 1
        self.googleMapsView.layer.cornerRadius = 8
    }
    
    
    func mapView(_ mapView: GMSMapView, didTapInfoWindowOf marker: GMSMarker) {
        containerView!.markerSelected = marker.accessibilityLabel!
        self.containerView!.performSegue(withIdentifier: "toBookingFromMap", sender: self)
    }
    

}
