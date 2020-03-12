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
    
    @IBOutlet weak var headerImage: UIImageView!
    
    @IBOutlet weak var headerInitialHeightConstraint: NSLayoutConstraint!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headerImage.frame = CGRect(x: 0, y: 0, width: view.frame.size.width, height: 0.25 * view.frame.size.height)
        headerImage.bounds = CGRect(x: 0, y: 0, width: view.frame.size.width, height: 0.25 * view.frame.size.height)
        headerImage.translatesAutoresizingMaskIntoConstraints = true
        dataSource = DashboardTableData()
    dataSource!.initializeHeaderImageHeight(header: headerImage)
        mainTV.dataSource = dataSource!
        mainTV.delegate = dataSource!
        mainTV.separatorStyle = UITableViewCell.SeparatorStyle.none
        mainTV.contentInset = UIEdgeInsets(top: headerImage.frame.size.height, left: 0, bottom: 0, right: 0)
        mainTV.frame = view.frame
        
        

        
        
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
