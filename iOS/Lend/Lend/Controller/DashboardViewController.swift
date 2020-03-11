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
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        dataSource = DashboardTableData()
        mainTV.dataSource = dataSource!
        mainTV.delegate = dataSource!
        mainTV.separatorStyle = UITableViewCell.SeparatorStyle.none

        
        
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
