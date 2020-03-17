//
//  ProfileImage.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/14/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class ProfileImage: UIImageView {
    override func layoutSubviews() {
      super.layoutSubviews()
      clipsToBounds = true
      layer.cornerRadius = min(frame.width, frame.height) / 2
    }
}
