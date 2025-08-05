# Dev Notes

## Goals
 - Easy to use
 - Minimal + clean UI
 - Progress View
 - Saves data

## MVP
 - Add/edit/remove habits
 - Mark habits complete
 - Show as list
 - Data persists in local storage
 - Streak counter
 - Last updated
 - For testing use mock data

## - **v**
- *topic*
- *topic*

## - **v0.1.0**
- *Streak Logic*: I had a really tough time with the logic of the streak because of the frequency list but also
  because I don't want a single missed day to lose the entire streak. Rather I want the streak to
  only be lost if I don't do the streak two days in a row(with both being on the frequency list). 
- *Version Control*: Another struggle(but also something I learned) was doing versions for my project but also naming
  of the package, filenames, etc. That was tough because usually we have a set naming structure we
  must follow for class. Another struggle is setting up the readme, because I haven't had to make a
  README at all yet so I just don't know what to put. Also tracking todos because usually an
  assignment is very structured and the procedure and todos are quite obvious, but not with this. 
- *When to Commit*: A small struggle is deciding when to commit and push to git. Also, something very laid out in class
  but not so much with my own. 
- *Unit model.Testing*: Something I learned were the unittests, I have never done them before, but I thought they would be
  harder to learn and figure out, but they aren't too bad at all. 
- *Python Data Structures*: Another thing I learned was the difference between set, tuple, list, and dictionary. Which I used
  to my advantage. Using a set when I want no duplicates, converting to a tuple when I wanted it to
  not be mutable during the method. I also used the sorting method for sets, which was very handy
  in early attempts at the streak. The dictionary was also nice to use for the frequency method!
- *Using Git Branches*: Figuring out the git branches was annoying, the pushes and commits wouldn't work for some reason,
  so I tried renaming, didn't work. So I had to remove the original one and then just keep the new
  default branch with the up-to-date commits.
  From previous


Website Notes:
- Use maven to manage dependencies. Use spring boot for web backend. 
- Use thymeleaf to make webpages with java. Use REST APIs to connect frontend and backend. 
- Use H2 + JPA w/ Hibernate to store and save data. For H2 switch to file-based instead of in-memory. 
- Do security stuff last, ensure the website is secure and safe. 
- Once set up for users, use like render or railway to host until then just run on laptop using http://localhost:8080.