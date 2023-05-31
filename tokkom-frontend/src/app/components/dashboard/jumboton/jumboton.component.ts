import { Component, OnInit } from '@angular/core';
import Typed from 'typed.js';

@Component({
  selector: 'app-jumboton',
  templateUrl: './jumboton.component.html',
  styleUrls: ['./jumboton.component.scss'],
})
export class JumbotonComponent implements OnInit {
  constructor() {}

  ngOnInit() {
    const options = {
      strings: ['Welcome to ToKKom...', 'Lots of digital tools are available here....'],
      typeSpeed: 50,
      backSpeed: 50,
      showCursor: false,
      loop: true,
    };

    const typed = new Typed('.typed-element', options);
  }
}
