# Original Post

Hi, I'm working on my grader script and am running into a strange error. No matter which test repository I run it on, the script  never finds the `ListExamples.java` file. Here's what I get when running the script, and the contents of `grade.sh`:

![[Screenshot 2024-03-12 at 9.36.29 PM.png]]

![[Screenshot 2024-03-12 at 9.37.03 PM.png]]

![[Screenshot 2024-03-12 at 9.37.24 PM.png]]

And then here's `grade.sh`:

```bash
CPATH='.:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar'

cleanup() {
    echo "Cleaning up..."
    rm -rf student-submission
    rm -rf grading-area
}

cleanup
mkdir grading-area

git clone $1 student-submission
echo 'Finished cloning'
submission_dir="$(pwd /student-submission/ListExamples.java)"
score=""

if ! [ -f "${submission_dir}" ]; then
    echo "ListExamples.java not found!" 2>> submission-errors.txt
    score="-1"
    echo "score ${score}"
    cleanup
    exit 1
fi

echo 'Running tests'
# Set up grading area
cp ./TestListExamples.java ./grading-area
cp "${submission_dir}" ./grading-area
cp -R ./lib ./grading-area
cd ./grading-area

# Compile and run tests
javac -cp "${CPATH}" TestListExamples.java ListExamples.java 2>> submission-errors.txt

if [ $? -ne 0 ]; then
    echo "ListExamples.java failed to compile."
    cat submission-errors.txt
    score="${score}-1"
    echo "score ${score}"
    cleanup
    exit 1
fi

java -cp "${CPATH}" org.junit.runner.JUnitCore TestListExamples > submission-tests.txt
cat submission-tests.txt
cd .. # After running tests we are inside ./grading-area
cleanup
```

I know this is a long question, but I'm really stuck!

# Tutor Response

Hmmm, from your output it looks like the if block when `${submission_dir}` does not exist. It could help to add `echo "${submission_dir}"` to see how the path is being expanded.

# Student Response

Adding the `echo` showed me that it wasn't going to the `student-submission` directory. I think that putting `/student-submission` inside the `$(pwd)` part is what messed up the script. It works now!

![[Screenshot 2024-03-12 at 10.16.06 PM.png]]

# Setup Information

File Structure:

```bash
.
├── GradeServer.java
├── Server.java
├── TestListExamples.java
├── grade.sh
└── lib
    ├── hamcrest-core-1.3.jar
    └── junit-4.13.2.jar
```

Bug inducing lines in `grade.sh` before the fix. See the bottom of the page for all file contents.

```bash
 12 git clone $1 student-submission
 13 echo 'Finished cloning'
 14 submission_dir="$(pwd /student-submission/ListExamples.java)"
 15 score=""
```

Command that triggers the bug:

```bash
bash grade.sh <any test github repo>
```

To fix the bug, move `/student-submission/ListExamples.java` outside of the command substitution on line 14:

```bash
 14 submission_dir="$(pwd)/student-submission/ListExamples.java"
```

# Reflection

I have learned so much in this class it's hard to pick just one or two things. I have learned how to effectively use `vim` which has been a rewarding challenge. During labs, I learned a lot about how to effectively read other peoples code and communicate about programming. I learned about how to use `jdb` and found how effective it is at finding difficult to pin down bugs by stepping through one line at a time and inspecting different parts of the program using commands. Overall, I think I am leaving this class as a much more effective and efficient computer scientist.

# Complete File Contents from Part 1

`GradeServer.java`

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Stream;

class ExecHelpers {

  /**
    Takes an input stream, reads the full stream, and returns the result as a
    string.

    In Java 9 and later, new String(out.readAllBytes()) would be a better
    option, but using Java 8 for compatibility with ieng6.
  */
  static String streamToString(InputStream out) throws IOException {
    String result = "";
    while(true) {
      int c = out.read();
      if(c == -1) { break; }
      result += (char)c;
    }
    return result;
  }

  /**
    Takes a command, represented as an array of strings as it would by typed at
    the command line, runs it, and returns its combined stdout and stderr as a
    string.
  */
  static String exec(String[] cmd) throws IOException {
    Process p = new ProcessBuilder()
                    .command(Arrays.asList(cmd))
                    .redirectErrorStream(true)
                    .start();
    InputStream outputOfBash = p.getInputStream();
    return String.format("%s\n", streamToString(outputOfBash));
  }

}

