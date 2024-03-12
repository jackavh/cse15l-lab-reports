# CSE 15L Lab 3

Jacob Hansen

PID: A18031849

# Part 1

There is a bug in `ArrayTests.reverseInPlace()`, the input array `{1, 2, 3}` produces the errored output `{1, 1, 1}`. Here is the code for `reverseInPlace()`:

```java
// ...ArrayExamples.java
static void reverseInPlace(int[] arr) {
    for (int i = 0; i < arr.length; i += 1) {
        arr[i] = arr[arr.length - i - 1];
    }
}
```

and the test `testReverseInPlace()`:

```java
// ...ArrayTests.java
@Test
 public void testReverseInPlace() {
   int[] input = { 1, 2, 3 };
   ArrayExamples.reverseInPlace(input);
   assertArrayEquals(new int[] { 3, 2, 1 }, input);

  // Output:
  // ...
  //   There was 1 failure:
  // 1) testReverseInPlace(ArrayTests)
  // arrays first differed at element [0]; expected:<3> but was:<1>
  // ...
 }
```

A test that doesn't induce a failure would be as follows:

```java
// ...ArrayTests.java
@Test
 public void testReverseInPlace() {
   int[] input = { 3 };
   ArrayExamples.reverseInPlace(input);
   assertArrayEquals(new int[] { 3 }, input);

   // Output:
   // ...
   // OK (2 tests)
   // ...
 }
```

The bug arises because `reverseInPlace()` doesn't assign a temporary variable when swapping the value for each index of the array in place. This causes each value to be overwritten instead of swapped. Another problem with the original code is that the for loop iterates over the entire array, when it only would need to loop over half of the array to swap each value. Here is code that works to reverse an array in place:

Before:

```java
// ...ArrayExamples.java
static void reverseInPlace(int[] arr) {
    for (int i = 0; i < arr.length; i += 1) {
        arr[i] = arr[arr.length - i - 1];
    }
}
```

After:

```java
// ...ArrayExamples.java
static void reverseInPlace(int[] arr) {
for (int i = 0; i < arr.length / 2; i += 1) {
    int temp = arr[i];
    arr[i] = arr[arr.length - i - 1];
    arr[arr.length - i - 1] = temp;
}
}
```

Running the JUnit tests:

```bash
❯ java -cp .:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar org.junit.runner.JUnitCore ArrayTests
JUnit version 4.13.2
..
Time: 0.012

OK (2 tests)
```

# Part 2

`find` is a command that can recursively search directories and files for different search queries. I found out in this exploration that `find` can also execute commands on the files it finds.

1. `find ~parameters~ -exec ~command~`

   executes `~command~` on each file or directory it finds. This can be useful for changing permissions on certain kinds of files. For example, if you had a lot of bash scripts that should be executed it would be efficient to do the following:

2. `find . -name '\*.sh' -exec chmod +x {} \;`

   searches the current working directory for scripts and allows them to be executed.

3. `find . -type f -exec cat {} \;`

4.

### From the `man` page:

```txt
The find utility recursively descends the directory tree for each path listed, evaluating an expression (composed of the “primaries” and “operands” listed below) in terms of each file in the tree.
```

### From ChatGPT

`Prompt:` Briefly explain the `find` command in zsh with four examples of useful commandline arguments.

`ChatGPT:` The find command in Unix-like operating systems, including when used in the Z shell (zsh), is a powerful tool for searching through the filesystem to find files and directories that match certain criteria. It allows users to specify complex search queries that include the file name, type, size, modification date, and more. The command syntax typically starts with find, followed by the path to search in, and various options and expressions that define the search criteria.

Here are four examples of useful command-line arguments for the find command, illustrating different search criteria:

5. Search by name:

- This example finds all files within the directory (and its subdirectories) that have a filename ending with .txt.

```sh
find /path/to/dir -name "*.txt"
```

6. Search by type:

- To find all directories (type d), can use:

```sh
find /path/to/dir -type d
```

7. Search by modification time:

- This command locates all files in /tmp that were modified more than 7 days ago.

```sh
find /path/to/dir -mtime +7
```

8. Execute a command on found files:

- For each .jpg file found in the directory, this command executes chmod 644 to change its permissions.

```sh
find /path/to/dir -type f -name "*.jpg" -exec chmod +x {} \;
```
