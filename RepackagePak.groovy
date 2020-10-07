import hudson.model.*;
import java.io.*;
import java.sql.*;

class Repackage extends AbstractRepackage {

   public Repackage(File archive, String version, String compression) {
      super(archive, version, compression);
   }

   public void decompress(File target) {
      println taskName+' Nothing to do with '+target.getAbsolutePath();
   }

   public void chown(File target) {
      println taskName+' Not using chown';
   }

   public void chgrp(File target) {
      println taskName+' Not using chgrp';
   }

   public void recompress(File source, File target) {
      String command = null;
      if (compression.equals("gz")) {
         command = "mv " + source.getAbsolutePath() + " " + target.getAbsolutePath();
      }
      Process process = command.execute();
      int result = process.waitFor();
      println taskName+process.text;
      if (result != 0) {
         println taskName+'mv reported '+result;
         System.exit(result);
      }
   }

   public void clean(File target) {
      println taskName+' No need to clean up';
   }
}

