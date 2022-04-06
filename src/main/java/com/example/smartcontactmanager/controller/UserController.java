package com.example.smartcontactmanager.controller;

import com.example.smartcontactmanager.dao.ConatctRepositry;
import com.example.smartcontactmanager.dao.UserRepository;
import com.example.smartcontactmanager.entities.Contact;
import com.example.smartcontactmanager.entities.User;
import com.example.smartcontactmanager.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConatctRepositry contactRepository;
    @ModelAttribute
    public void addCommonData(Model model,Principal principal ){
        String userName=principal.getName();

        User user=userRepository.getUserByUserName(userName);

        model.addAttribute("user",user);

    }


    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){

        model.addAttribute("title","User Dashboard");
        return "NormalUser/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){

        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());


        return "NormalUser/add_contact_form";
    }

    // processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file
            , Principal principal , HttpSession session) {

        try {
            String name = principal.getName();

            User user = this.userRepository.getUserByUserName(name);


            if(file.isEmpty()){
                System.out.println("file is empty");
                contact.setImage("contact.png");
            }
            else{
                contact.setImage(file.getOriginalFilename());
                File saveFile=new ClassPathResource("static/image").getFile();

                Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
                System.out.println("image is uploaded");
            }

            contact.setUser(user);
            user.getContacts().add(contact);

            this.userRepository.save(user);
            System.out.println("data " + contact);
            session.setAttribute("message", new Message("Your contact is added", "success")   );


        }

        catch(Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();

            session.setAttribute("message", new Message("Something went wrong || try again", "danger")   );
        }
        return "NormalUser/add_contact_form";
    }

    // show contacts handler
    @GetMapping("/show-contacts")
    public String showContacts(  Model m , Principal principal){

        m.addAttribute("title","show user contacts");
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        List<Contact> contacts= this.contactRepository.findContactsByUser(user.getId());
        m.addAttribute("contacts",contacts);




        return "NormalUser/show_contacts";
    }


    // showing particular contact detail
    @GetMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal  ){

        Optional<Contact> contactOptional= this.contactRepository.findById(cId);

        Contact contact= contactOptional.get();



        String userName= principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        if(user.getId()==contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "NormalUser/contact_detail";

    }

    // delete contact handler

    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cId, Model model, Principal principal,HttpSession session ){

        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact = contactOptional.get();

        String userName= principal.getName();
        User user = this.userRepository.getUserByUserName(userName);


        if(user.getId()==contact.getUser().getId()) {
             contact.setUser(null);
             this.contactRepository.delete(contact);
              session.setAttribute("message",new Message("Contact Deleted Successfully","success"));
        }
        return "redirect:/user/show-contacts";
    }

    // open update form handler

    @PostMapping("/update-contact/{cid}")
    public String updateForm(@PathVariable("cid") Integer cid ,Model m){


        m.addAttribute("title","update contact");

        Contact contact =this.contactRepository.findById(cid).get();
        m.addAttribute("contact",contact);
        return "NormalUser/update_form";
    }


    // update contact handler
    @PostMapping("/process-update")
    public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file
            , Model m,HttpSession session , Principal principal){
        try{

            Contact oldcontactdetail =  this.contactRepository.findById(contact.getcId()).get();
            if(!file.isEmpty()){

                // delete old photo
                File deleteFile=new ClassPathResource("static/image").getFile();
                File file1= new File(deleteFile,oldcontactdetail.getImage());

                file1.delete();


                // update new photo
                File saveFile=new ClassPathResource("static/image").getFile();

                Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);

                contact.setImage(file.getOriginalFilename());
            }
            else{
                contact.setImage(oldcontactdetail.getImage());
            }

            User user= this.userRepository.getUserByUserName(principal.getName());
            contact.setUser(user);
            this.contactRepository.save(contact);

            session.setAttribute("message",new Message("Your contact is updated ","success"));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/user/"+contact.getcId()+"/contact";
    }











}





