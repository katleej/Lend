//
//  UIImageViewCacher.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/12/20.
//  Copyright © 2020 mdb. All rights reserved.
//
import UIKit
import Kingfisher

extension UIImageView {
    func loadImage(url urlString: String) {
        let url = URL(string: urlString)
        let processor = DownsamplingImageProcessor(size: self.frame.size)
        self.kf.indicatorType = .activity
        self.kf.setImage(
            with: url,
            placeholder: UIImage(named: "elephant_green"),
            options: [
                .processor(processor),
                .scaleFactor(UIScreen.main.scale),
                .transition(.fade(1)),
                .cacheOriginalImage
            ])
        {
            result in
            switch result {
            case .success(let value):
                return
            case .failure(let error):
                return
            }
        }
    }
    func loadSmallImage(url urlString: String) {
        let url = URL(string: urlString)
        let processor = DownsamplingImageProcessor(size: self.frame.size)
        self.kf.indicatorType = .activity
        self.kf.setImage(
            with: url,
            placeholder: UIImage(named: "elephant_green"),
            options: [
                .processor(processor),
                .scaleFactor(UIScreen.main.scale),
                .transition(.fade(1)),
                .cacheOriginalImage
            ])
        {
            result in
            switch result {
            case .success(let value):
                return
            case .failure(let error):
                return
            }
        }
    }
}
