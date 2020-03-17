//
//  UserListingsViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/13/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class UserListingsViewController: UIViewController {
    
    
    @IBOutlet weak var pageSegmentedControl: UISegmentedControl!
    
    @IBOutlet weak var postingsContainerView: UIView!
    
    @IBOutlet weak var bookingsContainerView: UIView!
    
    
    
    @IBAction func segmentSelectedAction(sender: AnyObject) {
        switch sender.selectedSegmentIndex {
        case 0 :
            bookingsContainerView.isHidden = false
            postingsContainerView.isHidden = true
        case 1:
            bookingsContainerView.isHidden = true
            postingsContainerView.isHidden = false
        default:
            break
        }
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        bookingsContainerView.isHidden = false
        postingsContainerView.isHidden = true
        // Do any additional setup after loading the view.
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
