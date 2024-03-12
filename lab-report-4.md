# CSE 15L Lab 4

Jacob Hansen

PID: A18031849

# Steps

4. Log into ieng6

   After doing the exercise, `ssh j4hansen@ieng6.ucsd.edu` is the last command executed on my local terminal, so I go up one in my history and press enter.

   Keys pressed: `<up><enter>`

   ![alt text](<Screenshot 2024-02-22 at 9.25.43 AM.png>)

5. Clone the forked repository

   First, have to activate the course on ieng6 by typing `cs15lwi24`. Then, I have the ssh link to the repo on my clipboard, so I type git clone then paste and enter to clone the repository.

   Keys pressed: `<cs15lwi24> <enter> <git clone> <ctrl + v> <enter>`

   ![alt text](<Screenshot 2024-02-22 at 9.29.12 AM.png>)

6. Run the tests showing they fail

   Now, have to `cd` into the lab directory and run `test.sh`. I can type the first letter of the directory and autocomplete with tab, and do the same thing with running `test.sh` with bash because they are the only options for autocomplete.

   Keys pressed: `<cd l> <tab> <enter> <bash t> <tab> <enter>`

   ![alt text](<Screenshot 2024-02-22 at 9.32.33 AM.png>)

7. Edit the code file to fix the failing test

   From the lab, there's already a really effiecient key stroke sequence to fix the error, just have to vim into the file. Because there are multiple files starting with `ListExamples`, have to use tab twice and put a `.` in between to show the autocomplete which file we want. Then we can do the vim command sequence to fix the bug like we optimized in lab.

   Keys pressed: `<vim L> <tab> <.> <tab> <enter> <:44> <enter> <e> <r> <2> <:wq>`

   ![alt text](<Screenshot 2024-02-22 at 9.35.58 AM.png>)
   ![alt text](<Screenshot 2024-02-22 at 9.36.19 AM.png>)

8. Run the test to show it works.

   `bash test.sh` is the second command in the terminal history so using the up arrow twice is efficient.

   Keys pressed: `<up> <up> <enter>`

   ![alt text](<Screenshot 2024-02-22 at 9.37.58 AM.png>)

9. Commit and push changes!

   First `git add .` stages the local changes for the commit. After pressing enter, we make the commit to the main branch with an empty message, and then push to the remote repository on GitHub.

   Keys pressed: `<git add .> <enter> <git commit -m ""> <enter> <git push> <enter>`

   ![alt text](<Screenshot 2024-02-22 at 9.42.23 AM.png>)
