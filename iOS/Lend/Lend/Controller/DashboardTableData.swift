//
//  dashboardTableData.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import GoogleMaps

class DashboardTableData : UIViewController, UITableViewDataSource, UITableViewDelegate{
    
    var featuredItems : [Item] = [Item]()
    
    var nearbyItems : [Item] = [Item]()
    
    /*
     Map View for items near me.
     */
    var itemsNearMe : GMSMapView!
    
    /*
     Keeps track of the image header's original height. Used as reference
     due to the height of the header changing over time.
     */
    var headerImageHeight : CGFloat!
    
    /*
     Stretchy header image at top of UI
     */
    var headerImage : UIImageView!
    
    /*
     Var to keep track of whether or not the header is currently set to alpha=0 or alpha=1.
     */
    var imageIsHidden : Bool!
    
    /*
     Number of cells that are displayed.
     */
    let NUM_CELLS = 4
    
    /*
    Number of FeaturedLenderCollectionViewCells that are displayed.
    */
    let NUM_FEATURED_LENDERS = 10
    
    /*
    Number of FeaturedItemCollectionViewCells that are displayed.
    */
    let NUM_FEATURED_ITEMS = 8
    
    
    
    /*
     A dictionary mapping table view cell names to their corresponding indices
     within the view. Used throughout file to get index for cell type.
     */
    let cellRowMapping : [String : Int] = [
        "SearchBarCell" : 0,
        "FeaturedItemsCell" : 1,
        "MapViewCell" : 2,
        "FeaturedLendersCell" : 3
        ]
    
    func numberOfSections(in tableView: UITableView) -> Int {
            return 1
        }
    

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return NUM_CELLS
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        var cellFinal : UITableViewCell!
        
        switch(indexPath.row) {
        case cellRowMapping["SearchBarCell"]:
            let cell = tableView.dequeueReusableCell(withIdentifier: "searchCellId", for: indexPath) as! SearchBarCell
            cellFinal = setupSearchBarCell(cell: cell)
        case cellRowMapping["FeaturedItemsCell"]:
            let cell = tableView.dequeueReusableCell(withIdentifier: "featuredItemsId", for: indexPath) as! FeaturedItemsCell
            cellFinal = setupFeaturedItemsCell(cell: cell)
        case cellRowMapping["MapViewCell"]:
            let cell = tableView.dequeueReusableCell(withIdentifier: "mapViewCellId", for: indexPath) as! MapViewCell
            cellFinal = setupMapViewCell(cell: cell)
        case cellRowMapping["FeaturedLendersCell"]:
            let cell = tableView.dequeueReusableCell(withIdentifier: "featuredLendersId", for: indexPath) as! FeaturedLendersTableViewCell
            cellFinal = setupFeaturedLendersCell(cell: cell)
        default:
            break
        }
        cellFinal.selectionStyle = .none
        return cellFinal
        
    }
    
    
    /*
    Setup function for SearchBarCell.
    */
    func setupSearchBarCell(cell : SearchBarCell) -> SearchBarCell {
        return cell
    }
    
    
    /*
     Setup function for FeaturedLendersTableViewCell.
     */
    func setupFeaturedLendersCell(cell : FeaturedLendersTableViewCell) -> FeaturedLendersTableViewCell {
        cell.viewMoreButton.setTitle("View More >", for: .normal)
        return cell
    }
    
    /*
    Setup function for MapViewCell.
    */
    func setupMapViewCell(cell : MapViewCell) -> MapViewCell {
        cell.viewMoreButton.setTitle("View More >", for: .normal)
        for item in nearbyItems {
            let position = CLLocationCoordinate2D(latitude: item.lat!, longitude: item.lng!)
            let marker = GMSMarker(position: position)
            marker.title = item.itemName!
            marker.snippet = "$\(item.price!)"
            marker.map = cell.googleMapsView
        }
        return cell
    }
    
    /*
    Setup function for FeaturedItemsCell.
    */
    func setupFeaturedItemsCell(cell : FeaturedItemsCell) -> FeaturedItemsCell {
        cell.viewMoreButton.setTitle("View More >", for: .normal)
        return cell
    }
    
    func initFeaturedItemsArray(closure : @escaping () -> ()) {
        FirebaseQueries.getItemsDebug() { itemArr in
            self.featuredItems = itemArr
            closure()
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch(indexPath.row) {
        case cellRowMapping["SearchBarCell"]:
            return SearchBarCell.CELL_SIZE
        case cellRowMapping["FeaturedLendersCell"]:
            return FeaturedLendersTableViewCell.CELL_SIZE
        case cellRowMapping["MapViewCell"]:
            return MapViewCell.CELL_SIZE
        case cellRowMapping["FeaturedItemsCell"]:
            return FeaturedItemsCell.CELL_SIZE
        default:
            return 0
        }
    }
    
    
    /*
     Required function to set the collection view delegates, or update the map location.
     */
    func tableView(_ tableView: UITableView,
                    willDisplay cell: UITableViewCell,
                        forRowAt indexPath: IndexPath) {
        switch (indexPath.row) {
        case cellRowMapping["FeaturedItemsCell"]:
            guard let tableViewCell = cell as? FeaturedItemsCell else { return }
            tableViewCell.setCollectionViewDataSourceDelegate(dataSourceDelegate: self, forRow: indexPath.row)
        case cellRowMapping["MapViewCell"]:
            guard let tableViewCell = cell as? MapViewCell else { return }
            tableViewCell.showCurrentLocationOnMap()
        case cellRowMapping["FeaturedLendersCell"]:
            guard let tableViewCell = cell as? FeaturedLendersTableViewCell else { return }
            tableViewCell.setCollectionViewDataSourceDelegate(dataSourceDelegate: self, forRow: indexPath.row)
        default:
            break
        }

    }
    
    /*
    Setup function for FeaturedItemCollectionViewCell.
    */
    func setupFeaturedItemCollectionCell(cell : FeaturedCollectionViewCell, col : Int) -> FeaturedCollectionViewCell {
        cell.primaryImage.loadImage(url: featuredItems[col].photoURL!)
        cell.secondaryLabel.text = featuredItems[col].category!
        cell.primaryLabel.text = featuredItems[col].itemName!
        FirebaseQueries.getPropertyFromName(lenderName: featuredItems[col].lenderName!, property: "photoURL") { url in
            cell.secondaryImage.loadSmallImage(url: url)
        }
        cell.secondaryImage.makeRounded()
        cell.fourthLabel.text = "$\(featuredItems[col].price!)"
        cell.tertiaryLabel.text = featuredItems[col].lenderName!
        cell.layer.borderColor = Colors.BACKGROUND_COLOR.cgColor
        cell.layer.borderWidth = 3
        cell.layer.cornerRadius = 8
        return cell
    }
    
    /*
    Setup function for FeaturedLenderCollectionViewCell.
    */
    func setupFeaturedLenderCollectionCell(cell : FeaturedCollectionViewCell) -> FeaturedCollectionViewCell {
        cell.primaryImage.image = UIImage(named: "shrek")
        cell.secondaryLabel.text = "Berkeley, CA"
        cell.primaryLabel.text = "Bob the Builder"
        cell.secondaryImage.image = Utils.trophyImage!
        cell.fourthLabel.text = "Average Rating \(Int.random(in: 1...4)).\(Int.random(in: 0...9))"
        cell.tertiaryLabel.text = "\(Int.random(in: 1...999)) Ratings"
        cell.layer.borderColor = Colors.BACKGROUND_COLOR.cgColor
        cell.layer.borderWidth = 3
        cell.layer.cornerRadius = 8
        return cell
    }
    
    /*
     Function to initialize required variables for the header image.
     */
    func initializeHeaderImageHeight(header : UIImageView) {
        headerImageHeight = header.frame.height
        headerImage = header
        imageIsHidden = false
    }
    
    
}

