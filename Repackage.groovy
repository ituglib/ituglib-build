import hudson.model.*;
import java.io.*;
import java.sql.*;

class Repackage {
   String taskName = "[" + this.getClass().getSimpleName() + "] ";
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
      else if (compression.equals("bz2"))
         command = 'tar xjf ' + archive + ' -C ' + target.getAbsolutePath();
      if (!target.mkdir()) {
         println taskName+'Unable to create directory '+target.getAbsolutePath();
         System.exit(1);
      }
      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'tar reported '+result;
         System.exit(result);
      }
   }

   public void chown(File target) {
      String command = 'sudo chown -R super.super ' + 
         target.getAbsolutePath();

      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'chmod reported '+result;
         System.exit(result);
      }
   }

   public void chgrp(File target) {
      String command = 'sudo chgrp -R SUPER ' + 
         target.getAbsolutePath();

      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'chgrp reported '+result;
         System.exit(result);
      }
   }

   public void recompress(File source, File target) {
      String command = null;
      if (compression.equals("gz")) {
         command = "sh maketar.sh " + source.getAbsolutePath() + " " + target.getAbsolutePath();
      } else if (compression.equals("bz2")) {
         command = "sh maketar.sh " + source.getAbsolutePath() + " " + target.getAbsolutePath();
      }
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'maketar.sh reported '+result;
         System.exit(result);
      }
   }

   public void clean(File target) {
      String command = 'sudo rm -rf ' +
         target.getAbsolutePath();

      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'rm reported '+result;
         System.exit(result);
      }
   }

   public void setCompression(String value) {
      this.compression = value;
   }
}

