//
//  ProfileViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/11/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import Firebase

class ProfileViewController: UIViewController, UITextFieldDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var usernameLabel: UILabel!
    
    @IBOutlet weak var profilePicImageView: UIImageView!
    
    @IBOutlet weak var descriptionTextField: UITextField!
    
    @IBOutlet weak var editImageButton: UIButton!

    let imagePicker = UIImagePickerController()
    
    
    @IBAction func editImageButtonClicked(_ sender: Any) {
        print("PUSHED")
        imagePicker.allowsEditing = false
        imagePicker.sourceType = .photoLibrary
        present(imagePicker, animated: true, completion: nil)
    }
    
    
    var currentUser : LendUser!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        currentUser = CurrentUserData.currentUser.data!
        print(currentUser.photoURL!)
        profilePicImageView.loadImage(url: currentUser.photoURL!)
        descriptionTextField.delegate = self
        imagePicker.delegate = self
        setupViews()
        // Do any additional setup after loading the view.
    }
    

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "unwindToLogin" {
            do{
                try AuthInstance.instance.auth!.signOut()
            }
            catch let signOutError as NSError {
                print ("Error signing out: %@", signOutError)
            }
        }
    }
    
    
    func setupViews() {
        profilePicImageView.makeRounded()
        usernameLabel.text = currentUser.username
        editImageButton.alpha = 0.02
        editImageButton.layer.cornerRadius = 0.5 * editImageButton.bounds.size.width
        Utils.setupTextField(textField: descriptionTextField, placeholderText: "None", backgroundColor: UIColor.white)
        descriptionTextField.text = currentUser.description
    }

    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let pickedImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            let reducedImage = pickedImage.resized(toWidth: CGFloat(Utils.REDUCED_IMAGE_SIZE))
            profilePicImageView.image = reducedImage!
            FirebaseQueries.postImage(image: reducedImage!)
        }
        
        dismiss(animated: true, completion: nil)
    }
    
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
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
