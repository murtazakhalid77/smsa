import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from 'src/app/services/login.service';


@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})

export class LoginFormComponent implements OnInit {

  images: string[] = [
    'healthcare_upd_eng4.png',
    'smsa_ksamap_eng2.png',
    'FINAL_smartshipenglish2.png'
    // Add more image filenames here as needed
  ];

  myForm!: FormGroup;
  togglePassword: boolean = false;
  isToastVisible = true;
  currentIndex = 0;
  currentImageUrl?: string;

  constructor(private formbuilder: FormBuilder, private router: Router, private service: LoginService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.myForm = this.formbuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(7)]],
    });

    this.changeBackgroundImage(); // Set the initial image
    setInterval(() => this.changeBackgroundImage(), 5000); 
  }


  changeBackgroundImage() {
    this.currentImageUrl = `assets/images/${this.images[this.currentIndex]}`;
    this.currentIndex = (this.currentIndex + 1) % this.images.length;
  }

  submit() {
    if (this.myForm.invalid) {
      return;
    }

    const loginData = {
      name: this.myForm.value.email,
      password: this.myForm.value.password,
    };
   

    this.service.loginAuthenticated(loginData).subscribe(
      (response:any) => {
        const jwtToken = response.jwt; // Assuming the API returns a 'token' property in the response
    
        // Store the JWT token in Session Storage
        sessionStorage.setItem('jwtToken', jwtToken);
    
        // Navigate to the dashboard
        this.router.navigate(['/dashboard']);
       
      },
      (error) => {
        this.toastr.error('Incorrect Username or Password:')
      }
    
    )
    }
  showPassword() {
    this.togglePassword = !this.togglePassword;
  }

  hideToast() {
    this.isToastVisible = false;
  }


}

