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
      String command = "sudo gtacl -c 'fup give jenktemp.*,0,0'";

      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'fup give reported '+result;
         println taskName+'result ignored';
         /* System.exit(result); */
      }
   }

   public void chgrp(File target) {
      String command = 'sudo fup secure jenktemp.*,"NUNU"'

      println taskName+command
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'fup secure reported '+result;
         println taskName+'result ignored';
         /* System.exit(result); */
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
      println taskName+' No need to clean up';
   }
}