extension DashboardTableData : UIScrollViewDelegate {
    /*
     Handles the updating of the header image, as well as making it stretchy.
     */
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if (scrollView.tag == 5) {
            let y = -scrollView.contentOffset.y
            let height = min(max(y, 0), headerImageHeight + 200)
            animateImage(newHeight: height)
            headerImage.frame = CGRect(x: 0, y: 0, width: headerImage.frame.width, height: height)
        }
    }
    
    /*
     Function to animate the header image in/out depending on where the user
     is in the scroll process. The animation lasts for ANIMATION_DURATION, and is triggered
     when the height passes CUTOFF_HEIGHT.
     */
    func animateImage(newHeight height : CGFloat) {
        let ANIMATION_DURATION = 0.7
        let CUTOFF_HEIGHT = (headerImageHeight / 2) + 10
        if (CUTOFF_HEIGHT > height
            && headerImage.frame.height > height
            && !imageIsHidden) {
            self.imageIsHidden = true
            UIView.animate(withDuration: ANIMATION_DURATION, animations: {
                self.headerImage.alpha = 0.0
            })
        } else if (CUTOFF_HEIGHT < height
            && headerImage.frame.height < height
            && imageIsHidden){
            self.imageIsHidden = false
            UIView.animate(withDuration: ANIMATION_DURATION, animations: {
                self.headerImage.alpha = 1.0
            })
        }
    }
}

extension DashboardTableData: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if (collectionView is FeaturedItemCollectionView) {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "featuredCellNibId",
                                                          for: indexPath as IndexPath) as! FeaturedCollectionViewCell
            return setupFeaturedItemCollectionCell(cell: cell, col: indexPath.row)
        } else if (collectionView is FeaturedLenderCollectionView){
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "featuredCellNibId",
                                                          for: indexPath as IndexPath) as! FeaturedCollectionViewCell
            return setupFeaturedLenderCollectionCell(cell: cell)
        } else {
            /*
             If this code is ever run, an error has occured. CollectionView should have
            type of FeaturedItemCollectionView or FeaturedLenderCollectionView.
            */
            return UICollectionViewCell()
        }
    }
    
    
    func collectionView(_ collectionView: UICollectionView,
        numberOfItemsInSection section: Int) -> Int {
        if (collectionView is FeaturedItemCollectionView) {
            return NUM_FEATURED_ITEMS
        } else if (collectionView is FeaturedLenderCollectionView){
            return NUM_FEATURED_LENDERS
        } else {
            return 0
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 160, height: collectionView.frame.size.height)
    }
}