class Handler implements URLHandler {
    public String handleRequest(URI url) throws IOException {
       if (url.getPath().equals("/grade")) {
           String[] parameters = url.getQuery().split("=");
           if (parameters[0].equals("repo")) {
               String[] cmd = {"bash", "grade.sh", parameters[1]};
               String result = ExecHelpers.exec(cmd);
               return result;
           }
           else {
               return "Couldn't find query parameter repo";
           }
       }
       else {
           return "Don't know how to handle that path!";
       }
    }
}

class GradeServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}

class ExecExamples {
  public static void main(String[] args) throws IOException {
    String[] cmd1 = {"ls", "lib"};
    System.out.println(ExecHelpers.exec(cmd1));

    String[] cmd2 = {"pwd"};
    System.out.println(ExecHelpers.exec(cmd2));

    String[] cmd3 = {"touch", "a-new-file.txt"};
    System.out.println(ExecHelpers.exec(cmd3));
  }
}
```

`Server.java`

```java
// A simple web server using Java's built-in HttpServer

// Examples from https://dzone.com/articles/simple-http-server-in-java were useful references

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

interface URLHandler {
    String handleRequest(URI url) throws IOException;
}

class ServerHttpHandler implements HttpHandler {
    URLHandler handler;
    ServerHttpHandler(URLHandler handler) {
      this.handler = handler;
    }
    public void handle(final HttpExchange exchange) throws IOException {
        // form return body after being handled by program
        try {
            String ret = handler.handleRequest(exchange.getRequestURI());
            // form the return string and write it on the browser
            exchange.sendResponseHeaders(200, ret.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(ret.getBytes());
            os.close();
        } catch(Exception e) {
            String response = e.toString();
            exchange.sendResponseHeaders(500, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

public class Server {
    public static void start(int port, URLHandler handler) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        //create request entrypoint
        server.createContext("/", new ServerHttpHandler(handler));

        //start the server
        server.start();
        System.out.println("Server Started! Visit http://localhost:" + port + " to visit.");
    }
}
```

`TestListExamples.java`

```java
import static org.junit.Assert.*;
import org.junit.*;
import java.util.Arrays;
import java.util.List;

class IsMoon implements StringChecker {
  public boolean checkString(String s) {
    return s.equalsIgnoreCase("moon");
  }
}

public class TestListExamples {
    @Test(timeout = 500)
    public void testFilterEmptyList() {
        List<String> empty = Arrays.asList();
        List<String> filtered = ListExamples.filter(empty, new IsMoon());
        assertTrue(filtered.isEmpty());
    }

    @Test(timeout = 500)
    public void testAllMatch() {
        List<String> list = Arrays.asList("moon", "moon");
        List<String> filtered = ListExamples.filter(list, new IsMoon());
        assertEquals(Arrays.asList("moon", "moon"), filtered);
    }

    @Test(timeout = 500)
    public void testNoneMatch() {
        List<String> list = Arrays.asList("sun", "earth");
        List<String> filtered = ListExamples.filter(list, new IsMoon());
        assertTrue(filtered.isEmpty());
    }

    @Test(timeout = 500)
    public void testMergeRightEnd() {
        List<String> left = Arrays.asList("a", "b", "c");
        List<String> right = Arrays.asList("a", "d");
        List<String> merged = ListExamples.merge(left, right);
        List<String> expected = Arrays.asList("a", "a", "b", "c", "d");
        assertEquals(expected, merged);
    }
}
```

`grade.sh` (after the fix)

```bash
CPATH='.:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar'

cleanup() {
    echo "Cleaning up..."
    rm -rf student-submission
    rm -rf grading-area
}

cleanup
mkdir grading-area

git clone $1 student-submission
echo 'Finished cloning'
submission_dir="$(pwd)/student-submission/ListExamples.java"
score=""

if ! [ -f "${submission_dir}" ]; then
    echo "ListExamples.java not found!" 2>> submission-errors.txt
    score="-1"
    echo "score ${score}"
    cleanup
    exit 1
fi

echo 'Running tests'
# Set up grading area
cp ./TestListExamples.java ./grading-area
cp "${submission_dir}" ./grading-area
cp -R ./lib ./grading-area
cd ./grading-area

# Compile and run tests
javac -cp "${CPATH}" TestListExamples.java ListExamples.java 2>> submission-errors.txt

if [ $? -ne 0 ]; then
    echo "ListExamples.java failed to compile."
    cat submission-errors.txt
    score="${score}-1"
    echo "score ${score}"
    cleanup
    exit 1
fi

java -cp "${CPATH}" org.junit.runner.JUnitCore TestListExamples > submission-tests.txt
cat submission-tests.txt
cd .. # After running tests we are inside ./grading-area
cleanup
```