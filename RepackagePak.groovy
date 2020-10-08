import hudson.model.*;
import java.io.*;
import java.sql.*;

class RepackagePak extends AbstractRepackage {

   public RepackagePak(File archive, String version, String compression) {
      this.archive = archive;
      this.version = version;
      this.compression = compression;
   }

   public void decompress(File target) {
      String command = null;
      if (compression.equals("bin"))
         command = 'sh unpak.sh ' + archive;
      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'unpak reported '+result;
         System.exit(result);
      }
   }

   public void chown(File target) {
      String command = "sh give.sh SUPER.SUPER";

      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'give reported '+result;
         System.exit(result);
      }
   }

   public void chgrp(File target) {
      String command = "sh secure.sh NCNC";

      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'secure reported '+result;
         System.exit(result);
      }
   }

   public void recompress(File source, File target) {
      String command = null;
      command = "sh pak.sh "+target.getAbsolutePath();
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'pak reported '+result;
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
}

