import hudson.model.*;
import java.io.*;
import java.sql.*;

class Repackage {
   File archive;
   String version;
   String compression;
   public Repackage(File archive, String version, String compression) {
      this.archive = archive;
      this.version = version;
      this.compression = compression;
   }

   public void decompress(File target) {
      String command = null;
      if (compression.equals("gz"))
         command = 'tar xzf ' + archive + ' -C ' + target.getAbsolutePath();
      if (!target.mkdir()) {
         println 'Unable to create directory '+target.getAbsolutePath();
         System.exit(1);
      }
      println command
      Process process = command.execute();
      int result = process.waitFor();
      println 'tar reported '+result;
      println process.text;
      if (result != 0)
         System.exit(result);
   }

   public void chown(File target) {
      String command = 'sudo chown -R super.super ' + 
         target.getAbsolutePath();

      println command
      Process process = command.execute();
      int result = process.waitFor();
      println process.text;
      if (result != 0) {
         println 'chmod reported '+result;
         System.exit(result);
      }
   }

   public void chgrp(File target) {
      String command = 'sudo chgrp -R SUPER ' + 
         target.getAbsolutePath();

      println command
      Process process = command.execute();
      int result = process.waitFor();
      println process.text;
      if (result != 0) {
         println 'chgrp reported '+result;
         System.exit(result);
      }
   }

   public void recompress(File source, File target) {
      String command = null;
      if (compression.equals("gz")) {
         command = "sh maketar.sh " + source.getAbsolutePath() + " " + target.getAbsolutePath();
      }
      Process process = command.execute();
      int result = process.waitFor();
      println process.text;
      println 'maketar.sh reported '+result;
      if (result != 0) {
         println 'chmod reported '+result;
         System.exit(result);
      }
   }

   public void clean(File target) {
      String command = 'rm -rf ' +
         target.getAbsolutePath();

      println command
      Process process = command.execute();
      int result = process.waitFor();
      println process.text;
      if (result != 0) {
         println 'rm reported '+result;
         System.exit(result);
      }
   }
}

