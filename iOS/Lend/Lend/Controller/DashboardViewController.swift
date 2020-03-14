//
//  DashboardViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit


class DashboardViewController: UIViewController {

    @IBOutlet weak var mainTV: UITableView!
    
    var dataSource : DashboardTableData?
    
    /*
     Tracks the user selected map marker
     */
    var markerSelected : String?
    
    var featuredItemSelected : Item?
    
    @IBOutlet weak var headerImage: UIImageView!
    
    @IBOutlet weak var headerInitialHeightConstraint: NSLayoutConstraint!
    
    /*
     Used to return to dashboard after selecting an item
     */
    @IBAction func unwindToDashboard(_ unwindSegue: UIStoryboardSegue) {

    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        dataSource!.containerView = self
        currentActiveProfile = CurrentUserData.currentUser.data!
        if (dataSource!.DEBUG) {
            debug()
        }
        setupHeader()
        setupTableView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        navigationController?.setNavigationBarHidden(true, animated: animated)
        filterFeaturedItems()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
    func setupHeader() {
        headerImage.frame = CGRect(x: 0, y: 0, width: view.frame.size.width, height: 0.25 * view.frame.size.height)
        headerImage.bounds = CGRect(x: 0, y: 0, width: view.frame.size.width, height: 0.25 * view.frame.size.height)
        headerImage.translatesAutoresizingMaskIntoConstraints = true
        dataSource!.initializeHeaderImageHeight(header: headerImage)
    }
    
    func filterFeaturedItems() {
        let originalArray = dataSource?.featuredItems
        let filteredArray = originalArray?.filter({ item in
            return item.booked! == "false"
            }
        )
        dataSource?.featuredItems = filteredArray!
    }

    
    func setupTableView() {
        mainTV.dataSource = dataSource!
        mainTV.delegate = dataSource!
        mainTV.separatorStyle = UITableViewCell.SeparatorStyle.none
        mainTV.contentInset = UIEdgeInsets(top: headerImage.frame.size.height, left: 0, bottom: 0, right: 0)
        mainTV.frame = view.frame
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "toBookingFromMap") {
            let destinationVC = segue.destination as! BookingViewController
            destinationVC.item = dataSource?.nearbyItems[Int(self.markerSelected!)!]
        } else if (segue.identifier == "toBookingFromFeatured") {
            let destinationVC = segue.destination as! BookingViewController
            destinationVC.item = featuredItemSelected!
        }
    }
    
    func debug() {
        for item in dataSource!.featuredItems {
            Utils.addNewAttributeToAllItems(field: "city", item : item)
        }
    }
    
    
    
    
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
