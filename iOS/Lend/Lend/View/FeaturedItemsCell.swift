//
//  FeaturedItemsCell.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit

class FeaturedItemsCell : UITableViewCell {
    
    @IBOutlet weak var collectionView: FeaturedItemCollectionView!
    @IBOutlet weak var viewMoreButton: UIButton!
    static let CELL_SIZE : CGFloat = Utils.MAIN_CELL_HEIGHT + 30.0
    
    var viewMoreClickedAction : ((FeaturedItemsCell) -> Void)?
    
    @IBAction func viewMoreButtonClicked(_ sender: Any) {
        viewMoreClickedAction?(self)
    }
    
    
    func setCollectionViewDataSourceDelegate(dataSourceDelegate: UICollectionViewDataSource & UICollectionViewDelegate, forRow row: Int) {
        let featuredCell = UINib(nibName: "FeaturedCellNib", bundle: nil)
        collectionView.register(featuredCell, forCellWithReuseIdentifier: "featuredCellNibId")
        collectionView.delegate = dataSourceDelegate
        collectionView.dataSource = dataSourceDelegate
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.reloadData()
    }
}
