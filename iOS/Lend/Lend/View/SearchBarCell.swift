//
//  SearchBarCell.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit

class SearchBarCell : UITableViewCell, UISearchBarDelegate{
    
    @IBOutlet weak var searchBar: UISearchBar!
    static let CELL_SIZE : CGFloat = 100.0
    
    var returnClickedAction : ((SearchBarCell) -> Void)?
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        returnClickedAction?(self)
    }
    
}
