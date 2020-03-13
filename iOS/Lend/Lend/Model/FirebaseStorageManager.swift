//
//  FirebaseStorageManager.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/11/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit
import FirebaseStorage

class FirebaseStorageManager {
    
    public func uploadFile(localFile: URL, serverFileName: String, completionHandler: @escaping (_ isSuccess: Bool, _ url: String?) -> Void) {
        
        let storage = Storage.storage()
        let storageRef = storage.reference()
        // Create a reference to the file you want to upload
        let directory = "pictures/profiles/"
        let fileRef = storageRef.child(directory + serverFileName)

        _ = fileRef.putFile(from: localFile, metadata: nil) { metadata, error in
            fileRef.downloadURL { (url, error) in
                guard let downloadURL = url else {
                    // Uh-oh, an error occurred!
                    completionHandler(false, nil)
                    return
                }
                // File Uploaded Successfully
                completionHandler(true, downloadURL.absoluteString)
            }
        }
    }
    
    public func uploadImageData(data: Data, serverFileName: String, completionHandler: @escaping (_ isSuccess: Bool, _ url: String?) -> Void) {
        
        let storage = Storage.storage()
        let storageRef = storage.reference()
        // Create a reference to the file you want to upload
        let directory = "pictures/profiles/"
        let fileRef = storageRef.child(directory + serverFileName + ".png")
        let metadata = StorageMetadata()
        metadata.contentType = "image/jpeg"
        
        _ = fileRef.putData(data, metadata: metadata) { metadata, error in
            fileRef.downloadURL { (url, error) in
                guard let downloadURL = url else {
                    // Uh-oh, an error occurred!
                    completionHandler(false, nil)
                    return
                }
                // File Uploaded Successfully
                completionHandler(true, downloadURL.absoluteString)
            }
        }
    }

}
