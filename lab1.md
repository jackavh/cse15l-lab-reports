# CSE 15L Lab 1

Jacob Hansen

PID: A18031849

## cd

`cd` stands for _change directory_ and can be used to move the working directory elsewhere.

1. `cd` with no arguments has no output, and changes the working directory to home. On my machine that is `/Users/zoophere`. The only change is in the working directory displayed.

```bash
❯ cd
```

2. `cd Documents/Course\ Materials/WI24/CSE-15L/cse15l-lab-reports` changes the working directory to the one in the argument, regardless of where the current working directory is.

```bash
❯ cd Documents/Course\ Materials/WI24/CSE-15L/cse15l-lab-reports
```

3. `cd ./lab1.md` outputs an error because ./lab1.md is a file not a directory. This is an error because a file cannot be a working directory. My understanding is that files can be read, written, or executed.

```bash
❯ cd ./lab1.md
cd: not a directory: ./lab1.md
```

## ls

`ls` lists directory contents in a few ways.

1. `ls` with no arguments lists all files or folders in the current working directory to the standard output.

```bash
❯ ls
index.md lab1.md  test.md
```

2. `ls ~/Documents/Course\ Materials/WI24/CSE-15L` lists the directory contents of the argument.

```bash
❯ ls ~/Documents/Course\ Materials/WI24/CSE-15L
Lecture 1.pdf
Lecture 2.pdf
The Pocket Guide to Debugging.zip
cse15l-lab-reports
lecture1
lecture2-notes.txt
```

3. `ls ./index.md` just outputted the argument itself. I think that it has this behavior because the only file a file contains is itself.

```bash
❯ ls ./index.md
./index.md
```

# cat

`cat` is short for concatenate. It concatenates the content of files onto the standard output.

1. `cat` without arguments freezes my terminal. I have to exit the command with ^C. This is an error, and I think it happens because `cat` was not designed to work without an input.

```bash
❯ cat
^C
```

2. `cat /Users/zoophere/Documents/Course\ Materials/WI24/CSE-15L/cse15l-lab-reports` prints out an error message saying that the argument is a directory. I think this happens because cat is only supposed to operate on files, not folders.

```bash
❯ cat /Users/zoophere/Documents/Course\ Materials/WI24/CSE-15L/cse15l-lab-reports
cat: /Users/zoophere/Documents/Course Materials/WI24/CSE-15L/cse15l-lab-reports: Is a directory
```

3. `cat ./test.md` (in the same working directory as the command above) puts the contents of `test.md` into the standard output.

```bash
❯ cat ./test.md
This is a test!
```
