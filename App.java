package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        // Using JGit for the git operations.
        Path repoPath = Paths.get("D:\\AWSConnectorProjects\\Github\\POC\\GitProject");
        Git git = Git.init().setDirectory(repoPath.toFile()).call();
        Files.writeString(repoPath.resolve("README.md"), "# test_repo");
        git.add().addFilepattern("README.md").call();
        git.commit().setMessage("first commit").call();
        RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish("https://github.com/USER/REPO.git"));
        remoteAddCommand.call();
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(
                new UsernamePasswordCredentialsProvider("username", "password"));
        pushCommand.add("master");
        pushCommand.setRemote("origin");
        pushCommand.call();

        // Using the github rest api for the github related operations.
        URL url = new URL("https://api.github.com/repos/USER/REPO/commits");
        String res = given().contentType("application/json")
                .given().header("Authorization", "token tokenvalue")
                .when()
                .get(url)
                .asString();

    }
}
