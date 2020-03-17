//
//  FeaturedLendersTableViewCell.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/9/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class FeaturedLendersTableViewCell: UITableViewCell {

    
    @IBOutlet weak var collectionView: FeaturedLenderCollectionView!
    
    @IBOutlet weak var viewMoreButton: UIButton!
    
    static let CELL_SIZE : CGFloat = Utils.MAIN_CELL_HEIGHT + 20.0
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.translatesAutoresizingMaskIntoConstraints = false
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setCollectionViewDataSourceDelegate(dataSourceDelegate: UICollectionViewDataSource & UICollectionViewDelegate, forRow row: Int) {
        let featuredCell = UINib(nibName: "FeaturedCellNib", bundle: nil)
        collectionView.register(featuredCell, forCellWithReuseIdentifier: "featuredCellNibId")
        collectionView.delegate = dataSourceDelegate
        collectionView.dataSource = dataSourceDelegate
        collectionView.reloadData()
    }

}
