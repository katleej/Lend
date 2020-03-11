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
    
    var featuredItems : [Item]!
    var itemsNearMe : GMSMapView!
    let trophyImage = "🏆".image()
    
    func numberOfSections(in tableView: UITableView) -> Int {
            return 1
        }
    

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 5
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let index = indexPath.row
        var cellFinal : UITableViewCell!
        if (index == 0) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "headerId", for: indexPath) as! HeaderImageCell
            cellFinal = setupHeaderCell(cell: cell)
        } else if (index == 1) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "searchCellId", for: indexPath) as! SearchBarCell
            cellFinal = setupSearchBarCell(cell: cell)
        } else if (index == 2) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "featuredItemsId", for: indexPath) as! FeaturedItemsCell
            cellFinal = setupFeaturedItemsCell(cell: cell)
        } else  if (index == 3){
            let cell = tableView.dequeueReusableCell(withIdentifier: "mapViewCellId", for: indexPath) as! MapViewCell
            cellFinal = setupMapViewCell(cell: cell)
        } else  if (index == 4){
            let cell = tableView.dequeueReusableCell(withIdentifier: "featuredLendersId", for: indexPath) as! FeaturedLendersTableViewCell
            cellFinal = setupFeaturedLendersCell(cell: cell)
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "", for: indexPath)
            cellFinal = cell
        }
        cellFinal.selectionStyle = .none
        return cellFinal
        
    }
    
    func setupFeaturedLendersCell(cell : FeaturedLendersTableViewCell) -> FeaturedLendersTableViewCell {
        cell.viewMoreButton.setTitle("View More >", for: .normal)
        return cell
    }
    
    func setupMapViewCell(cell : MapViewCell) -> MapViewCell {
        cell.viewMoreButton.setTitle("View More >", for: .normal)
        return cell
    }
    
    func setupFeaturedItemsCell(cell : FeaturedItemsCell) -> FeaturedItemsCell {
        cell.viewMoreButton.setTitle("View More >", for: .normal)
        return cell
    }
    
    func setupHeaderCell(cell : HeaderImageCell) -> HeaderImageCell {
        let peopleView = UIImageView(frame: CGRect(x: 0, y: 0, width: cell.headerContentView.frame.width, height: cell.headerContentView.frame.height))
        peopleView.image = UIImage(named: "people")
        cell.headerContentView.addSubview(peopleView)
        return cell
    }
    
    func setupSearchBarCell(cell : SearchBarCell) -> SearchBarCell {
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let index = indexPath.row
        if (index == 0) {
            return 210.0
        } else if (index == 1) {
            return 100.0
        } else if (index == 2) {
            return 300.0
        } else if (index == 3){
            return 300.0
        } else if (index == 4){
            return 300.0
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView,
                    willDisplay cell: UITableViewCell,
                        forRowAt indexPath: IndexPath) {
        if (indexPath.row == 2) {
            print("Sup")
            guard let tableViewCell = cell as? FeaturedItemsCell else { return }
            tableViewCell.setCollectionViewDataSourceDelegate(dataSourceDelegate: self, forRow: indexPath.row)
        } else if (indexPath.row == 3) {
            guard let tableViewCell = cell as? MapViewCell else { return }
            tableViewCell.showCurrentLocationOnMap()
        } else if (indexPath.row == 4) {
            guard let tableViewCell = cell as? FeaturedLendersTableViewCell else { return }
            tableViewCell.setCollectionViewDataSourceDelegate(dataSourceDelegate: self, forRow: indexPath.row)
        }
    }
    
    
    func setupFeaturedItemCollectionCell(cell : FeaturedCollectionViewCell) -> FeaturedCollectionViewCell {
        cell.primaryImage.image = UIImage(named: "shrek")
        cell.secondaryLabel.text = "Category"
        cell.primaryLabel.text = "Item Name"
        cell.secondaryImage.image = UIImage(named: "elephant_green")
        cell.fourthLabel.text = "$\(Int.random(in: 5...250))"
        cell.tertiaryLabel.text = "Shrek the Great"
        cell.layer.borderColor = Colors.BACKGROUND_COLOR.cgColor
        cell.layer.borderWidth = 3
        cell.layer.cornerRadius = 8
        return cell
    }
    
    func setupFeaturedLenderCollectionCell(cell : FeaturedCollectionViewCell) -> FeaturedCollectionViewCell {
        cell.primaryImage.image = UIImage(named: "shrek")
        cell.secondaryLabel.text = "Berkeley, CA"
        cell.primaryLabel.text = "Bob the Builder"
        cell.secondaryImage.image = trophyImage!
        cell.fourthLabel.text = "Average Rating \(Int.random(in: 1...4)).\(Int.random(in: 0...9))"
        cell.tertiaryLabel.text = "\(Int.random(in: 1...999)) Ratings"
        cell.layer.borderColor = Colors.BACKGROUND_COLOR.cgColor
        cell.layer.borderWidth = 3
        cell.layer.cornerRadius = 8
        return cell
    }
}

extension DashboardTableData: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if (collectionView is FeaturedItemCollectionView) {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "featuredCellNibId",
                                                          for: indexPath as IndexPath) as! FeaturedCollectionViewCell
            return setupFeaturedItemCollectionCell(cell: cell)
        } else if (collectionView is FeaturedLenderCollectionView){
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "featuredCellNibId",
                                                          for: indexPath as IndexPath) as! FeaturedCollectionViewCell
            return setupFeaturedLenderCollectionCell(cell: cell)
        } else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "potato",
                                                          for: indexPath as IndexPath) as! FeaturedLenderCollectionViewCell
            return setupFeaturedLenderCollectionCell(cell: cell)
        }
        
    }
    func collectionView(_ collectionView: UICollectionView,
        numberOfItemsInSection section: Int) -> Int {
        return 10
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 160, height: collectionView.frame.size.height)
    }
}
